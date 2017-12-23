package com.zundrel.currency.client.render.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zundrel.currency.common.blocks.tiles.TileEntityShopController;
import com.zundrel.currency.common.network.MessageSyncController;
import com.zundrel.currency.common.network.PacketDispatcher;

@SideOnly(Side.CLIENT)
public class GuiShopController extends GuiScreen {
	GuiTextField textField;

	BlockPos pos;
	World world;

	public GuiShopController(BlockPos pos, World world) {
		super();
		this.pos = pos;
		this.world = world;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int x, int y, float ticks) {
		GL11.glEnable(GL11.GL_BLEND);
		drawDefaultBackground();
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glColor4f(1, 1, 1, 1);
		textField.drawTextBox();
		fontRenderer.drawStringWithShadow("Shop Name", ((width) / 2) - (fontRenderer.getStringWidth("Shop Name") / 2), (((height) / 2) - (15 / 2)) + 20, 0xFFFFFF);

		super.drawScreen(x, y, ticks);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		textField = new GuiTextField(0, fontRenderer, ((width) / 2) - (88 / 2), ((height) / 2) - (15 / 2), 88, 15);
		textField.setMaxStringLength(60);
		textField.setText("");
		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		if (!textField.getText().isEmpty()) {
			if (world.getTileEntity(pos) instanceof TileEntityShopController) {
				TileEntityShopController controller = (TileEntityShopController) world.getTileEntity(pos);
				controller.setName(textField.getText());
				PacketDispatcher.sendToServer(new MessageSyncController(textField.getText(), pos));
			}
		}
		super.onGuiClosed();
	}

	@Override
	protected void keyTyped(char c, int key) {
		if (key == Keyboard.KEY_E && this.textField.isFocused() == false) {
			mc.displayGuiScreen(null);
		}

		if (key == Keyboard.KEY_RETURN && this.textField.isFocused()) {
			mc.displayGuiScreen(null);
		}

		if (this.textField.isFocused()) {
			textField.textboxKeyTyped(c, key);
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
		this.textField.mouseClicked(x, y, btn);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.textField.updateCursorCounter();
	}
}
