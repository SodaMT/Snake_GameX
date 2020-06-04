package org.mahoProject.gui;

import org.mahoProject.SnakeLogic;


public class Snake  {

    private SnakeLogic snakeLogic;
    private Stage stage;

    public Snake(SnakeLogic snakeLogic, Stage stage)
    {
        this.snakeLogic = snakeLogic;
        this.stage = stage;
    }
    public SnakeLogic getSnakeLogic()
    {
        return snakeLogic;
    }
}
