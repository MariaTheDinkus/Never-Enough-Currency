package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

public class MessageSyncCart implements IMessage {
	private int entityId;
	private NonNullList<ItemStack> cart;

	public MessageSyncCart() {
	}

	public MessageSyncCart(EntityLivingBase entity, NonNullList<ItemStack> cart) {
		this.entityId = entity.getEntityId();
		this.cart = cart;
		;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(cart.size());
		for (int i = 0; i < cart.size(); i++) {
			ByteBufUtils.writeItemStack(buf, cart.get(i));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		int size = buf.readInt();
		NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
		for (int i = 0; i < list.size(); i++) {
			list.set(i, ByteBufUtils.readItemStack(buf));
		}
		this.cart = list;
	}

	public static class Handler extends AbstractClientMessageHandler<MessageSyncCart> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncCart message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityLivingBase en = (EntityLivingBase) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (en.getEntityWorld() != null && en.hasCapability(Currency.CART_DATA, null)) {
						CartCapability entityData = en.getCapability(Currency.CART_DATA, null);
						entityData.setCart(message.cart, false);
					}
				}
			}
			return null;
		}
	}

}