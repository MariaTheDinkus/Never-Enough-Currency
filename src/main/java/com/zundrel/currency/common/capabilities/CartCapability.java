package com.zundrel.currency.common.capabilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.network.MessageSyncCart;
import com.zundrel.currency.common.network.MessageSyncCartItem;
import com.zundrel.currency.common.network.MessageSyncPrices;
import com.zundrel.currency.common.network.PacketDispatcher;

public class CartCapability implements ICapabilitySerializable<NBTTagCompound> {
	private NonNullList<ItemStack> cart = NonNullList.withSize(25, ItemStack.EMPTY);
	private List<Float> prices = Arrays.asList(new Float[25]);
	private EntityLivingBase entity;

	public CartCapability(EntityLivingBase entity) {
		this.entity = entity;
	}

	public void setStackInSlot(int slot, ItemStack stack, boolean update) {
		cart.set(slot, stack);
		if (update)
			sendPacket();
	}

	public void setStackInSlot(int slot, ItemStack stack, float price, boolean update) {
		cart.set(slot, stack);
		prices.set(slot, price);
		if (update)
			sendPacket();
		sendPricesPacket();
	}

	public void addStackToCart(ItemStack stack, boolean update) {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (cart.get(i).isEmpty()) {
				cart.set(i, stack);

				if (update)
					sendPacket();
				break;
			}
		}
	}

	public void addStackToCart(ItemStack stack, float price, boolean update) {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (cart.get(i).isEmpty()) {
				cart.set(i, stack);
				prices.set(i, price);

				if (update) {
					sendPacket();
					sendPricesPacket();
				}
				break;
			}
		}
	}

	public ItemStack getStackInSlot(int slot) {
		return cart.get(slot);
	}

	public void setCart(NonNullList<ItemStack> cart, boolean update) {
		this.cart = cart;
		if (update)
			sendPacket();
	}

	public void setPrices(List<Float> prices, boolean update) {
		this.prices = prices;
		if (update)
			sendPricesPacket();
	}

	public NonNullList<ItemStack> getCart() {
		return cart;
	}

	public List<Float> getPrices() {
		return prices;
	}

	public int getSizeInventory() {
		return cart.size();
	}

	public void sendPacket() {
		if (cart != null) {
			PacketDispatcher.sendToAll(new MessageSyncCart(this.entity, cart));
		}
	}

	public void sendPricesPacket() {
		if (prices != null) {
			PacketDispatcher.sendToAll(new MessageSyncPrices(this.entity, prices));
		}
	}

	public void sendPacketItem(int slot, ItemStack stack) {
		PacketDispatcher.sendToAll(new MessageSyncCartItem(this.entity, slot, stack));
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CartCapability.class, new CartCapability.Storage(), new CartCapability.Factory());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return Currency.CART_DATA != null && capability == Currency.CART_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return Currency.CART_DATA != null && capability == Currency.CART_DATA ? (T) this : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
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

		NBTTagList pricesList = new NBTTagList();

		if (prices != null && !prices.isEmpty()) {
			for (int i = 0; i < getSizeInventory(); ++i) {
				if (prices.get(i) != null) {
					NBTTagCompound price = new NBTTagCompound();
					price.setInteger("Slot", i);
					price.setFloat("price", prices.get(i));

					pricesList.appendTag(price);
				}
			}
		}
		compound.setTag("Prices", pricesList);

		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		NonNullList<ItemStack> cart = NonNullList.withSize(25, ItemStack.EMPTY);

		NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			if (slot >= 0 && slot < getSizeInventory()) {
				cart.set(slot, new ItemStack(item));
			}
		}
		this.setCart(cart, true);

		ArrayList<Float> prices = new ArrayList<Float>(25);
		for (int i = 0; i < 25; i++) {
			prices.add((float) 0);
		}

		NBTTagList pricesList = compound.getTagList("Prices", Constants.NBT.TAG_COMPOUND);
		if (prices != null && !prices.isEmpty()) {
			for (int i = 0; i < pricesList.tagCount(); ++i) {
				NBTTagCompound price = pricesList.getCompoundTagAt(i);
				int slot = price.getInteger("Slot");

				if (slot >= 0 && slot < getSizeInventory()) {
					cart.set(slot, new ItemStack(price));
				}
			}
		}
		this.setPrices(prices, true);
	}

	public static class Storage implements Capability.IStorage<CartCapability> {

		@Override
		public NBTBase writeNBT(Capability<CartCapability> capability, CartCapability instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<CartCapability> capability, CartCapability instance, EnumFacing side, NBTBase nbt) {

		}

	}

	public static class Factory implements Callable<CartCapability> {
		@Override
		public CartCapability call() throws Exception {
			return null;
		}
	}
}
