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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

@SuppressWarnings("unused")
public class MagicalFireRoom  {

	@SuppressWarnings("unused")
	public static class EternalFire extends Blob {

		@Override
		public void seed(Level level, int cell, int amount) {
			super.seed(level, cell, amount);
			level.passable[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.PASSABLE) != 0;
		}

		@Override
		public void clear(int cell) {
			if (volume > 0 && cur[cell] > 0) {
				fullyClear();
			}
		}

		@Override
		public void fullyClear() {
			super.fullyClear();
			Dungeon.level.buildFlagMaps();
		}

		@Override
		public void use( BlobEmitter emitter ) {
			super.use( emitter );
			emitter.pour( ElmoParticle.FACTORY, 0.02f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}

		@Override
		public void onBuildFlagMaps( Level l ) {
			if (volume > 0){
				for (int i=0; i < l.length(); i++) {
					l.passable[i] = l.passable[i] && cur[i] == 0;
				}
			}
		}
	}

}
