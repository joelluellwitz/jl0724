/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import io.github.joelluellwitz.jl0724.exposed.ui.console.RetailConsole;

/**
 * TODO: Document.
 */
public class DatabaseInitialization implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    /**
     * TODO: Document.
     */
    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        try {
            final File tempDatabase = File.createTempFile("pointOfSale-", ".tmp");
            tempDatabase.deleteOnExit();
            final URL sourceDatabase = RetailConsole.class.getResource(
                    "/io/github/joelluellwitz/jl0724/internal/data/sqlite/pointOfSale.sqlite3");
            Files.copy(sourceDatabase.openStream(), tempDatabase.toPath(), StandardCopyOption.REPLACE_EXISTING);

            final ConfigurableEnvironment environment = event.getEnvironment();
            final Properties properties = new Properties();
            properties.put("spring.datasource.url", "jdbc:sqlite:" + tempDatabase.getAbsolutePath());
            environment.getPropertySources().addFirst(new PropertiesPropertySource("myProps", properties));
        }
        catch (final IOException e) {
            // Any failure above should crash the application.
            throw new RuntimeException(e);
        }
    }
}
