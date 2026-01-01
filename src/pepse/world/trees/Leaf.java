package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.Constants;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * A class that represents a leaf block in the game world.
 * Extends the Block class to inherit properties and behaviors of a block.
 * @author Eilam Soroka and Maayan Felig
 */
public class Leaf extends Block {
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);;
    private static final float LEAF_CHANGE_TIME = 2.0f;
    private static final float LEAF_ANGLE_DIFF = 20.0f;
    private static final float LEAF_SIZE_FACTOR = 2.0f;

    /**
     * Constructor for a Leaf object that moves with the wind.
     * @param TopLeftCorner top left corner where the leaf should appear on screen
     */
    public Leaf(Vector2 TopLeftCorner) {
        super(TopLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
        this.setTag(Constants.LEAF_TAG);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    /**
     * schedules the movement of a leaf in both its angle and dimensions
     * @param time the amount of time to wait before starting movement
     */
    public void createWindEffect(float time) {
        new ScheduledTask(this, time,true, () -> {
            changeLeafAngle();
            changeLeafDimensions();
        });
    }

    private void changeLeafAngle() {
        float startAngle = this.renderer().getRenderableAngle();
        float endAngle = startAngle + LEAF_ANGLE_DIFF;
        new Transition<>(this,
                (Float angle) -> this.renderer().setRenderableAngle(angle),
                startAngle,
                endAngle,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAF_CHANGE_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private void changeLeafDimensions() {
        Vector2 startSize = new Vector2(Block.getSize(), Block.getSize());
        Vector2 endSize = startSize.subtract(
                new Vector2((float) Block.getSize()/LEAF_SIZE_FACTOR,
                0));
        new Transition<>(this,
                this::setDimensions,
                startSize,
                endSize,
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                LEAF_CHANGE_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
                );
    }

}
