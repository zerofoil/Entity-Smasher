package EntitySmasher

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import org.bukkit.entity.Player
import org.bukkit.entity.Entity
import org.bukkit.ChatColor.translateAlternateColorCodes
import kotlin.math.*

class Functions(private val plugin: EntitySmasher) {
    
    fun get(what: String): String {
        return plugin.configs.getString(what, plugin.defaults[what]) ?: "Error occured"
    }

    fun cfginit(): FileConfiguration {
        var f = File(plugin.getDataFolder(), "config.yml")
        if (!f.exists()) {
            plugin.saveResource("config.yml", false)
        }
        var cfg = YamlConfiguration.loadConfiguration(f)
        return cfg
    }

    fun defaults(): MutableMap<String, String> {
        return mutableMapOf(
            "sneak_to_select" to "true",
            "sneak_to_deselect" to "true",
            "default_distance" to "10",
            "entity_select" to "&aEntity selected.",
            "entity_deselect" to "&cEntity deselected.",
            "entity_deselect_death" to "&cEntity unavailable, deselecting...",
            "entity_already_selected" to "&cYou have already entity selected! Deselect first.",
            "scroll_zoom" to "true",
            "changed_distance" to "&aDistance changed to %distance%",
            "distance_limit_min" to "2",
            "distance_limit_max" to "20",
            "distance_scroll_step" to "0.2",
            "death_particles" to "true",
            "unknown_command" to "&cSorry, but i don't recognize this command.",
            "allow_players" to "false",
            "allow_boat_minecart" to "false",
            "fall_damage" to "true"
        )
    }

    fun msg(sender: Player, what: String) {
        if (get(what).isNotEmpty()) {
            sender.sendMessage(translateAlternateColorCodes('&', get(what)))
        }
    }

    fun nextPos(player: Player, entity: Entity): Triple<Double, Double, Double> {
        try {
            val pp = player.location
            val plpitch = pp.pitch.toDouble()
            val plyaw = pp.yaw.toDouble()
            val plx = pp.x
            val ply = pp.y
            val plz = pp.z
            val op = entity.location
            val obx = op.x
            val oby = op.y
            val obz = op.z
            val height = entity.boundingBox.height / 2
    
            val distance = plugin.active[player]!!["distance"] as Double
    
            val pitchRad = Math.toRadians(plpitch)
            val yawRad = Math.toRadians(plyaw)
    
            val x = -sin(yawRad) * cos(pitchRad)
            val y = -sin(pitchRad)
            val z = cos(yawRad) * cos(pitchRad)
    
            val length = sqrt(x * x + y * y + z * z)
            val nx = x / length * distance
            val ny = y / length * distance
            val nz = z / length * distance
    
            val targetX = plx + nx
            val targetY = ply + ny
            val targetZ = plz + nz
    
            val moveX = targetX - obx
            val moveY = targetY - oby
            val moveZ = targetZ - obz
            return Triple(moveX, moveY+height, moveZ)       
        } catch (e: NullPointerException) {
            return Triple(0.0, 0.0, 0.0)
        }
    }
}