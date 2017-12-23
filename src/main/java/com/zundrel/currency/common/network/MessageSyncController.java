package com.zundrel.currency.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;

public class MessageSyncController implements IMessage {
	private String name;
	private BlockPos pos;

	public MessageSyncController() {
	}

	public MessageSyncController(String name, BlockPos pos) {
		this.name = name;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.name = ByteBufUtils.readUTF8String(buf);
		this.pos = new BlockPos(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeFloat(pos.getX());
		buf.writeFloat(pos.getY());
		buf.writeFloat(pos.getZ());
	}

	public static class Handler extends AbstractMessageHandler<MessageSyncController> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSyncController message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				if (player.getEntityWorld().getTileEntity(message.pos) instanceof TileEntityShopController) {
					TileEntityShopController controller = (TileEntityShopController) player.getEntityWorld().getTileEntity(message.pos);
					controller.setName(message.name);
				}
			}
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageSyncController message, MessageContext ctx) {
			if ((player != null) && (message != null) && (ctx != null)) {
				if (player.getEntityWorld().getTileEntity(message.pos) instanceof TileEntityShopController) {
					TileEntityShopController controller = (TileEntityShopController) player.getEntityWorld().getTileEntity(message.pos);
					controller.setName(message.name);
				}
			}
			return null;
		}
	}
}
