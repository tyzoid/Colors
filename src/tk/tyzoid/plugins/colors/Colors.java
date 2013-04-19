package tk.tyzoid.plugins.colors;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import tk.tyzoid.plugins.colors.cache.CacheControl;
import tk.tyzoid.plugins.colors.lib.Metrics;

import tk.tyzoid.plugins.colors.lib.Names;
import tk.tyzoid.plugins.colors.lib.Perms;
import tk.tyzoid.plugins.colors.lib.SaveData;
import tk.tyzoid.plugins.colors.lib.Settings;
import tk.tyzoid.plugins.colors.listeners.ColorsPListener;

/**
 * Chat plugin for Bukkit
 * 
 * @author tyzoid
 */
public class Colors extends JavaPlugin {
	private static final boolean debug = false;
	public String pluginname = "Colors";
	public char rainbowColor = 'z';
	
	public Settings colorSettings = new Settings();
	private final ColorsPListener playerListener = new ColorsPListener(this);
	private final HashMap<Player, Boolean> colorify = new HashMap<Player, Boolean>();
	private Perms perms;
	public Names PSnames;
	public SaveData data;
	private static Colors instance;
	public final CacheControl c = new CacheControl();
	
	public void onDisable() {
		PSnames.pluginClosing(true);
		data.pluginClosing(true);
	}
	
	public void onEnable() {
		instance = this;
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :(
		}
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(playerListener, this);
		
		setupPermissions();
		
		PSnames = new Names(this);
		PSnames.loadNames();
		
		data = new SaveData(this);
		data.loadData();
		
		colorSettings.readSettings();
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
		perms = Perms.getInstance();
	}
	
	public Perms p(){
		return perms;
	}
	
	public boolean hasPermission(Player p, String node) {
		return p.hasPermission(node);
	}
	
	public boolean isChatColoring(final Player player) {
		if (colorify.containsKey(player)) {
			return colorify.get(player);
		} else {
			return false;
		}
	}
	
	public void toggleChatColoring(final Player player) {
		boolean value = !isChatColoring(player);
		if (value) {
			player.sendMessage("§1C§2o§3l§4o§5r§6s §fare now enabled");
		} else {
			player.sendMessage("§1C§2o§3l§4o§5r§6s §fare now disabled");
		}
		colorify.put(player, value);
	}
	
	public String colorsChat(String message){
		return colorsChat(message, new boolean[]{false, false, false, false, false})[0];
	}
	
	public String[] colorsChat(String message, boolean[] format) {
		char[] charMessage = message.toCharArray();
		String finalMessage = "";
		// k (scramble), l (bold), m (strike-through), n(underline), o (italic)
		int color = 1;
		for (int i = 0; i < charMessage.length; i++) {
			if ((i + 1) < charMessage.length && charMessage[i] == '§' && isColorNumber(charMessage[i + 1])) {
				i++;
				continue;
			}
			if ((i + 1) < charMessage.length && isColorChar(charMessage[i]) && isFormattingChar(charMessage[i + 1])) {
				String tmp = Character.toString(Character.toUpperCase(charMessage[i+1]));
				int index = formattingChars.valueOf(tmp).ordinal();
				format[index] = true;
				i++;
				continue;
			}
			finalMessage += "§" + Integer.toHexString(color);
			if (format[0]) {
				finalMessage += "§k";
			}
			if (format[1]) {
				finalMessage += "§l";
			}
			if (format[2]) {
				finalMessage += "§m";
			}
			if (format[3]) {
				finalMessage += "§n";
			}
			if (format[4]) {
				finalMessage += "§o";
			}
			finalMessage += charMessage[i];
			color++;
			if (color >= 16) {
				color = 1;
			}
		}
		
		return new String[]{
				finalMessage, 
				(format[0]?"0":"1"),
				(format[1]?"0":"1"),
				(format[2]?"0":"1"),
				(format[3]?"0":"1"),
				(format[4]?"0":"1")
		};
	}
	
	public String convertToColor(String withoutColor, boolean rainbowAllowed) {
		boolean[] format = {false, false, false, false, false};
		int count = withoutColor.length();
		char[] colorless = withoutColor.toCharArray();
		char rcc = Character.toLowerCase(rainbowColor); // rainbow color char
		String withColor = "";
		for (int i = 0; i < count; i++) {
			if ((i + 1) < count && isColorChar(colorless[i]) && isFormattingChar(colorless[i + 1])) {
				String tmp = Character.toString(Character.toUpperCase(colorless[i+1]));
				int index = formattingChars.valueOf(tmp).ordinal();
				if(index < 5)
					format[index] = true;
				else
					format = new boolean[]{false, false, false, false, false};
			}
			if ((isColorChar(colorless[i])) && (i + 1) < count) {
				if (isColorNumber(colorless[i + 1]) || isFormattingChar(colorless[i + 1])) {
					withColor += "§";
				} else if (rainbowAllowed && Character.toLowerCase(colorless[i + 1]) == rcc) {
					boolean found = false;
					int indexOfColorChar = i + 2;
					String rainbowString = new String(colorless);
					
					while (indexOfColorChar < count && !found) {
						found = (isColorChar(colorless[indexOfColorChar]) &&
								(isColorNumber(colorless[indexOfColorChar + 1]) ||
										Character.toLowerCase(colorless[indexOfColorChar + 1]) == rcc ||
										Character.toLowerCase(colorless[indexOfColorChar + 1]) == 'r'));
						indexOfColorChar++;
					}
					
					if (found) {
						indexOfColorChar--;
					}
					String[] result = colorsChat(rainbowString.substring(i + 2, indexOfColorChar), format);
					rainbowString = result[0];
					for(int j = 1; j < result.length; j++){
						format[j-1] = result[j].equalsIgnoreCase("1");
					}
					withColor += rainbowString;
					
					i = indexOfColorChar - 1;
					
				} else {
					withColor += colorless[i];
				}
			} else {
				withColor += colorless[i];
			}
		}
		return withColor;
	}
	
	public boolean isColorChar(char c) {
		String[] chars = colorSettings.getProperty("color-chars").split(",");
		boolean charUsed = false;
		for (int i = 0; (i < chars.length && !charUsed); i++) {
			if (c == chars[i].toCharArray()[0]) {
				charUsed = true;
			}
		}
		return charUsed;
	}
	
	public boolean isColorNumber(char c) {
		c = Character.toLowerCase(c);
		return ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9') || (c == 'a') || (c == 'b') || (c == 'c') || (c == 'd') || (c == 'e') || (c == 'f'));
	}
	
	public boolean isFormattingChar(char c) {
		c = Character.toLowerCase(c);
		return ((c == 'k') || (c == 'l') || (c == 'm') || (c == 'n') || (c == 'o') || (c == 'r'));
	}
	
	private enum formattingChars {
		K, L, M, N, O, R
	}
	
	public static Colors getInstance(){
		return instance;
	}
	
	public static boolean isDebugging(){
		return debug;
	}
}