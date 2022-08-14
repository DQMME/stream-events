package de.dqmme.events.util

import de.dqmme.events.dataclass.MojangName
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object MojangAPI {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
    }

    suspend fun getMinecraftUUID(name: String) =
        httpClient.get("https://api.mojang.com/users/profiles/minecraft/$name").bodyOrNull<MojangName>()
}