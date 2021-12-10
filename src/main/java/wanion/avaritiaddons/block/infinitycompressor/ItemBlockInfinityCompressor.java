package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockInfinityCompressor extends ItemBlock
{
	public static final ItemBlockInfinityCompressor INSTANCE = new ItemBlockInfinityCompressor();

	public ItemBlockInfinityCompressor()
	{
		super(BlockInfinityCompressor.INSTANCE);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "infinity_compressor"));
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(@Nonnull final ItemStack stack)
	{
		return "container.infinity_compressor";
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

	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull final ItemStack stack, @Nullable final World worldIn, @Nonnull final List<String> tooltip, @Nonnull final ITooltipFlag flagIn)
	{
		final NBTTagCompound stackCompound = stack.hasTagCompound() ? stack.getTagCompound() : null;
		if (stackCompound != null) {
			final NBTTagList fieldList = stackCompound.getTagList("field", 10);
			for (int i = 0; i < fieldList.tagCount(); i++) {
				final NBTTagCompound fieldNBT = fieldList.getCompoundTagAt(i);
				if (fieldNBT.getString("fieldName").equals("compressor.recipe.field")) {
					final String resourceString = fieldNBT.getString("compressor.recipe");
					final ResourceLocation resourceLocation = !resourceString.isEmpty() ? new ResourceLocation(resourceString) : null;
					final ICompressorRecipe compressorRecipe = resourceLocation != null ? AvaritiaRecipeManager.COMPRESSOR_RECIPES.get(resourceLocation) : null;
					final int progress = fieldNBT.getInteger("compressor.recipe.progress");
					if (compressorRecipe != null) {
						final ItemStack outputStack = compressorRecipe.getResult();
						tooltip.add(TextFormatting.GOLD + I18n.format("avaritiaddons.compressor.making"));
						tooltip.add(outputStack.getRarity().rarityColor + compressorRecipe.getResult().getDisplayName());
						tooltip.add(progress + " / " + compressorRecipe.getCost());
					}
					break;
				}
			}
		}
	}

	@Override
	@Nonnull
	public EnumRarity getRarity(@Nonnull final ItemStack stack)
	{
		return ModItems.COSMIC_RARITY;
	}

	@Override
	public int getItemBurnTime(@Nonnull final ItemStack itemStack)
	{
		return 0;
	}
}