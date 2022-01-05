package eu.luftiger.luftigerlib.configuration.spigot;

import com.google.common.collect.Iterables;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class is useful for the communication between plugins
 * @author LuftigerLuca
 * @version 1.0
 */
public abstract class SpigotCommunicationHandler implements PluginMessageListener {

    protected final JavaPlugin plugin;

    protected SpigotCommunicationHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @param channel is the channel in which the channel gets send
     * @param subChannel is the subchannel of the message
     * @param message is the message which should be sent
     * @return the success of this method
     */
    public boolean sendPluginMessage(String channel, String subChannel, String message){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(subChannel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(plugin.getServer().getOfflinePlayers().length != 0){
            Iterables.getFirst(plugin.getServer().getOnlinePlayers(), null).sendPluginMessage(plugin, channel, stream.toByteArray());
            return true;
        }

        return false;
    }
}
