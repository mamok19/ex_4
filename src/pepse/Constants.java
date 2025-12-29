package pepse;

import java.awt.*;

public class Constants {
    //sky
    public static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static  final String SKY_TAG = "sky";

    //Block
    public static final int BLOCK_SIZE = 30;

    //Terrain
    public static final int START_x0 = 0;
    public static final double FACTOR_TERRAIN = 7;
    public static final double NOISE_FACTOR_TERRAIN = BLOCK_SIZE * FACTOR_TERRAIN;
    public static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public static final int TERRAIN_DEPTH = 20;
    public static final String TOP_LAYER_GROUND_BLOCK_TAG = "topGroundBlock";
    public static final String LOWER_LAYER_GROUND_BLOCK_TAG = "lowerGroundBlock";
    public static final float GROUND_HEIGHT_FACTOR = (2/3.0f);
}
