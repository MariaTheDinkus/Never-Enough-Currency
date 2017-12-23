package com.zundrel.currency.client.render.gui;

import java.text.NumberFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.blocks.tiles.TileEntityStockCrate;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.info.ModInfo;
import com.zundrel.currency.common.inventory.ContainerStockCrate;
import com.zundrel.currency.common.network.MessageSyncPrice;
import com.zundrel.currency.common.network.PacketDispatcher;

@SideOnly(Side.CLIENT)
public class GuiStockCrate extends GuiContainer {
	EntityPlayer player;
	BlockPos pos;

	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/gui/stock_crate.png");
	private TileEntityStockCrate tile;

	GuiTextField amountBox;

	String amount = "";

	public GuiStockCrate(EntityPlayer player, BlockPos pos, InventoryPlayer invPlayer, TileEntityStockCrate tile) {
		super(new ContainerStockCrate(invPlayer, tile));
		this.tile = tile;

		xSize = 176;
		ySize = 168;

		this.player = player;
		this.pos = pos;

		if (tile.getAmount() != 0) {
			amount = "" + Float.parseFloat("" + tile.getAmount());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

	}

	@Override
	public void initGui() {
		amountBox = new GuiTextField(0, fontRenderer, ((width / 2) - 29), (height / 2) - 12, 58, 12);
		amountBox.setMaxStringLength(100);
		amountBox.setText("");
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		amountBox.drawTextBox();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString(tile.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, 0xFFFFFF);
	}

	@Override
	public void onGuiClosed() {
		if (amount != null && !amount.isEmpty() && Float.parseFloat(amount) <= 100000) {
			tile.setAmount(Float.parseFloat(amount));
			PacketDispatcher.sendToServer(new MessageSyncPrice(Float.parseFloat(amount), pos));
		} else if (amount == null || amount.isEmpty()) {
			tile.setAmount(0);
			PacketDispatcher.sendToServer(new MessageSyncPrice(0, pos));
		}
	}

	@Override
	public void keyTyped(char c, int key) {
		if (c >= '0' && c <= '9') {
			if (!amount.contains(".") || amount.contains(".") && amount.substring(amount.lastIndexOf(".")).length() < 3) {
				System.out.println("WHAT");
				amount = amount.concat("" + c);
			}
		}
		if (c == '.') {
			if (!amount.isEmpty() && !amount.contains(".")) {
				amount = amount.concat("" + c);
			} else if (amount.isEmpty()) {
				amount = amount.concat("0" + c);
			}
		}
		if (key == Keyboard.KEY_BACK && amount != null && !amount.isEmpty()) {
			amount = amount.substring(0, amount.length() - 1);
		}
		if (key == Keyboard.KEY_RETURN) {
			AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
			if (amount != null && !amount.isEmpty() && Float.parseFloat(amount) <= 100000) {

				// if (!deposit.enabled) {
				// cap.addClientAmount(Float.parseFloat(amount), true);
				// CurrencyUtils.depositMoney(player, Float.parseFloat(amount));
				// } else if (!withdraw.enabled && Float.parseFloat(amount) <=
				// cap.getAmount()) {
				// cap.subtractClientAmount(Float.parseFloat(amount), true);
				// PacketDispatcher.sendToServer(new MessageSyncDrops(player,
				// Float.parseFloat(amount)));
				// }
			}
			this.mc.displayGuiScreen((GuiScreen) null);
		}
		if (key == Keyboard.KEY_E || key == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen((GuiScreen) null);
		}
	}

	@Override
	public void updateScreen() {
		AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
		NumberFormat format = NumberFormat.getInstance();
		if (amount != null && !amount.isEmpty() && Float.parseFloat(amount) > 100000) {
			amount = "100000";
		}

		if (amount != null && !amount.isEmpty()) {
			amountBox.setText("$" + amount);
		} else if (amount.isEmpty()) {
			amountBox.setText("$0");
		}

		if (!amount.isEmpty() && !amount.contains(".") && Float.parseFloat(amount) == 0) {
			amount = "";
		}

		if (amount != null && !amount.isEmpty()) {
			// amount = "" + Math.round(Float.parseFloat(amount));
		}
	}
}