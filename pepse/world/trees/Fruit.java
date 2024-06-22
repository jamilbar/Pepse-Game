package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.TimerTask;
import java.util.function.Supplier;

/**
 * This class represents the fruit in the game
 */
public class Fruit extends GameObject{
    /*The Runnable that updates the energy of the avatar*/
    private final Runnable updateEnergy;
    /*this Supplier return true if we need to change the fruit color to yellow,otherwise to false */
    private final Supplier<Boolean> changeFruitColor;
    /*The time to wait before re-adding the fruit to the screen*/
    private static final int WAIT_TIME = 30;
    /*The tag of the avatar*/
    private static final String AVATAR_TAG = "avatar";
    /*The tag of the fruit*/
    private static final String FRUIT_TAG = "fruit";

    /**
     * Constructor.
     *
     * @param topLeftCorner Position of the object, in window coordinates.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object.
     * @param updateEnergy  The Runnable that updates the energy of the avatar
     * @param changeFruitColor    The Supplier that returns true if the avatar is jumping
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable ,
                 Runnable updateEnergy, Supplier<Boolean> changeFruitColor) {
        super(topLeftCorner, dimensions, renderable);
        this.updateEnergy = updateEnergy;
        this.changeFruitColor = changeFruitColor;
        this.setTag(FRUIT_TAG);
    }

    /**
     * when the collision occurs, the fruit will be removed from the gameObjects and then added back after
     * 30 seconds
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(AVATAR_TAG) && renderer().getOpaqueness()!=0) {
            renderer().setOpaqueness(0);
            new ScheduledTask(this,WAIT_TIME,false,
                    ()->renderer().setOpaqueness(1));
            this.updateEnergy.run();

        }
    }

    /**
     * update the fruit color based on the avatar's jump status.
     * @param deltaTime The time that has passed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (changeFruitColor.get()){
            renderer().setRenderable(new OvalRenderable(Color.yellow));
        }
        else {
            renderer().setRenderable(new OvalRenderable(Color.RED));
        }

    }

}
