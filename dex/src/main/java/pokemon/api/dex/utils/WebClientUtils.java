package pokemon.api.dex.utils;

import static pokemon.api.dex.constant.Constant.POKEMON_HOST;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public final class WebClientUtils {

  private static final Logger logger = LoggerFactory.getLogger(WebClientUtils.class);
  private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

  public WebClientUtils(){
  }

  public static HttpRequest<Buffer> getPokemonByName(String uri,String name, Vertx vertx){
    WebClientOptions options = new WebClientOptions().setUserAgent(userAgent);
    WebClient client = WebClient.create(vertx,options);

    return client
      .get(POKEMON_HOST,uri+"/"+name)
      .timeout(10000)
      .putHeader("Accept", "application/json")
      .putHeader("Content-Type", "application/json");
  }
}
