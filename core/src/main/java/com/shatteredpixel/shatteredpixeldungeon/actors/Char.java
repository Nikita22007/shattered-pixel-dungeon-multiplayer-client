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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Bulk;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Char extends Actor {
	public String name = null;

	public int pos = 0;
	
	public CharSprite sprite;
	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;
	protected PathFinder.Path path;

	public int paralysed	    = 0;
	public boolean rooted		= false;
	public boolean flying		= false;
	public int invisible		= 0;
	public int shielding;

	//these are relative to the hero
	public enum Alignment{
		ENEMY,
		NEUTRAL,
		ALLY
	}
	public Alignment alignment;
	
	public int viewDistance	= 8;
	
	public boolean[] fieldOfView = null;
	
	private LinkedHashSet<Buff> buffs = new LinkedHashSet<>();
	
	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		//throw any items that are on top of an immovable char
		if (properties().contains(Property.IMMOVABLE)){
			throwItems();
		}
		return false;
	}

	protected void throwItems(){
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == Heap.Type.HEAP) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+n]){
					candidates.add(pos+n);
				}
			}
			if (!candidates.isEmpty()){
				Dungeon.level.drop( heap.pickUp(), Random.element(candidates) ).sprite.drop( pos );
			}
		}
	}

	public String name(){
		if(name == null) {
			return Messages.get(this, "name");
		}
		return name;
	}

	public boolean canInteract(Char c){
		if (Dungeon.level.adjacent( pos, c.pos )){
			return true;
		} else if (c instanceof Hero
				&& alignment == Alignment.ALLY
				&& !hasProp(this, Property.IMMOVABLE)
				&& Dungeon.level.distance(pos, c.pos) <= 2*Dungeon.hero.pointsInTalent(Talent.ALLY_WARP)){
			return true;
		} else {
			return false;
		}
	}
	
	//swaps places by default
	public boolean interact(Char c){

		//don't allow char to swap onto hazard unless they're flying
		//you can swap onto a hazard though, as you're not the one instigating the swap
		if (!Dungeon.level.passable[pos] && !c.flying){
			return true;
		}

		//can't swap into a space without room
		if (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[c.pos]
			|| c.properties().contains(Property.LARGE) && !Dungeon.level.openSpace[pos]){
			return true;
		}

		//we do a little raw position shuffling here so that the characters are never
		// on the same cell when logic such as occupyCell() is triggered
		int oldPos = pos;
		int newPos = c.pos;

		//can't swap or ally warp if either char is immovable
		if (hasProp(this, Property.IMMOVABLE) || hasProp(c, Property.IMMOVABLE)){
			return true;
		}

		//warp instantly with allies in this case
		if (c instanceof Hero && ((Hero) c).hasTalent(Talent.ALLY_WARP)){
			PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[pos] == Integer.MAX_VALUE){
				return true;
			}
			pos = newPos;
			c.pos = oldPos;
			ScrollOfTeleportation.appear(this, newPos);
			ScrollOfTeleportation.appear(c, oldPos);
			Dungeon.observe();
			GameScene.updateFog();
			return true;
		}

		//can't swap places if one char has restricted movement
		if (paralysed > 0 || c.paralysed > 0 || rooted || c.rooted
				|| false || false){
			return true;
		}

		c.pos = oldPos;
		moveSprite( oldPos, newPos );
		move( newPos );

		c.pos = newPos;
		c.sprite.move( newPos, oldPos );
		c.move( oldPos );
		
		c.spend( 1 / c.speed() );

		if (c == Dungeon.hero){
			if (Dungeon.hero.subClass == HeroSubClass.FREERUNNER){
				((Momentum) null).gainStack();
			}

			Dungeon.hero.busy();
		}
		
		return true;
	}
	
	public boolean moveSprite( int from, int to ) {
		
		if (sprite.isVisible() && sprite.parent != null && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to])) {
			sprite.move( from, to );
			return true;
		} else {
			sprite.turnTo(from, to);
			sprite.place( to );
			return true;
		}
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(Assets.Sounds.HIT, 1, pitch);
	}

	public boolean blockSound( float pitch ) {
		return false;
	}
	
	protected static final String POS       = "pos";
	protected static final String TAG_HP    = "HP";
	protected static final String TAG_HT    = "HT";
	protected static final String TAG_SHLD  = "SHLD";
	protected static final String BUFFS	    = "buffs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		
		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

	public static int INFINITE_ACCURACY = 1_000_000;
	public static int INFINITE_EVASION = 1_000_000;

	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}
	
	public int drRoll() {
		int dr = 0;

		dr += Random.NormalIntRange( 0 , Barkskin.currentLevel(this) );

		return dr;
	}
	
	public int damageRoll() {
		return 1;
	}

	//Returns the level a glyph is at for a char, or -1 if they are not benefitting from that glyph
	//This function is needed as (unlike enchantments) many glyphs trigger in a variety of cases
	public int glyphLevel(Class<? extends Armor.Glyph> cls){
        return -1;
    }

	public float speed() {
		float speed = baseSpeed;

		speed *= Swiftness.speedBoost(this, glyphLevel(Swiftness.class));
		speed *= Flow.speedBoost(this, glyphLevel(Flow.class));
		speed *= Bulk.speedBoost(this, glyphLevel(Bulk.class));

		return speed;
	}

	//currently only used by invisible chars, or by the hero
	public boolean canSurpriseAttack(){
		return true;
	}
	

	public boolean needsShieldUpdate = true;
	
	public int shielding(){
		return shielding;
	}

	public void destroy() {
		HP = 0;
		Actor.remove( this );

		for (Char ch : Actor.chars().toArray(new Char[0])) {
		}
	}
	
	public void die( Object src ) {
		destroy();
		if (src != Chasm.class) {
			sprite.die();
			if (!flying && Dungeon.level != null && sprite instanceof MobSprite && Dungeon.level.map[pos] == Terrain.CHASM){
				((MobSprite) sprite).fall();
			}
		}
	}

	//we cache this info to prevent having to call buff(...) in isAlive.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and thread coordination implications
	public boolean deathMarked = false;

	@Contract(pure = true)
	public boolean isAlive() {
		return HP > 0 || deathMarked;
	}

	public boolean isActive() { //todo sent this
		return isAlive();
	}

	@Override
	protected void spendConstant(float time) {
		TimekeepersHourglass.timeFreeze freeze = null;
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}

		Swiftthistle.TimeBubble bubble = null;
		if (bubble != null){
			bubble.processTime(time);
			return;
		}

		super.spendConstant(time);
	}

	@Override
	protected void spend( float time ) {

		float timeScale = 1f;
		{

		}

		super.spend(time / timeScale);
	}
	
	public synchronized LinkedHashSet<Buff> buffs() {
		return new LinkedHashSet<>(buffs);
	}

	public synchronized boolean add( Buff buff ) {

		if (sprite != null) {
		}

		buffs.add(buff);
		if (Actor.chars().contains(this)) Actor.add(buff);

		if (sprite != null && buff.announced) {
			switch (buff.type) {
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, Messages.titleCase(buff.name()));
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.WARNING, Messages.titleCase(buff.name()));
					break;
				case NEUTRAL:
				default:
					sprite.showStatus(CharSprite.NEUTRAL, Messages.titleCase(buff.name()));
					break;
			}
		}

		return true;

	}
	
	public synchronized boolean remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );

		return true;
	}

	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}
	
	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}
	
	public float stealth() {
		float stealth = 0;

		stealth += Obfuscation.stealthBoost(this, glyphLevel(Obfuscation.class));

		return stealth;
	}

	public final void move( int step ) {
		move( step, true );
	}

	//travelling may be false when a character is moving instantaneously, such as via teleportation
	public void move( int step, boolean travelling ) {

		if (travelling && Dungeon.level.adjacent(step, pos)) {
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}
		
		Dungeon.level.occupyCell(this );
	}
	
	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}

	public boolean[] modifyPassable( boolean[] passable){
		//do nothing by default, but some chars can pass over terrain that others can't
		return passable;
	}
	
	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}
	
	public void onAttackComplete() {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final HashSet<Class> resistances = new HashSet<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class effect ){
		return RingOfElements.resist(this, effect);
	}
	
	protected final HashSet<Class> immunities = new HashSet<>();
	
	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}
		if (glyphLevel(Brimstone.class) >= 0){
			immunes.add(Burning.class);
		}

		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		return false;
	}

	//similar to isImmune, but only factors in damage.
	//Is used in AI decision-making
	public boolean isInvulnerable( Class effect ) {
		return false;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		HashSet<Property> props = new HashSet<>(properties);
		//TODO any more of these and we should make it a property of the buff, like with resistances/immunities
		return props;
	}

	public enum Property{
		BOSS ( new HashSet<Class>( Arrays.asList(Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class)),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class) )),
		MINIBOSS ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class) )),
		BOSS_MINION,
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),
		FIERY ( new HashSet<Class>( Arrays.asList(WandOfFireblast.class, Elemental.FireElemental.class)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),
		ICY ( new HashSet<Class>( Arrays.asList(WandOfFrost.class, Elemental.FrostElemental.class)),
				new HashSet<Class>( Arrays.asList(Frost.class, Chill.class))),
		ACIDIC ( new HashSet<Class>( Arrays.asList(Corrosion.class)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),
		ELECTRIC ( new HashSet<Class>( Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class,
										Electricity.class, ShockingDart.class, Elemental.ShockElemental.class )),
				new HashSet<Class>()),
		LARGE,
		IMMOVABLE ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Vertigo.class) )),
		//A character that acts in an unchanging manner. immune to AI state debuffs or stuns/slows
		STATIC( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class, Terror.class, Amok.class, Charm.class, Sleep.class,
									Paralysis.class, Frost.class, Chill.class, Slow.class, Speed.class) ));

		private HashSet<Class> resistances;
		private HashSet<Class> immunities;
		
		Property(){
			this(new HashSet<Class>(), new HashSet<Class>());
		}
		
		Property( HashSet<Class> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}
		
		public HashSet<Class> resistances(){
			return new HashSet<>(resistances);
		}
		
		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}

	}

	@Contract(value = "null,_->false", pure=true)
	public static boolean hasProp( Char ch, Property p){
		return (ch != null && ch.properties().contains(p));
	}
}
