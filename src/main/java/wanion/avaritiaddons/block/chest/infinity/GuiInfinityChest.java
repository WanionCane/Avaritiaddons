package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ModContainer;
import wanion.avaritiaddons.block.chest.GuiAvaritiaddonsChest;
import wanion.lib.client.gui.ITooltipSupplier;
import wanion.lib.client.gui.ItemElement;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.Util;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;
import java.util.List;

public final class GuiInfinityChest extends GuiAvaritiaddonsChest<TileEntityInfinityChest>
{
	public GuiInfinityChest(@Nonnull final WContainer<TileEntityInfinityChest> wContainer)
	{
		super(wContainer);
	}

	@Override
	protected void renderToolTip(@Nonnull final ItemStack itemStack, final int x, final int y)
	{
		final Slot slot = getSlotUnderMouse();
		if (slot instanceof InfinitySlot && slot.getHasStack()) {
			final InfinitySlot infinitySlot = (InfinitySlot) slot;
			final ITooltipSupplier infinityTooltipSupplier = (interaction, stackSupplier) -> {
				final ItemStack stack = stackSupplier.get();
				final List<String> tooltip = ItemElement.DEFAULT_ITEMSTACK_TOOLTIP_SUPPLIER.getTooltip(interaction, stackSupplier);
				int line = getLine(tooltip, stack);
				tooltip.add(line, "");
				tooltip.add(++line, TextFormatting.GOLD + I18n.format("wanionlib.amount") + " " + TextFormatting.GRAY + infinitySlot.getInfinityMatching().getFormattedCount());
				return tooltip;
			};
			final WInteraction interaction = getLatestInteraction();
			drawHoveringText(infinityTooltipSupplier.getTooltip(interaction, infinitySlot::getStack), interaction.getMouseX(), interaction.getMouseY());
		} else super.renderToolTip(itemStack, x, y);
	}

	private int getLine(@Nonnull final List<String> tooltip, @Nonnull final ItemStack stack)
	{
		final ModContainer modContainer = Util.getModContainerFromStack(stack);
		final String modName = modContainer != null ? modContainer.getName() : null;
		int i = tooltip.size() - 1;
		if (modName == null)
			return i;
		for (; i > 0; i--)
			if (tooltip.get(i).contains(modName))
				return i;
		return tooltip.size() - 1;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GlStateManager.disableDepth();
		for (int i = 0; i < 243; i++) {
			final InfinitySlot infinitySlot = (InfinitySlot) inventorySlots.getSlot(i);
			final InfinityMatching infinityMatching = infinitySlot.getInfinityMatching();
			if (infinityMatching.getCount() > 1) {
				final String simplifiedCount = infinityMatching.getSimplifiedCount();
				final int countWidth = fontRenderer.getStringWidth(simplifiedCount);
				drawString(fontRenderer, simplifiedCount, infinitySlot.xPos + 17 - countWidth, infinitySlot.yPos + 9, 0xFFFFFF);
			}
		}
		GlStateManager.enableDepth();
	}
}