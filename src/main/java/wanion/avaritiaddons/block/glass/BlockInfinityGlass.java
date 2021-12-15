package wanion.avaritiaddons.block.glass;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Reference;

import javax.annotation.Nonnull;

public class BlockInfinityGlass extends Block
{
	public static final PropertyBool xEven = PropertyBool.create("xeven");
	public static final PropertyBool yEven = PropertyBool.create("yeven");
	public static final PropertyBool zEven = PropertyBool.create("zeven");

	public static final BlockInfinityGlass INSTANCE = new BlockInfinityGlass();

	private BlockInfinityGlass()
	{
		super(Material.GLASS);
		setHardness(50F);
		setResistance(2000F);
		setHarvestLevel("pickaxe", 3);
		setSoundType(SoundType.GLASS);
		setCreativeTab(Avaritiaddons.creativeTabs);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "infinity_glass"));
		setUnlocalizedName("avaritiaddons.infinity.glass");
		setDefaultState(blockState.getBaseState().withProperty(xEven, true).withProperty(yEven, true).withProperty(zEven, true));
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, xEven, yEven, zEven);
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		return this.getDefaultState().withProperty(xEven, (meta & 1) == 0).withProperty(yEven, (meta >> 1 & 1) == 0).withProperty(zEven, (meta >> 2 & 1) == 0);
	}

	@Override
	public int getMetaFromState(@Nonnull final IBlockState state)
	{
		int meta = 0;
		meta |= state.getValue(xEven) ? 0 : 1;
		meta |= ((state.getValue(yEven) ? 0 : 1) << 1);
		meta |= ((state.getValue(zEven) ? 0 : 1) << 2);
		return meta;
	}

	@Nonnull
	@Override
	public IBlockState getStateForPlacement(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, @Nonnull final EntityLivingBase placer)
	{
		return getDefaultState().withProperty(xEven, (pos.getX() & 1) == 0).withProperty(yEven, (pos.getY() & 1) == 0).withProperty(zEven, (pos.getZ() & 1) == 0);
	}

	@Override
	public boolean canRenderInLayer(@Nonnull final IBlockState state, @Nonnull final BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.TRANSLUCENT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side)
	{
		return world.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(state, world, pos, side);
	}

	@Override
	public boolean isOpaqueCube(@Nonnull final IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(@Nonnull final IBlockState state)
	{
		return false;
	}

	@Override
	public int getLightValue(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos)
	{
		return 15;
	}

}