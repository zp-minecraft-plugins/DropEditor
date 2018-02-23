package me.zackpollard.dropeditor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener{
	public static DropEditor plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public ArrayList<Player> alertedPlayers = new ArrayList<Player>();
	public BlockListener(DropEditor instance){
		plugin = instance;
		Bukkit.getServer().getPluginManager().registerEvents(this,instance);
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Integer id = block.getTypeId();
		Byte data = block.getData();
		String itemName = block.getType().toString();
		String perm = "dropeditor." + id + ":" + data;
		String config = "DropEditor.BrokenItemIds." + id + ":" + data;
		
		if(player.hasPermission("*")) return;
		
		if(player.isOp()) return;
		
		if(player.hasPermission(perm + ".deny")){
			
			player.sendMessage(ChatColor.RED + "[DropEditor] " + ChatColor.DARK_RED + "You don't have permission to destroy " + formatName(itemName));
			
			event.setCancelled(true);
			
			return;
			
		} else if(player.hasPermission(perm + ".specialdrop")){
			
			if(plugin.getConfig().isConfigurationSection(config)){
				
				String drop = plugin.getConfig().getString(config + ".SpecialDrop");
				String[] split = drop.split(":");
				Integer dropId = Integer.parseInt(split[0]);
				Byte dropData = Byte.parseByte(split[1]);
				Integer dropQuantity = plugin.getConfig().getInt(config + ".SpecialDropQuantity");
				List<String> toolIds = plugin.getConfig().getStringList(config + ".SpecialDropTools");
				Integer toolId = player.getItemInHand().getTypeId();
				World world = block.getWorld();
				Location location = block.getLocation();
				
				if(toolIds.contains("*") || toolIds.contains(toolId.toString())){
					
					ItemStack newDrop = new ItemStack(dropId, dropQuantity, dropData);
					
					event.setCancelled(true);
					block.setType(Material.AIR);
					world.dropItemNaturally(location, newDrop);
					
					return;
					
				} else {
					
					if(!alertedPlayers.contains(player)){
					
						player.sendMessage(ChatColor.RED + "[DropEditor] " + ChatColor.DARK_RED + "You could of had a special drop but you used the wrong tool! :(");
					
						alertedPlayers.add(player);
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new MyTask(this, player), 20 * 10);
					}
					
					return;
				}
				
			} else {
				
				player.sendMessage(ChatColor.RED + "[DropEditor] " + ChatColor.DARK_RED + "This item isn't in the config, something went wrong! Please contact an admin about this.");
				
				return;
			}
			
		} else if(player.hasPermission(perm + ".nodrop")){
			
			event.setCancelled(true);
			block.setType(Material.AIR);
			
			return;
			
		}
	}
	
	public String formatName(String itemName){
		
		String firstCharacter = itemName.substring(0, 1).toLowerCase();
		String newName = itemName.toLowerCase().replace("_", " ").replaceFirst(firstCharacter, firstCharacter.toUpperCase());
		
		return newName;
		
	}
}