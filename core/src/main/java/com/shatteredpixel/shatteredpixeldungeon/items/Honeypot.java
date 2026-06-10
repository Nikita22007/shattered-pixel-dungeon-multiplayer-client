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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Honeypot extends Item {

	{
		image = ItemSpriteSheet.HONEYPOT;

		defaultAction = AC_THROW;
		usesTargeting = true;

		stackable = true;
	}
	
	public Item shatter( Char owner, int pos ) {
		
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Splash.at( pos, 0xffd500, 5 );
		}
		
		int newPos = pos;
		if (Actor.findChar( pos ) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			
			for (int n : PathFinder.NEIGHBOURS4) {
				int c = pos + n;
				if (!Dungeon.level.solid[c] && Actor.findChar( c ) == null) {
					candidates.add( c );
				}
			}
	
			newPos = !candidates.isEmpty() ? Random.element( candidates ) : -1;
		}
		
		if (newPos != -1) {
			Bee bee = new Bee();
			bee.spawn( Dungeon.scalingDepth() );
			bee.setPotInfo( pos, owner );
			bee.HP = bee.HT;
			bee.pos = newPos;
			
			GameScene.add( bee );
			if (newPos != pos) Actor.add( new Pushing( bee, pos, newPos ) );

			bee.sprite.alpha( 0 );
			bee.sprite.parent.add( new AlphaTweener( bee.sprite, 1, 0.15f ) );
			
			Sample.INSTANCE.play( Assets.Sounds.BEE );
			return new ShatteredPot();
		} else {
			return this;
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}

	//The bee's broken 'home', all this item does is let its bee know where it is, and who owns it (if anyone).
	public static class ShatteredPot extends Item {

		{
			image = ItemSpriteSheet.SHATTPOT;
			stackable = true;
		}


		@Override
		public boolean isUpgradable() {
			return false;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public int value() {
			return 5 * quantity;
		}
	}
}
