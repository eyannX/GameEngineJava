package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Croissant extends Entity {

    GamePanel gp;
    public static final String objName = "croissant";

    public OBJ_Croissant(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = objName;
        value = 5;
        down1 = setup("/objects/croissant", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 50;
        stackable = true;

        setDialogue();
    }
    public void setDialogue()
    {
        dialogues[0][0] = "You Ate the " + name + "!\n" + "Your life has been recovered by " + value + "\n your hunger has been reset";
    }
    public boolean use(Entity entity)
    {
        startDialogue(this,0);

        entity.mana += 2;
        if(entity.mana <= 8){
            entity.life += value;
        }
        gp.playSE(2);
        return true;
    }
}
