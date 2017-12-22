package com.zundrel.currency.common.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

public class ItemScanner extends ItemBasic {
	public ItemScanner(String unlocalizedName)
    {
        super(unlocalizedName, 1);
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (playerIn.isSneaking()) {
			CartCapability entityData = playerIn.getCapability(Currency.CART_DATA, null);
			
			entityData.setCart(NonNullList.withSize(entityData.getSizeInventory(), ItemStack.EMPTY), true);
			List<Float> prices = Arrays.asList(new Float[25]);
			for (int i = 0; i < prices.size(); i++) {
				prices.set(i, (float) 0);
			}
			entityData.setPrices(prices, true);
			
			playerIn.sendStatusMessage(new TextComponentString(TextFormatting.UNDERLINE + "Emptied Scanner list."), true);
		} else if (worldIn.isRemote) {
			CartCapability entityData = playerIn.getCapability(Currency.CART_DATA, null);
			
			boolean isEmpty = true;
			
			for (ItemStack stack : entityData.getCart()) {
				if (!stack.isEmpty()) {
					isEmpty = false;
				}
			}
			
			if (!isEmpty) {
				playerIn.openGui(Currency.INSTANCE, 5, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ); 
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn,
			List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("The Scanner is capable of scanning items to go into your cart.");
	}
}
