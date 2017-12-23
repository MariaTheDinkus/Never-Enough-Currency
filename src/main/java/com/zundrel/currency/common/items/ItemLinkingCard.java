package com.zundrel.currency.common.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zundrel.currency.common.blocks.BlockShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;

public class ItemLinkingCard extends ItemBasic {
	public ItemLinkingCard(String unlocalizedName) {
		super(unlocalizedName, 1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.getBlockState(pos).getBlock() instanceof BlockShopController) {
			if (player.getHeldItem(hand).hasTagCompound()) {
				player.getHeldItem(hand).getTagCompound().setInteger("x", pos.getX());
				player.getHeldItem(hand).getTagCompound().setInteger("y", pos.getY());
				player.getHeldItem(hand).getTagCompound().setInteger("z", pos.getZ());
				if (world.isRemote) {
					player.sendMessage(new TextComponentString("Linking card has been linked to the position " + player.getHeldItem(hand).getTagCompound().getInteger("x") + ", " + player.getHeldItem(hand).getTagCompound().getInteger("y") + ", " + player.getHeldItem(hand).getTagCompound().getInteger("z") + "."));
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking() && stack.hasTagCompound()) {
			stack.getTagCompound().setBoolean("automatic", !stack.getTagCompound().getBoolean("automatic"));
			if (worldIn.isRemote) {
				playerIn.sendMessage(new TextComponentString("Automatic mode has been toggled to " + stack.getTagCompound().getBoolean("automatic") + "."));
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	};

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			BlockPos pos = new BlockPos(stack.getTagCompound().getInteger("x"), stack.getTagCompound().getInteger("y"), stack.getTagCompound().getInteger("z"));

			if (!world.isOutsideBuildHeight(pos) && world.getTileEntity(pos) instanceof TileEntityShopController) {
				tooltip.add("Currently linked to a shop controller at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
				tooltip.add("Shop Name: " + ((TileEntityShopController) world.getTileEntity(pos)).getName());
				tooltip.add("Shop Owner: " + ((TileEntityShopController) world.getTileEntity(pos)).getOwnerName());
			}
			tooltip.add("Automatic Mode: " + stack.getTagCompound().getBoolean("automatic"));
			tooltip.add("This makes it automatically set a new block's controller pos when placed.");
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		initTagCompound(stack);
	}

	public static void initTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("x", 0);
			stack.getTagCompound().setInteger("y", 0);
			stack.getTagCompound().setInteger("z", 0);
			stack.getTagCompound().setBoolean("automatic", false);
		}
	}
}
