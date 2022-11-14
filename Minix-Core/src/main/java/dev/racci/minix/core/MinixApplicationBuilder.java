package dev.racci.minix.core;

import dev.racci.minix.api.annotations.MinixInternal;
import dev.racci.minix.core.loader.MinixGsonDependencyDataProviderFactory;
import dev.racci.minix.core.loader.MinixDependencyVerifierFactory;
import dev.racci.minix.core.loader.MinixExternalDependencyDataProviderFactory;
import dev.racci.minix.core.loader.MinixPreResolutionDataProviderFactory;
import io.github.slimjar.app.Application;
import io.github.slimjar.app.builder.InjectingApplicationBuilder;
import io.github.slimjar.logging.ProcessLogger;
import io.github.slimjar.relocation.JarFileRelocatorFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.reader.dependency.GsonDependencyReader;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipInputStream;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class MinixApplicationBuilder {
    private static Path downloadFolder = null;
    private static ClassLoader classLoader = null;
    private static Set<FilePair> initialised = null;
    private static JarFileRelocatorFactory relocatorFactory = null;
    @MinixInternal
    static ProcessLogger logger = null;

    public static void init(
        final Path downloadFolder,
        final ClassLoader classLoader,
        final Logger slf4Logger
    ) throws ReflectiveOperationException, IOException, NoSuchAlgorithmException, URISyntaxException, InterruptedException {
        MinixApplicationBuilder.downloadFolder = downloadFolder;
        MinixApplicationBuilder.classLoader = classLoader;
        MinixApplicationBuilder.initialised = new HashSet<>();
        MinixApplicationBuilder.logger = (s, objects) -> {
            if (s.contains("Checksum") || s.contains("checksum")) return; // Only log download progress.
            slf4Logger.info(s.formatted(objects));
        };

        final var jarRelocatorFacadeFactory = ReflectiveJarRelocatorFacadeFactory.create(downloadFolder, Collections.singleton(Repository.central()));
        MinixApplicationBuilder.relocatorFactory = new JarFileRelocatorFactory(jarRelocatorFacadeFactory);

        final var facadeFactory = ReflectiveGsonFacadeFactory.create(downloadFolder, Collections.singleton(Repository.central()));
        final var gson = facadeFactory.createFacade();
        final var gsonReader = new GsonDependencyReader(gson);

        MinixDependencyVerifierFactory.init(downloadFolder.toFile());
        MinixPreResolutionDataProviderFactory.init(gson);

        MinixGsonDependencyDataProviderFactory.init(gsonReader);
        MinixExternalDependencyDataProviderFactory.init(gsonReader);
    }

    private MinixApplicationBuilder() {
    }

    public static Application createApplication(final Plugin plugin) {
        final var pluginName = plugin.getName();
        final var urlPair = findInput(plugin);

        logger.debug("Plugin name: %s", pluginName);
        logger.debug("PreResFile: %s", (urlPair.preRes != null ? urlPair.preRes.getFile() : null));
        logger.debug("DepFile: %s", (urlPair.deps != null ? urlPair.deps.getFile() : null));

        if (!urlPair.hasFiles()) {
            logger.debug("Failed to find input for %s", pluginName);
            return null;
        }

        if (MinixApplicationBuilder.initialised.stream().anyMatch(urlPair::equals)) {
            logger.debug("Plugin %s already initialised.", plugin.getName());
            return null;
        }

        try {
            final var classLoaderMethod = JavaPlugin.class.getDeclaredMethod("getClassLoader");
            classLoaderMethod.setAccessible(true);
            final var classLoader = (ClassLoader) classLoaderMethod.invoke(plugin);
            classLoaderMethod.setAccessible(false);

            final var app = InjectingApplicationBuilder.createAppending(pluginName, classLoader)
                .downloadDirectoryPath(downloadFolder)
                .preResolutionFileUrl(urlPair.preRes)
                .dependencyFileUrl(urlPair.deps)
                .preResolutionDataProviderFactory(MinixPreResolutionDataProviderFactory::create)
                .moduleDataProviderFactory(MinixExternalDependencyDataProviderFactory::create)
//                .dataProviderFactory(MinixGsonDependencyDataProviderFactory::create)
                .verifierFactory(MinixDependencyVerifierFactory::create)
                .relocatorFactory(relocatorFactory)
                .logger(logger)
                .build();

            if (app != null) MinixApplicationBuilder.initialised.add(urlPair);
            return app;
        } catch (Exception err) {
            throw new RuntimeException("Failed to create application for " + pluginName, err);
        }
    }

    // For some reason, using just class.getResource() doesn't work
    private static FilePair findInput(final Plugin plugin) {
        URL preResFile = null;
        URL depFile = null;

        final var src = plugin.getClass().getProtectionDomain().getCodeSource();
        if (src == null) return new FilePair(null, null);

        final var jar = src.getLocation();
        try (final var zip = new ZipInputStream(jar.openStream())) {
            while (preResFile == null || depFile == null) {
                final var entry = zip.getNextEntry();
                if (entry == null) break;
                final var name = entry.getName();

                if (name.equals("slimjar-resolutions.json")) {
                    preResFile = new URL("jar:" + jar + "!/" + name);
                }

                if (name.equals("slimjar.json")) {
                    depFile = new URL("jar:" + jar + "!/" + name);
                }
            }
        } catch (IOException e) {
            logger.log("Failed to find slimjar files in jar: {0}", e.getMessage());
            return new FilePair(null, null);
        }

        return new FilePair(preResFile, depFile);
    }

    record FilePair(
        @Nullable URL preRes,
        @Nullable URL deps
    ) {
        public boolean hasFiles() {
            return deps != null;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FilePair other)) return false;
            return Objects.equals(other.preRes, preRes) && Objects.equals(other.deps, deps);
        }
    }
}
