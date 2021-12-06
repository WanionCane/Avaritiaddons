package wanion.avaritiaddons.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import wanion.lib.client.gui.ItemBoxElement;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.interaction.WMouseInteraction;
import wanion.lib.network.ClearShapeMessage;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

public class RecipeResultItemBox extends ItemBoxElement
{
	public RecipeResultItemBox(@Nonnull final Supplier<ItemStack> stackSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(stackSupplier, wGuiContainer, x, y);
	}

	@Override
	public boolean canInteractWith(@Nonnull final WInteraction wInteraction)
	{
		return wInteraction.isHovering(this);
	}

	public void draw(@Nonnull WInteraction wInteraction)
	{
		if (stackSupplier.get().isEmpty())
			getFontRenderer().drawString("?", getUsableX() + 4, getY() + 4, 0x4040404);
	}

	@Override
	public void interaction(@Nonnull final WMouseInteraction wMouseInteraction)
	{
		if (isShiftKeyDown())
			ClearShapeMessage.sendToServer(wGuiContainer.getContainer());
	}
}