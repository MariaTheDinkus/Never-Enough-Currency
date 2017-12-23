package com.zundrel.currency.common.items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMoneyBase extends ItemBasic {
	private float value = 0;

	public ItemMoneyBase(String unlocalizedName, float value) {
		super(unlocalizedName, 64);

		this.value = value;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
		tooltip.add("Value ($): " + fmt.format(value));
	}

	public float getValue() {
		return value;
	}
}
