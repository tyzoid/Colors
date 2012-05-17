package tk.tyzoid.plugins.colors.lib;

import tk.tyzoid.plugins.colors.Colors;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

//bPermissions
import de.bananaco.bpermissions.api.Group;
import de.bananaco.bpermissions.api.User;
import de.bananaco.bpermissions.api.World;
import de.bananaco.bpermissions.api.WorldManager;

//permissions
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

//permissionsEx
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("unused")
public class Perms {
	Colors plugin;
	private PermissionHandler permissionHandler;
	private WorldManager bPermissionsHandler;
	private PermissionsEx permissionsExHandler;
	private GroupManager groupManagerHandler;
	
	private String pluginname;
	
	private boolean permissions = false;
	private boolean permissionsEx = false;
	private boolean bPermissions = false;
	private boolean groupManager = false;
	private boolean superperms = false;
	
	public Perms(Colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		setupPermissions();
	}
	
	private void setupPermissions() {
		Plugin permissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
		Plugin permissionsExPlugin = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
		Plugin bPermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("bPermissions");
		Plugin groupManagerPlugin = plugin.getServer().getPluginManager().getPlugin("GroupManager");
		
		if(loadPermissions(permissionsPlugin)){
			System.out.println("[" + pluginname + "] Hooked into Permissions.");
			permissions = true;
		} else if(loadPermissionsEx(permissionsExPlugin)){
			System.out.println("[" + pluginname + "] Hooked into PermissionsEX.");
			permissionsEx = true;
		} else if(loadBPermissions(bPermissionsPlugin)){
			System.out.println("[" + pluginname + "] Hooked into bPermissions.");
			bPermissions = true;
		} else if(loadGroupManager(groupManagerPlugin)){
			System.out.println("[" + pluginname + "] Hooked into GroupManager.");
			groupManager = true;
		} else {
			System.out.println("[" + pluginname + "] No group/permissions plugin found that " + pluginname + " supports. Using default groups.");
		}
		
		try{
			Permission fakePerm = new Permission("fake.perm");
			superperms = true;
		} catch(Exception e){
			//superperms doesn't exist. Is the user in the stone age?
		}
	}
	
	private boolean loadPermissions(Plugin permPlugin){
		boolean exists = false;
		if (permissionHandler == null) {
			if (permPlugin != null) {
				exists = true;
				permissionHandler = ((Permissions) permPlugin).getHandler();
			}
		}
		return exists;
	}
	
	private boolean loadPermissionsEx(Plugin permPlugin){
		boolean exists = false;
		if (permissionsExHandler == null) {
			if (permPlugin != null) {
				exists = true;
				permissionsExHandler = (PermissionsEx) permPlugin;
			}
		}
		return exists;
	}
	
	private boolean loadBPermissions(Plugin permPlugin){
		boolean exists = false;
		if(bPermissionsHandler == null){
			if(permPlugin != null){
				bPermissionsHandler = WorldManager.getInstance();
				exists = true;
			}
		}
		
		return exists;
	}
	
	private boolean loadGroupManager(Plugin permPlugin){
		boolean exists = false;
		if(groupManagerHandler == null){
			if(permPlugin != null){
				groupManagerHandler = (GroupManager) permPlugin;
				exists = true;
			}
		}
		
		return exists;
	}
	
	/* Valid nodes:
	 * colors.*
	 * colors.prefix
	 * colors.suffix
	 * colors.hex
	 * colors.rainbow
	 * colors.admin
	 */
	public boolean hasPermission(Player p, String node, boolean defaultValue){
		if(permissions){
			return permissionHandler.has(p, node);
		} else if(superperms) {
			return p.hasPermission(node);
		} else {
			return defaultValue;
		}
	}
	
	public String[] getGroups(Player player){
		String playerName = player.getName();
		String[] groups;
		if(permissions){
			groups = permissionHandler.getGroups(player.getWorld().getName(), playerName);
		}else if(permissionsEx){
			groups = PermissionsEx.getPermissionManager().getUser(playerName).getGroupsNames();
		} else if(bPermissions) {
			World w = bPermissionsHandler.getWorld(player.getWorld().getName());
			if (w == null) {
				return null;
			}
			User user = w.getUser(player.getName());
			groups = (user != null)? user.getGroupsAsString().toArray(new String[0]) : null;
		} else if(groupManager){
			AnjoPermissionsHandler handler;
			handler = groupManagerHandler.getWorldsHolder().getWorldPermissions(player);
			groups = handler.getGroups(playerName);
		} else {
			groups = null;
		}
		
		return groups;
	}
	
	public String getGroup(Player player){
		String[] groups = getGroups(player);
		return (groups != null && groups.length > 0)? groups[0] : ((player.isOp())? "Op" : "Default");
	}
	
	public String getPrefix(Player player){
		String prefix;
		String group = getGroup(player);
		String playername = player.getName();
		if(permissions){
			prefix = select(permissionHandler.getGroupPrefix(player.getWorld().getName(), getGroup(player)),
					permissionHandler.getPermissionString(player.getWorld().getName(), playername, "prefix"));
		} else if(permissionsEx){
			prefix = select(PermissionsEx.getPermissionManager().getGroup(group).getPrefix(player.getWorld().getName()),
					PermissionsEx.getPermissionManager().getUser(playername).getPrefix(player.getWorld().getName()));
		} else if(bPermissions) {
			prefix = null;
			
		} else if(groupManager){
			AnjoPermissionsHandler handler;
			handler = groupManagerHandler.getWorldsHolder().getWorldPermissions(player.getName());
			
			if(handler == null)
				prefix = null;
			else
				prefix = handler.getGroupPermissionString(getGroup(player), "prefix");
			
		} else {
			prefix = null;
		}
		
		return prefix;
	}
	
	private String select(String group, String player){
		String selected = (player == null) ? ((group==null) ? null:group):player;
		return selected;
	}
}
