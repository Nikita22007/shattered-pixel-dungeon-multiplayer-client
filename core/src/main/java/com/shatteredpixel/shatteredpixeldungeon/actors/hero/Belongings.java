/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.items.*;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.CustomBag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.InventoryPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Belongings implements Iterable<Item> {

	public List<Integer> pathOfItem(@NotNull Item item) {
		assert (item != null) : "path of null item";
		List<Integer> slot = new ArrayList<>(2);

		if (item == weapon) {

			slot.add(-1);
			return slot;
		}
		if (item == armor) {

			slot.add(-2);
			return slot;
		}
		if (item == artifact) {

			slot.add(-3);
			return slot;
		}
		if (item == misc) {

			slot.add(-4);
			return slot;
		}
		if (item == ring) {

			slot.add(-5);
			return slot;
		}

		/* maybe delete, because we will use slot variables instead of list of special slots
		if (specialSlots.get(i).item instanceof Bag) {
			List<Integer> path = ((Bag) specialSlots.get(i).item).pathOfItem(item);
			if (path != null) {
				path.add(0, -i - 1);
				return path;
			}
		}
		*/
		return backpack.pathOfItem(item);
	}


	private Hero owner;

	public static class Backpack extends CustomBag {
		{
			image = ItemSpriteSheet.BACKPACK;
		}

		public Backpack(JSONObject obj){
			super(obj);
		}
		public int capacity(){
			int cap = super.capacity();
			for (Item item : items){
				if (item instanceof Bag){
					cap++;
				}
			}
			if (hero != null && hero.belongings.secondWep != null){
				//secondary weapons still occupy an inv. slot
				cap--;
			}
			return cap;
		}
	}

	public Backpack backpack;
	
	public Belongings( Hero owner ) {
		this.owner = owner;
	}

	public CustomItem weapon = null;
	public CustomItem armor = null;
	public CustomItem artifact = null;
	public CustomItem misc = null;
	public CustomItem ring = null;

	//used when thrown weapons temporary become the current weapon
	public KindOfWeapon thrownWeapon = null;

	//used to ensure that the duelist always uses the weapon she's using the ability of
	public KindOfWeapon abilityWeapon = null;

	//used by the champion subclass
	public KindOfWeapon secondWep = null;

	//*** these accessor methods are so that worn items can be affected by various effects/debuffs
	// we still want to access the raw equipped items in cases where effects should be ignored though,
	// such as when equipping something, showing an interface, or dealing with items from a dead hero

	//normally the primary equipped weapon, but can also be a thrown weapon or an ability's weapon
	public CustomItem attackingWeapon(){
		if (thrownWeapon != null) return thrownWeapon;
		if (abilityWeapon != null) return abilityWeapon;
		return weapon();
	}

	//we cache whether belongings are lost to avoid lots of calls to hero.buff(LostInventory.class)
	private boolean lostInvent;
	public void lostInventory( boolean val ){
		lostInvent = val;
	}

	public boolean lostInventory(){
		return lostInvent;
	}

	public CustomItem weapon(){
		if (!lostInventory() || (weapon != null && weapon.keptThroughLostInventory())){
			return weapon;
		} else {
			return null;
		}
	}

	public CustomItem armor(){
		if (!lostInventory() || (armor != null && armor.keptThroughLostInventory())){
			return armor;
		} else {
			return null;
		}
	}

	public CustomItem artifact(){
		if (!lostInventory() || (artifact != null && artifact.keptThroughLostInventory())){
			return artifact;
		} else {
			return null;
		}
	}

	public CustomItem misc(){
		if (!lostInventory() || (misc != null && misc.keptThroughLostInventory())){
			return misc;
		} else {
			return null;
		}
	}

	public CustomItem ring(){
		if (!lostInventory() || (ring != null && ring.keptThroughLostInventory())){
			return ring;
		} else {
			return null;
		}
	}

	public CustomItem secondWep(){
		if (!lostInventory() || (secondWep != null && secondWep.keptThroughLostInventory())){
			return secondWep;
		} else {
			return null;
		}
	}

	// ***
	
	private static final String WEAPON		= "weapon";
	private static final String ARMOR		= "armor";
	private static final String ARTIFACT   = "artifact";
	private static final String MISC       = "misc";
	private static final String RING       = "ring";

	private static final String SECOND_WEP = "second_wep";

	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		
		bundle.put( WEAPON, weapon );
		bundle.put( ARMOR, armor );
		bundle.put( ARTIFACT, artifact );
		bundle.put( MISC, misc );
		bundle.put( RING, ring );
		bundle.put( SECOND_WEP, secondWep );
	}


	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		if (bundle.contains( ARMOR )){
			Armor armor = ((Armor)bundle.get( ARMOR ));
			if (armor instanceof ClassArmor){
				info.armorTier = 6;
			} else {
				info.armorTier = armor.tier;
			}
		} else {
			info.armorTier = 0;
		}
	}

	//ignores lost inventory debuff
	public ArrayList<Bag> getBags(){
		ArrayList<Bag> result = new ArrayList<>();

		result.add(backpack);

		for (Item i : this){
			if (i instanceof Bag){
				result.add((Bag)i);
			}
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		boolean lostInvent = lostInventory();

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return (T) item;
				}
			}
		}
		
		return null;
	}

	public<T extends Item> ArrayList<T> getAllItems( Class<T> itemClass ) {
		ArrayList<T> result = new ArrayList<>();

		boolean lostInvent = lostInventory();

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					result.add((T) item);
				}
			}
		}

		return result;
	}
	
	public boolean contains( Item contains ){

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (contains == item) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Item getSimilar( Item similar ){

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (similar != item && similar.isSimilar(item)) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return item;
				}
			}
		}
		
		return null;
	}
	
	public ArrayList<Item> getAllSimilar( Item similar ){
		ArrayList<Item> result = new ArrayList<>();

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (item != similar && similar.isSimilar(item)) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					result.add(item);
				}
			}
		}
		
		return result;
	}

	//triggers when a run ends, so ignores lost inventory effects
	public void identify() {
		for (Item item : this) {
			item.identify(false);
		}
	}
	
	public void observe() {
		if (weapon() != null) {
			if (ShardOfOblivion.passiveIDDisabled() && weapon() instanceof Weapon){
				((Weapon) weapon()).setIDReady();
			} else {
				weapon().identify();
				Badges.validateItemLevelAquired(weapon());
			}
		}
		if (secondWep() != null){
			if (ShardOfOblivion.passiveIDDisabled() && secondWep() instanceof Weapon){
				((Weapon) secondWep()).setIDReady();
			} else {
				secondWep().identify();
				Badges.validateItemLevelAquired(secondWep());
			}
		}
		if (armor() != null) {
			if (ShardOfOblivion.passiveIDDisabled()){
				//armor().setIDReady();
			} else {
				armor().identify();
				Badges.validateItemLevelAquired(armor());
			}
		}
		if (artifact() != null) {
			//oblivion shard does not prevent artifact IDing
			artifact().identify();
			Badges.validateItemLevelAquired(artifact());
		}
		if (misc() != null) {
			if (ShardOfOblivion.passiveIDDisabled() && misc() instanceof Ring){
				((Ring) misc()).setIDReady();
			} else {
				misc().identify();
				Badges.validateItemLevelAquired(misc());
			}
		}
		if (ring() != null) {
			if (ShardOfOblivion.passiveIDDisabled()){
				//ring().setIDReady();
			} else {
				ring().identify();
				Badges.validateItemLevelAquired(ring());
			}
		}
		if (ShardOfOblivion.passiveIDDisabled()){
			GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready_worn"));
		}
		for (Item item : backpack) {
			if (item instanceof EquipableItem || item instanceof Wand) {
				item.cursedKnown = true;
			}
		}
		Item.updateQuickslot();
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, armor(), weapon(), artifact(), misc(), ring(), secondWep());
	}
	
	public Item randomUnequipped() {
		if (owner.buff(LostInventory.class) != null) return null;

		return Random.element( backpack.items );
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
			count++;
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weapon, armor, artifact, misc, ring, secondWep};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
			case 0:
				equipped[0] = weapon = null;
				break;
			case 1:
				equipped[1] = armor = null;
				break;
			case 2:
				equipped[2] = artifact = null;
				break;
			case 3:
				equipped[3] = misc = null;
				break;
			case 4:
				equipped[4] = ring = null;
				break;
			case 5:
				equipped[5] = secondWep = null;
				break;
			default:
				backpackIterator.remove();
			}
		}
	}

	private void putItemIntoSpecialSlot(final int slotID_network, CustomItem item) {
		int slotID = slotID_network;
		if (slotID < 0) {
			slotID = -slotID - 1; // special slots are indexed from -1 with negative numbers
		}
		switch (slotID) {
			case (0): {
				weapon = item;
				break;
			}
			case (1): {
				armor = item;
				break;
			}
			case (2):{
				artifact = item;
				break;
			}
			case (3): {
				misc = item;
				break;
			}
			case (4): {
				ring = item;
				break;
			}
			case (5): {
				// ?
			}
			default: {
				GLog.n("Unknown special slot id: " + slotID_network);
				break;
			}
		}
	}

	public boolean isEquipped(Item item){
		return item == weapon || item == armor || item == artifact || item == misc || item == ring || item == secondWep;
	};

	public void putItemIntoSlot(@NotNull List<Integer> slotPath, @Nullable Item item, boolean replace) {
		assert (slotPath != null) : "null item path";
		assert (slotPath.size() > 0) : "empty item path";
		if (slotPath.get(0) < 0) {
			assert (slotPath.size() == 1) : "can't put bag into special slot";
			putItemIntoSpecialSlot(slotPath.get(0), (CustomItem) item);
			return;
		}
		backpack.putItemIntoSlot(slotPath, item, replace);
		Item.updateQuickslot();
	}

	public Item getItemInSlot(@NotNull List<Integer> slotPath) {
		assert (slotPath != null) : "null item path";
		assert (slotPath.size() > 0) : "empty item path";
		if (slotPath.get(0) < 0) {
			int slotID = slotPath.get(0);
			if (slotID < 0) {
				slotID = -slotID - 1; // special slots are indexed from -1 with negative numbers
			}
			switch (slotID) {
				case (0): {
					return weapon;
				}
				 case (1): {
					return armor;
				}
				 case (2): {
					return artifact;
				}
				 case (3): {
					return misc;
				}
				 case (4): {
					return ring;
				}

				default:
					backpack.getItemInSlot(slotPath);
					throw new RuntimeException("Unexpected slot id " + slotID);
					//todo add more
			}
		}
		return backpack.getItemInSlot(slotPath);
	}

	public void removeItemFromSlot(@NotNull List<Integer> slotPath){
		assert (slotPath != null) : "null item path";
		assert (slotPath.size() > 0) : "empty item path";
		Item item = getItemInSlot(slotPath);
		if (slotPath.get(0)<0) {
			putItemIntoSlot(slotPath, null, true);
		} else {
			backpack.removeItemFromSlot(slotPath);
		}
		Dungeon.quickslot.clearItem(item);
		Item.updateQuickslot();
	}
	public void updateSpecialSlot(CustomItem item, int id){
		switch (id) {
			case 0: weapon = item; break;
			case 1: armor = item; break;
			case 2: artifact = item; break;
			case 3: misc = item; break;
			case 4: ring = item; break;
            default:
				Gdx.app.error("updateSpecialSlot", "Invalid slot: " + id);
        }
		Item.updateQuickslot();
	}
}
