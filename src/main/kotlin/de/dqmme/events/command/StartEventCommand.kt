package de.dqmme.events.command

import de.dqmme.events.dataclass.Event
import de.dqmme.events.dataclass.EventElement
import de.dqmme.events.util.Items
import de.dqmme.events.util.deserializeMiniMessage
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.gotoPage
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import java.util.UUID

private val chosenEvents = hashMapOf<UUID, EventElement>()

fun startEventCommand() = command("startevent") {
    requiresPermission("command.startevent")

    runs {
        try {


            player.openGUI(kSpigotGUI(GUIType.THREE_BY_NINE) {
                page(1) {
                    transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                    transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                    placeholder(Slots.All, Items.PLACEHOLDER)

                    val compound = createRectCompound<EventElement>(
                        Slots.RowOneSlotOne,
                        Slots.RowThreeSlotEight,
                        iconGenerator = { it.itemStack },
                        onClick = { clickEvent, element ->
                            clickEvent.bukkitEvent.isCancelled = true
                            chosenEvents[clickEvent.player.uniqueId] = element
                            clickEvent.guiInstance.gotoPage(2)
                        })

                    compound.addContent(EventElement(itemStack(Material.NETHER_STAR) {
                        meta {
                            displayName("<white>Wither töten".deserializeMiniMessage())

                            setLore {
                                +"<green>Wer als erstes den <white>Wither <red>tötet, <green>gewinnt.".deserializeMiniMessage()
                            }
                        }
                    }, Event.KILL_WITHER))
                }

                page(2) {
                    transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                    transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                    placeholder(Slots.All, Items.PLACEHOLDER)

                    pageChanger(
                        Slots.RowTwoSlotThree, Items.getNumberSkull(
                            "<green>Erlaubte Spielerzahl setzen".deserializeMiniMessage(),
                            listOf(
                                "<green>Setze wie viele Spieler an dem Event teilnehmen können.".deserializeMiniMessage(),
                                "<green>Aktuell: <aqua>${chosenEvents[player.uniqueId]?.allowedPlayers}".deserializeMiniMessage()
                            )
                        ), 3, null, null
                    )

                    button(Slots.RowTwoSlotFive, itemStack(Material.GOLD_INGOT) {
                        meta {
                            displayName("<green>Nur Subs".deserializeMiniMessage())

                            setLore {
                                +"<green>Stelle ein, ob <aqua>nur Subs <green>oder <aqua>jeder <green>am Event teilnehen darf.".deserializeMiniMessage()
                            }
                        }
                    }) {
                        val onlySubs = !(chosenEvents[player.uniqueId]?.onlySubs ?: false)

                        if (onlySubs) {
                            it.guiInstance[Slots.RowThreeSlotFive] = itemStack(Material.LIME_DYE) {
                                meta {
                                    displayName("<green>Aktiviert".deserializeMiniMessage())

                                    setLore {
                                        +"<green>Derzeit können <aqua>nur Subs <green>am Event teilnehmen.".deserializeMiniMessage()
                                    }
                                }
                            }
                        } else {
                            it.guiInstance[Slots.RowThreeSlotFive] = itemStack(Material.RED_DYE) {
                                meta {
                                    displayName("<red>Deaktiviert".deserializeMiniMessage())

                                    setLore {
                                        +"<green>Derzeit kann <aqua>jeder <green>am Event teilnehmen.".deserializeMiniMessage()
                                    }
                                }
                            }
                        }
                    }

                    button(Slots.RowThreeSlotFive, itemStack(Material.RED_DYE) {
                        meta {
                            displayName("<red>Deaktiviert".deserializeMiniMessage())

                            setLore {
                                +"<green>Derzeit kann <aqua>jeder <green>am Event teilnehmen.".deserializeMiniMessage()
                            }
                        }
                    }) {

                    }
                }

                page(3) {
                    transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                    transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                    placeholder(Slots.All, Items.PLACEHOLDER)

                    button(Slots.RowTwoSlotOne, itemStack(Material.RED_DYE) {
                        meta {
                            displayName("<red>1 weniger".deserializeMiniMessage())

                            setLore {
                                +"<red>Verringere die Zahl der erlaubten Spieler um 1.".deserializeMiniMessage()
                            }
                        }
                    }) {
                        val event = chosenEvents[player.uniqueId]
                        var newPlayers = (event?.allowedPlayers ?: 20) - 1

                        if (newPlayers < 5) newPlayers = 5

                        if (event != null) chosenEvents[player.uniqueId] = event.copy(allowedPlayers = newPlayers)
                        it.guiInstance[Slots.RowTwoSlotFive] = itemStack(Material.NETHER_STAR) {
                            meta {
                                displayName("<green>Erlaubte Spieler - <aqua>$newPlayers".deserializeMiniMessage())

                                setLore {
                                    +"<aqua>$newPlayers <green>können an dem Event teilnehmen.".deserializeMiniMessage()
                                }
                            }
                        }
                    }

                    button(Slots.RowTwoSlotThree, itemStack(Material.RED_DYE) {
                        meta {
                            displayName("<red>10 weniger".deserializeMiniMessage())

                            setLore {
                                +"<red>Verringere die Zahl der erlaubten Spieler um 10.".deserializeMiniMessage()
                            }
                        }
                    }) {
                        val event = chosenEvents[player.uniqueId]
                        var newPlayers = event!!.allowedPlayers - 10

                        if (newPlayers < 5) newPlayers = 5

                        chosenEvents[player.uniqueId] = event.copy(allowedPlayers = newPlayers)
                        it.guiInstance[Slots.RowTwoSlotFive] = itemStack(Material.NETHER_STAR) {
                            meta {
                                displayName("<green>Erlaubte Spieler - <aqua>$newPlayers".deserializeMiniMessage())

                                setLore {
                                    +"<aqua>$newPlayers <green>können an dem Event teilnehmen.".deserializeMiniMessage()
                                }
                            }
                        }
                    }

                    val playerCount = chosenEvents[player.uniqueId]?.allowedPlayers ?: 20

                    button(Slots.RowTwoSlotFive, itemStack(Material.NETHER_STAR) {
                        meta {
                            displayName("<green>Erlaubte Spieler - <aqua>$playerCount".deserializeMiniMessage())

                            setLore {
                                +"<aqua>$playerCount <green>können an dem Event teilnehmen.".deserializeMiniMessage()
                            }
                        }
                    }) {}

                    button(Slots.RowTwoSlotSeven, itemStack(Material.LIME_DYE) {
                        meta {
                            displayName("<red>1 mehr".deserializeMiniMessage())

                            setLore {
                                +"<red>Steigere die Zahl der erlaubten Spieler um 1.".deserializeMiniMessage()
                            }
                        }
                    }) {
                        val event = chosenEvents[player.uniqueId]
                        var newPlayers = event!!.allowedPlayers + 1

                        if (newPlayers > 100) newPlayers = 100

                        chosenEvents[player.uniqueId] = event.copy(allowedPlayers = newPlayers)
                        it.guiInstance[Slots.RowTwoSlotFive] = itemStack(Material.NETHER_STAR) {
                            meta {
                                displayName("<green>Erlaubte Spieler - <aqua>$newPlayers".deserializeMiniMessage())

                                setLore {
                                    +"<aqua>$newPlayers <green>können an dem Event teilnehmen.".deserializeMiniMessage()
                                }
                            }
                        }
                    }

                    button(Slots.RowTwoSlotNine, itemStack(Material.LIME_DYE) {
                        meta {
                            displayName("<red>10 mehr".deserializeMiniMessage())

                            setLore {
                                +"<red>Steigere die Zahl der erlaubten Spieler um 10.".deserializeMiniMessage()
                            }
                        }
                    }) {
                        val event = chosenEvents[player.uniqueId]
                        var newPlayers = event!!.allowedPlayers + 10

                        if (newPlayers > 100) newPlayers = 100

                        chosenEvents[player.uniqueId] = event.copy(allowedPlayers = newPlayers)
                        it.guiInstance[Slots.RowTwoSlotFive] = itemStack(Material.NETHER_STAR) {
                            meta {
                                displayName("<green>Erlaubte Spieler - <aqua>$newPlayers".deserializeMiniMessage())

                                setLore {
                                    +"<aqua>$newPlayers <green>können an dem Event teilnehmen.".deserializeMiniMessage()
                                }
                            }
                        }
                    }

                    pageChanger(Slots.RowThreeSlotOne, itemStack(Material.ARROW) {
                        meta {
                            displayName("<green>Menü".deserializeMiniMessage())

                            setLore {
                                +"<green>Nehme weitere Einstellungen am Event vor.".deserializeMiniMessage()
                            }
                        }
                    }, 2, null, onChange = { guiClickEvent ->
                        guiClickEvent.guiInstance.reloadCurrentPage()
                    })
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}