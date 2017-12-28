package com.momnop.currency.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;

public class ClientProxy
  extends CommonProxy
{
  public void preInitRenderers() {}
  
  public void initRenderers() {}
  
  public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
  {
    switch (ctx.side)
    {
    case CLIENT: 
      EntityPlayer entityClientPlayerMP = Minecraft.getMinecraft().thePlayer;
      return entityClientPlayerMP;
    case SERVER: 
      EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
      return entityPlayerMP;
    }
    return null;
  }
}
