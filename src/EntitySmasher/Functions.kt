package EntitySmasher

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import org.bukkit.entity.Player
import org.bukkit.ChatColor.translateAlternateColorCodes

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
            "how_far" to "10",
            "entity_select" to "&aEntity selected.",
            "entity_deselect" to "&cEntity deselected.",
            "entity_deselect_death" to "&cEntity unavailable, deselecting...",
            "entity_already_selected" to "&cYou have already entity selected! Deselect first."
        )
    }

    fun msg(sender: Player, what: String) {
        if (get(what).isNotEmpty()) {
            sender.sendMessage(translateAlternateColorCodes('&', get(what)))
        }
    }

}