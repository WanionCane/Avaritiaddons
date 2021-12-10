package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wanion.lib.common.WTileEntity;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.redstone.RedstoneControl;
import wanion.lib.common.field.CheckBox;
import wanion.lib.common.field.FieldController;

import javax.annotation.Nonnull;

public class TileEntityInfinityCompressor extends WTileEntity
{
	private static final int[] slotsForFace = new int[243];

	public final CompressorRecipeField compressorRecipeField = new CompressorRecipeField(this);
	public final RedstoneControl redstoneControl;
	public final CheckBox trashBox = new CheckBox("avaritiaddons.compressor.trashcan", true);

	public TileEntityInfinityCompressor()
	{
		getController(ControlController.class).add((this.redstoneControl = new RedstoneControl(this)));
		final FieldController fieldController = getController(FieldController.class);
		fieldController.add(compressorRecipeField);
		fieldController.add(trashBox);
		addCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new InvWrapper(this));
	}

	private boolean metConditions(@Nonnull final ItemStack stack)
	{
		ItemStack output;
		if (compressorRecipeField.isNull())
			compressorRecipeField.setCompressorRecipe(AvaritiaRecipeManager.getCompressorRecipeFromInput(stack));
		else if (!trashBox.isChecked() && !compressorRecipeField.matches(stack))
			return false;
		return redstoneControl.canOperate() && !compressorRecipeField.isNull() && ((output = itemStacks.get(0)).isEmpty() || output.getCount() + compressorRecipeField.getCompressorRecipeOutput().getCount() <= output.getMaxStackSize());
	}

	@Override
	public boolean shouldRefresh(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState oldState, @Nonnull final IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	protected NonNullList<ItemStack> getItemStacks()
	{
		return NonNullList.withSize(1, ItemStack.EMPTY);
	}

	@Nonnull
	public int[] getSlotsForFace(@Nonnull final EnumFacing side)
	{
		return slotsForFace.clone();
	}

	@Nonnull
	public ItemStack decrStackSize(final int index, final int count)
	{
		return index == 0 ? super.decrStackSize(index, count) : ItemStack.EMPTY;
	}

	@Nonnull
	public ItemStack getStackInSlot(final int index)
	{
		return index == 0 ? super.getStackInSlot(index) : ItemStack.EMPTY;
	}

	@Override
	public boolean canInsertItem(final int index, @Nonnull final ItemStack itemStackIn, @Nonnull final EnumFacing direction)
	{
		return index > 0 && this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean isItemValidForSlot(final int index, @Nonnull final ItemStack stack)
	{
		return index > 0 && metConditions(stack);
	}

	@Nonnull
	public ItemStack removeStackFromSlot(final int index)
	{
		return index == 0 ? super.removeStackFromSlot(index) : ItemStack.EMPTY;
	}

	public void setInventorySlotContents(final int index, @Nonnull final ItemStack stack)
	{
		if (index == 0)
			super.setInventorySlotContents(index, stack);
		else if (compressorRecipeField.matches(stack))
			compressorRecipeField.addProgress(stack.getCount());
	}

	@Override
	public boolean canExtractItem(final int index, @Nonnull final ItemStack stack, @Nonnull final EnumFacing direction)
	{
		return index == 0;
	}

	@Nonnull
	@Override
	public String getDefaultName()
	{
		return "container.infinity_compressor.name";
	}

	@Override
	public int getSizeInventory()
	{
		return 243;
	}

	static {
		for (int i = 0; i < slotsForFace.length; i++)
			slotsForFace[i] = i;
	}
}