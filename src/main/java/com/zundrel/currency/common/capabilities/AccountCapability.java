package com.zundrel.currency.common.capabilities;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Callable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.network.MessageSyncAccount;
import com.zundrel.currency.common.network.MessageSyncServer;
import com.zundrel.currency.common.network.PacketDispatcher;

public class AccountCapability implements ICapabilitySerializable<NBTTagCompound> {
	private float amount = 0;
	private EntityLivingBase entity;

	public AccountCapability(EntityLivingBase entity) {
		this.entity = entity;
	}

	public void setAmount(float amount, boolean update) {
		this.amount = amount;
		if (update)
			sendPacket();
	}

	public void addAmount(float amount, boolean update) {
		this.amount += amount;
		if (update)
			sendPacket();
	}

	public void subtractAmount(float amount, boolean update) {
		this.subtractAmount(amount, false, update);
	}

	public void subtractAmount(float amount, boolean force, boolean update) {
		if (!force && amount > this.amount) {
			return;
		}

		this.amount -= amount;

		if (force && this.amount < 0) {
			this.amount = 0;
		}

		if (update)
			sendPacket();
	}

	public void setClientAmount(float amount, boolean update) {
		this.amount = amount;
		if (update)
			sendClientPacket();
	}

	public void addClientAmount(float amount, boolean update) {
		this.amount += amount;
		if (update)
			sendClientPacket();
	}

	public void subtractClientAmount(float amount, boolean update) {
		this.subtractClientAmount(amount, false, update);
	}

	public void subtractClientAmount(float amount, boolean force, boolean update) {
		if (!force && amount > this.amount) {
			return;
		}

		this.amount -= amount;

		if (force && this.amount < 0) {
			this.amount = 0;
		}

		if (update)
			sendClientPacket();
	}

	public float getAmount() {
		return this.amount;
	}

	public String getFormattedAmount() {
		NumberFormat fmt;
		float newAmount = amount;
		fmt = NumberFormat.getCurrencyInstance(Locale.US);
		return fmt.format(newAmount);
	}

	public void sendPacket() {
		PacketDispatcher.sendToAll(new MessageSyncAccount(this.entity, this.amount));
	}

	public void sendClientPacket() {
		PacketDispatcher.sendToServer(new MessageSyncServer(this.entity, this.amount));
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(AccountCapability.class, new AccountCapability.Storage(), new AccountCapability.Factory());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return Currency.ACCOUNT_DATA != null && capability == Currency.ACCOUNT_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return Currency.ACCOUNT_DATA != null && capability == Currency.ACCOUNT_DATA ? (T) this : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound properties = new NBTTagCompound();
		properties.setFloat("amount", amount);
		return properties;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		amount = nbt.getFloat("amount");
	}

	public static class Storage implements Capability.IStorage<AccountCapability> {

		@Override
		public NBTBase writeNBT(Capability<AccountCapability> capability, AccountCapability instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<AccountCapability> capability, AccountCapability instance, EnumFacing side, NBTBase nbt) {

		}

	}

	public static class Factory implements Callable<AccountCapability> {
		@Override
		public AccountCapability call() throws Exception {
			return null;
		}
	}
}
