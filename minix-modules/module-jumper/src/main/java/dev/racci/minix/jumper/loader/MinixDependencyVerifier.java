package dev.racci.minix.jumper.loader;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.verify.ChecksumCalculator;
import io.github.slimjar.downloader.verify.DependencyVerifier;
import io.github.slimjar.logging.LogDispatcher;
import io.github.slimjar.logging.ProcessLogger;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.util.Connections;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;

public final class MinixDependencyVerifier implements DependencyVerifier {
    private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
    private static final HashSet<Dependency> MATCHES = new HashSet<>();

    private final DependencyResolver resolver;
    private final OutputWriterFactory outputWriterFactory;
    private final DependencyVerifier fallbackVerifier;
    private final ChecksumCalculator checksumCalculator;

    public MinixDependencyVerifier(DependencyResolver resolver, OutputWriterFactory outputWriterFactory, DependencyVerifier fallbackVerifier, ChecksumCalculator checksumCalculator) {
        this.resolver = resolver;
        this.outputWriterFactory = outputWriterFactory;
        this.fallbackVerifier = fallbackVerifier;
        this.checksumCalculator = checksumCalculator;
    }

    @Override
    public boolean verify(
        final File file,
        final Dependency dependency
    ) throws IOException, InterruptedException {
        if (alreadyVerified(dependency)) return true;
        if (!file.exists()) return false;

        LOGGER.log("Verifying checksum for %s", dependency.artifactId());

        final var checksumFile = this.outputWriterFactory.getStrategy().selectFileFor(dependency);

        if (!checksumFile.exists() && !this.prepareChecksumFile(checksumFile, dependency)) {
            LOGGER.log("Unable to resolve checksum for %s, falling back to fallbackVerifier!", dependency.artifactId());
            return this.fallbackVerifier.verify(file, dependency);
        }

        if (checksumFile.length() == 0L) {
            LOGGER.log("Required checksum not found for %s, using fallbackVerifier!", dependency.artifactId());
            return this.fallbackVerifier.verify(file, dependency);
        }

        final var actualChecksum = this.checksumCalculator.calculate(file);
        final var expectedChecksum = Files.readString(checksumFile.toPath()).trim();

        LOGGER.debug("%s -> Actual checksum: %s;", dependency.artifactId(), actualChecksum);
        LOGGER.debug("%s -> Expected checksum: %s;", dependency.artifactId(), expectedChecksum);

        boolean match = Objects.equals(actualChecksum, expectedChecksum);

        LOGGER.log("Checksum %s for %s", match ? "matched" : "match failed", dependency.artifactId());

        if (match) MATCHES.add(dependency);

        return match;
    }

    @Override
    public File getChecksumFile(final Dependency dependency) {
        File checksumFile = this.outputWriterFactory.getStrategy().selectFileFor(dependency);
        checksumFile.getParentFile().mkdirs();
        return checksumFile;
    }

    private static boolean alreadyVerified(final Dependency dependency) {
        return MATCHES.stream().parallel().anyMatch(dep -> dep.equals(dependency));
    }

    private boolean prepareChecksumFile(
        final File checksumFile,
        final Dependency dependency
    ) throws RuntimeException {
        checksumFile.getParentFile().mkdirs();

        return this.resolver.resolve(dependency)
            .map(ResolutionResult::getChecksumURL)
            .map(checksumURL -> {
                    LOGGER.log("Resolved checksum for %s", dependency.artifactId());

                    URLConnection connection = null;
                    try {
                        if (checksumURL == null) {
                            checksumFile.createNewFile();
                            return true;
                        }

                        connection = Connections.createDownloadConnection(checksumURL);
                        final var inputStream = connection.getInputStream();
                        final var outputWriter = this.outputWriterFactory.create(dependency);

                        outputWriter.writeFrom(inputStream, connection.getContentLengthLong());
                    } catch (IOException err) {
                        try {
                            Files.deleteIfExists(checksumFile.toPath());
                        } catch (IOException ignored) {}

                        LOGGER.log("Failed to resolve checksum for %s", dependency.artifactId());

                        throw new RuntimeException(err);
                    } finally {
                        Connections.tryDisconnect(connection);
                    }

                    LOGGER.log("Downloaded checksum for %s", dependency.artifactId());

                    return true;
                }
            ).orElse(false);
    }
}