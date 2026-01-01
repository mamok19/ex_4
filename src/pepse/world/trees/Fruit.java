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
import java.awt.*;

public class Fruit extends Block {

    private static final Renderable FRUIT_RENDERABLE =
            new OvalRenderable(ColorSupplier.approximateColor(Color.ORANGE));
    private boolean isCollected = false;

    public Fruit(Vector2 TopLeftCorner)
    {
        super(TopLeftCorner, FRUIT_RENDERABLE);
        this.setTag(Constants.FRUIT_TAG);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Constants.AVATAR_TAG);
    }

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
        }
    }

    private void reappear() {
        this.isCollected = false;
        renderer().setRenderable(FRUIT_RENDERABLE);
        this.setDimensions(Vector2.ONES.mult(this.getSize()));
    }
}
