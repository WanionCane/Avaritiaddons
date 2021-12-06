package wanion.avaritiaddons.compat.jei.compressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.gui.recipes.RecipeLayout;
import morph.avaritia.compat.jei.compressor.CompressorRecipeWrapper;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wanion.avaritiaddons.block.infinitycompressor.ContainerInfinityCompressor;
import wanion.lib.common.Util;
import wanion.lib.network.DefineShapeMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfinityCompressorTransferHandler implements IRecipeTransferHandler<ContainerInfinityCompressor>
{
	@Nullable
	@Override
	public final IRecipeTransferError transferRecipe(@Nonnull final ContainerInfinityCompressor container, @Nonnull final IRecipeLayout recipeLayout, @Nonnull final EntityPlayer player, final boolean maxTransfer, final boolean doTransfer)
	{
		if (!doTransfer)
			return null;
		final IRecipeWrapper recipeWrapper = Util.getField(RecipeLayout.class, "recipeWrapper", recipeLayout, IRecipeWrapper.class);
		if (!(recipeWrapper instanceof CompressorRecipeWrapper))
			return null;
		final ICompressorRecipe compressionRecipe = Util.getField(CompressorRecipeWrapper.class, "recipe", recipeWrapper, ICompressorRecipe.class);
		final ResourceLocation recipeRegistryName = compressionRecipe != null ? compressionRecipe.getRegistryName() : null;
		ICompressorRecipe recipeFromRegistry;
		if (compressionRecipe != null && recipeRegistryName != null) {
			recipeFromRegistry = AvaritiaRecipeManager.COMPRESSOR_RECIPES.get(recipeRegistryName);
			if (compressionRecipe == recipeFromRegistry)
				DefineShapeMessage.sendToServer(container, recipeRegistryName);
		}
		return null;
	}

	@Nonnull
	@Override
	public Class<ContainerInfinityCompressor> getContainerClass()
	{
		return ContainerInfinityCompressor.class;
	}
}