package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

public class MessageSyncCartItem implements IMessage {
	private int entityId;
	private int slot;
	private ItemStack stack;

	public MessageSyncCartItem() {
	}

	public MessageSyncCartItem(EntityLivingBase entity, int slot, ItemStack stack) {
		this.entityId = entity.getEntityId();
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.slot);
		ByteBufUtils.writeItemStack(buf, this.stack);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.slot = buf.readInt();
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler extends AbstractClientMessageHandler<MessageSyncCartItem> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncCartItem message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityLivingBase en = (EntityLivingBase) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (en.getEntityWorld() != null && en.hasCapability(Currency.CART_DATA, null)) {
						CartCapability entityData = en.getCapability(Currency.CART_DATA, null);
						entityData.setStackInSlot(message.slot, message.stack, false);
					}
				}
			}
			return null;
		}
	}

}