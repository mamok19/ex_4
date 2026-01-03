package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * Simulates the night effect by gradually increasing and decreasing a dark overlay's opacity.
 * @author Eilam Soroka, Maayan Felig
 */
public class Night {

    private static final float NOON_OPACITY = 0.0f;
    private static final float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";

    /**
     * Creates a GameObject to simulate night by overlaying a dark semi-transparent rectangle.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength      The duration of a full day-night cycle
     * @return A GameObject representing the night effect
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night,
                night.renderer()::setOpaqueness,
                NOON_OPACITY,
                MIDNIGHT_OPACITY,
                danogl.components.Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
