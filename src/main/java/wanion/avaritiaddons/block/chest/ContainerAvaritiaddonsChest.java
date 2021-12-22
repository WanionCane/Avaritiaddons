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
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public abstract class ContainerAvaritiaddonsChest <C extends TileEntityAvaritiaddonsChest> extends WContainer<C>
{
	protected final int playerInventoryEnds, playerInventoryStarts;

	public ContainerAvaritiaddonsChest(@Nonnull final Supplier<List<Slot>> slotSupplier, @Nonnull final C wTileEntity, final InventoryPlayer inventoryPlayer)
	{
		super(wTileEntity);
		wTileEntity.openInventory(inventoryPlayer.player);
		slotSupplier.get().forEach(this::addSlotToContainer);
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