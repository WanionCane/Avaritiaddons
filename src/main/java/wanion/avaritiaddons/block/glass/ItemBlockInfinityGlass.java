package wanion.avaritiaddons.block.glass;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;

public class ItemBlockInfinityGlass extends ItemBlock
{
	public static final ItemBlockInfinityGlass INSTANCE = new ItemBlockInfinityGlass();

	private ItemBlockInfinityGlass()
	{
		super(BlockInfinityGlass.INSTANCE);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "infinity_glass"));
		setUnlocalizedName("avaritiaddons.infinity.glass");
	}

	@Override
	@Nonnull
	public EnumRarity getRarity(@Nonnull final ItemStack stack)
	{
		return ModItems.COSMIC_RARITY;
	}

	@Override
	public boolean hasCustomEntity(@Nonnull final ItemStack stack)
	{
		return true;
	}

	@Override
	public Entity createEntity(@Nonnull final World world, @Nonnull final Entity entity, @Nonnull final ItemStack itemStack)
	{
		return new EntityImmortalItem(world, entity, itemStack);
	}
}