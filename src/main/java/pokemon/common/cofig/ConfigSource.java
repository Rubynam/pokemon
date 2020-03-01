package pokemon.common.cofig;

import io.vertx.config.ConfigStoreOptions;

public interface ConfigSource {
    ConfigStoreOptions getConfigSource();
}
