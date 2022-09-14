package dev.racci.minix.core;

import io.github.slimjar.app.builder.InjectingApplicationBuilder;
import io.github.slimjar.logging.ProcessLogger;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;


/**
 * Creating a dummy plugin without any kotlin methods, so we can dynamically load the libraries.
 */
@SuppressWarnings({"java:S1171"}) // We need a non-static init.
public class MinixInit extends JavaPlugin {

    {
        final var downloadFolder = Path.of(String.format("%s/libraries", getDataFolder()));
        final var logger = new ProcessLogger() {
            @Override
            public void log(String s, Object... objects) {
                getSLF4JLogger().info(s.formatted(objects));
            }

            @Override
            public void debug(String message, Object... args) {
                getSLF4JLogger().info(message.formatted(args));
            }
        };

        if (!Files.exists(downloadFolder) && !downloadFolder.toFile().mkdirs()) {
            this.getLogger().log(Level.SEVERE, "Failed to create download folder at {0}!", downloadFolder.toAbsolutePath());
            throw new ReflectingInitializationException(null);
        }

        try {
            InjectingApplicationBuilder.createAppending("Minix", this.getClassLoader())
                .downloadDirectoryPath(downloadFolder)
                .logger(logger)
                .build();
        } catch(IOException | ReflectiveOperationException | URISyntaxException | NoSuchAlgorithmException | InterruptedException e) {
            this.getLogger().severe("Failed to build application.");
            throw new ReflectingInitializationException(e);
        }

        new DummyLoader().loadPlugin(this.getDescription(), this, (PluginClassLoader) this.getClassLoader());
    }
}
