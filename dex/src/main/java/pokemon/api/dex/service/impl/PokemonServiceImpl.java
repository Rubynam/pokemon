package pokemon.api.dex.service.impl;


import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import pokemon.api.dex.constant.Constant;
import pokemon.api.dex.service.PokemonService;
import pokemon.api.dex.utils.WebClientUtils;

public class PokemonServiceImpl implements PokemonService {

  private static final PokemonServiceImpl instance = new PokemonServiceImpl();
  private static final Logger logger = LoggerFactory.getLogger(PokemonServiceImpl.class);

  public static PokemonService getInstance() {
    return instance;
  }

  @Override
  public void getPokemonByName(String name, RoutingContext routingContext) {
    WebClientUtils.getPokemonByName(Constant.PATH_API, name, routingContext.vertx())
      .putHeader("Accept", "application/json")
      .putHeader("Content-Type", "application/json").send(response -> {
      if (response.failed()) {
        logger.error(response.cause());
        routingContext.response()
          .end(new JsonObject().put("error", response.cause().getMessage()).encode());
      } else {
        logger.info(response.result().bodyAsString());
        routingContext.response().end(response.result().bodyAsString());
      }
    });
  }

}
