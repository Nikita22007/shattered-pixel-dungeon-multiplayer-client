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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TrinketCatalyst extends Item {

	{
		image = ItemSpriteSheet.TRINKET_CATA;

		unique = true;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	private ArrayList<Trinket> rolledTrinkets = new ArrayList<>();

	public boolean hasRolledTrinkets(){
		return !rolledTrinkets.isEmpty();
	}

	public static class Recipe {

	}

	public static class WndTrinket extends Window {

		private static final int WIDTH		= 120;
		private static final int BTN_SIZE	= 24;
		private static final int BTN_GAP	= 4;
		private static final int GAP		= 2;

		private static final int NUM_TRINKETS = 4;

		public WndTrinket( TrinketCatalyst cata ){

			IconTitle titlebar = new IconTitle();
			titlebar.icon(new ItemSprite(cata));
			titlebar.label(Messages.titleCase(Messages.get(TrinketCatalyst.class, "window_title")));
			titlebar.setRect(0, 0, WIDTH, 0);
			add( titlebar );

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(TrinketCatalyst.class, "window_text"), 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			//roll new trinkets if trinkets were not already rolled
			while (cata.rolledTrinkets.size() < NUM_TRINKETS){
                cata.rolledTrinkets.add((Trinket) null);
			}

			for (int i = 0; i < NUM_TRINKETS; i++){
				ItemButton btnReward = new ItemButton() {
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new RewardWindow(item()));
					}
				};
				btnReward.item(cata.rolledTrinkets.get(i));
				btnReward.setRect( (i+1)*(WIDTH - BTN_GAP) / NUM_TRINKETS - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
				add( btnReward );

			}

			resize(WIDTH, (int)(message.top() + message.height() + 2*BTN_GAP + BTN_SIZE));

		}

		@Override
		public void onBackPressed() {
			//do nothing
		}

		private class RewardWindow extends WndInfoItem {

			public RewardWindow( Item item ) {
				super(item);

				RedButton btnConfirm = new RedButton(Messages.get(WndSadGhost.class, "confirm")){
					@Override
					protected void onClick() {
						RewardWindow.this.hide();
						WndTrinket.this.hide();

						Item result = item;

                        TrinketCatalyst cata = null;

						if (cata != null) {
							if (ShatteredPixelDungeon.scene() instanceof AlchemyScene) {

							} else {
								Sample.INSTANCE.play( Assets.Sounds.PUFF );

                                if (false){
									GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", item.name())) );
								} else {
									Dungeon.level.drop(result, Dungeon.hero.pos);
								}

								Statistics.itemsCrafted++;
								Badges.validateItemsCrafted();

							}
						}
					}
				};
				btnConfirm.setRect(0, height+2, width/2-1, 16);
				add(btnConfirm);

				RedButton btnCancel = new RedButton(Messages.get(WndSadGhost.class, "cancel")){
					@Override
					protected void onClick() {
						hide();
					}
				};
				btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
				add(btnCancel);

				resize(width, (int)btnCancel.bottom());
			}
		}

	}
}
