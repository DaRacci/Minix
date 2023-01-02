package dev.racci.minix.jumper.loader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Mirror;
import io.github.slimjar.resolver.reader.dependency.DependencyDataProvider;
import io.github.slimjar.resolver.reader.dependency.DependencyReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

public abstract class MinixDependencyDataProvider implements DependencyDataProvider {
    protected static final AtomicReference<DependencyData> dependencyData = new AtomicReference<>(new DependencyData(Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet()));
    protected final DependencyReader dependencyReader;
    protected boolean dirty = true;

    protected MinixDependencyDataProvider(final DependencyReader dependencyReader) {
        this.dependencyReader = dependencyReader;
    }

    abstract DependencyData load() throws IOException, ReflectiveOperationException;

    @Override
    public final DependencyData get() throws IOException, ReflectiveOperationException {
        if (this.dirty) {
            final var newData = this.load();

            dependencyData.getAndUpdate(existingData -> {
                final var mergedMirrors = this.mergeList(existingData.mirrors(), newData.mirrors(), Mirror::equals);
                final var mergedRepos = this.mergeList(existingData.repositories(), newData.repositories(), (a, b) -> a.url().equals(b.url()));
                final var mergedDependencies = this.mergeList(existingData.dependencies(), newData.dependencies(), (a, b) -> Objects.equals(a.groupId(), b.groupId()) && Objects.equals(a.artifactId(), b.artifactId()));

                return new DependencyData(mergedMirrors, mergedRepos, mergedDependencies, Collections.emptySet());
            });

            this.dirty = false;
        }

        return dependencyData.get();
    }

    protected <T> Collection<T> mergeList(
        Collection<T> existing,
        Collection<T> newData,
        BiPredicate<T, T> isSimilar
    ) {
        final var mutable = new HashSet<>(existing);
        for (final var data : newData) {
            final var existingData = mutable.stream().filter(it -> isSimilar.test(it, data)).findFirst();
            if (existingData.isPresent()) continue;

            mutable.add(data);
        }

        return Collections.unmodifiableCollection(mutable);
    }
}
