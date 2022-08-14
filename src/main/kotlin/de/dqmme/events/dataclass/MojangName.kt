package de.dqmme.events.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class MojangName(
    val name: String,
    val id: String
)
