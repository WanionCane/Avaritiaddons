package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import wanion.avaritiaddons.block.RecipeResultItemBox;
import wanion.avaritiaddons.block.RecipeResultItemElement;
import wanion.avaritiaddons.block.extremeautocrafter.GuiExtremeAutoCrafter;
import wanion.lib.client.gui.*;
import wanion.lib.client.gui.field.TextFieldWElement;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.WContainer;
import wanion.lib.common.control.energy.EnergyControl;
import wanion.lib.common.control.redstone.RedstoneControl;
import wanion.lib.common.control.redstone.RedstoneControlWButton;
import wanion.lib.common.field.text.TextField;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static wanion.avaritiaddons.Reference.MOD_ID;

public class GuiInfinityCompressor extends WGuiContainer<TileEntityInfinityCompressor>
{
	private static final ResourceLocation guiTexture = new ResourceLocation(MOD_ID, "textures/gui/infinity_compressor.png");
	private static final boolean JEI_PRESENT = Loader.isModLoaded("jei");

	public GuiInfinityCompressor(@Nonnull final WContainer<TileEntityInfinityCompressor> wContainer)
	{
		super(wContainer, guiTexture);
		xSize = 176;
		ySize = 166;
		//addElement(new TextElement(() -> getTile().cachedRecipe.getProgress(), this, getGuiLeft() + (getXSize() / 2), getGuiTop() + 60, TextElement.TextAnchor.MIDDLE));
		addElement(new RecipeResultItemElement(() -> getTile().cachedRecipe.getCompressorRecipeOutput(), this, getGuiLeft() + (getXSize() / 2) - 8, getGuiTop() + 33).setTooltipSupplier(new SingularityTooltipSupplier()));
		addElement(new RedstoneControlWButton(getControl(RedstoneControl.class), this, getGuiLeft() + getXSize() - 25, getGuiTop() + 29));
		addElement(new LetterElement('S', this, getGuiLeft() + getXSize() - 25, getGuiTop() + 7));
	}

	private final class SingularityTooltipSupplier implements ITooltipSupplier
	{
		@Override
		public List<String> getTooltip(@Nonnull final WInteraction wInteraction, @Nonnull final Supplier<ItemStack> supplier)
		{
			final List<String> tooltip = ITooltipSupplier.DEFAULT_TOOLTIP_SUPPLIER.getTooltip(wInteraction, supplier);
			tooltip.add("");
			tooltip.add(getTile().cachedRecipe.getProgress());
			tooltip.add("");
			tooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.clear.recipe"));
			tooltip.add(TextFormatting.RED + I18n.format("avaritiaddons.clear.note") + " " + TextFormatting.WHITE + I18n.format("avaritiaddons.clear.warning"));
			return tooltip;
		}
	}
}