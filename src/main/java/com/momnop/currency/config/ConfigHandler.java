package com.momnop.currency.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
  public static Configuration configuration;
  public static boolean dropMoney = true;
  public static float mobDivisionValue = 10.0F;
  
  public static void init(File file)
  {
    if (configuration == null)
    {
    	configuration = new Configuration(file);
      	loadConfiguration();
    }
  }
  
  public static void loadConfiguration()
  {
	  dropMoney = configuration.getBoolean("Drop Money from Hostile Mobs?", Configuration.CATEGORY_GENERAL, dropMoney, "Should money drop from hostile mobs?");
	  mobDivisionValue = configuration.getFloat("How much is each health value divided by to return money.", "general", mobDivisionValue, 0.0F, 100.0F, "Whenever money is calculated in hostile mobs, it is divided by a number to return the money you will get back. This changes the number you divide it by. Note: Lower values will provide larger amounts of money.");
	  if (configuration.hasChanged()) {
		  configuration.save();
	  }
  }
}
