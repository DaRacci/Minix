package dev.racci.minix.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;


/**
 * Creating a dummy plugin without any kotlin methods, so we can dynamically load the libraries.
 */
@SuppressWarnings({"java:S1171"}) // We need a non-static init.
public class MinixInit extends JavaPlugin {

    {
        final var downloadFolder = Path.of(String.format("%s/libraries", getDataFolder()));

        if (!Files.exists(downloadFolder) && !downloadFolder.toFile().mkdirs()) {
            this.getLogger().log(Level.SEVERE, "Failed to create download folder at {0}!", downloadFolder.toAbsolutePath());
            throw new ReflectingInitializationException(null);
        }

        try {
            MinixApplicationBuilder.init(downloadFolder, this.getClassLoader(), this.getSLF4JLogger());
        } catch (ReflectiveOperationException | IOException | NoSuchAlgorithmException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        MinixApplicationBuilder.createApplication(this);
        new DummyLoader().loadPlugin(this.getDescription(), this, (PluginClassLoader) this.getClassLoader());
    }
}
