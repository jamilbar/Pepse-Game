package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * This class represents the sun halo in the game
 */
public class SunHalo{
    /*The color of the sun halo*/
    private static final Color SUN_HALO_COLOR = new Color(255,255,0,20);
    /*The dimensions of the sun halo*/
    private static final Vector2 SUN_HALO_DIMENSIONS = new Vector2(200, 200);
    /*The radius of the sun halo*/
    private static final int SUN_HALO_RADIUS = 25;
    /*The tag of the sun halo*/
    private static final String SUN_HALO_TAG = "sunHalo";

    /**
     * Constructor.
     */
    public SunHalo() {}
    /**
     * a static method that create a sun halo GameObject
     * @param sun The sun GameObject
     * @return The sun halo GameObject
     */
    public static GameObject create(GameObject sun){
        Vector2 topLeftCorner = new Vector2(sun.getTopLeftCorner().x() - SUN_HALO_RADIUS ,
                sun.getTopLeftCorner().y() - SUN_HALO_RADIUS);
        GameObject sunHalo = new GameObject(topLeftCorner, SUN_HALO_DIMENSIONS,
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }




}
