package wanion.avaritiaddons.block.chest.infinity;

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
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.proxy.ClientProxy;

import javax.annotation.Nonnull;

public class TileEntityInfinityChest extends TileEntityAvaritiaddonsChest
{
	public int getSizeInventory()
	{
		return 244;
	}

	@Nonnull
	@Override
	public String getDefaultName()
	{
		return "container.infinity_chest.name";
	}

	public int getInventoryStackLimit()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ResourceLocation getTexture()
	{
		return ClientProxy.INFINITY_CHEST_ANIMATION.getCurrentFrame();
	}
}