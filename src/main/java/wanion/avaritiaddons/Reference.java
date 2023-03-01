package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import java.util.Random;

public class Reference
{
	public static final String MOD_ID = "avaritiaddons";
	public static final String MOD_NAME = "Avaritiaddons";
	public static final String MOD_VERSION = "1.12.2-1.7b";
	public static final String DEPENDENCIES = "required-after:avaritia;required-after:wanionlib@[1.12.2-2.9,)";
	public static final String CLIENT_PROXY = "wanion.avaritiaddons.proxy.ClientProxy";
	public static final String SERVER_PROXY = "wanion.avaritiaddons.proxy.CommonProxy";
	public static final Random RANDOM = new Random();

	private Reference() {}
}