package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

public class Sun {
    private static final int MID_SCREEN_FACTOR = 2;
    private static final Vector2 SUN_SIZE = new Vector2(64, 64);
    private static final String SUN_TAG = "sun";
    private static final float START_CYCLE = 90f;
    private static final float END_CYCLE = 450f;

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Vector2 sunPosition = new Vector2(windowDimensions.x() / MID_SCREEN_FACTOR,
                  (windowDimensions.y() * Terrain.getGroundHeightFactor()) / MID_SCREEN_FACTOR);
        GameObject sun = new GameObject(sunPosition, SUN_SIZE,
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        Vector2 initialSunCenter = sun.getCenter();
        new Transition<Float>(
                sun, // the game object being changed
                (Float angle) -> {
                    Vector2 cycleCenter = new Vector2(
                            windowDimensions.x() / 2f,
                            windowDimensions.y() * Terrain.getGroundHeightFactor()
                    );

                    float radius = Math.min(
                            windowDimensions.x() / 2f,
                            windowDimensions.y() * Terrain.getGroundHeightFactor()
                    ) - SUN_SIZE.y();

                    Vector2 offset = new Vector2(radius, 0).rotated(180f - angle);

                    sun.setCenter(cycleCenter.add(offset));
                },
                START_CYCLE,
                END_CYCLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return sun;
    }
}
