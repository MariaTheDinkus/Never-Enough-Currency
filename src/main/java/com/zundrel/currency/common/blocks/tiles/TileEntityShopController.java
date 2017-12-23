package com.zundrel.currency.common.blocks.tiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.zundrel.currency.Currency;

public class TileEntityShopController extends TileEntity implements ITickable {
	private String name;
	private UUID ownerUUID;
	public Set<BlockPos> linkedBlocks = new HashSet<BlockPos>();
	public Set<BlockPos> storageBlocks = new HashSet<BlockPos>();

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

	@Override
	public void update() {
		markDirty();

		// for (BlockPos pos : linkedBlocks) {
		// System.out.println(name + ", " + "Linked: " +
		// this.getWorld().getBlockState(pos).getBlock().getLocalizedName() +
		// ", " + pos);
		// }
		//
		// for (BlockPos pos : storageBlocks) {
		// System.out.println(name + ", " + "Linked Stock: " +
		// this.getWorld().getBlockState(pos).getBlock().getLocalizedName() +
		// ", " + pos);
		// }

		if (!this.world.isRemote) {
			// for (BlockPos stockPos : storageBlocks) {
			// TileEntityStockCrate crate = (TileEntityStockCrate)
			// world.getTileEntity(stockPos);
			//
			// for (ItemStack stack : crate.getInventory()) {
			// if (!stack.isEmpty()) {
			// System.out.println(stack.getDisplayName());
			// }
			// }
			// }
		}

		// if (this.world.getTotalWorldTime() % 6000 == 0) {
		// for (BlockPos pos : storageBlocks) {
		// if (!(this.world.getBlockState(pos).getBlock() instanceof
		// BlockStockCrate)) {
		// storageBlocks.remove(pos);
		// }
		// }
		// }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setString("name", name);
		compound.setString("ownerUUID", ownerUUID.toString());

		NBTTagList positions = new NBTTagList();

		for (BlockPos linkedPos : linkedBlocks) {
			NBTTagCompound pos = new NBTTagCompound();

			pos.setInteger("x", linkedPos.getX());
			pos.setInteger("y", linkedPos.getY());
			pos.setInteger("z", linkedPos.getZ());

			positions.appendTag(pos);
		}
		compound.setTag("linkedBlocks", positions);

		NBTTagList cratePositions = new NBTTagList();

		for (BlockPos storagePos : storageBlocks) {
			NBTTagCompound pos = new NBTTagCompound();

			pos.setInteger("x", storagePos.getX());
			pos.setInteger("y", storagePos.getY());
			pos.setInteger("z", storagePos.getZ());

			cratePositions.appendTag(pos);
		}
		compound.setTag("storageBlocks", cratePositions);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		name = compound.getString("name");
		ownerUUID = UUID.fromString(compound.getString("ownerUUID"));

		HashSet<BlockPos> linkedBlocks = new HashSet<BlockPos>();

		NBTTagList positions = compound.getTagList("linkedBlocks", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < positions.tagCount(); ++i) {
			NBTTagCompound pos = positions.getCompoundTagAt(i);

			linkedBlocks.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		this.linkedBlocks = linkedBlocks;

		HashSet<BlockPos> storageBlocks = new HashSet<BlockPos>();

		NBTTagList cratePositions = compound.getTagList("storageBlocks", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < cratePositions.tagCount(); ++i) {
			NBTTagCompound pos = cratePositions.getCompoundTagAt(i);

			storageBlocks.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		this.storageBlocks = storageBlocks;
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

	public void setName(String name) {
		this.name = name;
		markDirty();
	}

	public String getName() {
		return name;
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
}
