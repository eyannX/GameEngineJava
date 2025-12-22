package object;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class OBJ_Dinar extends Entity {

    GamePanel gp;
    public static final String objName = "Bronze Coin";
    public OBJ_Dinar(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        value = 30;
        down1 = setup("/objects/coin", 32, 32);
        price = 25;
    }
    public BufferedImage getCurrentImage() {

        return down1;
    }
    public boolean use(Entity entity)
    {
        gp.playSE(1);
        gp.ui.addMessage("Coin +" + value);
        entity.coin += value;
        return true;
    }
}
