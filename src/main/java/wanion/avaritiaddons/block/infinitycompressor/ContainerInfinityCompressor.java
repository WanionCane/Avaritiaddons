package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wanion.lib.common.IResourceShapedContainer;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;

public class ContainerInfinityCompressor extends WContainer<TileEntityInfinityCompressor> implements IResourceShapedContainer
{
	public ContainerInfinityCompressor(@Nonnull final TileEntityInfinityCompressor wTileEntity, final InventoryPlayer inventoryPlayer)
	{
		super(wTileEntity);
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 9; x++)
				addSlotToContainer(new Slot(inventoryPlayer, 9 + y * 9 + x, 8 + (18 * x), 84 + (18 * y)));
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + (18 * i), 142));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(@Nonnull final EntityPlayer entityPlayer, final int slot)
	{
		ItemStack itemstack = null;
		final Slot actualSlot = this.inventorySlots.get(slot);
		if (actualSlot != null && actualSlot.getHasStack()) {
			ItemStack itemstack1 = actualSlot.getStack();
			itemstack = itemstack1.copy();
			if (slot < 27) {
				if (!mergeItemStack(itemstack1, 27, 36, false))
					return ItemStack.EMPTY;
			} else {
				if (!mergeItemStack(itemstack1, 0, 27, false))
					return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
				actualSlot.putStack(ItemStack.EMPTY);
			actualSlot.onSlotChanged();
		}
		return itemstack != null ? itemstack : ItemStack.EMPTY;
	}

	@Override
	public void defineShape(@Nonnull final ResourceLocation resourceLocation)
	{
		getTile().compressorRecipeField.setCompressorRecipe(AvaritiaRecipeManager.COMPRESSOR_RECIPES.get(resourceLocation));
	}

	@Override
	public void clearShape()
	{
		getTile().compressorRecipeField.setCompressorRecipe(null);
	}
}