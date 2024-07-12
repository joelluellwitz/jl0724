/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * TODO: Document.
 */
// TODO: Remove class. This isn't going to work.
public class DatabaseInitializationTests {
    
    // TODO: @SuppressWarnings("resource")
    //@Test
    public void onApplicationEventSucceeds() throws IOException {
        final String fakePathname = "Fake pathname.";
        
        final File mockedFile = mock(File.class);
        when(mockedFile.getAbsolutePath()).thenReturn(fakePathname);
        
        final MockedStatic<File> mockedStaticFile = mockStatic(File.class);
        mockedStaticFile.when(() -> File.createTempFile("pointOfSale-", ".sqlite3")).thenReturn(mockedFile);

        final MockedStatic<Files> mockedStaticFiles = mockStatic(Files.class);
        mockedStaticFiles.when(() -> Files.copy(
                any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING))).thenAnswer(
                        (Answer<Void>) invocation -> null);

        final MutablePropertySources mockedPropertySources = mock(MutablePropertySources.class);

        final ConfigurableEnvironment mockedEnvironment = mock(ConfigurableEnvironment.class);
        when(mockedEnvironment.getPropertySources()).thenReturn(mockedPropertySources);
        
        final ApplicationEnvironmentPreparedEvent mockedEvent = mock(ApplicationEnvironmentPreparedEvent.class);
        when(mockedEvent.getEnvironment()).thenReturn(mockedEnvironment);
        
        new DatabaseInitialization().onApplicationEvent(mockedEvent);
        
        final ArgumentCaptor<PropertiesPropertySource> propertiesPropertySourceCaptor =
                ArgumentCaptor.forClass(PropertiesPropertySource.class);
        verify(mockedPropertySources).addFirst(propertiesPropertySourceCaptor.capture());
        
        final Properties properties = new Properties();
        properties.put("spring.datasource.url", "jdbc:sqlite:" + fakePathname);
        assertThat(propertiesPropertySourceCaptor.getValue()).isEqualTo(properties);
    }

    //@Test
    public void onApplicationEventFails() {
        
    }
}