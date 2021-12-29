package eu.luftiger.luftigerlib.configuration.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class BungeeConfiguration {

    private final Plugin plugin;
    private Configuration configuration;
    private File file;

    protected BungeeConfiguration(Plugin plugin) {
        this.plugin = plugin;
    }

    public void createDefaults(String name) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder().getPath() + "/" + name);

        if (!file.exists()) {
            InputStream inputStream = plugin.getResourceAsStream(name);
            try {
                assert inputStream != null;
                Files.copy(inputStream, Paths.get(plugin.getDataFolder().getPath() + "/" + name), new CopyOption[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: ConfigUpdater
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConverted(String path){
        return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
