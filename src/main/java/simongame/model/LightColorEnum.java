/* *****************************************
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 1:58 AM
 *
 * Project: SIMON-SAYS
 * Package: simongame.model
 * Class: LightColorEnum
 *
 * Description: A enum class for the light colors
 *
 * ****************************************
 */

package simongame.model;

import javafx.scene.paint.Color;

/**
 * A simple abstraction for a basic light that can turn on and off.
 * Our light will also have a color which the class will manage
 * to simulate darkening
 */
public enum LightColorEnum {
    RED(Color.RED),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE);

    /**
     * The color of the light
     */
    private Color color;

    /**
     * Constructor for the light color enum
     *
     * @param color the color of the light
     */
    private LightColorEnum(Color color) {
        this.color = color;
    }

    /**
     * Get the color of the light
     *
     * @return the color of the light
     */
    public Color getColor() {
        return color;
    }
}