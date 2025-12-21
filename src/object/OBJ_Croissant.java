package object;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class OBJ_Croissant extends Entity {

    GamePanel gp;
    public static final String objName = "croissant";

    public OBJ_Croissant(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_edible;
        name = objName;
        value = 5;
        down1 = setup("/objects/croissant", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nrecovers your hunger by " + value + ".";
        price = 50;
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

}
