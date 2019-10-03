package wanion.avaritiaddons.compat.jei;

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
import morph.avaritia.compat.jei.extreme.ExtremeRecipeWrapper;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.extremeautocrafter.ContainerExtremeAutoCrafter;
import wanion.avaritiaddons.network.ExtremeAutoCrafterJeiTransferMessage;
import wanion.lib.common.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class AutoExtremeCrafterTransferHandler implements IRecipeTransferHandler<ContainerExtremeAutoCrafter>
{
	@Nullable
	@Override
	public final IRecipeTransferError transferRecipe(@Nonnull final ContainerExtremeAutoCrafter container, @Nonnull final IRecipeLayout recipeLayout, @Nonnull final EntityPlayer player, final boolean maxTransfer, final boolean doTransfer)
	{
		if (!doTransfer)
			return null;
		final IRecipeWrapper recipeWrapper = Util.getField(RecipeLayout.class, "recipeWrapper", recipeLayout, IRecipeWrapper.class);
		if (!(recipeWrapper instanceof ExtremeRecipeWrapper))
			return null;
		final IExtremeRecipe extremeRecipe = Util.getField(ExtremeRecipeWrapper.class, "recipe", recipeWrapper, IExtremeRecipe.class);
		for (Map.Entry<ResourceLocation, IExtremeRecipe> extremeEntry : AvaritiaRecipeManager.EXTREME_RECIPES.entrySet()) {
			if (extremeEntry.getValue() == extremeRecipe) {
				Avaritiaddons.networkWrapper.sendToServer(new ExtremeAutoCrafterJeiTransferMessage(container.windowId, extremeEntry.getKey()));
				break;
			}
		}
		return null;
	}

	@Nonnull
	@Override
	public Class<ContainerExtremeAutoCrafter> getContainerClass()
	{
		return ContainerExtremeAutoCrafter.class;
	}
}