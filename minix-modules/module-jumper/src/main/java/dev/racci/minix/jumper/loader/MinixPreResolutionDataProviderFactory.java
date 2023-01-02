package dev.racci.minix.jumper.loader;

import io.github.slimjar.resolver.reader.facade.GsonFacade;
import io.github.slimjar.resolver.reader.resolution.GsonPreResolutionDataReader;
import io.github.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import java.net.URL;

public final class MinixPreResolutionDataProviderFactory {

    private static GsonPreResolutionDataReader gsonPreResolutionReader;

    private MinixPreResolutionDataProviderFactory() {}

    public static void init(final GsonFacade gsonFacade) {
        gsonPreResolutionReader = new GsonPreResolutionDataReader(gsonFacade);
    }

    public static PreResolutionDataProvider create(final URL resolutionFileURL) {
        return new MinixPreResolutionDataProvider(gsonPreResolutionReader, resolutionFileURL);
    }
}