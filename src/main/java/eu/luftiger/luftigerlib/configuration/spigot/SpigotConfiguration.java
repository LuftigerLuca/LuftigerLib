package eu.luftiger.luftigerlib.configuration.spigot;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public abstract class SpigotConfiguration {

    private final JavaPlugin plugin;
    private YamlConfiguration config;
    private File file;

    public SpigotConfiguration(JavaPlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Creates or loads the yml file in the plugin directory.
     *
     * @param name Is the name of the file
     */
    public void createDefaults(String name) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder().getPath() + "/" + name);

        if (!file.exists()) {
            InputStream inputStream = plugin.getResource(name);
            try {
                assert inputStream != null;
                Files.copy(inputStream, Paths.get(plugin.getDataFolder().getPath() + "/" + name), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ConfigUpdater.update(plugin, name, file, new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String getConverted(String path){
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(path)));
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    public void saveConfig(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
