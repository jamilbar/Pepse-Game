package pepse.world.daynight;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.function.Consumer;

/**
 * The Sun class is a GameObject that represents the sun in the game world.
 * The sun is a circular object that moves in a circular path around the center of the game world.
 * The sun is rendered as a yellow circle.
 */
public class Sun {
    /*The width of the sun*/
    private static final int SUN_WIDTH = 150;
    /*The height of the sun*/
    private static final int SUN_HEIGHT = 150;
    /*The radius of the sun*/
    private static final int SUN_RADIUS = 75;
    /*The maximum circle angle*/
    private static final float MAXIMUM_ANGLE = 360f;
    /*The tag of the sun*/
    private static final String SUN_TAG = "sun";
    /**
     * Constructor.
     */
    public Sun() {}

    /**
     * Create a new Sun GameObject. the sun is created at the center of the game, and moves in a circular
     * path.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The time it takes for the sun to complete one full cycle.
     * @return The Sun GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        float groundHeight = windowDimensions.y() * Terrain.RATIO;
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, groundHeight);
        Vector2 topLeftCorner = new Vector2((windowDimensions.x() / 2) - SUN_RADIUS,
                (groundHeight / 2) - SUN_RADIUS);
        GameObject sun = new GameObject(topLeftCorner, new Vector2(SUN_WIDTH,SUN_HEIGHT),
                new OvalRenderable(Color.yellow));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, groundHeight / 2);
        Consumer<Float> sunMovement = (Float angle)->
                sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter));

        new Transition<>(sun, sunMovement, 0f, MAXIMUM_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP,null);
        return sun;

    }
}
