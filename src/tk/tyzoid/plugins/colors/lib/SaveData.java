package tk.tyzoid.plugins.colors.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import tk.tyzoid.plugins.colors.Colors;

//import org.bukkit.entity.Player;

public class SaveData {
	Colors plugin;
	
	private Properties user = new Properties();
	
	private String pluginname;
	private String comment;
	
	private File players;
	private FileOutputStream playerOut;
	
	public SaveData(Colors instance) {
		plugin = instance;
		
		pluginname = plugin.pluginname;
		comment = "[" + pluginname + "] Other assorted data";
	}
	
	public void loadData() {
		try {
			String path = "plugins/" + pluginname;
			players = new File(path + "/data.list");
			if(!players.exists()) {
				(new File(path)).mkdir();
				players.createNewFile();
			}
			
			FileInputStream playerSP = new FileInputStream(players);
			
			user.load(playerSP);
			
			playerSP.close();
			
		} catch (Exception e) {
			System.out.println("[" + pluginname + "] Failed to load fother data. Aborting.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
	
	/**
	 * Gets the color lock for a whole group (player colorlocks override this)
	 * 
	 * @param player
	 * @return
	 */
	public char getGroupColorLock(String group) {
		String tmp = user.getProperty("-gcl-" + group, "-");
		return tmp.charAt(0);
	}
	
	/**
	 * Sets the color lock for a whole group (player colorlocks override this)
	 * 
	 * @param group
	 * @param c
	 */
	public void setGroupColorLock(String group, char c) {
		user.setProperty("-gcl-" + group, Character.toString(c));
		saveData();
	}
	
	/**
	 * Gets the color lock for a player (overrides groups while set)
	 * 
	 * @param player
	 * @return
	 */
	public char getPlayerColorLock(String player) {
		String tmp = user.getProperty(player.toLowerCase());
		if(tmp == null) return user.getProperty("-pcl-" + player.toLowerCase(), "-").charAt(0);
		if(tmp != null) {
			user.remove(player.toLowerCase());
			user.setProperty("-pcl-" + player.toLowerCase(), tmp.substring(0, 1));
			saveData();
		}
		return tmp.charAt(0);
	}
	
	/**
	 * Sets the color lock for a player (overrides groups while set)
	 * 
	 * @param player
	 * @param c
	 */
	public void setPlayerColorLock(String player, char c) {
		user.setProperty("-pcl-" + player.toLowerCase(), Character.toString(c));
		saveData();
	}
	
	public boolean reloadData() {
		boolean result = false;
		
		try {
			FileInputStream playerSP = new FileInputStream(players);
			
			user.load(playerSP);
			
			playerSP.close();
			result = true;
		} catch (Exception e) {
			result = false;
		}
		
		return result;
	}
	
	public void saveData() {
		pluginClosing(false);
	}
	
	public void pluginClosing(boolean show) {
		if(show) System.out.println("[" + pluginname + "] Saving other data.");
		try {
			playerOut = new FileOutputStream(players);
			
			user.store(playerOut, comment);
			
			playerOut.close();
		} catch (Exception e) {
			System.out.println("[" + pluginname + "] Could not save other data.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
}
