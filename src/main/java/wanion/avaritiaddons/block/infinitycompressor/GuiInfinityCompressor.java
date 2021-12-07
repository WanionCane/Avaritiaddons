package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import wanion.avaritiaddons.block.RecipeResultItemElement;
import wanion.lib.client.gui.*;
import wanion.lib.client.gui.field.CheckBoxWElement;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.WContainer;
import wanion.lib.common.control.redstone.RedstoneControl;
import wanion.lib.common.control.redstone.RedstoneControlWButton;
import wanion.lib.common.field.CheckBox;

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
		addElement(new RecipeResultItemElement(() -> getTile().compressorRecipeField.getCompressorRecipeOutput(), this, getGuiLeft() + (getXSize() / 2) - 8, getGuiTop() + 33).setTooltipSupplier(new SingularityTooltipSupplier()));
		final LetterElement showRecipesElement = new LetterElement('R', this, getGuiLeft() + getXSize() - 25, getGuiTop() + 7);
		if (!JEI_PRESENT)
			showRecipesElement.setDefaultForegroundCheck().setTooltipSupplier((interaction, stackSupplier) -> Lists.newArrayList(I18n.format("avaritiaddons.no.jei")));
		addElement(showRecipesElement);
		addElement(new RedstoneControlWButton(getControl(RedstoneControl.class), this, getGuiLeft() + getXSize() - 25, getGuiTop() + 29));
		addElement(new CheckBoxWElement((CheckBox) getField("avaritiaddons.compressor.trashcan"), this, guiLeft + getGuiLeft() + getXSize() - 25, getGuiTop() + 51));
	}

	private final class SingularityTooltipSupplier implements ITooltipSupplier
	{
		@Override
		public List<String> getTooltip(@Nonnull final WInteraction wInteraction, @Nonnull final Supplier<ItemStack> supplier)
		{
			final TileEntityInfinityCompressor tile = getTile();
			final CompressorRecipeField compressorRecipeField = tile.compressorRecipeField;
			final ItemStack outputStack = compressorRecipeField.getCompressorRecipeOutput();
			final List<String> tooltip = new ArrayList<>();
			if (compressorRecipeField.isNull() || outputStack.isEmpty())
				return tooltip;
			tooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.compressor.making"));
			tooltip.add(outputStack.getRarity().rarityColor + outputStack.getDisplayName());
			tooltip.add(compressorRecipeField.getProgress());
			tooltip.add("");
			tooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.clear.recipe"));
			tooltip.add(TextFormatting.RED + I18n.format("avaritiaddons.compressor.clear.note") + " " + TextFormatting.WHITE + I18n.format("avaritiaddons.compressor.clear.warning"));
			return tooltip;
		}
	}
}