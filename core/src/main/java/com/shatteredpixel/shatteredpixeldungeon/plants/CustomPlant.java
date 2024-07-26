package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

public class CustomPlant extends Plant {
    String desc;
    String plantName;

    public CustomPlant(int imageID, int pos, String name, String desc) {
        image = imageID;
        this.pos = pos;
        plantName = name;
        this.desc = desc;
    }

    @Override
    public void activate(Char ch){
        //do nothing
    }

    @Override
    public String name(){
        throw new RuntimeException("Stub!");
    }
    @Override
    public String desc() {
        return desc;
    }
}
