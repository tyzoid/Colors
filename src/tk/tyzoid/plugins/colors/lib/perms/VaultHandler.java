package tk.tyzoid.plugins.colors.lib.perms;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import tk.tyzoid.plugins.colors.Colors;

/**
 * @author Tyzoid Singleton Class
 * 
 * This class should only be used if Colors does not find a permissions plugin
 * that it doesn't support natively, or for extra functionality (like prefixes/suffixes)
 * 
 * VaultHandler also returns defaults (null in most cases).
 * 
 * Currently, colors only supports bpermissions.
 */
public class VaultHandler implements Permissionsplugin {
	private static VaultHandler instance = null;
	private static Permission perms = null;
	public static Chat chat = null;
	
	private VaultHandler() {
		if(Bukkit.getPluginManager().getPlugin("Vault") != null){
			// Gets the permissions plugin from vault.
			RegisteredServiceProvider<Permission> prsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
			perms = prsp.getProvider();
			
			// Gets the chat plugin from vault.
			RegisteredServiceProvider<Chat> crsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
			chat = crsp.getProvider();
			
			//makes sure that Colors does not try to call itself. That would be very awkward.
			if(chat != null && chat.getName().equalsIgnoreCase(Colors.getInstance().getName())) chat = null;
		}
	}
	
	/**
	 * Singleton Class
	 */
	public static VaultHandler getInstance() {
		if(instance == null) instance = new VaultHandler();
		return instance;
	}
	
	@Override
	public String[] getGroups(Player player) {
		return (perms == null) ? null : perms.getPlayerGroups(player);
	}
	
	@Override
	public String getGroup(Player player) {
		String[] groups = getGroups(player);
		return (groups != null && groups.length > 0) ? groups[0] : ((player.isOp()) ? "Op" : "Default");
	}
	
	@Override
	public String getPrefix(Player player) {
		return (chat == null) ? null : chat.getPlayerPrefix(player);
	}
	
	@Override
	public String getSuffix(Player player) {
		return (chat == null) ? null : chat.getPlayerSuffix(player);
	}
	
	public boolean isChatFeaturesEnabled(){
		return (chat != null);
	}
	
	public boolean isPermissionsFeaturesEnabled(){
		return (perms != null);
	}

	@Override
	public boolean loadPermissions(Plugin permPlugin) {
		return this.isPermissionsFeaturesEnabled();
	}
}
