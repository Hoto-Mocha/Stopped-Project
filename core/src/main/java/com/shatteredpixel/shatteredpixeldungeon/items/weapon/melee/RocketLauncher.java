package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RocketLauncher extends Gun{
    {

        image = ItemSpriteSheet.ASSULTRIFLE;
        tier = 4;

        timeToShoot = 2f;
        timeToReload = 3f;

        // hitSound = ?
        // hitSoundPitch = ?
        bones = true;

    }

    public int shootMin(int lvl) {
        return 6 * (tier + 1) +     // base
                lvl;                // level scaling
    }

    public int shootMax(int lvl) {
        return 15 * (tier + 1) +     // base
                lvl * (tier + 1);   // level scaling
    }

    @Override
    public int initialRounds() {
        return 1;
    }
}