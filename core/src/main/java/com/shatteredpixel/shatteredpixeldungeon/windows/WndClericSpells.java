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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.RightClickMenu;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PointF;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WndClericSpells extends Window {

	protected static final int WIDTH    = 120;

	public static int BTN_SIZE = 20;
	IconTitle title;
	ArrayList<IconButton> spellBtns = new ArrayList<>();
	int id;
	public WndClericSpells(int id, JSONObject object){
		this.id = id;
		boolean info = object.getBoolean("info");
		if (!info){
			title = new IconTitle(new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME), Messages.titleCase(Messages.get(this, "cast_title")));
		} else {
			title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
		}
		title.setRect(0, 0, WIDTH, 0);
		add(title);
		IconButton btnInfo = new IconButton(info ? new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME) : Icons.INFO.get()){
			@Override
			protected void onClick() {
				object.put("info", !info);
				GameScene.show(new WndClericSpells(id, object));
				hide();
			}
		};
		btnInfo.setRect(WIDTH-16, 0, 16, 16);
		add(btnInfo);

		RenderedTextBlock msg;
		if (info){
			msg = PixelScene.renderTextBlock( Messages.get( this, "info_desc"), 6);
		} else if (DeviceCompat.isDesktop()){
			msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_desktop"), 6);
		} else {
			msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_mobile"), 6);
		}
		msg.maxWidth(WIDTH);
		msg.setPos(0, title.bottom()+4);
		add(msg);

		int top = (int)msg.bottom()+4;
		JSONArray buttons = object.getJSONArray("buttons");
		for (int i = 1; i <= Talent.MAX_TALENT_TIERS; i++) {
			ArrayList<Integer> validIndexes = new ArrayList<>();
			for (int index = 0; index < buttons.length(); index++) {
				if (buttons.getJSONObject(index).getInt("tier") == i ){
					validIndexes.add(index);
				}
			}
			if (!validIndexes.isEmpty() && i != 1){
				top += BTN_SIZE + 2;
				ColorBlock sep = new ColorBlock(WIDTH, 1, 0xFF000000);
				sep.y = top;
				add(sep);
				top += 3;
			}
			int left = 2 + (WIDTH - validIndexes.size() * (BTN_SIZE + 4)) / 2;
			for (int spell : validIndexes) {
				SpellButton spellBtn = new SpellButton(buttons.getJSONObject(spell));
				spellBtn.setRect(left, top, BTN_SIZE, BTN_SIZE);
				left += spellBtn.width() + 4;
				add(spellBtn);
				spellBtns.add(spellBtn);

			}
		}

		resize(WIDTH, top + BTN_SIZE);
		if (SPDSettings.interfaceSize() != 2){
			offset(0, (int) (GameScene.uiCamera.height/2 - 30 - height/2));
		}

	}
	public WndClericSpells(HolyTome tome, Hero cleric, boolean info){

		if (!info){
			title = new IconTitle(new ItemSprite(tome), Messages.titleCase(Messages.get(this, "cast_title")));
		} else {
			title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
		}

		title.setRect(0, 0, WIDTH, 0);
		add(title);

		IconButton btnInfo = new IconButton(info ? new ItemSprite(tome) : Icons.INFO.get()){
			@Override
			protected void onClick() {
				GameScene.show(new WndClericSpells(tome, cleric, !info));
				hide();
			}
		};
		btnInfo.setRect(WIDTH-16, 0, 16, 16);
		add(btnInfo);

		RenderedTextBlock msg;
		if (info){
			msg = PixelScene.renderTextBlock( Messages.get( this, "info_desc"), 6);
		} else if (DeviceCompat.isDesktop()){
			msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_desktop"), 6);
		} else {
			msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_mobile"), 6);
		}
		msg.maxWidth(WIDTH);
		msg.setPos(0, title.bottom()+4);
		add(msg);

		int top = (int)msg.bottom()+4;

		for (int i = 1; i <= Talent.MAX_TALENT_TIERS; i++) {

			ArrayList<ClericSpell> spells = ClericSpell.getSpellList(cleric, i);

			if (!spells.isEmpty() && i != 1){
				top += BTN_SIZE + 2;
				ColorBlock sep = new ColorBlock(WIDTH, 1, 0xFF000000);
				sep.y = top;
				add(sep);
				top += 3;
			}


			for (ClericSpell spell : spells) {
				IconButton spellBtn = new SpellButton(spell, tome, info);
				add(spellBtn);
				spellBtns.add(spellBtn);
			}

			int left = 2 + (WIDTH - spellBtns.size() * (BTN_SIZE + 4)) / 2;
			for (IconButton btn : spellBtns) {
				btn.setRect(left, top, BTN_SIZE, BTN_SIZE);
				left += btn.width() + 4;
			}

		}

		resize(WIDTH, top + BTN_SIZE);

		//if we are on mobile, offset the window down to just above the toolbar
		if (SPDSettings.interfaceSize() != 2){
			offset(0, (int) (GameScene.uiCamera.height/2 - 30 - height/2));
		}

	}

	public class SpellButton extends IconButton {

		ClericSpell spell;
		HolyTome tome;
		boolean info;

		NinePatch bg;
		int spellID = -1;
		String spellName;
		String spellShortDesc;
		public SpellButton(JSONObject object) {
			super(new HeroIcon(new ClericSpell() {
				@Override
				public void onCast(HolyTome tome, Hero hero) {

				}

				@Override
				public int icon() {
				return 	object.getInt("icon");
				}
			}));
			info = object.getBoolean("info");
			icon.alpha((float) object.getDouble("alpha"));
			spellName = object.getString("spell_name");
			spellShortDesc = object.getString("spell_short_desc");
			spellID = object.getInt("spell_id");
			bg = Chrome.get(Chrome.Type.TOAST);
			addToBack(bg);
		}
		public SpellButton(ClericSpell spell, HolyTome tome, boolean info){
			super(new HeroIcon(spell));

			this.spell = spell;
			this.tome = tome;
			this.info = info;
			if (!tome.canCast(Dungeon.hero, spell)){
				icon.alpha( 0.3f );
			}

			bg = Chrome.get(Chrome.Type.TOAST);
			addToBack(bg);
		}

		@Override
		protected void onPointerUp() {
			super.onPointerUp();
			if (tome != null) {
				if (!tome.canCast(Dungeon.hero, spell)) {
					icon.alpha(0.3f);
				}
			}
		}

		@Override
		protected void layout() {
			super.layout();

			if (bg != null) {
				bg.size(width, height);
				bg.x = x;
				bg.y = y;
			}
		}

		@Override
		protected void onClick() {
			if (!info) {
				hide();
			}
			SendData.sendWindowResult(WndClericSpells.this.id, spellID, new JSONObject().put("info", info));
		}
		@Override
		protected String hoverText() {
			return "_" + Messages.titleCase(spellName) + "_\n" + spellShortDesc;
		}
	}

}
