package wanion.avaritiaddons.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.lib.common.IResourceShapedContainer;

public class ExtremeAutoCrafterJeiTransferMessage implements IMessage
{
	private int windowId;
	private ResourceLocation resourceLocation;

	public ExtremeAutoCrafterJeiTransferMessage(final int windowId, final ResourceLocation resourceLocation)
	{
		this.windowId = windowId;
		this.resourceLocation = resourceLocation;
	}

	@SuppressWarnings("unused")
	public ExtremeAutoCrafterJeiTransferMessage() {}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		resourceLocation = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeUTF8String(buf, resourceLocation.toString());
	}

	public static class Handler implements IMessageHandler<ExtremeAutoCrafterJeiTransferMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ExtremeAutoCrafterJeiTransferMessage message, final MessageContext ctx)
		{
			Avaritiaddons.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = Avaritiaddons.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof IResourceShapedContainer && entityPlayer.openContainer.windowId == message.windowId)
					((IResourceShapedContainer) entityPlayer.openContainer).defineShape(message.resourceLocation);
			});
			return null;
		}
	}
}