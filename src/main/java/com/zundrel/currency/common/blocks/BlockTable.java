package com.zundrel.currency.common.blocks;

import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.blocks.tiles.TileEntityDisplay;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.handlers.SoundHandler;
import com.zundrel.currency.common.items.ItemLinkingCard;
import com.zundrel.currency.common.items.ItemScanner;

public class BlockTable extends BlockBasic implements ITileEntityProvider {
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");

	public BlockTable(String unlocalizedName, Material material, float hardness, SoundType type, CreativeTabs tab) {
		super(unlocalizedName, material, hardness, type, tab);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, SOUTH, WEST });
	}
	
	public boolean isAdjacentBlockOfMyType(IBlockAccess world, BlockPos position, EnumFacing facing) {

        assert null != world : "world cannot be null";
        assert null != position : "position cannot be null";
        assert null != this : "type cannot be null";

        BlockPos newPosition = position.offset(facing);
        IBlockState blockState = world.getBlockState(newPosition);
        Block block = (null == blockState) ? null : blockState.getBlock();
        
        System.out.println(this == block);
        
        return this == block;
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState newState = state;

		state = state
                .withProperty(EAST, this.isAdjacentBlockOfMyType(worldIn, pos, EnumFacing.EAST))
                .withProperty(NORTH, this.isAdjacentBlockOfMyType(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, this.isAdjacentBlockOfMyType(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(WEST, this.isAdjacentBlockOfMyType(worldIn, pos, EnumFacing.WEST));

		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntityDisplay table = (TileEntityDisplay) worldIn.getTileEntity(pos);

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
					table.setShopControllerPos(controllerPos);
				}
			}
		}

		if (placer instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) placer;
			table.setOwnerUUID(player.getUniqueID());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityDisplay table = (TileEntityDisplay) worldIn.getTileEntity(pos);

		if (!playerIn.getHeldItem(hand).isEmpty()) {
			if (playerIn.getHeldItem(hand).getItem() instanceof ItemLinkingCard && playerIn.getHeldItem(hand).hasTagCompound()) {
				BlockPos shopControllerPos = new BlockPos(playerIn.getHeldItem(hand).getTagCompound().getInteger("x"), playerIn.getHeldItem(hand).getTagCompound().getInteger("y"), playerIn.getHeldItem(hand).getTagCompound().getInteger("z"));
				if (!shopControllerPos.equals(table.getShopControllerPos()) && worldIn.getBlockState(shopControllerPos).getBlock() instanceof BlockShopController) {
					if (((TileEntityShopController) worldIn.getTileEntity(shopControllerPos)).getOwnerUUID().equals(table.getOwnerUUID())) {
						if (worldIn.getTileEntity(table.getShopControllerPos()) instanceof TileEntityShopController) {
							TileEntityShopController prevController = (TileEntityShopController) worldIn.getTileEntity(table.getShopControllerPos());
							for (BlockPos linkedPos : prevController.linkedBlocks) {
								if (table.getShopControllerPos() != null && linkedPos != null && table.getShopControllerPos().getX() == linkedPos.getX() && table.getShopControllerPos().getY() == linkedPos.getY() && table.getShopControllerPos().getZ() == linkedPos.getZ()) {
									prevController.linkedBlocks.add(pos);
									prevController.markDirty();
									break;
								}
							}
							prevController.linkedBlocks.remove(table.getPos());
						}
						((TileEntityShopController) worldIn.getTileEntity(shopControllerPos)).linkedBlocks.add(pos);
						table.setShopControllerPos(shopControllerPos);
						return true;
					}
				}
			}
		}

		int slot = -1;

		if (hitX > 0.1F && hitX < 0.4F && hitZ < 0.9F && hitZ > 0.6F) {
			slot = 2;
		} else if (hitX > 0.6F && hitX < 0.9F && hitZ < 0.9F && hitZ > 0.6F) {
			slot = 3;
		} else if (hitX > 0.1F && hitX < 0.4F && hitZ < 0.4F && hitZ > 0.1F) {
			slot = 0;
		} else if (hitX > 0.6F && hitX < 0.9F && hitZ < 0.4F && hitZ > 0.1F) {
			slot = 1;
		}

		if (slot < 0) {
			return false;
		}

		if (!playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() instanceof ItemBlock && Block.getBlockFromItem(playerIn.getHeldItem(hand).getItem()) instanceof BlockTable) {
			return false;
		}

		if (!playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() instanceof ItemScanner) {
			CartCapability entityData = playerIn.getCapability(Currency.CART_DATA, null);
			if (entityData.getCart() != null && slot != -1 && !table.getStackInSlot(slot).isEmpty()) {
				ItemStack copy = table.getStackInSlot(slot).copy();

				int amount = 0;

				for (ItemStack cartStack : entityData.getCart()) {
					if (!cartStack.isEmpty()) {
						amount++;
					}
				}

				if (amount >= 25) {
					return false;
				}

				boolean sameType = false;
				for (ItemStack cartStack : entityData.getCart()) {
					if (!cartStack.isEmpty() && copy.getItem() == cartStack.getItem() && copy.getMetadata() == cartStack.getMetadata()) {
						if (cartStack.getCount() < cartStack.getMaxStackSize()) {
							cartStack.grow(1);
							worldIn.playSound(null, pos, SoundHandler.scanner, SoundCategory.NEUTRAL, 1, (float) (0.98F + Math.random() * (1 - 0.98F)));
							if (!worldIn.isRemote) {
								playerIn.sendStatusMessage(new TextComponentString("Added 1x " + TextFormatting.GREEN + copy.getDisplayName() + TextFormatting.RESET + " to your list. You now have " + cartStack.getCount() + "x " + TextFormatting.GREEN + copy.getDisplayName() + TextFormatting.RESET + " on your list."), true);
							}
							entityData.sendPacket();
						} else {
							worldIn.playSound(null, pos, SoundHandler.scanner, SoundCategory.NEUTRAL, 1, (float) (0.98F + Math.random() * (1 - 0.98F)));
							if (!worldIn.isRemote) {
								playerIn.sendStatusMessage(new TextComponentString("Your " + TextFormatting.GREEN + copy.getDisplayName() + TextFormatting.RESET + " stack is already full."), true);
							}
						}
						sameType = true;
						break;
					}
				}

				if (!sameType) {
					copy.setCount(1);

					if (!worldIn.isOutsideBuildHeight(table.getShopControllerPos()) && worldIn.getTileEntity(table.getShopControllerPos()) instanceof TileEntityShopController) {
						TileEntityShopController controller = (TileEntityShopController) worldIn.getTileEntity(table.getShopControllerPos());

						worldIn.playSound(null, pos, SoundHandler.scanner, SoundCategory.NEUTRAL, 1, (float) (0.98F + Math.random() * (1 - 0.98F)));
						if (!worldIn.isRemote) {
							playerIn.sendStatusMessage(new TextComponentString("Added 1x " + TextFormatting.GREEN + copy.getDisplayName() + TextFormatting.RESET + " to your list."), true);
						}

						for (BlockPos stockPos : controller.storageBlocks) {
							if (worldIn.getTileEntity(stockPos) instanceof TileEntityStockCrate) {
								TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(stockPos);

								if (!crate.getType().isEmpty() && crate.getType().getItem() == copy.getItem()) {
									entityData.addStackToCart(copy, crate.getAmount(), true);
									return true;
								}
							}
						}
						entityData.addStackToCart(copy, true);
					}
				}
			}
		} else if (table.getOwnerUUID().equals(playerIn.getUniqueID()) && table.getStackInSlot(slot).isEmpty() && !playerIn.getHeldItem(hand).isEmpty() && !(playerIn.getHeldItem(hand).getItem() instanceof ItemLinkingCard) && !(playerIn.getHeldItem(hand).getItem() instanceof ItemScanner)) {
			if (!playerIn.isSneaking()) {
				ItemStack copy = playerIn.getHeldItem(hand).copy();

				copy.setCount(1);

				table.setInventorySlotContents(slot, copy);

				if (!worldIn.isRemote) {
					playerIn.sendStatusMessage(new TextComponentString("Slot " + (slot + 1) + " has been set to " + TextFormatting.GREEN + copy.getDisplayName() + TextFormatting.RESET + "."), true);
				}
			}
		} else if (table.getOwnerUUID().equals(playerIn.getUniqueID()) && playerIn.getHeldItem(hand).isEmpty()) {
			if (playerIn.isSneaking()) {
				table.setInventorySlotContents(slot, ItemStack.EMPTY);

				if (!worldIn.isRemote) {
					playerIn.sendStatusMessage(new TextComponentString("Cleared slot " + (slot + 1) + "."), true);
				}
			} else if (!table.getStackInSlot(slot).isEmpty()) {
				if (!worldIn.isRemote) {
					float amount = 0;
					if (worldIn.getTileEntity(table.getShopControllerPos()) instanceof TileEntityShopController) {
						TileEntityShopController controller = (TileEntityShopController) worldIn.getTileEntity(table.getShopControllerPos());
						for (BlockPos stockPos : controller.storageBlocks) {
							if (worldIn.getTileEntity(stockPos) instanceof TileEntityStockCrate) {
								TileEntityStockCrate crate = (TileEntityStockCrate) worldIn.getTileEntity(stockPos);
								if (!crate.getType().isEmpty() && table.getStackInSlot(slot).getItem() == crate.getType().getItem()) {
									amount = crate.getAmount();
									break;
								}
							}
						}
						
						playerIn.sendStatusMessage(new TextComponentString("Slot " + (slot + 1) + " contains " + TextFormatting.GREEN + table.getStackInSlot(slot).getDisplayName() + TextFormatting.RESET + ". It is selling for " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + TextFormatting.RESET + "."), true);
					} else {
						playerIn.sendStatusMessage(new TextComponentString("Slot " + (slot + 1) + " contains " + TextFormatting.GREEN + table.getStackInSlot(slot).getDisplayName() + TextFormatting.RESET + ". It is selling for " + TextFormatting.GREEN + "$0.00" + TextFormatting.RESET + "."), true);
					}
				}
			}
		}

		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityDisplay shelf = (TileEntityDisplay) worldIn.getTileEntity(pos);

		if (shelf.getShopControllerPos() != null && worldIn.getTileEntity(shelf.getShopControllerPos()) instanceof TileEntityShopController) {
			TileEntityShopController prevController = (TileEntityShopController) worldIn.getTileEntity(shelf.getShopControllerPos());
			prevController.linkedBlocks.remove(shelf.getPos());
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDisplay();
	}
}
