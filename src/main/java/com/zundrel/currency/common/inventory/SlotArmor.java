package com.zundrel.currency.common.inventory;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotArmor extends Slot {
	private EntityPlayer player = null;
	private int armorNumber = 0;
	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

	public SlotArmor(EntityPlayer player, IInventory inv, int index, int xPos, int yPos, int armorNumber) {
		super(inv, index, xPos, yPos);
		this.player = player;
		this.armorNumber = armorNumber;
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor
	 * slots as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(ItemStack stack) {
		final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[armorNumber];
		return stack.getItem().isValidArmor(stack, entityequipmentslot, Minecraft.getMinecraft().player);
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		ItemStack itemstack = this.getStack();
		return itemstack != ItemStack.EMPTY && !playerIn.isCreative() ? false : super.canTakeStack(playerIn);
	}

	@Override
	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[armorNumber];
		return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
	}
}