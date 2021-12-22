package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.ResourceLocation;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;

import static wanion.avaritiaddons.Reference.MOD_ID;

public abstract class GuiAvaritiaddonsChest <C extends TileEntityAvaritiaddonsChest> extends WGuiContainer<C>
{
	private static final ResourceLocation guiTexture = new ResourceLocation(MOD_ID, "textures/gui/avaritiaddons_chest.png");

	public GuiAvaritiaddonsChest(@Nonnull final WContainer<C> wContainer)
	{
		super(wContainer, guiTexture, 500, 276);
	}
}