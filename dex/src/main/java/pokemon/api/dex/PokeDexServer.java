package pokemon.api.dex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pokemon.api.dex.config.Config;
import pokemon.api.dex.config.OrderConfig;
import pokemon.api.dex.constant.Constant;
import pokemon.api.dex.router.Index;

public class PokeDexServer extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    super.start();
    //this.vertx.setTimer(2000,(event)->{});
    createServer();
  }

  private void createServer() {
    this.vertx.setTimer(2000, (event) -> {
      Config config = new Config(OrderConfig.getConfig(Constant.ENV_CONFIG));
      HttpServerOptions options = config.getHttpOptions();
      int port = config.getHttpOptions().getPort();

      Router router = initRouter();

      vertx.createHttpServer(options)
        .requestHandler(router)
        .listen(port, this::handleListener);
    });

  }

  private void handleListener(AsyncResult<HttpServer> asyncResult) {
    if (asyncResult.succeeded()) {
      System.out.println("Server started");
    } else {
      System.out.println("Cannot start server: " + asyncResult.cause());
    }
  }

  private Router initRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/pokemon/:name").handler(Index::handleGetPokemonByName);
    return router;
  }
}
