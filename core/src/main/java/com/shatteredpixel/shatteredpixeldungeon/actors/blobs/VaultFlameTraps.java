package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.Arrays;

//contains both blob logic and logic for seeding itself
public class VaultFlameTraps extends Blob {

	public int[] initialCooldowns;
	public int[] cooldowns;

	@Override
	public boolean act() {
		super.act();

		for (int i = 0; i < initialCooldowns.length; i++){
			if (initialCooldowns[i] > -1){
				if (cooldowns[i] <= 0){
					cooldowns[i] = initialCooldowns[i];
				}
				cooldowns[i]--;
				if (cooldowns[i] <= 0){
					seed(Dungeon.level, i, 1);
				}
			}
		}

		return true;
	}

	public void seed(Level level, int cell, int amount ) {
		super.seed(level, cell, amount);
		if (initialCooldowns == null) {
			initialCooldowns = new int[level.length()];
			Arrays.fill(initialCooldowns, -1);
		}
		if (cooldowns == null){
			cooldowns = new int[level.length()];
		}
	}

	private static final String ONE	= "one";
	private static final String TWO	= "two";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ONE, initialCooldowns);
		bundle.put(TWO, cooldowns);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		initialCooldowns = bundle.getIntArray(ONE);
		cooldowns = bundle.getIntArray(TWO);
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.bound.set(0.4f, 0.4f, 0.6f, 0.6f);
		emitter.pour( ElmoParticle.FACTORY, 0.3f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

}
