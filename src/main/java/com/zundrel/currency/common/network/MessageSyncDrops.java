package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.utils.CurrencyUtils;

public class MessageSyncDrops implements IMessage {
	private int entityId;
	private float amount;

	public MessageSyncDrops() {
	}

	public MessageSyncDrops(EntityLivingBase entity, float amount) {
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

	public static class Handler extends AbstractMessageHandler<MessageSyncDrops> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncDrops message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityLivingBase en = (EntityLivingBase) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (player.getEntityId() == en.getEntityId() && en.getEntityWorld() != null && en.hasCapability(Currency.ACCOUNT_DATA, null)) {
						AccountCapability entityData = en.getCapability(Currency.ACCOUNT_DATA, null);
						CurrencyUtils.dropMoneyAmount(message.amount, en.getEntityWorld(), en.posX, en.posY, en.posZ);
					}
				}
			}
			return null;
		}

		@Override
		public IMessage handleClientMessage(EntityPlayer paramEntityPlayer, MessageSyncDrops paramT, MessageContext paramMessageContext) {
			return null;
		}
	}

}