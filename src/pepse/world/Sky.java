package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.Constants;

import java.awt.*;

/** * A class responsible for creating the sky background.
 * @author Eilam Soroka, Maayan Felig
 */
public class Sky {

    /**
     * Creates a sky GameObject.
     * @param windowDimensions The dimensions of the window.
     * @return The sky GameObject.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky =new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(Constants.BASIC_SKY_COLOR)
        );
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(Constants.SKY_TAG);
        return sky;
    }
}
