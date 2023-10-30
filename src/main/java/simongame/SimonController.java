/* *****************************************
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 3:22 PM
 *
 * Project: SIMON-SAYS
 * Package: simongame
 * Class: SimonController
 *
 * Description: A controller class that controls the Simon game
 *
 * ****************************************
 */

package simongame;

import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.Bindings;
import javafx.animation.PauseTransition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;

import simongame.model.SimonModel;
import simongame.view.SimonView;

/**
 * A controller class that controls the Simon game
 */
public class SimonController {

    /**
     * The {@link SimonModel} that represents the model of the Simon game
     */
    private SimonModel theModel;

    /**
     * The {@link SimonView} that represents the view of the Simon game
     * light
     */
    private SimonView theView;

    /**
     * The {@link AtomicInteger} that represents the index of the
     * current light in the sequence
     */
    private AtomicInteger sequenceIndex;

    /**
     * The {@link Timeline} that represents the timeline of the
     * sequence display
     */
    private Timeline timeline;

    /**
     * The {@link Integer} that represents the index of the current
     * light in the sequence
     */
    private int currentIndexInSequence = 0; // To track user's progress in the sequence

    /**
     * Constructs a {@link SimonController} object
     *
     * @param theModel the {@link SimonModel} that represents the model
     *                 of the Simon game
     * @param theView  the {@link SimonView} that represents the view of
     *                 the Simon game
     */
    public SimonController(SimonModel theModel, SimonView theView) {
        // Initialize the model and view
        this.theModel = theModel;
        this.theView = theView;

        // Initialize the bindings and event handlers
        initBindings();
        initEventHandlers();

        // Initialize the game controls
        setupGameControls();

        // Initialize the score display
        updateScores(theModel, theView);
    }

    /**
     * Updates the score display
     *
     * @param theModel the {@link SimonModel} that represents the model
     *                 of the Simon game
     * @param theView  the {@link SimonView} that represents the view of
     *                 the Simon game
     */
    private static void updateScores(SimonModel theModel, SimonView theView) {
        // Call this method when the score in the model changes
        theModel.scoreProperty().addListener((observable, oldValue, newValue) -> {
            theView.updateScoreDisplay(newValue.intValue());
        });
    }

    /**
     * Initializes the bindings
     */
    private void initBindings() {
        // Let's create a binding that is based on whatever dimension of our window is the largest
        NumberBinding radiusBinding =
                Bindings.max(theView.getRoot().heightProperty(),
                                theView.getRoot().widthProperty())
                        .divide(6)
                        .add(-15);

        for (Rectangle r : theView.getLights()) {
            r.widthProperty().bind(radiusBinding);
        }

        // Bind the color of the light in the view to the model light color
        for (int i = 0; i < theModel.getLights().size(); i++) {
            simongame.Light lightModel = theModel.getLight(i);
            Rectangle lightView = theView.getLight(i);

            lightView.fillProperty().bind(lightModel.currentColorProperty());

            // Set up an event if the user clicks on a light
            lightView.onMouseClickedProperty().setValue(event -> {
                lightModel.toggle();
            });
        }
    }

    /**
     * Initializes the event handlers
     */
    private void initEventHandlers() {
        // Event handlers for user clicks on lights
        for (int i = 0; i < theModel.getLights().size(); i++) {
            int finalI = i;
            Rectangle lightView = theView.getLight(i);
            lightView.setOnMouseClicked(event -> {
                // Only process user input if the clicked light is the next in the sequence
                if (currentIndexInSequence < theModel.getGameSequence().size() &&
                        theModel.getGameSequence().get(currentIndexInSequence) == finalI) {
                    applyClickEffects(lightView);
                    processUserInput(finalI);
                    // Highlight the light temporarily to indicate correct click
                    theView.highlightLight(finalI);
                    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                    pause.setOnFinished(e -> theView.unhighlightLight(finalI));
                    pause.play();
                } else {
                    // Handle wrong click or out-of-sequence click
                    handleGameOver();
                }
            });
        }
    }

    /**
     * Applies click effects to the light
     *
     * @param light the {@link Rectangle} that represents the light
     */
    private void applyClickEffects(Rectangle light) {
        light.setCursor(Cursor.HAND);
        light.setEffect(new DropShadow(10, Color.BLACK));
    }

    /**
     * Removes click effects from the light
     *
     * @param light the {@link Rectangle} that represents the light
     */
    private void removeClickEffects(Rectangle light) {
        light.setCursor(Cursor.DEFAULT);
        light.setEffect(null);
    }

    /**
     * Processes user input
     *
     * @param lightIndex the {@link Integer} that represents the index
     *                   of the light
     */
    private void processUserInput(int lightIndex) {
        // Check if the user clicked the correct light
        if (theModel.getGameSequence().get(currentIndexInSequence) == lightIndex) {
            // Correct choice
            currentIndexInSequence++;
            if (currentIndexInSequence == theModel.getGameSequence().size()) {
                // Sequence completed successfully, start next round
                currentIndexInSequence = 0;
                theModel.extendSequence(); // Method to add a new random light to the sequence
                displaySequence();
            }
        } else {
            // Wrong choice
            handleGameOver();
        }
    }

    /**
     * Handles game over
     */
    private void handleGameOver() {
        // Reset the game state
        theModel.startNewGame();
        currentIndexInSequence = 0;

        // Display a game-over message to the user
        theView.showGameOverMessage();
    }

    /**
     * Starts the game
     */
    public void startGame() {
        theModel.startNewGame();
        displaySequence();
    }

    /**
     * Sets up the game controls
     */
    private void setupGameControls() {
        this.theView.getBtnStartGame().setOnAction(event -> {
            // Start a new game
            this.theModel.startNewGame();
            displaySequence();
            this.theView.showStartGameUI();
        });

        theView.getBtnQuitGame().setOnAction(event -> {
            // Logic to handle game quit
            handleGameQuit();
            theView.showEndGameUI(); // Update UI for ending the game
        });
    }

    /**
     * Displays the sequence
     */
    private void displaySequence() {
        sequenceIndex = new AtomicInteger(0);

        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            int lightIndex = theModel.getGameSequence().get(sequenceIndex.getAndIncrement());
            highlightLight(lightIndex);
            if (sequenceIndex.get() == theModel.getGameSequence().size()) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(theModel.getGameSequence().size());
        timeline.play();
    }

    /**
     * Highlights the light
     *
     * @param lightIndex the {@link Integer} that represents the index
     *                   of the light
     */
    private void highlightLight(int lightIndex) {
        theView.highlightLight(lightIndex);

        PauseTransition visiblePause = new PauseTransition(Duration.seconds(1));
        visiblePause.setOnFinished(event -> theView.unhighlightLight(lightIndex));
        visiblePause.play();
    }

    /**
     * Handles game quit
     */
    private void handleGameQuit() {
        stopGameProcesses();
    }

    /**
     * Stops the game processes
     */
    private void stopGameProcesses() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}