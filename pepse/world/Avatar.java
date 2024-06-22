package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
/**
 * This class represents the avatar in the game
 */

public class Avatar extends GameObject {
    /*The time between clips*/
    private static final double TIME_BETWEEN_CLIPS = 0.2;
    /*The minimum energy required to jump*/
    private static final int MINIMUM_ENERGY_TO_JUMP = 10;
    /*The minimum energy required to move*/
    private static final double MINIMUM_ENERGY_TO_MOVE = 0.5;
    /*The factor by which the avatar's size is multiplied*/
    private static final int FACTOR = 50;
    /*The velocity of the avatar in the x direction*/
    private static final float VELOCITY_X = 400;
    /*The velocity of the avatar in the y direction*/
    private static final float VELOCITY_Y = -650;
    /*The gravity*/
    private static final float GRAVITY = 600;
    /*The color of the avatar*/
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    /*The maximum energy of the avatar*/
    private static final double MAXIMUM_ENERGY = 100;
    /*The tag of the avatar*/
    private static final String AVATAR_TAG = "avatar";
    /** change the fruit color to yellow after every jump of the avatar*/
    public boolean changeFruitColorToYellow = false;
    /*The UserInputListener that handles user input*/
    private final UserInputListener inputListener;
    /*The list of AnimationRenderables that represent the avatar*/
    private final List<AnimationRenderable> animationRenderableList;
    /*The ImageReader that reads the images*/
    private final ImageReader imageReader;
    /*The energy of the avatar*/
    private double energy;
    /**
     * A Supplier that creates a TextRenderable that represents the energy of the avatar
     */
    public Supplier<TextRenderable> createTextRenderable = () -> {
        TextRenderable textRenderable = new TextRenderable((int) Math.ceil(energy) + "%");
        textRenderable.setColor(Color.BLACK);
        return textRenderable;
    };
    /**
     * A Runnable that updates the energy of the avatar
     */
    public Runnable updateEnergy = () -> energy = Math.min(MAXIMUM_ENERGY, energy + MINIMUM_ENERGY_TO_JUMP);
    /**
     * A Supplier that returns true if the fruit changed its color to yellow when the avatar is jumping
     */
    public Supplier<Boolean> changeFruitColor = () -> changeFruitColorToYellow;


    /**
     * Construct a new GameObject instance.
     *
     * @param pos The position of the object, in window coordinates.
     * @param inputListener The input listener to use for handling user input.
     * @param imageReader The image reader to use for loading images.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(FACTOR),
                imageReader.readImage("assets/idle_0.png", true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        setTag(AVATAR_TAG);
        this.inputListener = inputListener;
        this.energy = MAXIMUM_ENERGY;
        this.imageReader = imageReader;
        animationRenderableList = createAnimationRenderable();
    }

    /**
     * update the avatar's energy and velocity based on the user input
     * @param deltaTime The time that has passed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (getVelocity().equals(Vector2.ZERO)) {
            energy = Math.min(MAXIMUM_ENERGY, energy + 1);
            renderer().setRenderable(animationRenderableList.get(0));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= MINIMUM_ENERGY_TO_MOVE) {
            xVel -= VELOCITY_X;
            energy -= MINIMUM_ENERGY_TO_MOVE;
            renderer().setRenderable(animationRenderableList.get(2));
            renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= MINIMUM_ENERGY_TO_MOVE) {
            xVel += VELOCITY_X;
            energy -= MINIMUM_ENERGY_TO_MOVE;
            renderer().setRenderable(animationRenderableList.get(2));
            renderer().setIsFlippedHorizontally(false);

        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                energy >= MINIMUM_ENERGY_TO_JUMP) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= MINIMUM_ENERGY_TO_JUMP;
            renderer().setRenderable(animationRenderableList.get(1));
            if (changeFruitColorToYellow){
                changeFruitColorToYellow = false;

            }else{
                changeFruitColorToYellow = true;
                PepseGameManager.trunkChangedColor = false;

            }
        }



    }

    /*create a list of AnimationRenderables that represent the avatar */
    private List<AnimationRenderable> createAnimationRenderable() {
        List<AnimationRenderable> animationRenderableList = new ArrayList<>();
        // avatar not moving
        Renderable[] avatarInPlace = {imageReader.readImage("assets/idle_0.png",
                true),
                imageReader.readImage("assets/idle_1.png", true),
                imageReader.readImage("assets/idle_2.png", true),
                imageReader.readImage("assets/idle_3.png", true)};
        animationRenderableList.add(new AnimationRenderable(avatarInPlace, TIME_BETWEEN_CLIPS));

        // avatar is jumping
        Renderable[] avatarIsJump = {imageReader.readImage("assets/jump_0.png",
                true),
                imageReader.readImage("assets/jump_1.png", true),
                imageReader.readImage("assets/jump_2.png", true),
                imageReader.readImage("assets/jump_3.png", true)};

        animationRenderableList.add(new AnimationRenderable(avatarIsJump, TIME_BETWEEN_CLIPS));

        // avatar is moving right/left
        Renderable[] avatarIsRun = {imageReader.readImage("assets/run_0.png",
                true),
                imageReader.readImage("assets/run_1.png", true),
                imageReader.readImage("assets/run_2.png", true),
                imageReader.readImage("assets/run_3.png", true),
                imageReader.readImage("assets/run_4.png", true),
                imageReader.readImage("assets/run_5.png", true)};

        animationRenderableList.add(new AnimationRenderable(avatarIsRun, TIME_BETWEEN_CLIPS));
        return animationRenderableList;
    }
}

