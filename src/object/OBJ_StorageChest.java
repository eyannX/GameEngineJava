package object;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class OBJ_StorageChest extends Entity {

    GamePanel gp;
    public static final String objName = "Storage Chest";

    public OBJ_StorageChest(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = objName;

        image = setup("/objects/chest", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);
        down1 = image;

        collision = true;

        solidArea.x = 15;
        solidArea.y = 40;
        solidArea.width = 60;
        solidArea.height = 60;

        // STORAGE SIZE: 5x4 = 20
        maxInventorySize = 20;
    }

    @Override
    public void interact() {
        if (!opened) {
            opened = true;
            down1 = image2;
            gp.playSE(3);

            gp.ui.openedChest = this;
            gp.gameState = gp.chestState;
        } else {
            closeChest();
        }
    }

    public void closeChest() {
        opened = false;
        down1 = image;
        gp.ui.openedChest = null;
        gp.gameState = gp.playState;
    }

    @Override
    public BufferedImage getCurrentImage() {

        if (opened) {
            return image2; // opened chest sprite
        }

        return image; // closed chest sprite
    }

}
