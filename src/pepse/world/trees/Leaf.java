package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
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

    public Leaf(Vector2 TopLeftCorner) {
        super(TopLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
    }
}
