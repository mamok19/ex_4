package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Tree {

    private static final int MIN_TREE_HEIGHT = 4;
    private static final int MAX_TREE_HEIGHT_ADDITION = 4;
    private static final int TREE_WIDTH = 2;
    public static final Color BASE_TRUNK_COLOR = new Color(139, 69, 19);
    private static final Renderable TRUNK_RENDERABLE =
            new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
    private static final int TREE_TOP_SIZE = 6;
    private static final double LEAF_CHANCE = 0.7;
    private static final double FRUIT_CHANCE = 0.1;
    private final Random random;
    private final int trunkHeight;
    private final List<Block> TrunkBlocks = new ArrayList<>();
    private final List<Leaf> allLeaves = new ArrayList<>();
    private final List<Fruit> allFruit = new ArrayList<>();


    /**
     * Constructor for a Tree object that creates the trunk, leaves, and fruit.
     * @param bottomLeftCorner the bottom left corner where the tree trunk should start
     * @param seed a seed for random number generation to ensure reproducibility
     */
    public Tree(Vector2 bottomLeftCorner, int seed) {
        this.random = new Random(Objects.hash(bottomLeftCorner.x(), seed));
        this.trunkHeight = MIN_TREE_HEIGHT + (random.nextInt(MAX_TREE_HEIGHT_ADDITION));
        createTreeTrunk(bottomLeftCorner);
        createLeavesAndFruit();;
    }

    private void createTreeTrunk(Vector2 bottomLeftCorner) {
        for (int i = 0; i < this.trunkHeight; i++) {//check whether i should start at 0 or 1
            for (int j = 0; j < TREE_WIDTH; j++) {
                Vector2 blockTopLeftCorner = new Vector2(bottomLeftCorner.x() + j * Block.getSize(),
                        bottomLeftCorner.y() - (i + 1) * Block.getSize());
                Block trunkBlock = new Block(blockTopLeftCorner, TRUNK_RENDERABLE);
                trunkBlock.setTag(Constants.TRUNK_BLOCK_TAG);
                TrunkBlocks.add(trunkBlock);
            }
        }
    }

    /**
     * getter for the trunk blocks of the tree
     * @return list of trunk blocks
     */
    public List<Block> getTrunkBlocks() {
        return TrunkBlocks;
    }


    private void createLeavesAndFruit() {
        Vector2 treeTopParticleTopLeftCorner = new Vector2(
                TrunkBlocks.getFirst().getTopLeftCorner().x() -
                        ((float) (TREE_TOP_SIZE * Block.getSize()) / 2),
                TrunkBlocks.getLast().getTopLeftCorner().y());
        for (int i = 0; i < TREE_TOP_SIZE; i++) {
            for(int j = 0; j < TREE_TOP_SIZE; j++) {
                if (random.nextDouble() < LEAF_CHANCE){
                    Leaf leaf = new Leaf(treeTopParticleTopLeftCorner);
                    leaf.setTag(Constants.LEAF_TAG);
                    allLeaves.add(leaf);
                }
                if (random.nextDouble() >= LEAF_CHANCE && random.nextDouble() < LEAF_CHANCE + FRUIT_CHANCE){
                    Fruit fruit = new Fruit(treeTopParticleTopLeftCorner);
                    fruit.setTag(Constants.FRUIT_TAG);
                    allFruit.add(fruit);
                }
                treeTopParticleTopLeftCorner = treeTopParticleTopLeftCorner.subtract(
                        new Vector2(i * Block.getSize(), j * Block.getSize()));
            }
        }
    }

    /**
     * getter for all leaves on the tree
     * @return list of all leaves on the tree
     */
    public List<Leaf> getAllLeaves() {
        return allLeaves;
    }

    /**
     * getter for all fruit on the tree
     * @return list of all fruit on the tree
     */
    public List<Fruit> getAllFruit() {
        return allFruit;
    }
}
