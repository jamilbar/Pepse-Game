package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * This class represents the night in the game
 */
public class Night {
    /*The opacity of the night*/
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    /*The tag of the night*/
    private static final String NIGHT_TAG = "night";
    /**
     * Constructor.
     */
    public Night() {}
    /**
     * a static method that create a night GameObject
     * @param windowDimensions The dimensions of the window
     * @param cycleLength The length of the cycle
     * @return The night GameObject
     */

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        RectangleRenderable rectangleRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, rectangleRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<>(night, night.renderer()::setOpaqueness, 0f, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
