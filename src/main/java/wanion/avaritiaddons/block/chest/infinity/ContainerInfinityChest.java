package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import wanion.avaritiaddons.block.chest.ContainerAvaritiaddonsChest;

import javax.annotation.Nonnull;

public final class ContainerInfinityChest extends ContainerAvaritiaddonsChest
{
	@SuppressWarnings("unchecked")
	public ContainerInfinityChest(@Nonnull final TileEntityInfinityChest tileEntityCompressedChest, final InventoryPlayer inventoryPlayer)
	{
		super(tileEntityCompressedChest, inventoryPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer entityPlayer, final int slot)
	{
		ItemStack itemstack = null;
		final Slot actualSlot = (Slot) this.inventorySlots.get(slot);

		if (actualSlot != null && actualSlot.getHasStack()) {
			ItemStack itemstack1 = actualSlot.getStack();
			itemstack = itemstack1.copy();

			if (slot > 242) {
				if (!mergeItemStack(itemstack1, 0, 242, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 243, 279, true))
				return null;

			if (itemstack1.stackSize == 0)
				actualSlot.putStack(null);
			actualSlot.onSlotChanged();
		}
		return itemstack;
	}

	@Override
	public boolean mergeItemStack(final ItemStack itemStack, final int start, final int end, final boolean backwards)
	{
		boolean done = false;
		int currentSlot = !backwards ? start : end - 1;

		while (itemStack.stackSize > 0 && (!backwards && currentSlot < end || backwards && currentSlot >= start)) {
			final Slot slot = (Slot) inventorySlots.get(currentSlot);
			final ItemStack slotStack = slot.getStack();

			if (!backwards) {
				if (slotStack == null) {
					slot.putStack(itemStack.copy());
					itemStack.stackSize = 0;
					slot.onSlotChanged();
					done = true;
				} else if (slotStack.getItem() == itemStack.getItem() && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == slotStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, slotStack)) {
					slotStack.stackSize += itemStack.stackSize;
					itemStack.stackSize = 0;
					slot.onSlotChanged();
					done = true;
				}
				++currentSlot;
			} else {
				if (slotStack == null) {
					if (itemStack.stackSize <= itemStack.getMaxStackSize()) {
						slot.putStack(itemStack.copy());
						itemStack.stackSize = 0;
					} else {
						final ItemStack newStack = itemStack.copy();
						newStack.stackSize = itemStack.getMaxStackSize();
						itemStack.stackSize -= itemStack.getMaxStackSize();
						slot.putStack(newStack);
					}
					slot.onSlotChanged();
					done = true;
				} else if (slotStack.getItem() == itemStack.getItem() && slotStack.stackSize < slotStack.getMaxStackSize() && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == slotStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, slotStack)) {
					final int dif = slotStack.getMaxStackSize() - slotStack.stackSize;
					slotStack.stackSize += dif;
					itemStack.stackSize -= dif;
					slot.onSlotChanged();
					done = true;
				}
				--currentSlot;
			}
		}
		return done;
	}

	/*
	@Override
	public ItemStack slotClick(int slotId, int p_75144_2_, int p_75144_3_, EntityPlayer entityPlayer)
	{
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = entityPlayer.inventory;
		int i1;
		ItemStack itemstack3;

		if (p_75144_3_ == 5)
		{
			int l = this.field_94536_g;
			this.field_94536_g = func_94532_c(p_75144_2_);

			if ((l != 1 || this.field_94536_g != 2) && l != this.field_94536_g)
			{
				this.func_94533_d();
			}
			else if (inventoryplayer.getItemStack() == null)
			{
				this.func_94533_d();
			}
			else if (this.field_94536_g == 0)
			{
				this.field_94535_f = func_94529_b(p_75144_2_);

				if (func_94528_d(this.field_94535_f))
				{
					this.field_94536_g = 1;
					this.field_94537_h.clear();
				}
				else
				{
					this.func_94533_d();
				}
			}
			else if (this.field_94536_g == 1)
			{
				Slot slot = (Slot)this.inventorySlots.get(slotId);

				if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.field_94537_h.size() && this.canDragIntoSlot(slot))
				{
					this.field_94537_h.add(slot);
				}
			}
			else if (this.field_94536_g == 2)
			{
				if (!this.field_94537_h.isEmpty())
				{
					itemstack3 = inventoryplayer.getItemStack().copy();
					i1 = inventoryplayer.getItemStack().stackSize;
					Iterator iterator = this.field_94537_h.iterator();

					while (iterator.hasNext())
					{
						Slot slot1 = (Slot)iterator.next();

						if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.field_94537_h.size() && this.canDragIntoSlot(slot1))
						{
							ItemStack itemstack1 = itemstack3.copy();
							int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							func_94525_a(this.field_94537_h, this.field_94535_f, itemstack1, j1);

							if (itemstack1.stackSize > itemstack1.getMaxStackSize())
							{
								itemstack1.stackSize = itemstack1.getMaxStackSize();
							}

							if (itemstack1.stackSize > slot1.getSlotStackLimit())
							{
								itemstack1.stackSize = slot1.getSlotStackLimit();
							}

							i1 -= itemstack1.stackSize - j1;
							slot1.putStack(itemstack1);
						}
					}

					itemstack3.stackSize = i1;

					if (itemstack3.stackSize <= 0)
					{
						itemstack3 = null;
					}

					inventoryplayer.setItemStack(itemstack3);
				}

				this.func_94533_d();
			}
			else
			{
				this.func_94533_d();
			}
		}
		else if (this.field_94536_g != 0)
		{
			this.func_94533_d();
		}
		else
		{
			Slot slot2;
			int l1;
			ItemStack itemstack5;

			if ((p_75144_3_ == 0 || p_75144_3_ == 1) && (p_75144_2_ == 0 || p_75144_2_ == 1))
			{
				if (slotId == -999)
				{
					if (inventoryplayer.getItemStack() != null && slotId == -999)
					{
						if (p_75144_2_ == 0)
						{
							entityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
							inventoryplayer.setItemStack((ItemStack)null);
						}

						if (p_75144_2_ == 1)
						{
							entityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

							if (inventoryplayer.getItemStack().stackSize == 0)
							{
								inventoryplayer.setItemStack((ItemStack)null);
							}
						}
					}
				}
				else if (p_75144_3_ == 1)
				{
					if (slotId < 0)
					{
						return null;
					}

					slot2 = (Slot)this.inventorySlots.get(slotId);

					if (slot2 != null && slot2.canTakeStack(entityPlayer))
					{
						itemstack3 = this.transferStackInSlot(entityPlayer, slotId);

						if (itemstack3 != null)
						{
							Item item = itemstack3.getItem();
							itemstack = itemstack3.copy();

							if (slot2.getStack() != null && slot2.getStack().getItem() == item)
							{
								this.retrySlotClick(slotId, p_75144_2_, true, entityPlayer);
							}
						}
					}
				}
				else
				{
					if (slotId < 0)
					{
						return null;
					}

					slot2 = (Slot)this.inventorySlots.get(slotId);

					if (slot2 != null)
					{
						itemstack3 = slot2.getStack();
						ItemStack itemstack4 = inventoryplayer.getItemStack();

						if (itemstack3 != null)
						{
							itemstack = itemstack3.copy();
						}

						if (itemstack3 == null)
						{
							if (itemstack4 != null && slot2.isItemValid(itemstack4))
							{
								l1 = p_75144_2_ == 0 ? itemstack4.stackSize : 1;

								if (l1 > slot2.getSlotStackLimit())
								{
									l1 = slot2.getSlotStackLimit();
								}

								if (itemstack4.stackSize >= l1)
								{
									slot2.putStack(itemstack4.splitStack(l1));
								}

								if (itemstack4.stackSize == 0)
								{
									inventoryplayer.setItemStack((ItemStack)null);
								}
							}
						}
						else if (slot2.canTakeStack(entityPlayer))
						{
							if (itemstack4 == null)
							{
								l1 = p_75144_2_ == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
								itemstack5 = slot2.decrStackSize(l1);
								inventoryplayer.setItemStack(itemstack5);

								if (itemstack3.stackSize == 0)
								{
									slot2.putStack((ItemStack)null);
								}

								slot2.onPickupFromSlot(entityPlayer, inventoryplayer.getItemStack());
							}
							else if (slot2.isItemValid(itemstack4))
							{
								if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4))
								{
									l1 = p_75144_2_ == 0 ? itemstack4.stackSize : 1;

									if (l1 > slot2.getSlotStackLimit() - itemstack3.stackSize)
									{
										l1 = slot2.getSlotStackLimit() - itemstack3.stackSize;
									}

									if (l1 > itemstack4.getMaxStackSize() - itemstack3.stackSize)
									{
										l1 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
									}

									itemstack4.splitStack(l1);

									if (itemstack4.stackSize == 0)
									{
										inventoryplayer.setItemStack((ItemStack)null);
									}

									itemstack3.stackSize += l1;
								}
								else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
								{
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack3);
								}
							}
							else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4))
							{
								l1 = itemstack3.stackSize;

								if (l1 > 0 && l1 + itemstack4.stackSize <= itemstack4.getMaxStackSize())
								{
									itemstack4.stackSize += l1;
									itemstack3 = slot2.decrStackSize(l1);

									if (itemstack3.stackSize == 0)
									{
										slot2.putStack((ItemStack)null);
									}

									slot2.onPickupFromSlot(entityPlayer, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			}
			else if (p_75144_3_ == 2 && p_75144_2_ >= 0 && p_75144_2_ < 9)
			{
				slot2 = (Slot)this.inventorySlots.get(slotId);

				if (slot2.canTakeStack(entityPlayer))
				{
					itemstack3 = inventoryplayer.getStackInSlot(p_75144_2_);
					boolean flag = itemstack3 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack3);
					l1 = -1;

					if (!flag)
					{
						l1 = inventoryplayer.getFirstEmptyStack();
						flag |= l1 > -1;
					}

					if (slot2.getHasStack() && flag)
					{
						itemstack5 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(p_75144_2_, itemstack5.copy());

						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack3)) && itemstack3 != null)
						{
							if (l1 > -1)
							{
								inventoryplayer.addItemStackToInventory(itemstack3);
								slot2.decrStackSize(itemstack5.stackSize);
								slot2.putStack((ItemStack)null);
								slot2.onPickupFromSlot(entityPlayer, itemstack5);
							}
						}
						else
						{
							slot2.decrStackSize(itemstack5.stackSize);
							slot2.putStack(itemstack3);
							slot2.onPickupFromSlot(entityPlayer, itemstack5);
						}
					}
					else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3))
					{
						inventoryplayer.setInventorySlotContents(p_75144_2_, (ItemStack)null);
						slot2.putStack(itemstack3);
					}
				}
			}
			else if (p_75144_3_ == 3 && entityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0)
			{
				slot2 = (Slot)this.inventorySlots.get(slotId);

				if (slot2 != null && slot2.getHasStack())
				{
					itemstack3 = slot2.getStack().copy();
					itemstack3.stackSize = itemstack3.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack3);
				}
			}
			else if (p_75144_3_ == 4 && inventoryplayer.getItemStack() == null && slotId >= 0)
			{
				slot2 = (Slot)this.inventorySlots.get(slotId);

				if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(entityPlayer))
				{
					itemstack3 = slot2.decrStackSize(p_75144_2_ == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(entityPlayer, itemstack3);
					entityPlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
				}
			}
			else if (p_75144_3_ == 6 && slotId >= 0)
			{
				slot2 = (Slot)this.inventorySlots.get(slotId);
				itemstack3 = inventoryplayer.getItemStack();

				if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(entityPlayer)))
				{
					i1 = p_75144_2_ == 0 ? 0 : this.inventorySlots.size() - 1;
					l1 = p_75144_2_ == 0 ? 1 : -1;

					for (int i2 = 0; i2 < 2; ++i2)
					{
						for (int j2 = i1; j2 >= 0 && j2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); j2 += l1)
						{
							Slot slot3 = (Slot)this.inventorySlots.get(j2);

							if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(entityPlayer) && this.func_94530_a(itemstack3, slot3) && (i2 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
							{
								int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, slot3.getStack().stackSize);
								ItemStack itemstack2 = slot3.decrStackSize(k1);
								itemstack3.stackSize += k1;

								if (itemstack2.stackSize <= 0)
								{
									slot3.putStack((ItemStack)null);
								}

								slot3.onPickupFromSlot(entityPlayer, itemstack2);
							}
						}
					}
				}

				this.detectAndSendChanges();
			}
		}

		return itemstack;
	}
	*/
}