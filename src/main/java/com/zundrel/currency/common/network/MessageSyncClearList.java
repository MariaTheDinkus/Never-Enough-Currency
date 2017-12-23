package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

public class MessageSyncClearList implements IMessage {
	private int entityId;

	public MessageSyncClearList() {
	}

	public MessageSyncClearList(EntityLivingBase entity) {
		this.entityId = entity.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
	}

	public static class Handler extends AbstractMessageHandler<MessageSyncClearList> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncClearList message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityPlayer en = (EntityPlayer) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (player.getEntityId() == en.getEntityId() && en.getEntityWorld() != null && en.hasCapability(Currency.CART_DATA, null)) {
						CartCapability entityData = en.getCapability(Currency.CART_DATA, null);

						entityData.setCart(NonNullList.withSize(entityData.getSizeInventory(), ItemStack.EMPTY), true);
						List<Float> prices = Arrays.asList(new Float[25]);
						for (int i = 0; i < prices.size(); i++) {
							prices.set(i, (float) 0);
						}
					}
				}
			}
			return null;
		}

		@Override
		public IMessage handleClientMessage(EntityPlayer paramEntityPlayer, MessageSyncClearList paramT, MessageContext paramMessageContext) {
			return null;
		}
	}

}