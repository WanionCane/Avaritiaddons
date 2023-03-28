package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public class CompressorRecipeField implements IField<CompressorRecipeField>
{
	private final TileEntityInfinityCompressor tileEntityInfinityCompressor;
	private ICompressorRecipe compressorRecipe;
	private int progress;

	public CompressorRecipeField(@Nonnull final TileEntityInfinityCompressor tileEntityInfinityCompressor)
	{
		this.tileEntityInfinityCompressor = tileEntityInfinityCompressor;
	}

	@Nonnull
	@Override
	public String getFieldName()
	{
		return "compressor.recipe.field";
	}

	public void addProgress(final int progressToAdd) {
		final int compressionCost = compressorRecipe.getCost();
		progress += progressToAdd;
		final int dif = progress - compressionCost;
		progress = MathHelper.clamp(progress, 0, compressionCost);
		if (dif >= 0) {
			progress = dif;
			final ItemStack outputStack = tileEntityInfinityCompressor.getStackInSlot(0);
			final ItemStack recipeResult = compressorRecipe.getResult();
			if (outputStack.isEmpty())
				tileEntityInfinityCompressor.setInventorySlotContents(0, recipeResult.copy());
			else
				outputStack.setCount(outputStack.getCount() + recipeResult.getCount());
			if (progress == 0)
				setCompressorRecipe(null);
		}
	}

	public String getProgress()
	{
		return compressorRecipe != null ? progress + " / " + compressorRecipe.getCost() : I18n.format("avaritiaddons.compressor.no.recipe.set");
	}

	public boolean isNull()
	{
		return compressorRecipe == null;
	}

	public void setCompressorRecipe(final ICompressorRecipe compressorRecipe)
	{
		this.compressorRecipe = compressorRecipe;
		progress = 0;
	}

	public ICompressorRecipe getCompressorRecipe()
	{
		return compressorRecipe;
	}

	public ItemStack getCompressorRecipeOutput()
	{
		return compressorRecipe != null ? compressorRecipe.getResult().copy() : ItemStack.EMPTY;
	}

	public boolean matches(@Nonnull final ItemStack stack)
	{
		return compressorRecipe != null && compressorRecipe.matches(stack);
	}

	@Nonnull
	@Override
	public CompressorRecipeField copy()
	{
		final CompressorRecipeField extremeRecipeField = new CompressorRecipeField(tileEntityInfinityCompressor);
		extremeRecipeField.readNBT(writeNBT());
		return extremeRecipeField;
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		readNBT(nbtTagCompound);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound tag = IField.super.writeNBT();
		final ResourceLocation resourceLocation;
		tag.setString("fieldName", getFieldName());
		if (compressorRecipe != null && (resourceLocation = compressorRecipe.getRegistryName()) != null) {
			tag.setString("compressor.recipe", resourceLocation.toString());
			tag.setInteger("compressor.recipe.progress", progress);
		}
		return tag;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		this.compressorRecipe = nbtTagCompound.hasKey("compressor.recipe") ? AvaritiaRecipeManager.COMPRESSOR_RECIPES.get(new ResourceLocation(nbtTagCompound.getString("compressor.recipe"))) : null;
		this.progress = nbtTagCompound.getInteger("compressor.recipe.progress");
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof CompressorRecipeField && ((CompressorRecipeField) obj).compressorRecipe == this.compressorRecipe && ((CompressorRecipeField) obj).progress == this.progress;
	}
}