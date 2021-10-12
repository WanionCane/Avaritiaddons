package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockAvaritiaddonsChest extends ItemBlock
{
	public static final ItemBlockAvaritiaddonsChest INSTANCE = new ItemBlockAvaritiaddonsChest();

	public ItemBlockAvaritiaddonsChest()
	{
		super(BlockAvaritiaddonsChest.INSTANCE);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "avaritiaddons_chest"));
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage << 2;
	}

	@Override
	public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> items)
	{
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(@Nonnull final ItemStack stack)
	{
		return "container." + (stack.getItemDamage() == 0 ? "compressed_chest" : "infinity_chest");
	}

	@Override
	public Entity createEntity(@Nonnull final World world, @Nonnull final Entity entity, @Nonnull final ItemStack itemStack)
	{
		return !itemStack.isEmpty() && itemStack.getItemDamage() == 1 ? new EntityImmortalItem(world, entity, itemStack) : null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull final ItemStack stack, @Nullable final World worldIn, @Nonnull final List<String> tooltip, @Nonnull final ITooltipFlag flagIn)
	{
		if (!stack.isEmpty() && stack.getItemDamage() == 1)
			tooltip.add(TextFormatting.RED + "WIP");
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(@Nonnull final ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItemDamage() == 1 ? EnumRarity.EPIC : EnumRarity.COMMON;
	}
}