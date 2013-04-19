package tk.tyzoid.plugins.colors.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tk.tyzoid.plugins.colors.Colors;

public class CacheControl {
	private final ConcurrentHashMap<String, PlayerCache> players = new ConcurrentHashMap<String, PlayerCache>(8,.9f,1);
	
	private final static String[] cachenodes = {
		"colors.rainbow",
		"colors.hex",
		"colors.admin",
		"colors.prefix",
		"colors.suffix",
		"colors.reload",
		"colors.colorLock"
	};
	

	public void init(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Colors.getInstance(), new Runnable(){
			public void run(){
				for(PlayerCache player : players.values()){
					player.updateCache();
				}
			}
		}, 0, 20);
	}
	
	public PlayerCache getPlayer(String playername){
		return players.get(playername);
	}
	
	public PlayerCache getPlayer(Player player){
		return players.get(player.getName());
	}
	
	public PlayerCache addPlayer(Player player){
		PlayerCache pl = new PlayerCache(player);
		players.put(pl.getName(), pl);
		return pl;
	}
	
	public boolean removePlayer(Player player){
		if(players.containsKey(player.getName())){
			players.remove(player.getName());
			return true;
		}
		return false;
	}
	
	public static String[] nodesToCache(){
		return cachenodes;
	}
}
