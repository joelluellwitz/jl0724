/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import org.hibernate.cfg.Configuration;

/**
 * TODO: Document.
 */
public class Hibernate {
    static {
        try {
            // Copy the database to a temporary file.
            final File tempDatabase = File.createTempFile("pointOfSale-", ".tmp");
            tempDatabase.deleteOnExit();
            final URL sourceDatabase = Hibernate.class.getResource(
                    "/src/main/resources/io/github/joelluellwitz/jl0624/internal/dao/sqlite/pointOfSale.sqlite3");
            Files.copy(sourceDatabase.openStream(), tempDatabase.toPath());

            Configuration config = new Configuration();
            //config.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
            //config.setProperty("hibernate.connection.url", "jdbc:sqlite:" + tempDatabase.getAbsolutePath());
            // TODO: config.setProperty("hibernate.connection.username", "root");
            // TODO: config.setProperty("hibernate.connection.password", "password");
            config.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        }
        catch(final Exception e) {
            // I want the program to fail on any exception.
            throw new RuntimeException(e);
        }
    }
}