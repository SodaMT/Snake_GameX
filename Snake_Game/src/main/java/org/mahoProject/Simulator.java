package org.mahoProject;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.mahoProject.gui.Simulation;

import java.util.Optional;

public class Simulator extends Pane {
    private Simulation simulation;
    private int height;
    private int width;


    public Simulator(int height, int width)
    {
        this.height = height;
        this.width = width;
        simulation = new Simulation(height,width,this);

        this.getChildren().add(simulation);


    }

    public Simulation getSimulation()
    {
        return simulation;
    }

    public void startGame()
    {
        for(int i = 0 ; i< 500 ; i++)
        {
            i++;
        }
        simulation.getSnakeLogic().move();
    }

    public void newGame(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"You have lost. Do you want to try again?",ButtonType.YES,ButtonType.NO);
        alert.setTitle("GAME OVER");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            this.getChildren().remove(this.simulation);
            this.simulation = new Simulation(height,width,this);
            this.getChildren().add(simulation);

            startGame();
        }
        else
        {
            System.exit(1);
        }

    }



}
