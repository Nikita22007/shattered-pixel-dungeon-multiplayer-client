/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.HashMap;

public class ChaoticCenser extends Trinket {

	{
		image = ItemSpriteSheet.CHAOTIC_CENSER;
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc", averageTurnsUntilGas(buffedLvl()));
		} else {
			return Messages.get(this, "stats_desc", averageTurnsUntilGas(0));
		}
	}

	public static int averageTurnsUntilGas(){
		return averageTurnsUntilGas(-1);
	}

	public static int averageTurnsUntilGas(int level){
		if (level <= -1){
			return -1;
		} else {
			return 300 / (level + 1);
		}
	}

	private static final float[][] GAS_CAT_CHANCES = new float[4][3];
	static {
		GAS_CAT_CHANCES[0] = new float[]{70, 25, 5};
		GAS_CAT_CHANCES[1] = new float[]{60, 30, 10};
		GAS_CAT_CHANCES[2] = new float[]{50, 35, 15};
		GAS_CAT_CHANCES[3] = new float[]{40, 40, 20};
	}

	private static final HashMap<Class<? extends Blob>, Float> COMMON_GASSES = new HashMap<>();
	static {
		COMMON_GASSES.put(ToxicGas.class, 300f);
		COMMON_GASSES.put(ConfusionGas.class, 300f);
		COMMON_GASSES.put(Regrowth.class, 200f);
	}

	private static final HashMap<Class<? extends Blob>, Float> UNCOMMON_GASSES = new HashMap<>();
	static {
		UNCOMMON_GASSES.put(StormCloud.class, 300f);
		UNCOMMON_GASSES.put(SmokeScreen.class, 300f);
		UNCOMMON_GASSES.put(StenchGas.class, 200f);
	}

	private static final HashMap<Class<? extends Blob>, Float> RARE_GASSES = new HashMap<>();
	static {
		RARE_GASSES.put(Inferno.class, 300f);
		RARE_GASSES.put(Blizzard.class, 300f);
		RARE_GASSES.put(CorrosiveGas.class, 200f);
	}

	private static final HashMap<Class<? extends Blob>, Integer> MISSILE_VFX = new HashMap<>();
	static {
		MISSILE_VFX.put(ToxicGas.class, MagicMissile.SPECK + Speck.TOXIC);
		MISSILE_VFX.put(ConfusionGas.class, MagicMissile.SPECK + Speck.CONFUSION);
		MISSILE_VFX.put(Regrowth.class, MagicMissile.FOLIAGE);
		MISSILE_VFX.put(StormCloud.class, MagicMissile.SPECK + Speck.STORM);
		MISSILE_VFX.put(SmokeScreen.class, MagicMissile.SPECK + Speck.SMOKE);
		MISSILE_VFX.put(StenchGas.class, MagicMissile.SPECK + Speck.STENCH);
		MISSILE_VFX.put(Inferno.class, MagicMissile.SPECK + Speck.INFERNO);
		MISSILE_VFX.put(Blizzard.class, MagicMissile.SPECK + Speck.BLIZZARD);
		MISSILE_VFX.put(CorrosiveGas.class, MagicMissile.SPECK + Speck.CORROSION);
	}

}
