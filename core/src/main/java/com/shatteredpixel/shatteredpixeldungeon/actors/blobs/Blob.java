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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Rect;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

public class Blob extends Actor {

	{
		actPriority = BLOB_PRIO;
	}
	
	public int volume = 0;
	
	public int[] cur;
	protected int[] off;
	
	public BlobEmitter emitter;

	public Rect area = new Rect();
	
	public boolean alwaysVisible = false;

	@Override
	public boolean act() {
		
		spend( TICK );
		
		if (volume > 0) {

			if (area.isEmpty())
				setupArea();

			volume = 0;


			int[] tmp = off;
			off = cur;
			cur = tmp;
			
		} else {
			if (!area.isEmpty()) {
				area.setEmpty();
				//clear any values remaining in off
				System.arraycopy(cur, 0, off, 0, cur.length);
			}
		}
		
		return true;
	}

	public void setupArea(){
		for (int cell=0; cell < cur.length; cell++) {
			if (cur[cell] != 0){
				area.union(cell% level.width(), cell/ level.width());
			}
		}
	}
	
	public void use( BlobEmitter emitter ) {
		this.emitter = emitter;
	}

	public void seed( Level level, int cell, int amount ) {
		if (cur == null) cur = new int[level.length()];
		if (off == null) off = new int[cur.length];
		if(cell > cur.length) {
			int[] tempCur = new int[cell+1];
			System.arraycopy(cur, 0, tempCur,0, cur.length);
			cur = new int[cell+1];
			System.arraycopy(tempCur, 0, cur,0, tempCur.length);
		}
		cur[cell] += amount;
		volume += amount;

		area.union(cell%level.width(), cell/level.width());
	}
	
	public void clear( int cell ) {
		if (volume == 0) return;
		volume -= cur[cell];
		cur[cell] = 0;
	}

	public void fullyClear(){
		volume = 0;
		area.setEmpty();
		cur = new int[level.length()];
		off = new int[level.length()];
	}

	public void onBuildFlagMaps( Level l ){
		//do nothing by default, only some blobs affect flags
	}

	//some blobs have an associated landmark entry, which is added when the hero sees them
	//blobs may also remove this landmark in some cases, such as when they expire or are consumed
	public Notes.Landmark landmark(){
		return null;
	}
	
	public String tileDesc() {
		return null;
	}

	@Contract(value = "_, _, _, _ -> fail", pure = true)
	public static<T extends Blob> T seed(int id, int cell, int amount, Class<T> type ) {
		throw new RuntimeException("Not implemented");
	}
	public void seed( int cell, int amount ) {
		cur[cell] += amount;
		volume += amount;
	}
	public void clearBlob( ) {
		if(level != null) {
			if (cur == null) cur = new int[level.length()];
			if (off == null) off = new int[cur.length];
		} else {
			if (cur == null) cur = new int[32*32];
			if (off == null) off = new int[cur.length];
		}
		Arrays.fill( cur, 0 );
		volume = 0;
	}
}
