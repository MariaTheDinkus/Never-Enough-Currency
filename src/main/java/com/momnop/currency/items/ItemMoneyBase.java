package com.momnop.currency.items;

import com.momnop.currency.CurrencyCreativeTab;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMoneyBase
  extends Item
{
  private float value = 0.0F;
  
  public ItemMoneyBase(String unlocalizedName, float value)
  {
    setCreativeTab(CurrencyCreativeTab.INSTANCE);
    setUnlocalizedName(unlocalizedName);
    this.value = value;
  }
  
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
  {
    NumberFormat fmtUS = NumberFormat.getCurrencyInstance(Locale.US);
    tooltip.add("Value ($): " + fmtUS.format(this.value));
  }
  
  public float getValue()
  {
    return this.value;
  }
  
  @Override
  public void registerIcons(IIconRegister register)
  {
    this.itemIcon = register.registerIcon("currency:" + getUnlocalizedName().substring(5));
  }
}
