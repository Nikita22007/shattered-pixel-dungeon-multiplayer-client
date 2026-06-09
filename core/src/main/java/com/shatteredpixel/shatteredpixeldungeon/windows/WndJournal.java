/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.RemoteJournal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CustomCharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.input.PointerEvent;

import java.util.ArrayList;

public class WndJournal extends WndTabbed {

	public static final int WIDTH_P     = 126;
	public static final int HEIGHT_P    = 180;

	public static final int WIDTH_L     = 216;
	public static final int HEIGHT_L    = 130;

	private static final int ITEM_HEIGHT	= 18;

	private GuideTab guideTab;
	private AlchemyTab alchemyTab;
	private NotesTab notesTab;
	private CatalogTab catalogTab;
	private BadgesTab badgesTab;
	private boolean journalDirty;

	public static int last_index = 0;

	private static WndJournal INSTANCE = null;

	public static void refreshNotes() {
		if (INSTANCE != null) {
			INSTANCE.journalDirty = true;
		}
	}

	public WndJournal(){

		if (INSTANCE != null){
			INSTANCE.hide();
		}

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
		int height = PixelScene.landscape() ? HEIGHT_L : HEIGHT_P;

		resize(width, height);

		guideTab = new GuideTab();
		add(guideTab);
		guideTab.setRect(0, 0, width, height);
		guideTab.updateList();

		alchemyTab = new AlchemyTab();
		add(alchemyTab);
		alchemyTab.setRect(0, 0, width, height);

		notesTab = new NotesTab();
		add(notesTab);
		notesTab.setRect(0, 0, width, height);
		notesTab.updateList();

		catalogTab = new CatalogTab();
		add(catalogTab);
		catalogTab.setRect(0, 0, width, height);
		catalogTab.updateList();

		badgesTab = new BadgesTab();
		add(badgesTab);
		badgesTab.setRect(0, 0, width, height);
		badgesTab.updateList();

		Tab[] tabs = {
				new IconTab( Icons.JOURNAL.get() ) {
					protected void select( boolean value ) {
						super.select( value );
						notesTab.active = notesTab.visible = value;
						if (value) last_index = 0;
					}

					@Override
					protected String hoverText() {
						return Messages.get(notesTab, "title");
					}
				},
				new IconTab( new ItemSprite(ItemSpriteSheet.MASTERY, null) ) {
					protected void select( boolean value ) {
						super.select( value );
						guideTab.active = guideTab.visible = value;
						if (value) last_index = 1;
					}

					@Override
					protected String hoverText() {
						return Messages.get(guideTab, "title");
					}
				},
				new IconTab( Icons.ALCHEMY.get() ) {
					protected void select( boolean value ) {
						super.select( value );
						alchemyTab.active = alchemyTab.visible = value;
						if (value) last_index = 2;
					}

					@Override
					protected String hoverText() {
						return Messages.get(alchemyTab, "title");
					}
				},
				new IconTab( Icons.CATALOG.get() ) {
					protected void select( boolean value ) {
						super.select( value );
						catalogTab.active = catalogTab.visible = value;
						if (value) last_index = 3;
					}

					@Override
					protected String hoverText() {
						return Messages.get(catalogTab, "title");
					}
				},
				new IconTab( Icons.BADGES.get() ) {
					protected void select( boolean value ) {
						super.select( value );
						badgesTab.active = badgesTab.visible = value;
						if (value) last_index = 4;
					}

					@Override
					protected String hoverText() {
						return Messages.get(badgesTab, "title");
					}
				}
		};

		for (Tab tab : tabs) {
			add( tab );
		}

		layoutTabs();

		select(last_index);

		INSTANCE = this;
	}

	@Override
	public synchronized void update() {
		super.update();
		if (journalDirty) {
			journalDirty = false;
			INSTANCE = null;
			hide();
			WndJournal replacement = new WndJournal();
			if (ShatteredPixelDungeon.scene() instanceof GameScene) {
				GameScene.show(replacement);
			} else {
				ShatteredPixelDungeon.scene().addToFront(replacement);
			}
		}
	}

	@Override
	public boolean onSignal(KeyEvent event) {
		if (event.pressed && KeyBindings.getActionForKey( event ) == SPDAction.JOURNAL) {
			onBackPressed();
			return true;
		} else {
			return super.onSignal(event);
		}
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		guideTab.layout();
		alchemyTab.layout();
		notesTab.layout();
		catalogTab.layout();
	}

	public static class GuideTab extends Component {

		private ScrollingListPane list;

		@Override
		protected void createChildren() {
			list = new ScrollingListPane();
			add( list );
		}

		@Override
		protected void layout() {
			super.layout();
			list.setRect( x, y, width, height);
		}

		public void updateList(){
			list.clear();
			RemoteJournal.Tab tab = RemoteJournal.findTab("guide");
			if (tab == null) {
				list.addTitle(Messages.get(this, "title"));
				list.setRect(x, y, width, height);
				return;
			}
			for (RemoteJournal.Entry entry : tab.entries){
				if ("header".equals(entry.kind)) {
					list.addTitle(entry.title.resolve());
					continue;
				}
				ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(
						remoteIcon(entry.icon),
						null,
						entry.title.resolve()
				){
					@Override
					public boolean onClick(float x, float y) {
						if (inside( x, y ) && entry.enabled) {
							ShatteredPixelDungeon.scene().addToFront( new WndStory( remoteIcon(entry.icon),
									entry.title.resolve(),
									entry.body.resolve() ));
							return true;
						} else {
							return false;
						}
					}
				};
				if (!entry.seen || !entry.enabled){
					item.hardlight(0x999999);
					item.hardlightIcon(0x999999);
				}
				list.addItem(item);
			}

			list.setRect(x, y, width, height);
		}

	}

	public static class AlchemyTab extends Component {

		private RedButton[] pageButtons;
		private static final int NUM_BUTTONS = 9;

		public static int currentPageIdx   = 0;

		private IconTitle title;
		private RenderedTextBlock body;

		private ScrollPane list;
		private ArrayList<QuickRecipe> recipes = new ArrayList<>();

		@Override
		protected void createChildren() {
			RemoteJournal.Tab tab = RemoteJournal.findTab("alchemy");
			pageButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				pageButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentPageIdx = idx;
						updateList();
					}
				};
				RemoteJournal.Entry entry = entryAt(tab, i);
				if (entry != null) {
					pageButtons[i].icon(remoteIcon(entry.icon));
					pageButtons[i].enable(entry.enabled);
				} else {
					pageButtons[i].icon(new ItemSprite(ItemSpriteSheet.SOMETHING, null));
					pageButtons[i].enable(false);
				}
				add( pageButtons[i] );
			}

			title = new IconTitle();
			title.icon( new ItemSprite(ItemSpriteSheet.ALCH_PAGE));
			title.visible = false;

			body = PixelScene.renderTextBlock(6);

			list = new ScrollPane(new Component());
			add(list);
		}

		@Override
		protected void layout() {
			super.layout();

			if (width() >= 180){
				float buttonWidth = width()/pageButtons.length;
				for (int i = 0; i < NUM_BUTTONS; i++) {
					pageButtons[i].setRect(x + i*buttonWidth, y, buttonWidth, ITEM_HEIGHT);
					PixelScene.align(pageButtons[i]);
				}
			} else {
				//for first row
				float buttonWidth = width()/5;
				float y = 0;
				float x = 0;
				for (int i = 0; i < NUM_BUTTONS; i++) {
					pageButtons[i].setRect(this.x + x, this.y + y, buttonWidth, ITEM_HEIGHT);
					PixelScene.align(pageButtons[i]);
					x += buttonWidth;
					if (i == 4){
						y += ITEM_HEIGHT;
						x = 0;
						buttonWidth = width()/4;
					}
				}
			}

			list.setRect(x, pageButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - pageButtons[NUM_BUTTONS-1].bottom() + y - 1);

			updateList();
		}

		public void updateList() {
			RemoteJournal.Tab tab = RemoteJournal.findTab("alchemy");

			if (currentPageIdx != -1) {
				RemoteJournal.Entry selected = entryAt(tab, currentPageIdx);
				if (selected == null || !selected.enabled) {
					currentPageIdx = firstEnabledEntry(tab);
				}
			}
			if (currentPageIdx == -1){
				currentPageIdx = -1;
			}

			for (int i = 0; i < NUM_BUTTONS; i++) {
				RemoteJournal.Entry entry = entryAt(tab, i);
				pageButtons[i].enable(entry != null && entry.enabled);
				if (entry != null) {
					pageButtons[i].icon(remoteIcon(entry.icon));
				}
				if (i == currentPageIdx) {
					pageButtons[i].icon().color(TITLE_COLOR);
				} else {
					pageButtons[i].icon().resetColor();
				}
			}

			if (currentPageIdx == -1){
				return;
			}

			RemoteJournal.Entry page = entryAt(tab, currentPageIdx);
			if (page == null) {
				return;
			}

			for (QuickRecipe r : recipes){
				if (r != null) {
					r.killAndErase();
					r.destroy();
				}
			}
			recipes.clear();

			Component content = list.content();

			content.clear();

			title.visible = true;
			title.icon(remoteIcon(page.titleIcon));
			title.label(page.title.resolve());
			title.setRect(0, 0, width(), 10);
			content.add(title);

			body.maxWidth((int)width());
			body.text(page.body.resolve());
			body.setPos(0, title.bottom());
			content.add(body);

			ArrayList<QuickRecipe> toAdd = new ArrayList<>();
			for (RemoteJournal.RemoteRecipe rr : page.recipes) {
				if (rr == null) {
					toAdd.add(null);
				} else {
					toAdd.add(new QuickRecipe(rr.ingredients, rr.output, rr.cost));
				}
			}

			float left;
			float top = body.bottom()+2;
			int w;
			ArrayList<QuickRecipe> toAddThisRow = new ArrayList<>();
			while (!toAdd.isEmpty()){
				if (toAdd.get(0) == null){
					toAdd.remove(0);
					top += 6;
				}
				
				w = 0;
				while(!toAdd.isEmpty() && toAdd.get(0) != null
						&& w + toAdd.get(0).width() <= width()){
					toAddThisRow.add(toAdd.remove(0));
					w += toAddThisRow.get(0).width();
				}
				
				float spacing = (width() - w)/(toAddThisRow.size() + 1);
				left = spacing;
				while (!toAddThisRow.isEmpty()){
					QuickRecipe r = toAddThisRow.remove(0);
					r.setPos(left, top);
					left += r.width() + spacing;
					if (!toAddThisRow.isEmpty()) {
						ColorBlock spacer = new ColorBlock(1, 16, 0xFF222222);
						spacer.y = top;
						spacer.x = left - spacing / 2 - 0.5f;
						PixelScene.align(spacer);
						content.add(spacer);
					}
					recipes.add(r);
					content.add(r);
				}
				
				if (!toAdd.isEmpty() && toAdd.get(0) == null){
					toAdd.remove(0);
				}
				
				if (!toAdd.isEmpty() && toAdd.get(0) != null) {
					ColorBlock spacer = new ColorBlock(width(), 1, 0xFF222222);
					spacer.y = top + 16;
					spacer.x = 0;
					content.add(spacer);
				}
				top += 17;
				toAddThisRow.clear();
			}

			content.setSize(width(), top);
			list.setSize(list.width(), list.height());
			list.scrollTo(0, 0);
		}
	}

	private static class NotesTab extends Component {

		private ScrollingGridPane grid;

		@Override
		protected void createChildren() {
			grid = new ScrollingGridPane();
			add(grid);
		}

		@Override
		protected void layout() {
			super.layout();
			grid.setRect( x, y, width, height);
		}

		private void updateList(){
			grid.clear();
			addGridEntries(grid, RemoteJournal.findTab("notes"));
			grid.setRect(x, y, width, height);

		}

	}

	public static class CatalogTab extends Component{

		private RedButton[] itemButtons;
		private static final int NUM_BUTTONS = 4;

		public static int currentItemIdx   = 0;
		private static float[] scrollPositions = new float[NUM_BUTTONS];

		//sprite locations
		private static final int EQUIP_IDX = 0;
		private static final int CONSUM_IDX = 1;
		private static final int BESTIARY_IDX = 2;
		private static final int LORE_IDX = 3;

		private ScrollingGridPane grid;

		@Override
		protected void createChildren() {
			itemButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				itemButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentItemIdx = idx;
						updateList();
					}
				};
				add( itemButtons[i] );
			}
			itemButtons[EQUIP_IDX].icon(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER));
			itemButtons[CONSUM_IDX].icon(new ItemSprite(ItemSpriteSheet.POTION_HOLDER));
			itemButtons[BESTIARY_IDX].icon(new ItemSprite(ItemSpriteSheet.MOB_HOLDER));
			itemButtons[LORE_IDX].icon(new ItemSprite(ItemSpriteSheet.DOCUMENT_HOLDER));

			grid = new ScrollingGridPane(){
				@Override
				public synchronized void update() {
					super.update();
					scrollPositions[currentItemIdx] = content.camera.scroll.y;
				}
			};
			add( grid );
		}

		@Override
		protected void layout() {
			super.layout();

			int perRow = NUM_BUTTONS;
			float buttonWidth = width()/perRow;

			for (int i = 0; i < NUM_BUTTONS; i++) {
				itemButtons[i].setRect(x +(i%perRow) * (buttonWidth),
						y + (i/perRow) * (ITEM_HEIGHT ),
						buttonWidth, ITEM_HEIGHT);
				PixelScene.align(itemButtons[i]);
			}

			grid.setRect(x,
					itemButtons[NUM_BUTTONS-1].bottom() + 1,
					width,
					height - itemButtons[NUM_BUTTONS-1].height() - 1);
		}

		public void updateList() {

			grid.clear();
			RemoteJournal.Tab tab = RemoteJournal.findTab("catalog");

			for (int i = 0; i < NUM_BUTTONS; i++){
				if (i == currentItemIdx){
					itemButtons[i].icon().color(TITLE_COLOR);
				} else {
					itemButtons[i].icon().resetColor();
				}
			}

			grid.scrollTo( 0, 0 );

			addGridEntries(grid, childTabAt(tab, currentItemIdx));

			grid.setRect(x, itemButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - itemButtons[NUM_BUTTONS-1].height() - 1);

			grid.scrollTo(0, scrollPositions[currentItemIdx]);
		}

	}

	private static RemoteJournal.Entry entryAt(RemoteJournal.Tab tab, int index) {
		if (tab == null || index < 0 || index >= tab.entries.size()) {
			return null;
		}
		return tab.entries.get(index);
	}

	private static int firstEnabledEntry(RemoteJournal.Tab tab) {
		if (tab == null) {
			return -1;
		}
		for (int i = 0; i < tab.entries.size(); i++) {
			if (tab.entries.get(i).enabled) {
				return i;
			}
		}
		return -1;
	}

	private static RemoteJournal.Tab childTabAt(RemoteJournal.Tab tab, int index) {
		if (tab == null || index < 0 || index >= tab.tabs.size()) {
			return null;
		}
		return tab.tabs.get(index);
	}

	private static void addGridEntries(ScrollingGridPane grid, RemoteJournal.Tab tab) {
		grid.clear();
		if (tab == null) {
			return;
		}
		for (RemoteJournal.Entry entry : tab.entries) {
			if ("header".equals(entry.kind)) {
				grid.addHeader(entry.title.resolve(), entry.headerSize, entry.headerCenter);
				continue;
			}
			Image icon = remoteIcon(entry.icon);
			ScrollingGridPane.GridItem gridItem = new ScrollingGridPane.GridItem(icon) {
				@Override
				public boolean onClick(float x, float y) {
					if (inside(x, y) && entry.enabled) {
						Image image = remoteIcon(entry.icon);
						if ("story".equals(entry.kind)) {
							if (ShatteredPixelDungeon.scene() instanceof GameScene) {
								GameScene.show(new WndStory(image, entry.title.resolve(), entry.body.resolve()));
							} else {
								ShatteredPixelDungeon.scene().addToFront(new WndStory(image, entry.title.resolve(), entry.body.resolve()));
							}
						} else if (ShatteredPixelDungeon.scene() instanceof GameScene) {
							GameScene.show(new WndJournalItem(image, entry.title.resolve(), entry.body.resolve()));
						} else {
							ShatteredPixelDungeon.scene().addToFront(new WndJournalItem(image, entry.title.resolve(), entry.body.resolve()));
						}
						return true;
					} else {
						return false;
					}
				}
			};
			if (entry.secondIcon != null) {
				Visual secondIcon = remoteVisual(entry.secondIcon);
				if (secondIcon != null) {
					gridItem.addSecondIcon(secondIcon);
				}
			}
			if (!entry.seen || !entry.read) {
				gridItem.hardLightBG(entry.seen ? 0.6f : 2f, 1f, 2f);
			}
			grid.addItem(gridItem);
		}
	}

	public static class BadgesTab extends Component {

		private RedButton btnLocal;
		private RedButton btnGlobal;

		private RenderedTextBlock title;

		private Component badgesLocal;
		private Component badgesGlobal;

		public static boolean global = false;

		@Override
		protected void createChildren() {

			if (Dungeon.hero != null) {
				btnLocal = new RedButton(Messages.get(this, "this_run")) {
					@Override
					protected void onClick() {
						super.onClick();
						global = false;
						updateList();
					}
				};
				btnLocal.icon(Icons.BADGES.get());
				add(btnLocal);

				btnGlobal = new RedButton(Messages.get(this, "overall")) {
					@Override
					protected void onClick() {
						super.onClick();
						global = true;
						updateList();
					}
				};
				btnGlobal.icon(Icons.BADGES.get());
				add(btnGlobal);

				badgesLocal = remoteBadgesComponent(childTabAt(RemoteJournal.findTab("badges"), 0), true);
				add( badgesLocal );
			} else {
				title = PixelScene.renderTextBlock(Messages.get(this, "title_main_menu"), 9);
				title.hardlight(Window.TITLE_COLOR);
				add(title);
			}

			badgesGlobal = remoteBadgesComponent(childTabAt(RemoteJournal.findTab("badges"), 1), false);
			add( badgesGlobal );
		}

		@Override
		protected void layout() {
			super.layout();

			if (btnLocal != null) {
				btnLocal.setRect(x, y, width / 2, 18);
				btnGlobal.setRect(x + width / 2, y, width / 2, 18);

				badgesLocal.setRect(x, y + 20, width, height-20);
				badgesGlobal.setRect( x, y + 20, width, height-20);
			} else {
				title.setPos( x + (width - title.width())/2, y + (12-title.height())/2);

				badgesGlobal.setRect( x, y + 14, width, height-14);
			}
		}

		private void updateList(){
			if (btnLocal != null) {
				badgesLocal.visible = badgesLocal.active = !global;
				badgesGlobal.visible = badgesGlobal.active = global;

				btnLocal.textColor(global ? Window.WHITE : Window.TITLE_COLOR);
				btnGlobal.textColor(global ? Window.TITLE_COLOR : Window.WHITE);
			} else {
				badgesGlobal.visible = badgesGlobal.active = true;
			}
		}

	}

	private static Component remoteBadgesComponent(RemoteJournal.Tab tab, boolean local) {
		if (local && tab != null && tab.entries.size() <= 8) {
			return new RemoteBadgesList(tab);
		}
		return new RemoteBadgesGrid(tab);
	}

	private static class RemoteBadgesGrid extends Component {

		private final ArrayList<RemoteBadgeButton> badgeButtons = new ArrayList<>();

		private RemoteBadgesGrid(RemoteJournal.Tab tab) {
			super();
			if (tab == null) {
				return;
			}
			for (RemoteJournal.Entry entry : tab.entries) {
				if (!"header".equals(entry.kind)) {
					RemoteBadgeButton button = new RemoteBadgeButton(entry);
					add(button);
					badgeButtons.add(button);
				}
			}
		}

		@Override
		protected void layout() {
			super.layout();
			if (badgeButtons.isEmpty()) {
				return;
			}
			float badgeArea = (float) Math.sqrt(width * height / badgeButtons.size());
			int nCols = Math.max(1, Math.round(width / badgeArea));
			int nRows = (int) Math.ceil(badgeButtons.size() / (float) nCols);

			float badgeWidth = width() / nCols;
			float badgeHeight = height() / nRows;

			for (int i = 0; i < badgeButtons.size(); i++) {
				int row = i / nCols;
				int col = i % nCols;
				RemoteBadgeButton button = badgeButtons.get(i);
				button.setPos(
						left() + col * badgeWidth + (badgeWidth - button.width()) / 2,
						top() + row * badgeHeight + (badgeHeight - button.height()) / 2);
				PixelScene.align(button);
			}
		}
	}

	private static class RemoteBadgesList extends ScrollPane {

		private final ArrayList<ListItem> items = new ArrayList<>();

		private RemoteBadgesList(RemoteJournal.Tab tab) {
			super(new Component());
			if (tab == null) {
				return;
			}
			for (RemoteJournal.Entry entry : tab.entries) {
				if (!"header".equals(entry.kind)) {
					ListItem item = new ListItem(entry);
					content.add(item);
					items.add(item);
				}
			}
		}

		@Override
		protected void layout() {
			float pos = 0;
			for (ListItem item : items) {
				item.setRect(0, pos, width, ListItem.HEIGHT);
				pos += ListItem.HEIGHT;
			}
			content.setSize(width, pos);
			super.layout();
		}

		@Override
		public void onClick(float x, float y) {
			for (ListItem item : items) {
				if (item.onClick(x, y)) {
					break;
				}
			}
		}

		private static class ListItem extends Component {

			private static final float HEIGHT = 18;

			private final RemoteJournal.Entry entry;
			private Image icon;
			private RenderedTextBlock label;

			private ListItem(RemoteJournal.Entry entry) {
				super();
				this.entry = entry;
				icon.copy(remoteIcon(entry.icon));
				label.text(entry.title.resolve());
			}

			@Override
			protected void createChildren() {
				icon = new Image();
				add(icon);

				label = PixelScene.renderTextBlock(6);
				add(label);
			}

			@Override
			protected void layout() {
				icon.x = x;
				icon.y = y + (height - icon.height) / 2;
				PixelScene.align(icon);

				label.setPos(
						icon.x + icon.width + 2,
						y + (height - label.height()) / 2
				);
				PixelScene.align(label);
			}

			private boolean onClick(float x, float y) {
				if (inside(x, y)) {
					Sample.INSTANCE.play(Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f);
					Game.scene().addToFront(new RemoteBadgeWindow(entry));
					return true;
				}
				return false;
			}
		}
	}

	private static class RemoteBadgeButton extends Button {

		private final RemoteJournal.Entry entry;
		private Image icon;

		private RemoteBadgeButton(RemoteJournal.Entry entry) {
			super();
			this.entry = entry;

			icon = remoteIcon(entry.icon);
			if (!entry.seen) {
				icon.brightness(0.4f);
			}
			add(icon);
			setSize(icon.width(), icon.height());
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
		}

		@Override
		protected void onClick() {
			Sample.INSTANCE.play(Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f);
			Game.scene().addToFront(new RemoteBadgeWindow(entry));
		}

		@Override
		protected String hoverText() {
			return entry.title.resolve();
		}
	}

	private static class RemoteBadgeWindow extends Window {

		private static final int MAX_WIDTH = 125;
		private static final int MARGIN = 4;

		private RemoteBadgeWindow(RemoteJournal.Entry entry) {
			super();

			Image icon = remoteIcon(entry.icon);
			icon.scale.set(2);
			if (!entry.seen) {
				icon.brightness(0.4f);
			}
			add(icon);

			RenderedTextBlock title = PixelScene.renderTextBlock(entry.title.resolve(), 9);
			title.maxWidth(MAX_WIDTH - MARGIN * 2);
			title.align(RenderedTextBlock.CENTER_ALIGN);
			title.hardlight(entry.seen ? TITLE_COLOR : 0x888822);
			add(title);

			RenderedTextBlock info = PixelScene.renderTextBlock(entry.body.resolve(), 6);
			info.maxWidth(MAX_WIDTH - MARGIN * 2);
			info.align(RenderedTextBlock.CENTER_ALIGN);
			if (!entry.seen) {
				info.hardlight(0x888888);
				info.setHightlighting(true, 0x888822);
			}
			add(info);

			float w = Math.max(icon.width(), Math.max(title.width(), info.width())) + MARGIN * 2;

			icon.x = (w - icon.width()) / 2f;
			icon.y = MARGIN;
			PixelScene.align(icon);

			title.setPos((w - title.width()) / 2, icon.y + icon.height() + MARGIN);
			PixelScene.align(title);

			info.setPos((w - info.width()) / 2, title.bottom() + MARGIN);
			PixelScene.align(info);
			resize((int) w, (int) (info.bottom() + MARGIN));

			if (entry.seen) {
				BadgeBanner.highlight(icon, entry.icon.image);
			}

			PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
				@Override
				protected void onClick(PointerEvent event) {
					onBackPressed();
				}
			};
			blocker.camera = PixelScene.uiCamera;
			add(blocker);
		}
	}

	private static Visual remoteVisual(RemoteJournal.Icon icon) {
		if ("text".equals(icon.type)) {
			BitmapText text = new BitmapText(icon.text.resolve(), PixelScene.pixelFont);
			text.measure();
			return text;
		}
		return remoteIcon(icon);
	}

	private static Image remoteIcon(RemoteJournal.Icon icon) {
		Image image;
		if ("item".equals(icon.type)) {
			if (icon.spriteSheet != null) {
				image = new ItemSprite().view(icon.spriteSheet, icon.image, null);
			} else {
				image = new ItemSprite(icon.image == -1 ? ItemSpriteSheet.SOMETHING : icon.image, null);
			}
		} else if ("char_sprite".equals(icon.type)) {
			if (icon.spriteAsset != null) {
				image = new CustomCharSprite(icon.spriteAsset);
			} else {
				Class<? extends CharSprite> spriteClass = CharSprite.spriteClassFromName(icon.name, true);
				CharSprite sprite = spriteClass == null ? null : CharSprite.spriteFromClass(spriteClass);
				if (sprite != null) {
					sprite.idle();
					image = new Image(sprite);
				} else {
					image = Icons.WARNING.get();
				}
			}
		} else if ("terrain_feature".equals(icon.type)) {
			image = new Image(Assets.Environment.TERRAIN_FEATURES);
			TextureFilm film = new TextureFilm(image.texture, DungeonTilemap.SIZE, DungeonTilemap.SIZE);
			image.frame(film.get(icon.terrainFeature));
		} else if ("badge".equals(icon.type)) {
			image = BadgeBanner.image(icon.image);
		} else {
			try {
				image = Icons.valueOf(icon.name).get();
			} catch (Exception e) {
				image = Icons.STAIRS.get();
			}
		}
		if (icon.dark) {
			image.lightness(0f);
		}
		return image;
	}

}
