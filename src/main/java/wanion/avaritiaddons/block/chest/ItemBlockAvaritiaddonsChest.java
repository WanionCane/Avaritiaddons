package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
		if (!this.isInCreativeTab(tab))
			return;
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(@Nonnull final ItemStack stack)
	{
		return "container." + (stack.getItemDamage() == 0 ? "compressed_chest" : "infinity_chest");
	}

	@Override
	public boolean hasCustomEntity(@Nonnull final ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItemDamage() == 1;
	}

	@Override
	public Entity createEntity(@Nonnull final World world, @Nonnull final Entity entity, @Nonnull final ItemStack itemStack)
	{
		return new EntityImmortalItem(world, entity, itemStack);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull final ItemStack stack, @Nullable final World worldIn, @Nonnull final List<String> tooltip, @Nonnull final ITooltipFlag flagIn)
	{
		if (!stack.isEmpty() && stack.getItemDamage() == 0) {
			final NBTTagCompound tag = stack.getTagCompound();
			final NBTTagList list = tag != null ? tag.getTagList("Contents", 10) : null;
			if (list == null || list.hasNoTags())
				tooltip.add(I18n.format("avaritiaddons.tooltip.empty"));
			else tooltip.add(I18n.format("avaritiaddons.tooltip.filling_range", list.tagCount(), 243));
		}
		else if (!stack.isEmpty() && stack.getItemDamage() == 1)
			tooltip.add(TextFormatting.RED + "WIP");
	}

	@Override
	@Nonnull
	public EnumRarity getRarity(@Nonnull final ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItemDamage() == 1 ? ModItems.COSMIC_RARITY : EnumRarity.UNCOMMON;
	}

	@Override
	public int getItemBurnTime(@Nonnull final ItemStack itemStack)
	{
		return 0;
	}
}