package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import wanion.avaritiaddons.Avaritiaddons;

public class BlockExtremeAutoCrafter extends BlockContainer
{
	public static final BlockExtremeAutoCrafter instance = new BlockExtremeAutoCrafter();
	private static IIcon top;
	private static IIcon sides;
	private static IIcon bottom;

	private BlockExtremeAutoCrafter()
	{
		super(Material.iron);
		setBlockName("ExtremeAutoCrafter").setStepSound(Block.soundTypeGlass).setHardness(50.0F).setResistance(2000.0F).setCreativeTab(Avaritiaddons.creativeTabs).setHarvestLevel("pickaxe", 3);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister)
	{
		top = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterTop");
		sides = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterSide");
		bottom = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterBottom");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata)
	{
		return side == 0 ? bottom : side == 1 ? top : sides;
	}

	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer entityPlayer, final int side, final float hitX, final float hitY, final float hitZ)
	{
		if (!world.isRemote)
			FMLNetworkHandler.openGui(entityPlayer, Avaritiaddons.instance, Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata)
	{
		return new TileEntityExtremeAutoCrafter();
	}
}