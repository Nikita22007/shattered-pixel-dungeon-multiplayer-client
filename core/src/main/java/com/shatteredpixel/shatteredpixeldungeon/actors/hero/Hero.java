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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Log;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Hero extends Char {
	public int gold;

	{
		actPriority = HERO_PRIO;

		alignment = Alignment.ALLY;
	}

	public static final int STARTING_STR = 10;

	private static final float TIME_TO_SEARCH = 2f;
	private static final float HUNGER_FOR_SEARCH = 6f;

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;
	public ArmorAbility armorAbility = null;
	public ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
	public LinkedHashMap<Talent, Talent> metamorphedTalents = new LinkedHashMap<>();

	public boolean ready = false;
	public boolean damageInterrupt = true;

	//reference to the enemy the hero is currently in the process of attacking
	private Char attackTarget;

	public Belongings belongings;

	public int STR;

	public int lvl = 1;
	public int exp = 0;

	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero() {
		super();

		HP = HT = 20;
		STR = STARTING_STR;

		belongings = new Belongings(this);

		visibleEnemies = new ArrayList<>();
	}

	public int STR() {
		int strBonus = 0;

		strBonus += RingOfMight.strengthBonus(this);

		if (hasTalent(Talent.STRONGMAN)) {
			strBonus += (int) Math.floor(STR * (0.03f + 0.05f * pointsInTalent(Talent.STRONGMAN)));
		}

		return STR + strBonus;
	}

	private static final String CLASS = "class";
	private static final String SUBCLASS = "subClass";
	private static final String ABILITY = "armorAbility";

	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "defenseSkill";
	private static final String STRENGTH = "STR";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String HTBOOST = "htboost";

	public static void preview(GamesInProgress.Info info, Bundle bundle) {
		info.level = bundle.getInt(LEVEL);
		info.str = bundle.getInt(STRENGTH);
		info.exp = bundle.getInt(EXPERIENCE);
		info.hp = bundle.getInt(Char.TAG_HP);
		info.ht = bundle.getInt(Char.TAG_HT);
		info.shld = bundle.getInt(Char.TAG_SHLD);
		info.heroClass = bundle.getEnum(CLASS, HeroClass.class);
		info.subClass = bundle.getEnum(SUBCLASS, HeroSubClass.class);
		Belongings.preview(info, bundle);
	}

	@Contract(pure = true)
	public boolean hasTalent(Talent talent) {
		return pointsInTalent(talent) > 0;
	}

	@Contract(pure = true)
	public int pointsInTalent(Talent talent) {
		for (LinkedHashMap<Talent, Integer> tier : talents) {
			for (Talent f : tier.keySet()) {
				if (f == talent) return tier.get(f);
			}
		}
		return 0;
	}

	public void upgradeTalent(Talent talent) {
		for (LinkedHashMap<Talent, Integer> tier : talents) {
			for (Talent f : tier.keySet()) {
				if (f == talent) tier.put(talent, tier.get(talent) + 1);
			}
		}
		SendData.sendTalentUpgrade(talent);
		Talent.onTalentUpgraded(this, talent);
	}

	public int talentPointsSpent(int tier) {
		int total = 0;
		for (int i : talents.get(tier - 1).values()) {
			total += i;
		}
		return total;
	}

	public int talentPointsAvailable(int tier) {
		if (lvl < (Talent.tierLevelThresholds[tier] - 1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (lvl >= Talent.tierLevelThresholds[tier + 1]) {
			return Talent.tierLevelThresholds[tier + 1] - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		} else {
			return 1 + lvl - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		}
	}

	public int bonusTalentPoints(int tier) {
		if (lvl < (Talent.tierLevelThresholds[tier] - 1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else {
			{
				return 0;
			}
		}
	}

	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	@Override
	public String name() {
		{
			return className();
		}
	}

	public void live() {
		for (Buff b : buffs()) {
			if (!b.revivePersists) b.detach();
		}
	}

	//We need this for status pane
	public int tier = 1;

	public int tier() {
		return tier;
	}


	@Override
	public float speed() {

		float speed = super.speed();

		speed *= RingOfHaste.speedMultiplier(this);


        ((HeroSprite) sprite).sprint(1f);


		return speed;

	}


	public boolean canAttack(Char enemy) {
		if (enemy == null || pos == enemy.pos || !Actor.chars().contains(enemy)) {
			return false;
		}
		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return true;
		}
		return false;
	}

	public float attackDelay() {

		float delay = 1f;

		{
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			float speed = RingOfFuror.attackSpeedMultiplier(this);

			//ditto for furor + sword dance!

			//and augments + brawler's stance! My goodness, so many options now compared to 2014!
			if (RingOfForce.unarmedGetsWeaponAugment(this)) {
				delay = ((Weapon) belongings.weapon).augment.delayFactor(delay);
			}

			return delay / speed;
		}
	}

	@Override
	public void spend(float time) {
		super.spend(time);
	}

	@Override
	public void spendConstant(float time) {
		super.spendConstant(time);
	}

	public void spendAndNext(float time) {
		busy();
		spend(time);
		next();
	}

	@Override
	public boolean act() {

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;

		if (!ready) {
			//do a full observe (including fog update) if not resting.
            Dungeon.observe();
        }

		checkVisibleMobs();
		BuffIndicator.refreshHero();
		BuffIndicator.refreshBoss();

		if (paralysed > 0) {

			spendAndNext(TICK);
			return false;
		}

		ready();

        return false;
	}

	public void busy() {
		ready = false;
	}

	public void ready() {
		if (sprite.looping()) sprite.idle();
		damageInterrupt = true;
		waitOrPickup = false;
		ready = true;
		canSelfTrample = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}

	public void interrupt() {
        GameScene.resetKeyHold();
	}

	public void resume() {
		damageInterrupt = false;
		next();
	}

	private boolean canSelfTrample = false;

	public boolean canSelfTrample() {
		return canSelfTrample && !rooted && !flying &&
				//standing in high grass
				(Dungeon.level.map[pos] == Terrain.HIGH_GRASS ||
						//standing in furrowed grass and not huntress
						(heroClass != HeroClass.HUNTRESS && Dungeon.level.map[pos] == Terrain.FURROWED_GRASS) ||
						//standing on a plant
						Dungeon.level.plants.get(pos) != null);
	}

	//used to keep track if the wait/pickup action was used
	// so that the hero spends a turn even if the fail to pick up an item
	public boolean waitOrPickup = false;

	public void rest(boolean fullRest) {
		if (fullRest) {
			SendData.sendRestAction();
		} else {
			SendData.sendWaitAction();
		}
	}


	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[m.pos] && m.landmark() != null) {
				Notes.add(m.landmark());
			}

			if (fieldOfView[m.pos] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains(m)) {
					newMob = true;
				}

				//only do a simple check for mind visioned enemies, better performance
				if ((!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1)
						|| (mindVisionEnemies.contains(m) && new Ballistica(pos, m.pos, Ballistica.PROJECTILE).collisionPos == m.pos)) {
					if (target == null) {
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
                }
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
				!lastTarget.isAlive() || !lastTarget.isActive() ||
				lastTarget.alignment == Alignment.ALLY ||
				!fieldOfView[lastTarget.pos])) {
			QuickSlotButton.target(target);
		}

		if (newMob) {
			if (false) {
				Dungeon.observe();
			}
			interrupt();
		}

		visibleEnemies = visible;

		//we also scan for blob landmarks here
		for (Blob b : Dungeon.level.blobs.values().toArray(new Blob[0])) {
			if (b.volume > 0 && b.landmark() != null && !Notes.contains(b.landmark())) {
				int cell;
				boolean found = false;
				//if a single cell within the blob is visible, we add the landmark
				for (int i = b.area.top; i < b.area.bottom; i++) {
					for (int j = b.area.left; j < b.area.right; j++) {
						cell = j + i * Dungeon.level.width();
						if (b.cur.length > cell && fieldOfView[cell] && b.cur[cell] > 0) {
							Notes.add(b.landmark());
							found = true;
							break;
						}
					}
					if (found) break;
				}
			}
		}
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy(int index) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	public ArrayList<Mob> getVisibleEnemies() {
		return new ArrayList<>(visibleEnemies);
	}

	private boolean walkingToVisibleTrapInFog = false;

	public boolean handle(int cell) {
		SendData.SendCellListenerCell(cell);
		return true;
	}

	public int maxExp() {
		return maxExp(lvl);
	}

	public static int maxExp(int lvl) {
		return 5 + lvl * 5;
	}

	@Override
	public boolean add(Buff buff) {

		boolean added = super.add(buff);

		if (sprite != null && added) {
			String msg = buff.heroMessage();
			if (msg != null) {
				GLog.w(msg);
			}

		}

		BuffIndicator.refreshHero();

		return added;
	}

	@Override
	public boolean remove(Buff buff) {
		if (super.remove(buff)) {
			BuffIndicator.refreshHero();
			return true;
		}
		return false;
	}

	@Override
	public void die(Object cause) {

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Ankh i : belongings.getAllItems(Ankh.class)) {
			if (ankh == null || i.isBlessed()) {
				ankh = i;
			}
		}

		if (ankh != null) {
			interrupt();

			if (ankh.isBlessed()) {
				this.HP = HT / 4;

				PotionOfHealing.cure(this);

				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.w(Messages.get(this, "revive"));
				Statistics.ankhsUsed++;

            } else {

				//this is hacky, basically we want to declare that a wndResurrect exists before
				//it actually gets created. This is important so that the game knows to not
				//delete the run or submit it to rankings, because a WndResurrect is about to exist
				//this is needed because the actual creation of the window is delayed here
				WndResurrect.instance = new Object();
				Ankh finalAnkh = ankh;
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndResurrect(finalAnkh));
					}
				});

				if (cause instanceof Doom) {
					((Doom) cause).onDeath();
				}


            }
			return;
		}

		Actor.fixTime();
		super.die(cause);
		reallyDie(cause);
	}

	public static void reallyDie(Object cause) {

		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;

		for (int i = 0; i < length; i++) {

			int terr = map[i];

			if (discoverable[i]) {

				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Dungeon.level.discover(i);
				}
			}
		}

		Dungeon.observe();
		GameScene.updateFog();

        int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get(cell) == null) {
				passable.add(cell);
			}
		}
		Collections.shuffle(passable);

		ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element(items);
			Dungeon.level.drop(item, cell).sprite.drop(pos);
			items.remove(item);
		}

		for (Char c : Actor.chars()) {
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.gameOver();
				Sample.INSTANCE.play(Assets.Sounds.DEATH);
			}
		});

		if (cause instanceof Doom) {
			((Doom) cause).onDeath();
		}


	}

	@Override
	public boolean isAlive() {

		if (HP <= 0) {
			return false;
		} else {
			return super.isAlive();
		}
	}

	@Override
	public void move(int step, boolean travelling) {
		boolean wasHighGrass = Dungeon.level.map[step] == Terrain.HIGH_GRASS;

		super.move(step, travelling);

		if (!flying && travelling) {
			if (Dungeon.level.water[pos]) {
				Sample.INSTANCE.play(Assets.Sounds.WATER, 1, Random.Float(0.8f, 1.25f));
			} else if (Dungeon.level.map[pos] == Terrain.EMPTY_SP) {
				Sample.INSTANCE.play(Assets.Sounds.STURDY, 1, Random.Float(0.96f, 1.05f));
			} else if (Dungeon.level.map[pos] == Terrain.GRASS
					|| Dungeon.level.map[pos] == Terrain.EMBERS
					|| Dungeon.level.map[pos] == Terrain.FURROWED_GRASS) {
				if (step == pos && wasHighGrass) {
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float(0.96f, 1.05f));
				} else {
					Sample.INSTANCE.play(Assets.Sounds.GRASS, 1, Random.Float(0.96f, 1.05f));
				}
			} else {
				Sample.INSTANCE.play(Assets.Sounds.STEP, 1, Random.Float(0.96f, 1.05f));
			}
		}
	}

	@Override
	public void onAttackComplete() {

		if (attackTarget == null) {
			super.onAttackComplete();
			return;
		}

		AttackIndicator.target(attackTarget);


		attackTarget = null;

		super.onAttackComplete();
	}

	@Override
	public void onMotionComplete() {
		GameScene.checkKeyHold();
	}

	@Override
	public void onOperateComplete() {

		if ((HeroAction) null instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock) null).dst;
			int door = Dungeon.level.map[doorCell];

			SkeletonKey.keyRecharge skele = null;
			SkeletonKey.KeyReplacementTracker keyUseTrack = null;

			if (skele != null && skele.isCursed() && Random.Int(6) != 0) {
				GLog.n(Messages.get(this, "key_distracted"));
				spendAndNext(2 * Key.TIME_TO_UNLOCK);
				((Hunger) null).affectHunger(-4);
			} else if (Dungeon.level.distance(pos, doorCell) <= 1) {
				boolean hasKey = true;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.depth));
					if (hasKey) {
						if (keyUseTrack != null) {
							keyUseTrack.processIronLockOpened();
						}
						Level.set(doorCell, Terrain.DOOR);
					}
				} else if (door == Terrain.HERO_LKD_DR) {
					hasKey = true;
					Level.set(doorCell, Terrain.DOOR);
					GLog.i(Messages.get(SkeletonKey.class, "force_lock"));
				} else if (door == Terrain.CRYSTAL_DOOR) {
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
					if (hasKey) {
						if (keyUseTrack != null) {
							keyUseTrack.processCrystalLockOpened();
						}
						Level.set(doorCell, Terrain.EMPTY);
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						CellEmitter.get(doorCell).start(Speck.factory(Speck.DISCOVER), 0.025f, 20);
					}
				} else {
					hasKey = Notes.remove(new WornKey(Dungeon.depth));
					if (hasKey) {
						Level.set(doorCell, Terrain.UNLOCKED_EXIT);
					}
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		} else if ((HeroAction) null instanceof HeroAction.OpenChest) {

			Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) null).dst);
			SkeletonKey.keyRecharge skele = null;
			SkeletonKey.KeyReplacementTracker keyUseTrack = null;

			if (skele != null && skele.isCursed()
					&& (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST)
					&& Random.Int(6) != 0) {
				GLog.n(Messages.get(this, "key_distracted"));
				spend(2 * Key.TIME_TO_UNLOCK);
				((Hunger) null).affectHunger(-4);
			} else if (Dungeon.level.distance(pos, heap.pos) <= 1) {
				boolean hasKey = true;
				if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
					Sample.INSTANCE.play(Assets.Sounds.BONES);
				} else if (heap.type == Type.LOCKED_CHEST) {
					hasKey = Notes.remove(new GoldenKey(Dungeon.depth));
					if (hasKey && keyUseTrack != null) {
						keyUseTrack.processGoldLockOpened();
					}
				} else if (heap.type == Type.CRYSTAL_CHEST) {
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
					if (hasKey && keyUseTrack != null) {
						keyUseTrack.processCrystalLockOpened();
					}
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		}

		if (!ready) {
			super.onOperateComplete();
		}
	}


	public void search(boolean intentional) {
		if (intentional) {
			SendData.sendSearchAction();
		} else {
			Log.w("Game", "Search not from button");
		}
	}

	public void resurrect() {
		HP = HT;
		live();

		//lost inventory is dropped in interlevelscene

		//activate items that persist after lost inventory
		//FIXME this is very messy, maybe it would be better to just have one buff that
		// handled all items that recharge over time?


	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public static interface Doom {
		public void onDeath();
	}
}
