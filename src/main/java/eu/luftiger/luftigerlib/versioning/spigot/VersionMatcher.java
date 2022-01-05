package eu.luftiger.luftigerlib.versioning.spigot;
import org.bukkit.Bukkit;

/**
 * This class is useful to get a version matching class
 * @author LuftigerLuca
 * @version 1.0
 */
public class VersionMatcher {

    /**
     * @param pluginName is the name of the plugin
     * @param path is the package-path of the class: "eu.luftiger.test.v1_16_r3.Test"
     * @param className
     * @return the version matching class
     */
    public Object match(String pluginName, String path, String className) {
        final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        try {
            return Class.forName(path + ".v" + serverVersion + "." + className).newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(pluginName + " does not support server version \"" + serverVersion + "\"", exception);
        }
    }
}
