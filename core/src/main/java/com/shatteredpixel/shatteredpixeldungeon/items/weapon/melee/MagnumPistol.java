package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MagnumPistol extends Gun{
    {

        image = ItemSpriteSheet.MAGNUMPISTOL;
        tier = 5;

        timeToReload = 1f;

        // hitSound = ?
        // hitSoundPitch = ?
        bones = true;

    }

    @Override
    public int initialRounds() {
        return 8;
    }
}