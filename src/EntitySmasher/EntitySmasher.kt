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
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.configuration.file.FileConfiguration

class EntitySmasher : JavaPlugin(), Listener {

    var active: MutableMap<Player, Entity> = mutableMapOf()
    val func = Functions(this)
    lateinit var config: FileConfiguration

    @EventHandler
    fun onRClick(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val entity = event.rightClicked
        val sneak = player.isSneaking

        if (func.get("sneak_to_select"))
        active[player] = entity
    }

    @EventHandler
    fun onBClick(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) return
        if (block?.type == Material.AIR) {
            active.remove(player)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        active.remove(player)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val entity = active[player]
        if (entity == null) return
        if (!entity.isValid && entity.isDead) return

    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        config = func.cfginit()
        logger.info("EntitySmasher is active.")
    }
    override fun onDisable() {
        logger.info("EntitySmasher disabled.")
    }

}