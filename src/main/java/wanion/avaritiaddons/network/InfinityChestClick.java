package wanion.avaritiaddons.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.chest.infinity.ContainerInfinityChest;

public class InfinityChestClick implements IMessage
{
	private int slot;
	private int mouseButton;
	private int modifier;

	public InfinityChestClick() {}

	public InfinityChestClick(final int slot, final int mouseButton, final int modifier)
	{
		this.slot = slot;
		this.mouseButton = mouseButton;
		this.modifier = modifier;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		slot = ByteBufUtils.readVarShort(buf);
		mouseButton = ByteBufUtils.readVarShort(buf);
		modifier = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeVarShort(buf, slot);
		ByteBufUtils.writeVarShort(buf, mouseButton);
		ByteBufUtils.writeVarShort(buf, modifier);
	}

	public static class Handler implements IMessageHandler<InfinityChestClick, IMessage>
	{
		@Override
		public IMessage onMessage(InfinityChestClick message, MessageContext ctx)
		{
			final EntityPlayer entityPlayer = Avaritiaddons.proxy.getEntityPlayerFromContext(ctx);
			if (entityPlayer.openContainer instanceof ContainerInfinityChest)
				entityPlayer.openContainer.slotClick(message.slot, message.mouseButton, message.modifier, entityPlayer);
			return null;
		}
	}
}