package pepse.world.trees;

import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;
/**
 * A class responsible for creating flora (trees) in the game world.
 * @author Eilam Soroka, Maayan Felig
 */
public class Flora {

    private static final int MIN_SPACE_BETWEEN_TREES = 150;
    private static final int FIRST_POSSIBLE_TREE_X = 10;
    private static final double TREE_CHANCE = 0.8;
    private final int seed;
    private final UnaryOperator<Float> groundHeightAt;

    /**
     * Constructs a Flora object.
     * @param seed The seed for random number generation to ensure consistent tree placement.
     * @param groundHeightAt A function that provides the ground height at a given x-coordinate.
     */
    public Flora(int seed, UnaryOperator<Float> groundHeightAt) {
        this.seed = seed;
        this.groundHeightAt = groundHeightAt;
    }

    /**
     * Creates trees within the specified x-coordinate range.
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of trees within the specified range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        ArrayList<Tree> trees = new ArrayList<>();
        for (int i = minX; i <= maxX; i += MIN_SPACE_BETWEEN_TREES) {
            if(i == 0)
                continue;
            Random random = new Random(Objects.hash(seed, i));
            if (random.nextDouble() < TREE_CHANCE) {
                Tree tree = new Tree(new Vector2((float) i, this.groundHeightAt.apply((float) i)), this.seed);
                trees.add(tree);
            }
        }
        return trees;
    }
}
