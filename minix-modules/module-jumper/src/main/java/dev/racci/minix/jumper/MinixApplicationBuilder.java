package dev.racci.minix.jumper;

import dev.racci.minix.jumper.loader.MinixGsonDependencyDataProviderFactory;
import dev.racci.minix.jumper.loader.MinixDependencyVerifierFactory;
import dev.racci.minix.jumper.loader.MinixExternalDependencyDataProviderFactory;
import dev.racci.minix.jumper.loader.MinixPreResolutionDataProviderFactory;
import io.github.slimjar.app.builder.InjectingApplicationBuilder;
import io.github.slimjar.logging.ProcessLogger;
import io.github.slimjar.relocation.JarFileRelocatorFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.reader.dependency.GsonDependencyReader;

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

import io.github.slimjar.resolver.reader.facade.GsonFacade;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import org.apiguardian.api.API;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@SuppressWarnings("java:S2142") // Interrupt exceptions are handled by the caller.
public final class MinixApplicationBuilder {
    private static Path downloadFolder = null;
    private static ClassLoader classLoader = null;
    private static Set<FilePair> initialised = null;
    private static JarFileRelocatorFactory relocatorFactory = null;

    @API(status = API.Status.INTERNAL)
    @SuppressWarnings("java:S1444") // Updated by Minix
    public static ProcessLogger logger = null;

    private MinixApplicationBuilder() {}

    public static void init(
        @NotNull final Path downloadFolder,
        @NotNull final ClassLoader classLoader,
        @NotNull final Logger slf4Logger
    ) throws MinixApplicationException {
        MinixApplicationBuilder.downloadFolder = downloadFolder;
        MinixApplicationBuilder.classLoader = classLoader;
        MinixApplicationBuilder.initialised = new HashSet<>();
        MinixApplicationBuilder.logger = (s, objects) -> {
            if (s.contains("Checksum") || s.contains("checksum")) return; // Only log download progress.
            slf4Logger.info(s.formatted(objects));
        };

        final GsonFacade gsonFacade;
        final GsonDependencyReader gsonReader;
        try {
            final var jarRelocatorFacadeFactory = ReflectiveJarRelocatorFacadeFactory.create(downloadFolder, Collections.singleton(Repository.central()));
            MinixApplicationBuilder.relocatorFactory = new JarFileRelocatorFactory(jarRelocatorFacadeFactory);

            final var facadeFactory = ReflectiveGsonFacadeFactory.create(downloadFolder, Collections.singleton(Repository.central()));
            gsonFacade = facadeFactory.createFacade();
            gsonReader = new GsonDependencyReader(gsonFacade);
        } catch (final IOException | NoSuchAlgorithmException | URISyntaxException | ReflectiveOperationException | InterruptedException err) {
            throw new MinixApplicationException(err);
        }

        MinixDependencyVerifierFactory.init(downloadFolder.toFile());
        MinixPreResolutionDataProviderFactory.init(gsonFacade);

        MinixGsonDependencyDataProviderFactory.init(gsonReader);
        MinixExternalDependencyDataProviderFactory.init(gsonReader);
    }

    @Contract(pure = true)
    @SuppressWarnings("java:S3011") // We need reflection to access to the plugin's classloader.
    public static void createApplication(@NotNull final Plugin plugin) throws MinixApplicationException {
        final var pluginName = plugin.getName();
        final var urlPair = findInput(plugin);

        logger.debug("Plugin name: %s", pluginName);
        logger.debug("PreResFile: %s", (urlPair.preRes != null ? urlPair.preRes.getFile() : null));
        logger.debug("DepFile: %s", (urlPair.deps != null ? urlPair.deps.getFile() : null));

        if (!urlPair.hasFiles()) {
            logger.debug("Failed to find input for %s", pluginName);
            throw new MinixApplicationException("Failed to find input for " + pluginName);
        }

        if (MinixApplicationBuilder.initialised.stream().anyMatch(urlPair::equals)) {
            logger.debug("Plugin %s already initialised.", plugin.getName());
            throw new MinixApplicationException("Plugin " + plugin.getName() + " already initialised.");
        }

        try {
            final var classLoaderMethod = JavaPlugin.class.getDeclaredMethod("getClassLoader");
            final var currentAccess = classLoaderMethod.canAccess(plugin);
            if (!currentAccess) classLoaderMethod.setAccessible(true);
            final var classLoader = (ClassLoader) classLoaderMethod.invoke(plugin);
            classLoaderMethod.setAccessible(currentAccess); // Reset to original state.

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

            Objects.requireNonNull(app, "Application is null");
            MinixApplicationBuilder.initialised.add(urlPair);
        } catch (final NullPointerException | ReflectiveOperationException | IOException | URISyntaxException | NoSuchAlgorithmException | InterruptedException err) {
            throw new MinixApplicationException(err);
        }
    }

    // For some reason, using just class.getResource() doesn't work
    @Contract(pure = true)
    private static @NotNull FilePair findInput(@NotNull final Plugin plugin) throws MinixApplicationException {
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
        } catch (final IOException e) {
            throw new MinixApplicationException("Failed to find slimjar files in %s".formatted(src.toString()), e);
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
        public boolean equals(@Nullable final Object obj) {
            if (!(obj instanceof FilePair other)) return false;
            return Objects.equals(other.preRes, preRes) && Objects.equals(other.deps, deps);
        }
    }
}
