package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class IT_BigTree extends TallInteractiveObject {

    public IT_BigTree(GamePanel gp, int col, int row) {
        super(gp, col, row);

        down1 = setup(
                "/tiles_interactive/Tree",
                gp.tileSize * 2,
                gp.tileSize * 4
        );

        spriteWidth = gp.tileSize * 2;
        spriteHeight = gp.tileSize * 4;

        hasCanopy = true;
        canopyHeight = gp.tileSize * 2;

        // Align sprite base to grid
        alignSpriteToBase();

        // Trunk collision
        solidArea.x = 50;
        solidArea.y = gp.tileSize * 2;
        solidArea.width = 30;
        solidArea.height = gp.tileSize * 2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        life = 2;
    }


    public boolean isCorrectItem(Entity entity) {
        return entity.currentWeapon.type == type_axe;
    }


    public InteractiveTile getDestroyedForm() {
        return new IT_Trunk(gp,
                (int)(worldX / gp.tileSize),
                (int)((worldY + spriteHeight - gp.tileSize) / gp.tileSize)
        );
    }

    public void playSE() {
        gp.playSE(11);
    }

    @Override
    public Color getParticleColor() {
        return new Color(65,50,30);
    }

}
