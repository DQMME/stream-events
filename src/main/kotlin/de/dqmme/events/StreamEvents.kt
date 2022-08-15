package de.dqmme.events

import de.dqmme.events.command.startEventCommand
import de.dqmme.events.config.Config
import de.dqmme.events.discord.BotLauncher
import de.dqmme.events.listener.ConnectionListeners
import de.dqmme.events.util.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.axay.kspigot.main.KSpigot

class StreamEvents : KSpigot() {
    override fun load() {

    }

    override fun startup() {
        Config()
        Database()
        commandRegistration()
        listenerRegistration()

        CoroutineScope(Dispatchers.IO).launch {
            BotLauncher.startBot()
            BotLauncher.startTimer()
        }
    }

    override fun shutdown() {

    }

    private fun commandRegistration() {
        startEventCommand()
    }

    private fun listenerRegistration() {
        ConnectionListeners()
    }
}
