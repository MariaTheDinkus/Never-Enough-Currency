package com.momnop.currency.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemHandler
{
  public static Item penny;
  public static Item nickel;
  public static Item dime;
  public static Item quarter;
  public static Item dollarBill;
  public static Item fiveDollarBill;
  public static Item tenDollarBill;
  public static Item twentyDollarBill;
  public static Item fiftyDollarBill;
  public static Item hundredDollarBill;
  
  public static void load()
  {
    penny = new ItemMoneyBase("penny", 0.01F);
    nickel = new ItemMoneyBase("nickel", 0.05F);
    dime = new ItemMoneyBase("dime", 0.1F);
    quarter = new ItemMoneyBase("quarter", 0.25F);
    
    dollarBill = new ItemMoneyBase("dollarBill", 1.0F);
    fiveDollarBill = new ItemMoneyBase("fiveDollarBill", 5.0F);
    tenDollarBill = new ItemMoneyBase("tenDollarBill", 10.0F);
    twentyDollarBill = new ItemMoneyBase("twentyDollarBill", 20.0F);
    fiftyDollarBill = new ItemMoneyBase("fiftyDollarBill", 50.0F);
    hundredDollarBill = new ItemMoneyBase("hundredDollarBill", 100.0F);
    
    register(penny);
    register(nickel);
    register(dime);
    register(quarter);
    
    register(dollarBill);
    register(fiveDollarBill);
    register(tenDollarBill);
    register(twentyDollarBill);
    register(fiftyDollarBill);
    register(hundredDollarBill);
  }
  
  public static void register(Item i)
  {
    GameRegistry.registerItem(i, i.getUnlocalizedName().substring(5));
  }
}
