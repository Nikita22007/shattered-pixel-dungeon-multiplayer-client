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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

@SuppressWarnings("unused")
public class SpectralNecromancerSprite extends MobSprite {

	private final Animation charging;

	public SpectralNecromancerSprite(){
		super();

		texture( Assets.Sprites.NECRO );
		TextureFilm film = new TextureFilm( texture, 16, 16 );

		int c = 16;

		idle = new Animation( 1, true );
		idle.frames( film, c+0, c+0, c+0, c+1, c+0, c+0, c+0, c+0, c+1 );

		run = new Animation( 8, true );
		run.frames( film, c+0, c+0, c+0, c+2, c+3, c+4 );

		zap = new Animation( 10, false );
		zap.frames( film, c+5, c+6, c+7, c+8 );

		charging = new Animation( 5, true );
		charging.frames( film, c+7, c+8 );

		die = new Animation( 10, false );
		die.frames( film, c+9, c+10, c+11, c+12 );

		attack = zap.clone();

		idle();
	}


	public void charge(){
		play(charging);
	}

	@Override
	public void zap(int cell) {
		super.zap(cell);
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == zap){
            idle();
        }
	}

}
