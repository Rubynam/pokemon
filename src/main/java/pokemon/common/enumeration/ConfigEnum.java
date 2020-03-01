package pokemon.common.enumeration;

public enum ConfigEnum {
    CONFIG_PATH("config.path"),
    ENV("env"),
    FILE_CONFIG("file"),
    PROPERTIES("properties"),
    PATH("path"),
    CONF("src/main/resources/application.properties");

    private String type;

    ConfigEnum(String message) {
        this.type = message;
    }

    public String getType() {
        return type;
    }

}