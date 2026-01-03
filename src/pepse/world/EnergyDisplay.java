package pepse.world;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The energy display of the game.
 * Displays the player's remaining energy as text on the screen.
 * @author Eilam Soroka, Maayan Felig
 */
public class EnergyDisplay {

    private static final Vector2 ENERGY_DISPLAY_POSITION = new Vector2(20, 20);
    private static final Vector2 ENERGY_DISPLAY_SIZE = new Vector2(150, 50);
    private static final String ENERGY_TEXT_SUFFIX = "%";

    /**
     * Creates an energy text display GameObject.
     *
     * @param energySupplier A supplier of the player's current energy.
     * @param addGameObject  A consumer that adds a GameObject to the game.
     */
    public EnergyDisplay(Supplier<Integer> energySupplier,
                         BiConsumer<GameObject, Integer> addGameObject) {

        int initialEnergy = energySupplier.get();
        TextRenderable textRenderable =
                new TextRenderable(initialEnergy + ENERGY_TEXT_SUFFIX);
        GameObject energyText = new GameObject(
                ENERGY_DISPLAY_POSITION,
                ENERGY_DISPLAY_SIZE,
                textRenderable
        );
        energyText.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        final int[] lastEnergy = {initialEnergy};
        energyText.addComponent(deltaTime -> {
            int currentEnergy = energySupplier.get();
            if (currentEnergy != lastEnergy[0]) {
                textRenderable.setString(currentEnergy + ENERGY_TEXT_SUFFIX);
                lastEnergy[0] = currentEnergy;
            }
        });
        addGameObject.accept(energyText, Layer.UI);
    }
}
