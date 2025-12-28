package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

public class Moon {
    private static final int MID_SCREEN_FACTOR = 2;
    private static final Vector2 MOON_SIZE = new Vector2(64, 64);
    private static final String MOON_TAG = "moon";
    private static final float START_CYCLE = -90f;
    private static final float END_CYCLE = 270f;

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Vector2 moonPosition = new Vector2(windowDimensions.x() / MID_SCREEN_FACTOR,
                (windowDimensions.y() * Terrain.getGroundHeightFactor()) / MID_SCREEN_FACTOR);
        GameObject moon = new GameObject(moonPosition, MOON_SIZE,
                new OvalRenderable(Color.GRAY));
        moon.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        moon.setTag(MOON_TAG);
        Vector2 initialSunCenter = moon.getCenter();
        new Transition<Float>(
                moon, // the game object being changed
                (Float angle) -> {
                    Vector2 cycleCenter = new Vector2(
                            windowDimensions.x() / 2f,
                            windowDimensions.y() * Terrain.getGroundHeightFactor()
                    );

                    float radius = Math.min(
                            windowDimensions.x() / 2f,
                            windowDimensions.y() * Terrain.getGroundHeightFactor()
                    ) - MOON_SIZE.y();

                    Vector2 offset = new Vector2(radius, 0).rotated(180f - angle);

                    moon.setCenter(cycleCenter.add(offset));
                },
                START_CYCLE,
                END_CYCLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return moon;
    }
}

