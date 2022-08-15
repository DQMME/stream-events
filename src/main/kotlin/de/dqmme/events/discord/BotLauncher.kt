package de.dqmme.events.discord

import de.dqmme.events.config.Config
import de.dqmme.events.dataclass.UserData
import de.dqmme.events.util.Database
import de.dqmme.events.util.MojangAPI
import de.dqmme.events.util.hasRole
import de.dqmme.events.util.idWithDashes
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

object BotLauncher {
    private var kord: Kord? = null

    suspend fun startBot() {
        val token = Config.getBotToken() ?: return

        kord = Kord(token) {}

        CoroutineScope(Dispatchers.IO).launch {
            kord!!.login {}
        }

        if(kord == null) return

        val channel = kord!!.getChannelOf<TextChannel>(Snowflake(Config.getWhitelistChannel())) ?: return
        val messageId = Config.getWhitelistMessageId()

        if (channel.getMessageOrNull(Snowflake(messageId)) == null) {
            val message = channel.createMessage {
                actionRow {
                    interactionButton(ButtonStyle.Success, "link-minecraft-button") {
                        label = "Minecraft Account verknüpfen"
                    }
                }

                embed {
                    title = "Verknüpfe deinen Minecraft Account"

                    description =
                        "Klicke den untenstehenden Button und trage deinen Minecraft Namen ein, um diesen zu verknüpfen und bei Events mitspielen zu können."

                    color = Color(0, 234, 255)
                }
            }
            Config.setWhitelistMessageId(message.id.value.toLong())
        }

        kord!!.on<ButtonInteractionCreateEvent> {
            if(interaction.componentId != "link-minecraft-button") return@on

            interaction.modal("Minecraft Account verknüpfen", "link-minecraft-modal") {
                actionRow {
                    textInput(TextInputStyle.Short, "ingame-name", "Minecraft Name") {
                        required = true
                        placeholder = "DQMME"
                        allowedLength = 3..16
                    }
                }
            }
        }

        kord!!.on<GuildModalSubmitInteractionCreateEvent> {
            if(interaction.guild.id != Snowflake(Config.getGuildId())) return@on
            val inGameName = interaction.textInputs["ingame-name"]?.value

            if(inGameName == null) {
                interaction.respondEphemeral {
                    content = "Du musst einen Minecraft Namen angeben."
                }
                return@on
            }

            val name = MojangAPI.getMinecraftUUID(inGameName)

            if(name == null) {
                interaction.respondEphemeral {
                    content = "Dieser Spieler wurde nicht gefunden."
                }
                return@on
            }

            if(Database.getUserData(interaction.user.id, name.idWithDashes) != null) {
                interaction.respondEphemeral {
                    content = "Dieser Discord oder Minecraft Account ist bereits verbunden."
                }
                return@on
            }

            var userData = UserData(interaction.user.id, name.idWithDashes)
            val subRole = interaction.guild.getRoleOrNull(Snowflake(Config.getSubRoleId()))

            if(subRole != null) {
                if(!hasRole(subRole)) return@on

                userData = userData.copy(isSub = true)
            }

            Database.saveUserData(userData)
            interaction.respondEphemeral {
                content = "Du hast deinen Minecraft Account erfolgreich verknüpft."
            }
        }
    }

    suspend fun startTimer() {
        if(kord == null) return

        coroutineScope {
            launch {
                while(isActive) {
                    val guild = kord!!.getGuild(Snowflake(Config.getGuildId())) ?: return@launch
                    val subRole = guild.getRoleOrNull(Snowflake(Config.getSubRoleId())) ?: return@launch

                    Database.getUserData().forEach {
                        val member = guild.getMemberOrNull(it.discordId) ?: return@forEach
                        Database.saveUserData(it.copy(isSub = member.hasRole(subRole)))
                    }

                    delay(15.minutes)
                }
            }
        }
    }
}