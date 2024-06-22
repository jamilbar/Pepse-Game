package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

/**
 * This class represents the Leafs of the tree
 */
public class Leafs {
    /*The color of the leaf*/
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    /*The dimensions of the leaf*/
    private static final int LEAF_SIZE = 25;
    /*The initial angle of the leaf*/
    private static final float INITIAL_ANGLE = 20f;
    /*The time it takes for the leaf to rotate*/
    private static final float TIME = 10f;
    /*The time it takes for the leaf to change its dimensions*/
    private static final float TRANSITION_TIME_FOR_CHANGING_DIMENSIONS = 5f;
    /**The minimum transition time for leaf to rotate*/
    public static final float MINIMUM_TRANSITION_TIME = 0.1f;
    /**The maximum transition time for leaf to rotate*/
    public static final float MAXIMUM_TRANSITION_TIME = 1f;
    /*The tag of the leaf*/
    private static final String LEAF_TAG = "leaf";


    /**
     * Constructor
     */
    public Leafs() {}

    /**
     * a static method that create a leaf GameObject, with a wait time before it starts to fall
     * @param pos      The position of the leaf
     * @return The leaf GameObject
     */
    public static GameObject createLeaf(Vector2 pos) {
        Random random = new Random();
        float randomInitialValue = random.nextFloat();
        float randomTransitionTime = random.nextFloat();
        float factor = randomTransitionTime * (MAXIMUM_TRANSITION_TIME - MINIMUM_TRANSITION_TIME) +
                MAXIMUM_TRANSITION_TIME;
        RectangleRenderable rectangleRenderable = new RectangleRenderable(LEAF_COLOR);
        GameObject leaf = new GameObject(pos, new Vector2(LEAF_SIZE, LEAF_SIZE), rectangleRenderable);
        leaf.setTag(LEAF_TAG);
        Runnable func = () -> {
            new Transition<>(leaf, leaf.renderer()::setRenderableAngle,
                    INITIAL_ANGLE * randomInitialValue ,
                    -(INITIAL_ANGLE * randomInitialValue),
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    TIME * factor,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                    null);
            new Transition<>(leaf, x -> leaf.setDimensions(new Vector2(x, leaf.getDimensions().y())),
                    (float)LEAF_SIZE , (float)Block.SIZE,
                    Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME_FOR_CHANGING_DIMENSIONS,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                    null);
        };
        new ScheduledTask(leaf,  factor, false, func);
        return leaf;
    }


}
