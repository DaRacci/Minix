package dev.racci.minix.core.plugin

import dev.racci.minix.api.plugin.MinixPlugin

public class Minix : MinixPlugin() {

    override suspend fun handleEnabled() {
        super.handleEnabled()
    }
}