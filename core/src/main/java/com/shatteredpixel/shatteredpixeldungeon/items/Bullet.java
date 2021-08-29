package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Bullet extends Item {
    {
        image = ItemSpriteSheet.DEWDROP; // TODO: 이미지 변경
        stackable = true;
        bones = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return Math.max(1, quantity/10);
    }

}
