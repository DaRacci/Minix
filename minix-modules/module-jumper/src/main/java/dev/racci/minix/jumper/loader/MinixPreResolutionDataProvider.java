package dev.racci.minix.jumper.loader;

import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import io.github.slimjar.resolver.reader.resolution.PreResolutionDataReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MinixPreResolutionDataProvider implements PreResolutionDataProvider {
    private final PreResolutionDataReader resolutionDataReader;
    private final URL resolutionFileURL;
    private boolean dirty = true;

    private static final Map<String, ResolutionResult> cachedData = new ConcurrentHashMap<>();

    public MinixPreResolutionDataProvider(
        final PreResolutionDataReader resolutionDataReader,
        final URL resolutionFileURL
    ) {
        this.resolutionDataReader = resolutionDataReader;
        this.resolutionFileURL = resolutionFileURL;
    }

    private void load() throws ReflectiveOperationException, IOException {
        InputStream stream;
        try {
            stream = this.resolutionFileURL.openStream();
        } catch (Exception e) {
            return;
        }

        try {
            final var newData = this.resolutionDataReader.read(stream);
            newData.forEach(cachedData::putIfAbsent);
        } catch (final Exception err) {
            try {
                stream.close();
            } catch (final Exception err2) {
                err.addSuppressed(err2);
            }

            throw err;
        }

        this.dirty = false;
    }

    @Override
    public Map<String, ResolutionResult> get() throws ReflectiveOperationException, IOException {
        if (this.dirty) {
            this.load();
        }

        return cachedData;
    }
}