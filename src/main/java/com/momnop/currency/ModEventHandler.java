package com.momnop.currency;

import com.momnop.currency.config.ConfigHandler;
import com.momnop.currency.items.ItemMoneyBase;
import com.momnop.currency.utils.CurrencyUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.lwjgl.opengl.GL11;

public class ModEventHandler
{
  public static int counter = 0;
  public static int timeSinceSneak = 0;
  
  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event)
  {
    if (ConfigHandler.dropMoney && (!(event.entityLiving instanceof EntityPlayer)) && ((event.entityLiving instanceof IMob)) && (!event.entityLiving.worldObj.isRemote))
    {
      if ((event.source.getSourceOfDamage() != null) && ((event.source.getSourceOfDamage() instanceof EntityPlayer))) {
        CurrencyUtils.dropMoneyAmount(event.entityLiving.getMaxHealth() / ConfigHandler.mobDivisionValue, event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
      }
      if ((event.source.getSourceOfDamage() != null) && (event.source.getSourceOfDamage() != null) && ((event.source.getSourceOfDamage() instanceof EntityArrow)))
      {
        EntityArrow arrow = (EntityArrow)event.source.getSourceOfDamage();
        if ((arrow.shootingEntity instanceof EntityPlayer)) {
          CurrencyUtils.dropMoneyAmount(event.entityLiving.getMaxHealth() / ConfigHandler.mobDivisionValue, event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
        }
      }
    }
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
  {
    if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
      return;
    }
    Tessellator tessellator = Tessellator.instance;
    FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
    
    GL11.glEnable(3042);
    if (Minecraft.getMinecraft().thePlayer.isSneaking())
    {
      GL11.glPushMatrix();
      
      int s = 49;
      GL11.glTranslatef(0.0F, -11F, 0.0F);
      tessellator.startDrawing(7);
      
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().renderEngine
        .bindTexture(new ResourceLocation("currency:textures/items/dollarBill.png"));
      
      tessellator.addVertexWithUV(2.0D, 2.0D, 0.0D, 0.0D, 0.0D);
      tessellator.addVertexWithUV(2.0D, 2 + s, 0.0D, 0.0D, 1.0D);
      tessellator.addVertexWithUV(2 + s, 2 + s, 0.0D, 1.0D, 1.0D);
      tessellator.addVertexWithUV(2 + s, 2.0D, 0.0D, 1.0D, 0.0D);
      
      tessellator.draw();
      fr.drawString("Physical: " + CurrencyUtils.getAllCurrency(Minecraft.getMinecraft().thePlayer), s + 4, (s / 2) - 2, 
        ColorHelper.getDecimalFromRGB(255, 255, 255), true);
      GL11.glPopMatrix();
    }
    GL11.glDisable(3042);
  }
  
  public static void drawOutlinedBoundingBox(AxisAlignedBB p_147590_0_, int p_147590_1_)
  {
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(3);
    if (p_147590_1_ != -1) {
      tessellator.setColorOpaque_I(p_147590_1_);
    }
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
    tessellator.draw();
    tessellator.startDrawing(3);
    if (p_147590_1_ != -1) {
      tessellator.setColorOpaque_I(p_147590_1_);
    }
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
    tessellator.draw();
    tessellator.startDrawing(1);
    if (p_147590_1_ != -1) {
      tessellator.setColorOpaque_I(p_147590_1_);
    }
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
    tessellator.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
    tessellator.draw();
  }
}
