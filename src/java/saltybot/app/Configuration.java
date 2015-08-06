package saltybot.app;

public class Configuration {

    private static Configuration instance;

    public static Configuration instance() {
        return instance;
    }

    private final String username;
    private final String password;

    private Configuration(final String username,
                          final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    protected static class ConfigurationBuilder {

        private String username;
        private String password;

        public ConfigurationBuilder() {
        }

        public ConfigurationBuilder setUsername(final String username) {
            this.username = username;
            return this;
        }

        public ConfigurationBuilder setPassword(final String password) {
            this.password = password;
            return this;
        }

        public Configuration build() {
            return new Configuration(username, password);
        }

        public Configuration buildAndMakeGlobal() {
            return instance = build();
        }
    }
}
