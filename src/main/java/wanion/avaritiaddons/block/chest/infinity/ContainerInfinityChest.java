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
import java.util.ArrayList;
import java.util.List;

public final class ContainerInfinityChest extends ContainerAvaritiaddonsChest<TileEntityInfinityChest>
{
	public ContainerInfinityChest(@Nonnull final TileEntityInfinityChest wTileEntity, @Nonnull final InventoryPlayer inventoryPlayer)
	{
		super(() -> {
					final List<Slot> slotList = new ArrayList<>();
					for (int y = 0; y < 9; y++)
						for (int x = 0; x < 27; x++)
							slotList.add(new InfinitySlot(wTileEntity, y * 27 + x, 8 + (18 * x), 18 + (18 * y)));
					return slotList;
				},
				wTileEntity, inventoryPlayer);
	}

	@Nonnull
	public ItemStack transferStackInSlot(@Nonnull final EntityPlayer entityPlayer, final int slot)
	{
		ItemStack itemstack = null;
		final Slot actualSlot = this.inventorySlots.get(slot);
		if (actualSlot != null && actualSlot.getHasStack()) {
			ItemStack itemstack1 = actualSlot.getStack();
			itemstack = itemstack1.copy();
			if (slot < 269) {
				if (!mergeItemStack(itemstack1, 270, 279, false))
					return ItemStack.EMPTY;
			} else {
				if (!mergeItemStack(itemstack1, 243, 270, false))
					return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
				actualSlot.putStack(ItemStack.EMPTY);
			actualSlot.onSlotChanged();
		}
		return itemstack != null ? itemstack : ItemStack.EMPTY;
	}
}