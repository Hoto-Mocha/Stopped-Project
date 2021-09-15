package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class DualPistol extends Gun{

    {
        image = ItemSpriteSheet.DUALPISTOL;

        // hitSound = ?
        // hitSoundPitch = ?

        maxRounds = 12;
        timeToShoot = 0.5f;
        timeToReload = 2f;
        tier = 3;
        bones = true;
    }

    public int min(int lvl) {
        return 1 * (tier - 1) +    // base
                lvl;               // level scaling
    }

    public int max(int lvl) {
        return 3 * (tier - 1) +    // base
                lvl * (tier - 1);  // level scaling
    }

    public int shootMin(int lvl) {
        return 2 * (tier - 1) +     // base
                lvl;                // level scaling
    }

    public int shootMax(int lvl) {
        return 5 * (tier - 1) +     // base
                lvl * (tier - 1);   // level scaling
    }