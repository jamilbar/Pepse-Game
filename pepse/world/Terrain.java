package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class represents the terrain in the game
 */
public class Terrain {
    /*The color of the ground*/
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    /*The depth of the terrain*/
    private static final int TERRAIN_DEPTH = 20;
    /**
     * The ratio of the height of the ground at x = 0 to the height of the window
     */
    public static final float RATIO = (float) 2 / 3;
    /*The factor of the noise*/
    private static final int FACTOR = Block.SIZE * 7;
    /*The dimensions of the window*/
    private final Vector2 windowDimensions;
    /*The ground height at x = 0*/
    private final int groundHeightAtX0;
    /*instance of NoiseGenerator class*/
    private final NoiseGenerator noiseGenerator;
    /**The function that return the ground height at position x*/
    public Function<Float, Float> groundHeight = this::groundHeightAt;
    /**
     * Construct a new Terrain instance.
     *
     * @param windowDimensions The dimensions of the window
     * @param seed The seed of the terrain
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = (int) (windowDimensions.y() * RATIO);
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, groundHeightAtX0);

    }
    /**
     * this method return the ground height at position x
     * @param x The x position
     * @return The ground height at position x
     */
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, FACTOR);
        return groundHeightAtX0 + noise;
    }
    /**
     * this method create a list of Blocks that represent the terrain in the game
     * @param minX the minimum x value to put a block
     * @param maxX the maximum x value to put a block
     * @return a  list of blocks
     */
    public List<Block> createInRange(int minX, int maxX){
        List<Block> blockList = new ArrayList<>();
        minX =  (minX / Block.SIZE) * Block.SIZE;
        maxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;
        for (int height = 0 ; height < windowDimensions.y() ; height += Block.SIZE) {
            for (int xCoordination = minX; xCoordination < maxX; xCoordination += Block.SIZE) {
                float YCoordination = (float) (Math.floor(groundHeightAt(xCoordination) / Block.SIZE) *
                        Block.SIZE);
                Block block = new Block(new Vector2(xCoordination, YCoordination + height),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                blockList.add(block);
            }
        }
        return blockList;
    }
}
