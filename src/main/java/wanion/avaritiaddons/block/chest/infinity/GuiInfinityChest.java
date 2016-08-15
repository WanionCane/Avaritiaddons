package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.InventoryPlayer;
import wanion.avaritiaddons.block.chest.GuiAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;

import javax.annotation.Nonnull;

public final class GuiInfinityChest extends GuiAvaritiaddonsChest
{
	private final TileEntityInfinityChest tileEntityInfinityChest;

	public GuiInfinityChest(@Nonnull final TileEntityInfinityChest tileEntityInfinityChest, final InventoryPlayer inventoryPlayer)
	{
		super(new ContainerInfinityChest(tileEntityInfinityChest, inventoryPlayer));
		this.tileEntityInfinityChest = tileEntityInfinityChest;
	}

	@Override
	@Nonnull
	protected TileEntityAvaritiaddonsChest getTileEntity()
	{
		return tileEntityInfinityChest;
	}
}