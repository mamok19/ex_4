package pepse.world;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.utils.NoiseGenerator;
import danogl.gui.rendering.RectangleRenderable;
import pepse.utils.ColorSupplier;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
/**
 * Represents the terrain of the game world.
 * The terrain's height is determined using a noise generator to create a natural, uneven surface.
 * @author Eilam Soroka, Maayan Felig
 */
public class Terrain {
    private static final int START_x0 = 0;
    private static final double FACTOR_TERRAIN = 7;
    private static final double NOISE_FACTOR_TERRAIN = Block.SIZE * FACTOR_TERRAIN;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final int seed;
    private final NoiseGenerator noiseGenerator;

    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        //Random rand = new Random(Objects.hash(START_x0, seed));
        //this.groundHeightAtX0 = rand.nextInt((int)(windowDimensions.y()*(2/3.0)), (int)(windowDimensions.y()*(5/6.0)));
        this.groundHeightAtX0 = (2/3.0f) * windowDimensions.y();
        this.noiseGenerator = new NoiseGenerator(seed, (int)groundHeightAtX0);
    }
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x,NOISE_FACTOR_TERRAIN);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        
        Block block = new Block(Vector2.ZERO,
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            float groundHeight = groundHeightAt(x);
            Block topBlock = new Block(new Vector2(x, (int) groundHeight),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            topBlock.setTag("topGroundBlock");
            blocks.add(topBlock);
            for (int y = (int) (groundHeight + Block.SIZE);
                 y < (int) groundHeight + (TERRAIN_DEPTH * Block.SIZE); y += Block.SIZE) {
                Block newBlock = new Block(new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                newBlock.setTag("lowerGroundBlock");
                blocks.add(newBlock);
            }
        }
        return blocks;
    }

}
