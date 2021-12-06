package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.proxy.ClientProxy;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;

public final class TileEntityInfinityChest extends TileEntityAvaritiaddonsChest
{
	private final InfinityChestInvWrapper infinityChestInvWrapper;
	private final InfinityMatching[] infinityMatchings = new InfinityMatching[243];

	public TileEntityInfinityChest()
	{
		final MatchingController matchingController = getController(MatchingController.class);
		for (int i = 0; i < 243; i++)
			matchingController.add((infinityMatchings[i] = new InfinityMatching(itemStacks, i)));
		infinityChestInvWrapper = this.new InfinityChestInvWrapper();
		addCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, infinityChestInvWrapper);
	}

	@Override
	protected NonNullList<ItemStack> getItemStacks()
	{
		return NonNullList.withSize(243, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory()
	{
		return 244;
	}

	public InfinityMatching getInfinityMatching(final int num)
	{
		return infinityMatchings[num];
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(final int index)
	{
		return infinityChestInvWrapper.getStackInSlot(index);
	}

	@Override
	@Nonnull
	public ItemStack decrStackSize(final int index, final int count)
	{
		return infinityChestInvWrapper.extractItem(index, count, false);
	}

	@Override
	@Nonnull
	public ItemStack removeStackFromSlot(final int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(final int index, @Nonnull final ItemStack stack)
	{
		infinityChestInvWrapper.setStackInSlot(index, stack);
	}

	public int getInventoryStackLimit()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isItemValidForSlot(final int index, @Nonnull final ItemStack stack)
	{
		return infinityChestInvWrapper.isItemValid(index, stack);
	}

	@Override
	public boolean canInsertItem(final int index,  @Nonnull final ItemStack itemStackIn, @Nonnull final EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(final int index, @Nonnull final ItemStack stack, @Nonnull final EnumFacing direction)
	{
		return false;
	}

	@Nonnull
	@Override
	public String getDefaultName()
	{
		return "container.infinity_chest.name";
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ResourceLocation getTexture()
	{
		return ClientProxy.INFINITY_CHEST_ANIMATION.getCurrentFrame();
	}

	private class InfinityChestInvWrapper implements IItemHandlerModifiable
	{
		private InfinityChestInvWrapper() {}

		private int findSlotFor(@Nonnull final ItemStack itemStack)
		{
			for (final InfinityMatching infinityMatching : infinityMatchings)
				if (infinityMatching.matches(itemStack))
					return infinityMatching.getNumber();
			return -1;
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull final ItemStack itemStack)
		{
			/*
			if (slot == 243)
				return;
			else if (itemStack.isEmpty()) {
				getInfinityMatching(slot).setStack(itemStack);
				markDirty();
				return;
			}
			final int perfectSlot = findSlotFor(itemStack);
			if (perfectSlot == -1)
				return;
			final InfinityMatching infinityMatching = getInfinityMatching(perfectSlot);
			if (perfectSlot != slot) {
				slot = perfectSlot;
				if (inventoryAvaritiaddonsChest.contents[slot] != null) {
					inventoryAvaritiaddonsChest.contents[slot].stackSize += itemStack.stackSize;
					itemStack.stackSize = 0;
				} else inventoryAvaritiaddonsChest.contents[slot] = itemStack;
			} else if (perfectSlot != -1) {
				final ItemStack slotStack = inventoryAvaritiaddonsChest.contents[(slot = perfectSlot)];
				final int dif = slotStack.stackSize - itemStack.stackSize;
				slotStack.stackSize -= dif;
				itemStack.stackSize += dif;
				if (slotStack.stackSize <= 0)
					inventoryAvaritiaddonsChest.contents[slot] = null;
			} else inventoryAvaritiaddonsChest.contents[slot] = itemStack;
			markDirty();
			 */
		}

		@Override
		public int getSlots()
		{
			return 244;
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(final int slot)
		{
			if (slot == 243 || slot == -1)
				return ItemStack.EMPTY;
			final ItemStack newStack = itemStacks.get(slot).copy();
			newStack.setCount(getInfinityMatching(slot).getCount());
			return newStack;
		}

		@Nonnull
		@Override
		public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate)
		{
			final int perfectSlot = findSlotFor(stack);
			return perfectSlot == -1 ? stack : ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			if (slot == 243)
				return ItemStack.EMPTY;
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isItemValid(final int slot, @Nonnull final ItemStack stack)
		{
			return slot != 243 || findSlotFor(stack) != -1;
		}
	}
}