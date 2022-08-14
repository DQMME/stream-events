package de.dqmme.events.dataclass

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("_id") val discordId: Snowflake,
    @SerialName("minecraft_uuid") val minecraftUuid: String,
    @SerialName("id_sub") val isSub: Boolean = false
)
