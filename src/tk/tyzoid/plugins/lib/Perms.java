package tk.tyzoid.plugins.lib;

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

import tk.tyzoid.plugins.colors.colors;

@SuppressWarnings("unused")
public class Perms {
	colors plugin;
    private PermissionHandler permissionHandler;
    private WorldManager bPermissionsHandler;
    
    public String pluginname;

    public boolean permissionsExists = false;
    public boolean useSuperperms = false;
    
	public Perms(colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		setupPermissions();
	}
	
	private void setupPermissions() {
        Plugin permissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
            	permissionsExists = true;
                permissionHandler = ((Permissions) permissionsPlugin).getHandler();
                System.out.println("[" + pluginname + "] Permissions found!");
            } else {
                System.out.println("[" + pluginname + "] Permissions not detected. Using defaults.");
                permissionsExists = false;
                
            }
        }
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
    	if(permissionsExists){
    		return permissionHandler.has(p, node);
    	} else if(useSuperperms) {
    		return p.hasPermission(node);
    	} else {
    		return defaultValue;
    	}
    }
    
    public String getGroup(){
    	
    	return "";
    }
}
