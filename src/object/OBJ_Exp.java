package object;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class OBJ_Exp extends Entity {

    GamePanel gp;
    public static final String objName = "exp";
    public OBJ_Exp(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        value = 30;
        down1 = setup("/objects/xp", 32, 32);
        price = 25;
    }
    public boolean use(Entity entity)
    {
        gp.playSE(1);
        gp.ui.addMessage("exp +" + value);
        entity.exp += value;
        return true;
    }
    public BufferedImage getCurrentImage() {

        return down1;
    }
}
