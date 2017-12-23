package com.zundrel.currency.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.zundrel.currency.common.items.ItemWallet;

public class ContainerItem extends Container {
	/**
	 * The Item Inventory for this Container, only needed if you want to
	 * reference isUseableByPlayer
	 */
	private final InventoryItem inventory;

	/**
	 * Using these will make transferStackInSlot easier to understand and
	 * implement INV_START is the index of the first slot in the Player's
	 * Inventory, so our InventoryItem's number of slots (e.g. 5 slots is array
	 * indices 0-4, so start at 5) Notice how we don't have to remember how many
	 * slots we made? We can just use InventoryItem.INV_SIZE and if we ever
	 * change it, the Container updates automatically.
	 */
	private static final int INV_START = InventoryItem.INV_SIZE,
			INV_END = INV_START + 26, HOTBAR_START = INV_END + 1,
			HOTBAR_END = HOTBAR_START + 8;

	// If you're planning to add armor slots, put those first like this:
	// ARMOR_START = InventoryItem.INV_SIZE, ARMOR_END = ARMOR_START+3,
	// INV_START = ARMOR_END+1, and then carry on like above.

	public ContainerItem(EntityPlayer par1Player, InventoryPlayer inventoryPlayer, InventoryItem inventoryItem) {
		this.inventory = inventoryItem;

		int i;

		// ITEM INVENTORY - you'll need to adjust the slot locations to match
		// your texture file
		// I have them set vertically in columns of 4 to the right of the player
		// model
		for (i = 0; i < InventoryItem.INV_SIZE; ++i) {
			// You can make a custom Slot if you need different behavior,
			// such as only certain item types can be put into this slot
			// We made a custom slot to prevent our inventory-storing item
			// from being stored within itself, but if you want to allow that
			// and
			// you followed my advice at the end of the above step, then you
			// could get away with using the vanilla Slot class
			this.addSlotToContainer(new SlotWallet(this.getItemInventory(), i, 80 + (18 * (i / 4)), 8 + (18 * (i % 4))));
		}

		// If you want, you can add ARMOR SLOTS here as well, but you need to
		// make a public version of SlotArmor. I won't be doing that in this
		// tutorial.
		/*
		 * for (i = 0; i < 4; ++i) { // These are the standard positions for
		 * survival inventory layout this.addSlotToContainer(new
		 * SlotArmor(this.player, inventoryPlayer,
		 * inventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18, i)); }
		 */

		// for (i = 0; i < 4; ++i) {
		// this.addSlotToContainer(new
		// SlotArmor(par1Player, inventoryPlayer,
		// inventoryPlayer.getSizeInventory() - 2 - i, 8, 8 + i * 18, i)); }

		// PLAYER INVENTORY - uses default locations for standard inventory
		// texture file
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// PLAYER ACTION BAR - uses default locations for standard action bar
		// texture file
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	public IInventory getItemInventory() {
		return inventory;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// be sure to return the inventory's isUseableByPlayer method
		// if you defined special behavior there:
		return getItemInventory().isUsableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {
		// ItemStack itemstack = ItemStack.EMPTY;
		// Slot slot = (Slot) this.inventorySlots.get(index);
		//
		// if (slot != null && slot.getHasStack())
		// {
		// ItemStack itemstack1 = slot.getStack();
		// itemstack = itemstack1.copy();
		//
		// // If item is in our custom Inventory or armor slot
		// if (index < INV_START)
		// {
		// // try to place in player inventory / action bar
		// if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true))
		// {
		// slot.putStack(ItemStack.EMPTY);
		// return ItemStack.EMPTY;
		// }
		//
		// slot.onSlotChange(itemstack1, itemstack);
		// }
		// // Item is in inventory / hotbar, try to place in custom inventory or
		// armor slots
		// else
		// {
		// // Check that the item is the right type
		// if (itemstack1.getItem() instanceof ItemMoneyBase)
		// {
		// // Try to merge into your custom inventory slots
		// // We use 'InventoryItem.INV_SIZE' instead of INV_START just in case
		// // you also add armor or other custom slots
		// if (!this.mergeItemStack(itemstack1, 0, InventoryItem.INV_SIZE,
		// false))
		// {
		// return ItemStack.EMPTY;
		// }
		// }
		//
		// /**
		// * Implementation number 1: Shift-click into your custom inventory
		// */
		// if (index >= INV_START)
		// {
		// // place in custom inventory
		// if (!this.mergeItemStack(itemstack1, 0, INV_START, false))
		// {
		// if (CompatLayer.isV10() && ItemStackTools.getStackSize(itemstack1) ==
		// 0)
		// {
		// System.out.println("OH LOOK IT DOES");
		// slot.putStack(ItemStack.EMPTY);
		// }
		// return ItemStack.EMPTY;
		// }
		// }
		//
		// /**
		// * Implementation number 2: Shift-click items between action bar and
		// inventory
		// */
		// // item is in player's inventory, but not in action bar
		// if (index >= INV_START && index < HOTBAR_START)
		// {
		// // place in action bar
		// if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END+1,
		// false))
		// {
		// return ItemStack.EMPTY;
		// }
		// }
		// // item in action bar - place in player inventory
		// else if (index >= HOTBAR_START && index < HOTBAR_END+1)
		// {
		// if (!this.mergeItemStack(itemstack1, INV_START, INV_END+1, false))
		// {
		// return ItemStack.EMPTY;
		// }
		// }
		// }
		//
		// if (ItemStackTools.getStackSize(itemstack1) == 0)
		// {
		// System.out.println("OH LOOK IT DOES");
		// slot.putStack(ItemStack.EMPTY);
		// }
		// else
		// {
		// slot.onSlotChanged();
		// }
		//
		// if (ItemStackTools.getStackSize(itemstack1) ==
		// ItemStackTools.getStackSize(itemstack))
		// {
		// return ItemStack.EMPTY;
		// }
		//
		// slot.onTake(par1EntityPlayer, itemstack1);
		// }
		//
		// return itemstack;
		return ItemStack.EMPTY;
	}

	/**
	 * You should override this method to prevent the player from moving the
	 * stack that opened the inventory, otherwise if the player moves it, the
	 * inventory will not be able to save properly
	 */
	@Override
	public ItemStack slotClick(int slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		// this will prevent the player from interacting with the item that
		// opened the inventory:
		if (slot >= 0 && getSlot(slot).getStack() != ItemStack.EMPTY && (getSlot(slot).getStack().getItem() instanceof ItemWallet)) {
			return ItemStack.EMPTY;
		}
		return super.slotClick(slot, dragType, clickTypeIn, player);
	}
}
