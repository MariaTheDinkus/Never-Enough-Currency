package com.zundrel.currency;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Logger;

import com.zundrel.currency.client.render.gui.GuiHandler;
import com.zundrel.currency.common.CommonProxy;
import com.zundrel.currency.common.blocks.BlockHandler;
import com.zundrel.currency.common.blocks.tiles.TileEntityDisplay;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.config.ConfigHandler;
import com.zundrel.currency.common.info.ModInfo;
import com.zundrel.currency.common.network.PacketDispatcher;

@Mod(name = ModInfo.NAME, modid = ModInfo.MODID, version = ModInfo.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", dependencies = "after:waila")
public class Currency {
	@SidedProxy(clientSide = "com.zundrel.currency.client.ClientProxy", serverSide = "com.zundrel.currency.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value = ModInfo.MODID)
	public static Currency INSTANCE;

	@CapabilityInject(AccountCapability.class)
	public static final Capability<AccountCapability> ACCOUNT_DATA = null;

	@CapabilityInject(CartCapability.class)
	public static final Capability<CartCapability> CART_DATA = null;

	public static CreativeTabs tabGeneral = new CreativeTabs(ModInfo.MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockHandler.atm);
		}
	};

	public static Logger logger;

	public static SimpleNetworkWrapper networkWrapper;
	public static HashMap<ChunkPos, Integer> ticketList;
	private static Ticket chunkLoaderTicket;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		proxy.preInitRenderers();

		ConfigHandler.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initRenderers();

		NetworkRegistry.INSTANCE.registerGuiHandler(Currency.INSTANCE, new GuiHandler());

		AccountCapability.register();
		CartCapability.register();

		PacketDispatcher.registerPackets();

		GameRegistry.registerTileEntity(TileEntityShopController.class, ModInfo.MODID + ":" + "shop_controller");
		GameRegistry.registerTileEntity(TileEntityDisplay.class, ModInfo.MODID + ":" + "display");
		GameRegistry.registerTileEntity(TileEntityStockCrate.class, ModInfo.MODID + ":" + "stock_crate");

		FMLInterModComms.sendMessage("waila", "register", "com.zundrel.currency.common.compat.waila.CurrencyWailaProvider.callbackRegister");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ticketList = new HashMap<ChunkPos, Integer>();
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new LoadingCallback() {
			@Override
			public void ticketsLoaded(List<Ticket> tickets, World world) {

			}
		});
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

	}

	public static void forceChunkLoad(World w, ChunkPos pos) {
		if (!ticketList.containsKey(pos)) {
			if (chunkLoaderTicket == null) {
				chunkLoaderTicket = ForgeChunkManager.requestTicket(INSTANCE, w, Type.NORMAL);
			}
			ticketList.put(pos, 1);
			ForgeChunkManager.forceChunk(chunkLoaderTicket, pos);
		} else {
			ticketList.put(pos, ticketList.get(pos) + 1);
		}
	}

	public static void releaseChunkLoad(World w, ChunkPos pos) {
		if (!ticketList.containsKey(pos) || chunkLoaderTicket == null) {
			return;
		} else {
			int num = ticketList.get(pos) - 1;
			if (num > 0)
				ticketList.put(pos, num);
			else
				ForgeChunkManager.unforceChunk(chunkLoaderTicket, pos);
		}
	}
}