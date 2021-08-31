package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrudePistol extends MeleeWeapon {
    {
        image = ItemSpriteSheet.DAGGER; // TODO: Sprite 연결하기
        // hitSound = ?
        // hitSoundPitch = ?
        tier = 1;
        bones = false; // 기본 무기는 영웅의 유해에 포함되지 않음
    }


}
