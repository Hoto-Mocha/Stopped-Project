package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MachineGun extends Gun {

    {
        image = ItemSpriteSheet.MACHINEGUN;

        timeToReload = 4;
        timeToShoot = 0.25f;

        // hitSound = ?
        // hitSoundPitch = ?

        tier = 5;
        bones = true;

        ACC = 0.7f; //70% accuracy
    }

    @Override
    public int initialRounds() {
        return 40;
    }
}
