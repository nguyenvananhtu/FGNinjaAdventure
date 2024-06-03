package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.bullets.EnemyBullet;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class AdventureGirl extends Enemy implements Pool.Poolable {

    public static final String TAG = AdventureGirl.class.getName();

    public static final int SHOOT = 0;
    public static final int MELEE = 1;

    public enum State {
        IDLE, RUN, DEAD, SHOOT, JUMP, MELEE, SLIDE
    }

    private State currentState;
    private State previousState;

    private Animation adventureGirlIdle;
    private Animation adventureGirlRun;
    private Animation adventureGirlDead;
    private Animation adventureGirlShoot;
    private Animation adventureGirlMelee;
    private Animation adventureGirlSlide;
    private Animation adventureGirlJump;

    private boolean isRun;
    private boolean isShoot;
    private boolean isMelee;
    private boolean isSlide;

    private float timeDelayIdle = 1;
    private float timeDelayRun = 2;

    public AdventureGirl(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        adventureGirlIdle = Assets.instance.adventureGirl.animIdle;
        adventureGirlRun = Assets.instance.adventureGirl.animRun;
        adventureGirlDead = Assets.instance.adventureGirl.animDead;
        adventureGirlShoot = Assets.instance.adventureGirl.animShoot;
        adventureGirlMelee = Assets.instance.adventureGirl.animMelee;
        adventureGirlSlide = Assets.instance.adventureGirl.animSlide;
        adventureGirlJump = Assets.instance.adventureGirl.animJump;
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isShoot = false;
        isMelee = false;
        isSlide = false;
        speed = 1.5f;
        // Extend Abstract
        init();
        setRegion((TextureRegion) adventureGirlIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isShoot = false;
        isMelee = false;
        isSlide = false;
        speed = 1f;
    }

    public void update(float dt) {
        if (timeDelayIdle > 0) {
            timeDelayIdle -= dt;
            if (timeDelayIdle < 0) timeDelayRun = 3;
        }

        if (isRun) {
            if (timeDelayRun > 0) {
                timeDelayRun -= dt;
                running();
            }
            if (timeDelayRun < 0 && timeDelayIdle < 0) {
                previousState = State.SHOOT;
                isShoot = true;
            }

        }

        // spawn
        handleSpawningBullet();

        // update bullets
        for (EnemyBullet bullet : bullets) {
            bullet.update(dt);
        }
        // clean
        for (int i = 0; i < bullets.size; i++) {
            if (!bullets.get(i).isAlive()) {
                bullets.removeIndex(i);
            }
        }

        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
        currentState = getState();
        switch (currentState) {
            case DEAD:
                setBounds(0, 0, 91 * 2  / PPM, 91 * 2 / PPM);
                break;
            case RUN:
            case SHOOT:
            case SLIDE:
            case JUMP:
            case MELEE:
            case IDLE:
            default:
                setBounds(0, 0, 96 * 2  / PPM, 81 * 2 / PPM);
                break;
        }
    }

    @Override
    protected TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case RUN:
                region = (TextureRegion) adventureGirlRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) adventureGirlDead.getKeyFrame(stateTimer);
                break;
            case SHOOT:
                region = (TextureRegion) adventureGirlShoot.getKeyFrame(stateTimer);
                if (adventureGirlShoot.isAnimationFinished(stateTimer)) {
                    if (isShoot) {
                        float x = runningRight ? 1f : -1f;
                        float y = -0.1f;
                        // if you want to spawn a new bullet:
                        addSpawnBullet(body.getPosition().x + x, body.getPosition().y + y, runningRight ? true : false);
                        isShoot = false;
                        timeDelayIdle = 1;
                    }
                }
                break;
            case MELEE:
                region = (TextureRegion) adventureGirlMelee.getKeyFrame(stateTimer, true);
                break;
            case SLIDE:
                region = (TextureRegion) adventureGirlSlide.getKeyFrame(stateTimer);
                break;
            case JUMP:
                region = (TextureRegion) adventureGirlJump.getKeyFrame(stateTimer);
                break;
            case IDLE:
            default:
                region = (TextureRegion) adventureGirlIdle.getKeyFrame(stateTimer,true);
                break;
        }

        //if player is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        //if player is running right and the texture isnt facing right... flip it.
        else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (isDead)
            return State.DEAD;
        else if (isMelee)
            return State.MELEE;
        else if (isSlide)
            return State.SLIDE;
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
        else if (isShoot)
            return State.SHOOT;
            // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        // draw bullets
        for (EnemyBullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    private void makeBoxRobotBody(float posx, float posy) {
        float width = (96 - 45)/ PPM;
        float height = (81 - 10) / PPM;
        // create adventureGirl
        body = bodyFactory.makeBoxPolyBody(
                posx,
                posy,
                width,
                height,
                BodyFactory.ENEMY_DISABLE_PLAYER,
                BodyDef.BodyType.DynamicBody,
                this
        );
        // create foot sensor
        bodyFactory.makeShapeSensor(body,
                width,
                10 / PPM,
                new Vector2(0, (-height - 60) / PPM),
                0,
                BodyFactory.ENEMY_SENSOR,
                this
        );
        // create keep shape
        bodyFactory.makeEdgeSensor(body,
                new Vector2(0, (-height - 60) / PPM),
                new Vector2(6.8f / PPM / 6, 6.8f / PPM * 3),
                BodyFactory.ENEMY,
                this
        );
    }

    protected void defineEnemy() {
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        makeBoxRobotBody(
                (rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM
        );
    }

    @Override
    public int score() {
        return 100;
    }

    @Override
    public void killed() {
        super.killed();
        setRegion((TextureRegion) adventureGirlDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) adventureGirlMelee.getKeyFrame(stateTimer));
        player.playerDie();
        isMelee = true;
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) adventureGirlIdle.getKeyFrame(stateTimer));
        isRun = true;
        isMelee = false;
    }
}
