package com.tyzoid.plugins.colors.lib.perms;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import com.tyzoid.plugins.colors.Colors;

public class PBukkitPerms implements Permissionsplugin {
	private PermissionsPlugin perms;
	private Colors plugin;
	private String pluginname;
	private boolean exists = false;
	
	public PBukkitPerms(Colors instance){
		plugin = instance;
		pluginname = plugin.pluginname;
		if(exists == false) setupPermissions();
	}
	
	private void setupPermissions() {
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit");
		
		if(loadPermissions(perm)) {
			System.out.println("[" + pluginname + "] Hooked into PermissionsBukkit.");
		}
	}
	
	@Override
	public boolean loadPermissions(Plugin permPlugin) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String[] getGroups(Player pl) {
		if(perms.getPlayerInfo(pl.getName()) == null) return new String[] { "" };
		List<Group> groups = perms.getPlayerInfo(pl.getName()).getGroups();
		String[] groupsarr = new String[groups.size()];
		
		int i = 0;
		for(Group g : groups) {
			groupsarr[i] = g.getName();
			i++;
		}
		
		return groupsarr;
	}
	
	@Override
	public String getGroup(Player pl) {
		return (perms.getPlayerInfo(pl.getName()) == null || perms.getPlayerInfo(pl.getName()).getGroups().size() == 0) ?
				((pl.isOp()) ? "Op" : "Default") : perms.getPlayerInfo(pl.getName()).getGroups().get(0).getName();
	}
	
	@Override
	public String getPrefix(Player pl) {
		// PermissionsBukkit does not support prefixes
		return null;
	}
	
	@Override
	public String getSuffix(Player pl) {
		// PermissionsBukkit does not support suffixes
		return null;
	}
}
