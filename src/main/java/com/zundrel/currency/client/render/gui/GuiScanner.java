package com.zundrel.currency.client.render.gui;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;

@SideOnly(Side.CLIENT)
public class GuiScanner extends GuiScreen {
	int guiWidth = 256;
	int guiHeight = 256;

	int guiX = (width - guiWidth) / 2;
	int guiY = (height - guiHeight) / 2;

	EntityPlayer player;

	public GuiScanner(EntityPlayer player) {
		super();

		this.player = player;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int x, int y, float ticks) {
		ArrayList<String> strings = new ArrayList<String>();

		if (player.getCapability(Currency.CART_DATA, null) != null) {
			CartCapability cap = player.getCapability(Currency.CART_DATA, null);

			GL11.glEnable(GL11.GL_BLEND);
			drawDefaultBackground();
			GL11.glDisable(GL11.GL_BLEND);

			int i = 0;
			
			float overallAmount = 0;
			
			for (ItemStack stack : cap.getCart()) {
				if (!stack.isEmpty() && cap.getPrices().get(i) != null) {
					fontRenderer.drawString(stack.getCount() + "x " + TextFormatting.GREEN + stack.getDisplayName() + TextFormatting.RESET + ", " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(cap.getPrices().get(i)) + TextFormatting.RESET + " each.", (width / 2) - (fontRenderer.getStringWidth(stack.getCount() + "x " + TextFormatting.GREEN + stack.getDisplayName() + TextFormatting.RESET + ", " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(cap.getPrices().get(i)) + TextFormatting.RESET + " each.") / 2), (height / 2) + (i * 10) - ((25 * 10) / 2), 0xFFFFFF);
					overallAmount = overallAmount + (cap.getPrices().get(i) * stack.getCount());
					
					i++;
				} else if (!stack.isEmpty() && cap.getPrices().get(i) == null) {
					fontRenderer.drawString(stack.getCount() + "x " + TextFormatting.GREEN + stack.getDisplayName() + TextFormatting.RESET + ", " + TextFormatting.GREEN + "$0.00" + TextFormatting.RESET + " each.", (width / 2) - (fontRenderer.getStringWidth(stack.getCount() + "x " + TextFormatting.GREEN + stack.getDisplayName() + TextFormatting.RESET + ", " + TextFormatting.GREEN + "$0.00" + TextFormatting.RESET + " each.") / 2), (height / 2) + (i * 10) - ((25 * 10) / 2), 0xFFFFFF);
					
					i++;
				}
			}
			
			fontRenderer.drawString("Overall Amount: " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(overallAmount), (width / 2) - ((fontRenderer.getStringWidth("Overall Amount: " + TextFormatting.GREEN + NumberFormat.getCurrencyInstance(Locale.US).format(overallAmount))) / 2), (height / 2) + 130, 0xFFFFFF);
		}

		Collections.sort(strings);

		super.drawScreen(x, y, ticks);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		super.initGui();
	}

	@Override
	public void onGuiClosed() {

		super.onGuiClosed();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {

		}

		try {
			super.actionPerformed(button);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void keyTyped(char c, int key) {
		if (key == Keyboard.KEY_E) {
			mc.displayGuiScreen(null);
		}

		try {
			super.keyTyped(c, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) {

	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
