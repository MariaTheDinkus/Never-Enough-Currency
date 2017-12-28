package com.momnop.currency;

import com.momnop.currency.config.ConfigHandler;
import com.momnop.currency.items.ItemHandler;
import com.momnop.currency.proxy.CommonProxy;
import com.momnop.currency.utils.CurrencyUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraftforge.common.MinecraftForge;

@Mod(name="Never Enough Currency", modid="currency", version="1.0.5", dependencies="before:cityworks;before:yeoldeshoppe")
public class Currency
{
  @SidedProxy(clientSide="com.momnop.currency.proxy.ClientProxy", serverSide="com.momnop.currency.proxy.CommonProxy")
  public static CommonProxy proxy;
  @Mod.Instance("currency")
  public static Currency INSTANCE;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    ItemHandler.load();
    RecipeHandler.loadRecipes();
    proxy.preInitRenderers();
    
    ConfigHandler.init(event.getSuggestedConfigurationFile());
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    FMLCommonHandler.instance().bus().register(new ModEventHandler());
    MinecraftForge.EVENT_BUS.register(new ModEventHandler());
    
    proxy.initRenderers();
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {}
  
  @Mod.EventHandler
  public void serverLoad(FMLServerStartingEvent event) {}
}
