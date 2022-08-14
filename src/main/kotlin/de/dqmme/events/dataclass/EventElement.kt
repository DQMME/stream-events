package de.dqmme.events.dataclass

import org.bukkit.inventory.ItemStack

data class EventElement(
    val itemStack: ItemStack,
    val event: Event,
    val allowedPlayers: Int = 20,
    val onlySubs: Boolean = false
)
