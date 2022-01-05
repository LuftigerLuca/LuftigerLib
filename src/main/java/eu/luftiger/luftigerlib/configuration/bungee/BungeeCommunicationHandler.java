package eu.luftiger.luftigerlib.configuration.bungee;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class is useful for the communication between plugins
 * @author LuftigerLuca
 * @version 1.0
 */
public abstract class BungeeCommunicationHandler implements Listener {

    protected final Plugin plugin;

    public BungeeCommunicationHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param event listens to the plugin-messages
     */
    @EventHandler
    public abstract void onPluginMessage(PluginMessageEvent event);

    /**
     * @param server is the server, that the message should be sent to
     * @param params are the parameters, which should be sent: "Subchanel", "Message"
     */
    public void send(ServerInfo server, Object... params) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            for (Object param : params) {
                if (param instanceof String) {
                    out.writeUTF((String) param);
                } else if (param instanceof Integer) {
                    out.writeInt((int) param);
                } else if (param instanceof Double) {
                    out.writeDouble((double) param);
                } else if (param instanceof Float) {
                    out.writeFloat((float) param);
                } else if (param instanceof Boolean) {
                    out.writeBoolean((boolean) param);
                } else if (param instanceof Short) {
                    out.writeShort((short) param);
                } else if (param instanceof Long) {
                    out.writeLong((long) param);
                } else if (param instanceof Byte) {
                    out.writeByte((byte) param);
                } else if (param instanceof Character) {
                    out.writeChar((char) param);
                }
            }

            plugin.getProxy().getScheduler().runAsync(plugin, () -> server.sendData("BungeeCord", b.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
