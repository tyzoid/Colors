package com.tyzoid.plugins.colors.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.tyzoid.plugins.colors.Colors;
import com.tyzoid.plugins.colors.lib.perms.*;


public class Perms {
	private static Perms instance;
	private static Colors plugin;
	
	private Permissionsplugin permplugin = null;
	
	private Perms() {
		Plugin permissionsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Permissions");
		Plugin permissionsExPlugin = Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx");
		Plugin bPermissionsPlugin = Bukkit.getServer().getPluginManager().getPlugin("bPermissions");
		Plugin groupManagerPlugin = Bukkit.getServer().getPluginManager().getPlugin("GroupManager");
		
		permplugin = new Pex();
		if(!permplugin.loadPermissions(permissionsExPlugin)) permplugin = new bPerms(plugin);
		else if(!permplugin.loadPermissions(bPermissionsPlugin)) permplugin = new GMPerms(plugin);
		else if(!permplugin.loadPermissions(groupManagerPlugin)) permplugin = new NijPerms(plugin);
		else if(!permplugin.loadPermissions(permissionsPlugin)) permplugin = VaultHandler.getInstance(plugin);
	}
	
	public static Perms getInstance(Colors plugininstance) {
		plugin = plugininstance;
		if(instance == null) instance = new Perms();
		return instance;
	}
	
	public String getGroup(Player player) {
		return permplugin.getGroup(player);
	}
	
	public String[] getGroups(Player player){
		return permplugin.getGroups(player);
	}
	
	public String getPrefix(Player player){
		return permplugin.getPrefix(player);
	}
	
	public String getSuffix(Player player){
		return permplugin.getSuffix(player);
	}
}
