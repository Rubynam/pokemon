package pokemon.common.service;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import pokemon.api.dex.PokeDexServer;
import pokemon.common.MicroServiceVertx;

public class PokedexService extends MicroServiceVertx {

  @Override
  public void start() throws Exception {
    super.start();

    JsonObject configObj = config();
    initService(configObj);
  }

  private void initService(JsonObject configObj) {
    deployService(configObj);
  }

  private void deployService(JsonObject configObj) {
    String className = PokeDexServer.class.getName();
    DeploymentOptions options = new DeploymentOptions()
      .setConfig(configObj);
    vertx.deployVerticle(className, options);
  }
}
