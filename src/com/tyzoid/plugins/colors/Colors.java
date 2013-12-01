package com.tyzoid.plugins.colors;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.tyzoid.plugins.colors.cache.CacheControl;
import com.tyzoid.plugins.colors.lib.ColorChars;
import com.tyzoid.plugins.colors.lib.Metrics;
import com.tyzoid.plugins.colors.lib.Names;
import com.tyzoid.plugins.colors.lib.Perms;
import com.tyzoid.plugins.colors.lib.SaveData;
import com.tyzoid.plugins.colors.lib.Settings;
import com.tyzoid.plugins.colors.listeners.ColorsPListener;



/**
 * Chat plugin for Bukkit
 * 
 * @author tyzoid
 */
public class Colors extends JavaPlugin {
	private final boolean debug = false;
	public String pluginname = "Colors";
	public char rainbowColor = 'z';
	
	public Settings colorSettings = new Settings();
	private final ColorsPListener playerListener = new ColorsPListener(this);
	private Perms perms;
	public Names PSnames;
	public SaveData data;
	public final CacheControl c = new CacheControl(this);
	
	public final ColorChars lib = new ColorChars(this);
	
	public Colors(){
		PSnames = new Names(this);
		PSnames.loadNames();
		
		data = new SaveData(this);
		data.loadData();
		
		colorSettings.readSettings();
	}
	
	public void onDisable() {
		PSnames.pluginClosing(true);
		data.pluginClosing(true);
	}
	
	public void onEnable() {
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :(
		}
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(playerListener, this);
		
		setupPermissions();
		playerListener.plugin_init();
		c.init();
		
		if(this.getServer().getOnlinePlayers().length > 0){
			System.out.println("[" + pluginname + "] ----------------------------------");
			System.out.println("[" + pluginname + "] YOUR SERVER IS RELOADING.");
			System.out.println("[" + pluginname + "] THIS MAY CAUSE PLUGIN ISSUES.");
			System.out.println("[" + pluginname + "] RESTART IF YOU ENCOUNTER ANY.");
			System.out.println("[" + pluginname + "] ----------------------------------");
			Player[] players = this.getServer().getOnlinePlayers();
			for(Player player : players){
				playerListener.onPlayerJoin(new PlayerJoinEvent(player, ""));
			}
		}
	}
	
	private void setupPermissions() {
		perms = Perms.getInstance(this);
	}
	
	public Perms p(){
		return perms;
	}
	
	public boolean hasPermission(Player p, String node) {
		return p.hasPermission(node);
	}
	
	
	public boolean isDebugging(){
		return debug;
	}
}