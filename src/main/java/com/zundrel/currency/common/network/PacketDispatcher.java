package com.zundrel.currency.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketDispatcher {
	private static byte packetId = 0;
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("currency");

	public static final void registerPackets() {
		registerMessage(MessageSyncAccount.Handler.class, MessageSyncAccount.class, Side.CLIENT);
		registerMessage(MessageSyncCart.Handler.class, MessageSyncCart.class, Side.CLIENT);
		registerMessage(MessageSyncPrices.Handler.class, MessageSyncPrices.class, Side.CLIENT);
		registerMessage(MessageSyncCartItem.Handler.class, MessageSyncCartItem.class, Side.CLIENT);
		registerMessage(MessageSyncController.Handler.class, MessageSyncController.class, Side.SERVER);
		registerMessage(MessageSyncPrice.Handler.class, MessageSyncPrice.class, Side.SERVER);
		registerMessage(MessageSyncServer.Handler.class, MessageSyncServer.class, Side.SERVER);
		registerMessage(MessageSyncDrops.Handler.class, MessageSyncDrops.class, Side.SERVER);
		registerMessage(MessageSyncRemove.Handler.class, MessageSyncRemove.class, Side.SERVER);
		registerMessage(MessageSyncAdd.Handler.class, MessageSyncAdd.class, Side.SERVER);
		registerMessage(MessageSyncClearList.Handler.class, MessageSyncClearList.class, Side.SERVER);
	}

	private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
		dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}

	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		dispatcher.sendTo(message, player);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAll(IMessage message) {
		dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		sendToAllAround(message, player.getEntityWorld().provider.getDimension(), player.posX, player.posY, player.posZ, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		dispatcher.sendToServer(message);
	}
}
