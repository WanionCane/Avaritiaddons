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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.LocaleUtils;
import wanion.lib.common.matching.AbstractMatching;
import wanion.lib.common.matching.matcher.AbstractMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.List;

public final class InfinityMatching extends AbstractMatching<InfinityMatching>
{
	private final static long LAST_BIT = 1L << 31;
	// basically the max "stack" size will be 2^31-1 =D
	private int count;

	public InfinityMatching(@Nonnull final List<ItemStack> itemStacks, final int number)
	{
		super(itemStacks, number, null);
	}

	public InfinityMatching(@Nonnull final List<ItemStack> itemStacks, final int number, final NBTTagCompound tagToRead)
	{
		super(itemStacks, number, tagToRead);
	}

	public void setStack(@Nonnull final ItemStack itemStack)
	{
		itemStacks.set(number, itemStack);
		validate();
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	@Override
	@Nonnull
	public AbstractMatcher<?> getDefaultMatcher()
	{
		return new ItemStackMatcher(this).validate();
	}

	@Override
	public void nextMatcher()
	{
		this.matcher = matcher.next().validate();
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public String getSimplifiedCount()
	{
		if (count > 0 && count < 1_000)
			return Long.toString(count);
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
		return NumberFormat.getInstance(LocaleUtils.toLocale(Minecraft.getMinecraft().gameSettings.language)).format(count);
	}

	@Override
	public void customWriteNBT(@Nonnull final NBTTagCompound nbtToWrite)
	{
		nbtToWrite.setInteger("count", count);
	}

	@Override
	public void customReadNBT(@Nonnull final NBTTagCompound nbtToRead)
	{
		this.count = nbtToRead.getInteger("count");
	}

	@Override
	@Nonnull
	public InfinityMatching copy()
	{
		return new InfinityMatching(this.itemStacks, this.number, this.writeNBT());
	}
}