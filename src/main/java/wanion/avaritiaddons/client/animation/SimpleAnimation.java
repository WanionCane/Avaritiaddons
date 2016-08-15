package wanion.avaritiaddons.client.animation;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SimpleAnimation extends Animation
{
	public SimpleAnimation(@Nonnull final ResourceLocation[] frames)
	{
		super(frames);
	}

	@Override
	public void updateAnimation()
	{
		if (++currentFrame > lastFrameIndex)
			currentFrame = 0;
	}
}