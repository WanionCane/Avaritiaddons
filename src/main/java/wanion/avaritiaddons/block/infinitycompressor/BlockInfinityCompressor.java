package wanion.avaritiaddons.block.infinitycompressor;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Reference;
import wanion.lib.common.WrenchHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class BlockInfinityCompressor extends BlockContainer
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public static final BlockInfinityCompressor INSTANCE = new BlockInfinityCompressor();

	protected BlockInfinityCompressor()
	{
		super(Material.IRON);
		setHardness(50F);
		setResistance(2000F);
		setHarvestLevel("pickaxe", 3);
		setUnlocalizedName("infinity_compressor");
		setCreativeTab(Avaritiaddons.creativeTabs);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "infinity_compressor"));
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull final World worldIn, final int meta)
	{
		return new TileEntityInfinityCompressor();
	}

	@Override
	public boolean isToolEffective(@Nonnull final String type, @Nonnull final IBlockState state)
	{
		return type.equals("pickaxe");
	}

	@Override
	public int getHarvestLevel(@Nonnull final IBlockState state)
	{
		return 3;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getLightValue(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos)
	{
		return 15;
	}

	@Override
	public void onBlockPlacedBy(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nonnull final EntityLivingBase placer, @Nonnull final ItemStack itemStack)
	{
		if (world == null)
			return;
		world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite()), 3);
		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityInfinityCompressor) {
			final NBTTagCompound stackNBT = itemStack.getTagCompound();
			if (stackNBT != null)
				((TileEntityInfinityCompressor) tileEntity).readCustomNBT(itemStack.getTagCompound());
		}
	}

	@Nonnull
	@Override
	public IBlockState getStateForPlacement(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() & 3;
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}

	@Nonnull
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Nonnull
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Nonnull
	@Override
	public Item getItemDropped(@Nonnull final IBlockState blockState, @Nonnull final Random random, final int fortune)
	{
		return Items.AIR;
	}

	@Override
	public boolean onBlockActivated(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState state, @Nonnull final EntityPlayer entityPlayer, @Nonnull  final EnumHand hand, @Nonnull final EnumFacing facing, final float hitX, final float hitY, final float hitZ)
	{
		if (!world.isRemote && !entityPlayer.isSneaking() && !WrenchHelper.INSTANCE.isWrench(entityPlayer.getHeldItem(hand))) {
			final TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity instanceof TileEntityInfinityCompressor)
				FMLNetworkHandler.openGui(entityPlayer, Avaritiaddons.instance, Avaritiaddons.GUI_ID_INFINITY_COMPRESSOR, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else
				return false;
		}
		return true;
	}

	@Override
	public void breakBlock(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState blockState)
	{
		if (world == null)
			return;
		final TileEntityInfinityCompressor tileEntityInfinityCompressor = (TileEntityInfinityCompressor) world.getTileEntity(blockPos);
		if (tileEntityInfinityCompressor != null) {
			final ItemStack droppedStack = new ItemStack(this, 1, 0);
			final NBTTagCompound nbtTagCompound = tileEntityInfinityCompressor.writeCustomNBT(new NBTTagCompound());
			if (!nbtTagCompound.hasNoTags())
				droppedStack.setTagCompound(nbtTagCompound);
			world.spawnEntity(new EntityImmortalItem(world, blockPos.getX() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getY() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getZ() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, droppedStack));
		}
		super.breakBlock(world, blockPos, blockState);
	}
}