package dev.racci.minix.core.loader;

import io.github.slimjar.resolver.reader.dependency.GsonDependencyReader;
import java.net.URL;

public final class MinixGsonDependencyDataProviderFactory {
    private static GsonDependencyReader dependencyReader;

    private MinixGsonDependencyDataProviderFactory() {}

    public static void init(final GsonDependencyReader dependencyReader) {
        MinixGsonDependencyDataProviderFactory.dependencyReader = dependencyReader;
    }

    public static MinixURLDependencyDataProvider create(final URL dependencyFileURL) {
        return new MinixURLDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}