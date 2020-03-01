package pokemon.common.cofig.impl;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import pokemon.common.cofig.ConfigSource;
import pokemon.common.enumeration.ConfigEnum;

public class EnvVarStoreSource implements ConfigSource {

    @Override
    public ConfigStoreOptions getConfigSource() {
        ConfigStoreOptions config = new ConfigStoreOptions();
        config.setType(ConfigEnum.FILE_CONFIG.getType());
        config.setFormat(ConfigEnum.PROPERTIES.getType());
        config.setConfig(new JsonObject().put(ConfigEnum.PATH.getType(), ConfigEnum.CONF.getType()));
        return config;
    }
}
