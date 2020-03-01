package pokemon.api.dex.service;

import io.vertx.ext.web.RoutingContext;

public interface PokemonService {

  void getPokemonByName(String name, RoutingContext routingContext);
}
