package tk.tyzoid.plugins.colors;

import java.util.HashMap;

//permissions
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import org.bukkit.permissions.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event.Priority;


/**
 * Message plugin for Bukkit
 *
 * @author tyzoid
 */
public class colors extends JavaPlugin {
	String pluginname = "colors";
	
    private final colorsPListener playerListener = new colorsPListener(this);
    private final HashMap<Player, Boolean> colorify = new HashMap<Player, Boolean>();
    public settings colorSettings = new settings();
    public PermissionHandler permissionHandler;
    public boolean permissionsExists = false;
    public boolean useSuperperms = false;

    public void onDisable() {
        System.out.println("[" + pluginname +"] " + pluginname + " is closing...");
        playerListener.savePSNames();
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pluginname + "] Starting " + pluginname + " v" + pdfFile.getVersion() + "...");
        setupPermissions();
        colorSettings.readSettings();
    }
    
    private void setupPermissions() {
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
            	permissionsExists = true;
                permissionHandler = ((Permissions) permissionsPlugin).getHandler();
                System.out.println("[" + pluginname + "] Permissions found!");
            } else {
                System.out.println("[" + pluginname + "] Permissions not detected. Using defaults.");
                permissionsExists = false;
                
                try{
                	@SuppressWarnings("unused")
					Permission fakePerm = new Permission("fake.perm");
                	useSuperperms = true;
                } catch(Exception e){
                	//superperms doesn't exist
                }
            }
        }
    }
    
    /* Valid nodes:
     * colors.*
     * colors.prefix
     * colors.suffix
     * colors.hex
     * colors.rainbow
     * colors.admin
     */
    public boolean hasPermission(Player p, String node){
    	if(!useSuperperms){
    		return permissionHandler.has(p, node);
    	} else {
    		return p.hasPermission(node);
    	}
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
    	if(value){
    		player.sendMessage("§1C§2o§3l§4o§5r§6s §fare now enabled");
    	} else {
    		player.sendMessage("§1C§2o§3l§4o§5r§6s §fare now disabled");
    	}
        colorify.put(player, value);
    }
    
    public String colorsChat(String message){
    	char[] charMessage = message.toCharArray();
    	String finalMessage ="";
    	int color = 1;
    	for(int i = 0; i < charMessage.length; i++){
    		finalMessage += "§" + Integer.toHexString(color);
    		finalMessage += charMessage[i];
    		color++;
    		if(color >= 16){
    			color = 1;
    		}
    	}
    	
    	return finalMessage;
    }
    
    public String convertToColor(String withoutColor, boolean rainbowAllowed){
    	int count = withoutColor.length();
    	char[] colorless = withoutColor.toCharArray();
    	//char[] colored = withoutColor.toCharArray();
    	String withColor = "";
    	for(int i = 0; i < count; i++){
    		if(isColorChar(colorless[i]) && (i+1) < count){
    			if(isColorNumber(colorless[i+1])){
    				withColor += "§";
    			} else if(Character.toLowerCase(colorless[i+1]) == 'r' && rainbowAllowed){
    				boolean found = false;
    				int indexOfColorChar = i+2;
    				String rainbowString = new String(colorless);
    				
    				while(indexOfColorChar < count && !found){
    					found = isColorChar(colorless[indexOfColorChar]) && (isColorNumber(colorless[indexOfColorChar+1]) || Character.toLowerCase(colorless[indexOfColorChar+1]) == 'r');
    					indexOfColorChar++;
    				}
    				
    				indexOfColorChar--;
    				
    				rainbowString = colorsChat(rainbowString.substring(i+2, indexOfColorChar));
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
    
    public boolean isColorChar(char c){
    	String[] chars = colorSettings.getProperty("color-chars").split(",");
    	boolean charUsed = false;
		for(int i = 0; (i < chars.length && !charUsed); i++){
			if(c == chars[i].toCharArray()[0]){
				charUsed = true;
			}
		}
    	return charUsed;
    }
    
    public boolean isColorNumber(char c){
    	c = Character.toLowerCase(c);
    	return ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9') || (c == 'a') || (c == 'b') || (c == 'c') || (c == 'd') || (c == 'e') || (c == 'f'));
    }
}