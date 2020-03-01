package pokemon.api.dex.config;

import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

public final class OrderConfig {

  private static final Map<String, JsonObject> config =  new HashMap<>();

  public static synchronized void push(String key, JsonObject object){
    config.put(key, object);
  }

  public static synchronized JsonObject getConfig(String key){
    return config.get(key);
  }
}
