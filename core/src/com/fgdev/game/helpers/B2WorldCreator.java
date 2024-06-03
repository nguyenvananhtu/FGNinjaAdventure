package com.fgdev.game.helpers;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.Constants;
import com.fgdev.game.entitiles.enemies.*;
import com.fgdev.game.entitiles.tiles.box.BoxObject;
import com.fgdev.game.entitiles.tiles.box.Crate;
import com.fgdev.game.entitiles.tiles.item.Coin;
import com.fgdev.game.entitiles.tiles.item.Feather;
import com.fgdev.game.entitiles.tiles.item.ItemObject;
import com.fgdev.game.entitiles.tiles.platform.*;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.PPM;

public class B2WorldCreator {
    private World world;
    private ScoreIndicator scoreIndicator;
    private Map map;
    // Platform
    private final Array<Ground> activeGrounds = new Array<Ground>();
    private final Pool<Ground> groundPool = new Pool<Ground>() {
        @Override
        protected Ground newObject() {
            return new Ground(getWorld());
        }
    };
    private final Array<Sign> activeSigns = new Array<Sign>();
    private final Pool<Sign> signPool = new Pool<Sign>() {
        @Override
        protected Sign newObject() {
            return new Sign(getWorld());
        }
    };
    private final Array<HiddenWall> activeHiddenWalls = new Array<HiddenWall>();
    private final Pool<HiddenWall> hiddenWallPool = new Pool<HiddenWall>() {
        @Override
        protected HiddenWall newObject() {
            return new HiddenWall(getWorld());
        }
    };
    private final Array<Spike> activeSpikes = new Array<Spike>();
    private final Pool<Spike> spikePool = new Pool<Spike>() {
        @Override
        protected Spike newObject() {
            return new Spike(getWorld());
        }
    };
    private final Array<Ladder> activeLadders = new Array<Ladder>();
    private final Pool<Ladder> ladderPool = new Pool<Ladder>() {
        @Override
        protected Ladder newObject() {
            return new Ladder(getWorld());
        }
    };
    // Items
    private final Array<Coin> activeCoins = new Array<Coin>();
    private final Pool<Coin> coinPool = new Pool<Coin>() {
        @Override
        protected Coin newObject() {
            return new Coin(getWorld(), getScoreIndicator());
        }
    };
    private final Array<Feather> activeFeathers = new Array<Feather>();
    private final Pool<Feather> featherPool = new Pool<Feather>() {
        @Override
        protected Feather newObject() {
            return new Feather(getWorld(), getScoreIndicator());
        }
    };
    // Box
    private final Array<Crate> activeCrates = new Array<Crate>();
    private final Pool<Crate> cratePool = new Pool<Crate>() {
        @Override
        protected Crate newObject() {
            return new Crate(getWorld(), getScoreIndicator());
        }
    };
    // Enemies
    private final Array<Zombie> activeZombies = new Array<Zombie>();
    private final Pool<Zombie> zombiePool = new Pool<Zombie>() {
        @Override
        protected Zombie newObject() {
            return new Zombie(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Santa> activeSantas = new Array<Santa>();
    private final Pool<Santa> santaPool = new Pool<Santa>() {
        @Override
        protected Santa newObject() {
            return new Santa(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Robot> activeRobots = new Array<Robot>();
    private final Pool<Robot> robotPool = new Pool<Robot>() {
        @Override
        protected Robot newObject() {
            return new Robot(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Knight> activeKnights = new Array<Knight>();
    private final Pool<Knight> knightPool = new Pool<Knight>() {
        @Override
        protected Knight newObject() {
            return new Knight(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Dino> activeDinos = new Array<Dino>();
    private final Pool<Dino> dinoPool = new Pool<Dino>() {
        @Override
        protected Dino newObject() {
            return new Dino(getWorld(), getScoreIndicator());
        }
    };

    private final Array<AdventureGirl> activeAdventureGirls = new Array<AdventureGirl>();
    private final Pool<AdventureGirl> adventureGirlPool = new Pool<AdventureGirl>() {
        @Override
        protected AdventureGirl newObject() {
            return new AdventureGirl(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Ghost> activeGhosts = new Array<Ghost>();
    private final Pool<Ghost> ghostPool = new Pool<Ghost>() {
        @Override
        protected Ghost newObject() {
            return new Ghost(getWorld(), getScoreIndicator());
        }
    };

    private final Array<Bone> activeBones = new Array<Bone>();
    private final Pool<Bone> bonePool = new Pool<Bone>() {
        @Override
        protected Bone newObject() {
            return new Bone(getWorld(), getScoreIndicator());
        }
    };

    public B2WorldCreator(World world, Map map, ScoreIndicator scoreIndicator) {
        this.world = world;
        this.map = map;
        this.scoreIndicator = scoreIndicator;
        //create grounds bodies/fixtures
        for (MapObject object: map.getLayers().get("grounds").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Ground item = groundPool.obtain();
            item.init(object);
            activeGrounds.add(item);
        }

        //create hidden wall bodies/fixtures
        for (MapObject object: map.getLayers().get("hidden_walls").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            HiddenWall item = hiddenWallPool.obtain();
            item.init(object);
            activeHiddenWalls.add(item);
        }

        //create signs bodies/fixtures
        for (MapObject object: map.getLayers().get("signs").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Sign item = signPool.obtain();
            int type = object.getProperties().get("type", Integer.class) != null ? object.getProperties().get("type", Integer.class) : Sign.DEFAULT_SIGN;
            int level = object.getProperties().get("level", Integer.class) != null ? object.getProperties().get("level", Integer.class) : 1;
            item.init(object, type, level);
            activeSigns.add(item);
        }

        //create spikes bodies/fixtures
        for (MapObject object: map.getLayers().get("spikes").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Spike item = spikePool.obtain();
            item.init(object);
            activeSpikes.add(item);
        }

        //create ladders bodies/fixtures
        for (MapObject object: map.getLayers().get("ladders").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Ladder item = ladderPool.obtain();
            item.init(object);
            activeLadders.add(item);
        }

        //create crates bodies/fixtures
        for (MapObject object: map.getLayers().get("crates").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Crate item = cratePool.obtain();
            item.init(object);
            activeCrates.add(item);
        }

        //create coins bodies/fixtures
        for (MapObject object: map.getLayers().get("coins").getObjects().getByType(EllipseMapObject.class)) {
            // if you want to spawn a new:
            Coin item = coinPool.obtain();
            item.init(object);
            activeCoins.add(item);
        }

        //create feathers bodies/fixtures
        for (MapObject object: map.getLayers().get("feathers").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Feather item = featherPool.obtain();
            item.init(object);
            activeFeathers.add(item);
        }

        //create zombies bodies/fixtures
        for (MapObject object: map.getLayers().get("zombies").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Zombie item = zombiePool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeZombies.add(item);
        }

        //create robots bodies/fixtures
        for (MapObject object: map.getLayers().get("robots").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Robot item = robotPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeRobots.add(item);
        }

        //create adventure_girls bodies/fixtures
        for (MapObject object: map.getLayers().get("adventure_girls").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            AdventureGirl item = adventureGirlPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeAdventureGirls.add(item);
        }

        //create dinos bodies/fixtures
        for (MapObject object: map.getLayers().get("dinos").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Dino item = dinoPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeDinos.add(item);
        }

        //create knights bodies/fixtures
        for (MapObject object: map.getLayers().get("knights").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Knight item = knightPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeKnights.add(item);
        }

        //create santas bodies/fixtures
        for (MapObject object: map.getLayers().get("santas").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Santa item = santaPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeSantas.add(item);
        }

        //create ghosts bodies/fixtures
        for (MapObject object: map.getLayers().get("ghosts").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Ghost item = ghostPool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeGhosts.add(item);
        }

        //create bones bodies/fixtures
        for (MapObject object: map.getLayers().get("bones").getObjects().getByType(RectangleMapObject.class)) {
            // if you want to spawn a new:
            Bone item = bonePool.obtain();
            item.init(object, object.getProperties().get("type", Integer.class));
            activeBones.add(item);
        }
    }

    public Array<BoxObject> getActiveBoxObjects() {
        Array<BoxObject> boxObjects = new Array<BoxObject>();
        boxObjects.addAll(activeCrates);
        return boxObjects;
    }

    public Array<ItemObject> getActiveItemObjects() {
        Array<ItemObject> itemObjects = new Array<ItemObject>();
        itemObjects.addAll(activeCoins);
        itemObjects.addAll(activeFeathers);
        return itemObjects;
    }

    public Array<Enemy> getActiveEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(activeZombies);
        enemies.addAll(activeRobots);
        enemies.addAll(activeAdventureGirls);
        enemies.addAll(activeDinos);
        enemies.addAll(activeKnights);
        enemies.addAll(activeSantas);
        enemies.addAll(activeGhosts);
        enemies.addAll(activeBones);
        return enemies;
    }

    public Array<Spike> getActiveSpikes() {
        return activeSpikes;
    }

    public Pool<Spike> getSpikePool() {
        return spikePool;
    }

    public Array<HiddenWall> getActiveHiddenWalls() {
        return activeHiddenWalls;
    }

    public Pool<HiddenWall> getHiddenWallPool() {
        return hiddenWallPool;
    }

    public Array<Ground> getActiveGrounds() {
        return activeGrounds;
    }

    public Pool<Ground> getGroundPool() {
        return groundPool;
    }

    public Array<Sign> getActiveSigns() {
        return activeSigns;
    }

    public Pool<Sign> getSignPool() {
        return signPool;
    }

    public Array<Coin> getActiveCoins() {
        return activeCoins;
    }

    public Pool<Coin> getCoinPool() {
        return coinPool;
    }

    public Array<Feather> getActiveFeathers() {
        return activeFeathers;
    }

    public Pool<Feather> getFeatherPool() {
        return featherPool;
    }

    public Array<Crate> getActiveCrates() {
        return activeCrates;
    }

    public Pool<Crate> getCratePool() {
        return cratePool;
    }

    public Array<Zombie> getActiveZombies() {
        return activeZombies;
    }

    public Pool<Zombie> getZombiePool() {
        return zombiePool;
    }

    public Array<Santa> getActiveSantas() {
        return activeSantas;
    }

    public Pool<Santa> getSantaPool() {
        return santaPool;
    }

    public Array<Robot> getActiveRobots() {
        return activeRobots;
    }

    public Pool<Robot> getRobotPool() {
        return robotPool;
    }

    public Array<Knight> getActiveKnights() {
        return activeKnights;
    }

    public Pool<Knight> getKnightPool() {
        return knightPool;
    }

    public Array<Dino> getActiveDinos() {
        return activeDinos;
    }

    public Pool<Dino> getDinoPool() {
        return dinoPool;
    }

    public Array<AdventureGirl> getActiveAdventureGirls() {
        return activeAdventureGirls;
    }

    public Pool<AdventureGirl> getAdventureGirlPool() {
        return adventureGirlPool;
    }

    public Array<Ghost> getActiveGhosts() {
        return activeGhosts;
    }

    public Pool<Ghost> getGhostPool() {
        return ghostPool;
    }

    public Array<Bone> getActiveBones() {
        return activeBones;
    }

    public Pool<Bone> getBonePool() {
        return bonePool;
    }

    public World getWorld() {
        return world;
    }

    public Map getMap() {
        return map;
    }

    public ScoreIndicator getScoreIndicator() {
        return scoreIndicator;
    }
}
