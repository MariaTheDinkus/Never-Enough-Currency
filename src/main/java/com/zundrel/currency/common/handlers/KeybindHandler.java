package com.zundrel.currency.common.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.info.ModInfo;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class KeybindHandler {
	public static KeyBinding openList;

	public static void init() {
		openList = new KeyBinding("key.shoppinglist.desc", Keyboard.KEY_G, "key.currency.category");

		ClientRegistry.registerKeyBinding(openList);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public static void onEvent(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;

		CartCapability entityData = player.getCapability(Currency.CART_DATA, null);

		boolean isEmpty = true;

		if (entityData != null && entityData.getCart() != null) {
			for (ItemStack stack : entityData.getCart()) {
				if (!stack.isEmpty()) {
					isEmpty = false;
				}
			}
		}

		if (!isEmpty && openList.isPressed()) {
			Minecraft.getMinecraft().player.openGui(Currency.INSTANCE, 5, mc.world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}
}
