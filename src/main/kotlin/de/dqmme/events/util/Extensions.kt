package de.dqmme.events.util

import de.dqmme.events.dataclass.MojangName
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.firstOrNull
import net.kyori.adventure.text.minimessage.MiniMessage


fun String.deserializeMiniMessage() = MiniMessage.miniMessage().deserialize(this)

suspend inline fun <reified T> HttpResponse.bodyOrNull() = try {
    body<T>()
} catch (_: NoTransformationFoundException) {
    null
}

suspend fun Member.hasRole(role: Role) = roles.firstOrNull { it == role } != null

fun GuildModalSubmitInteractionCreateEvent.hasRole(role: Role) = interaction.data.member.value!!.roles.contains(role.id)

val MojangName.idWithDashes: String
    get() = id.replaceFirst(
        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
    )