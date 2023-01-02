package dev.racci.minix.jumper.loader;

import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.downloader.strategy.ChecksumFilePathStrategy;
import io.github.slimjar.downloader.verify.FileChecksumCalculator;
import io.github.slimjar.downloader.verify.PassthroughDependencyVerifierFactory;
import io.github.slimjar.resolver.DependencyResolver;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MinixDependencyVerifierFactory {
    private static final Logger LOGGER = Logger.getLogger(MinixDependencyVerifierFactory.class.getName());

    private static FileChecksumCalculator checksumCalculator;
    private static DependencyOutputWriterFactory outputWriterFactory;
    private static PassthroughDependencyVerifierFactory fallbackFactory;

    private MinixDependencyVerifierFactory() {}

    public static void init(final File downloadFolder) {
        try {
            checksumCalculator = new FileChecksumCalculator("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final var filePathStrategy = ChecksumFilePathStrategy.createStrategy(downloadFolder, "SHA-1");
        outputWriterFactory = new DependencyOutputWriterFactory(filePathStrategy);
        fallbackFactory = new PassthroughDependencyVerifierFactory();
    }

    public static MinixDependencyVerifier create(final DependencyResolver resolver) {
        LOGGER.log(Level.FINEST, "Creating verifier...");
        return new MinixDependencyVerifier(resolver, outputWriterFactory, fallbackFactory.create(resolver), checksumCalculator);
    }
}