package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.AccountCapability;

public class MessageSyncServer implements IMessage {
	private int entityId;
	private float amount;

	public MessageSyncServer() {
	}

	public MessageSyncServer(EntityLivingBase entity, float amount) {
		this.entityId = entity.getEntityId();
		this.amount = amount;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeFloat(amount);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.amount = buf.readFloat();
	}

	public static class Handler extends AbstractMessageHandler<MessageSyncServer> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncServer message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityLivingBase en = (EntityLivingBase) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (en.getEntityWorld() != null && en.hasCapability(Currency.ACCOUNT_DATA, null)) {
						AccountCapability entityData = en.getCapability(Currency.ACCOUNT_DATA, null);
						entityData.setAmount(message.amount, false);
					}
				}
			}
			return null;
		}

		@Override
		public IMessage handleClientMessage(EntityPlayer paramEntityPlayer, MessageSyncServer paramT, MessageContext paramMessageContext) {
			return null;
		}
	}

}