package com.zundrel.currency.common.compat.waila;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.blocks.BlockATM;
import com.zundrel.currency.common.blocks.BlockShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityDisplay;
import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.AccountCapability;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class CurrencyWailaProvider implements IWailaDataProvider
{

	public static void callbackRegister(IWailaRegistrar registrar)
	{
		CurrencyWailaProvider dataProvider = new CurrencyWailaProvider();
		registrar.registerBodyProvider(dataProvider, BlockATM.class);
		registrar.registerBodyProvider(dataProvider, TileEntityShopController.class);
		registrar.registerNBTProvider(dataProvider, TileEntityShopController.class);
		registrar.registerBodyProvider(dataProvider, TileEntityDisplay.class);
		registrar.registerBodyProvider(dataProvider, TileEntityStockCrate.class);
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity tile = accessor.getTileEntity();

		if (accessor.getBlock() instanceof BlockATM) {
			AccountCapability capability = accessor.getPlayer().getCapability(Currency.ACCOUNT_DATA, null);
			currenttip.add(I18n.format("currency.account.waila") + " " + capability.getFormattedAmount());
		}

		if(tile instanceof TileEntityShopController)
		{
			TileEntityShopController controller = (TileEntityShopController) tile;
			NBTTagCompound compound = accessor.getNBTData().getCompoundTag("controller");
			
			currenttip.add(I18n.format("currency.ownername.waila") + " " + controller.getOwnerName());
			currenttip.add(I18n.format("currency.name.waila") + " " + controller.getName());
		} else if(tile instanceof TileEntityDisplay)
		{
			TileEntityDisplay shelf = (TileEntityDisplay) tile;
			World world = shelf.getWorld();

			currenttip.add("Shelf " + I18n.format("currency.ownername.waila") + " " + shelf.getOwnerName());
			currenttip.add(I18n.format("currency.linked.waila") + " " + shelf.getShopControllerPos().getX() + ", " + shelf.getShopControllerPos().getY() + ", " + shelf.getShopControllerPos().getZ());
		} else if (tile instanceof TileEntityStockCrate) {
			TileEntityStockCrate crate = (TileEntityStockCrate) tile;
			World world = crate.getWorld();
			
			currenttip.add("Stock Crate " + I18n.format("currency.ownername.waila") + " " + crate.getOwnerName());
			currenttip.add(I18n.format("currency.linked.waila") + " " + crate.getShopControllerPos().getX() + ", " + crate.getShopControllerPos().getY() + ", " + crate.getShopControllerPos().getZ());
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
	{
		if (te instanceof TileEntityShopController) {
			tag.setTag("controller", ((TileEntityShopController) te).writeToNBT(new NBTTagCompound()));
		}
		return tag;
	}

}