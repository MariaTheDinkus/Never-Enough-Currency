package com.zundrel.currency.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSameItem extends Slot {
	public SlotSameItem(IInventory inv, int index, int xPos, int yPos) {
		super(inv, index, xPos, yPos);
	}

	// This is the only method we need to override so that
	// we can't place our inventory-storing Item within
	// its own inventory (thus making it permanently inaccessible)
	// as well as preventing abuse of storing backpacks within backpacks
	/**
	 * Check if the stack is a valid item for this slot.
	 */
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isEmpty = true;

		for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
			if (!this.inventory.getStackInSlot(i).isEmpty()) {
				isEmpty = false;
				break;
			}
		}

		if (isEmpty) {
			return true;
		} else if (!itemstack.isEmpty()) {
			for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
				if (!this.inventory.getStackInSlot(i).isEmpty() && this.inventory.getStackInSlot(i).getItem() == itemstack.getItem()) {
					return true;
				}
			}
		}
		return false;
	}
}