package com.tyzoid.plugins.colors.lib.perms;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.tyzoid.plugins.colors.Colors;


public class GMPerms implements Permissionsplugin {
	private Colors plugin;
	private String pluginname;
	private boolean exists = false;
	private GroupManager groupManagerHandler;
	
	public GMPerms(Colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		if(exists == false) setupPermissions();
	}
	
	private void setupPermissions(){
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("GroupManager");
		
		if(loadPermissions(perm)){
			System.out.println("[" + pluginname + "] Hooked into GroupManager.");
		}
	}
	
	@Override
	public boolean loadPermissions(Plugin permPlugin) {
		if(groupManagerHandler == null){
			if(permPlugin != null && permPlugin instanceof GroupManager){
				groupManagerHandler = (GroupManager) permPlugin;
				exists = true;
			}
		}
		
		return exists;
	}
	
	@Override
	public String[] getGroups(Player pl) {
		AnjoPermissionsHandler handler = groupManagerHandler.getWorldsHolder().getWorldPermissions(pl);
		return handler.getGroups(pl.getName());
	}
	
	@Override
	public String getGroup(Player pl) {
		String[] groups = getGroups(pl);
		return (groups != null && groups.length > 0) ? groups[0] : ((pl.isOp()) ? "Op" : "Default");
	}
	
	@Override
	public String getPrefix(Player pl) {
		AnjoPermissionsHandler handler = groupManagerHandler.getWorldsHolder().getWorldPermissions(pl);
		return handler.getUserPrefix(pl.getName());
	}
	
	@Override
	public String getSuffix(Player pl) {
		AnjoPermissionsHandler handler = groupManagerHandler.getWorldsHolder().getWorldPermissions(pl);
		return handler.getUserSuffix(pl.getName());
	}
}
