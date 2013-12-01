package com.tyzoid.plugins.colors.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.tyzoid.plugins.colors.Colors;


public class ColorsAPI {
	private Colors plugin = null;
	private static ColorsAPI instance = null;
	
	private ColorsAPI(){
		plugin = (Colors) Bukkit.getServer().getPluginManager().getPlugin("Colors");
	}
	
	public static ColorsAPI getInstance(){
		if(instance==null)
			instance = new ColorsAPI();
		
		return instance;
	}
	
	public String getPrefix(Player player){
		return plugin.PSnames.getUserPrefix(player.getName())==null ? "":plugin.PSnames.getUserPrefix(player.getName());
	}
	
	public String getSuffix(Player player){
		return plugin.PSnames.getUserSuffix(player.getName())==null ? "":plugin.PSnames.getUserSuffix(player.getName());
	}
	
	public String getGroupPrefix(String groupname){
		return plugin.PSnames.getGroupPrefix(groupname)==null ? "":plugin.PSnames.getGroupPrefix(groupname);
	}
	
	public String getGroupSuffix(String groupname){
		return plugin.PSnames.getGroupSuffix(groupname)==null ? "":plugin.PSnames.getGroupSuffix(groupname);
	}
	
	public String rainbow(String message){
		return plugin.lib.colorsChat(message);
	}
	
	public String convertToColor(String withoutColor, boolean rainbowAllowed){
		return plugin.lib.convertToColor(withoutColor, rainbowAllowed);
	}
	
	public boolean isColorChar(char c){
		return plugin.lib.isColorChar(c);
	}
	
	public boolean isColorNumber(char c){
		return plugin.lib.isColorNumber(c);
	}
}