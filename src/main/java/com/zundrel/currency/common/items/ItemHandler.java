package com.zundrel.currency.common.items;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import com.zundrel.currency.common.blocks.BlockBasic;
import com.zundrel.currency.common.info.ModInfo;

@EventBusSubscriber(modid = ModInfo.MODID)
public class ItemHandler {
	public static IForgeRegistry<Item> registry;
	public static ArrayList<ItemBlock> itemBlocks = new ArrayList<ItemBlock>();

	public static Item wallet;
	public static Item linkingCard;
	public static Item shopping_list;

	public static Item penny, nickel, dime, quarter;
	public static Item dollarBill, fiveDollarBill, tenDollarBill,
			twentyDollarBill, fiftyDollarBill, hundredDollarBill;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		registry = event.getRegistry();

		wallet = register(new ItemWallet("wallet"));
		linkingCard = register(new ItemLinkingCard("linking_card"));

		penny = register(new ItemMoneyBase("penny", 0.01F));
		nickel = register(new ItemMoneyBase("nickel", 0.05F));
		dime = register(new ItemMoneyBase("dime", 0.10F));
		quarter = register(new ItemMoneyBase("quarter", 0.25F));

		dollarBill = register(new ItemMoneyBase("dollar_bill", 1F));
		fiveDollarBill = register(new ItemMoneyBase("five_dollar_bill", 5F));
		tenDollarBill = register(new ItemMoneyBase("ten_dollar_bill", 10F));
		twentyDollarBill = register(new ItemMoneyBase("twenty_dollar_bill", 20F));
		fiftyDollarBill = register(new ItemMoneyBase("fifty_dollar_bill", 50F));
		hundredDollarBill = register(new ItemMoneyBase("hundred_dollar_bill", 100F));

		for (ItemBlock ib : itemBlocks) {
			registry.register(ib);
			if (ib.getBlock() instanceof BlockBasic) {
				((BlockBasic) ib.getBlock()).registerItemModel(ib);
			}
		}
	}

	public static <T extends Item> T register(T i) {
		registry.register(i);
		if (i instanceof ItemBasic) {
			((ItemBasic) i).registerItemModel(i);
		}
		return i;
	}
}
