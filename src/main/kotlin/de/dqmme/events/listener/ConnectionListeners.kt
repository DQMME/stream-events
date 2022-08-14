package de.dqmme.events.listener

import de.dqmme.events.util.deserializeMiniMessage
import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionListeners {
    init {
        onJoin()
        onQuit()
    }

    private fun onJoin() = listen<PlayerJoinEvent> {
        it.joinMessage("<grey>[<gold>Events<grey>] <aqua>${it.player.name} <green>hat das Spiel betreten.".deserializeMiniMessage())
    }

    private fun onQuit() = listen<PlayerQuitEvent> {
        it.quitMessage("<grey>[<gold>Events<grey>] <aqua>${it.player.name} <red>hat das Spiel verlassen.".deserializeMiniMessage())
    }
}