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
import wanion.avaritiaddons.block.infinitycompressor.GuiInfinityCompressor;
import wanion.avaritiaddons.block.infinitycompressor.ItemBlockInfinityCompressor;
import wanion.avaritiaddons.compat.jei.compressor.InfinityCompressorTransferHandler;
import wanion.avaritiaddons.compat.jei.extreme.AutoExtremeCrafterGhostHandler;
import wanion.avaritiaddons.compat.jei.extreme.AutoExtremeCrafterTransferHandler;

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
        // Infinity Compressor
        modRegistry.addRecipeCatalyst(new ItemStack(ItemBlockInfinityCompressor.INSTANCE, 1, 0), AvaritiaJEIPlugin.NEUTRONIUM_COMPRESSOR);
        modRegistry.addRecipeClickArea(GuiInfinityCompressor.class, 151, 7, 18, 18, AvaritiaJEIPlugin.NEUTRONIUM_COMPRESSOR);
        modRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(new InfinityCompressorTransferHandler(), AvaritiaJEIPlugin.NEUTRONIUM_COMPRESSOR);

    }
}