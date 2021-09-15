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
        return (tier - 1) +       // 2 base, down from 4
                lvl;              // level scaling


    }

    public int shootMin(int lvl) {
        return (tier) +         // 3 base, down from 4
                lvl;            // level scaling
    }

    public int shootMax(int lvl) {
        return 4 * (tier) +         // 12 base, down from 20
                lvl * (tier);       // +3 per lvl, down from +4
    }

}