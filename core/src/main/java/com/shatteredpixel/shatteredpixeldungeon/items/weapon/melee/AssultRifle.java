package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AssultRifle extends Gun{
    {

        image = ItemSpriteSheet.ASSULTRIFLE;
        tier = 4;

        maxRounds = 12;
        timeToReload = 2f;

        // hitSound = ?
        // hitSoundPitch = ?
        bones = true;

    }

}