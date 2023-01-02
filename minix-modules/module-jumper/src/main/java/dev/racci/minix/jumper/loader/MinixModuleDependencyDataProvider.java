package dev.racci.minix.jumper.loader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.dependency.DependencyReader;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class MinixModuleDependencyDataProvider extends MinixDependencyDataProvider {

    private final URL moduleUrl;


    public MinixModuleDependencyDataProvider(DependencyReader dependencyReader, URL moduleUrl) {
        super(dependencyReader);
        this.moduleUrl = moduleUrl;
    }

    @Override
    public DependencyData load() throws IOException, ReflectiveOperationException {
        final var fileURL = this.getURL();
        final var connection = fileURL.openConnection();

        if (!(connection instanceof JarURLConnection urlConnection)) {
            throw new AssertionError("Invalid Module URL provided(Non-Jar File)");
        }

        final var jarFile = urlConnection.getJarFile();
        final var fileEntry = jarFile.getEntry("slimjar.json");

        if (fileEntry == null) return null;

        try (final var stream = jarFile.getInputStream(fileEntry)) {
            this.dependencyReader.read(stream);
        }

        return null;
    }

    public URL getURL() throws MalformedURLException {
        return new URL("jar:file:" + this.moduleUrl.getFile() + "!/slimjar.json");
    }
}