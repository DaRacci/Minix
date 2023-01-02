package dev.racci.minix.jumper.loader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.dependency.DependencyReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public final class MinixURLDependencyDataProvider extends MinixDependencyDataProvider {
    private final URL depFileURL;

    public MinixURLDependencyDataProvider(
        final DependencyReader dependencyReader,
        final URL depFileURL
    ) {
        super(dependencyReader);
        this.depFileURL = depFileURL;
    }

    @Override
    public DependencyData load() throws IOException, ReflectiveOperationException {
        URLConnection connection = depFileURL.openConnection();
        // Do not cache so we can re-read (ex during some form of reload) from this jar file if it changes.
        connection.setUseCaches(false);

        try (final var stream = connection.getInputStream()) {
            return dependencyReader.read(stream);
        }
    }
}
