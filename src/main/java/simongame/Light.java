/* *****************************************
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 1:58 AM
 *
 * Project: SIMON-SAYS
 * Package: simongame
 * Class: Light
 *
 * Description: A simple abstraction for a basic light
 *
 * ****************************************
 */

package simongame;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * A simple abstraction for a basic light that can turn on and off.
 * Our light will also have a color which the class will manage
 * to simulate darkening
 */
public class Light {

    /**
     * Get the onColor property
     *
     * @return the onColor property
     */
    public boolean isIsOn() {
        return isOn.get();
    }

    /**
     * Get the onColor property
     *
     * @return the onColor property
     */
    public SimpleBooleanProperty isOnProperty() {
        return isOn;
    }

    /**
     * Is the light on?
     */
    private SimpleBooleanProperty isOn;

    /**
     * Get the currentColor
     *
     * @return the currentColor
     */
    public Color getCurrentColor() {
        return currentColor.get();
    }

    /**
     * Get the simple object currentColor property
     *
     * @return the simple object currentColor property
     */
    public SimpleObjectProperty<Color> currentColorProperty() {
        return currentColor;
    }

    /**
     * The current color of the light
     */
    private SimpleObjectProperty<Color> currentColor;

    /**
     * The light's onColor
     */
    private Color onColor;

    /**
     * The light's offColor
     */
    private Color offColor;

    /**
     * Construct a new Light instance that is currently set
     * to the light's off color
     *
     * @param color represents the "on" color of the light
     */
    public Light(Color color) {
        this.isOn = new SimpleBooleanProperty(false);
        this.onColor = color;
        this.offColor = color.darker();
        this.currentColor = new SimpleObjectProperty<>(this.offColor);
    }

    /**
     * Toggle the state of the light to be either on or off
     */
    public void toggle() {
        this.isOn.set(!this.isOn.get());
        if (this.isOn.get()) {
            this.currentColor.set(this.onColor);
        } else {
            this.currentColor.set(this.offColor);
        }
    }

    /**
     * Turn our light on for a specified time. We do this in a separate
     * thread as to not cause delays in the main JavaFX thread
     *
     * @param timeInMs the time in milliseconds to keep the light on
     */
    public void turnOnForMs(long timeInMs) {
        Runnable r = () -> {
            try {
                // If we're not on, then turn on
                if (!this.isOn.get()) {
                    this.toggle();
                    Thread.sleep(timeInMs);
                }
            } catch (InterruptedException e) {
                // Do nothing
            } finally {
                // If we're on, then make sure we turn off
                if (this.isIsOn()) {
                    this.toggle();
                }
            }
        };

        // Encapsulate our Runnable in a thread and start it
        Thread t = new Thread(r);
        t.start();
    }
}