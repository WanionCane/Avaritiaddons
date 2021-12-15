package wanion.avaritiaddons.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.avaritiaddons.Reference;
import wanion.avaritiaddons.block.chest.ItemBlockAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.RendererAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.avaritiaddons.block.extremeautocrafter.BlockExtremeAutoCrafter;
import wanion.avaritiaddons.block.glass.BlockInfinityGlass;
import wanion.avaritiaddons.block.infinitycompressor.BlockInfinityCompressor;
import wanion.avaritiaddons.block.infinitycompressor.ItemBlockInfinityCompressor;
import wanion.lib.WanionLib;
import wanion.lib.client.animation.ComplexHalfAnimation;
import wanion.lib.common.Util;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy
{
	public static final ResourceLocation COMPRESSED_CHEST_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/compressed_chest.png");
	public static final ComplexHalfAnimation INFINITY_CHEST_ANIMATION;

	static {
		final ResourceLocation[] INFINITY_CHEST_FRAMES = new ResourceLocation[]{
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/0.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/1.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/2.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/3.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/4.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/5.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/6.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/7.png"),
				new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_chest/8.png")
		};
		final int[] INFINITY_CHEST_ANIMATION_STAGES = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 6, 7, 8, 7, 6, 5, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1};
		INFINITY_CHEST_ANIMATION = new ComplexHalfAnimation(INFINITY_CHEST_FRAMES, INFINITY_CHEST_ANIMATION_STAGES);
	}

	@Override
	public void init() {
		WanionLib.getClientTickHandler().registerAnimation(INFINITY_CHEST_ANIMATION);
	}

	public void modelInit()
	{
		// Extreme Auto Crafter
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockExtremeAutoCrafter.INSTANCE), 0, new ModelResourceLocation(Reference.MOD_ID + ":extreme_auto_crafter"));

		// Chests
		ModelLoader.setCustomModelResourceLocation(ItemBlockAvaritiaddonsChest.INSTANCE, 0, new ModelResourceLocation(Reference.MOD_ID + ":avaritiaddons_chest"));
		ModelLoader.setCustomModelResourceLocation(ItemBlockAvaritiaddonsChest.INSTANCE, 1, new ModelResourceLocation(Reference.MOD_ID + ":avaritiaddons_chest"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAvaritiaddonsChest.class, RendererAvaritiaddonsChest.INSTANCE);
		ItemBlockAvaritiaddonsChest.INSTANCE.setTileEntityItemStackRenderer(ItemStackRendererAvaritiaddonsChest.INSTANCE);

		// Infinity Compressor
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockInfinityCompressor.INSTANCE), 0, new ModelResourceLocation(Reference.MOD_ID + ":infinity_compressor", "inventory"));

		// Infinity Glass
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockInfinityGlass.INSTANCE), 0, new ModelResourceLocation(Reference.MOD_ID + ":infinity_glass", "inventory"));
	}

	@Override
	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.side.isClient() ? Minecraft.getMinecraft().player : super.getEntityPlayerFromContext(messageContext);
	}

	@Override
	public IThreadListener getThreadListener()
	{
		return Minecraft.getMinecraft();
	}

	public static final class ItemStackRendererAvaritiaddonsChest extends TileEntityItemStackRenderer
	{
		public static final ItemStackRendererAvaritiaddonsChest INSTANCE = new ItemStackRendererAvaritiaddonsChest();

		private final TileEntityAvaritiaddonsChest compressedChest = new TileEntityCompressedChest();
		private final TileEntityAvaritiaddonsChest infinityChest = new TileEntityInfinityChest();

		private ItemStackRendererAvaritiaddonsChest()
		{
			Util.setField(TileEntityItemStackRenderer.class, "chestBasic", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "chestTrap", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "enderChest", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "banner", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "bed", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "skull", this, null);
			Util.setField(TileEntityItemStackRenderer.class, "modelShield", this, null);
		}

		@Override
		public void renderByItem(@Nonnull final ItemStack itemStack)
		{
			renderByItem(itemStack, 1.0F);
		}

		@Override
		public void renderByItem(@Nonnull final ItemStack itemStack, float partialTicks)
		{
			TileEntityRendererDispatcher.instance.render(itemStack.getItemDamage() == 0 ? compressedChest : infinityChest, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
		}
	}
}