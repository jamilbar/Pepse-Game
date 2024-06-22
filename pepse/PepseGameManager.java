package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Leafs;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


/**
 * This class represents the game manager for the Pepse game.
 */

public class PepseGameManager extends GameManager {
    /*The length of the day-night cycle, in seconds.*/
    private static final float CYCLE_LENGTH = 30;
    /*The width of the text*/
    private static final int TEXT_WIDTH = 25;
    /*The height of the text*/
    private static final int TEXT_HEIGHT = 25;
    /*The color of the trunk*/
    private static final Color TRUNK_COLOR = new Color(100,50,20);
    /*The minimum x value for the flora*/
    private static final int MINIMUM_X = 60;
    /*The maximum angle of the leaf to rotate*/
    private static final float MAXIMUM_ANGEL = 90f;
    /*The trunk tag*/
    private static final String TRUNK_TAG = "trunk";
    /*The leaf tag*/
    private static final String LEAF_TAG = "leaf";
    /*The transition time for the leaf to rotate*/
    private static final int TRANSITION_TIME = 5;
    /**
     * A boolean that is true if the trunk changed color
     */
    public static boolean trunkChangedColor = false;
    /* The list of GameObjects that represent the flora in the game*/
    private List<GameObject> gameObjectList;
    /*The Supplier that returns true if the avatar is jumping*/
    private Supplier<Boolean> changeFruitColor;
    /*The UserInputListener that handles user input*/
    private UserInputListener inputListener;
    /*The random object*/
    private final Random random;

    /**
     * Constructor
     */
    public PepseGameManager(){
        random = new Random();
    }
    /**
     * Initialize the game.
     *
     * @param imageReader       The image reader to use for loading images.
     * @param soundReader       The sound reader to use for loading sounds.
     * @param inputListener     The input listener to use for handling user input.
     * @param windowController  The window controller to use for creating the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimension = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        // create sky
        GameObject sky = Sky.create(windowDimension);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // create terrain
        int seed = random.nextInt();
        Terrain terrain = new Terrain(windowDimension, seed);
        List<Block> blockList = terrain.createInRange(0, (int) windowDimension.x());
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        // create sun
        GameObject sun = Sun.create(windowDimension, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        // create sunHalo
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        // create night
        GameObject night = Night.create(windowDimension, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.UI);

        // create avatar
        Vector2 pos = new Vector2(0, windowDimension.y() * Terrain.RATIO - Block.SIZE);
        Avatar avatar = new Avatar(pos, inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        // create numeric energy
        NumericEnergy numericEnergy = new NumericEnergy(Vector2.ZERO, new Vector2(TEXT_WIDTH, TEXT_HEIGHT)
                    , avatar.createTextRenderable);
        gameObjects().addGameObject(numericEnergy, Layer.BACKGROUND);
        this.changeFruitColor = avatar.changeFruitColor;

        // create flora
        Flora flora = new Flora( terrain.groundHeight, avatar.updateEnergy, avatar.changeFruitColor);
        gameObjectList = flora.createInRange(MINIMUM_X, (int) windowDimension.x());
        for (GameObject game: gameObjectList){
            gameObjects().addGameObject(game, Layer.STATIC_OBJECTS);
        }
    }
    /**
     * change the color of the trunk when the avatar jumps, and rotate the leaves when the avatar jumps
     * @param deltaTime The time that has passed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (changeFruitColor.get() && !trunkChangedColor){
            trunkChangedColor = true;
            for( GameObject game: gameObjectList){
                if (game.getTag().equals(TRUNK_TAG)){
                    game.renderer().setRenderable(new RectangleRenderable(
                            ColorSupplier.approximateColor(TRUNK_COLOR)));
                }
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
            for( GameObject game: gameObjectList){
                if (game.getTag().equals(LEAF_TAG)){
                    float randomTransitionTime = random.nextFloat();
                    float factor = randomTransitionTime * (Leafs.MAXIMUM_TRANSITION_TIME -
                            Leafs.MINIMUM_TRANSITION_TIME) + Leafs.MAXIMUM_TRANSITION_TIME;
                    Runnable func = () ->
                            new Transition<>(game,game.renderer()::setRenderableAngle,0f,
                                    MAXIMUM_ANGEL,Transition.LINEAR_INTERPOLATOR_FLOAT,TRANSITION_TIME,
                                    Transition.TransitionType.TRANSITION_ONCE,null);
                    new ScheduledTask(game, factor, false, func);
                }
            }
        }

    }

    /**
     * main method that run the game.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
