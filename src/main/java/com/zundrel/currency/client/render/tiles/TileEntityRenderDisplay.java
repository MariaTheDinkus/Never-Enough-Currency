package com.zundrel.currency.client.render.tiles;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import com.google.common.primitives.SignedBytes;
import com.zundrel.currency.common.blocks.BlockShelf;
import com.zundrel.currency.common.blocks.BlockTable;
import com.zundrel.currency.common.blocks.tiles.TileEntityDisplay;
import com.zundrel.currency.common.config.ConfigHandler;

public class TileEntityRenderDisplay extends TileEntitySpecialRenderer<TileEntityDisplay> {
	private RenderEntityItem itemRender;

	public TileEntityRenderDisplay() {
		itemRender = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
			@Override
			protected int getModelCount(ItemStack stack) {
				return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
			}

			@Override
			public boolean shouldBob() {
				return false;
			}

			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
		};
	}

	@Override
	public void render(TileEntityDisplay te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.getBlockType() instanceof BlockShelf) {
			renderShelf(te, x, y, z, partialTicks, destroyStage, alpha);
		} else if (te.getBlockType() instanceof BlockTable) {
			renderTable(te, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	public void renderShelf(TileEntityDisplay te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);

		for (int i = 0; i < 4; ++i) {
			if (!te.getStackInSlot(i).isEmpty()) {
				EntityItem customItem = new EntityItem(te.getWorld());
				customItem.hoverStart = 0.0F;
				customItem.setItem(te.getStackInSlot(i));
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) x, (float) y, (float) z);
				if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
					if (i < 2) {
						GlStateManager.translate(0.25F + (0.5F * i), 0.5F, 0.5F);
					} else {
						GlStateManager.translate(0.25F + (0.5F * (i - 2)), 0F, 0.5F);
					}
				} else {
					if (i < 2) {
						GlStateManager.translate(0.5F, 0.5F, 0.75F - (0.5F * i));
					} else {
						GlStateManager.translate(0.5F, 0F, 0.75F - (0.5F * (i - 2)));
					}
				}

				if (ConfigHandler.laidDownShelves && !(te.getStackInSlot(i).getItem() instanceof ItemBlock)) {
					GlStateManager.rotate(-90, 1, 0, 0);

					GlStateManager.translate(0, -0.25, 0.075);
				}

				GlStateManager.rotate(90 * facing.getOpposite().getHorizontalIndex(), 0.0F, 1.0F, 0.0F);

				GlStateManager.scale(0.7F, 0.7F, 0.7F);

				itemRender.doRender(customItem, 0, 0, 0, 0, 0);
				GlStateManager.popMatrix();
			}
		}
	}

	public void renderTable(TileEntityDisplay te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		for (int i = 0; i < 4; ++i) {
			if (!te.getStackInSlot(i).isEmpty()) {
				EntityItem customItem = new EntityItem(te.getWorld());
				customItem.hoverStart = 0.0F;
				customItem.setItem(te.getStackInSlot(i));
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) x, (float) y, (float) z);

				if (i < 2) {
					GlStateManager.translate(0.25F + (0.5F * (i)), 0.94F, 0.25F);
				} else {
					GlStateManager.translate(0.25F + (0.5F * (i - 2)), 0.94F, 0.75F);
				}

				if (ConfigHandler.laidDownTables && !(te.getStackInSlot(i).getItem() instanceof ItemBlock)) {
					GlStateManager.rotate(-90, 1, 0, 0);

					GlStateManager.translate(0, -0.25, 0.075);
				}

				GlStateManager.scale(0.7F, 0.7F, 0.7F);

				itemRender.doRender(customItem, 0, 0, 0, 0, 0);
				GlStateManager.popMatrix();
			}
		}
	}

}
