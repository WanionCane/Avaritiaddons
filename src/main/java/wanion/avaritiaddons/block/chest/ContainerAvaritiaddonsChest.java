package wanion.avaritiaddons.block.chest;

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
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;

public abstract class ContainerAvaritiaddonsChest <C extends TileEntityAvaritiaddonsChest> extends WContainer<C>
{
	protected int playerInventoryEnds, playerInventoryStarts;

	public ContainerAvaritiaddonsChest(@Nonnull final C wTileEntity, final InventoryPlayer inventoryPlayer)
	{
		super(wTileEntity);
		wTileEntity.openInventory(inventoryPlayer.player);
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 27; x++)
				addSlotToContainer(new Slot(wTileEntity, y * 27 + x, 8 + (18 * x), 18 + (18 * y)));
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 9; x++)
				addSlotToContainer(new Slot(inventoryPlayer, 9 + y * 9 + x, 170 + (18 * x), 194 + (18 * y)));
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 170 + (18 * i), 252));
		playerInventoryEnds = inventorySlots.size();
		playerInventoryStarts = playerInventoryEnds - 36;
	}

	public void onContainerClosed(@Nonnull final EntityPlayer playerIn)
	{
		this.getTile().closeInventory(playerIn);
	}
}