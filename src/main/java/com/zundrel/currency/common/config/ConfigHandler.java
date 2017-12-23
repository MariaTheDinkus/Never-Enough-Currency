package com.zundrel.currency.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.zundrel.currency.common.info.ConfigInfo;

public class ConfigHandler {
	public static Configuration configuration;
	public static boolean dropMoney = true;
	public static float mobDivisionValue = 8F;
	public static int position = 0;
	public static boolean laidDownShelves = false;
	public static boolean laidDownTables = true;
	public static boolean laidDownPedestals = true;

	public static void init(File file) {
		if (configuration == null) {
			configuration = new Configuration(file);
			loadConfiguration();
		}
	}

	public static void loadConfiguration() {
		dropMoney = configuration.getBoolean("Drop Money from Hostile Mobs?", Configuration.CATEGORY_GENERAL, dropMoney, "Should money drop from hostile mobs?");
		mobDivisionValue = configuration.getFloat(ConfigInfo.MOB_HEALTH_DIVISION_NAME, Configuration.CATEGORY_GENERAL, mobDivisionValue, 0F, 100F, ConfigInfo.MOB_HEALTH_DIVISION_DESC);
		position = configuration.getInt(ConfigInfo.GUI_POSITION_NAME, Configuration.CATEGORY_CLIENT, position, 0, 3, ConfigInfo.GUI_POSITION_DESC);
		laidDownShelves = configuration.getBoolean("Should Items lay down on shelves?", Configuration.CATEGORY_CLIENT, laidDownShelves, "Changes whether Items lay down on shelves or not. This is purely visual. Note: Laid down items are harder to see on higher up shelves but look more aesthetically pleasing.");
		laidDownTables = configuration.getBoolean("Should Items lay down on tables?", Configuration.CATEGORY_CLIENT, laidDownTables, "Changes whether Items lay down on tables or not. This is purely visual. Note: This is set to true by default because it has no impact on the visibility of items.");
		laidDownPedestals = configuration.getBoolean("Should Items lay down on pedestals?", Configuration.CATEGORY_CLIENT, laidDownPedestals, "Changes whether Items lay down on pedestals or not. This is purely visual. Note: This is set to true by default because it has no impact on the visibility of items.");
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}