/* *****************************************
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 2:33 AM
 *
 * Project: SIMON-SAYS
 * Package: simongame.view
 * Class: SimonView
 *
 * Description: A class that represents the view of the Simon game
 *
 * ****************************************
 */

package simongame.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.Glow;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

import simongame.model.SimonModel;
import simongame.Light;
import simongame.model.LightColorEnum;

/**
 * A class that represents the view of the Simon game
 */
public class SimonView {

    /**
     * A getter method that returns the array of lights
     */
    public ArrayList<Rectangle> getLights() {
        return lights;
    }

    /**
     * The array of lights that we will use to display the light colors for the Simon game
     * The array is initialized in the constructor
     */
    private ArrayList<Rectangle> lights = new ArrayList<>();

    /**
     * The {@link SimonView} where the Simon game is displayed
     */
    private SimonModel theModel;

    /**
     * A getter method that returns the root of the scene graph
     *
     * @return the root of the scene graph
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * The {@link VBox} that contains the scene graph
     */
    private VBox root;

    /**
     * The {@link GridPane} that contains the lights
     */
    private GridPane grid;

    /**
     * A getter method that returns the Start Game button
     *
     * @return the Start Game button
     */
    public Button getBtnStartGame() {
        return btnStartGame;
    }

    /**
     * The Start Game button
     */
    private Button btnStartGame;

    /**
     * A getter method that returns the Quit Game button
     *
     * @return the Quit Game button
     */
    public Button getBtnQuitGame() {
        return btnQuitGame;
    }

    /**
     * The Quit Game button
     */
    private Button btnQuitGame;

    /**
     * A getter method that returns the status label
     *
     * @return the status label
     */
    public Label getLblStatus() {
        return lblStatus;
    }

    /**
     * The status label
     */
    private Label lblStatus;

    /**
     * A getter method that returns the light
     *
     * @return the light
     */
    private Rectangle light;

    /**
     * A getter method that returns the score label
     *
     * @return the score label
     */
    private Label lblScore;

    /**
     * A constructor that creates a {@link SimonView} object
     *
     * @param theModel
     */
    public SimonView(SimonModel theModel) {
        this.theModel = theModel;

        // Initialize the scene graph
        initSceneGraph();
        // Initialize the styling
        initStyling();

        // Initialize the lights
        initializeLights();

        // Initialize the game controls
        gameControls();
    }

    /**
     * A method that initializes the bindings and event handlers for the view components of the Simon game
     */
    private void gameControls() {
        // Initialize the Start Game button
        btnStartGame = new Button("Start Game");
        btnStartGame.setVisible(true);
        // Initialize the status label
        lblStatus = new Label("Press 'Start Game' to play!");
        lblStatus.setTextFill(Color.WHITE);
        lblStatus.setVisible(true);

        // Initialize the Quit Game button
        btnQuitGame = new Button("Quit Game");
        btnQuitGame.setVisible(false); // Initially, the Quit Game button is not visible

        // Create a VBox to hold the game controls
        VBox gameControls = new VBox(5, btnStartGame, btnQuitGame, lblStatus);
        gameControls.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(gameControls); // Add game controls to our layout
    }

    /**
     * A method that returns the root of the scene graph
     *
     * @return the root of the scene graph
     */
    private void initSceneGraph() {
        // Initialize the grid pane
        grid = new GridPane();
        // Initialize the root
        root = new VBox();

        // Initialize the Score Label
        lblScore = new Label("Score: " + theModel.getScore());
        GridPane.setHalignment(lblScore, javafx.geometry.HPos.CENTER);

//        // Add the score label to our layout before the grid
//        grid.add(lblScore, 0, 0, theModel.getLights().size(), 1);

        this.lights = new ArrayList<>();
        for (int i = 0; i < theModel.getLights().size(); i++) {
            // Get the light from the model
            Light modelLight = theModel.getLights().get(i);
            // Set the initial size of the light to be 550 x 200
            Rectangle light = new Rectangle(550, 200);

            // Set a style class so that we can set additional styles in CSS later
            light.getStyleClass().add("light");

            // Set the fill color based on the model
            light.setFill(modelLight.getCurrentColor());

            // Add the light to our array
            lights.add(light);

            // Add the light to the grid
            grid.add(light, i % 2, 1 + (i / 2));
        }

        // Add the light to the root
        this.root.getChildren().add(grid);
    }

    /**
     * A method that initializes the styling of the scene graph
     */
    public void initStyling() {
        root.setStyle("-fx-background-color: #152238;");

        grid.setPadding(new javafx.geometry.Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(javafx.geometry.Pos.CENTER);
    }

    /**
     * A method that initializes the lights
     */
    private void initializeLights() {
        // Assuming you want to create a view for each color in the enum
        for (LightColorEnum color : LightColorEnum.values()) {
            light = new Rectangle(300, 300, color.getColor());
            light.setFill(Color.DARKGRAY); // Initially, all lights are off.
            lights.add(light);
        }

        for (Rectangle light : lights) {
            light.setOnMouseClicked(event -> {
                Glow currentGlow = (light.getEffect() instanceof Glow) ? (Glow) light.getEffect() : null;
                if (currentGlow == null || currentGlow.getLevel() < 0.8) {
                    light.setEffect(new Glow(0.8));
                } else {
                    light.setEffect(null); // Turn off the glow
                }
            });
        }
    }

    /**
     * Returns the light at the given index.
     *
     * @param index The index of the light to return.
     * @return The light at the given index.
     */
    public Rectangle getLight(int index) {
        if (index >= 0 && index < lights.size()) {
            return lights.get(index);
        }
        return null;
    }

    /**
     * Highlights the light at the given index.
     *
     * @param index The index of the light to highlight.
     */
    public void highlightLight(int index) {
        Rectangle light = getLight(index);
        if (light != null) {
            light.setEffect(new Glow(0.8));
            // Additional highlighting logic
        }
    }

    /**
     * Un-highlights the light at the given index, reverting it back to its normal state.
     *
     * @param index The index of the light to unhighlight.
     */
    public void unhighlightLight(int index) {
        Rectangle light = getLight(index);
        if (light != null) {
            light.setEffect(null); // Remove the glow effect.
        }
    }

    /**
     * A method that displays a message when the user clicks on the wrong light
     */
    public void showGameOverMessage() {
        // Create an alert
        Alert gameOverAlert = new Alert(AlertType.INFORMATION);
        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setHeaderText(null);
        gameOverAlert.setContentText("Oops! Wrong color. Game Over!");

        // Display the alert and wait for the user to close it
        gameOverAlert.showAndWait();

        // Update the score to reflect the final score or reset it to 0
        updateScoreDisplay(theModel.getScore());
    }

    /**
     * A method that displays the btnQuitGame button and hides the btnStartGame button and lblStatus label
     */
    public void showStartGameUI() {
        btnStartGame.setVisible(false);
        lblStatus.setVisible(false);
        btnQuitGame.setVisible(true);
    }

    /**
     * A method that displays the btnStartGame button and lblStatus label and hides the btnQuitGame button
     */
    public void showEndGameUI() {
        btnQuitGame.setVisible(false);
        btnStartGame.setVisible(true);
        lblStatus.setVisible(true);
    }

    /**
     * A method that updates the score display
     *
     * @param score the score to be displayed
     */
    public void updateScoreDisplay(int score) {
        lblScore.setText("Score: " + score);
    }
}