package com.zundrel.currency.client.render.gui;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zundrel.currency.Currency;
import com.zundrel.currency.common.capabilities.AccountCapability;
import com.zundrel.currency.common.info.ModInfo;
import com.zundrel.currency.common.network.MessageSyncDrops;
import com.zundrel.currency.common.network.PacketDispatcher;
import com.zundrel.currency.common.utils.CurrencyUtils;

public class GuiATM extends GuiScreen
{
	int guiWidth = 175;
    int guiHeight = 165;
    
    int checkmarkWidth = 15;
    int checkmarkHeight = 16;
    
    int declineWidth = 16;
    int declineHeight = 16;
    
    int guiX = (width - guiWidth) / 2;
	int guiY = (height - guiHeight) / 2;
	
	ArrayList<GuiButton> numbers = new ArrayList<GuiButton>();
	GuiButton withdraw;
	GuiButton deposit;
	GuiButton accept;
	GuiButton decline;
	GuiTextField amountBox;
	
	String amount = "";
	
	EntityPlayer player;
	
	public GuiATM(EntityPlayer player)
	{
		super();
		this.player = player;
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GL11.glEnable(GL11.GL_BLEND);
    	drawDefaultBackground();
    	GL11.glDisable(GL11.GL_BLEND);
    	
    	mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/atm.png"));
        drawTexturedModalRect((width / 2) + guiX, (height / 2) + guiY, 0, 0, guiWidth, guiHeight);
    	
        for (GuiButton button : numbers) {
    		button.drawButton(mc, mouseX, mouseY, partialTicks);
    	}
    	
    	withdraw.drawButton(mc, mouseX, mouseY, partialTicks);
    	deposit.drawButton(mc, mouseX, mouseY, partialTicks);
    	accept.drawButton(mc, mouseX, mouseY, partialTicks);
    	decline.drawButton(mc, mouseX, mouseY, partialTicks);
    	
    	AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
    	
    	fontRenderer.drawString("Bank Balance: " + NumberFormat.getCurrencyInstance(Locale.US).format(cap.getAmount()), (width / 2) - (fontRenderer.getStringWidth("Bank Balance: " + NumberFormat.getCurrencyInstance().format(cap.getAmount())) / 2), (height / 2) + 90, 0xFFFFFF, true);
    	fontRenderer.drawString("$25,000 Withdraw Limit.", (width / 2) - (fontRenderer.getStringWidth("$25,000 Withdraw Limit.") / 2), (height / 2) + 100, 0xFFFFFF, true);
    	
    	amountBox.drawTextBox();
	}
	
	@Override
	public void initGui()
	{
		int rowNum = 0;
		int columnNum = 0;
		numbers.clear();
		for (int i = 0; i < 12; i++) {
			if (i == 0 || i == 3 || i == 6) {
				rowNum = 2;
			}
			if (i == 1 || i == 4 || i == 7) {
				rowNum = 1;
			}
			if (i == 2 || i == 5 || i == 8) {
				rowNum = 0;
			}
			if (i / 3 >= 1) {
				columnNum = 1;
			}
			if (i / 3 >= 2) {
				columnNum = 2;
			}
			if (i / 3 >= 3) {
				columnNum = 3;
			}
			if (i == 9) {
				rowNum = 1;
				columnNum = -1;
			}
			if (i == 10) {
				rowNum = 2;
				columnNum = -1;
			}
			if (i == 11) {
				rowNum = 0;
				columnNum = -1;
			}
			if (9 - i == -1) {
				numbers.add(new GuiButton(9 - i, (((width / 2) + guiX) - 40 + ((guiWidth / 4) + 15) + (20 * (rowNum))), ((height / 2) + guiY) + (100) - (20 * (columnNum)), 20, 20, "CE"));
			} else if (9 - i == -2) {
				numbers.add(new GuiButton(9 - i, (((width / 2) + guiX) - 40 + ((guiWidth / 4) + 15) + (20 * (rowNum))), ((height / 2) + guiY) + (100) - (20 * (columnNum)), 20, 20, "<-"));
			} else {
				numbers.add(new GuiButton(9 - i, (((width / 2) + guiX) - 40 + ((guiWidth / 4) + 15) + (20 * (rowNum))), ((height / 2) + guiY) + (100) - (20 * (columnNum)), 20, 20, "" + (9 - i)));
			}
			buttonList.add(numbers.get(i));
			numbers.get(i).x = (((width / 2) + guiX) - 40 + ((guiWidth / 4) + 15) + (20 * (rowNum)));
			numbers.get(i).y = ((height / 2) + guiY) + (100) - (20 * (columnNum));
		}
		
		deposit = new GuiButton(20, ((width / 2) + guiX) + ((guiWidth / 4) + 15) + 40, ((height / 2) + guiY) + (59), 60, 20, "Deposit");
		withdraw = new GuiButton(21, ((width / 2) + guiX) + ((guiWidth / 4) + 15) + 40, ((height / 2) + guiY) + 79, 60, 20, "Withdraw");
		
		accept = new GuiButton(22, ((width / 2) + guiX) + ((guiWidth / 4) + 15) + 40, ((height / 2) + guiY) + (101), 60, 20, "Accept");
		decline = new GuiButton(23, ((width / 2) + guiX) + ((guiWidth / 4) + 15) + 40, ((height / 2) + guiY) + 121, 60, 20, "Decline");
		
		deposit.enabled = false;
		
		buttonList.add(withdraw);
		buttonList.add(deposit);
		buttonList.add(accept);
		buttonList.add(decline);
		
		amountBox = new GuiTextField(0, fontRenderer, ((width / 2) + guiX) + 12, ((height / 2) + guiY + 15), 151, 25);
    	amountBox.setMaxStringLength(100);
    	amountBox.setText("");
		super.initGui();
	}
	
	@Override
    public void keyTyped(char c, int key)
    {
		if (c >= '0' && c <= '9')
		{
			amount = amount.concat("" + c);
		}
		if (key == Keyboard.KEY_BACK && amount != null && !amount.isEmpty()) {
			amount = amount.substring(0, amount.length() - 1);
		}
		if (key == Keyboard.KEY_RETURN) {
			AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
			if (amount != null && !amount.isEmpty() && Float.parseFloat(amount) <= 25000) {
				if (!deposit.enabled) {
					cap.addClientAmount(Float.parseFloat(amount), true);
					CurrencyUtils.depositMoney(player, Float.parseFloat(amount));
				} else if (!withdraw.enabled && Float.parseFloat(amount) <= cap.getAmount()) {
					cap.subtractClientAmount(Float.parseFloat(amount), true);
					PacketDispatcher.sendToServer(new MessageSyncDrops(player, Float.parseFloat(amount)));
				}
			}
			this.mc.displayGuiScreen((GuiScreen)null);
		}
		if (key == Keyboard.KEY_E || key == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}
		if (key == Keyboard.KEY_UP || key == Keyboard.KEY_DOWN) {
			withdraw.enabled = !withdraw.enabled;
			deposit.enabled = !deposit.enabled;
		}
    }
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button != null && button.id >= 0 && button.id < 10) {
			if (!amount.contains(".") || amount.contains(".") && amount.substring(amount.indexOf(".") + 1).length() != 2) {
				amount = amount.concat("" + button.id);
			}
		}
		if (button.id == -1 && !amount.isEmpty()) {
			amount = "";
		}
		if (button.id == -2 && !amount.isEmpty()) {
			amount = amount.substring(0, amount.length() - 1);
		}
		if (button.id == 20) {
			deposit.enabled = false;
			withdraw.enabled = true;
		}
		if (button.id == 21) {
			deposit.enabled = true;
			withdraw.enabled = false;
		}
		if (button.id == 22) {
			AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
			if (amount != null && !amount.isEmpty() && Float.parseFloat(amount) <= 25000) {
				if (!deposit.enabled) {
					cap.addClientAmount(Float.parseFloat(amount), true);
					CurrencyUtils.depositMoney(player, Float.parseFloat(amount));
				} else if (!withdraw.enabled && Float.parseFloat(amount) <= cap.getAmount()) {
					cap.subtractClientAmount(Float.parseFloat(amount), true);
					PacketDispatcher.sendToServer(new MessageSyncDrops(player, Float.parseFloat(amount)));
				}
			}
			this.mc.displayGuiScreen((GuiScreen)null);
		}
		if (button.id == 23) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}
			
		super.actionPerformed(button);
	}
	
	@Override
	public void updateScreen()
	{
		AccountCapability cap = player.getCapability(Currency.ACCOUNT_DATA, null);
		NumberFormat format = NumberFormat.getInstance();
		if (!withdraw.enabled && amount != null && !amount.isEmpty() && Float.parseFloat(amount) > 25000) {
			amount = "25000";
		} else if (!withdraw.enabled && amount != null && !amount.isEmpty() && Float.parseFloat(amount) > cap.getAmount()) {
			amount = "" + cap.getAmount();
		}
		
		if (!deposit.enabled && amount != null && !amount.isEmpty() && Float.parseFloat(amount) > CurrencyUtils.getCurrencyNoWallet(player)) {
			amount = "" + CurrencyUtils.getCurrencyNoWallet(player);
		}
		
		if (amount != null && !amount.isEmpty()) {
			if (amount.contains(".") && amount.substring(amount.indexOf(".") + 1).length() == 0) {
				amountBox.setText("$" + format.format(Float.parseFloat(amount)) + ".");
			} else {
				amountBox.setText("$" + format.format(Float.parseFloat(amount)));
			}
		} else if (amount.isEmpty()) {
			amountBox.setText("$0");
		}
		
		if (amount != null && !amount.isEmpty()) { 
			amount = "" + Math.round(Float.parseFloat(amount));
		}
	}
}
