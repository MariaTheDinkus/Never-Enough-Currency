package com.zundrel.currency.common.blocks.tiles;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.zundrel.currency.Currency;

public class TileEntityStockCrate extends TileEntity implements IInventory, ITickable {
	private UUID ownerUUID;
	private BlockPos shopControllerPos;
	private NonNullList<ItemStack> inventory;
	private ItemStack type;
	private float amount;

	@Override
	public void validate() {
		if (!world.isRemote)
			Currency.forceChunkLoad(world, new ChunkPos(pos));
		super.validate();
	}

	@Override
	public void invalidate() {
		if (!world.isRemote)
			Currency.releaseChunkLoad(world, new ChunkPos(pos));
		super.invalidate();
	}

	public TileEntityStockCrate() {
		inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		type = ItemStack.EMPTY;
	}

	public ItemStack getType() {
		return type;
	}

	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	@Override
	public void update() {
		boolean isEmpty = true;

		// System.out.println("HEH");

		// System.out.println(amount);

		if (this.world.getTotalWorldTime() % 100 == 0) {
			// if (!world.isOutsideBuildHeight(shopControllerPos) &&
			// world.getTileEntity(shopControllerPos) instanceof
			// TileEntityShopController) {
			// TileEntityShopController shopController =
			// (TileEntityShopController)
			// world.getTileEntity(shopControllerPos);
			//
			// boolean containsPos = false;
			//
			// for (BlockPos pos : shopController.storageBlocks) {
			// if (pos == this.pos) {
			// containsPos = true;
			// break;
			// }
			// }
			//
			// if (!containsPos) {
			// shopController.storageBlocks.add(this.pos);
			// }
			// }

			for (ItemStack stack : inventory) {
				if (!stack.isEmpty()) {
					this.type = stack;
					isEmpty = false;
					break;
				}
			}

			if (isEmpty) {
				this.type = ItemStack.EMPTY;
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setString("ownerUUID", ownerUUID.toString());

		compound.setFloat("amount", amount);

		if (shopControllerPos != null) {
			compound.setInteger("shopx", shopControllerPos.getX());
			compound.setInteger("shopy", shopControllerPos.getY());
			compound.setInteger("shopz", shopControllerPos.getZ());
		}

		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			if (!getStackInSlot(i).isEmpty()) {
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				getStackInSlot(i).writeToNBT(item);

				items.appendTag(item);
			}
		}
		compound.setTag("ItemInventory", items);

		NBTTagCompound type = new NBTTagCompound();

		this.type.writeToNBT(type);

		compound.setTag("type", type);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		ownerUUID = UUID.fromString(compound.getString("ownerUUID"));

		amount = compound.getFloat("amount");

		shopControllerPos = new BlockPos(compound.getInteger("shopx"), compound.getInteger("shopy"), compound.getInteger("shopz"));

		NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			if (slot >= 0 && slot < getSizeInventory()) {
				inventory.set(slot, new ItemStack(item));
			}
		}

		NBTTagCompound type = compound.getCompoundTag("type");

		this.type = new ItemStack(type);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
		final int METADATA = 0;
		return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
		handleUpdateTag(updateTagDescribingTileEntityState);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	public void setShopControllerPos(BlockPos shopControllerPos) {
		BlockPos prevShopControllerPos = this.shopControllerPos;
		this.shopControllerPos = shopControllerPos;

		boolean crateExists = false;

		for (BlockPos stockPos : ((TileEntityShopController) world.getTileEntity(shopControllerPos)).storageBlocks) {
			if (stockPos == pos) {
				crateExists = true;
			}
		}

		if (!crateExists) {
			((TileEntityShopController) this.world.getTileEntity(shopControllerPos)).storageBlocks.add(this.pos);
		}
		markDirty();
	}

	public BlockPos getShopControllerPos() {
		return shopControllerPos;
	}

	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		markDirty();
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public String getOwnerName() {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getProfileByUUID(ownerUUID).getName() != null) {
			return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getProfileByUUID(ownerUUID).getName();
		} else {
			return "None";
		}
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
		markDirty();
	}

	@Override
	public String getName() {
		return "Stock Crate";
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index < 0 || index >= this.getSizeInventory()) {
			return ItemStack.EMPTY;
		}

		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (!this.getStackInSlot(index).isEmpty()) {
			ItemStack itemstack;

			if (this.getStackInSlot(index).getCount() <= count) {
				itemstack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, ItemStack.EMPTY);
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.getStackInSlot(index).splitStack(count);

				if (this.getStackInSlot(index).getCount() <= 0) {
					this.setInventorySlotContents(index, ItemStack.EMPTY);
				} else {
					this.setInventorySlotContents(index, this.getStackInSlot(index));
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory()) {
			return;
		}

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (!stack.isEmpty() && stack.getCount() == 0) {
			stack = ItemStack.EMPTY;
		}

		this.inventory.set(index, stack);
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (!stack.isEmpty() && !type.isEmpty() && stack.getItem() == type.getItem()) {
			return true;
		} else if (type.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}
}
