package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;

public class MessageSyncPrice implements IMessage {
	private float amount;
	private BlockPos pos;

	public MessageSyncPrice() {
	}

	public MessageSyncPrice(float amount, BlockPos pos) {
		this.amount = amount;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.amount = buf.readFloat();
		this.pos = new BlockPos(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(amount);
		buf.writeFloat(pos.getX());
		buf.writeFloat(pos.getY());
		buf.writeFloat(pos.getZ());
	}

	public static class Handler extends AbstractMessageHandler<MessageSyncPrice> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncPrice message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				if (player.getEntityWorld().getTileEntity(message.pos) instanceof TileEntityStockCrate) {
					TileEntityStockCrate crate = (TileEntityStockCrate) player.getEntityWorld().getTileEntity(message.pos);
					crate.setAmount(message.amount);
				}
			}
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncPrice message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				if (player.getEntityWorld().getTileEntity(message.pos) instanceof TileEntityStockCrate) {
					TileEntityStockCrate crate = (TileEntityStockCrate) player.getEntityWorld().getTileEntity(message.pos);
					crate.setAmount(message.amount);
				}
			}
			return null;
		}
	}
}
