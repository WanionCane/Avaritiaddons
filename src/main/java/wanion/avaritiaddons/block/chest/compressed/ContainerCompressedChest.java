package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import wanion.avaritiaddons.block.chest.ContainerAvaritiaddonsChest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@ChestContainer(rowSize = 27)
public final class ContainerCompressedChest extends ContainerAvaritiaddonsChest<TileEntityCompressedChest>
{
	public ContainerCompressedChest(@Nonnull TileEntityCompressedChest wTileEntity, @Nonnull final InventoryPlayer inventoryPlayer)
	{
		super(() -> {
					final List<Slot> slotList = new ArrayList<>();
					for (int y = 0; y < 9; y++)
						for (int x = 0; x < 27; x++)
							slotList.add(new Slot(wTileEntity, y * 27 + x, 8 + (18 * x), 18 + (18 * y)));
					return slotList;
				},
				wTileEntity, inventoryPlayer);
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(@Nonnull final EntityPlayer entityPlayer, final int slot)
	{
		ItemStack itemstack = null;
		final Slot actualSlot = inventorySlots.get(slot);
		if (actualSlot != null && actualSlot.getHasStack()) {
			ItemStack itemstack1 = actualSlot.getStack();
			itemstack = itemstack1.copy();
			if (slot >= playerInventoryStarts) {
				if (!mergeItemStack(itemstack1, 0, playerInventoryStarts, false))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemstack1, playerInventoryStarts, playerInventoryEnds, true))
				return ItemStack.EMPTY;
			if (itemstack1.getCount() == 0)
				actualSlot.putStack(ItemStack.EMPTY);
			else
				actualSlot.onSlotChanged();
			if (itemstack1.getCount() != itemstack.getCount())
				actualSlot.onTake(entityPlayer, itemstack1);
		}
		return itemstack != null ? itemstack : ItemStack.EMPTY;
	}

}