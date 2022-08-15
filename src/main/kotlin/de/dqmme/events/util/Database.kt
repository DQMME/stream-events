package de.dqmme.events.util

import de.dqmme.events.config.Config
import de.dqmme.events.dataclass.UserData
import dev.kord.common.entity.Snowflake
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.or
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    private lateinit var client: CoroutineClient
    private lateinit var database: CoroutineDatabase
    private lateinit var userCollection: CoroutineCollection<UserData>

    operator fun invoke() {
        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.serialization.SerializationClassMappingTypeService")
        client = KMongo.createClient().coroutine
        database = client.getDatabase(Config.getUserDatabaseName())
        userCollection = database.getCollection()
    }

    suspend fun getUserData() = userCollection.find().toList()

    suspend fun getUserData(discordId: Snowflake) = userCollection.findOne(UserData::discordId eq discordId)

    suspend fun getUserData(minecraftUuid: String) = userCollection.findOne(UserData::minecraftUuid eq minecraftUuid)

    suspend fun getUserData(discordId: Snowflake, minecraftUuid: String) =
        userCollection.findOne(or(UserData::discordId eq discordId, UserData::minecraftUuid eq minecraftUuid))

    suspend fun saveUserData(userData: UserData) = userCollection.save(userData)
}