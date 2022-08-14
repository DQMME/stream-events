package de.dqmme.events

import de.dqmme.events.config.Config
import de.dqmme.events.listener.ConnectionListeners
import de.dqmme.events.util.Database
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.main.KSpigot

class StreamEvents : KSpigot() {
    override fun load() {
        Config()
        Database()
    }

    override fun startup() {
        listenerRegistration()
    }

    override fun shutdown() {

    }

    private fun listenerRegistration() {
        ConnectionListeners()
    }
}
