package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wanion.lib.common.IGhostAcceptorContainer;
import wanion.lib.common.IResourceShapedContainer;
import wanion.lib.common.Util;
import wanion.lib.common.WContainer;
import wanion.lib.inventory.slot.DeadSlot;
import wanion.lib.inventory.slot.ShapeSlot;
import wanion.lib.inventory.slot.SpecialSlot;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class ContainerExtremeAutoCrafter extends WContainer<TileEntityExtremeAutoCrafter> implements IResourceShapedContainer, IGhostAcceptorContainer
{
	private final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter;
	private final int playerInventoryEnds, playerInventoryStarts, inventoryFull, shapeEnds, result;

	public ContainerExtremeAutoCrafter(@Nonnull final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter, final InventoryPlayer inventoryPlayer)
	{
		super(tileEntityExtremeAutoCrafter);
		this.tileEntityExtremeAutoCrafter = tileEntityExtremeAutoCrafter;
		final List<Slot> slotList = new ArrayList<>();
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				slotList.add((new Slot(tileEntityExtremeAutoCrafter, y * 9 + x, 8 + (18 * x), 18 + (18 * y))));
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 9; x++)
				slotList.add((new ShapeSlot(tileEntityExtremeAutoCrafter, 81 + (y * 9 + x), 175 + (18 * x), 18 + (18 * y))));
		slotList.add((new DeadSlot(tileEntityExtremeAutoCrafter, 163, 247, 194)));
		slotList.add((new SpecialSlot(tileEntityExtremeAutoCrafter, 162, 247, 222)));
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 9; x++)
				slotList.add((new Slot(inventoryPlayer, 9 + y * 9 + x, 43 + (18 * x), 194 + (18 * y))));
		for (int i = 0; i < 9; i++)
			slotList.add((new Slot(inventoryPlayer, i, 43 + (18 * i), 252)));
		slotList.forEach(this::addSlotToContainer);
		final int inventorySize = inventorySlots.size();
		playerInventoryEnds = inventorySize;
		playerInventoryStarts = inventorySize - 36;
		inventoryFull = (playerInventoryStarts - 2) / 2;
		shapeEnds = inventoryFull * 2;
		result = shapeEnds + 1;
	}

	@Nonnull
	@Override
	public final ItemStack transferStackInSlot(@Nonnull final EntityPlayer entityPlayer, final int slot)
	{
		ItemStack itemstack = null;
		final Slot actualSlot = this.inventorySlots.get(slot);
		if (actualSlot != null && actualSlot.getHasStack()) {
			ItemStack itemstack1 = actualSlot.getStack();
			itemstack = itemstack1.copy();
			if (slot >= playerInventoryStarts) {
				if (!mergeItemStack(itemstack1, 0, inventoryFull, false))
					return ItemStack.EMPTY;
			} else if (slot <= inventoryFull || slot == result) {
				if (!mergeItemStack(itemstack1, playerInventoryStarts, playerInventoryEnds, true))
					return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
				actualSlot.putStack(ItemStack.EMPTY);
			actualSlot.onSlotChanged();
		}
		return itemstack != null ? itemstack : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public final ItemStack slotClick(final int slot, final int mouseButton, @Nonnull final ClickType clickType, @Nonnull final EntityPlayer entityPlayer)
	{
		if (slot >= inventoryFull && slot < shapeEnds) {
			final Slot actualSlot = inventorySlots.get(slot);
			if (clickType == ClickType.QUICK_MOVE) {
				actualSlot.putStack(ItemStack.EMPTY);
			} else if (clickType == ClickType.PICKUP) {
				final ItemStack playerStack = entityPlayer.inventory.getItemStack();
				final boolean slotHasStack = actualSlot.getHasStack();
				if (!playerStack.isEmpty() && !slotHasStack) {
					final ItemStack newSlotStack = playerStack.copy();
					newSlotStack.setCount(1);
					actualSlot.putStack(newSlotStack);
				} else if (playerStack.isEmpty() && slotHasStack || !playerStack.isEmpty() && playerStack.isItemEqual(actualSlot.getStack()))
					actualSlot.putStack(ItemStack.EMPTY);
			}
			tileEntityExtremeAutoCrafter.recipeShapeChanged();
			return ItemStack.EMPTY;
		} else if (slot == shapeEnds) {
			if (inventorySlots.get(slot).getHasStack()) {
				clearShape(tileEntityExtremeAutoCrafter.half, tileEntityExtremeAutoCrafter.full);
				tileEntityExtremeAutoCrafter.recipeShapeChanged();
			}
			return ItemStack.EMPTY;
		} else return super.slotClick(slot, mouseButton, clickType, entityPlayer);
	}

	@Override
	public final void defineShape(@Nonnull final ResourceLocation resourceLocation)
	{
		IExtremeRecipe extremeRecipe = AvaritiaRecipeManager.EXTREME_RECIPES.get(resourceLocation);
		if (extremeRecipe == null)
			return;
		final int slotCount = inventorySlots.size();
		final int startsIn = ((slotCount - 36) / 2) - 1, endsIn = slotCount - 38;
		final int root = (int) Math.sqrt(endsIn - startsIn + 1);
		clearShape(startsIn, endsIn);
		final List<Ingredient> inputs = extremeRecipe.getIngredients();
		if (extremeRecipe.isShapedRecipe()) {
			int i = 0;
			for (int y = 0; y < root; y++) {
				for (int x = 0; x < root; x++) {
					if (i >= inputs.size() || x >= extremeRecipe.getWidth() || y >= extremeRecipe.getHeight())
						continue;
					final Slot slot = inventorySlots.get(startsIn + (x + (root * y)));
					final ItemStack stackInput = Util.getStackFromIngredient(inputs.get(i++));
					if (stackInput != null)
						slot.putStack(stackInput);
				}
			}
		} else {
			for (int i = 0; i < inputs.size() && i < root * root; i++) {
				final Slot slot = inventorySlots.get(startsIn + i);
				final ItemStack stackInput = Util.getStackFromIngredient(inputs.get(i));
				if (stackInput != null)
					slot.putStack(stackInput);
			}
		}
		tileEntityExtremeAutoCrafter.recipeShapeChanged();
		detectAndSendChanges();
	}

	@Override
	public void clearShape()
	{
		final int slotCount = inventorySlots.size();
		final int startsIn = ((slotCount - 36) / 2) - 1, endsIn = slotCount - 38;
		clearShape(startsIn, endsIn);
	}

	private void clearShape(final int startsIn, final int endsIn)
	{
		for (int i = startsIn; i < endsIn; i++)
			inventorySlots.get(i).putStack(ItemStack.EMPTY);
	}

	@Override
	public void acceptGhostStack(final int slot, @Nonnull ItemStack itemStack)
	{
		if (slot >= inventoryFull && slot < shapeEnds) {
			final Slot actualSlot = inventorySlots.get(slot);
			if (itemStack.isItemEqual(actualSlot.getStack()))
				actualSlot.putStack(ItemStack.EMPTY);
			else
				actualSlot.putStack(itemStack);
			tileEntityExtremeAutoCrafter.recipeShapeChanged();
			detectAndSendChanges();
		}
	}
}