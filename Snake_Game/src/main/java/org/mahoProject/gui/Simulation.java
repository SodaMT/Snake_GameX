package org.mahoProject.gui;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.mahoProject.*;
import java.util.ArrayList;
import java.util.List;

public class Simulation extends Pane  {
    private Stage stage;
    private Snake snake;
    private Button bt;
    private List<Rectangle> previousRectangles ;
    private List<Rectangle> previousApple;
    private Rectangle[][] guiArray;
    private Simulator simulator;
    private Button lostChecker;


    public Simulation(int height, int width,Simulator simulator)
    {
        guiArray = new Rectangle[height][width];
        this.stage = new Stage(new StageLogic(height,width,this),guiArray);
        bt = new Button();
        bt.setStyle("-fx-background-color: Transparent");
        lostChecker = new Button();
        lostChecker.setStyle("-fx-background-color: Transparent");
        previousRectangles = new ArrayList<>();
        previousApple = new ArrayList<>();
        this.simulator = simulator;

        this.getChildren().addAll(bt,lostChecker,stage);

        //fill stage with empty space and place an apple in a random position
        stage.fillStage();
        stage.getStageLogic().placeApple();
        this.snake = new Snake(new SnakeLogic(stage.getStageLogic() , this), stage);
        //these buttons function as  hidden control event registers to ensure that an event is fired when conditions are met,
        bt.setOnKeyPressed(this::handleDirection);
        lostChecker.setOnAction(simulator::newGame);
        lostChecker();
    }


    public void removeApple(int c,int r)
    {
        updateStage(c,r, StageLogic.StageStates.ONE);
        //place a new apple on stage
        stage.getStageLogic().placeApple();
    }

    public void updateStage(int c, int r, StageLogic.StageStates state)
    {
        if (state == StageLogic.StageStates.ZERO)
        {
                Rectangle rect = previousRectangles.get(0);
                rect.setFill(Color.WHITE);
                previousRectangles.remove(rect);
        }
        else if (state == StageLogic.StageStates.ONE)
        {
                Rectangle rect = new Rectangle(20, 20, Color.GREEN);
                rect.setFill(Color.GREEN);
                this.stage.add(rect, c, r);
                guiArray[r][c] = rect;
                previousRectangles.add(rect);
        }
        else if (state == StageLogic.StageStates.TWO)
        {
            Rectangle rect = new Rectangle(20,20,Color.RED);
            rect.setArcHeight(10);
            rect.setArcWidth(10);
            this.stage.add(rect,c,r);
            guiArray[r][c] = rect;
            this.previousApple.add(rect);
        }
    }


    private synchronized void handleDirection(KeyEvent keyEvent) {

        SnakeLogic snakeLogic = snake.getSnakeLogic();

        if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
        {
            if(snakeLogic.getDirection() != Direction.RIGHT && snakeLogic.getDirection() != Direction.LEFT)
                snakeLogic.setDirection(Direction.LEFT);
        }
        else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
        {
            if(snakeLogic.getDirection() != Direction.LEFT && snakeLogic.getDirection() != Direction.RIGHT)
                snakeLogic.setDirection(Direction.RIGHT);
        }
        else if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
        {
            if (snakeLogic.getDirection() != Direction.UP && snakeLogic.getDirection() != Direction.DOWN)
                snakeLogic.setDirection(Direction.DOWN);
        }
        else if (keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
        {
            if (snakeLogic.getDirection() != Direction.DOWN && snakeLogic.getDirection() != Direction.UP)
                snakeLogic.setDirection(Direction.UP);
        }
    }

    //creates a seperate thread that forever runs concurrently with other threads
    // checking on whether the player has lost or not
    public synchronized void lostChecker()
    {
        SnakeLogic snakeLogic = getSnakeLogic();
        class lostCheckerTask extends Task<Void> {
            public Void call() throws Exception {

                while (!snakeLogic.getHasLost()) {
                        //event firing has to be done from javafx thread
                    Thread.sleep(200);
                    if(snakeLogic.getHasLost()) {
                        Platform.runLater(() -> {
                            lostChecker.fireEvent(new ActionEvent());
                        });

                        this.cancel();
                        break;
                    }

                }
                return null;
            }
        }
            Service<Void> lostCheckerService = new Service() {
                public Task<Void> createTask() {
                    return new lostCheckerTask();
                }
            };
            lostCheckerService.start();
    }


    public SnakeLogic getSnakeLogic()
    {
        return snake.getSnakeLogic();
    }

    public Rectangle[][] getGuiArray()
    {
        return  guiArray;
    }
}


