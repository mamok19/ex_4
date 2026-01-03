package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;

import static pepse.Constants.BLOCK_SIZE;
/**
 * A block GameObject.
 * Blocks are basic GameObjects that other GameObjects can be constructed from.
 * @author Eilam Soroka, Maayan Felig
 */
public class Block extends GameObject {


    /**
     * Constructs a new Block.
     * @param topLeftCorner The top-left corner of the block.
     * @param renderable The renderable to use for the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(BLOCK_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Gets the size of the block.
     * @return The size of the block.
     */
    public static int getSize() {
        return BLOCK_SIZE;
    }

}
