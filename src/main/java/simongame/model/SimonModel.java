/* *****************************************
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 2:19 AM
 *
 * Project: SIMON-SAYS
 * Package: simongame.model
 * Class: SimonModel
 *
 * Description: A model class for the Simon game
 *
 * ****************************************
 */

package simongame.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

import simongame.Light;

/**
 * A model class for the traffic light
 */
public class SimonModel {

    /**
     * A getter for the lights array
     */
    public ArrayList<Light> getLights() {
        return lights;
    }

    /**
     * Our array of {@link Light} objects for our Simon game
     */
    private ArrayList<Light> lights;

    /**
     * A getter for the auto off property
     */
    public boolean isIsAutoOff() {
        return isAutoOff.get();
    }

    /**
     * A boolean property to determine if the light is on or off
     */
    public SimpleBooleanProperty isAutoOffProperty() {
        return isAutoOff;
    }

    /**
     * A boolean property to determine if the light is on or off
     */
    private SimpleBooleanProperty isAutoOff;

    /**
     * A getter method for the light at index i
     */
    public Light getLight(int i) {
        return this.lights.get(i);
    }

    /**
     * Our observable list of integers that represents the game sequence
     *
     * @return the game sequence
     */
    public ObservableList<Integer> getGameSequence() {
        return gameSequence;
    }

    /**
     * Our observable list of integers that represents the user sequence
     *
     * @return the user sequence
     */
    private ObservableList<Integer> gameSequence = FXCollections.observableArrayList();

    /**
     * Our observable list of integers that represents the user sequence
     *
     * @return the user sequence
     */
    private ObservableList<Integer> userSequence = FXCollections.observableArrayList();

    /**
     * A boolean property to determine if the game is in progress
     */
    private boolean gameInProgress = false;

    /**
     * A random number generator
     */
    private Random random = new Random();

    /**
     * A getter method for the score
     */
    public int getScore() {
        return score;
    }

    /**
     * An integer property to represent the score
     */
    private SimpleIntegerProperty scoreProperty;

    /**
     * A method to calculate the score
     */
    private int score;

    /**
     * A constructor for the SimonModel class
     */
    public SimonModel() {
        // Initialize the lights array
        this.lights = new ArrayList<>();

        // Let's initialize their colors and add them to the array
        for (LightColorEnum light : LightColorEnum.values())
            this.lights.add(new Light(light.getColor()));

        // Initialize the auto off property
        this.isAutoOff = new SimpleBooleanProperty(false);

        // Initialize score to 0
        scoreProperty = new SimpleIntegerProperty(0);
    }

    /**
     * A method to get the score property
     */
    public SimpleIntegerProperty scoreProperty() {
        return scoreProperty;
    }

    /**
     * A method to start a new game
     */
    public void startNewGame() {
        gameSequence.clear();
        userSequence.clear();
        gameInProgress = true;
        score = 0;
        generateNextInSequence();
    }

    /**
     * A method to generate the next light in the sequence
     */
    public void generateNextInSequence() {
        gameSequence.add(random.nextInt(getLights().size()));
    }

    /**
     * A method to add user input
     */
    public void addUserInput(int lightIndex) {
        if (!gameInProgress) return;
        userSequence.add(lightIndex);
        if (!isSequenceCorrect()) {
            gameInProgress = false;
            // Handle game over
        } else if (userSequence.size() == gameSequence.size()) {
            userSequence.clear();
            score += 1; // Increment the score
            scoreProperty.set(score); // Update the score property
            generateNextInSequence();
        }
    }

    /**
     * A method to check if the user sequence is correct
     */
    private boolean isSequenceCorrect() {
        for (int i = 0; i < userSequence.size(); i++) {
            if (!userSequence.get(i).equals(gameSequence.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * A method to extend the sequence
     */
    public void extendSequence() {
        int nextLightIndex = new Random().nextInt(4);
        gameSequence.add(nextLightIndex);
    }
}