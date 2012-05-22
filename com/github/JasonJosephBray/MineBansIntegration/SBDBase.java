
/*--------------------------------------------------------------------------------
-- SBDBase
-- Manages everything MySQL to connect to the SourceBans database
-- Authors: Jason Bray
-- Revision: 1.0
-- Released: May 22, 2012
--------------------------------------------------------------------------------*/

package com.github.JasonJosephBray.MineBansIntegration;

import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class SBDBase {
	
	/*----------------------------------------------------------------------------
	-- Instance Variables
	-- Purpose: *stmt - Holds a reference to make a modification to the database
	----------------------------------------------------------------------------*/
	static private Statement stmt = null; 
	
	/*----------------------------------------------------------------------------
	-- connect( String, int, String, String, String )
	-- Purpose: Create a connection to a MySQL database
	-- Input: Properly formatted IPv4 address (as String)
	--	The port the connection should be made over (normally 3306) (as int)
	--	A username for the user with access to the database (As String)
	--	The provided user's password (as String)
	--	The MySQL database name where the SourceBans tables are stored (as String)
	-- Output: true = banned ;  false = not banned or error
	----------------------------------------------------------------------------*/
	static public boolean connect( String ip, int port, String user, String pass, 
			String database ) {
		String url = "jdbc:mysql://" + ip +":"+ port;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection(
				 url, user, pass);
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("USE "+ database);
			
			try{
				stmt.executeQuery("SELECT(1) FROM mb_integration");
				
			} catch(Exception e){
				stmt.executeUpdate("CREATE TABLE mb_integration(un_id INT " +
						"AUTO_INCREMENT PRIMARY KEY, Minecraft_Name VARCHAR (16)"+
				 ", STEAM_ID VARCHAR (18))");
			}
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/*----------------------------------------------------------------------------
	-- disconnect()
	-- Purpose: Close the connection to the MySQL server
	-- Output: true = disconnected
	--         false = error
	----------------------------------------------------------------------------*/
	static public boolean disconnect() {
		try{
			stmt.getConnection().close();
			return true;
		} catch( Exception e ){
			return false;
		}
	}
	
	/*----------------------------------------------------------------------------
	-- link( String, String )
	-- Purpose: Link a Minecraft name to a STEAM ID
	-- Input: (String) A Minecraft name that is less than 17 characters
	--        (String) A properly formatted STEAM ID less than 19 characters
	-- Output: true = connection was made
	--         false = an error occurred
	----------------------------------------------------------------------------*/
	static public boolean link(String mine, String steam){
		if(stmt != null)
		try{
			stmt.executeUpdate("INSERT INTO mb_integration(Minecraft_Name, " +
					"STEAM_ID) VALUES('"+ mine +"', '"+ steam +"')");
			return true;
		} catch(Exception e){
			return false;
		}
		return false;
	}
	
	/*----------------------------------------------------------------------------
	-- checkBanned( String )
	-- Purpose: Check whether the provided Minecraft name is banned in SourceBans
	-- Input: (String) A Minecraft name that is less than 17 characters
	-- Output: true = banned
	--         false = not banned or error
	----------------------------------------------------------------------------*/
	static public boolean checkBanned(String mine) {
		if (stmt != null) try{
			ResultSet set = stmt.executeQuery("SELECT STEAM_ID FROM mb_integration WHERE Minecraft_Name"+
					"='"+ mine.trim().toLowerCase() +"'");
			while(set.next()){
			String steamID = set.getString(1);
				if( steamID != null ){
					ResultSet banset = stmt.executeQuery("SELECT bid FROM sb_bans"+
							" WHERE authid='"+ steamID +"'");
					banset.next();
					String ban = banset.getString(1);
					if(ban != null)
						return true;
				}
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
	/*----------------------------------------------------------------------------
	-- checkAdmin( String )
	-- Purpose: Check whether the provided Minecraft name is admin in SourceBans
	-- Input: (String) A Minecraft name that is less than 16 characters
	-- Output: true = is admin
	--         false = not admin
	----------------------------------------------------------------------------*/
	static public boolean checkAdmin(String mine){
		if (stmt != null) try{
			ResultSet set = stmt.executeQuery("SELECT STEAM_ID FROM " +
					"mb_integration WHERE Minecraft_Name"+
					"='"+ mine.trim().toLowerCase() +"'");
			while(set.next()){
			String steamID = set.getString(1);
				if( steamID != null ){
					ResultSet adminset = stmt.executeQuery("SELECT aid FROM " +
							"sb_admins WHERE authid='"+ steamID +"'");
					adminset.next();
					String admin = adminset.getString(1);
					if(admin != null)
						return true;
				}
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
}
