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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.HashSet;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;

		new HashSet<Property>().add(Property.IMMOVABLE);
	}

	public static int MAX_BUYBACK_HISTORY = 3;
	public ArrayList<Item> buybackItems = new ArrayList<>();

	private int turnsSinceHarmed = -1;

	@Override
	public Notes.Landmark landmark() {
		return Notes.Landmark.SHOP;
	}

	@Override
	protected boolean act() {

		if (turnsSinceHarmed >= 0) {
			turnsSinceHarmed++;
		}

		sprite.turnTo(pos, Dungeon.hero.pos);
		spend(TICK);
		return super.act();
	}

	@Override
	public boolean add(Buff buff) {
		if (buff.type == Buff.buffType.NEGATIVE) {
			processHarm();
		}
		return false;
	}

	public void processHarm() {

		//do nothing if the shopkeeper is out of the hero's FOV
		if (!Dungeon.level.heroFOV[pos]) {
			return;
		}

		if (turnsSinceHarmed == -1) {
			turnsSinceHarmed = 0;
			yell(Messages.get(this, "warn"));

			//use a new actor as we can't clear the gas while we're in the middle of processing it
			Actor.add(new Actor() {
				{
					actPriority = VFX_PRIO;
				}

				@Override
				protected boolean act() {
					//cleanses all harmful blobs in the shop

					PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 4);

					Actor.remove(this);
					return true;
				}
			});

			//There is a 1 turn buffer before more damage/debuffs make the shopkeeper flee
			//This is mainly to prevent stacked effects from causing an instant flee
		} else if (turnsSinceHarmed >= 1) {
			flee();
		}
	}

	public void flee() {
		destroy();

		Notes.remove(landmark());
		GLog.newLine();
		GLog.n(Messages.get(this, "flee"));

		if (sprite != null) {
			sprite.killAndErase();
			CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		for (Heap heap : Dungeon.level.heaps.valueList()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				if (ShatteredPixelDungeon.scene() instanceof GameScene) {
					CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
				}
				if (heap.size() == 1) {
					heap.destroy();
				} else {
					heap.items.remove(heap.size() - 1);
					heap.type = Heap.Type.HEAP;
				}
			}
		}
	}

	@Override
	public boolean reset() {
		return true;
	}

	//shopkeepers are greedy!
	public static int sellPrice(Item item) {
		return item.value() * 5 * (Dungeon.depth / 5 + 1);
	}

	public static WndBag sell() {
		return GameScene.selectItem(itemSelector);
	}

	public static boolean canSell(Item item) {
		if (item.value() <= 0) return false;
		if (item.unique && !item.stackable) return false;
		if (item.isEquipped(Dungeon.hero) && item.cursed) return false;
		return true;
	}

	private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Shopkeeper.class, "sell");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Shopkeeper.canSell(item);
		}

		@Override
		public void onSelect(Item item) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show(new WndTradeItem(item, parentWnd));
			}
		}
	};

	public String chatText() {
		switch (Dungeon.depth) {
			case 6:
			default:
				return Messages.get(this, "talk_prison_intro") + "\n\n" + Messages.get(this, "talk_prison_" + Dungeon.hero.heroClass.name());
			case 11:
				return Messages.get(this, "talk_caves");
			case 16:
				return Messages.get(this, "talk_city");
			case 20:
				return Messages.get(this, "talk_halls");
		}
	}

	public static String BUYBACK_ITEMS = "buyback_items";

	public static String TURNS_SINCE_HARMED = "turns_since_harmed";

}
