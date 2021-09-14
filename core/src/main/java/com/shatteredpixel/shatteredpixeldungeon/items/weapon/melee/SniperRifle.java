package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SniperRifle extends Gun {

    {
        image = ItemSpriteSheet.SNIPERRIFLE;

        maxRounds = 3;
        timeToReload = 2f;

        tier = 4;
        bones = true;
    }

    @Override
    public void onShoot(Ballistica shot) {

        Char ch = Actor.findChar(shot.collisionPos);
        if (ch != null) {

            float dmgBonus = (float) Math.min(Random.Int(shot.dist), 5 + buffedLvl());
            curUser.shoot(ch, 1f, dmgBonus, 1.2f);
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
            // TODO: Hit Sound 변경

            ch.sprite.burst(0xFFFFFFFF, buffedLvl() / 2 + 2);
            // TODO: 스프라이트 변경

        }

    }

}
