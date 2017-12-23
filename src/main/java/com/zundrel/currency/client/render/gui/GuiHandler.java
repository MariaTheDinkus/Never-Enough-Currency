package com.zundrel.currency.client.render.gui;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.inventory.ContainerItem;
import com.zundrel.currency.common.inventory.ContainerStockCrate;
import com.zundrel.currency.common.inventory.GuiWallet;
import com.zundrel.currency.common.inventory.InventoryItem;

public class GuiHandler implements IGuiHandler {
	public static HashMap guiScreens = new HashMap();
	public static HashMap containers = new HashMap();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 1) {
			return new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItemMainhand()));
		} else if (ID == 2) {
			return new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItemOffhand()));
		} else if (ID == 4) {
			return new ContainerStockCrate(player.inventory, (TileEntityStockCrate) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new GuiATM(player);
		} else if (ID == 1) {
			return new GuiWallet(new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItemMainhand())));
		} else if (ID == 2) {
			return new GuiWallet(new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItemOffhand())));
		} else if (ID == 3) {
			return new GuiShopController(new BlockPos(x, y, z), world);
		} else if (ID == 4) {
			return new GuiStockCrate(player, new BlockPos(x, y, z), player.inventory, (TileEntityStockCrate) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == 5) {
			return new GuiShoppingList(player);
		}

		return null;
	}
}
