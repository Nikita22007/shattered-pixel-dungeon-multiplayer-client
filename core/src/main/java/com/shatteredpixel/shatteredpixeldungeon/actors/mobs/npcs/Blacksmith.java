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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Blacksmith extends NPC {

	{
		spriteClass = BlacksmithSprite.class;
	}

	@Override
	public Notes.Landmark landmark() {
		return (!Quest.completed() || Quest.rewardsAvailable()) ? Notes.Landmark.TROLL : null;
	}

	@Override
	protected boolean act() {
		{
			if (!Quest.rewardsAvailable() && Quest.completed()) {
				Notes.remove(landmark());
			}
		}
		return super.act();
	}

	private void tell(String text) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndQuest(Blacksmith.this, text));
			}
		});
	}

	@Override
	public int defenseSkill(Char enemy) {
		return INFINITE_EVASION;
	}

	@Override
	public boolean add(Buff buff) {
		return false;
	}

	@Override
	public boolean reset() {
		return true;
	}

	public static class Quest {

		private static int type = 0;
		public static final int CRYSTAL = 1;
		public static final int GNOLL = 2;
		public static final int FUNGI = 3; //The fungi quest is not implemented, only exists partially in code

		//quest state information
		private static boolean spawned;
		private static boolean given;
		private static boolean started;
		private static boolean bossBeaten;
		private static boolean completed;

		//reward tracking. Stores remaining favor, the pickaxe, and how many of each reward has been chosen
		public static int favor;
		public static Item pickaxe;
		public static boolean freePickaxe;
		public static int reforges;
		public static int hardens;
		public static int upgrades;
		public static int smiths;

		//pre-generate these so they are consistent between seeds
		public static ArrayList<Item> smithRewards;
		public static Weapon.Enchantment smithEnchant;
		public static Armor.Glyph smithGlyph;

		public static void reset() {
			type = 0;

			spawned = false;
			given = false;
			started = false;
			bossBeaten = false;
			completed = false;

			favor = 0;
            Item item = new Pickaxe();
            pickaxe = item;
			freePickaxe = false;
			reforges = 0;
			hardens = 0;
			upgrades = 0;
			smiths = 0;

			smithRewards = null;
			smithEnchant = null;
			smithGlyph = null;
		}

		private static final String NODE = "blacksmith";

		private static final String TYPE = "type";
		private static final String ALTERNATIVE = "alternative";

		private static final String SPAWNED = "spawned";
		private static final String GIVEN = "given";
		private static final String STARTED = "started";
		private static final String BOSS_BEATEN = "boss_beaten";
		private static final String COMPLETED = "completed";

		private static final String FAVOR = "favor";
		private static final String PICKAXE = "pickaxe";
		private static final String FREE_PICKAXE = "free_pickaxe";
		private static final String REFORGES = "reforges";
		private static final String HARDENS = "hardens";
		private static final String UPGRADES = "upgrades";
		private static final String SMITHS = "smiths";
		private static final String SMITH_REWARDS = "smith_rewards";
		private static final String ENCHANT = "enchant";
		private static final String GLYPH = "glyph";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {
				node.put(TYPE, type);

				node.put(GIVEN, given);
				node.put(STARTED, started);
				node.put(BOSS_BEATEN, bossBeaten);
				node.put(COMPLETED, completed);

				node.put(FAVOR, favor);
				node.put(FREE_PICKAXE, freePickaxe);
				node.put(REFORGES, reforges);
				node.put(HARDENS, hardens);
				node.put(UPGRADES, upgrades);
				node.put(SMITHS, smiths);

				if (smithRewards != null) {
					if (smithEnchant != null) {

						node.put(GLYPH, smithGlyph);
					}
				}
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
				type = node.getInt(TYPE);

				given = node.getBoolean(GIVEN);
				started = node.getBoolean(STARTED);
				bossBeaten = node.getBoolean(BOSS_BEATEN);
				completed = node.getBoolean(COMPLETED);

				favor = node.getInt(FAVOR);
				if (node.contains(PICKAXE)) {
					pickaxe = (Item) node.get(PICKAXE);
				} else {
					pickaxe = null;
				}
				if (node.contains(FREE_PICKAXE)) {
					freePickaxe = node.getBoolean(FREE_PICKAXE);
				} else {
					//some for pre-3.1 saves, some from incorrect values from v3.1-BETA-1.0
					if (favor >= 2500) {
						freePickaxe = true;
					} else {
						freePickaxe = false;
					}
				}
				reforges = node.getInt(REFORGES);
				hardens = node.getInt(HARDENS);
				upgrades = node.getInt(UPGRADES);
				smiths = node.getInt(SMITHS);

				if (node.contains(SMITH_REWARDS)) {
					smithRewards = new ArrayList<>((Collection<Item>) ((Collection<?>) node.getCollection(SMITH_REWARDS)));
					if (node.contains(ENCHANT)) {
						smithEnchant = (Weapon.Enchantment) node.get(ENCHANT);
						smithGlyph = (Armor.Glyph) node.get(GLYPH);
					}
				} else {
					smithRewards = null;
				}

			} else {
				reset();
			}
		}


		public static int Type() {
			return type;
		}

		public static boolean given() {
			return given;
		}

		public static boolean started() {
			return started;
		}

		public static void start() {
			started = true;
		}

		public static boolean beatBoss() {
			return bossBeaten = true;
		}

		public static boolean bossBeaten() {
			return bossBeaten;
		}

		public static boolean completed() {
			return given && completed;
		}

		public static void complete() {
			completed = true;

			favor = 0;

			DarkGold gold = null;
			if (gold != null) {
				favor += Math.min(2000, gold.quantity() * 50);
			}

			Pickaxe pick = null;
			if (pick.isEquipped(Dungeon.hero)) {
				boolean wasCursed = pick.cursed;
				pick.cursed = false; //so that it can always be removed
				pick.doUnequip(Dungeon.hero, false);
				pick.cursed = wasCursed;
			}
			Quest.pickaxe = pick;

			if (bossBeaten) favor += 1000;

			Statistics.questScores[2] += favor;

			if (favor >= 2500) {
				freePickaxe = true;
			}
		}

		public static boolean rewardsAvailable() {
			return favor > 0
					|| (Quest.smithRewards != null && Quest.smiths > 0)
					|| (pickaxe != null && freePickaxe);
		}

	}
}
