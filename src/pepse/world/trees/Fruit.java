package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;
import pepse.utils.ColorSupplier;
import pepse.world.Block;
import pepse.world.avatar.Avatar;

import java.awt.*;

/**
 * A fruit GameObject that can be collected by the avatar.
 * When collected, it disappears for a short period before reappearing.
 * @author Eilam Soroka, Maayan Felig
 */
public class Fruit extends Block {

    private static final Renderable FRUIT_RENDERABLE =
            new OvalRenderable(ColorSupplier.approximateColor(Color.ORANGE));
    private boolean isCollected = false;

    /**
     * Constructs a new Fruit.
     * @param TopLeftCorner The top-left corner of the fruit.
     */
    public Fruit(Vector2 TopLeftCorner)
    {
        super(TopLeftCorner, FRUIT_RENDERABLE);
        this.setTag(Constants.FRUIT_TAG);
    }

    /* * Determines if the fruit should collide with another GameObject.
     * @param other The other GameObject to check collision with.
     * @return true if the other GameObject is the avatar, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Constants.AVATAR_TAG);
    }

    /**
     * Handles the collision event when the fruit collides with another GameObject.
     * If the other GameObject is the avatar and the fruit is not already collected,
     * it marks the fruit as collected, makes it disappear, and schedules it to reappear
     * after a set duration. It also adds energy to the avatar.
     * @param other The other GameObject involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Constants.AVATAR_TAG) && !isCollected) {
            isCollected = true;
            renderer().setRenderable(null);
            this.setDimensions(Vector2.ZERO);

            new ScheduledTask(this,
                    Constants.CYCLE_LENGTH,
                    false,
                    this::reappear);
            //todo add 10 points of energy to avatar
            ((Avatar) other).addEnergy(Constants.FRUIT_ENERGY);
        }
    }

    private void reappear() {
        this.isCollected = false;
        renderer().setRenderable(FRUIT_RENDERABLE);
        this.setDimensions(Vector2.ONES.mult(Block.getSize()));
    }
}
