package com.zundrel.currency.common.items;

import net.minecraft.item.Item;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.info.ModInfo;

public class ItemBasic extends Item
{
	public ItemBasic(String unlocalizedName, int maxStackSize)
	{
		super();
		setRegistryName(unlocalizedName);
		setCreativeTab(Currency.tabGeneral);
		setUnlocalizedName(this.getRegistryName().toString().replace(ModInfo.MODID + ":", ""));
		setMaxStackSize(maxStackSize);
	}
	
	public void registerItemModel(Item i) {
		Currency.proxy.registerItemRenderer(i, 0, this.getUnlocalizedName().substring(5));
	}
}