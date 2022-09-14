package eu.luftiger.luftigerlib.configuration.spigot;

import net.md_5.bungee.api.ChatColor;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class is useful to create and manage {@link YamlConfiguration}
 * @author LuftigerLuca
 * @version 1.2
 */

public abstract class SpigotConfiguration {

    private final JavaPlugin plugin;
    private YamlConfiguration config;
    private File file;

    public SpigotConfiguration(JavaPlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Creates or loads the yml file in the plugin directory.
     * @param name Is the name of the file
     * @param copyDefault Should the default config be copied?
     * @param updateConfig Should the config be updated?
     */
    public void createDefaults(String name, boolean copyDefault, boolean updateConfig) {
        createDefaults(name, copyDefault, updateConfig, plugin.getDataFolder().getPath());
    }

    /**
     * Creates or loads the yml file in the plugin directory.
     * @param name Is the name of the file
     * @param copyDefault Should the default config be copied?
     * @param updateConfig Should the config be updated?
     * @param path Is the path of the file
     */
    public void createDefaults(String name, boolean copyDefault, boolean updateConfig, String path) {
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        this.file = new File(path + "/" + name);

        if (!file.exists()) {
            if(copyDefault){
                InputStream inputStream = plugin.getResource(name);
                try {
                    assert inputStream != null;
                    Files.copy(inputStream, Paths.get(plugin.getDataFolder().getPath() + "/" + name), new CopyOption[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(updateConfig){
                try {
                    ConfigUpdater.update(plugin, name, file, new ArrayList<>());
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * @param path is the path of the String to convert
     * @return colorcode converted String
     */
    public String getConverted(String path){
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(translateHEXCodes(config.getString(path))));
    }

    /**
     *
     * @param string is the string in which the hexcodes are to be translated
     * @return hexxcode converted String
     */
    public String translateHEXCodes(String string){
        Matcher matcher = Pattern.compile("<.*?>").matcher(string);
        while (matcher.find()) {
            String replace = matcher.group();
            string = string.replace(replace, ChatColor.of(replace.replace("<", "").replace(">", "")) + "");
        }
        return string;
    }

    /**
     * @return the used {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     *
     * @return the used {@link File}
     */
    public File getFile() {
        return file;
    }

    /**
     * saves the configuration
     */
    public void saveConfig(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
