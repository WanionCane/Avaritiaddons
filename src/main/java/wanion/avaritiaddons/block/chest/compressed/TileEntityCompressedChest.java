package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.client.ClientConstants;

import javax.annotation.Nonnull;

public final class TileEntityCompressedChest extends TileEntityAvaritiaddonsChest
{
	public TileEntityCompressedChest()
	{
		super(64);
	}

	@Override
	@Nonnull
	public String getDefaultInventoryName()
	{
		return I18n.format("container.CompressedChest");
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nonnull
	protected ResourceLocation getTexture()
	{
		return ClientConstants.COMPRESSED_CHEST_TEXTURE;
	}
}