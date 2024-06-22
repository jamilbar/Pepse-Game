package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;
/**
 * This class represents the numeric energy in the game
 */
public class NumericEnergy extends GameObject {
    /*The function that return the energy of the avatar*/
    private final Supplier<TextRenderable> textRenderableSupplier;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param textRenderableSupplier   The Supplier that returns the TextRenderable representing the object.
     */
    public NumericEnergy(Vector2 topLeftCorner, Vector2 dimensions,
                         Supplier<TextRenderable> textRenderableSupplier) {
        super(topLeftCorner, dimensions, textRenderableSupplier.get());
        this.textRenderableSupplier = textRenderableSupplier;

    }
    /**
     * this method update the energy of the avatar that is displayed on the screen
     * @param deltaTime The time that has passed since the last update, in seconds.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderer().setRenderable(textRenderableSupplier.get());
    }
}
