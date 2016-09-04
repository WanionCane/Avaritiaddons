package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public final class BasicMetaItem
{
	public static final FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();

	private BasicMetaItem()
	{
	}

	public static int get(final ItemStack itemStack)
	{
		Item item;
		if (itemStack == null || (item = itemStack.getItem()) == null)
			return 0;
		final int id = itemRegistry.getId(item);
		return (id > 0) ? id | item.getDamage(itemStack) + 1 << 16 : 0;
	}

	public static TIntIntMap getKeySizeMap(final int startIndex, final int endIndex, @Nonnull ItemStack[] itemStacks)
	{
		final TIntIntMap keySizeMap = new TIntIntHashMap();
		for (int i = startIndex; i < endIndex; i++) {
			if (itemStacks[i] == null)
				continue;
			final int key = get(itemStacks[i]);
			if (keySizeMap.containsKey(key))
				keySizeMap.put(key, keySizeMap.get(key) + 1);
			else
				keySizeMap.put(key, 1);
		}
		return keySizeMap;
	}

	public static TIntIntMap getSmartKeySizeMap(final int startIndex, final int endIndex, @Nonnull ItemStack[] itemStacks)
	{
		final TIntIntMap smartKeySizeMap = new TIntIntHashMap();
		for (int i = startIndex; i < endIndex; i++) {
			final ItemStack itemStack = itemStacks[i];
			if (itemStack == null)
				continue;
			final int key = get(itemStack);
			if (smartKeySizeMap.containsKey(key))
				smartKeySizeMap.put(key, smartKeySizeMap.get(key) + itemStack.stackSize);
			else
				smartKeySizeMap.put(key, itemStack.stackSize);
		}
		return smartKeySizeMap;
	}
}