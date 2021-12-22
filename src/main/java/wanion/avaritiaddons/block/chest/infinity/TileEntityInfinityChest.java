package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
import java.util.ArrayList;
import java.util.List;

public final class TileEntityInfinityChest extends TileEntityAvaritiaddonsChest
{
	private static final NonNullList<ItemStack> EMPTY_NON_NULL_LIST = NonNullList.withSize(0, ItemStack.EMPTY);
	private final List<InfinityMatching> infinityMatchingList = new ArrayList<>();
	private final InfinityChestInvWrapper infinityChestInvWrapper;

	public TileEntityInfinityChest()
	{
		final MatchingController matchingController = getController(MatchingController.class);
		for (int i = 0; i < 243; i++)
			addInfinityMatching(matchingController, i);
		infinityChestInvWrapper = this.new InfinityChestInvWrapper();
		addCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, infinityChestInvWrapper);
	}

	public InfinityMatching getInfinityMatching(final int num)
	{
		return infinityMatchingList.get(num);
	}

	private void addInfinityMatching(@Nonnull final MatchingController matchingController, final int number)
	{
		final InfinityMatching infinityMatching = new InfinityMatching(this, number);
		infinityMatchingList.add(infinityMatching);
		matchingController.add(infinityMatching);
	}

	@Override
	protected NonNullList<ItemStack> getItemStacks()
	{
		return EMPTY_NON_NULL_LIST;
	}

	@Override
	public int getSizeInventory()
	{
		return 244;
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
		return index != 243;
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
			for (final InfinityMatching infinityMatching : infinityMatchingList)
				if (infinityMatching.accepts(itemStack) && infinityMatching.matches(itemStack))
					return infinityMatching.getNumber();
			for (final InfinityMatching infinityMatching : infinityMatchingList)
				if (infinityMatching.isEmpty())
					return infinityMatching.getNumber();
			return -1;
		}

		private int findSlotFor(final int slot, @Nonnull final ItemStack itemStack)
		{
			return getInfinityMatching(slot).matches(itemStack) ? slot : findSlotFor(itemStack);
		}

		@Override
		public void setStackInSlot(final int slot, @Nonnull final ItemStack itemStack)
		{
			if (slot == -1 || slot == 243)
				return;
			getInfinityMatching(findSlotFor(slot, itemStack)).insert(itemStack);
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
			if (slot == -1 || slot == 243)
				return ItemStack.EMPTY;
			return getInfinityMatching(slot).getStack();
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull final ItemStack stack, final boolean simulate)
		{
			if (slot == 243)
				return stack;
			final InfinityMatching infinityMatching = infinityMatchingList.get(slot);
			slot = infinityMatching.isEmpty() || infinityMatching.matches(stack) ? slot : findSlotFor(stack);
			if (slot == -1)
				return stack;
			if (!simulate)
				setStackInSlot(slot, stack);
			return ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(final int slot, final int amount, final boolean simulate)
		{
			if (slot == 243)
				return ItemStack.EMPTY;
			return getInfinityMatching(slot).extract(amount, simulate);
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull final ItemStack stack)
		{
			if (slot == 243)
				slot = findSlotFor(stack);
			final InfinityMatching infinityMatching = slot != -1 ? infinityMatchingList.get(slot) : null;
			return infinityMatching != null ? infinityMatching.isEmpty() || infinityMatching.matches(stack) : findSlotFor(stack) != -1;
		}
	}
}