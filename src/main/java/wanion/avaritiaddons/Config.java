package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import joptsimple.internal.Strings;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;

import static java.io.File.separatorChar;

public final class Config
{
	public static final Config INSTANCE = new Config();

	public final boolean createAutoExtremeTableRecipe, createCompressedChestRecipe, createInfinityChestRecipe, hardCompressedChestRecipe, hardInfinityChestRecipe, hardCompressedChestRecipeUsesNeutroniumCompressor, shouldUseRedstoneSingularity, shouldUseNeutroniumIngot, createInfinityCompressorRecipe, hardInfinityCompressorRecipe;

	// Infinity Glass
	public final boolean createInfinityGlassRecipe;
	public final int infinityGlassLightValue;

	public final int howManyChestsShouldTheCompressorTake;
	public final int powerMultiplier;
	public final int capacityMultiplier;

	private Config()
	{
		final Configuration config = new Configuration(new File("." + separatorChar + "config" + separatorChar + Reference.MOD_ID.replace(" ", Strings.EMPTY) + ".cfg"), Reference.MOD_VERSION);

		createAutoExtremeTableRecipe = config.getBoolean("createAutoExtremeTableRecipe", Configuration.CATEGORY_GENERAL, true, "should be created a recipe for the Auto Extreme Crafting Table?");
		createCompressedChestRecipe = config.getBoolean("createCompressedChestRecipe", Configuration.CATEGORY_GENERAL, true, "should be created a recipe for the Compressed Chest?");
		createInfinityChestRecipe = config.getBoolean("createInfinityChestRecipe", Configuration.CATEGORY_GENERAL, true, "should be created a recipe for the Infinity Chest?");

		hardCompressedChestRecipe = config.getBoolean("hardCompressedChestRecipe", Configuration.CATEGORY_GENERAL, false, "should be created an harder recipe for the Compressed Chest?");
		hardInfinityChestRecipe = config.getBoolean("hardInfinityChestRecipe", Configuration.CATEGORY_GENERAL, false, "should be created an harder recipe for the Infinity Chest?");

		hardCompressedChestRecipeUsesNeutroniumCompressor = config.getBoolean("hardCompressedChestRecipeUsesNeutroniumCompressor", Configuration.CATEGORY_GENERAL, true, "the hard recipe should be made in a Neutronium Compressor?");
		howManyChestsShouldTheCompressorTake = config.getInt("howManyChestsShouldTheCompressorTake", Configuration.CATEGORY_GENERAL, 729, 9, Integer.MAX_VALUE, "how many chests it needs to create the Compressed Chest using the Neutronium Compressor?");

		shouldUseRedstoneSingularity = config.getBoolean("shouldUseRedstoneSingularity", Configuration.CATEGORY_GENERAL, false, "should the recipe use Redstone Singularity instead of Redstone Block?");
		shouldUseNeutroniumIngot = config.getBoolean("shouldUseNeutroniumIngot", Configuration.CATEGORY_GENERAL, false, "should the recipe use Neutronium Ingot instead of Neutronium Nugget?");

		createInfinityCompressorRecipe = config.getBoolean("createInfinityCompressorRecipe", Configuration.CATEGORY_GENERAL, true, "should be created a recipe for the Infinity Compressor?");
		hardInfinityCompressorRecipe = config.getBoolean("hardInfinityCompressorRecipe", Configuration.CATEGORY_GENERAL, false, "should be created an harder recipe for the Infinity Compressor?");

		powerMultiplier = config.getInt("powerMultiplier", Configuration.CATEGORY_GENERAL, 10, 1, Short.MAX_VALUE, "Formula: powerConsumption = craftingSlotAmount * powerMultiplier");
		capacityMultiplier = config.getInt("capacityMultiplier", Configuration.CATEGORY_GENERAL, 100, 1, Short.MAX_VALUE, "Formula: capacity = powerConsumption * capacityMultiplier");

		// Infinity Glass
		createInfinityGlassRecipe = config.getBoolean("createInfinityGlassRecipe", Configuration.CATEGORY_GENERAL, true, "should be created a recipe for the Infinity Glass?");
		infinityGlassLightValue = config.getInt("infinityGlassLightValue", Configuration.CATEGORY_GENERAL, 15, 0, 15, "Light value for Infinity Glass.\nbe aware that it will only glow in the Overworld!");

		if (config.hasChanged())
			config.save();
	}
}