package tk.tyzoid.plugins.colors.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.bukkit.entity.Player;

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
			if (!players.exists()) {
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
	
	public void pluginClosing(boolean show) {
		if (show) System.out.println("[" + pluginname + "] Saving other data.");
		try {
			playerOut = new FileOutputStream(players);
			
			user.store(playerOut, comment);
			
			playerOut.close();
		} catch (Exception e) {
			System.out.println("[" + pluginname + "] Could not save other data.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
	
	public char getColorLock(Player player){
		String tmp = user.getProperty(player.getName().toLowerCase(), "0");
		return tmp.charAt(0);
	}
	
	public void setColorLock(Player player, char c){
		user.setProperty(player.getName().toLowerCase(), Character.toString(c));
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
}
