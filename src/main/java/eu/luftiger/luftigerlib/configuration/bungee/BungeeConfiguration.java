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

/**
 * This class is useful to create and manage {@link Configuration}
 * @author LuftigerLuca
 * @version 1.0
 */
public abstract class BungeeConfiguration {

    private final Plugin plugin;
    private Configuration configuration;
    private ConfigurationProvider configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private File file;

    protected BungeeConfiguration(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates or loads the yml file in the plugin directory.
     * @param name Is the name of the file
     * @param copyDefault Should the default config be copied?
     */
    public void createDefaults(String name, boolean copyDefault) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder().getPath() + "/" + name);

        if (!file.exists()) {
            if(copyDefault){
                InputStream inputStream = plugin.getResourceAsStream(name);
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
            //TODO: ConfigUpdater
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path is the path of the String to convert
     * @return colorcode converted String
     */
    public String getConverted(String path){
        return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
    }

    /**
     * @return the used {@link Configuration}
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @return the used {@link ConfigurationProvider}
     */
    public ConfigurationProvider getConfigurationProvider() {
        return configurationProvider;
    }

    /**
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
            configurationProvider.load(file);
            configurationProvider.save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
