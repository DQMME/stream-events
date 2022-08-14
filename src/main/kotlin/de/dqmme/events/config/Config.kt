package de.dqmme.events.config

import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.configuration.file.FileConfiguration

object Config {
    private lateinit var yamlConfiguration: FileConfiguration

    operator fun invoke() {
        yamlConfiguration = KSpigotMainInstance.config
    }

    fun getBotToken(): String? = yamlConfiguration.getString("bot_token")

    fun getUserDatabaseName(): String = yamlConfiguration.getString("user_database_name") ?: "users"

    fun getGuildId(): Long = yamlConfiguration.getLong("guild_id")

    fun getSubRoleId(): Long = yamlConfiguration.getLong("sub_role_id")

    fun getWhitelistChannel(): Long = yamlConfiguration.getLong("whitelist_channel")

    fun getWhitelistMessageId(): Long = yamlConfiguration.getLong("whitelist_message_id")

    fun setWhitelistMessageId(id: Long) = yamlConfiguration.set("whitelist_message_id", id)

    fun save() {
        KSpigotMainInstance.saveDefaultConfig()
    }

    fun reload() {
        KSpigotMainInstance.reloadConfig()
    }
}