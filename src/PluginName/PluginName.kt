package PluginName

import org.bukkit.plugin.java.JavaPlugin

class PluginName : JavaPlugin() {

    override fun onEnable() {
        logger.info("Plugin started.")
    }
    override fun onDisable() {
        logger.info("Plugin disabled.")
    }

}