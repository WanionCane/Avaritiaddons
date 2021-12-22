package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public final class InfinitySlot extends Slot
{
	private final InfinityMatching infinityMatching;

	public InfinitySlot(@Nonnull final TileEntityInfinityChest tileEntityInfinityChest, final int index, final int xPosition, final int yPosition)
	{
		super(tileEntityInfinityChest, index, xPosition, yPosition);
		this.infinityMatching = tileEntityInfinityChest.getInfinityMatching(index);
	}

	@Nonnull
	public InfinityMatching getInfinityMatching()
	{
		return infinityMatching;
	}

	@Override
	@Nonnull
	public ItemStack getStack()
	{
		return infinityMatching.getActualStack();
	}

	@Override
	public boolean isItemValid(@Nonnull final ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canTakeStack(@Nonnull final EntityPlayer playerIn)
	{
		return false;
	}
}