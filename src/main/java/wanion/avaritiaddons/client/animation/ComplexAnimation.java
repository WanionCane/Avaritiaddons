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
public class ComplexAnimation extends Animation
{
	private final int[] animationStages;
	private final int lastStageIndex;
	private int currentStage = 0;

	public ComplexAnimation(@Nonnull final ResourceLocation[] frames, final int[] animationStages)
	{
		super(frames);
		lastStageIndex = (this.animationStages = animationStages).length - 1;
	}

	@Override
	public void updateAnimation()
	{
		if (++currentStage > lastStageIndex)
			currentStage = 0;
		currentFrame = animationStages[currentStage];
	}
}