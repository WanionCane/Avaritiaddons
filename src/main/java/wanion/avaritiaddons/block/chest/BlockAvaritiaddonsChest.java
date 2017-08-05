package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import wanion.avaritiaddons.Avaritiaddons;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockAvaritiaddonsChest extends BlockContainer
{
	private final static Random rand = new Random();

	public BlockAvaritiaddonsChest(@Nonnull final Material material)
	{
		super(material);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		setCreativeTab(Avaritiaddons.creativeTabs);
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}

	@Override
	public final void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int metadata)
	{
		final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest = (TileEntityAvaritiaddonsChest) world.getTileEntity(x, y, z);
		if (tileEntityAvaritiaddonsChest != null) {
			final ItemStack droppedStack = new ItemStack(block, 1, 0);
			droppedStack.setTagCompound(tileEntityAvaritiaddonsChest.writeCustomNBT(new NBTTagCompound()));
			world.spawnEntityInWorld(new EntityItem(world, x + rand.nextFloat() * 0.8F + 0.1F, y + rand.nextFloat() * 0.8F + 0.1F, z + rand.nextFloat() * 0.8F + 0.1F, droppedStack));
		}
		super.breakBlock(world, x, y, z, block, metadata);
		world.func_147453_f(x, y, z, block);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	@Override
	public final void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemStack)
	{
		final int side = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest = (TileEntityAvaritiaddonsChest) world.getTileEntity(x, y, z);
		if (tileEntityAvaritiaddonsChest != null) {
			tileEntityAvaritiaddonsChest.setFacingSide(side == 0 ? 180 : side == 1 ? -90 : side == 2 ? 0 : side == 3 ? 90 : 0);
			if (itemStack.stackTagCompound != null)
				tileEntityAvaritiaddonsChest.readCustomNBT(itemStack.stackTagCompound);
		}
	}
}