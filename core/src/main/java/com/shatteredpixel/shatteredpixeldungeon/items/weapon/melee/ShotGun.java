package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ShotGun extends Gun {

    private final static int MAX_DIST = 4;  // 최대 사거리

    {
        image = ItemSpriteSheet.SHOTGUN;
        maxRounds = 2;
        timeToReload = 2f;

        tier = 3;
        bones = true;
    }

    @Override
    public int min(int lvl) {
        // (티어 + 강화수치)
        return tier +      // base
                lvl;       // level scaling
    }

    @Override
    public int max(int lvl) {
        // ((3 + 강화수치) * (티어+1))
        return 3 * (tier + 1) +      // base
                lvl * (tier + 1);    // level scaling
    }

    @Override
    public void onShoot(Ballistica shot) {

        Char ch = Actor.findChar(shot.collisionPos);
        if (ch != null) {

            float dmgMulti = (float) MAX_DIST - Math.min(Random.Int(shot.dist), MAX_DIST);
            curUser.shoot(ch, dmgMulti, 0, 1f);
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
            // TODO: Hit Sound 변경

            ch.sprite.burst(0xFFFFFFFF, buffedLvl() / 2 + 2);
            // TODO: 스프라이트 변경

        }

    }

}
