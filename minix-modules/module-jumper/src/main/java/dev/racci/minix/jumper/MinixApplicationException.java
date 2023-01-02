package dev.racci.minix.jumper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MinixApplicationException extends RuntimeException {

    @Contract(pure = true)
    MinixApplicationException(
        @Nullable final String message,
        @Nullable final Throwable nested
    ) {
        super(message, nested);
    }

    @Contract(pure = true)
    MinixApplicationException(@NotNull final Exception nested) {
        this(null, nested);
    }

    @Contract(pure = true)
    MinixApplicationException(@NotNull final String message) {
        this(message, null);
    }
}
