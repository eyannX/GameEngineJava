package object;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OBJ_Quiver extends Entity {

    public static final String objName = "Quiver";

    public OBJ_Quiver(GamePanel gp) {
        super(gp);

        type = type_container;
        name = objName;
        down1 = setup("/objects/quiver", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nStores arrows.";
        price = 75;

        inventory = new ArrayList<>();
        maxInventorySize = 5; // 1 column × 5 rows
    }

    @Override
    public BufferedImage getCurrentImage(){

        return down1;
    }
}
