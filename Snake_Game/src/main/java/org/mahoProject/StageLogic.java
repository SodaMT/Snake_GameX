package org.mahoProject;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.mahoProject.gui.Simulation;

import java.util.Random;



public class StageLogic
{
    private int height;
    private int width;
    private Simulation simulation;

    public enum StageStates
    {
        ZERO,ONE,TWO
    }

    public StageLogic(int height, int width,Simulation simulation)
    {
        this.height = height;
        this.width = width;
        this.simulation = simulation;
    }

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }

    //keep repeating until a suitable position to place an apple is found
    public Point2D placeApple() {
        int randomRow;
        int randomCol;
        while (true) {
            Random r = new Random();
            randomRow = r.nextInt(height-1);
            randomCol = r.nextInt(width-1);
            if (simulation.getGuiArray()[randomRow][randomCol].getFill() != Color.GREEN) {
                simulation.updateStage(randomCol,randomRow,StageStates.TWO);
                break;
            }
        }
        return new Point2D(randomCol, randomRow); // x,y
    }


}
