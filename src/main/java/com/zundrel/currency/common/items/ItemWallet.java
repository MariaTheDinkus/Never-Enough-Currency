package com.zundrel.currency.common.items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.inventory.InventoryItem;

public class ItemWallet extends ItemBasic {
	public ItemWallet(String unlocalizedName) {
		super(unlocalizedName, 1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			if (!playerIn.isSneaking()) {
				if (hand == EnumHand.MAIN_HAND) {
					System.out.println("HEY");
					playerIn.openGui(Currency.INSTANCE, 1, worldIn, 0, 0, 0);
				} else if (hand == EnumHand.OFF_HAND) {
					playerIn.openGui(Currency.INSTANCE, 2, worldIn, 0, 0, 0);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Extra storage for your money!");

		if (stack.hasTagCompound()) {
			InventoryItem inventory = new InventoryItem(stack);

			float value = 0;
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory.getStackInSlot(i) != ItemStack.EMPTY && inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
					value += ((ItemMoneyBase) inventory.getStackInSlot(i).getItem()).getValue() * inventory.getStackInSlot(i).getCount();
				}
			}

			tooltip.add("Currently storing: " + NumberFormat.getCurrencyInstance(Locale.US).format(value));
		}

		tooltip.add(TextFormatting.BLUE + "Only holds currency.");
	}
}
