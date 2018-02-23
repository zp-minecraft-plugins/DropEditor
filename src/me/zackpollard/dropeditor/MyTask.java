package me.zackpollard.dropeditor;

import org.bukkit.entity.Player;

public class MyTask implements Runnable{
	private BlockListener plugin;
	private Player player;
	
	public MyTask(BlockListener plugin, Player player){
		this.plugin = plugin;
		this.player = player;
	}
	
	public void run(){
		
		plugin.alertedPlayers.remove(player);
		
	}

}