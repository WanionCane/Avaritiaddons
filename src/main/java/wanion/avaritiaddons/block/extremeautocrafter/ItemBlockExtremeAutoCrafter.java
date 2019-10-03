package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;

public class ItemBlockExtremeAutoCrafter extends ItemBlock
{
	public static final ItemBlockExtremeAutoCrafter INSTANCE = new ItemBlockExtremeAutoCrafter();

	public ItemBlockExtremeAutoCrafter()
	{
		super(BlockExtremeAutoCrafter.INSTANCE);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "extreme_auto_crafter"));
		setHasSubtypes(true);
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(final ItemStack itemStack)
	{
		return "container.extreme_auto_crafter";
	}
}