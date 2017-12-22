package com.zundrel.currency.common;

import java.util.HashMap;
import java.util.List;

import com.zundrel.currency.Currency;
import com.zundrel.currency.client.render.gui.GuiHandler;
import com.zundrel.currency.common.blocks.tiles.TileEntityDisplay;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.config.ConfigHandler;
import com.zundrel.currency.common.info.ModInfo;
import com.zundrel.currency.common.network.PacketDispatcher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
      return ctx.getServerHandler().player;
    }
	
	public void registerItemRenderer(Item i, int meta, String id) {
    	
    }
}