package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Handgun extends Gun{

    {
        image = ItemSpriteSheet.HANDGUN;

        // hitSound = ?
        // hitSoundPitch = ?

        maxRounds = 6;
        timeToShoot = 0.5f;

        tier = 2;
        bones = true;
    }

    public int shootMin(int lvl) {
        return (tier) +         // 2 base, down from 3
                lvl;            // level scaling
    }

    public int shootMax(int lvl) {
        return 2 * (tier + 1) +     // 6 base, down from 12
                lvl * (tier);       // +2 per lvl, down from +3
    }

}
