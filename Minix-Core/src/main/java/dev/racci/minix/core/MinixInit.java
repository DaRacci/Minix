package dev.racci.minix.core;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.builder.InjectingApplicationBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class MinixInit extends JavaPlugin {

    {
        ClassLoader libraryLoader;
        try {
            var field = PluginClassLoader.class.getDeclaredField("libraryLoader");
            field.setAccessible(true);
            libraryLoader = (ClassLoader) field.get(getClassLoader());
        } catch(NoSuchFieldException e) {
            getLogger().severe("Failed to find field 'libraryLoader' in class 'PluginClassLoader', unsupported version?");
            throw new RuntimeException(e);
        } catch(IllegalAccessException e) {
            getLogger().severe("Failed to access field 'libraryLoader' in class 'PluginClassLoader', unsupported version?");
            throw new RuntimeException(e);
        } catch(ClassCastException e) {
            getLogger().severe("Failed to cast field 'libraryLoader' as 'ClassLoader' in class 'PluginClassLoader', unsupported version?");
            throw new RuntimeException(e);
        }

        ApplicationBuilder builder;
        try {
            builder = InjectingApplicationBuilder.createAppending("Minix", libraryLoader);
        } catch(ReflectiveOperationException | IOException | URISyntaxException | NoSuchAlgorithmException e) {
            getLogger().severe("Failed to create application builder.");
            throw new RuntimeException(e);
        }

        var folder = Path.of(String.format("%s/libraries", getDataFolder()));
        if(!Files.exists(folder)) folder.toFile().mkdirs();
        builder.downloadDirectoryPath(folder);

        builder.logger((m, anies) -> {
            var logger = getLogger();
            logger.info(m.formatted(anies));
        });

        try {
            builder.build();
        } catch(IOException | ReflectiveOperationException | URISyntaxException | NoSuchAlgorithmException e) {
            getLogger().severe("Failed to build application.");
            throw new RuntimeException(e);
        }

        new DummyLoader().loadPlugin(getDescription(), this, (JavaPluginLoader) getPluginLoader(), (PluginClassLoader) getClassLoader());
    }
}
