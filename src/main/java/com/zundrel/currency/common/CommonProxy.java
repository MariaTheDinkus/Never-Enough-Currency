package com.zundrel.currency.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	public void preInitRenderers() {
		
	}
	
	public void initRenderers() {
		
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
      return ctx.getServerHandler().player;
    }
	
	public void registerItemRenderer(Item i, int meta, String id) {
    	
    }
}