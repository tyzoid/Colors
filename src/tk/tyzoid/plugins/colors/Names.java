package tk.tyzoid.plugins.colors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

//import org.bukkit.entity.Player;

public class Names {
	colors plugin;
	
	private Properties user = new Properties();
	private Properties group = new Properties();
	
	private String pluginname;
	private String pComment;
	private String gComment;
	
	private File players;
	private File groups;
	private FileOutputStream playerOut;
	private FileOutputStream groupOut;
	
	public Names(colors instance){
		plugin = instance;
		
		pluginname = plugin.pluginname;
		pComment = "[" + pluginname +"] Players' prefix and suffix list.";
		gComment = "[" + pluginname +"] Groups' prefix and suffix list.";
	}
	
	public void loadNames(){
		try{
			String path = "plugins/Colors";
			players = new File(path + "/players.list");
    		if(!players.exists()){
    			(new File(path)).mkdir();
    			players.createNewFile();
    		}
    		
    		groups = new File(path + "/groups.list");
    		if(!groups.exists()){
    			(new File(path)).mkdir();
    			groups.createNewFile();
    		}
		    
			FileInputStream playerSP = new FileInputStream(players);
			FileInputStream groupSP = new FileInputStream(groups);
			
			user.load(playerSP);
			group.load(groupSP);
			
			playerSP.close();
			groupSP.close();
			
		} catch(Exception e){
			System.out.println("[" + pluginname + "] Failed to load usernames. Aborting.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
	
	public String getUserPrefix(String playername){
		if(playername != playername.toLowerCase() && user.containsKey(playername)){
			setUserPrefix(playername.toLowerCase(), ((user.getProperty(playername).split("•").length > 0) ? user.getProperty(playername).split("•")[0] : ""));
			user.remove(playername);
		} else if(playername != playername.toLowerCase() && !user.containsKey(playername) && !user.containsKey(playername.toLowerCase())){
			setUserPrefix(playername.toLowerCase(), "");
		}
		if(user.containsKey(playername.toLowerCase())){
            return (user.getProperty(playername.toLowerCase()).split("•").length > 0) ? user.getProperty(playername.toLowerCase()).split("•")[0] : "";
        } else {
        	setUserPrefix(playername.toLowerCase(), "");
        	return null;
        }
	}
	
	public String getUserSuffix(String playername){
		if(playername != playername.toLowerCase() && user.containsKey(playername)){
			setUserSuffix(playername.toLowerCase(), (user.getProperty(playername).split("•").length > 1) ? user.getProperty(playername).split("•")[1] : "");
			user.remove(playername);
		} else if(playername != playername.toLowerCase() && !user.containsKey(playername) && !user.containsKey(playername.toLowerCase())){
			setUserPrefix(playername.toLowerCase(), "");
		}
		if(user.containsKey(playername.toLowerCase())){
            return (user.getProperty(playername.toLowerCase()).split("•").length > 1) ? user.getProperty(playername.toLowerCase()).split("•")[1] : "";
        } else {
        	setUserPrefix(playername.toLowerCase(), "");
        	return null;
        }
	}
	
	public String getGroupPrefix(String groupname){
		if(group.containsKey(groupname)){
            return (group.getProperty(groupname).split("•").length > 0) ? group.getProperty(groupname).split("•")[0] : "";
        } else {
        	setGroupPrefix(groupname, "");
        	return null;
        }
	}
	
	public String getGroupSuffix(String groupname){
		if(group.containsKey(groupname)){
            return (group.getProperty(groupname).split("•").length > 1) ? group.getProperty(groupname).split("•")[1] : "";
        } else {
        	setGroupPrefix(groupname, "");
        	return null;
        }
	}
	
	//section
	
	public void setUserPrefix(String playername, String prefix){
		if(user.containsKey(playername.toLowerCase())){
			String suffix = (user.getProperty(playername.toLowerCase()).split("•").length > 1) ? user.getProperty(playername.toLowerCase()).split("•")[1] : "";
			user.put(playername.toLowerCase(), prefix + "•" + suffix);
		} else {
			user.put(playername.toLowerCase(), prefix + "•");
		}
	}
	
	public void setUserSuffix(String playername, String suffix){
		if(user.containsKey(playername.toLowerCase())){
			String prefix = (user.getProperty(playername.toLowerCase()).split("•").length > 0) ? user.getProperty(playername.toLowerCase()).split("•")[0] : "";
			user.put(playername.toLowerCase(), prefix + "•" + suffix);
		} else {
			user.put(playername.toLowerCase(), "•" + suffix);
		}
	}
	
	public void setGroupPrefix(String groupname, String prefix){
		if(group.containsKey(groupname)){
			String suffix = (group.getProperty(groupname).split("•").length > 1) ? group.getProperty(groupname).split("•")[1] : "";
			group.put(groupname, prefix + "•" + suffix);
		} else {
			group.put(groupname, prefix + "•");
		}
	}
	
	public void setGroupSuffix(String groupname, String suffix){
		if(group.containsKey(groupname)){
			String prefix = (group.getProperty(groupname).split("•").length > 0) ? group.getProperty(groupname).split("•")[0] : "";
			group.put(groupname, prefix + "•" + suffix);
		} else {
			group.put(groupname, "•" + suffix);
		}
	}
	
	public void pluginClosing(boolean show){
		if(show){System.out.println("[" + pluginname + "] Saving prefix/suffix data.");}
		try{
			playerOut = new FileOutputStream(players);
			groupOut = new FileOutputStream(groups);
			
			user.store(playerOut, pComment);
			group.store(groupOut, gComment);
			
			playerOut.close();
			groupOut.close();
		} catch(Exception e){
			if(show){
				System.out.println("[" + pluginname + "] Could not save prefix/suffix data.");
				System.out.println("[" + pluginname + "] Error: " + e.toString());
			}
		}
	}
	
	public boolean reloadData(){
		boolean result = false;
		
		try{
			FileInputStream playerSP = new FileInputStream(players);
			FileInputStream groupSP = new FileInputStream(groups);
		
			user.load(playerSP);
			group.load(groupSP);
			
			playerSP.close();
			groupSP.close();
			result = true;
		} catch(Exception e){
			result = false;
		}
		
		return result;
	}
	
	public void saveData(){
		pluginClosing(false);
	}
}
