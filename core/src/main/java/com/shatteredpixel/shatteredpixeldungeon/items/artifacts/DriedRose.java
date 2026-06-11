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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.items.CustomItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import org.json.JSONObject;

import java.util.ArrayList;

public class DriedRose extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ROSE1;

		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_SUMMON;
	}

	private boolean talkedTo = false;
	private boolean firstSummon = false;

	private MeleeWeapon weapon = null;
	private Armor armor = null;

	public int droppedPetals = 0;

	public static final String AC_SUMMON = "SUMMON";
	public static final String AC_DIRECT = "DIRECT";
	public static final String AC_OUTFIT = "OUTFIT";

	@Override
	public String defaultAction() {
        return AC_SUMMON;
    }

	public int ghostStrength(){
		return 13 + level()/2;
	}

	@Override
	public String desc() {
		if ((ShatteredPixelDungeon.scene() instanceof GameScene || ShatteredPixelDungeon.scene() instanceof AlchemyScene)){
			return Messages.get(this, "desc_no_quest");
		}
		
		String desc = super.desc();

		if (isEquipped( Dungeon.hero )){
			if (!cursed){

				if (level() < levelCap)
					desc+= "\n\n" + Messages.get(this, "desc_hint");

			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			}
		}

		if (weapon != null || armor != null) {
			desc += "\n";

			if (weapon != null) {
				desc += "\n" + Messages.get(this, "desc_weapon", Messages.titleCase(weapon.title()));
			}

			if (armor != null) {
				desc += "\n" + Messages.get(this, "desc_armor", Messages.titleCase(armor.title()));
			}

			desc += "\n" + Messages.get(this, "desc_strength", ghostStrength());

		}
		
		return desc;
	}
	
	@Override
	public int value() {
		if (weapon != null){
			return -1;
		}
		if (armor != null){
			return -1;
		}
		return super.value();
	}


	@Override
	public Item upgrade() {
		if (level() >= 9)
			image = ItemSpriteSheet.ARTIFACT_ROSE3;
		else if (level() >= 4)
			image = ItemSpriteSheet.ARTIFACT_ROSE2;

		//For upgrade transferring via well of transmutation
		droppedPetals = Math.max( level(), droppedPetals );

        return super.upgrade();
	}
	
	public Weapon ghostWeapon(){
		return weapon;
	}
	
	public Armor ghostArmor(){
		return armor;
	}

	private static final String TALKEDTO =      "talkedto";
	private static final String FIRSTSUMMON =   "firstsummon";
	private static final String GHOSTID =       "ghostID";
	private static final String PETALS =        "petals";
	
	private static final String WEAPON =        "weapon";
	private static final String ARMOR =         "armor";

	public class roseRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			
			spend( TICK );
			
			if (false){
			}

            //rose does not charge while ghost hero is alive

            if (charge < chargeCap
                    && !cursed
                    && Regeneration.regenOn()) {
				//500 turns to a full charge
				partialCharge += (1/5f * RingOfEnergy.artifactChargeMultiplier(target));
				while (partialCharge > 1){
					charge++;
					partialCharge--;
					if (charge == chargeCap){
						partialCharge = 0f;
						GLog.p( Messages.get(DriedRose.class, "charged") );
					}
				}
			} else if (cursed && Random.Int(100) == 0) {

				ArrayList<Integer> spawnPoints = new ArrayList<>();

				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = target.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (!spawnPoints.isEmpty()) {
                    Random.element(spawnPoints);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}

			}

			updateQuickslot();

			return true;
		}
	}

	public static class WndGhostHero extends Window{
		
		private static final int BTN_SIZE	= 32;
		private static final float GAP		= 2;
		private static final float BTN_GAP	= 12;
		private static final int WIDTH		= 116;
		
		private final ItemButton btnWeapon;
		private final ItemButton btnArmor;
		
		public WndGhostHero(JSONObject obj){
			final JSONObject args = obj.getJSONObject("args");
			final int id = obj.getInt("id");
			final CustomItem weapon = args.has("weapon") && !args.isNull("weapon") ? CustomItem.createItem(args.getJSONObject("weapon")) : null;
			final CustomItem armor = args.has("armor") && !args.isNull("armor") ? CustomItem.createItem(args.getJSONObject("armor")) : null;
			final CustomItem rose = CustomItem.createItem(args.getJSONObject("rose"));
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(rose) );
			titlebar.label( JsonStringHelper.getString(args, "title") );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock message =
					PixelScene.renderTextBlock(JsonStringHelper.getString(args, "message"), 6);
			message.maxWidth( WIDTH );
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );
			
			btnWeapon = new ItemButton(){
				@Override
				protected void onClick() {
					SendData.sendWindowResult(id, 0);
					hide();
				}

				@Override
				protected boolean onLongClick() {
					if (item() != null && item().name() != null){
						GameScene.show(new WndInfoItem(item()));
						return true;
					}
					return false;
				}
			};
			btnWeapon.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE );
			if (weapon != null) {
				btnWeapon.item(weapon);
			} else {
				btnWeapon.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
			}
			add( btnWeapon );
			
			btnArmor = new ItemButton(){
				@Override
				protected void onClick() {
					SendData.sendWindowResult(id, 1);
					hide();
				}

				@Override
				protected boolean onLongClick() {
					if (item() != null && item().name() != null){
						GameScene.show(new WndInfoItem(item()));
						return true;
					}
					return false;
				}
			};
			btnArmor.setRect( btnWeapon.right() + BTN_GAP, btnWeapon.top(), BTN_SIZE, BTN_SIZE );
			if (armor != null) {
				btnArmor.item(armor);
			} else {
				btnArmor.item(new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
			}
			add( btnArmor );
			
			resize(WIDTH, (int)(btnArmor.bottom() + GAP));
		}
	
	}
}
