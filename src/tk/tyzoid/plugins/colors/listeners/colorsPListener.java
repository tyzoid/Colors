package tk.tyzoid.plugins.colors.listeners;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import tk.tyzoid.plugins.colors.Colors;


public class colorsPListener implements Listener {
	private final HashMap<Player, String> names = new HashMap<Player, String>();
	private final HashMap<Player, Character> colorLocks = new HashMap<Player, Character>();
    private final Colors plugin;
	@SuppressWarnings("unused")
	private String pluginname;
	private String dcc; //the default color char - usually "&"
	private String regcc; //all color chars
	private char rcc; //rainbow color char
	private char crcc; //capitol rainbow color char

    public colorsPListener(Colors instance) {
        plugin = instance;
        pluginname = plugin.pluginname;
        rcc = Character.toLowerCase(plugin.rainbowColor);
        crcc = Character.toUpperCase(plugin.rainbowColor);
    }
    
    public void plugin_init(){
    	String[] colorchars = plugin.colorSettings.getProperty("color-chars").split(",");
    	dcc = colorchars[0];
    	regcc = "[\\Q";
    	for(int i = 0; i < colorchars.length; i++){
    		regcc += colorchars[i];
    	}
    	regcc += "\\E]";
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	String[] split = event.getMessage().split(" ");
    	String mess = event.getMessage();
		Player player = event.getPlayer();
		
		//The /colors command
		String[] rainbowCommands = plugin.colorSettings.getProperty("commands").split(",");
		boolean useColors = plugin.colorSettings.getProperty("use-rainbow").equalsIgnoreCase("true");
		boolean rainbowCommandUsed = false;
		
		if(plugin.hasPermission(player, "colors.rainbow", player.isOp()) || player.isOp()){
			for(int i = 0; (i < rainbowCommands.length && !rainbowCommandUsed) && useColors; i++){
				if(split[0].equalsIgnoreCase(rainbowCommands[i])){
					rainbowCommandUsed = true;
				}
			}
		} else {
			boolean usedCommand = false;
			for(int i = 0; (i < rainbowCommands.length && !usedCommand) && useColors; i++){
				if(split[0].equalsIgnoreCase(rainbowCommands[i])){
					usedCommand = true;
				}
			}
			if(usedCommand){
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §cYou do not have permissions to use that command.");
			}
		}
		
		if (rainbowCommandUsed) {
			if(split.length > 1){
				String message = "";
				for(int i = 1; i < (split.length); i++){
					if(i > 1){
						message += " ";
					}
					message += split[i];
				}
				
				player.chat(plugin.colorsChat(message));
			} else {
				plugin.toggleChatColoring(player);
			}
			event.setCancelled(true);
		}
		
		//the /ca command
		//Finished Code
		String[] adminCommands = plugin.colorSettings.getProperty("admin-commands").split(",");
		boolean adminCommandUsed = false;
		boolean useAdminCommands = plugin.colorSettings.getProperty("use-admin").equalsIgnoreCase("true");
		if(plugin.hasPermission(player, "colors.admin", player.isOp()) || player.isOp()){
			for(int i = 0; (i < adminCommands.length && !adminCommandUsed) && useAdminCommands; i++){
				if(split[0].equalsIgnoreCase(adminCommands[i])){
					adminCommandUsed = true;
				}
			}
		}
		
		if(adminCommandUsed){
			//boolean somewhere = false;
			if(split.length > 1 && !split[1].equalsIgnoreCase("?")){
				//regular command
				if(split[1].equalsIgnoreCase("set") && split.length > 2){
					if(split[2].equalsIgnoreCase("prefix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 5){
							int start = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length() + 5;
							String PSset = mess.substring(start, mess.length());
							plugin.PSnames.setUserPrefix(split[4], PSset);
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aUser prefix has been set.");
							resetIxes(plugin.getServer().getPlayer(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 5){
							int start = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length() + 5;
							String PSset = mess.substring(start, mess.length());
							plugin.PSnames.setGroupPrefix(split[4], PSset);
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aGroup prefix has been set.");
							
							Object[] str = names.values().toArray();
							for(int i = 0; i < names.size(); i++){
								if(getGroup(plugin.getServer().getPlayer((String)str[i])).equalsIgnoreCase(split[4])){
									resetIxes(plugin.getServer().getPlayer((String)str[i]));
								}
							}
						} else {
							showHelpMenu(player);
						}
					} else if(split[2].equalsIgnoreCase("suffix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 5){
							int start = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length() + 5;
							String PSset = mess.substring(start, mess.length());
							plugin.PSnames.setUserSuffix(split[4], PSset);
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aUser suffix has been set.");
							resetIxes(plugin.getServer().getPlayer(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 5){
							int start = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length() + 5;
							String PSset = mess.substring(start, mess.length());
							plugin.PSnames.setGroupSuffix(split[4], PSset);
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aGroup suffix has been set.");
							
							Object[] str = names.values().toArray();
							for(int i = 0; i < names.size(); i++){
								if(getGroup(plugin.getServer().getPlayer((String)str[i])).equalsIgnoreCase(split[4])){
									resetIxes(plugin.getServer().getPlayer((String)str[i]));
								}
							}
						} else {
							showHelpMenu(player);
						}
					} else {
						showHelpMenu(player);
					}
				} else if(split[1].equalsIgnoreCase("get") && split.length > 2){
					if(split[2].equalsIgnoreCase("prefix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] " + split[4] + "'s prefix is: " + plugin.PSnames.getUserPrefix(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] The group " + split[4] + " has the prefix: " + plugin.PSnames.getGroupSuffix(getGroup(plugin.getServer().getPlayer(split[4]))));
						} else {
							showHelpMenu(player);
						}
					} else if(split[2].equalsIgnoreCase("suffix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] " + split[4] + "'s suffix is: " + plugin.PSnames.getUserSuffix(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] " + split[4] + "'s suffix is: " + plugin.PSnames.getGroupSuffix(getGroup(plugin.getServer().getPlayer(split[4]))));
						} else {
							showHelpMenu(player);
						}
					} else {
						showHelpMenu(player);
					}
				} else if((split[1].equalsIgnoreCase("remove") || split[1].equalsIgnoreCase("reset")) && split.length > 2) {
					if(split[2].equalsIgnoreCase("prefix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] " + split[4] + "'s prefix is removed.");
							plugin.PSnames.setUserPrefix(split[4], "");
							resetIxes(plugin.getServer().getPlayer(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] The group " + split[4] + "'s prefix is removed.");
							plugin.PSnames.setGroupPrefix(split[4], "");
							
							Object[] str = names.values().toArray();
							for(int i = 0; i < names.size(); i++){
								if(getGroup(plugin.getServer().getPlayer((String)str[i])).equalsIgnoreCase(split[4])){
									resetIxes(plugin.getServer().getPlayer((String)str[i]));
								}
							}
						} else {
							showHelpMenu(player);
						}
					} else if(split[2].equalsIgnoreCase("suffix") && split.length > 3){
						if(split[3].equalsIgnoreCase("p:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] " + split[4] + "'s suffix is removed.");
							plugin.PSnames.setUserSuffix(split[4], "");
							resetIxes(plugin.getServer().getPlayer(split[4]));
						} else if(split[3].equalsIgnoreCase("g:") && split.length > 4){
							player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] The group " + split[4] + "'s prefix is removed.");
							plugin.PSnames.setGroupSuffix(split[4], "");
							
							Object[] str = names.values().toArray();
							for(int i = 0; i < names.size(); i++){
								if(getGroup(plugin.getServer().getPlayer((String)str[i])).equalsIgnoreCase(split[4])){
									resetIxes(plugin.getServer().getPlayer((String)str[i]));
								}
							}
						} else {
							showHelpMenu(player);
						}
					} else {
						showHelpMenu(player);
					}
				} else {
					showHelpMenu(player);
				}
			} else {
				showHelpMenu(player);
			}
			event.setCancelled(true);
		}
		
		//the /prefix command
		String[] prefixCommands = plugin.colorSettings.getProperty("prefix-commands").split(",");
		boolean prefixCommandUsed = false;
		boolean rainbowPermissions = false;
		rainbowPermissions = plugin.hasPermission(player, "colors.rainbow", player.isOp()) || player.isOp();
		if(plugin.hasPermission(player, "colors.prefix", true) || player.isOp()){
			for(int i = 0; (i < prefixCommands.length && !prefixCommandUsed); i++){
				if(split[0].equalsIgnoreCase(prefixCommands[i])){
					prefixCommandUsed = true;
				}
			}
		}
		
		if(prefixCommandUsed){
			if(split.length == 1){
				plugin.PSnames.setUserPrefix(player.getName(), "");
				resetIxes(player);
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour user prefix has been reset.");
			} else {
				int start = split[0].length() + 1;
				String PSset = mess.substring(start, mess.length());
				if(!rainbowPermissions){
					PSset.replaceAll(regcc + "[" + rcc + crcc + "]", "");
				}
				
				plugin.PSnames.setUserPrefix(player.getName(), PSset);
				resetIxes(player);
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour user prefix has been set.");
			}
			event.setCancelled(true);
		}
		
		//the /suffix command
		String[] suffixCommands = plugin.colorSettings.getProperty("suffix-commands").split(",");
		boolean suffixCommandUsed = false;
		//boolean rainbowPermissions = false; **Declared and evaluated in /prefix**
		
		if(plugin.hasPermission(player, "colors.suffix", true) || player.isOp()){
			for(int i = 0; (i < suffixCommands.length && !suffixCommandUsed); i++){
				if(split[0].equalsIgnoreCase(suffixCommands[i])){
					suffixCommandUsed = true;
				}
			}
		}
				
		if(suffixCommandUsed){
			if(split.length == 1){
				plugin.PSnames.setUserSuffix(player.getName(), "");
				resetIxes(player);
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour user suffix has been reset.");
			} else {
				int start = split[0].length() + 1;
				String PSset = mess.substring(start, mess.length());

				if(!rainbowPermissions){
					PSset.replaceAll(regcc + "[" + rcc + crcc + "]", "");
				}
				
				plugin.PSnames.setUserSuffix(player.getName(), PSset);
				resetIxes(player);
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour user suffix has been set.");
			}
			event.setCancelled(true);
		}
		
		//the /creload command
		String[] reloadCommands = plugin.colorSettings.getProperty("reload-commands").split(",");
		boolean reloadCommandUsed = false;
		
		if(plugin.hasPermission(player, "colors.reload", player.isOp()) || player.isOp()){
			for(int i = 0; (i < reloadCommands.length && !reloadCommandUsed); i++){
				if(split[0].equalsIgnoreCase(reloadCommands[i])){
					reloadCommandUsed = true;
				}
			}
		}
				
		if(reloadCommandUsed){
			plugin.colorSettings.reloadData();
			event.setCancelled(true);
			
			player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aThe settings have been reloaded.");
		}
		
		//The /colorlock command
		String[] colorlockCommands = plugin.colorSettings.getProperty("colorlock-commands").split(",");
		boolean colorlockCommandUsed = false;
		
		if(plugin.hasPermission(player, "colors.colorlock", true) || player.isOp()){
			for(int i = 0; (i < colorlockCommands.length && !colorlockCommandUsed); i++){
				if(split[0].equalsIgnoreCase(colorlockCommands[i])){
					colorlockCommandUsed = true;
				}
			}
		}
		
		if(colorlockCommandUsed){
			//TODO: reference point
			if(split.length == 2 && split[1].length() == 2){
				if(plugin.isColorChar(split[1].toCharArray()[0]) && (plugin.isColorNumber(split[1].toCharArray()[1]) || plugin.isFormattingChar(split[1].toCharArray()[1]) || Character.toLowerCase(split[1].toCharArray()[0]) == 'r')){
					char colorChar = split[1].toCharArray()[1];
					colorLocks.put(player, colorChar);
					plugin.data.setColorLock(player, colorChar);
					player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour specified color has been set.");
				} else {
					showHelpMenu_colorlock(player);
				}
			} else if(split.length == 1) {
				colorLocks.remove(player);
				plugin.data.setColorLock(player, '0');
				player.sendMessage("§b[§1C§2o§3l§4o§5r§6s§b] §aYour color lock has been removed.");
			} else {
				showHelpMenu_colorlock(player);
			}
			event.setCancelled(true);
		}
	}
		
	private void showHelpMenu_colorlock(Player player){
		String[] message = {
				"§b[§1C§2o§3l§4o§5r§6s§b] §aSyntax: §e/<colorlock> [" + dcc + "<color>]"};
    	
    	for(int i = 0; i < message.length; i++){
    		player.sendMessage(message[i]);
    	}
	}
    
    private void showHelpMenu(Player player){
    	String[] message = {
    			"§b[§1C§2o§3l§4o§5r§6s§b] §aSyntax: §e/<coloradmin> [get/set/remove/reset]",
    			"§b[§1C§2o§3l§4o§5r§6s§b] §e[prefix/suffix] [p:/g:] <user_name> (<prefix/suffix>)"};
    	
    	for(int i = 0; i < message.length; i++){
    		player.sendMessage(message[i]);
    	}
    }
    
    @EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
    	Player player = event.getPlayer();
    	names.put(player, player.getDisplayName());
    	char c = plugin.data.getColorLock(player);
    	if(c != '0'){

        	colorLocks.put(player, c);
    	}
    	resetIxes(player);
    }
    
    private void resetIxes(Player player){
    	if(!player.isOnline()){
    		return;
    	}
    	
    	String playername = player.getName();
    	String prefix = "";
    	String suffix = "";
    	String displayName = "";
    	
    	plugin.PSnames.saveData();
    	plugin.PSnames.reloadData();
    	
    	if(!plugin.colorSettings.getProperty("use-chat-formatting").equalsIgnoreCase("true")){
    		String group = getGroup(player);
    		
    		if(plugin.PSnames.getGroupPrefix(group) != null){
    			prefix = (plugin.PSnames.getGroupPrefix(group).length() > 0) ? plugin.PSnames.getGroupPrefix(group) : prefix;
    			suffix = (plugin.PSnames.getGroupSuffix(group).length() > 0) ? plugin.PSnames.getGroupSuffix(group) : suffix;
    		}
    	
    		if(plugin.PSnames.getUserPrefix(playername) != null){
    			prefix = (plugin.PSnames.getUserPrefix(playername).length() > 0) ? plugin.PSnames.getUserPrefix(playername) : prefix;
    			suffix = (plugin.PSnames.getUserSuffix(playername).length() > 0) ? plugin.PSnames.getUserSuffix(playername) : suffix;
    		}
    		
    		displayName = plugin.convertToColor(prefix + player.getName() + suffix + "§r", true);
    		player.setDisplayName(displayName);
    		//player.setDisplayName(prefix + names.get(player) + suffix + "§f");
    	}
    }
    
	public String getGroup(Player player){		
		return plugin.permissionHandler.getGroup(player);
    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerChatEvent event){
		if(event.isCancelled()){
			return;
		}
    	boolean colorPerms = false;
    	boolean rainbowPerms = false;
    	boolean rainbowEnabled = plugin.colorSettings.getProperty("use-rainbow").equalsIgnoreCase("true");
    	boolean chatFormatting = plugin.colorSettings.getProperty("use-chat-formatting").equalsIgnoreCase("true");
    	Player player = event.getPlayer();
    	
    	colorPerms = plugin.hasPermission(player, "colors.hex", true) || player.isOp();
    	rainbowPerms = (plugin.hasPermission(player, "colors.rainbow", player.isOp()) || player.isOp()) && rainbowEnabled;
    	
    	if(colorPerms){
    		String message = event.getMessage();
			if(plugin.isChatColoring(player)){
				if(chatFormatting){
					event.setMessage(dcc + rcc + message);
				} else {
					event.setMessage(plugin.colorsChat(message));
				}
			} else {
				if(colorLocks.containsKey(player)){
					event.setMessage(dcc + colorLocks.get(player) + message);
				}
				if(!chatFormatting){
					event.setMessage(plugin.convertToColor(message, rainbowPerms));
				}
			}
    	}
    	
    	if(chatFormatting){
    		if(!rainbowPerms){
    			String message = event.getMessage();
    			message.replaceAll(regcc + "[" + rcc + crcc + "]", "");
    			event.setMessage(message);
    		}
    		String format = plugin.colorSettings.getProperty("chat-formatting");
    		String finalMessage = "";
    		
    		int start, end;
    		int pend = 0;
    		Matcher m = Pattern.compile("\\Q*\\E.+?\\Q*\\E").matcher(format);
			while(m.find()){
				start = m.start();
				end = m.end();
				
				if(pend < start){
					finalMessage += format.substring(pend, start);
					//TODO: debug message
					/* Debug message */ /* Delete this to comment out -->
					System.out.println("[" + pluginname + "] (1) Player: " + player.getDisplayName() + ", Text: " + format.substring(pend, start));
					/* End debug message */
				}
				
				finalMessage += getNameProperty(format.substring(start+1, end-1), player, event.getMessage());
				//TODO: debug message
				/* Debug message */ /* Delete this to comment out --> 
				String dbpmessage = getNameProperty(format.substring(start+1, end-1), player, event.getMessage());
				System.out.println("[" + pluginname + "] (2) Player: " + player.getDisplayName() + ", Property: " + format.substring(start+1, end-1));
		    	System.out.println("[" + pluginname + "] (3) Player: " + player.getDisplayName() + ", Property: " + dbpmessage);
		    	/*End debug message*/
				
				pend = end;
			}
			
			event.setCancelled(true);
			plugin.getServer().broadcastMessage(plugin.convertToColor(finalMessage, rainbowEnabled));
			//TODO: debug message
			/* Debug message */ /* Delete this to comment out --> 
			System.out.println("[" + pluginname + " (4) Message: " + plugin.convertToColor(finalMessage, rainbowEnabled));
			/*End debug message*/
    	}
    }
    
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
    	names.remove(event.getPlayer());
    }
        
    public String getNameProperty(String propertyName, Player player, String message){
    	//TODO: debug message
    	/* Debug message */ /* Delete this to comment out --> 
    	System.out.println("[" + pluginname + "] (2) Player: " + player.getDisplayName() + ", Property: " + propertyName);
    	/*End debug message*/
    	
    	String group = getGroup(player);
    	String playername = player.getName();
    	String property = "";
    	
    	if(propertyName.equalsIgnoreCase("prefix")){
    		if(plugin.PSnames.getGroupPrefix(group) != null){
    			property = (plugin.PSnames.getGroupPrefix(group).length() > 0) ? plugin.PSnames.getGroupPrefix(group) : property;
    		}
    		if(plugin.PSnames.getUserPrefix(playername) != null){
    			property = (plugin.PSnames.getUserPrefix(playername).length() > 0) ? plugin.PSnames.getUserPrefix(playername) : property;
    		}
    		return property;
    	}
    	
    	if(propertyName.equalsIgnoreCase("suffix")){
    		if(plugin.PSnames.getGroupPrefix(group) != null){
    			property = (plugin.PSnames.getGroupSuffix(group).length() > 0) ? plugin.PSnames.getGroupSuffix(group) : property;
    		}
    		
    		if(plugin.PSnames.getUserPrefix(playername) != null){
    			property = (plugin.PSnames.getUserSuffix(playername).length() > 0) ? plugin.PSnames.getUserSuffix(playername) : property;
    		}
    		return property;
    	}
    	
    	if(propertyName.equalsIgnoreCase("message")){
    		return message;
    	}
    	
    	if(propertyName.equalsIgnoreCase("name")){
    		return playername;
    	}
    	
    	if(propertyName.equalsIgnoreCase("displayname")){
    		return player.getDisplayName();
    	}
    	
    	return "";
    }
}