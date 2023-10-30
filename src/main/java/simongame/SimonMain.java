/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Fall 2023
 * Instructor: Prof. Joshua Stough
 *
 * Section: MWF (10:00 AM - 10:50 AM)
 * Lab Section: Thursday (10:00 AM - 11:50 AM)
 *
 * Name: Nolan Lwin
 * Date: 10/26/23
 * Time: 2:34 PM
 *
 * Project: csci205_labs
 * Package: lab10.simongame
 * Class: SimonMain
 *
 * Lab Assignment: Lab10
 *
 * Description: A main class that runs the Simon game application
 *
 * ****************************************
 */

package simongame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

import simongame.model.SimonModel;
import simongame.view.SimonView;

/**
 * A main class that runs the Simon game application
 */
public class SimonMain extends Application {

    /**
     * The {@link SimonModel} that represents the model of the Simon game
     * light
     */
    private SimonModel theModel;

    /**
     * The {@link SimonView} that represents the view of the Simon game
     * light
     */
    private SimonView theView;

    /**
     * The {@link SimonController} that represents the controller of the
     * Simon game
     */
    private SimonController theController;

    /**
     * The {@link URL} that stores the external CSS file
     */
    private URL cssURL;

    /**
     * The main method that runs the Simon game application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the Simon game application
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(theView.getRoot());

        primaryStage.setTitle("Simon Says Game");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        externalCSS(scene);

        // Initialize the controller
        controller();
    }

    /**
     * Initializes the controller
     */
    private void controller() {
        // Initialize the controller
        this.theController = new SimonController(this.theModel, this.theView);
    }

    /**
     * Adds the external CSS file to the scene
     *
     * @param scene the scene to add the CSS file to
     */
    private void externalCSS(Scene scene) {
        cssURL = getClass().getResource("/simongame/simon.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
        // Handle the case where the resource is not found
        System.err.println("Could not find simon.css");
}

    }

    /**
     * Initializes the model and view
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        super.init();
        this.theModel = new SimonModel();
        this.theView = new SimonView(this.theModel);
    }
}