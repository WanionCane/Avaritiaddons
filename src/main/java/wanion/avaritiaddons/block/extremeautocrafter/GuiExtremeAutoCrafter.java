package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import wanion.avaritiaddons.block.RecipeResultItemBox;
import wanion.lib.client.gui.EnergyElement;
import wanion.lib.client.gui.ITooltipSupplier;
import wanion.lib.client.gui.ItemElement;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.control.energy.EnergyControl;
import wanion.lib.common.control.redstone.RedstoneControl;
import wanion.lib.common.control.redstone.RedstoneControlWButton;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

import static wanion.avaritiaddons.Reference.MOD_ID;

public final class GuiExtremeAutoCrafter extends WGuiContainer<TileEntityExtremeAutoCrafter>
{
	private static final ResourceLocation guiTexture = new ResourceLocation(MOD_ID, "textures/gui/extreme_auto_crafter.png");
	private static final ITooltipSupplier extremeTooltipSupplier = new ExtremeTooltipSupplier();

	public GuiExtremeAutoCrafter(@Nonnull final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter, @Nonnull final InventoryPlayer inventoryPlayer)
	{
		super(new ContainerExtremeAutoCrafter(tileEntityExtremeAutoCrafter, inventoryPlayer), guiTexture);
		final Slot slot = inventorySlots.getSlot(tileEntityExtremeAutoCrafter.full);
		xSize = 343;
		ySize = 276;
		addElement(new RecipeResultItemBox(() -> getTile().getExtremeRecipeField().getExtremeRecipeOutput(), this, slot.xPos - 1, slot.yPos - 29).setTooltipSupplier(extremeTooltipSupplier));
		addElement(new RedstoneControlWButton(getControl(RedstoneControl.class), this, getGuiLeft() + getXSize() - 25, getGuiTop() + getYSize() - 25));
		addElement(new EnergyElement(getControl(EnergyControl.class),this, getGuiLeft() + getXSize() - 25, getGuiTop() + getYSize() - 83));
	}

	private static final class ExtremeTooltipSupplier implements ITooltipSupplier
	{
		@Override
		public List<String> getTooltip(@Nonnull final WInteraction wInteraction, @Nonnull final Supplier<ItemStack> supplier)
		{
			final List<String> tooltip = ItemElement.DEFAULT_ITEMSTACK_TOOLTIP_SUPPLIER.getTooltip(wInteraction, supplier);
			tooltip.add("");
			tooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.clear.recipe"));
			return tooltip;
		}
	}
}