package org.mahoProject.gui;


import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.mahoProject.StageLogic;


public class Stage extends GridPane {
    private StageLogic stageLogic;
    private Rectangle[][] guiList;

    public Stage(StageLogic logic,Rectangle[][] guiList) {
        stageLogic = logic;
        this.setGridLinesVisible(true);
        this.guiList = guiList;
    }

    public StageLogic getStageLogic() {
        return stageLogic;
    }


    //fills stage with empty space
    public void fillStage()
    {
        for(int r = 0 ; r <  stageLogic.getHeight(); r++ )
        {
            for (int c = 0; c < stageLogic.getWidth(); c++)
            {
                Rectangle rect =new Rectangle(20,20,Color.WHITE);

                this.add(rect,c,r);
                this.guiList[r][c] = rect;
            }
        }
    }
    
}
