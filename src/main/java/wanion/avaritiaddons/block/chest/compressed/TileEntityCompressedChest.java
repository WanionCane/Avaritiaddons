package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.proxy.ClientProxy;

import javax.annotation.Nonnull;

public class TileEntityCompressedChest extends TileEntityAvaritiaddonsChest
{
	public TileEntityCompressedChest()
	{
		addCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new InvWrapper(this));
	}

	@Nonnull
	@Override
	public String getDefaultName()
	{
		return "container.compressed_chest.name";
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ResourceLocation getTexture()
	{
		return ClientProxy.COMPRESSED_CHEST_TEXTURE;
	}
}