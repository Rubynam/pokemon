package pokemon.common;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import pokemon.api.dex.config.OrderConfig;
import pokemon.api.dex.constant.Constant;
import pokemon.common.cofig.ConfigSource;
import pokemon.common.cofig.impl.EnvVarStoreSource;
import pokemon.common.enumeration.ConfigEnum;

public class PokeLaucher extends Launcher {

  private static final Logger logger = LoggerFactory.getLogger(PokeLaucher.class);
  private ConfigRetrieverOptions configRetriever;

  public static void main(String[] args) {
    String[] runService = "run pokemon.common.service.PokedexService".split(" ");
    PokeLaucher pokemon = new PokeLaucher();
    pokemon.dispatch(runService);
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions){
    super.beforeDeployingVerticle(deploymentOptions);
    initConfig();
    if (isNotConfig(deploymentOptions)) {
      logger.error("Can not read config");
    } else {

      ConfigRetriever retriever = ConfigRetriever.create(Vertx.vertx(), this.configRetriever);
      retriever.getConfig(reader -> {
        if (reader.failed()) {
          logger.info("beforeDeploying exception {}", reader.cause());
        } else {
          logger.info("info config before deploy %s", reader.result());
          try {
            readerConfigEnv(reader.result(), deploymentOptions);
          } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException exception {}", e.getMessage());
          }
        }
      });
    }
  }

  private void initConfig() {
    ConfigSource configSource = new EnvVarStoreSource();

    this.configRetriever = new ConfigRetrieverOptions()
      .addStore(configSource.getConfigSource());
  }

  private void readerConfigEnv(JsonObject result, DeploymentOptions options)
    throws FileNotFoundException {
    pushConfig2Cache(Constant.APPLICATION_CONFIG,result);
    //load config
    String path = result.getString(ConfigEnum.CONFIG_PATH.getType());
    File file = new File(path);
    JsonObject object = getConfig(file);
    pushConfig2Cache(Constant.ENV_CONFIG,object);
    options.getConfig().mergeIn(object);
  }

  private void pushConfig2Cache(String key,JsonObject result) {
    OrderConfig.push(key,result);
  }

  private boolean isNotConfig(DeploymentOptions deploymentOptions) {
    return deploymentOptions.getConfig() == null;
  }

  private JsonObject getConfig(File config) throws FileNotFoundException {
    if (!config.isFile()) {
      return new JsonObject();
    }

    Scanner scanner = new Scanner(config).useDelimiter("\\A");
    String confStr = scanner.next();

    return new JsonObject(confStr);
  }
}
