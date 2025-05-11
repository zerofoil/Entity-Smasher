package EntitySmasher

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Functions(private var plugin: EntitySmasher) {
    val defaults: MutableMap<String, String> = mutableMapOf(
        "sneak_to_select" to "true"
        "sneak_to_deselect" to "true"
        "how_far" to "10"
    )
    fun cfginit(): FileConfiguration {
        var f = File(plugin.getDataFolder(), "config.yml")
        if (!f.exists()) {
            plugin.saveResource("config.yml", false)
        }
        var cfg = YamlConfiguration.loadConfiguration(f)
        return cfg
    }
    fun get(what: String): String {
        return plugin.config.getString(what, defaults[what])
    }
}