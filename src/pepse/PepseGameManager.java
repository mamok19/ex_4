package pepse;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.EnergyDisplay;
import pepse.world.Sky;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Terrain;
import pepse.world.avatar.Avatar;
import pepse.world.daynight.Moon;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;

import java.util.List;

import static pepse.Constants.BLOCK_SIZE;

/**
 * The main class of the Pepse game.
 * incharge of initializing and running the game.
 * @author Eilam Soroka, Maayan Felig
 */
public class PepseGameManager extends GameManager {


    private final java.util.Map<Integer, java.util.List<ObjInLayer>> loadedZones =
            new java.util.HashMap<>();


    private static final int AVATAR_LAYER = Layer.FOREGROUND + 2;
    private static final int FRUIT_LAYER = Layer.FOREGROUND + 1;
    private static final int SKY_LAYER = -250;
    private static final int SEED = 73;
    private static final int CYCLE_OF_DAY_LENGTH = 10;
    private static final String TOP_LAYER_TAG = "topGroundBlock";
    private static final int SUN_LAYER = -225;
    private static final float FIRST_X_POSITION = 0f;
    private float groundHeightAtX0;
    private Avatar avatar;
    private Terrain terrain;
    private Flora flora;
    private float zoneWidth;
    private int currentZoneIdx;

    /**
     * Initializes the game by setting up the sky and terrain.
     *
     * @param imageReader     Provides functionality for reading images.
     * @param soundReader     Provides functionality for playing sounds.
     * @param inputListener   Listens for user input.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND,Layer.STATIC_OBJECTS,true);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND,FRUIT_LAYER,true);
        initializeSky(windowController);

        terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        flora = new Flora(SEED, terrain::groundHeightAt);
        this.groundHeightAtX0 = Terrain.groundHeightAtX0(windowController.getWindowDimensions());
        initializeNight(windowController);
        initializeSun(windowController);
        initializeMoon(windowController);



        initializeAvatar(new Vector2(FIRST_X_POSITION,groundHeightAtX0),inputListener, imageReader,
                windowController);
        initializeEnergyDisplay(windowController);

        zoneWidth = windowController.getWindowDimensions().x();
        currentZoneIdx = worldXToZone(avatar.getCenter().x());
        loadZone(currentZoneIdx - 1);
        loadZone(currentZoneIdx);
        loadZone(currentZoneIdx + 1);
    }

    /**
     * Updates the game state, loading and unloading zones as the avatar moves.
     *
     * @param deltaTime Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int newZone = worldXToZone(avatar.getCenter().x());
        if (newZone == currentZoneIdx) return;

        if (newZone > currentZoneIdx) {
            // moved right: drop leftmost, add new rightmost
            unloadZone(currentZoneIdx - 1);
            loadZone(newZone + 1);
        } else {
            // moved left: drop rightmost, add new leftmost
            unloadZone(currentZoneIdx + 1);
            loadZone(newZone - 1);
        }

        currentZoneIdx = newZone;
        System.out.println("loadedZones=" + loadedZones.keySet());

    }


    private void loadZone(int zoneIdx) {
        if (loadedZones.containsKey(zoneIdx)) return;

        int start = zoneStartX(zoneIdx);
        int end = zoneEndX(zoneIdx);

        java.util.List<ObjInLayer> created = new java.util.ArrayList<>();

        for (Block block : terrain.createInRange(start, end)) {
            int layer = TOP_LAYER_TAG.equals(block.getTag())
                    ? Layer.STATIC_OBJECTS
                    : Layer.BACKGROUND;
            gameObjects().addGameObject(block, layer);
            created.add(new ObjInLayer(block, layer));
        }

        List<Tree> trees = flora.createInRange(start, end);
        for (Tree tree : trees) {
            for (Block trunk : tree.getTrunkBlocks()) {
                int layer = Layer.FOREGROUND + 1; // you used this for trunk previously
                gameObjects().addGameObject(trunk, layer);
                created.add(new ObjInLayer(trunk, layer));
            }
            for (Leaf leaf : tree.getAllLeaves()) {
                int layer = Layer.FOREGROUND;
                gameObjects().addGameObject(leaf, layer);
                created.add(new ObjInLayer(leaf, layer));
            }
            for (Fruit fruit : tree.getAllFruit()) {
                int layer = FRUIT_LAYER;
                gameObjects().addGameObject(fruit, layer);
                created.add(new ObjInLayer(fruit, layer));
            }

        }
        loadedZones.put(zoneIdx, created);
    }

    private void unloadZone(int zoneIdx) {
        java.util.List<ObjInLayer> objs = loadedZones.get(zoneIdx);
        if (objs == null) return;

        for (ObjInLayer ref : objs) {
            gameObjects().removeGameObject(ref.obj, ref.layer);
        }
        loadedZones.remove(zoneIdx);
    }


    private void initializeTrees(WindowController windowController) {
        List<Tree> trees = flora.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Tree tree : trees) {
            List<Block> trunkBlocks = tree.getTrunkBlocks();
            for (Block block : trunkBlocks) {
                gameObjects().addGameObject(block, Layer.FOREGROUND + 1);
            }
            List<Leaf> leaves = tree.getAllLeaves();
            for (Leaf l : leaves) {
                gameObjects().addGameObject(l, Layer.FOREGROUND);
            }
            List<Fruit> fruits = tree.getAllFruit();
            for (Fruit f : fruits) {
                gameObjects().addGameObject(f, FRUIT_LAYER);
            }

        }
    }


    private void initializeEnergyDisplay(WindowController windowController) {
        new EnergyDisplay(
                avatar::getEnergyMeter,
                (gameObject, layer) -> gameObjects().addGameObject(gameObject, layer)
        );
    }

    private void initializeAvatar(Vector2 topBlockAtX0, UserInputListener inputListener,
                                  ImageReader imageReader,
                                 WindowController windowController) {
        this.avatar = new Avatar(topBlockAtX0, inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.FOREGROUND);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    private void initializeMoon(WindowController windowController) {
        GameObject moon = Moon.create(windowController.getWindowDimensions(),
                CYCLE_OF_DAY_LENGTH);
        gameObjects().addGameObject(moon, SUN_LAYER);
    }

    private void initializeSun(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(),
                CYCLE_OF_DAY_LENGTH);
        gameObjects().addGameObject(sun, SUN_LAYER);
        gameObjects().addGameObject(SunHalo.create(sun), SUN_LAYER);

    }

    private void initializeNight(WindowController windowController) {
        gameObjects().addGameObject(Night.create(windowController.getWindowDimensions(),
                CYCLE_OF_DAY_LENGTH), Layer.UI);
    }

    private void initializeTerrain(WindowController windowController) {
        for (Block block : terrain.createInRange(0, (int) windowController.getWindowDimensions().x())) {
            if (block.getTag().equals(TOP_LAYER_TAG) ) {
                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
                continue;
            }
            gameObjects().addGameObject(block, Layer.BACKGROUND);
        }
    }

    /**
     * The main method to run the Pepse game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    private void initializeSky(WindowController windowController) {
        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), SKY_LAYER);
    }

    private int worldXToZone(float worldX) {
        return Math.floorDiv((int)Math.floor(worldX), (int)zoneWidth);
    }

    private int zoneStartX(int zoneIdx) {
        return (int) (zoneIdx * zoneWidth);
    }

    private int zoneEndX(int zoneIdx) {
        return (int) ((zoneIdx + 1) * zoneWidth);
    }

    /** A helper class to track a GameObject along with its layer.
     * Used for managing loaded zones in the game.
     * @author Eilam Soroka, Maayan Felig
     */
    private static class ObjInLayer {
        final GameObject obj;
        final int layer;
        ObjInLayer(GameObject obj, int layer) {
            this.obj = obj;
            this.layer = layer;
        }
    }
}
