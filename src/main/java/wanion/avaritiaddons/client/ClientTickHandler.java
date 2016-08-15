package wanion.avaritiaddons.client;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.client.animation.Animation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class ClientTickHandler
{
	public static final ClientTickHandler instance = new ClientTickHandler();

	private final List<Animation> registeredAnimations = new ArrayList<>();

	private ClientTickHandler() {}

	public void registerAnimation(@Nonnull final Animation animation)
	{
		registeredAnimations.add(animation);
	}

	@SubscribeEvent
	public void tickEvent(final TickEvent.ClientTickEvent event)
	{
		registeredAnimations.forEach(Animation::updateAnimation);
	}
}