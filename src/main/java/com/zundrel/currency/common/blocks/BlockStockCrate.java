package com.zundrel.currency.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.items.ItemLinkingCard;

public class BlockStockCrate extends BlockBasic implements ITileEntityProvider {

	public BlockStockCrate(String unlocalizedName, Material material, float hardness, SoundType type, CreativeTabs tab) {
		super(unlocalizedName, material, hardness, type, tab);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(pos);

		if (placer instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) placer;
			ItemStack linkingCard = ItemStack.EMPTY;
			for (ItemStack stacks : player.inventory.mainInventory) {
				if (!stacks.isEmpty() && stacks.getItem() instanceof ItemLinkingCard) {
					linkingCard = stacks;
					break;
				}
			}
			if (linkingCard.hasTagCompound()) {
				NBTTagCompound compound = linkingCard.getTagCompound();
				BlockPos controllerPos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));

				if (compound.getBoolean("automatic") && worldIn.getBlockState(controllerPos).getBlock() instanceof BlockShopController) {
					crate.setShopControllerPos(controllerPos);
				}
			}
		}

		if (placer instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) placer;
			crate.setOwnerUUID(player.getUniqueID());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(pos);

		if (!playerIn.getHeldItem(hand).isEmpty()) {
			if (playerIn.getHeldItem(hand).getItem() instanceof ItemLinkingCard && playerIn.getHeldItem(hand).hasTagCompound()) {
				BlockPos shopControllerPos = new BlockPos(playerIn.getHeldItem(hand).getTagCompound().getInteger("x"), playerIn.getHeldItem(hand).getTagCompound().getInteger("y"), playerIn.getHeldItem(hand).getTagCompound().getInteger("z"));
				if (!shopControllerPos.equals(crate.getShopControllerPos()) && worldIn.getBlockState(shopControllerPos).getBlock() instanceof BlockShopController) {
					if (((TileEntityShopController) worldIn.getTileEntity(shopControllerPos)).getOwnerUUID().equals(crate.getOwnerUUID())) {
						crate.setShopControllerPos(shopControllerPos);
						return true;
					}
				}
			}
		}

		if (!worldIn.isRemote && crate.getOwnerUUID().equals(playerIn.getUniqueID())) {
			playerIn.openGui(Currency.INSTANCE, 4, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(pos);

		if (crate instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, crate);
		}

		if (crate.getShopControllerPos() != null && worldIn.getTileEntity(crate.getShopControllerPos()) instanceof TileEntityShopController) {
			TileEntityShopController prevController = (TileEntityShopController) worldIn.getTileEntity(crate.getShopControllerPos());
			prevController.storageBlocks.remove(crate.getPos());
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityStockCrate();
	}
}
