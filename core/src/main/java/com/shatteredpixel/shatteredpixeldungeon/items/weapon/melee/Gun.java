package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Bullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Gun extends MeleeWeapon {

    public static final String AC_SHOOT = "SHOOT";
    public static final String AC_RELOAD = "RELOAD";

    public float timeToShoot = 1f;
    public float timeToReload = 1f;
    public int maxRounds = 6;
    public int curRounds = maxRounds;

    protected int collisionProperties = Ballistica.MAGIC_BOLT;

    private static final int USES_TO_ID = 20;
    private float usesLeftToID = USES_TO_ID;
    private float availableUsesToID = USES_TO_ID / 2f;

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;
    }

    @Override
    public int min(int lvl) {
        return 1 * tier +    // base
                lvl;         // level scaling
    }

    @Override
    public int max(int lvl) {
        return 3 * (tier + 1) +    // base
                lvl;               // level scaling
    }

    public int shootMin(int lvl) {
        return 1 * (tier + 1) +     // base
                lvl;                // level scaling
    }

    public int shootMax(int lvl) {
        return 5 * (tier + 1) +     // base
                lvl * (tier + 1);   // level scaling
    }

    public int shootMin() {
        return shootMin(buffedLvl());
    }

    public int shootMax() {
        return shootMax(buffedLvl());
    }

    public int shootDamageRoll() {
        return augment.damageFactor(Random.NormalIntRange(shootMin(), shootMax()));
    }

    public int shootDamageRoll(Char owner) {
        int damage = augment.damageFactor(Random.NormalIntRange(shootMin(), shootMax()));

        if (owner instanceof Hero) {
            int exStr = ((Hero) owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange(0, exStr);
            }
        }

        return damage;
    }

    @Override
    public String info() {
        String info = super.info();
        info += "\n\n" + Messages.get(Gun.class, "not_enchantable");
        return info;
    }

    public String statsInfo(){
        return Messages.get(this, "stats");
    }

    @Override
    public String status() {
        return curRounds + "/" + maxRounds;
    }

    @Override
    public Weapon enchant(Enchantment ench) {
        return this; // Not enchantable
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_RELOAD);
        actions.add(AC_SHOOT);
        return actions;
    }

    public int collisionProperties(int target) {
        return collisionProperties;
    }

    public int collisionProperties() {
        return collisionProperties;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_RELOAD)) {

            curUser = hero;
            curItem = this;
            reloadBullet();

        } else if (action.equals(AC_SHOOT)) {

            if (cursed || hasCurseEnchant()) {
                // TODO: 저주받았을 때 메커니즘 추가
            }
            curUser = hero;
            curItem = this;
            GameScene.selectCell(shooter);
        }
    }

    public boolean tryToShoot(Hero owner, int target) {

        if (curRounds >= 1) {
            return true;
        } else {
            // TODO: 애니메이션 출력
            GLog.w(Messages.get(this, "fizzles"));
            return false;
        }
    }

    public void onShoot(Ballistica shot) {

        Char ch = Actor.findChar(shot.collisionPos);
        if (ch != null) {

            curUser.shoot(ch);
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
            // TODO: Hit Sound 변경

            ch.sprite.burst(0xFFFFFFFF, buffedLvl() / 2 + 2);
            // TODO: 스프라이트 변경

        }

    }

    public void fx(Ballistica bolt, Callback callback) {
        // TODO: 효과 추가
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.MAGIC_MISSILE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        if (curRounds == 0) Sample.INSTANCE.play(Assets.Sounds.ZAP); // TODO: 효과음 추가
        else Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
    }

    protected void gunUsed() {
        if (!isIdentified()) {
            float uses = Math.min(availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this));
            availableUsesToID -= uses;
            usesLeftToID -= uses;
            if (usesLeftToID <= 0 || Dungeon.hero.pointsInTalent(Talent.SCHOLARS_INTUITION) == 2) {
                identify();
                GLog.p(Messages.get(Gun.class, "identify"));
                // TODO: 감정했을 때 텍스트 추가
                Badges.validateItemLevelAquired(this);
            }
        }

        curRounds -= 1;

        Invisibility.dispel();
        updateQuickslot();

        curUser.spendAndNext(timeToShoot);
    }

    public void reload(int amount) {
        curRounds += amount;
    }

    public void reloadBullet() {

        Bullet bullet = Dungeon.hero.belongings.getItem(Bullet.class);

        int maxToUse = maxRounds - curRounds;

        if (maxToUse == 0) {
            GLog.w(Messages.get(Gun.class, "already_full_loaded"));
            return;
        } else if (maxToUse < bullet.quantity()) {
            reload(maxToUse);
            bullet.quantity(bullet.quantity() - maxToUse);
            GLog.i(Messages.get(Gun.class, "reload_success", maxToUse));
            curUser.spendAndNext(timeToReload);
        } else if (bullet.quantity() == 0) {
            GLog.w(Messages.get(Gun.class, "reload_fail"));
            return;
        } else {
            reload(bullet.quantity());
            GLog.i(Messages.get(Gun.class, "reload_success", bullet.quantity()));
            bullet.detachAll(Dungeon.hero.belongings.backpack);
            curUser.spendAndNext(timeToReload);
        }

        updateQuickslot();
        Sample.INSTANCE.play(Assets.Sounds.BURNING); // TODO: 사운드 변경
        curUser.sprite.emitter().burst(ElmoParticle.FACTORY, 12); // TODO: 재장전 이펙트
        evoke(curUser); // TODO: 재장전 이펙트
    }

    protected static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final Gun curGun;
                if (curItem instanceof Gun) {
                    curGun = (Gun) Gun.curItem;
                } else {
                    return;
                }

                final Ballistica shot = new Ballistica(curUser.pos, target, curGun.collisionProperties(target));
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    curGun.reloadBullet();
                }

                // curUser.sprite.zap(cell);
                // TODO: 발사 스프라이트 추가

                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                if (curGun.tryToShoot(curUser, target)) {

                    curUser.busy();

                    curGun.fx(shot, new Callback() {
                        public void call() {
                            curGun.onShoot(shot);
                            curGun.gunUsed();
                        }
                    });

                    curGun.cursedKnown = true;

                }

            }
        }

        @Override
        public String prompt() {
            return Messages.get(Gun.class, "prompt");
        }
    };

}
