package com.tyzoid.plugins.colors.lib.perms;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.tyzoid.plugins.colors.Colors;


import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.api.ApiLayer;

@SuppressWarnings("deprecation")
public class bPerms implements Permissionsplugin {
	private Colors plugin;
	private String pluginname;
	private boolean exists = false;
	
	public bPerms(Colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		if(exists == false) setupPermissions();
	}
	
	private void setupPermissions(){
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("bPermissions");
		
		if(loadPermissions(perm)){
			System.out.println("[" + pluginname + "] Hooked into bPermissions.");
		}
	}
	
	@Override
	public boolean loadPermissions(Plugin permPlugin) {
		if(permPlugin != null){
			exists = true;
		}
		return exists;
	}
	
	@Override
	public String[] getGroups(Player pl) {
		// Split to help identify error.
		World w = pl.getWorld();
		String wnam = w.getName();
		return ApiLayer.getGroups(
				wnam,
				CalculableType.USER,
				pl.getName()
				);
	}

	@Override
	public String getGroup(Player pl) {
		String[] groups = getGroups(pl);
		return (groups != null && groups.length > 0)? groups[0] : ((pl.isOp())? "Op" : "Default");
	}
	
	@Override
	public String getPrefix(Player pl) {
		return ApiLayer.getValue(pl.getWorld().getName(), CalculableType.USER, pl.getName(), "prefix");
	}
	
	@Override
	public String getSuffix(Player pl) {
		return ApiLayer.getValue(pl.getWorld().getName(), CalculableType.USER, pl.getName(), "suffix");
	}
}
