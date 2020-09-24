package wanion.avaritiaddons.compat.jei;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.minecraft.item.ItemStack;
import wanion.avaritiaddons.block.extremeautocrafter.GuiExtremeAutoCrafter;
import wanion.avaritiaddons.block.extremeautocrafter.ItemBlockExtremeAutoCrafter;

@JEIPlugin
public final class AvaritiaddonsJEIPlugin implements IModPlugin
{
    @Override
    public void register(final IModRegistry modRegistry)
    {
        // Extreme Auto Crafter
        modRegistry.addRecipeCatalyst(new ItemStack(ItemBlockExtremeAutoCrafter.INSTANCE, 1, 0), AvaritiaJEIPlugin.EXTREME_CRAFTING);
        modRegistry.addRecipeClickArea(GuiExtremeAutoCrafter.class, 251, 184, 7, 3, AvaritiaJEIPlugin.EXTREME_CRAFTING);
        modRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(new AutoExtremeCrafterTransferHandler(), AvaritiaJEIPlugin.EXTREME_CRAFTING);
        modRegistry.addGhostIngredientHandler(GuiExtremeAutoCrafter.class, new AutoExtremeCrafterGhostHandler());
    }
}