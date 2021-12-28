package eu.luftiger.luftigerlib.versioning.spigot;
import org.bukkit.Bukkit;

public class VersionMatcher {

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
