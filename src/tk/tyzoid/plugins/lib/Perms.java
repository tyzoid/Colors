package tk.tyzoid.plugins.lib;

import tk.tyzoid.plugins.colors.colors;

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
	colors plugin;
    private PermissionHandler permissionHandler;
    private WorldManager bPermissionsHandler;
    private PermissionsEx permissionsExHandler;
    
    public String pluginname;

    public boolean permissions = false;
    public boolean permissionsEx = false;
    public boolean bPermissions = false;
    public boolean superperms = false;
    
	public Perms(colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		setupPermissions();
	}
	
	private void setupPermissions() {
        Plugin permissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
        Plugin permissionsExPlugin = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
        Plugin bPermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("bPermissions");
        
        if(loadPermissions(permissionsPlugin)){
        	System.out.println("[" + "pluginname" + "] Using Permissions for permissions.");
        	permissions = true;
        } else if(loadPermissionsEx(permissionsExPlugin)){
        	System.out.println("[" + "pluginname" + "] Using PermissionsEX for permissions.");
        	permissionsEx = true;
        } else if(loadBPermissions(bPermissionsPlugin)){
        	System.out.println("[" + "pluginname" + "] Using bPermissions for permissions.");
        	bPermissions = true;
        } else {
        	
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
	
	public boolean loadPermissionsEx(Plugin permPlugin){
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
    
    public String getGroup(Player player){
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
    	} else {
    		groups = null;
    	}
    	
    	return (groups != null && groups.length > 1)? groups[0] : ((player.isOp())? "Op" : "Default");
    }
}
