
/*--------------------------------------------------------------------------------
-- MineBansIntegration
-- A Bukkit plug-in to provide simple connectivity between Bukkit and SourceBans
-- Authors: Jason Bray
-- Revision: 1.0
-- Released: May 22, 2012
--------------------------------------------------------------------------------*/


package com.github.JasonJosephBray.MineBansIntegration;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MineBansIntegration extends JavaPlugin implements Listener {
	
	/*----------------------------------------------------------------------------
	-- Instance Variables
	-- Purpose: *log - references the server's console logger
	----------------------------------------------------------------------------*/
	Logger log ;
	
	/*----------------------------------------------------------------------------
	-- onEnable()
	-- Purpose: Execute when the plugin is loading
	----------------------------------------------------------------------------*/
	public void onEnable() {
		log = this.getLogger();
		log.info("Minebans Integration Plugin intializing...");
		getServer().getPluginManager().registerEvents(this, this);
		log.info( TXTInterface.initialize() );
		if( SBDBase.connect( TXTInterface.getIP(), TXTInterface.getPort(), 
			TXTInterface.getUser(), TXTInterface.getPass(), TXTInterface.getDBase()
			) )
			log.info("Your Minebans Integration has been enabled!");
		else
			log.info("ERROR: Could not connect to database! Ensure information in"+
					" MBSetup.txt is accurate!");
	}
	
	/*----------------------------------------------------------------------------
	-- onEnable()
	-- Purpose: Execute when the plugin is being stopped
	----------------------------------------------------------------------------*/
	public void onDisable() {
		if( SBDBase.disconnect() )
			log.info("Minebans integration quit!");
		else
			log.info("ERROR: Minebans did not shut down correctly!");
	}
	
	/*----------------------------------------------------------------------------
	-- onPlayerLogin( PlayerLoginEvent )
	-- Purpose: Execute when a player is authenticated by the server
	-- Input: (PlayerLoginEvent) A Player's Login Event
	----------------------------------------------------------------------------*/
	@EventHandler //EventHandler defaults to a priority of NORMAL
	public void onPlayerLogin( PlayerLoginEvent event ) {
		Player player = event.getPlayer();
		
		if( SBDBase.checkBanned(player.getName()) )
			player.setBanned(true);
		if( !player.isOp() && SBDBase.checkAdmin(player.getName()) )
			player.setOp(true);
	}
	
	/*----------------------------------------------------------------------------
	-- OnCommand( CommandSender, Command, String, String[] )
	-- Purpose: Check whether a command has been typed in by a user
	-- Input: (CommandSender) The sender of the command
	--        (Command) The command to be executed
	--        (String) The alias of the command that was used
	--        (String[]) An array of the arguments used
	-- Output: true = command was successfully executed
	--         false = Will direct the sender to the information provided in the
	--                   plugin.yml file
	----------------------------------------------------------------------------*/
	public boolean onCommand( CommandSender sender, Command cmd, 
			String commandLabel, String[] args ) {
		if( cmd.getName().equalsIgnoreCase("linkprofile") && args.length == 2){
			if( sender.hasPermission("minebans.link") ){
				args[0] = args[0].trim().toLowerCase();
				args[1] = args[1].trim().toUpperCase();
				if( args[0].length() <= 16 && args[1].length() <= 18 &&
					args[1].startsWith("STEAM_") )
					if( SBDBase.link(args[0], args[1]) )
						return true;
					else
						sender.sendMessage("Something went wrong! Ensure " +
								"connectivity to the database!");
				else
					sender.sendMessage("Invalid arguements! Please try again.");
			}
			else
				sender.sendMessage("You don't have the proper permissions to " +
						"run this command!");
				
			return false;
		}
		return false;
	}

}
