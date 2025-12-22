package object;
import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;
import java.util.Random;

public class OBJ_Chicken extends Entity {

    GamePanel gp;
    public static final String objName = "Chicken";

    public OBJ_Chicken(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_edible;
        name = objName;
        value = 6;
        down1 = setup("/objects/chicken", 32, 32);
        description = "[" + name + "]\nrecovers your hunger by " + value + ".";
        price = 100;
        stackable = true;

        setDialogue();
    }
    public void setDialogue(){
        // meow meow meow
    }
    public boolean use(Entity entity) {

        entity.currentHunger = Math.min(
                entity.currentHunger + 2,
                entity.maxHunger
        );

        entity.saturation = Math.min(
                entity.saturation + 4,
                entity.maxSaturation
        );


        int sound = new Random().nextInt(3);
        gp.playSE(30 + sound);

        return true;
    }
    public BufferedImage getCurrentImage() {

        return down1;
    }

}