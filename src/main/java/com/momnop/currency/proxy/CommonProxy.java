package com.momnop.currency.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;

public class CommonProxy
{
  public static final Map<String, NBTTagCompound> dataBankAccount = new HashMap();
  
  public void preInitRenderers() {}
  
  public void initRenderers() {}
  
  public static void storeEntityData(String name, NBTTagCompound compound)
  {
    dataBankAccount.put(name, compound);
  }
  
  public static NBTTagCompound getEntityData(String name)
  {
    return (NBTTagCompound)dataBankAccount.remove(name);
  }
  
  public EntityPlayer getPlayerEntity(MessageContext ctx)
  {
    return ctx.getServerHandler().playerEntity;
  }
  
  public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
  {
    switch (ctx.side)
    {
    case SERVER: 
      EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
      return entityPlayerMP;
	default:
		break;
    }
    return null;
  }
}
