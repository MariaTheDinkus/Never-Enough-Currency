package com.zundrel.currency.common.handlers;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zundrel.currency.common.info.ModInfo;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class SoundHandler {
	public static SoundEvent scanner, register;

	public static ArrayList<SoundEvent> sounds = new ArrayList<SoundEvent>();

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName
	 *            The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		scanner = new SoundEvent(new ResourceLocation(ModInfo.MODID, "currency.scanner")).setRegistryName(new ResourceLocation(ModInfo.MODID, "currency.scanner"));
		register = new SoundEvent(new ResourceLocation(ModInfo.MODID, "currency.register")).setRegistryName(new ResourceLocation(ModInfo.MODID, "currency.register"));

		event.getRegistry().register(scanner);
		event.getRegistry().register(register);
	}
}
