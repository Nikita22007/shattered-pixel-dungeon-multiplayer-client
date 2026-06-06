package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TalentCache;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class HeroPatchParser implements ActionParser {

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONObject heroObj = action;
        Hero hero = Dungeon.hero;
        if (hero == null) {
            if (!heroObj.has("actor_id")) {
                return;
            }
            Dungeon.hero = new Hero();
            hero = Dungeon.hero;
            hero.changeID(heroObj.getInt("actor_id"));
            Actor.add(hero);
        } else {
            if (heroObj.has("actor_id")) {
                Actor.remove(hero);
                hero.changeID(heroObj.getInt("actor_id"));
                Actor.add(hero);
            }
        }
        for (Iterator<String> it = heroObj.keys(); it.hasNext(); ) {
            String token = it.next();
            switch (token) {
                case "actor_id": {
                    //parsed before
                    break;
                }
                case "strength": {
                    hero.STR = heroObj.getInt(token);
                    break;
                }
                case "lvl": {
                    hero.lvl = heroObj.getInt(token);
                    break;
                }
                case "exp": {
                    hero.exp = heroObj.getInt(token);
                    break;
                }
                case "class": {
                    String className = JsonStringHelper.getString(heroObj, token);
                    className = className.toUpperCase();
                    hero.heroClass = HeroClass.valueOf(className);
                    if (hero.sprite instanceof HeroSprite) {
                        ((HeroSprite) hero.sprite).disguise(hero.heroClass);
                    }
                    break;
                }


                case "talents": {
                    if (hero.talents.size() < 4) { //todo check this trash
                        Talent.initClassTalents(hero);
                    }
                    JSONArray talentsArray = heroObj.getJSONArray("talents");
                    for (int i = 0; i < talentsArray.length(); i++) {
                        JSONArray talentRow = talentsArray.getJSONArray(i);
                        LinkedHashMap<Talent, Integer> talentIntMap = new LinkedHashMap<>();
                        for (int index = 0; index < talentRow.length(); index++) {
                            JSONObject talentObject = talentRow.getJSONObject(index);
                            int points = talentObject.getInt("points");
                            int icon = talentObject.getInt("icon");
                            Talent talent = TalentCache.talentByIcon(icon);
                            talentIntMap.put(talent, points);
                        }
                        hero.talents.set(i, talentIntMap);
                    }
                    break;
                }
            }
        }
    }
}
