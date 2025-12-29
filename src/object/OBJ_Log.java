package object;
import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;


public class OBJ_Log extends Entity {

    public static final String objName = "Log";

    public OBJ_Log(GamePanel gp){
        super(gp);

        type = type_pickaxe;
        name = objName;
        down1 = setup("/objects/log",48,48);
        description = "[" + name + "]\nit's made from wood!";
        price = 5;

    }
    public BufferedImage getCurrentImage() {

        return down1;
    }
}
