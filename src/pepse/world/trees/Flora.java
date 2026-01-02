package pepse.world.trees;

import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

public class Flora {

    private static final int MIN_SPACE_BETWEEN_TREES = 150;
    private static final int FIRST_POSSIBLE_TREE_X = 10;
    private static final double TREE_CHANCE = 0.8;
    private final int seed;
    private final UnaryOperator<Float> groundHeightAt;

    public Flora(int seed, UnaryOperator<Float> groundHeightAt) {
        this.seed = seed;
        this.groundHeightAt = groundHeightAt;
    }

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
