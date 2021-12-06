package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.common.WTileEntity;

import javax.annotation.Nonnull;

public abstract class TileEntityAvaritiaddonsChest extends WTileEntity implements ITickable
{
	/**
	 * The current angle of the lid (between 0 and 1)
	 */
	public float lidAngle;
	/**
	 * The angle of the lid last tick
	 */
	public float prevLidAngle;
	/**
	 * The number of players currently using this chest
	 */
	public int numPlayersUsing;
	/**
	 * Server sync counter (once per 20 ticks)
	 */
	private int ticksSinceSync;

	@SideOnly(Side.CLIENT)
	protected abstract ResourceLocation getTexture();

	@Override
	public final void update()
	{
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		++this.ticksSinceSync;
		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + x + y + z) % 200 == 0) {
			this.numPlayersUsing = 0;
			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((float) x - 5.0F, (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F))))
				if (entityplayer.openContainer instanceof ContainerAvaritiaddonsChest && ((ContainerAvaritiaddonsChest<?>) entityplayer.openContainer).getTile() == this)
					++this.numPlayersUsing;
		}
		this.prevLidAngle = this.lidAngle;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
			this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f2 = this.lidAngle;
			if (this.numPlayersUsing > 0)
				this.lidAngle += 0.1F;
			else
				this.lidAngle -= 0.1F;
			if (this.lidAngle > 1.0F)
				this.lidAngle = 1.0F;
			if (this.lidAngle < 0.5F && f2 >= 0.5F)
				this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			if (this.lidAngle < 0.0F)
				this.lidAngle = 0.0F;
		}
	}

	@Override
	public boolean canRenderBreaking()
	{
		return true;
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

	@Override
	public boolean shouldRefresh(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState oldState, @Nonnull final IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0)
				this.numPlayersUsing = 0;
			++this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		if (!player.isSpectator() && this.getBlockType() instanceof BlockAvaritiaddonsChest) {
			--this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}
}