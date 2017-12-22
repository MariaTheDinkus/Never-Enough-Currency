package com.zundrel.currency.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.info.ModInfo;

public class BlockBasic extends BlockHorizontal
{
	public BlockBasic(String unlocalizedName, Material material, float hardness, SoundType type, CreativeTabs tab)
	{
		super(material);
		setCreativeTab(tab);
		setHardness(hardness);
		setRegistryName(unlocalizedName);
		setUnlocalizedName(this.getRegistryName().toString().replace(ModInfo.MODID + ":", ""));
		setSoundType(type);
	}
	
	public void registerItemModel(ItemBlock ib) {
		Currency.proxy.registerItemRenderer(ib, 0, this.getUnlocalizedName().substring(5));
	}
	
	public TileEntity getTileEntitySafely(IBlockAccess blockAccess, BlockPos pos) {
		if (blockAccess instanceof ChunkCache) {
			return ((ChunkCache) blockAccess).getTileEntity(pos,
					Chunk.EnumCreateEntityType.CHECK);
		} else {
			return blockAccess.getTileEntity(pos);
		}
	}
}
