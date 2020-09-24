package wanion.avaritiaddons.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Config;
import wanion.avaritiaddons.Reference;
import wanion.avaritiaddons.block.extremeautocrafter.*;
import wanion.avaritiaddons.network.ExtremeAutoCrafterGhostTransferMessage;
import wanion.avaritiaddons.network.ExtremeAutoCrafterJeiTransferMessage;

import javax.annotation.Nonnull;

public class CommonProxy implements IGuiHandler
{
	public final void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.INSTANCE.registerGuiHandler(Avaritiaddons.instance, this);
		GameRegistry.registerTileEntity(TileEntityExtremeAutoCrafter.class, new ResourceLocation(Reference.MOD_ID, "extremeautocrafter"));
		int d = 0;
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterJeiTransferMessage.Handler.class, ExtremeAutoCrafterJeiTransferMessage.class, d++, Side.SERVER);
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterJeiTransferMessage.Handler.class, ExtremeAutoCrafterJeiTransferMessage.class, d++, Side.CLIENT);
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterGhostTransferMessage.Handler.class, ExtremeAutoCrafterGhostTransferMessage.class, d++, Side.SERVER);
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterGhostTransferMessage.Handler.class, ExtremeAutoCrafterGhostTransferMessage.class, d, Side.CLIENT);
		final Config config = Config.INSTANCE;
		if (config.createAutoExtremeTableRecipe)
			GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "extremeautocrafter"), null, new ItemStack(ItemBlockExtremeAutoCrafter.INSTANCE, 1), " S ", "NCN", " N ", 'S', config.shouldUseRedstoneSingularity ? Ingredient.fromStacks(ModItems.redstoneSingularity) : "blockRedstone", 'N', config.shouldUseNeutroniumIngot ? "blockCosmicNeutronium" : "nuggetCosmicNeutronium", 'C', Ingredient.fromStacks(new ItemStack(ModBlocks.extremeCraftingTable)));
	}

	public void init() {}

	public void postInit() {}

	@SubscribeEvent
	public void registerItems(final RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemBlockExtremeAutoCrafter.INSTANCE);
	}

	@SubscribeEvent
	public void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockExtremeAutoCrafter.INSTANCE);
	}

	@SubscribeEvent
	public void modelRegistryEvent(final ModelRegistryEvent event)
	{
		modelInit();
	}

	public void modelInit() {}

	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity == null)
			return null;
		switch (ID) {
			case Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER:
				if (tileEntity instanceof TileEntityExtremeAutoCrafter)
					return new ContainerExtremeAutoCrafter((TileEntityExtremeAutoCrafter) tileEntity, player.inventory);
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity == null)
			return null;
		switch (ID) {
			case Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER:
				if (tileEntity instanceof TileEntityExtremeAutoCrafter)
					return new GuiExtremeAutoCrafter((TileEntityExtremeAutoCrafter) tileEntity, player.inventory);
			default:
				return null;
		}
	}

	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.getServerHandler().player;
	}

	public final boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	public final boolean isServer()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	public IThreadListener getThreadListener()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
}