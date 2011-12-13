package tk.tyzoid.plugins.colors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class settings {
	private Properties props = new Properties();
	
	private final HashMap<String, String> settingsHolder = new HashMap<String, String>();
	private String pluginname = "Colors";
	
	public void readSettings(){
		try{
			String path = "plugins/Colors";
			File propertiesFile = new File(path + "/Colors.properties");
    		if(!propertiesFile.exists()){
    			(new File(path)).mkdir();
    			propertiesFile.createNewFile();
    		}
		    
			FileInputStream propertiesStream = new FileInputStream(propertiesFile);
			
			props.load(propertiesStream);
			System.out.println("[" + pluginname + "] Properties loaded.");
			propertiesStream.close();
			
			settingsHolder.put("use-rainbow", loadProperty("use-rainbow", "true"));
			settingsHolder.put("use-chat-formatting", loadProperty("use-chat-formatting", "true"));
			settingsHolder.put("chat-formatting", loadProperty("chat-formatting", "<*prefix**displayname**suffix*&f> *message*"));
			settingsHolder.put("use-admin", loadProperty("use-admin", "true"));
			settingsHolder.put("commands", loadProperty("commands", "/colors,/colours"));
			settingsHolder.put("color-chars", loadProperty("color-chars", "&,^"));
			settingsHolder.put("admin-commands", loadProperty("admin-commands", "/ca"));
			settingsHolder.put("prefix-commands", loadProperty("prefix-commands", "/prefix"));
			settingsHolder.put("suffix-commands", loadProperty("suffix-commands", "/suffix"));
			settingsHolder.put("reload-commands", loadProperty("reload-commands", "/creload,/cr"));
			settingsHolder.put("colorlock-commands", loadProperty("colorlock-commands", "/cl,/color,/colorlock"));
			
			if(!((settingsHolder.get("use-rainbow").equalsIgnoreCase("true")) || (settingsHolder.get("use-rainbow").equalsIgnoreCase("false")))){
				System.out.println("[" + pluginname + "] Malformed property \"use-rainbow\". Resetting...");
				setCProperty("use-rainbow", "true");
				settingsHolder.put("use-rainbow", "true");
			}
			
			if(!((settingsHolder.get("use-admin").equalsIgnoreCase("true")) || (settingsHolder.get("use-admin").equalsIgnoreCase("false")))){
				System.out.println("[" + pluginname + "] Malformed property \"use-admin\". Resetting...");
				setCProperty("use-admin", "true");
				settingsHolder.put("use-admin", "true");
			}
			
			if(!((settingsHolder.get("use-chat-formatting").equalsIgnoreCase("true")) || (settingsHolder.get("use-chat-formatting").equalsIgnoreCase("false")))){
				System.out.println("[" + pluginname + "] Malformed property \"use-chat-formatting\". Resetting...");
				setCProperty("use-chat-formatting", "true");
				settingsHolder.put("use-chat-formatting", "true");
			}
			
			int start, end;
			boolean notValid = false;
			Matcher m = Pattern.compile("\\Q*\\E.+?\\Q*\\E").matcher(settingsHolder.get("chat-formatting"));
			while(m.find() && !notValid){
				start = m.start();
				end = m.end();
				
				if(!isValidChatOption(settingsHolder.get("chat-formatting").substring(start+1, end - 1))){
					notValid = true;
					System.out.println("[" + pluginname + "] Malformed chat option: " + settingsHolder.get("chat-formatting").substring(start+1, end - 1));
					System.out.println("[" + pluginname + "] Malformed property \"chat-formatting\". Resetting...");
					setCProperty("chat-formatting", "<*prefix**displayname**suffix*> *message*");
					settingsHolder.put("chat-formatting", "<*prefix**displayname**suffix*> *message*");
				}
			}
		
			String[] chars = settingsHolder.get("color-chars").split(",");
			boolean resetChatIndicators = false;
			boolean exists = chars.length > 0;
			for(int i = 0; i < chars.length && !resetChatIndicators; i++){
				if(chars[i].toCharArray().length != 1){
					resetChatIndicators = true;
				}
			}
			if(resetChatIndicators || !exists){
				System.out.println("[" + pluginname + "] Malformed property \"color-chars\". Resetting...");
				setCProperty("color-chars", "&,^");
				settingsHolder.put("color-chars", "&,^");
			}
			
			FileOutputStream propertiesOutputStream = new FileOutputStream(propertiesFile);
			props.store(propertiesOutputStream, "");
		} catch(Exception e){
			System.out.println("[" + pluginname + "] Failed to load properties. Aborting.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
	
	public String getProperty(String property){
		return settingsHolder.get(property);
	}
	
	public void reloadData(){
		readSettings();
	}
	
	private String loadProperty(String property, String defaultValue){
		String currentProperty;
		currentProperty = props.getProperty(property);
		String value;
    	if(currentProperty == null){
    		System.out.println("[" + pluginname + "] Property not found: " + property + ". Resetting to: " + defaultValue);
    		props.setProperty(property, defaultValue);
    		value = defaultValue;
    	} else {
    		value = currentProperty;
    	}
    	return value;
	}
	
	private boolean isValidChatOption(String option){		
		return (option.equalsIgnoreCase("prefix") ||
				option.equalsIgnoreCase("name") ||
				option.equalsIgnoreCase("displayname") ||
				option.equalsIgnoreCase("suffix") ||
				option.equalsIgnoreCase("message"));
	}
	
	private void setCProperty(String property, String value){
		props.setProperty(property, value);		
	}
}