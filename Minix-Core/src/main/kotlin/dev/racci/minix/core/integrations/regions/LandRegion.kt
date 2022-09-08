package dev.racci.minix.core.integrations.regions

import dev.racci.minix.api.integrations.regions.Region
import me.angeschossen.lands.api.land.Land

class LandRegion(private val land: Land) : Region {

    override val id get() = land.id
    override val name get() = land.name
    override val owner get() = land.ownerUID
}
