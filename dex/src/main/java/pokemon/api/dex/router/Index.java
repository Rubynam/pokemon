package pokemon.api.dex.router;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pokemon.api.dex.service.PokemonService;
import pokemon.api.dex.service.impl.PokemonServiceImpl;

public class Index {

  private static final PokemonService pokemonService = PokemonServiceImpl.getInstance();
  private static final Logger logger = LoggerFactory.getLogger(Index.class);

  public static void handleGetPokemonByName(RoutingContext routingContext) {
    String name = routingContext.request().getParam("name");

    final HttpServerResponse response = routingContext.response();
    if (StringUtils.isBlank(name)) {
      logger.warn("params can't is blank {}", name);
      response.write("error");
    } else {
      pokemonService.getPokemonByName(name, routingContext);
    }
  }
}
