package com.tyzoid.plugins.colors.lib.perms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.tyzoid.plugins.colors.Colors;

public class NijPerms implements Permissionsplugin {
	private Colors plugin;
	private PermissionHandler permissionHandler;
	private boolean exists = false;
	private String pluginname;
	
	public NijPerms(Colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		if(exists == false) setupPermissions();
	}
	
	private void setupPermissions(){
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("Permissions");
		
		if(loadPermissions(perm)){
			System.out.println("[" + pluginname + "] Hooked into Permissions.");
		}
	}
	
	@Override
	public boolean loadPermissions(Plugin permPlugin){
		if (permissionHandler == null) {
			if (permPlugin != null) {
				exists = true;
				permissionHandler = ((Permissions) permPlugin).getHandler();
			}
		}
		return exists;
	}
	
	@Override
	public String getGroup(Player pl) {
		String[] groups = getGroups(pl);
		return (groups != null && groups.length > 0)? groups[0] : ((pl.isOp())? "Op" : "Default");
	}
	
	@Override
	public String[] getGroups(Player pl) {
		return permissionHandler.getGroups(pl.getWorld().getName(), pl.getName());
	}
	
	@Override
	public String getPrefix(Player pl) {
		return permissionHandler.getPermissionString(pl.getWorld().getName(), pl.getName(), "prefix");
	}
	
	@Override
	public String getSuffix(Player pl) {
		return permissionHandler.getPermissionString(pl.getWorld().getName(), pl.getName(), "suffix");
	}
	
}
