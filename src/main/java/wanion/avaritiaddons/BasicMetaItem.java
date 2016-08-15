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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class BasicMetaItem
{
	public static final FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();

	private BasicMetaItem() {}

	public static int get(final ItemStack itemStack)
	{
		Item item;
		if (itemStack == null || (item = itemStack.getItem()) == null)
			return 0;
		final int id = itemRegistry.getId(item);
		return (id > 0) ? id | item.getDamage(itemStack) + 1 << 16 : 0;
	}
}