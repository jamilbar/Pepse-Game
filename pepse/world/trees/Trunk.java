package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

/**
 * This class represents the trunk of the tree
 */
public class Trunk {
    /*The maximum height of the tree*/
    private static final int MAXIMUM_TREE_HEIGHT = 380;
    /*The minimum height of the tree*/
    private static final int MINIMUM_TREE_HEIGHT = 120;
    /*The color of the trunk*/
    private static final Color TRUNK_COLOR = new Color(100,50,20);
    /*The width of the trunk*/
    private static final int TRUNK_WIDTH = 30;
    /*The tag of the trunk*/
    private static final String TRUNK_TAG = "trunk";

    /**
     * Constructor
     */
    public Trunk() {}
    /**
     * a static method that create a trunk GameObject
     * @param x The x position of the trunk
     * @param groundHeightAtX The ground height at position x
     * @return The trunk GameObject
     */
    public static GameObject createTrunk(float x, float groundHeightAtX){
        Random random = new Random();
        int treeHeight = random.nextInt(MAXIMUM_TREE_HEIGHT);
        if (treeHeight < MINIMUM_TREE_HEIGHT){
            treeHeight = MINIMUM_TREE_HEIGHT;
        }
        RectangleRenderable rectangleRenderable = new RectangleRenderable(TRUNK_COLOR);
        GameObject trunk = new GameObject(new Vector2(x, (groundHeightAtX - treeHeight)),
                new Vector2(TRUNK_WIDTH, (float) treeHeight), rectangleRenderable);
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        trunk.setTag(TRUNK_TAG);
        return trunk;
    }

}
