package com.momnop.currency;

import com.momnop.currency.items.ItemHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class RecipeHandler
{
  public static void loadRecipes()
  {
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.hundredDollarBill), new Object[] { ItemHandler.twentyDollarBill, ItemHandler.twentyDollarBill, ItemHandler.twentyDollarBill, ItemHandler.twentyDollarBill, ItemHandler.twentyDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.hundredDollarBill), new Object[] { ItemHandler.fiftyDollarBill, ItemHandler.fiftyDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.fiftyDollarBill), new Object[] { ItemHandler.twentyDollarBill, ItemHandler.twentyDollarBill, ItemHandler.tenDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.fiftyDollarBill), new Object[] { ItemHandler.tenDollarBill, ItemHandler.tenDollarBill, ItemHandler.tenDollarBill, ItemHandler.tenDollarBill, ItemHandler.tenDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.twentyDollarBill), new Object[] { ItemHandler.tenDollarBill, ItemHandler.tenDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.tenDollarBill), new Object[] { ItemHandler.fiveDollarBill, ItemHandler.fiveDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.fiveDollarBill), new Object[] { ItemHandler.dollarBill, ItemHandler.dollarBill, ItemHandler.dollarBill, ItemHandler.dollarBill, ItemHandler.dollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.dollarBill), new Object[] { ItemHandler.quarter, ItemHandler.quarter, ItemHandler.quarter, ItemHandler.quarter });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.quarter), new Object[] { ItemHandler.dime, ItemHandler.dime, ItemHandler.nickel });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.dime), new Object[] { ItemHandler.nickel, ItemHandler.nickel });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.nickel), new Object[] { ItemHandler.penny, ItemHandler.penny, ItemHandler.penny, ItemHandler.penny, ItemHandler.penny });
	  
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.fiftyDollarBill, 2), new Object[] { ItemHandler.hundredDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.tenDollarBill, 5), new Object[] { ItemHandler.fiftyDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.tenDollarBill, 2), new Object[] { ItemHandler.twentyDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.fiveDollarBill, 2), new Object[] { ItemHandler.tenDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.dollarBill, 5), new Object[] { ItemHandler.fiveDollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.quarter, 4), new Object[] { ItemHandler.dollarBill });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.nickel, 2), new Object[] { ItemHandler.dime });
	  GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.penny, 5), new Object[] { ItemHandler.nickel });
  }
}
