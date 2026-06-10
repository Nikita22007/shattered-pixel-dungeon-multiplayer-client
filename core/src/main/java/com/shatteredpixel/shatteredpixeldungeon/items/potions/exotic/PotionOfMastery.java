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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class PotionOfMastery extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_MASTERY;

		unique = true;

		talentFactor = 2f;
	}

	protected static boolean identifiedByUse = false;

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(PotionOfMastery.class, "prompt");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return
					(false && !(false) && !((Weapon) item).masteryPotionBonus)
					|| (false && !((Armor) item).masteryPotionBonus);
		}

		@Override
		public void onSelect(Item item) {

			if (item == null && identifiedByUse){
				GameScene.show( new WndOptions(new ItemSprite(PotionOfMastery.this),
						Messages.titleCase(name()),
						Messages.get(ExoticPotion.class, "warning"),
						Messages.get(ExoticPotion.class, "yes"),
						Messages.get(ExoticPotion.class, "no") ) {
					@Override
					protected void onSelect( int index ) {
						switch (index) {
							case 0:
								curUser.spendAndNext(1f);
								identifiedByUse = false;
								break;
							case 1:
								GameScene.selectItem(itemSelector);
								break;
						}
					}
					public void onBackPressed() {}
				} );
			} else if (item != null) {

				if (false) {
					((Weapon) item).masteryPotionBonus = true;
					GLog.p( Messages.get(PotionOfMastery.class, "weapon_easier") );
				} else if (false) {
					((Armor) item).masteryPotionBonus = true;
					GLog.p( Messages.get(PotionOfMastery.class, "armor_easier") );
				}
				updateQuickslot();

				Sample.INSTANCE.play( Assets.Sounds.DRINK );
				curUser.sprite.operate(curUser.pos);

				if (!identifiedByUse) {
                }
				identifiedByUse = false;

				if (!anonymous) {
                    if (Random.Float() < talentChance) {
						Talent.onPotionUsed(curUser, curUser.pos, talentFactor);
					}
				}
			}

		}
	};
	
}
