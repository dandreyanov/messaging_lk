package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:credentials.properties"
})

public interface CredentialsConfig extends Config {
    @Key("username")
    String username();

    @Key("password")
    String password();
}
