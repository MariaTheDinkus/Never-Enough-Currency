package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

public class MessageSyncPrices implements IMessage {
	private int entityId;
	private List<Float> prices;

	public MessageSyncPrices() {
	}

	public MessageSyncPrices(EntityLivingBase entity, List<Float> prices) {
		this.entityId = entity.getEntityId();
		this.prices = prices;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(prices.size());
		for (int i = 0; i < prices.size(); i++) {
			if (prices.get(i) != null) {
				buf.writeFloat(prices.get(i));
			} else {
				buf.writeFloat(0);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		int size = buf.readInt();
		List<Float> list = Arrays.asList(new Float[25]);
		for (int i = 0; i < size; i++) {
			list.set(i, buf.readFloat());
		}
		this.prices = list;
	}

	public static class Handler extends AbstractClientMessageHandler<MessageSyncPrices> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncPrices message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityLivingBase en = (EntityLivingBase) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (en.getEntityWorld() != null && en.hasCapability(Currency.CART_DATA, null)) {
						CartCapability entityData = en.getCapability(Currency.CART_DATA, null);
						entityData.setPrices(message.prices, false);
					}
				}
			}
			return null;
		}
	}

}