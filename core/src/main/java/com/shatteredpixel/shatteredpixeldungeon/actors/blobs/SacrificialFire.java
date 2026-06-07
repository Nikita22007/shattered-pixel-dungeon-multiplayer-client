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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class SacrificialFire extends Blob {

	BlobEmitter curEmitter;

	{
		//acts after mobs, so they can get marked as they move
		actPriority = MOB_PRIO-1;
	}

	//Can spawn extra mobs to make sacrificing less tedious
	// The limit is to prevent farming
	private int bonusSpawns = 3;

	private Item prize;

	@Override
	public Notes.Landmark landmark() {
		return Notes.Landmark.SACRIFICIAL_FIRE;
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		curEmitter = emitter;

		//a bit brittle, assumes only one tile of sacrificial fire can exist per floor
		int max = 6 + Dungeon.depth * 4;
		curEmitter.pour( SacrificialParticle.FACTORY, 0.01f + ((volume / (float)max) * 0.09f) );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	private static final String BONUS_SPAWNS = "bonus_spawns";
	private static final String PRIZE = "prize";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BONUS_SPAWNS, bonusSpawns);
		bundle.put(PRIZE, prize);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		bonusSpawns = bundle.getInt(BONUS_SPAWNS);
		if (bundle.contains(PRIZE)) prize = (Item) bundle.get(PRIZE);
	}

	public static class Marked extends FlavourBuff {

	}

}
