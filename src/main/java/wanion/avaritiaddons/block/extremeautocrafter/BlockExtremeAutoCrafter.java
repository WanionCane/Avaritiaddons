package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public final class BlockExtremeAutoCrafter extends BlockContainer
{
	public static final BlockExtremeAutoCrafter INSTANCE = new BlockExtremeAutoCrafter();

	private BlockExtremeAutoCrafter()
	{
		super(Material.WOOD);
		setHardness(2.5F).setCreativeTab(Avaritiaddons.creativeTabs);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "extreme_auto_crafter"));
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull final World world, final int metadata)
	{
		return new TileEntityExtremeAutoCrafter();
	}

	@Nonnull
	@Override
	public Item getItemDropped(final IBlockState blockState, final Random random, final int fortune)
	{
		return Items.AIR;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos blockPos, final IBlockState state, final EntityPlayer entityPlayer, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ)
	{
		if (world != null) {
			final TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity instanceof TileEntityExtremeAutoCrafter)
				FMLNetworkHandler.openGui(entityPlayer, Avaritiaddons.instance, Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else
				return false;
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos blockPos, final IBlockState blockState, final EntityLivingBase entityLivingBase, final ItemStack itemStack)
	{
		if (world == null)
			return;
		final TileEntity tileEntity = world.getTileEntity(blockPos);
		if (tileEntity instanceof TileEntityExtremeAutoCrafter && itemStack.hasTagCompound()) {
			((TileEntityExtremeAutoCrafter) tileEntity).readCustomNBT(itemStack.getTagCompound());
			((TileEntityExtremeAutoCrafter) tileEntity).recipeShapeChanged();
		}
	}

	@Nonnull
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
	{
		return SoundType.WOOD;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState blockState)
	{
		if (world == null)
			return;
		final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter = (TileEntityExtremeAutoCrafter) world.getTileEntity(blockPos);
		if (tileEntityExtremeAutoCrafter != null) {
			final ItemStack droppedStack = new ItemStack(this, 1, getMetaFromState(blockState));
			final NBTTagCompound nbtTagCompound = tileEntityExtremeAutoCrafter.writeCustomNBT(new NBTTagCompound());
			if (nbtTagCompound.getTagList("Contents", 10).tagCount() > 0)
				droppedStack.setTagCompound(nbtTagCompound);
			world.spawnEntity(new EntityItem(world, blockPos.getX() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getY() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getZ() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, droppedStack));
		}
		super.breakBlock(world, blockPos, blockState);
	}
}