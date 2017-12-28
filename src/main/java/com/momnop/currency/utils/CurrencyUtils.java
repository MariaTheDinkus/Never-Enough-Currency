package com.momnop.currency.utils;

import com.momnop.currency.items.ItemHandler;
import com.momnop.currency.items.ItemMoneyBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CurrencyUtils
{
  public static String getAllCurrency(EntityPlayer player)
  {
    float currencyTotal = 0.0F;
    for (int i = 0; i < player.inventory.mainInventory.length; i++) {
      if ((player.inventory.mainInventory[i] != null) && 
        ((player.inventory.mainInventory[i].getItem() instanceof ItemMoneyBase)))
      {
        ItemMoneyBase money = (ItemMoneyBase)player.inventory.mainInventory[i].getItem();
        currencyTotal += money.getValue() * player.inventory.mainInventory[i].stackSize;
      }
    }
    NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
    return fmt.format(currencyTotal);
  }
  
  public static void dropMoneyAmount(float value, World world, double x, double y, double z)
  {
    float newValue = value;
    if (value >= 100.0F)
    {
      EntityItem hundreds = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.hundredDollarBill, (int)Math.floor(value / 100.0F)));
      world.spawnEntityInWorld(hundreds);
      newValue = (float)(newValue - 100.0D * Math.floor(newValue / 100.0F));
    }
    if (newValue >= 20.0F)
    {
      EntityItem twenties = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.twentyDollarBill, (int)Math.floor(newValue / 20.0F)));
      world.spawnEntityInWorld(twenties);
      newValue = (float)(newValue - 20.0D * Math.floor(newValue / 20.0F));
    }
    if (newValue >= 10.0F)
    {
      EntityItem tens = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.tenDollarBill, (int)Math.floor(newValue / 10.0F)));
      world.spawnEntityInWorld(tens);
      newValue = (float)(newValue - 10.0D * Math.floor(newValue / 10.0F));
    }
    if (newValue >= 5.0F)
    {
      EntityItem fives = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.fiveDollarBill, (int)Math.floor(newValue / 5.0F)));
      world.spawnEntityInWorld(fives);
      newValue = (float)(newValue - 5.0D * Math.floor(newValue / 5.0F));
    }
    if (newValue >= 1.0F)
    {
      EntityItem dollars = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.dollarBill, (int)Math.floor(newValue)));
      world.spawnEntityInWorld(dollars);
      newValue = (float)(newValue - 1.0D * Math.floor(newValue));
    }
    EntityItem quarters = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.quarter, (int)(newValue / 0.25F)));
    
    world.spawnEntityInWorld(quarters);
    newValue -= 0.25F * (int)(newValue / 0.25F);
    
    EntityItem dimes = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.dime, (int)(newValue / 0.1F)));
    
    world.spawnEntityInWorld(dimes);
    newValue -= 0.1F * (int)(newValue / 0.1F);
    
    EntityItem nickels = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.nickel, (int)(newValue / 0.05F)));
    
    world.spawnEntityInWorld(nickels);
    newValue -= 0.05F * (int)(newValue / 0.05F);
    
    EntityItem pennies = new EntityItem(world, x, y, z, new ItemStack(ItemHandler.penny, (int)(newValue / 0.01F)));
    
    world.spawnEntityInWorld(pennies);
    newValue -= 0.01F * (int)(newValue / 0.01F);
  }
  
  public static double getCurrencyRate()
  {
    URL currencyRateUrl = null;
    try
    {
      currencyRateUrl = new URL("http://");
    }
    catch (MalformedURLException e)
    {
      System.out.println("Sorry");
      return 0.0D;
    }
    if (netIsAvailable()) {
      return findExchangeRateAndConvert("USD", "GBP", 1).doubleValue();
    }
    return 0.0D;
  }
  
  private static Double findExchangeRateAndConvert(String from, String to, int amount)
  {
    try
    {
      URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s=" + from + to + "=X");
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      String line = reader.readLine();
      if (line.length() > 0) {
        return Double.valueOf(Double.parseDouble(line) * amount);
      }
      reader.close();
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
    return null;
  }
  
  private static boolean netIsAvailable()
  {
    try
    {
      URL url = new URL("http://finance.yahoo.com");
      
      URLConnection conn = url.openConnection();
      conn.connect();
      return true;
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
    catch (IOException e) {}
    return false;
  }
}
