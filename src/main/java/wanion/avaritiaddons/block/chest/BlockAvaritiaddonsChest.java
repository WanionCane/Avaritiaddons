package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.entity.EntityImmortalItem;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Reference;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.lib.common.WrenchHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public final class BlockAvaritiaddonsChest extends BlockContainer
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger CHEST_TYPE = PropertyInteger.create("type", 0, 1);
	private static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

	public static final BlockAvaritiaddonsChest INSTANCE = new BlockAvaritiaddonsChest();

	private BlockAvaritiaddonsChest()
	{
		super(Material.IRON);
		setCreativeTab(Avaritiaddons.creativeTabs);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "avaritiaddons_chest"));
		setUnlocalizedName("avaritiaddons_chest");
		setLightOpacity(0);
		setResistance(2000F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CHEST_TYPE, 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(@Nonnull final IBlockState state)
	{
		return true;
	}

	@Override
	@Nonnull
	public Material getMaterial(@Nonnull final IBlockState state)
	{
		return getMetaFromState(state) >> 2 == 0 ? Material.WOOD : Material.IRON;
	}

	@Override
	@Nonnull
	public SoundType getSoundType(@Nonnull final IBlockState state, @Nonnull final World world, @Nonnull final BlockPos pos, @Nullable Entity entity)
	{
		return getMetaFromState(state) >> 2 == 0 ? SoundType.WOOD : SoundType.METAL;
	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox(@Nonnull final IBlockState state, @Nonnull final IBlockAccess source, @Nonnull final BlockPos pos)
	{
		return NOT_CONNECTED_AABB;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull final World world, final int metadata)
	{
		return metadata >> 2 == 0 ? new TileEntityCompressedChest() : new TileEntityInfinityChest();
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(@Nonnull final IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isFullCube(@Nonnull final IBlockState state)
	{
		return false;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(@Nonnull final IBlockAccess worldIn, @Nonnull final IBlockState state, @Nonnull final BlockPos pos, @Nonnull final EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(@Nonnull final IBlockState state)
	{
		return false;
	}

	@Nonnull
	@Override
	public IBlockState getStateForPlacement(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(CHEST_TYPE, meta >> 2);
	}

	@Override
	public void onBlockPlacedBy(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nonnull final EntityLivingBase placer, @Nonnull final ItemStack itemStack)
	{
		if (world == null)
			return;
		world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite()), 3);
		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityAvaritiaddonsChest) {
			final NBTTagCompound stackNBT = itemStack.getTagCompound();
			if (stackNBT != null)
				((TileEntityAvaritiaddonsChest) tileEntity).readCustomNBT(itemStack.getTagCompound());
		}
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(CHEST_TYPE, meta >> 2);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(FACING).getHorizontalIndex();
		i = i | state.getValue(CHEST_TYPE) << 2;
		return i;
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, CHEST_TYPE);
	}

	@Override
	public int getLightValue(@Nonnull final IBlockState state, @Nonnull final  IBlockAccess world, @Nonnull final BlockPos pos)
	{
		return getMetaFromState(state) >> 2 == 1 ? 15 : 0;
	}

	@Override
	public float getBlockHardness(@Nonnull final IBlockState blockState, @Nonnull final World worldIn, @Nonnull final BlockPos pos)
	{
		return getMetaFromState(blockState) >> 2 == 0 ? 5.0F : 50.0F;
	}

	@Override
	public int getHarvestLevel(@Nonnull final IBlockState state)
	{
		return getMetaFromState(state) >> 2 == 0 ? 2 : 3;
	}

	@Override
	public boolean isToolEffective(@Nonnull final String type, @Nonnull final IBlockState state)
	{
		final int meta = getMetaFromState(state) >> 2;
		return meta == 0 && type.equals("axe") || meta == 1 && type.equals("pickaxe");
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
	public boolean onBlockActivated(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState state, @Nonnull final EntityPlayer entityPlayer, @Nonnull final EnumHand hand, @Nonnull final EnumFacing facing, final float hitX, final float hitY, final float hitZ)
	{
		if (!world.isRemote && !entityPlayer.isSneaking() && !WrenchHelper.INSTANCE.isWrench(entityPlayer.getHeldItem(hand))) {
			final TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity instanceof TileEntityAvaritiaddonsChest)
				FMLNetworkHandler.openGui(entityPlayer, Avaritiaddons.instance, tileEntity instanceof TileEntityCompressedChest ? Avaritiaddons.GUI_ID_COMPRESSED_CHEST : Avaritiaddons.GUI_ID_INFINITY_CHEST, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else
				return false;
		} else if (!world.isRemote && entityPlayer.isSneaking() && WrenchHelper.INSTANCE.isWrench(entityPlayer.getHeldItem(hand)) && world.getTileEntity(blockPos) instanceof TileEntityAvaritiaddonsChest) {
			//breakBlock(world, blockPos, state);
			world.setBlockToAir(blockPos);
		}
		return true;
	}

	@Override
	public int damageDropped(@Nonnull final IBlockState state)
	{
		return getMetaFromState(state) >> 2;
	}

	@Override
	public void breakBlock(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState blockState)
	{
		if (world == null)
			return;
		final TileEntity tileEntity = world.getTileEntity(blockPos);
		if (tileEntity instanceof TileEntityAvaritiaddonsChest) {
			final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest = (TileEntityAvaritiaddonsChest) tileEntity;
			final ItemStack droppedStack = new ItemStack(this, 1, getMetaFromState(blockState) >> 2);
			final NBTTagCompound nbtTagCompound = tileEntityAvaritiaddonsChest.writeCustomNBT(new NBTTagCompound());
			if (tileEntityAvaritiaddonsChest instanceof TileEntityCompressedChest && nbtTagCompound.getTagList("Contents", 10).tagCount() > 0)
				droppedStack.setTagCompound(nbtTagCompound);
			else
				droppedStack.setTagCompound(nbtTagCompound);
			world.spawnEntity(new EntityImmortalItem(world, blockPos.getX() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getY() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getZ() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, droppedStack));
		}
		super.breakBlock(world, blockPos, blockState);
	}
}