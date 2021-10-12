package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.InventoryPlayer;
import wanion.avaritiaddons.block.chest.GuiAvaritiaddonsChest;
import wanion.lib.common.WContainer;

import javax.annotation.Nonnull;

public final class GuiCompressedChest extends GuiAvaritiaddonsChest<TileEntityCompressedChest>
{
	public GuiCompressedChest(@Nonnull WContainer<TileEntityCompressedChest> wContainer)
	{
		super(wContainer);
	}
}