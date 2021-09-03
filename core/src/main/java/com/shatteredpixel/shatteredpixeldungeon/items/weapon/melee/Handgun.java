package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Handgun extends Gun{

    {
        image = ItemSpriteSheet.DAGGER; // TODO: Sprite 연결하기
        // hitSound = ?
        // hitSoundPitch = ?

        maxRounds = 6;
        timeToShoot = 0.5f;

        tier = 2;
        bones = true;
    }

}
