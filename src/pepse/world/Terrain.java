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

    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        //Random rand = new Random(Objects.hash(START_x0, seed));
        //this.groundHeightAtX0 = rand.nextInt((int)(windowDimensions.y()*(2/3.0)), (int)(windowDimensions.y()*(5/6.0)));
//        this.groundHeightAtX0 = (2/3.0f) * windowDimensions.y();
        this.noiseGenerator = new NoiseGenerator(seed, (int)(GROUND_HEIGHT_FACTOR * windowDimensions.y()));
    }
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x,NOISE_FACTOR_TERRAIN);
        return (GROUND_HEIGHT_FACTOR * windowDimensions.y() )+ noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        
        Block block = new Block(Vector2.ZERO,
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
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


    public static float getGroundHeightFactor() {
        return GROUND_HEIGHT_FACTOR;
    }
    public static float getDepth() {
        return TERRAIN_DEPTH;
    }

}
