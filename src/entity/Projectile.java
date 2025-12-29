package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends Entity{

    Entity user;

    // Arrow trail
    private static final int TRAIL_LENGTH = 6;
    private int[] trailX = new int[TRAIL_LENGTH];
    private int[] trailY = new int[TRAIL_LENGTH];
    private boolean trailInitialized = false;


    public Projectile(GamePanel gp) {
        super(gp);

    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user)
    {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;  //Reset the life to the max value every time you shoot it.
    }
    public void update() {

        // Reset collision flag
        collisionOn = false;

        // Tile collision
        gp.cChecker.checkTile(this);

        // Object collision (optional but recommended for walls, doors, etc.)
        gp.cChecker.checkObject(this, false);

        if(collisionOn) {
            alive = false;
            gp.renderListDirty = true;
            return;
        }
        // Initialize trail on first frame
        if(!trailInitialized) {
            for(int i = 0; i < TRAIL_LENGTH; i++) {
                trailX[i] = (int) worldX;
                trailY[i] = (int) worldY;
            }
            trailInitialized = true;
        }

        // Shift trail positions
        for(int i = TRAIL_LENGTH - 1; i > 0; i--) {
            trailX[i] = trailX[i - 1];
            trailY[i] = trailY[i - 1];
        }

        // Store current position
        trailX[0] = (int) worldX;
        trailY[0] = (int) worldY;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);

        if(collisionOn) {
            alive = false;
            gp.renderListDirty = true;

            generateParticle(this, this);
            return;
        }

        if(user == gp.player)
        {
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            if(monsterIndex != 999) //collision with monster
            {
                gp.player.damageMonster(monsterIndex, this, attack * (1 + (gp.player.level / 2)), knockBackPower);   //attack : projectile's attack (2) // fireball dmg increases in every 2 levels
                generateParticle(user.projectile,gp.monster[gp.currentMap][monsterIndex]);
                alive = false;
                gp.renderListDirty = true;

            }
        }
        if(user != gp.player)
        {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(!gp.player.playerInvincible && contactPlayer) {
                damagePlayer(attack);
                if(gp.player.guarding)
                {
                    generateParticle(user.projectile,user.projectile);
                }
                else
                {
                    generateParticle(user.projectile,gp.player);
                }

                alive = false;
                gp.renderListDirty = true;
            }
        }

        switch (direction)
        {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        life--;
        if(life <= 0) {
            alive = false;  //once you shoot projectile, it loses its life
        }

        spriteCounter++;
        if(spriteCounter > 12)
        {
            if(spriteNum == 1)
            {
                spriteNum = 2;
            }
            else if(spriteNum == 2)
            {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }
    public boolean haveResource(Entity user)
    {
        boolean haveResource = false;
        return haveResource;
    }
    public void subtractResource(Entity user) {

    }
    public void draw(Graphics2D g2) {

        BufferedImage image = getCurrentImage();
        if(image == null) return;

        // Draw trail
        for(int i = TRAIL_LENGTH - 1; i > 0; i--) {

            float alpha = 1f - ((float)i / TRAIL_LENGTH);
            alpha *= 0.25f; // overall intensity (tweak)


            g2.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
            );

            int screenX = (int) (trailX[i] - gp.player.worldX + gp.player.screenX);
            int screenY = (int) (trailY[i] - gp.player.worldY + gp.player.screenY);

            g2.setColor(Color.WHITE);

            // Thin streak
            int w = 50;
            int h = 20;

            if(direction.equals("up") || direction.equals("down")) {
                w = 20;
                h = 50;
            }

            int offsetX = 0;
            int offsetY = 0;

            if(direction.equals("left") || direction.equals("right")){

                offsetY = 20;
                offsetX = 6;
            }
            else{
                offsetY = 40;
                offsetX = 23;
            }

            g2.fillRect(screenX + offsetX, screenY + offsetY, w, h);

        }


        // trail effect
        g2.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
        );



        int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);

        g2.drawImage(image, screenX, screenY, null);

        // DEBUG
        if(gp.debug) {
            drawDebugSolidArea(g2);
        }
    }

}
