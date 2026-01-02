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
    private static final int TREE_WIDTH = 1;
    public static final Color BASE_TRUNK_COLOR = new Color(139, 69, 19);
    private static final Renderable TRUNK_RENDERABLE =
            new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
    private static final int TREE_TOP_RADIUS = 3;
    private static final double LEAF_CHANCE = 0.4;
    private static final double FRUIT_CHANCE = 0.08;
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
        createTreeTop();
        createWindEffectForLeaves();
    }

    private void createWindEffectForLeaves() {
        for (Leaf leaf : allLeaves) {
            float timeToStart = random.nextFloat() * Constants.WIND_EFFECT_DIFFERENCES;
            leaf.createWindEffect(timeToStart);
        }
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

    private void createTreeTop(){
        Vector2 treeTopCenter = new Vector2(
                TrunkBlocks.getFirst().getTopLeftCorner().x() + Block.getSize(),
                TrunkBlocks.getLast().getTopLeftCorner().y() - (float) this.trunkHeight * Block.getSize()/2
        );
        for (int i = -TREE_TOP_RADIUS; i <= TREE_TOP_RADIUS; i++) {
            for (int j = -TREE_TOP_RADIUS; j <= TREE_TOP_RADIUS; j++) {
                double distance = Math.sqrt(i * i + j * j);
                if (distance > TREE_TOP_RADIUS) {
                    continue;
                }
                Vector2 particleTopLeftCorner = treeTopCenter.add(
                        new Vector2(i * Block.getSize(), j * Block.getSize())//todo check whether it's ok
                        // to have Block mentioned here or whether we should have a constant instead
                );
                double curRandom = random.nextDouble();

                if (curRandom < LEAF_CHANCE) {
                    Leaf leaf = new Leaf(particleTopLeftCorner);
                    leaf.setTag(Constants.LEAF_TAG);
                    allLeaves.add(leaf);
                }
                else if (curRandom < LEAF_CHANCE + FRUIT_CHANCE) {
                    Fruit fruit = new Fruit(particleTopLeftCorner);
                    fruit.setTag(Constants.FRUIT_TAG);
                    allFruit.add(fruit);
                }
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
