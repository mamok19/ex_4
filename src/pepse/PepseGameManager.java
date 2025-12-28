package pepse;
import danogl.GameObject;
import danogl.collisions.Layer;
import pepse.world.Block;
import pepse.world.Sky;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Terrain;
import pepse.world.daynight.Moon;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

/**
 * The main class of the Pepse game.
 * incharge of initializing and running the game.
 * @author Eilam Soroka, Maayan Felig
 */
public class PepseGameManager extends GameManager {

    private static final int SKY_LAYER = -250;
    private static final int SEED = 73;
    private static final int CYCLE_OF_DAY_LENGTH = 10;
    private static final String TOP_LAYER_TAG = "topGroundBlock";
    private static final int SUN_LAYER = -225;

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

        initializeSky(windowController);
        initializeTerrain(windowController);
        initializeNight(windowController);
        initializeSun(windowController);
        initializeMoon(windowController);

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
