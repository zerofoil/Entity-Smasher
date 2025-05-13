package EntitySmasher

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.entity.Player
import org.bukkit.entity.Entity
import org.bukkit.entity.Boat
import org.bukkit.entity.Minecart
import java.util.UUID
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.util.Vector
import org.bukkit.Bukkit
import org.bukkit.Particle
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent

class EntitySmasher : JavaPlugin(), Listener {

    var active: MutableMap<Player, MutableMap<String, Any>> = mutableMapOf()
    val func = Functions(this)
    lateinit var configs: FileConfiguration
    lateinit var defaults: MutableMap<String, String>

    @EventHandler
    fun onRClick(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val entity = event.rightClicked
        val sneak = player.isSneaking
        if (entity is Boat || entity is Minecart) {
            if (func.get("allow_boat_minecart").equals("false")) return
        }
        if (entity is Player) {
            if (func.get("allow_players").equals("false")) return
        }
        if (func.get("sneak_to_select").equals(sneak.toString())) {
            if (active.containsKey(player)) {
                func.msg(player, "entity_already_selected")
            } else {
                val new: MutableMap<String, Any> = mutableMapOf(
                    "e" to entity,
                    "distance" to func.get("default_distance").toDouble()
                )
                active.put(player, new)
                func.msg(player, "entity_select")
            } 
        }
    }

    @EventHandler
    fun onBClick(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        val sneak = player.isSneaking
        if (event.hand == EquipmentSlot.OFF_HAND) return
        if (action == Action.RIGHT_CLICK_BLOCK) {
            if (func.get("sneak_to_deselect").equals(sneak.toString())) {
                val c = active.containsKey(player)
                active.remove(player)
                if (c) {
                    func.msg(player, "entity_deselect")
                }
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        active.remove(player)
    }

    fun toDo() {
        for (player in active.keys) {
            val entity = active[player]!!["e"] as Entity
            if (!entity.isValid && entity.isDead) {
                if (func.get("death_particles").equals("true")) {
                    val loc = entity.location
                    val world = loc.world

                    world?.spawnParticle(
                        Particle.FLAME,
                        loc,
                        200,
                        0.0, 0.0, 0.0,
                        0.5
                    )
                }
                active.remove(player)
                func.msg(player, "entity_deselect_death")
            }
            val (x, y, z) = func.nextPos(player, entity)
            entity.velocity = Vector(x, y, z)
            if (func.get("fall_damage").equals("false")) {
                entity.fallDistance = 0f
            }
        }
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        configs = func.cfginit()
        defaults = func.defaults()
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            toDo()
        }, 0L, 1L)
        logger.info("EntitySmasher is active.")
    }
    override fun onDisable() {
        logger.info("EntitySmasher disabled.")
    }

    @EventHandler
    fun onScroll(event: PlayerItemHeldEvent) {
        val player = event.player
        val prev = event.previousSlot
        val new = event.newSlot
        
        if (func.get("scroll_zoom").equals("true")) {
            try {
                val distc = active[player]!!["distance"] as Double
                var max = func.get("distance_limit_max").toDoubleOrNull() ?: 20.0
                var min = func.get("distance_limit_min").toDoubleOrNull() ?: 2.0
                var step = func.get("distance_scroll_step").toDoubleOrNull() ?: 0.2
                if (player.isSneaking) {
                    step = step*2
                }
                var newdistc = when {
                    (new == 0 && prev == 8) -> maxOf(distc - step, min)
                    (new == 8 && prev == 0) -> minOf(distc + step, max)
                    (new > prev) -> maxOf(distc - step, min)
                    (new < prev) -> minOf(distc + step, max)
                    else -> distc
                }
                newdistc = String.format("%.2f", newdistc).toDouble()
                active[player]!!["distance"] = newdistc

                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    *TextComponent.fromLegacyText(
                        func.get("changed_distance").replace("&", "§").replace("%distance%", "$newdistc")
                    )
                )
            } catch (e: NullPointerException) {
                return
            }
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (cmd.getName().equals("es", ignoreCase = true) && sender is Player) {
            if (args.size == 0) {
                if (sender in active) {
                    active.remove(sender)
                    func.msg(sender, "entity_deselect")
                }
            } else {
                func.msg(sender, "unknown_command")
            }
            return true
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, label: String, args: Array<String>): List<String> {
        if(cmd.getName().equals("es", ignoreCase = true)) { // to be continued...
            return when (args.size) {
                1 -> listOf("reload", "sec").filter { it.startsWith(args[0]) }
                else -> emptyList()
            }
        }
        return emptyList()
    }
}