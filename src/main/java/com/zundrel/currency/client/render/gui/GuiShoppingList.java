package com.zundrel.currency.client.render.gui;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.CartCapability;
import com.zundrel.currency.common.network.MessageSyncClearList;
import com.zundrel.currency.common.network.PacketDispatcher;

@SideOnly(Side.CLIENT)
public class GuiShoppingList extends GuiScreen {
	int guiWidth = 256;
	int guiHeight = 256;

	int guiX = (width - guiWidth) / 2;
	int guiY = (height - guiHeight) / 2;

	EntityPlayer player;

	GuiButton clearList;

	public GuiShoppingList(EntityPlayer player) {
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
		clearList = new GuiButton(0, (width / 2) - 35, (height / 2) + 150, 70, 20, "Clear List");
		buttonList.add(clearList);
		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			System.out.println("HEH DUDE");
			CartCapability entityData = player.getCapability(Currency.CART_DATA, null);

			entityData.setCart(NonNullList.withSize(entityData.getSizeInventory(), ItemStack.EMPTY), true);
			List<Float> prices = Arrays.asList(new Float[25]);
			for (int i = 0; i < prices.size(); i++) {
				prices.set(i, (float) 0);
			}
			entityData.setPrices(prices, true);

			PacketDispatcher.sendToServer(new MessageSyncClearList(player));

			mc.displayGuiScreen(null);
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
		try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
