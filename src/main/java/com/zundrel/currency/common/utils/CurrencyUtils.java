package com.zundrel.currency.common.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zundrel.currency.common.inventory.InventoryItem;
import com.zundrel.currency.common.items.ItemHandler;
import com.zundrel.currency.common.items.ItemMoneyBase;
import com.zundrel.currency.common.items.ItemWallet;
import com.zundrel.currency.common.network.MessageSyncDrops;
import com.zundrel.currency.common.network.MessageSyncRemove;
import com.zundrel.currency.common.network.PacketDispatcher;

public class CurrencyUtils {
	public static String getAllCurrency(EntityPlayer player) {
		float currencyTotal = 0;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
				ItemMoneyBase money = (ItemMoneyBase) player.inventory.getStackInSlot(i).getItem();
				currencyTotal += (money.getValue() * player.inventory.getStackInSlot(i).getCount());
			}
		}

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemWallet) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack.hasTagCompound()) {
					InventoryItem inventory = new InventoryItem(stack);

					for (int i1 = 0; i1 < inventory.getSizeInventory(); i1++) {
						if (inventory.getStackInSlot(i1) != ItemStack.EMPTY && inventory.getStackInSlot(i1).getItem() instanceof ItemMoneyBase) {
							currencyTotal += ((ItemMoneyBase) inventory.getStackInSlot(i1).getItem()).getValue() * inventory.getStackInSlot(i1).getCount();
						}
					}
				}
			}
		}
		NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
		return fmt.format(currencyTotal);
	}

	public static String getAllCurrencyNoWallet(EntityPlayer player) {
		float currencyTotal = 0;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
				ItemMoneyBase money = (ItemMoneyBase) player.inventory.getStackInSlot(i).getItem();
				currencyTotal += (money.getValue() * player.inventory.getStackInSlot(i).getCount());
			}
		}

		NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
		return fmt.format(currencyTotal);
	}

	public static void depositMoney(EntityPlayer player, float amount) {
		float playerAmount = getCurrencyNoWallet(player);

		PacketDispatcher.sendToServer(new MessageSyncRemove(player));

		PacketDispatcher.sendToServer(new MessageSyncDrops(player, playerAmount - amount));
	}

	public static void combineMoney(EntityPlayer player) {
		float playerAmount = getCurrencyNoWallet(player);

		if (player.getEntityWorld().isRemote) {
			return;
		}

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
				player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}

		for (ItemStack stack : CurrencyUtils.itemMoneyAmount(playerAmount)) {
			dropMoneyAmount(playerAmount, player.getEntityWorld(), player.posX, player.posY, player.posZ);
		}
	}

	public static float getCurrency(EntityPlayer player) {
		float currencyTotal = 0;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
				ItemMoneyBase money = (ItemMoneyBase) player.inventory.getStackInSlot(i).getItem();
				currencyTotal += (money.getValue() * player.inventory.getStackInSlot(i).getCount());
			}
		}

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemWallet) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack.hasTagCompound()) {
					InventoryItem inventory = new InventoryItem(stack);

					for (int i1 = 0; i1 < inventory.getSizeInventory(); i1++) {
						if (inventory.getStackInSlot(i1) != ItemStack.EMPTY && inventory.getStackInSlot(i1).getItem() instanceof ItemMoneyBase) {
							currencyTotal += ((ItemMoneyBase) inventory.getStackInSlot(i1).getItem()).getValue() * inventory.getStackInSlot(i1).getCount();
						}
					}
				}
			}
		}
		return currencyTotal;
	}

	public static float getCurrencyNoWallet(EntityPlayer player) {
		float currencyTotal = 0;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
				ItemMoneyBase money = (ItemMoneyBase) player.inventory.getStackInSlot(i).getItem();
				currencyTotal += (money.getValue() * player.inventory.getStackInSlot(i).getCount());
			}
		}
		return currencyTotal;
	}

	public static void dropMoneyAmount(float value, World world, double x, double y, double z) {
		float newValue = value;

		if (value >= 100) {
			EntityItem hundreds = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.hundredDollarBill, (int) Math.floor(value / 100)));
			world.spawnEntity(hundreds);
			newValue -= (100 * Math.floor(newValue / 100));
		}

		if (newValue >= 50) {
			EntityItem fifties = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.fiftyDollarBill, (int) Math.floor(newValue / 50)));
			world.spawnEntity(fifties);
			newValue -= (50 * Math.floor(newValue / 50));
		}

		if (newValue >= 20) {
			EntityItem twenties = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.twentyDollarBill, (int) Math.floor(newValue / 20)));
			world.spawnEntity(twenties);
			newValue -= (20 * Math.floor(newValue / 20));
		}

		if (newValue >= 10) {
			EntityItem tens = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.tenDollarBill, (int) Math.floor(newValue / 10)));
			world.spawnEntity(tens);
			newValue -= (10 * Math.floor(newValue / 10));
		}

		if (newValue >= 5) {
			EntityItem fives = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.fiveDollarBill, (int) Math.floor(newValue / 5)));
			world.spawnEntity(fives);
			newValue -= (5 * Math.floor(newValue / 5));
		}

		if (newValue >= 1) {
			EntityItem dollars = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.dollarBill, (int) Math.floor(newValue)));
			world.spawnEntity(dollars);
			newValue -= (1F * Math.floor(newValue));
		}

		EntityItem quarters = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.quarter, (int) (newValue / 0.25F)));
		world.spawnEntity(quarters);
		newValue -= (0.25F * (int) (newValue / 0.25F));

		EntityItem dimes = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.dime, (int) (newValue / 0.10F)));
		world.spawnEntity(dimes);
		newValue -= (0.10F * (int) (newValue / 0.10F));

		EntityItem nickels = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.nickel, (int) (newValue / 0.05F)));
		world.spawnEntity(nickels);
		newValue -= (0.05F * (int) (newValue / 0.05F));

		EntityItem pennies = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.penny, (int) (newValue / 0.01F)));
		world.spawnEntity(pennies);
		newValue -= (0.01F * (int) (newValue / 0.01F));
	}

	public static ArrayList<ItemStack> itemMoneyAmount(float value) {
		float newValue = value;

		ArrayList items = new ArrayList<ItemStack>();

		if (value >= 100) {
			items.add(new ItemStack(ItemHandler.hundredDollarBill, (int) Math.floor(value / 100)));
			newValue -= (100 * Math.floor(newValue / 100));
		}

		if (newValue >= 50) {
			items.add(new ItemStack(ItemHandler.fiftyDollarBill, (int) Math.floor(newValue / 50)));
			newValue -= (50 * Math.floor(newValue / 50));
		}

		if (newValue >= 20) {
			items.add(new ItemStack(ItemHandler.twentyDollarBill, (int) Math.floor(newValue / 20)));
			newValue -= (20 * Math.floor(newValue / 20));
		}

		if (newValue >= 10) {
			items.add(new ItemStack(ItemHandler.tenDollarBill, (int) Math.floor(newValue / 10)));
			newValue -= (10 * Math.floor(newValue / 10));
		}

		if (newValue >= 5) {
			items.add(new ItemStack(ItemHandler.fiveDollarBill, (int) Math.floor(newValue / 5)));
			newValue -= (5 * Math.floor(newValue / 5));
		}

		if (newValue >= 1) {
			items.add(new ItemStack(ItemHandler.dollarBill, (int) Math.floor(newValue)));
			newValue -= (1F * Math.floor(newValue));
		}

		items.add(new ItemStack(ItemHandler.quarter, (int) (newValue / 0.25F)));
		newValue -= (0.25F * (int) (newValue / 0.25F));

		items.add(new ItemStack(ItemHandler.dime, (int) (newValue / 0.10F)));
		newValue -= (0.10F * (int) (newValue / 0.10F));

		items.add(new ItemStack(ItemHandler.nickel, (int) (newValue / 0.05F)));
		newValue -= (0.05F * (int) (newValue / 0.05F));

		items.add(new ItemStack(ItemHandler.penny, (int) (newValue / 0.01F)));
		newValue -= (0.01F * (int) (newValue / 0.01F));

		return items;
	}

	public static void subtractFromInventory(EntityPlayer playerIn, float amount) {
		float amountToGiveBack = getCurrencyNoWallet(playerIn) - amount;

		if (amountToGiveBack >= 0) {
			for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
				if (playerIn.inventory.getStackInSlot(i) != ItemStack.EMPTY && playerIn.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
					playerIn.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}

			for (ItemStack stack : CurrencyUtils.itemMoneyAmount(amountToGiveBack)) {
				playerIn.addItemStackToInventory(stack);
			}
		}
	}
}
