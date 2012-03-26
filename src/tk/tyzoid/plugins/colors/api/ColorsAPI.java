package tk.tyzoid.plugins.colors.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tk.tyzoid.plugins.colors.Colors;

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
	    	char[] charMessage = message.toCharArray();
	    	String finalMessage ="";
	    	int color = 1;
	    	for(int i = 0; i < charMessage.length; i++){
	    		if((i-1) < charMessage.length && charMessage[i] == '§' && isColorNumber(charMessage[i+1])){
	    			i++;
	    			continue;
	    		}
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
	    	char rcc = ' '; //rainbow color char
	    	String withColor = "";
	    	for(int i = 0; i < count; i++){
	    		if(isColorChar(colorless[i]) && (i+1) < count){
	    			if(isColorNumber(colorless[i+1])){
	    				withColor += "§";
	    			} else if(rainbowAllowed && Character.toLowerCase(colorless[i+1]) == rcc){
	    				boolean found = false;
	    				int indexOfColorChar = i+2;
	    				String rainbowString = new String(colorless);
	    				
	    				while(indexOfColorChar < count && !found){
	    					found = isColorChar(colorless[indexOfColorChar]) && (isColorNumber(colorless[indexOfColorChar+1]) || Character.toLowerCase(colorless[indexOfColorChar+1]) == rcc);
	    					indexOfColorChar++;
	    				}
	    				
	    				if(found){
	    					indexOfColorChar--;
	    				}
	    				
	    				rainbowString = rainbow(rainbowString.substring(i+2, indexOfColorChar));
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
	    	String[] chars = plugin.colorSettings.getProperty("color-chars").split(",");
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
	    	return ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9') || (c == 'a') || (c == 'b') || (c == 'c') || (c == 'd') || (c == 'e') || (c == 'f') || (c == 'k') || (c == 'l') || (c == 'm') || (c == 'n') || (c == 'o') || (c == 'r'));
	    }
}
