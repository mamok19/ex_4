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
import pepse.world.trees.Fruit;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;

import java.util.List;

/**
 * The main class of the Pepse game.
 * incharge of initializing and running the game.
 * @author Eilam Soroka, Maayan Felig
 */
public class PepseGameManager extends GameManager {

    private static final int FRUIT_LAYER = Layer.FOREGROUND + 1;
    private static final int SKY_LAYER = -250;
    private static final int SEED = 73;
    private static final int CYCLE_OF_DAY_LENGTH = 10;
    private static final String TOP_LAYER_TAG = "topGroundBlock";
    private static final int SUN_LAYER = -225;
    private static final float FIRST_X_POSITION = 0f;
    private float groundHeightAtX0;
    private Avatar avatar; // todo check if it is ok that i hold the avatar object here

    /**
     * Initializes the game by setting up the sky and terrain.
     *
     * @param imageReader     Provides functionality for reading images.
     * @param soundReader     Provides functionality for playing sounds.
     * @param inputListener   Listens for user input.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND,Layer.STATIC_OBJECTS,true);
        gameObjects().layers().shouldLayersCollide(Layer.FOREGROUND,FRUIT_LAYER,true);
        initializeSky(windowController);
        initializeTerrain(windowController);
        this.groundHeightAtX0 = Terrain.groundHeightAtX0(windowController.getWindowDimensions());
        initializeNight(windowController);
        initializeSun(windowController);
        initializeMoon(windowController);
        //testing - will be removed
//        Fruit fruit = new Fruit(new Vector2(200, 200));;
//        gameObjects().addGameObject(fruit, FRUIT_LAYER);
//        Leaf leaf = new Leaf(new Vector2(300, 300));
//        gameObjects().addGameObject(leaf, Layer.FOREGROUND);
//        leaf.createWindEffect(0f);
        Tree tree = new Tree(new Vector2(150,
                Terrain.groundHeightAtX0(windowController.getWindowDimensions())-30),73);
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
//        gameObjects().addGameObject(tree, Layer.FOREGROUND + 1);
        // end of testing

        initializeAvatar(new Vector2(FIRST_X_POSITION,groundHeightAtX0),inputListener, imageReader,
                windowController);
        initializeEnergyDisplay(windowController);

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
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        for (Block block : terrain.createInRange(0, (int) windowController.getWindowDimensions().x())) { //todo change range
            if (block.getTag() == TOP_LAYER_TAG) {
                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
                continue;
            }
            gameObjects().addGameObject(block, Layer.BACKGROUND);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    private void initializeSky(WindowController windowController) {
        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), SKY_LAYER);
    }
}
