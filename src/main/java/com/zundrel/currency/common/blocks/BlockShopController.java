package com.zundrel.currency.common.blocks;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.handlers.SoundHandler;
import com.zundrel.currency.common.utils.CurrencyUtils;

public class BlockShopController extends BlockBasic implements ITileEntityProvider {
	public BlockShopController(String unlocalizedName, Material material, float hardness, SoundType type, CreativeTabs tab) {
		super(unlocalizedName, material, hardness, type, tab);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntityShopController controller = (TileEntityShopController) worldIn.getTileEntity(pos);

		controller.setName("Shop Controller");
		if (placer instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) placer;
			controller.setOwnerUUID(player.getUniqueID());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityShopController te = (TileEntityShopController) worldIn.getTileEntity(pos);

		if (!worldIn.isRemote && playerIn.getHeldItem(hand).isEmpty()) {
			CartCapability cap = playerIn.getCapability(Currency.CART_DATA, null);

			boolean receivedItems = false;

			float overallAmount = 0;

			for (BlockPos stockPos : te.storageBlocks) {
				if (worldIn.getBlockState(stockPos).getBlock() instanceof BlockStockCrate) {
					TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(stockPos);

					float amount = crate.getAmount();
					EntityPlayer shopOwner = null;

					List<EntityPlayerMP> allPlayers = worldIn.getMinecraftServer().getPlayerList().getPlayers();
					for (EntityPlayerMP player : allPlayers) {
						if (player.getUniqueID().equals(crate.getOwnerUUID())) {
							shopOwner = player;
						}
					}

					if (shopOwner != null && !crate.getType().isEmpty() && !cap.getCart().isEmpty()) {
						for (int i = 0; i < cap.getSizeInventory(); i++) {
							ItemStack cartStack = cap.getStackInSlot(i);
							if (!cartStack.isEmpty() && cartStack.getItem() == crate.getType().getItem() && CurrencyUtils.getCurrencyNoWallet(playerIn) >= amount * cartStack.getCount()) {
								ItemStack stockStack = ItemStack.EMPTY;
								int stockSlot = 0;

								for (int j = 0; j < crate.getSizeInventory(); j++) {
									if (!crate.getStackInSlot(j).isEmpty() && cartStack.getItem() == crate.getStackInSlot(j).getItem() && crate.getStackInSlot(j).getCount() >= cartStack.getCount()) {
										stockStack = crate.getStackInSlot(j);
										stockSlot = j;
										break;
									}
								}

								if (!stockStack.isEmpty()) {
									playerIn.sendMessage(new TextComponentString("Purchased " + cartStack.getCount() + "x " + TextFormatting.GREEN + cartStack.getDisplayName() + TextFormatting.RESET + " for " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(amount * cartStack.getCount()) + TextFormatting.RESET + "."));
									if (amount != 0) {
										worldIn.getMinecraftServer().getPlayerList().getPlayerByUUID(crate.getOwnerUUID()).sendMessage(new TextComponentString("You have had " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(amount * cartStack.getCount()) + TextFormatting.RESET + " deposited into your account from a shop transaction."));
									} else {
										worldIn.getMinecraftServer().getPlayerList().getPlayerByUUID(crate.getOwnerUUID()).sendMessage(new TextComponentString("You have given away a free item(s). (If this isn't intentional, check your stock pricing.)"));
									}
									crate.getStackInSlot(stockSlot).shrink(cartStack.getCount());
									CurrencyUtils.subtractFromInventory(playerIn, amount * cartStack.getCount());
									overallAmount = amount * cartStack.getCount();
									worldIn.getMinecraftServer().getPlayerList().getPlayerByUUID(crate.getOwnerUUID()).getCapability(Currency.ACCOUNT_DATA, null).addAmount(amount * cartStack.getCount(), true);
									playerIn.addItemStackToInventory(cartStack);
									receivedItems = true;
									cap.setStackInSlot(i, ItemStack.EMPTY, 0, true);

									worldIn.playSound(null, pos, SoundHandler.register, SoundCategory.NEUTRAL, 1, 1);
								}
							}
						}
					} else if (shopOwner == null) {

					}

					if (shopOwner != null && receivedItems) {

					}
				}
			}

			if (!receivedItems) {
				boolean cartEmpty = true;

				for (int i = 0; i < cap.getSizeInventory(); i++) {
					ItemStack cartStack = cap.getStackInSlot(i);
					if (!cartStack.isEmpty()) {
						cartEmpty = false;
						overallAmount = overallAmount + (cap.getPrices().get(i) * cartStack.getCount());
					}
				}

				if (!cartEmpty) {
					playerIn.sendStatusMessage(new TextComponentString("Insufficient funds. You need at least " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(overallAmount) + TextFormatting.RESET + "."), true);
				}
			}
		}
		// } else if (playerIn.getUniqueID().equals(te.getOwnerUUID())) {
		// playerIn.sendStatusMessage(new
		// TextComponentString("You can't buy from your own shop!"), true);
		// }

		if (playerIn.isSneaking() && te.getOwnerUUID().equals(playerIn.getUniqueID())) {
			if (worldIn.isRemote) {
				playerIn.openGui(Currency.INSTANCE, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityShopController();
	}
}
