package dev.racci.minix.api.integrations.regions

import java.util.UUID

interface Region {

    val id: Int
    val name: String
    val owner: UUID
}
