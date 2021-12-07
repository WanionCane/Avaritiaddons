package wanion.avaritiaddons.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import wanion.lib.client.gui.ITooltipSupplier;
import wanion.lib.client.gui.ItemElement;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.interaction.WMouseInteraction;
import wanion.lib.network.ClearShapeMessage;
import wanion.lib.network.DefineShapeMessage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

public class RecipeResultItemElement extends ItemElement
{
	private final ITooltipSupplier helpTooltipSupplier;
	private final FontRenderer fontRenderer;

	public RecipeResultItemElement(@Nonnull final Supplier<ItemStack> stackSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(stackSupplier, wGuiContainer, x, y);
		this.fontRenderer = getFontRenderer();
		final List<String> helpTooltip = new ArrayList<>();
		helpTooltip.add(TextFormatting.GOLD + I18n.format("wanionlib.how-to-use"));
		helpTooltip.add(I18n.format("avaritiaddons.compressor.usage.1"));
		helpTooltip.add(I18n.format("avaritiaddons.compressor.usage.2"));
		helpTooltip.add("");
		helpTooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.compressor.to.set"));
		helpTooltip.add(I18n.format("avaritiaddons.compressor.to.set.desc.1"));
		helpTooltip.add(I18n.format("avaritiaddons.compressor.to.set.desc.2"));
		helpTooltipSupplier = (interaction, stackSupplier2) -> helpTooltip;
		setForegroundCheck((interaction -> interaction.isHovering(this)));
	}

	public void draw(@Nonnull final WInteraction wInteraction)
	{
		if (stackSupplier.get().isEmpty())
			fontRenderer.drawStringWithShadow("?", getUsableX() + 5, getUsableY() + 5, 0xFFFFFF);
		else
			super.draw(wInteraction);
	}

	@Override

	public List<String> getTooltip(@Nonnull final WInteraction interaction)
	{
		return stackSupplier.get().isEmpty() ? helpTooltipSupplier.getTooltip(interaction) : super.getTooltip(interaction);
	}

	@Override
	public void interaction(@Nonnull final WMouseInteraction wMouseInteraction)
	{
		if (stackSupplier.get().isEmpty()) {
			final ItemStack playerStack = wMouseInteraction.getEntityPlayer().inventory.getItemStack();
			final ICompressorRecipe compressorRecipe = AvaritiaRecipeManager.getCompressorRecipeFromInput(playerStack);
			final ResourceLocation recipeRegistryName = compressorRecipe != null ? compressorRecipe.getRegistryName() : null;
			if (recipeRegistryName != null)
				DefineShapeMessage.sendToServer(wMouseInteraction.getContainer().getContainer(), recipeRegistryName);
		} else if (isShiftKeyDown()) {
			ClearShapeMessage.sendToServer(wGuiContainer.getContainer());
			playPressSound();
		}
	}
}