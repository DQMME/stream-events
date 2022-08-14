package de.dqmme.events.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

object Items {
    val PLACEHOLDER = itemStack(Material.BLACK_STAINED_GLASS_PANE) {
        meta {
            displayName(Component.text("§c"))
        }
    }

    private fun getCustomSkull(displayName: Component, lore: List<Component>, value: String) = itemStack(Material.PLAYER_HEAD) {
        meta<SkullMeta> {
            val profile = GameProfile(UUID.randomUUID(), null)
            profile.properties.put("textures", Property("textures", value))

            val method = javaClass.getDeclaredMethod("setProfile", GameProfile::class.java)
            method.isAccessible = true
            method.invoke(this, profile)

            lore(lore)
            displayName(displayName)
        }
    }

    fun getNumberSkull(displayName: Component, lore: List<Component>) = getCustomSkull(displayName, lore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk4ZmI5MWM5MjVhNmQ0Y2MyMjdjYWIyNTQ3ODJjYzNlZDQzNmRjZjM3ZTZiNjdjODhhOWMyNjg4NDg3In19fQ==")
}