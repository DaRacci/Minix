package dev.racci.minix.core.loader;

import io.github.slimjar.resolver.reader.dependency.GsonDependencyReader;
import java.net.URL;

public final class MinixExternalDependencyDataProviderFactory {
    private static GsonDependencyReader dependencyReader;

    private MinixExternalDependencyDataProviderFactory() {}

    public static void init(final GsonDependencyReader dependencyReader) {
        MinixExternalDependencyDataProviderFactory.dependencyReader = dependencyReader;
    }

    public static MinixModuleDependencyDataProvider create(final URL dependencyFileURL) {
        return new MinixModuleDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}