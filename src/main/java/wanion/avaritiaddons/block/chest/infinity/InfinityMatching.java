package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.common.matching.AbstractMatching;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.matcher.NbtMatcher;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

public final class InfinityMatching extends AbstractMatching<InfinityMatching>
{
	private final static int LAST_BIT = 1 << 31;
	private final TileEntityInfinityChest tileEntityInfinityChest;
	private ItemStack stack = ItemStack.EMPTY;
	private int count;

	public InfinityMatching(@Nonnull final TileEntityInfinityChest tileEntityInfinityChest, final int number)
	{
		this(tileEntityInfinityChest, number, null);
	}

	public InfinityMatching(@Nonnull final TileEntityInfinityChest tileEntityInfinityChest, final int number, final NBTTagCompound tagToRead)
	{
		super(null, number, tagToRead);
		this.tileEntityInfinityChest = tileEntityInfinityChest;
		setMatcher(new NbtMatcher(this));
	}

	public void insert(@Nonnull final ItemStack stack)
	{
		if (matches(stack)) {
			if ((count + stack.getCount() & LAST_BIT) == LAST_BIT)
				count = Integer.MAX_VALUE;
			else count += stack.getCount();
		} else if (isEmpty()) {
			this.count = stack.getCount();
			this.stack = stack.copy();
			this.stack.setCount(1);
		}
		setMatcher(new NbtMatcher(this));
		markTileDirty();
	}

	@Nonnull
	public ItemStack extract(int amount, final boolean simulate)
	{
		if (isEmpty())
			return ItemStack.EMPTY;
		final ItemStack newStack = stack.copy();
		amount = MathHelper.clamp(amount, 1, newStack.getMaxStackSize());
		final int remaining = count - amount;
		if (remaining < 0)
			amount += remaining;
		newStack.setCount(amount);
		if (!simulate) {
			count -= amount;
			if (count == 0)
				setEmpty();
		}
		markTileDirty();
		return newStack;
	}

	@Override
	public ItemStack getStack()
	{
		final ItemStack newStack = stack.copy();
		newStack.setCount(count);
		return newStack;
	}

	public ItemStack getActualStack()
	{
		return stack;
	}

	private void setEmpty()
	{
		setStack(ItemStack.EMPTY, 0);
		validate();
	}

	private void setStack(@Nonnull final ItemStack stack, final int count)
	{
		this.stack = stack;
		this.count = count;
		setMatcher(new NbtMatcher(this));
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(final int count)
	{
		this.count = count;
	}

	private void markTileDirty()
	{
		tileEntityInfinityChest.markDirty();
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public String getSimplifiedCount()
	{
		if (count > 0 && count < 1_000)
			return Integer.toString(count);
		else if (count >= 1_000 && count < 1_000_000)
			return count / 1_000 + "K";
		else if (count >= 1_000_000L && count < 1_000_000_000)
			return count / 1_000_000 + "M";
		else if (count >= 1_000_000_000)
			return count / 1_000_000_000 + "B";
		else return Integer.toString(count);
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public String getFormattedCount()
	{
		final String lang = Minecraft.getMinecraft().gameSettings.language;
		return NumberFormat.getInstance(new Locale(lang.substring(0, 1), lang.substring(3, 4))).format(count);
	}

	@Override
	public void customWriteNBT(@Nonnull final NBTTagCompound nbtToWrite)
	{
		stack.writeToNBT(nbtToWrite);
		nbtToWrite.setInteger("count", count);
	}

	@Override
	public void customReadNBT(@Nonnull final NBTTagCompound nbtToRead)
	{
		setStack(new ItemStack(nbtToRead), nbtToRead.getInteger("count"));
	}

	@Override
	@Nonnull
	public InfinityMatching copy()
	{
		return new InfinityMatching(this.tileEntityInfinityChest, this.number, this.writeNBT());
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj instanceof InfinityMatching) {
			final InfinityMatching infinityMatching = (InfinityMatching) obj;
			if (infinityMatching.number == this.number)
				return this.matcher.equals(infinityMatching.matcher) && matches(infinityMatching.stack) && this.count == infinityMatching.count;
			else return false;
		} else return false;
	}
}