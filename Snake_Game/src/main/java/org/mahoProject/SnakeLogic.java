package org.mahoProject;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.mahoProject.gui.Simulation;
import java.util.ArrayList;

public class SnakeLogic {
    private StageLogic stageLogic;
    private Direction direction = Direction.RIGHT; // default direction of snake
    private int snakeX;
    private int snakeY;
    private ArrayList<Point2D> previousPositions; // stores the snake's change in position
    private Simulation simulation;
    private boolean hasLost;
    private int moves;
    private int lengthOfSnake;
    private boolean ateApple;

    public SnakeLogic(StageLogic logic,Simulation simulation)
    {
        previousPositions = new ArrayList<>();
        this.simulation = simulation;
        moves = 0;
        hasLost = false;
        //starting length 3
        lengthOfSnake = 3;
        snakeX = 0;
        snakeY = 0;
        stageLogic = logic;
        ateApple = false;

        Platform.runLater( () -> {
            for( int i = 0; i < lengthOfSnake  ;i++) {
            int finalI = i;
            snakeX++;
            simulation.updateStage(snakeX, snakeY, StageLogic.StageStates.ONE);
            previousPositions.add(finalI, new Point2D(snakeX, snakeY));
            moves++;
        } });
    }

    public void moveStartingFrom(int y, int x)
    {
        class MoveStartingFromTask extends Task<Void>
        {
            @Override
            protected Void call() throws Exception {
                while (hasLost == false) {
                            if (direction == Direction.RIGHT) {
                                snakeX++;
                            } else if (direction == Direction.LEFT) {
                                snakeX--;
                            } else if (direction == Direction.UP) { // the topmost y position is 0
                                snakeY--;
                            } else if (direction == Direction.DOWN) {
                                snakeY++;
                            }
                            canMoveTo(snakeX, snakeY);
                            if (hasLost == false) {
                                //since this is code that affects the GUI I need to switch to the javaFx thread to run it
                                //otherwise no change will happen in the GUI
                                moves++;
                                if (ateApple == false) {
                                    Platform.runLater(() -> {
                                        simulation.updateStage(snakeX, snakeY, StageLogic.StageStates.ONE);
                                    });
                                    Thread.sleep(20);
                                }
                                //-1 cause lists indexes are inclusive of 0
                                previousPositions.add(moves - 1, new Point2D(snakeX, snakeY));
                                // remove traces after the first initial number of movements
                                removeTraces(moves - lengthOfSnake -1);

                                ateApple = false;
                            } else // hasLost == true
                            {
                                break;
                            }

                            Thread.sleep(52); // Affects the speed of snake movements
                    }
                return null; // task has Void return type
            }
        }
        /* creates a new background thread to run the above MoveStartingFromtask.
         this is done to make the code in the task and the code in javaFx Thread run concurrently  */
        Service<Void> moveStartingFromYXService = new Service() {
            @Override
            protected Task createTask() {
                return new MoveStartingFromTask();
            }
        };
        moveStartingFromYXService.start();
    }//end of method

    public void move()
    {
        hasLost = false;
        moveStartingFrom(snakeX, snakeY);
    }

    public void removeTraces(int i)
    {
        /* Its function is to remove one of the snake's old positions from the grid/array
         each time the snake makes a new movement */
        int emptyX = (int)previousPositions.get(i).getX();
        int emptyY = (int)previousPositions.get(i).getY();

        //moving to JavaFX thread since we're changing the GUI from background thread
        Platform.runLater( () -> {
            simulation.updateStage(emptyX,emptyY, StageLogic.StageStates.ZERO);
        });
    }


    public void canMoveTo(int x, int y)
    {
        if ( x < 0 || y < 0 || x == simulation.getGuiArray()[0].length-1 || y == simulation.getGuiArray().length-1)
        {
            hasLost = true;
        }
        if (simulation.getGuiArray()[y][x].getFill() == Color.GREEN  )
        {
            hasLost = true;
        }
        else if (simulation.getGuiArray()[y][x].getFill() == Color.RED) {
            eatApple(y,x);
        }
    }

    public void eatApple(int y,int x)
    {
        ateApple = true;
        lengthOfSnake++;
        Platform.runLater( () -> {
            simulation.removeApple(x, y);
            simulation.updateStage(x, y, StageLogic.StageStates.ONE);
        });
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public Direction getDirection()
    {
        return direction;
    }
    public boolean getHasLost()
    {
        return hasLost;
    }
}
