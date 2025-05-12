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
import java.util.UUID
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.util.Vector
import org.bukkit.Bukkit

class EntitySmasher : JavaPlugin(), Listener {

    var active: MutableMap<Player, Entity> = mutableMapOf()
    val func = Functions(this)
    lateinit var configs: FileConfiguration
    lateinit var defaults: MutableMap<String, String>

    @EventHandler
    fun onRClick(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val entity = event.rightClicked
        val sneak = player.isSneaking

        if (func.get("sneak_to_select").equals(sneak.toString())) {
            if (active.containsKey(player)) {
                func.msg(player, "entity_already_selected")
            } else {
                active[player] = entity
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
            val entity = active[player]
            if (entity == null) return
            if (!entity.isValid && entity.isDead) {
                active.remove(player)
                func.msg(player, "entity_deselect_death")
            }
            val (x, y, z) = func.nextPos(player, entity)
            entity.velocity = Vector(x, y, z)
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
            if 
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (cmd.getName().equals("es", ignoreCase = true) && sender is Player) {
            if (args.size == 0) {
                func.msg(sender, "")
            } else {
                func.msg(sender, "")
            }
            return true
        }
        return false
    }

}