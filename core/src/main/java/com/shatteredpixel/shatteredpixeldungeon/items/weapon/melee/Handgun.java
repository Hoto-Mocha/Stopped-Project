package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Handgun extends Gun{

    {
        image = ItemSpriteSheet.HANDGUN;

        // hitSound = ?
        // hitSoundPitch = ?

        timeToShoot = 0.5f;

        tier = 2;
        bones = true;
    }

    @Override
    public int shootMin(int lvl) {
        return (tier) +         // 2 base, down from 3
                lvl;            // level scaling
    }

    @Override
    public int shootMax(int lvl) {
        return 4 * (tier) +     // 8 base, down from 15
                lvl * (tier);       // +2 per lvl, down from +3
    }

    @Override
    public int initialRounds() {
        return 12;
    }
}