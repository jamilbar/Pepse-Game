package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class represents the flora in the game
 */
public class Flora{
    /*The color of the trunk*/
    private static final int FRUIT_SIZE = 15;
    /*The probability of a trunk to be created*/
    private static final double TRUNK_PROBABILITY = 0.5;
    /*The probability of a leaf to be created*/
    private static final double LEAF_PROBABILITY = 0.75;
    /*The dimensions of the square*/
    private static final Vector2 SQUARE_DIMENSIONS = new Vector2(100, 100);
    /*The factor of creating a possible tree*/
    private static final int TREE_FACTOR = Block.SIZE * 4;
    /*The probability of a fruit to be created*/
    private static final double FRUIT_PROBABILITY = 0.75;
    /*The function that return the ground height at position x*/
    private final Function<Float,Float> groundHeight;

    /*The square factor*/
    private static final int TRUNK_SQUARE_FACTOR = 35;
    /*The random object*/
    private final Random random;
    /*The list of GameObjects that represent the flora in the game*/
    private final List<GameObject> gameObjectList;
    /*The Runnable that updates the energy of the avatar*/
    private final Runnable updateEnergy;
    /*The Supplier that returns true if the avatar is jumping*/
    private final Supplier<Boolean> changeFruitColor;

    /**
     * Constructor.
     * @param groundHeight The function that return the ground height at position x
     * @param updateEnergy The Runnable that updates the energy of the avatar
     * @param changeFruitColor The Supplier that returns true if the avatar is jumping
     */
    public Flora(Function<Float,Float> groundHeight, Runnable updateEnergy,
                 Supplier<Boolean> changeFruitColor) {
        this.groundHeight = groundHeight;
        random = new Random();
        gameObjectList = new ArrayList<>();
        this.updateEnergy = updateEnergy;
        this.changeFruitColor = changeFruitColor;
    }

    /**
     * this method creates a list of GameObjects that represent the flora in the game, it has trunks,
     * leafs and fruit, all of them are created in a random position in the game according to minX and maxX.
     * @param maxX the maximum x value to put a tree
     * @param minX the minimum x value to put a tree
     * @return The list of GameObjects .
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        for (float x = minX; x < maxX; x += TREE_FACTOR) {
            float groundHeightAtX = groundHeight.apply(x);
            double treeProbability = random.nextDouble();
            if (treeProbability < TRUNK_PROBABILITY) {
                GameObject trunk = Trunk.createTrunk(x, groundHeightAtX);
                gameObjectList.add(trunk);
                // create a square around the trunk;
                Vector2 topLeftCorner = new Vector2((int) trunk.getTopLeftCorner().x() -
                        TRUNK_SQUARE_FACTOR, (int) trunk.getTopLeftCorner().y() - TRUNK_SQUARE_FACTOR);
                GameObject trunkSquare = new GameObject(topLeftCorner, SQUARE_DIMENSIONS,
                        new RectangleRenderable(Color.yellow));
                addLeafAndFruit(trunkSquare, trunk);
            }
        }
        return gameObjectList;
    }

    /*this method adds leafs and fruits to the list of GameObjects.*/
    private void addLeafAndFruit(GameObject trunkSquare, GameObject trunk){
        double fruitProbability = random.nextDouble();
        for (int i = 0; i < trunkSquare.getDimensions().x(); i += Block.SIZE) {
            for (int j = 0; j < trunkSquare.getDimensions().y(); j += Block.SIZE) {
               double leafProbability = random.nextDouble();
                Vector2 leafPos = new Vector2(trunkSquare.getTopLeftCorner().x() + i,
                        (int)trunkSquare.getTopLeftCorner().y() + j);
                GameObject leaf = Leafs.createLeaf(leafPos);
                if (leafProbability < LEAF_PROBABILITY){
                    gameObjectList.add(leaf);
                }else if(isFruitPositionValid(leafPos, trunk) && fruitProbability < FRUIT_PROBABILITY){
                    //create a fruit
                    Fruit fruit  = new Fruit(leafPos, new Vector2(FRUIT_SIZE, FRUIT_SIZE),
                            new OvalRenderable(Color.RED), updateEnergy, changeFruitColor);
                    fruit.setCenter(leaf.getCenter());
                    gameObjectList.add(fruit);
                }
            }
        }
    }
    /*this method checks if the fruit position is valid*/
    private boolean isFruitPositionValid(Vector2 leafPos, GameObject trunk){
        return (leafPos.x() + FRUIT_SIZE < trunk.getTopLeftCorner().x()
                || (leafPos.x()+ FRUIT_SIZE > trunk.getTopLeftCorner().x()+trunk.getDimensions().x() )
                ||(leafPos.y() + FRUIT_SIZE < trunk.getTopLeftCorner().y()));
    }
}
