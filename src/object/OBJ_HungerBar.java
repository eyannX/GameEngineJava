package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_HungerBar extends Entity {

    GamePanel gp;
    public static final String objName = "Hunger Bar";

    public OBJ_HungerBar(GamePanel gp)
    {
        super(gp);

        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        value = 1;
        down1 = setup("/objects/hungerFull", gp.tileSize,gp.tileSize);
        image = setup("/objects/hungerFull", gp.tileSize,gp.tileSize);
        image2 = setup("/objects/hungerNull", gp.tileSize,gp.tileSize);
        price = 105;
    }
    public boolean use(Entity entity)
    {
        gp.playSE(2);
        gp.ui.addMessage("hunger +" + value);
        entity.currentHunger += value;
        return true;
    }
}
