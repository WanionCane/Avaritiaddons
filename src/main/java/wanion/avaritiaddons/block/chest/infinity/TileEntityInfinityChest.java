package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.client.ClientConstants;

import javax.annotation.Nonnull;

public final class TileEntityInfinityChest extends TileEntityAvaritiaddonsChest
{
	public TileEntityInfinityChest()
	{
		super(Integer.MAX_VALUE);
	}

	@Override
	public final void setInventorySlotContents(int slot, final ItemStack itemStack)
	{
		if (itemStack == null) {
			inventoryAvaritiaddonsChest.contents[slot] = null;
			return;
		}
		final int perfectSlot = findSlotFor(itemStack);
		if (slot == 243 && perfectSlot == -1)
			return;
		if (perfectSlot != -1 && perfectSlot != slot) {
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
			if (slotStack.stackSize < 0)
				inventoryAvaritiaddonsChest.contents[slot] = null;
		} else inventoryAvaritiaddonsChest.contents[slot] = itemStack;
		markDirty();
	}

	private int findSlotFor(@Nonnull final ItemStack itemStack)
	{
		for (int i = 0; i < 243; i++) {
			final ItemStack slotStack = inventoryAvaritiaddonsChest.contents[i];
			if (slotStack != null && slotStack.getItem() == itemStack.getItem() && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == slotStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, slotStack))
				return i;
		}
		return -1;
	}

	@Override
	public final void readCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
			final int slot = slotCompound.getShort("Slot");
			if (slot >= 0 && slot < 243)
				inventoryAvaritiaddonsChest.setInventorySlotContents(slot, readItemStackFromNbt(slotCompound));
		}
	}

	@Override
	public final NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < 243; i++) {
			final ItemStack itemStack = inventoryAvaritiaddonsChest.getStackInSlot(i);
			if (itemStack != null) {
				final NBTTagCompound slotCompound = new NBTTagCompound();
				slotCompound.setShort("Slot", (short) i);
				nbtTagList.appendTag(writeItemStackToNbt(slotCompound, itemStack));
			}
		}
		nbtTagCompound.setTag("Contents", nbtTagList);
		return nbtTagCompound;
	}

	private NBTTagCompound writeItemStackToNbt(@Nonnull final NBTTagCompound nbtTagCompound, @Nonnull final ItemStack itemStack)
	{
		itemStack.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("intCount", itemStack.stackSize);
		if (itemStack.stackTagCompound != null)
			nbtTagCompound.setTag("tag", itemStack.stackTagCompound);
		return nbtTagCompound;
	}

	private ItemStack readItemStackFromNbt(final NBTTagCompound nbtTagCompound)
	{
		final ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbtTagCompound);
		itemStack.stackSize = nbtTagCompound.getInteger("intCount");
		if (nbtTagCompound.hasKey("tag"))
			itemStack.setTagCompound(nbtTagCompound.getCompoundTag("tag"));
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlot(final int slot)
	{
		return slot <= 242 ? inventoryAvaritiaddonsChest.getStackInSlot(slot) : null;
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemStack)
	{
		return slot != 243 || findSlotFor(itemStack) != -1;
	}

	@Override
	public final int getSizeInventory()
	{
		return 244;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nonnull
	protected ResourceLocation getTexture()
	{
		return ClientConstants.INFINITY_CHEST_ANIMATION.getCurrentFrame();
	}

	@Override
	@Nonnull
	public String getDefaultInventoryName()
	{
		return I18n.format("container.InfinityChest");
	}
}