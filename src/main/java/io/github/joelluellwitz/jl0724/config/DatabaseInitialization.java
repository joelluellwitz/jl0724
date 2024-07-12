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

import io.github.joelluellwitz.jl0724.RetailConsole;

/**
 * Creates a persisted database instance in the user's home directory and sets the spring.datasource.url property.
 *   The database is removed at program termination.
 *
 * Note: I could have used the H2 in-memory database here instead, but I wanted a more authentic persistent data tier
 *   for the non-unit testing code. This way I can demonstrate how to swap out a database implementation for use with
 *   testing (where I do use the H2 in-memory database). This class is not unit tested as it would not exist outside of
 *   a demo environment and is (somewhat deceptively) difficult mock. Normally one would define spring.datasource.url in
 *   application.properties and the value would reference a permanent database URL.
 */
public class DatabaseInitialization implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    /**
     * @see DatabaseInitialization.
     */
    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        try {
            final File tempDatabase = File.createTempFile("pointOfSale-", ".sqlite3");
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
            throw new RuntimeException("Cannot setup the persistent database.", e);
        }
    }
}