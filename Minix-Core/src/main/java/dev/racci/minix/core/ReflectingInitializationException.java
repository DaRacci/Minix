package dev.racci.minix.core;

import org.jetbrains.annotations.Nullable;

public class ReflectingInitializationException extends RuntimeException{

    public ReflectingInitializationException(@Nullable Exception nested) {
        super(null, nested);
    }
}
