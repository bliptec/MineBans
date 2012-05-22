
/*--------------------------------------------------------------------------------
-- TXTInterface
-- Manages the connection information of the SourceBans MySQL database, handled
--	from within the plugin's .jar file
-- Authors: Jason Bray
-- Revision: 1.0
-- Released: May 22, 2012
--------------------------------------------------------------------------------*/

package com.github.JasonJosephBray.MineBansIntegration;

import java.io.*;
import java.util.*;

public class TXTInterface {
	
	/*----------------------------------------------------------------------------
	-- Instance Variables
	-- Purpose: *cfgList - Holds all the information from the config file in an
	--            array list
	----------------------------------------------------------------------------*/
	private static ArrayList<String> cfglist;
	
	/*----------------------------------------------------------------------------
	-- initialize()
	-- Purpose: Starts the file reader
	-- Output: (String) A status message
	----------------------------------------------------------------------------*/
	static public String initialize(){
		cfglist = new ArrayList<String>();
		InputStream is = null;
	    BufferedReader br = null;
	    String line;
	    try { 
	        is = TXTInterface.class.getResourceAsStream("/MBSetup.txt");
	        br = new BufferedReader(new InputStreamReader(is));
	        while (null != (line = br.readLine())) {
	           cfglist.add(line);
	        }
	      }
	      catch (Exception e) {
	        return "WARNING: Error initializing setup file!";
	      }
	      finally {
	        try {
	          if (br != null) br.close();
	          if (is != null) is.close();
	        }
	        catch (IOException e) {
	          return "WARNING: Error initializing setup file!";
	        }
	      }
		return "TXTInteface successfully initialized.";
	}
	
	/*----------------------------------------------------------------------------
	-- disconnect()
	-- Purpose: Frees the resources used for the file reader
	----------------------------------------------------------------------------*/
	static public void disconnect(){
		cfglist = null;
	}
	
	/*----------------------------------------------------------------------------
	-- getIP()
	-- Purpose: Return the IP provided in the config file
	-- Output: (String) The IP provided in the config file
	----------------------------------------------------------------------------*/
	static public String getIP(){
		try{
			Iterator<String> reader = cfglist.iterator();
			String next;
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					return next.trim();
			}
			return "";
		} catch(Exception e){
			return "";
		}
	}
	
	/*----------------------------------------------------------------------------
	-- getPort()
	-- Purpose: Return the Port provided in the config file
	-- Output: (String) The Port provided in the config file
	----------------------------------------------------------------------------*/
	static public int getPort(){
		try{
			Iterator<String> reader = cfglist.iterator();
			String next;
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					break;
			}
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					return Integer.parseInt(next);
			}
			return -1;
		} catch(Exception e){
			return -1;
		}
	}
	
	
	/*----------------------------------------------------------------------------
	-- getUser()
	-- Purpose: Return the User-name provided in the config file
	-- Output: (String) The User-name provided in the config file
	----------------------------------------------------------------------------*/
	static public String getUser(){
		try{
			Iterator<String> reader = cfglist.iterator();
			String next;
			for(int i=0; i<2; i++)
				while(reader.hasNext()){
					next = reader.next();
					if(!next.startsWith("#"))
						break;
				}
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					return next;
			}
			return "derp";
		} catch(Exception e){
			return "";
		}
	}
	
	/*----------------------------------------------------------------------------
	-- getPass()
	-- Purpose: Return the Password provided in the config file
	-- Output: (String) The Password provided in the config file
	----------------------------------------------------------------------------*/
	static public String getPass(){
		try{
			Iterator<String> reader = cfglist.iterator();
			String next;
			for(int i=0; i<3; i++)
				while(reader.hasNext()){
					next = reader.next();
					if(!next.startsWith("#"))
						break;
				}
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					return next;
			}
			return "";
		} catch(Exception e){
			return "";
		}
	}
	
	/*----------------------------------------------------------------------------
	-- getDBase()
	-- Purpose: Return the Database name provided in the config file
	-- Output: (String) The Database name provided in the config file
	----------------------------------------------------------------------------*/
	static public String getDBase(){
		try{
			Iterator<String> reader = cfglist.iterator();
			String next;
			for(int i=0; i<4; i++)
				while(reader.hasNext()){
					next = reader.next();
					if(!next.startsWith("#"))
						break;
				}
			while(reader.hasNext()){
				next = reader.next();
				if(!next.startsWith("#"))
					return next;
			}
			return "";
		} catch(Exception e){
			return "";
		}
	}

}
