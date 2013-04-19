package tk.tyzoid.plugins.colors.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCache {
	/* LOCKS FOR THREAD SAFETY */
	//private Object a = new Object();
	
	/* Variables */
	private String playername;
	private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<String, Boolean>(15,.9f,1);
	
	public PlayerCache(Player player){
		this.playername = player.getName();
		
		for(String node : CacheControl.nodesToCache()){
			this.cache.put(node, player.hasPermission(node));
		}
	}
	
	/*  */
	public String getName(){
		return this.playername;
	}
	
	public boolean hasPermission(String node){
		return this.cache.get(node);
	}
	
	protected void updateCache(){
		Player player = Bukkit.getPlayer(this.playername);
		
		for(String node : CacheControl.nodesToCache()){
			this.cache.put(node, player.hasPermission(node));
		}
	}
}
