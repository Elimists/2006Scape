package com.rs2.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

public class ConfigLoaderV2 {

    private static final LoaderOptions loaderOptions = createLoaderOptions();

    private static LoaderOptions createLoaderOptions() {
        return new LoaderOptions();
    }

    private static <T> T loadConfig(String fileName, Class<T> configClass) throws IOException {
        Constructor constructor = new Constructor(configClass, loaderOptions);
        Yaml yaml = new Yaml(constructor);

        InputStream inputStream = ConfigLoaderV2.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IOException(fileName + " not found");
        }

        try {
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new IOException("Failed to load " + fileName + ": " + e.getMessage(), e);
        }
    }

    public static ServerConfig loadServerConfig() throws IOException {
        return loadConfig("server_config.yaml", ServerConfig.class);
    }

    public static GameConfig loadGameConfig() throws IOException {
        return loadConfig("game_config.yaml", GameConfig.class);
    }

}
