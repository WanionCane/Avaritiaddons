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
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.NonNullList;
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
import net.minecraftforge.oredict.OreIngredient;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.Config;
import wanion.avaritiaddons.Reference;
import wanion.avaritiaddons.block.chest.BlockAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.ItemBlockAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.compressed.ContainerCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.GuiCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.ContainerInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.GuiInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.avaritiaddons.block.extremeautocrafter.*;
import wanion.avaritiaddons.block.infinitycompressor.*;
import wanion.avaritiaddons.network.ExtremeAutoCrafterGhostTransferMessage;

import javax.annotation.Nonnull;

public class CommonProxy implements IGuiHandler
{
	public final void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.INSTANCE.registerGuiHandler(Avaritiaddons.instance, this);
		GameRegistry.registerTileEntity(TileEntityExtremeAutoCrafter.class, new ResourceLocation(Reference.MOD_ID, "extremeautocrafter"));
		GameRegistry.registerTileEntity(TileEntityCompressedChest.class, new ResourceLocation(Reference.MOD_ID, "compressed_chest"));
		GameRegistry.registerTileEntity(TileEntityInfinityChest.class, new ResourceLocation(Reference.MOD_ID, "infinity_chest"));
		GameRegistry.registerTileEntity(TileEntityInfinityCompressor.class, new ResourceLocation(Reference.MOD_ID, "infinity_compressor"));
		int d = 0;
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterGhostTransferMessage.Handler.class, ExtremeAutoCrafterGhostTransferMessage.class, d++, Side.SERVER);
		Avaritiaddons.networkWrapper.registerMessage(ExtremeAutoCrafterGhostTransferMessage.Handler.class, ExtremeAutoCrafterGhostTransferMessage.class, d, Side.CLIENT);
		final Config config = Config.INSTANCE;
		if (config.createAutoExtremeTableRecipe)
			GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "extremeautocrafter"), null, new ItemStack(ItemBlockExtremeAutoCrafter.INSTANCE, 1), " S ", "NCN", " N ", 'S', config.shouldUseRedstoneSingularity ? Ingredient.fromStacks(ModItems.redstoneSingularity) : "blockRedstone", 'N', config.shouldUseNeutroniumIngot ? "blockCosmicNeutronium" : "nuggetCosmicNeutronium", 'C', Ingredient.fromStacks(new ItemStack(ModBlocks.extremeCraftingTable)));
		if (config.createCompressedChestRecipe) {
			if (config.hardCompressedChestRecipe) {
				if (config.hardCompressedChestRecipeUsesNeutroniumCompressor)
					AvaritiaRecipeManager.COMPRESSOR_RECIPES.put(new ResourceLocation(Reference.MOD_ID, "compressed_chest"), new CompressorRecipe(new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 0), config.howManyChestsShouldTheCompressorTake, true, NonNullList.withSize(1, Ingredient.fromItem(Item.getItemFromBlock(Blocks.CHEST)))));
				else
					AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID, "compressed_chest"), new ExtremeShapelessRecipe(NonNullList.withSize(81, new OreIngredient("chest")), new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 0)));
			} else
				GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "compressed_chest"), null, new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 0), "CCC", "CCC", "CCC", 'C', Ingredient.fromItem(Item.getItemFromBlock(Blocks.CHEST)));
		}
		if (config.createInfinityChestRecipe) {
			if (config.hardInfinityChestRecipe)
				GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "infinity_chest"), null, new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 1), "INI", "BCB", "IBI", 'I', "ingotInfinity", 'N', "blockCosmicNeutronium", 'B', "blockInfinity", 'C', Ingredient.fromStacks(new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 0)));
			else
				GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "infinity_chest"), null, new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 1), "TNT", "ICI", "TIT", 'T', Ingredient.fromStacks(ModItems.infinity_catalyst), 'N', "blockCosmicNeutronium", 'I', "ingotInfinity", 'C', Ingredient.fromStacks(new ItemStack(ItemBlockAvaritiaddonsChest.INSTANCE, 1, 0)));
		}
		if (config.createInfinityCompressorRecipe) {
			if (config.hardInfinityCompressorRecipe)
				GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "infinity_compressor"), null, new ItemStack(ItemBlockInfinityCompressor.INSTANCE, 1), "III", "ICI", "III", 'I', "ingotInfinity", 'C', Ingredient.fromStacks(new ItemStack(ModBlocks.neutronium_compressor, 1)));
			else
				GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "infinity_compressor"), null, new ItemStack(ItemBlockInfinityCompressor.INSTANCE, 1), "TTT", "TCT", "TTT", 'T', Ingredient.fromStacks(ModItems.infinity_catalyst), 'C', Ingredient.fromStacks(new ItemStack(ModBlocks.neutronium_compressor, 1)));
		}
	}

	public void init() {}

	public void postInit() {}

	@SubscribeEvent
	public void registerItems(final RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemBlockExtremeAutoCrafter.INSTANCE, ItemBlockAvaritiaddonsChest.INSTANCE, ItemBlockInfinityCompressor.INSTANCE);
	}

	@SubscribeEvent
	public void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockExtremeAutoCrafter.INSTANCE, BlockAvaritiaddonsChest.INSTANCE, BlockInfinityCompressor.INSTANCE);
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
			case Avaritiaddons.GUI_ID_COMPRESSED_CHEST:
				if (tileEntity instanceof TileEntityCompressedChest)
					return new ContainerCompressedChest((TileEntityCompressedChest) tileEntity, player.inventory);
			case Avaritiaddons.GUI_ID_INFINITY_CHEST:
				if (tileEntity instanceof TileEntityInfinityChest)
					return new ContainerInfinityChest((TileEntityInfinityChest) tileEntity, player.inventory);
			case Avaritiaddons.GUI_ID_INFINITY_COMPRESSOR:
				if (tileEntity instanceof TileEntityInfinityCompressor)
					return new ContainerInfinityCompressor((TileEntityInfinityCompressor) tileEntity, player.inventory);
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
			case Avaritiaddons.GUI_ID_COMPRESSED_CHEST:
				if (tileEntity instanceof TileEntityCompressedChest)
					return new GuiCompressedChest(new ContainerCompressedChest((TileEntityCompressedChest) tileEntity, player.inventory));
			case Avaritiaddons.GUI_ID_INFINITY_CHEST:
				if (tileEntity instanceof TileEntityInfinityChest)
					return new GuiInfinityChest(new ContainerInfinityChest((TileEntityInfinityChest) tileEntity, player.inventory));
			case Avaritiaddons.GUI_ID_INFINITY_COMPRESSOR:
				if (tileEntity instanceof TileEntityInfinityCompressor)
					return new GuiInfinityCompressor(new ContainerInfinityCompressor((TileEntityInfinityCompressor) tileEntity, player.inventory));
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