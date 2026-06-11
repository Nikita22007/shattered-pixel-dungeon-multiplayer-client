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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Combo  {

	public enum ComboMove {
		CLOBBER(2, 0x00FF00),
		SLAM   (4, 0xCCFF00),
		PARRY  (6, 0xFFFF00),
		CRUSH  (8, 0xFFCC00),
		FURY   (10, 0xFF0000);

		public int comboReq, tintColor;

		ComboMove(int comboReq, int tintColor){
			this.comboReq = comboReq;
			this.tintColor = tintColor;
		}

		public String title(){
			return Messages.get(this, name() + ".name");
		}

		public String desc(int count){
			switch (this){
				case CLOBBER: default:
					if (count >= 7 && Dungeon.hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 1){
						return Messages.get(this, name() + ".empower_desc");
					} else {
						return Messages.get(this, name() + ".desc");
					}
				case SLAM:
					if (count >= 3 && Dungeon.hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 3){
						return Messages.get(this, name() + ".empower_desc", count/3, count*20);
					} else {
						return Messages.get(this, name() + ".desc", count*20);
					}
				case PARRY:
					if (count >= 9 && Dungeon.hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 2){
						return Messages.get(this, name() + ".empower_desc");
					} else {
						return Messages.get(this, name() + ".desc");
					}
				case CRUSH:
					if (count >= 3 && Dungeon.hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 3){
						return Messages.get(this, name() + ".empower_desc", count/3, count*25);
					} else {
						return Messages.get(this,  name() + ".desc", count*25);
					}
				case FURY:
					if (count >= 3 && Dungeon.hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 3){
						return Messages.get(this, name() + ".empower_desc", count/3);
					} else {
						return Messages.get(this,  name() + ".desc");
					}
			}
		}

	}

}
