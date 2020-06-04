package org.mahoProject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mahoProject.gui.Simulation;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage)
    {
        Simulator simulator = new Simulator(31,41);
        Scene scene = new Scene(simulator,801,601);

        stage.setScene(scene);
        stage.setTitle("Snake Game by Mahmoud");
        stage.setResizable(false);
        stage.show();
        simulator.startGame();



    }

    public static void main(String[] args) {

        launch();


    }

}