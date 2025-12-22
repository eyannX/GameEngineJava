package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class TallInteractiveObject extends Entity {

    protected GamePanel gp;

    protected int spriteWidth;
    protected int spriteHeight;

    protected int canopyHeight;     // pixels
    protected boolean hasCanopy;

    public TallInteractiveObject(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;

        // Default: no collision
        solidArea = new Rectangle();
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        // Anchor object at BASE tile
        worldX = col * gp.tileSize;
        worldY = row * gp.tileSize;
    }

    /**
     * Call this AFTER loading the sprite.
     * Anchors the sprite so its base aligns with the grid.
     */
    protected void alignSpriteToBase() {
        worldY -= (spriteHeight - gp.tileSize);
    }

    // =========================
    // DRAW TRUNK (Y-SORTED)
    // =========================
    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image = down1;

        int screenX = (int)(worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY - gp.player.worldY + gp.player.screenY);

        if (!isOnScreen()) return;

        if (hasCanopy) {
            int trunkY = screenY + canopyHeight;
            int trunkHeight = spriteHeight - canopyHeight;

            g2.drawImage(
                    image,
                    screenX, trunkY,
                    screenX + spriteWidth, trunkY + trunkHeight,
                    0, canopyHeight,
                    spriteWidth, spriteHeight,
                    null
            );
        } else {
            g2.drawImage(image, screenX, screenY, null);
        }
    }

    // =========================
    // DRAW CANOPY (OVERLAY)
    // =========================
    public void drawCanopy(Graphics2D g2) {

        if (!hasCanopy) return;

        BufferedImage image = down1;

        int screenX = (int)(worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY - gp.player.worldY + gp.player.screenY);

        if (!isOnScreen()) return;

        g2.drawImage(
                image,
                screenX, screenY,
                screenX + spriteWidth, screenY + canopyHeight,
                0, 0,
                spriteWidth, canopyHeight,
                null
        );
    }

    // =========================
    // SCREEN CHECK
    // =========================
    protected boolean isOnScreen() {
        return worldX + spriteWidth > gp.player.worldX - gp.player.screenX &&
                worldX - spriteWidth < gp.player.worldX + gp.player.screenX &&
                worldY + spriteHeight > gp.player.worldY - gp.player.screenY &&
                worldY - spriteHeight < gp.player.worldY + gp.player.screenY;
    }

}
