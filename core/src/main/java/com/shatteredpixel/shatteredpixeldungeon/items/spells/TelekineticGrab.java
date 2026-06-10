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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class TelekineticGrab extends TargetedSpell {

	{
		image = ItemSpriteSheet.TELE_GRAB;

		talentChance = 1/(float) 8;
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.BEACON,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	protected float timeToCast() {
		//time is processed in the spell's logic, as it relates to items picked up
		return 0;
	}

	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {


		float totalpickupTime = 0;

        if (Dungeon.level.heaps.get(bolt.collisionPos) != null){

            Heap h = Dungeon.level.heaps.get(bolt.collisionPos);

            if (h.type != Heap.Type.HEAP){
                GLog.w(Messages.get(this, "cant_grab"));
                hero.spend(Actor.TICK);
                h.sprite.drop();
                return;
            }

            while (!h.isEmpty()) {
                Item item = h.peek();
                GLog.w(Messages.get(this, "cant_grab"));
                h.sprite.drop();
                break;
            }


        } else {
            GLog.w(Messages.get(this, "no_target"));
            hero.spend(Actor.TICK);
        }

		onSpellused();
	}

	@Override
	public int value() {
		return (int)(50 * (quantity/(float) 8));
	}

	@Override
	public int energyVal() {
		return (int)(10 * (quantity/(float) 8));
	}

}
