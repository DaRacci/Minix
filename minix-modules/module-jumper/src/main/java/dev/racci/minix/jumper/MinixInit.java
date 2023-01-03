package dev.racci.minix.jumper;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

/**
 * Creating a dummy plugin without any kotlin methods, so we can dynamically load the libraries.
 */
// FIXME -> Some plugins try hook into this using reflection and fail to register listeners since this never gets enabled.
public final class MinixInit extends JavaPlugin {

    public MinixInit() {
        final var downloadFolder = Path.of(String.format("%s/libraries", getDataFolder()));

        if (!Files.exists(downloadFolder) && !downloadFolder.toFile().mkdirs()) {
            this.getLogger().log(Level.SEVERE, "Failed to create download folder at {0}!", downloadFolder.toAbsolutePath());
            JumperUtils.errorInit(null, this);
            return;
        }

        try {
            MinixApplicationBuilder.init(downloadFolder, this.getClassLoader(), this.getSLF4JLogger());
            MinixApplicationBuilder.createApplication(this);
        } catch (final MinixApplicationException err) {
            JumperUtils.errorInit(err, this);
            return;
        }

        try {
            Class.forName("dev.racci.minix.core.plugin.DummyLoader", true, this.getClassLoader()).getConstructor(
                PluginDescriptionFile.class,
                MinixInit.class,
                PluginClassLoader.class
            ).newInstance(this.getDescription(), this, (PluginClassLoader) this.getClassLoader());
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException err) {
            JumperUtils.errorInit(err, this);
        }
    }
}
