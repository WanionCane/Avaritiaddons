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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public class ExtremeRecipeField implements IField<ExtremeRecipeField>
{
	private IExtremeRecipe extremeRecipe;

	@Nonnull
	@Override
	public String getFieldName()
	{
		return "extreme.recipe.field";
	}

	public boolean isNull()
	{
		return extremeRecipe == null;
	}

	public void setExtremeRecipe(final IExtremeRecipe extremeRecipe)
	{
		this.extremeRecipe = extremeRecipe;
	}

	public IExtremeRecipe getExtremeRecipe()
	{
		return extremeRecipe;
	}

	public ItemStack getExtremeRecipeOutput()
	{
		return extremeRecipe != null ? extremeRecipe.getRecipeOutput() : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ExtremeRecipeField copy()
	{
		final ExtremeRecipeField extremeRecipeField = new ExtremeRecipeField();
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
		if (extremeRecipe != null && (resourceLocation = extremeRecipe.getRegistryName()) != null)
			tag.setString("extreme.recipe", resourceLocation.toString());
		return tag;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		this.extremeRecipe = nbtTagCompound.hasKey("extreme.recipe") ? AvaritiaRecipeManager.EXTREME_RECIPES.get(new ResourceLocation(nbtTagCompound.getString("extreme.recipe"))) : null;
	}

	@Override
	public boolean equals(final Object obj)
	{
		return (obj instanceof ExtremeRecipeField && ((ExtremeRecipeField) obj).extremeRecipe == this.extremeRecipe) || (obj instanceof ResourceLocation && obj.equals(extremeRecipe.getRegistryName()));
	}
}