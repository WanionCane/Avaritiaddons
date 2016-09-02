package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExtremeAutoCrafter extends TileEntity implements ISidedInventory
{
	private final ItemStack[] itemStacks = new ItemStack[163];
	private final InventoryCrafting craftingMatrix = new ExtremeCraftingMatrix();
	private static final int[] slotsForAllSides = new int[82];
	private boolean recipeChanged = true;

	static {
		for (int i = 0; i < 81; i++)
			slotsForAllSides[i] = i;
		slotsForAllSides[81] = 162;
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote && recipeChanged) {
			final ItemStack output = ExtremeCraftingManager.getInstance().findMatchingRecipe(craftingMatrix, worldObj);
			final ItemStack slotStack = getStackInSlot(162);
			if (slotStack != null && slotStack.stackSize > 0)
				recipeChanged = false;
			else if (output == null && slotStack != null && slotStack.stackSize == 0) {
				itemStacks[162] = null;
				markDirty();
			} else if (output != null) {
				output.stackSize = 0;
				itemStacks[162] = output;
				markDirty();
			} else
				recipeChanged = false;
		}
	}

	@Override
	public int getSizeInventory()
	{
		return 163;
	}

	@Override
	public ItemStack getStackInSlot(final int slot)
	{
		return itemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, final int howMuch)
	{
		if (itemStacks[slot] == null)
			return null;
		final ItemStack itemStack = itemStacks[slot].copy();
		if ((itemStacks[slot].stackSize -= howMuch) == 0)
			itemStacks[slot] = null;
		itemStack.stackSize = howMuch;
		markDirty();
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot)
	{
		return itemStacks[slot];
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack itemStack)
	{
		if (slot > 80 && slot < 162)
			recipeChanged = true;
		itemStacks[slot] = itemStack;
		markDirty();
	}

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public final boolean isUseableByPlayer(final EntityPlayer entityPlayer)
	{
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public final Packet getDescriptionPacket()
	{
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, nbttagcompound);
	}

	@Override
	public final void onDataPacket(final NetworkManager networkManager, final S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public final void readFromNBT(final NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		readCustomNBT(nbtTagCompound);
	}

	@Override
	public final void writeToNBT(final NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		writeCustomNBT(nbtTagCompound);
	}

	public void readCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
			final int slot = slotCompound.getShort("Slot");
			if (slot >= 0 && slot < 163)
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(slotCompound));
		}
	}

	public NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < 162; i++) {
			final ItemStack itemStack = getStackInSlot(i);
			if (itemStack != null) {
				final NBTTagCompound slotCompound = new NBTTagCompound();
				slotCompound.setShort("Slot", (short) i);
				nbtTagList.appendTag(itemStack.writeToNBT(slotCompound));
			}
		}
		final ItemStack output = getStackInSlot(162);
		if (output != null && output.stackSize > 0) {
			final NBTTagCompound slotCompound = new NBTTagCompound();
			slotCompound.setShort("Slot", (short) 162);
			nbtTagList.appendTag(output.writeToNBT(slotCompound));
		}
		nbtTagCompound.setTag("Contents", nbtTagList);
		return nbtTagCompound;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemStack)
	{
		return slot >= 0 && slot < 81;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int side)
	{
		return slotsForAllSides;
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack itemStack, final int side)
	{
		return slot >= 0 && slot < 81;
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack itemStack, final int side)
	{
		return slot == 162;
	}

	public class ExtremeCraftingMatrix extends InventoryCrafting
	{
		public ExtremeCraftingMatrix()
		{
			super(new Container()
			{
				@Override
				public boolean canInteractWith(final EntityPlayer entityPlayer)
				{
					return false;
				}
			}, 9, 9);
		}

		@Override
		public ItemStack getStackInSlot(final int slot)
		{
			return itemStacks[slot + 81];
		}

		@Override
		public void setInventorySlotContents(final int slot, final ItemStack itemStack)
		{
			recipeChanged = true;
			itemStacks[slot + 81] = itemStack;
			markDirty();
		}

		@Override
		public ItemStack decrStackSize(int slot, final int howMuch)
		{
			if (itemStacks[slot += 81] == null)
				return null;
			final ItemStack itemStack = itemStacks[slot].copy();
			if ((itemStacks[slot].stackSize -= howMuch) == 0)
				itemStacks[slot] = null;
			itemStack.stackSize = howMuch;
			markDirty();
			return itemStack;
		}
	}
}