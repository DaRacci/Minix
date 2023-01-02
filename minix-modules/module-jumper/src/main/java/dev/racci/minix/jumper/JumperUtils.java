package dev.racci.minix.jumper;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class JumperUtils {
    private JumperUtils() {}

    @Contract(value = "_, _ -> fail", pure = true)
    static void errorInit(
        @Nullable final Throwable err,
        @NotNull final Plugin plugin
    ) {
        plugin.getSLF4JLogger().error("Failed to initialize Minix, disabling plugin!", err);
        plugin.getServer().getPluginManager().disablePlugin(plugin);

        if (!(err instanceof InterruptedException) || !(err.getCause() instanceof InterruptedException)) return;
        Thread.currentThread().interrupt();
    }
}
