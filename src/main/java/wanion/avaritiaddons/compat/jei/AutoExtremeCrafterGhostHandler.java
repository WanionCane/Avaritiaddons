package wanion.avaritiaddons.compat.jei;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import mezz.jei.api.gui.IGhostIngredientHandler;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import wanion.avaritiaddons.block.extremeautocrafter.GuiExtremeAutoCrafter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class AutoExtremeCrafterGhostHandler implements IGhostIngredientHandler<GuiExtremeAutoCrafter>
{
	@Override
	@Nonnull
	public <I> List<Target<I>> getTargets(@Nonnull final GuiExtremeAutoCrafter gui, @Nonnull final I ingredient, final boolean doStart)
	{
		List<Target<I>> targets = new ArrayList<>();
		final int endsIn = gui.inventorySlots.inventorySlots.size() - 38;
		final int startsIn = endsIn / 2;
		if (ingredient instanceof ItemStack) {
			for (int i = startsIn; i < endsIn; i++) {
				final Slot slot = gui.inventorySlots.getSlot(i);
				targets.add(new ExtremeTarget<>(slot.slotNumber, new Rectangle(gui.getGuiLeft() + slot.xPos - 1, gui.getGuiTop() + slot.yPos - 1, 18, 18), gui));
			}
		}
		return targets;
	}

	@Override
	public final void onComplete() {}
}