package com.momnop.currency;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.momnop.currency.items.ItemHandler;

public class CurrencyCreativeTab extends CreativeTabs {
	List list;
	public static CurrencyCreativeTab INSTANCE = new CurrencyCreativeTab();

	public CurrencyCreativeTab() {
		super("currency");
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(ItemHandler.penny);
	}

	@Override
	public Item getTabIconItem() {
		return getIconItemStack().getItem();
	}

	@Override
	public void displayAllReleventItems(List list) {
		this.list = list;

		addItem(ItemHandler.penny);
		addItem(ItemHandler.nickel);
		addItem(ItemHandler.dime);
		addItem(ItemHandler.quarter);

		addItem(ItemHandler.dollarBill);
		addItem(ItemHandler.fiveDollarBill);
		addItem(ItemHandler.tenDollarBill);
		addItem(ItemHandler.twentyDollarBill);
		addItem(ItemHandler.fiftyDollarBill);
		addItem(ItemHandler.hundredDollarBill);
	}

	private void addItem(Item item) {
		item.getSubItems(item, this, this.list);
	}

	private void addBlock(Block block) {
		ItemStack stack = new ItemStack(block);
		block.getSubBlocks(stack.getItem(), this, this.list);
	}
}
