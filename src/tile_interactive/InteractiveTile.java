package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InteractiveTile extends Entity {

    GamePanel gp;
    public boolean destructible = false;
    public int canopyHeight = 0;
    public boolean hasCanopy = false;


    public InteractiveTile(GamePanel gp, int col, int row) {

        super(gp);
        this.gp = gp;


        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public boolean isCorrectItem(Entity entity)
    {
        boolean isCorrectItem = false;
        //Subclass specifications
        return isCorrectItem;
    }
    public void playSE()
    {

    }
    public InteractiveTile getDestroyedForm() {

        InteractiveTile tile = null;
        //Subclass specifications
        return tile;
    }
    public void update() {

        if(invincible) {

            invincibleCounter++;
            if(invincibleCounter > 20) {

                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void drawCanopy(Graphics2D g2) {

        if(!hasCanopy) return;

        BufferedImage image = down1;

        int screenX = (int)(worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY - gp.player.worldY + gp.player.screenY);

        // Camera culling (canopy only)
        if (worldX + image.getWidth() > gp.player.worldX - gp.player.screenX &&
                worldX - image.getWidth() < gp.player.worldX + gp.player.screenX &&
                worldY + canopyHeight > gp.player.worldY - gp.player.screenY &&
                worldY < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(
                    image,
                    screenX, screenY,
                    screenX + image.getWidth(), screenY + canopyHeight,
                    0, 0,
                    image.getWidth(), canopyHeight,
                    null
            );
        }
    }


    public void draw(Graphics2D g2) {

        BufferedImage image = down1;

        int screenX = (int)(worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY - gp.player.worldY + gp.player.screenY);

        // Camera culling
        if (worldX + image.getWidth() > gp.player.worldX - gp.player.screenX &&
                worldX - image.getWidth() < gp.player.worldX + gp.player.screenX &&
                worldY + image.getHeight() > gp.player.worldY - gp.player.screenY &&
                worldY - image.getHeight() < gp.player.worldY + gp.player.screenY) {

            // Draw TRUNK only (bottom part)
            if(hasCanopy) {

                int trunkY = screenY + canopyHeight;
                int trunkHeight = image.getHeight() - canopyHeight;

                g2.drawImage(
                        image,
                        screenX, trunkY,
                        screenX + image.getWidth(), trunkY + trunkHeight,
                        0, canopyHeight,
                        image.getWidth(), image.getHeight(),
                        null
                );
            }
            // Draw FULL image (no canopy logic)
            else {
                g2.drawImage(image, screenX, screenY, null);
            }
        }
    }

}
