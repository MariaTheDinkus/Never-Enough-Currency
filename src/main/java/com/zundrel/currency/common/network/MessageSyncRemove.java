package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.items.ItemMoneyBase;

public class MessageSyncRemove implements IMessage {
	private int entityId;

	public MessageSyncRemove() {
	}

	public MessageSyncRemove(EntityLivingBase entity) {
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

	public static class Handler extends AbstractMessageHandler<MessageSyncRemove> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncRemove message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				EntityPlayer en = (EntityPlayer) player.getEntityWorld().getEntityByID(message.entityId);
				if (en != null) {
					if (player.getEntityId() == en.getEntityId() && en.getEntityWorld() != null && en.hasCapability(Currency.ACCOUNT_DATA, null)) {
						AccountCapability entityData = en.getCapability(Currency.ACCOUNT_DATA, null);
						for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
							if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY && player.inventory.getStackInSlot(i).getItem() instanceof ItemMoneyBase) {
								player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
							}
						}
					}
				}
			}
			return null;
		}

		@Override
		public IMessage handleClientMessage(EntityPlayer paramEntityPlayer, MessageSyncRemove paramT, MessageContext paramMessageContext) {
			return null;
		}
	}

}