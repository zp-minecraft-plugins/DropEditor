package me.zackpollard.dropeditor;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DropEditor extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public void onDisable() {
		this.logger.info("DropEditor is now disabled.");
	}
	public void onEnable() {
		
		generateConfig();
		
		new BlockListener(this);
		
		this.logger.info("DropEditor is now enabled.");
	}
	
	public void generateConfig(){
		
		final FileConfiguration config = this.getConfig();
        config.options().header("DropEditor Config");
        
        String[] itemids = {"16:0", "15:0", "14:0", "56:0", "129:0"};
        config.addDefault("DropEditor.BrokenItemIds", Arrays.asList(itemids));
        
        List<String> items = config.getStringList("DropEditor.BrokenItemIds");
        
        for(String id : items){
        	
        	config.addDefault("DropEditor.BrokenItemIds." + id + ".SpecialDrop", "20:0");
        	config.addDefault("DropEditor.BrokenItemIds." + id + ".SpecialDropQuantity", Integer.valueOf(1));
        	
        	String[] toolids = {"274", "257", "285", "278"};
        	config.addDefault("DropEditor.BrokenItemIds." + id + ".SpecialDropTools", Arrays.asList(toolids));
        }
        
        config.options().copyDefaults(true);
        
        saveConfig();
	}
}