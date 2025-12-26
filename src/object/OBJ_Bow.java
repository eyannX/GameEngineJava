package object;

import main.GamePanel;

import java.awt.image.BufferedImage;
import entity.Entity;


public class OBJ_Bow extends Entity{

    public static final String objName = "Bow";


    public OBJ_Bow(GamePanel gp) {

        super(gp);

        type = type_bow;
        name = objName;
        down1 = setup("/objects/bow",gp.tileSize, gp.tileSize);
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height= 40;
        description = "[" + name + "]\nJafar's old bow.";
        price = 30;
        knockBackPower = 3;

    }
    public BufferedImage getCurrentImage() {

        return down1;
    }
}

