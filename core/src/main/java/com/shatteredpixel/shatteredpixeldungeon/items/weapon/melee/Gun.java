package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
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

import java.util.ArrayList;

public abstract class Gun extends MeleeWeapon {

    public static final String AC_SHOOT = "SHOOT";
    public static final String AC_RELOAD = "RELOAD";

    private static final float TIME_TO_SHOOT = 1f;

    public int maxRounds = initialRounds();
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
        return 1 +    // base
                lvl;  // level scaling
    }

    @Override
    public int max(int lvl) {
        return 2 * tier +    // base
                lvl;         // level scaling
    }

    @Override
    public String info() {
        String info = super.info();
        // TODO: 장전 등 메시지 추가하여 Info Message 변경
        return info;
    }

    @Override
    public String status() {
        return curRounds + "/" + maxRounds;
    }

    @Override
    public Weapon enchant(Enchantment ench) {
        // TODO: 총기 인챈트 불가능?
        return super.enchant(ench);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_RELOAD);
        actions.add(AC_SHOOT);
        return actions;
    }

    protected int initialRounds() {
        return 1;
    }

    public int collisionProperties(int target) {
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
            GLog.w(Messages.get(this, "fizzles"));
            // TODO: 총알이 없다는 메시지 추가
            return false;
        }
    }

    public abstract void onShoot(Ballistica attack);

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

        curUser.spendAndNext(TIME_TO_SHOOT);
    }

    public void reload(int amount) {
        curRounds += amount;
    }

    public void reloadBullet() {

        Bullet bullet = Dungeon.hero.belongings.getItem(Bullet.class);

        int maxToUse = maxRounds - curRounds;

        if (maxToUse == 0) {
            GLog.w(Messages.get(Gun.class, "already_full_loaded"));
            // TODO: 재장전 실패 텍스트
            return;
        } else if (maxToUse < bullet.quantity()) {
            reload(maxToUse);
            bullet.quantity(bullet.quantity() - maxToUse);
            GLog.i(Messages.get(Gun.class, "reload_success", maxToUse));
            // TODO: 재장전 성공 텍스트
        } else {
            reload(bullet.quantity());
            GLog.i(Messages.get(Gun.class, "reload_success", bullet.quantity()));
            bullet.detachAll(Dungeon.hero.belongings.backpack);
        }

        updateQuickslot();
        Sample.INSTANCE.play(Assets.Sounds.BURNING); // TODO: 사운드 변경
        curUser.sprite.emitter().burst(ElmoParticle.FACTORY, 12); // TODO: 재장전 이펙트
        evoke(curUser); // TODO: 재장전 이펙트
        GLog.p(Messages.get(Gun.class, "reload", bullet.name()));
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
