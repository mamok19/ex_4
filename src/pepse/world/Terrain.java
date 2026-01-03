package pepse.world;

import danogl.util.Vector2;
import pepse.Constants;
import pepse.utils.NoiseGenerator;
import danogl.gui.rendering.RectangleRenderable;
import pepse.utils.ColorSupplier;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static pepse.Constants.*;

/**
 * Represents the terrain of the game world.
 * The terrain's height is determined using a noise generator to create a natural, uneven surface.
 * @author Eilam Soroka, Maayan Felig
 */
public class Terrain {
    private final Vector2 windowDimensions;
    private final int seed;
    private final NoiseGenerator noiseGenerator;

    /**
     * Constructs a Terrain object.
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed for the noise generator to ensure consistent terrain generation.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.noiseGenerator = new NoiseGenerator(seed, (int)(GROUND_HEIGHT_FACTOR * windowDimensions.y()));
    }
    /**
     * Calculates the ground height at a given x-coordinate using the noise generator.
     * @param x The x-coordinate.
     * @return The ground height at the specified x-coordinate.
     */
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x,NOISE_FACTOR_TERRAIN);
        return (GROUND_HEIGHT_FACTOR * windowDimensions.y() )+ noise;
    }

    /**
     * Creates terrain blocks within the specified x-coordinate range.
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of terrain blocks within the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {

        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x += BLOCK_SIZE) {
            float groundHeight = groundHeightAt(x);
            Block topBlock = new Block(new Vector2(x, (int) groundHeight),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            topBlock.setTag(TOP_LAYER_GROUND_BLOCK_TAG);
            blocks.add(topBlock);
            for (int y = (int) (groundHeight + BLOCK_SIZE);
                 y < (int) groundHeight + (TERRAIN_DEPTH * BLOCK_SIZE); y += BLOCK_SIZE) {
                Block newBlock = new Block(new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                newBlock.setTag(LOWER_LAYER_GROUND_BLOCK_TAG);
                blocks.add(newBlock);
            }
        }
        return blocks;
    }

    /**
     * Static method to get the ground height at x=0 based on window dimensions.
     * @param windowDimensions The dimensions of the game window.
     * @return The ground height at x=0.
     */
    public static float groundHeightAtX0(Vector2 windowDimensions) {
        return GROUND_HEIGHT_FACTOR * windowDimensions.y();
    }

    /** Getter for GROUND_HEIGHT_FACTOR
     * @return GROUND_HEIGHT_FACTOR
     * */
    public static float getGroundHeightFactor() {
        return GROUND_HEIGHT_FACTOR;
    }
    /**
     * Getter for TERRAIN_DEPTH
     * @return TERRAIN_DEPTH
     * */
    public static float getDepth() {
        return TERRAIN_DEPTH;
    }

}
