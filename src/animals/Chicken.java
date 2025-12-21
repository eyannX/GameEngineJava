package animals;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Chicken extends Entity {

    GamePanel gp;
    Random random = new Random();


    int eatingCounter = 0;
    int hitReactionCounter = 0;
    boolean inHitReaction = false;



    BufferedImage eat_left_1, eat_left_2, eat_left_3, eat_left_4, eat_left_5, eat_left_6, eat_left_7, eat_left_8;
    BufferedImage eat_right_1, eat_right_2, eat_right_3, eat_right_4, eat_right_5, eat_right_6, eat_right_7, eat_right_8;

    public Chicken(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_monster;          // reuse monster systems (HP, drops, damage)
        name = "Chicken";

        defaultSpeed = 1;
        speed = defaultSpeed;

        maxLife = 2;
        life = maxLife;

        attack = 0;                   // IMPORTANT: chicken never attacks
        defense = 0;
        exp = 1;
        hostile = false;

        direction = "right";

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        getEatingImage();
//                            :)
    }

    public void getImage() {

        left1  = setup("/animals/chicken_run_left1",  gp.tileSize, gp.tileSize);
        left2  = setup("/animals/chicken_run_left2",  gp.tileSize, gp.tileSize);
        left3  = setup("/animals/chicken_run_left3",  gp.tileSize, gp.tileSize);
        left4  = setup("/animals/chicken_run_left4",  gp.tileSize, gp.tileSize);

        right1 = setup("/animals/chicken_run_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/animals/chicken_run_right2", gp.tileSize, gp.tileSize);
        right3 = setup("/animals/chicken_run_right3", gp.tileSize, gp.tileSize);
        right4 = setup("/animals/chicken_run_right4", gp.tileSize, gp.tileSize);
    }
    public void getEatingImage() {

        eat_left_1 = setup("/animals/chicken_eating_left1", gp.tileSize, gp.tileSize);
        eat_left_2 = setup("/animals/chicken_eating_left2", gp.tileSize, gp.tileSize);
        eat_left_3 = setup("/animals/chicken_eating_left3", gp.tileSize, gp.tileSize);
        eat_left_4 = setup("/animals/chicken_eating_left4", gp.tileSize, gp.tileSize);
        eat_left_5 = setup("/animals/chicken_eating_left5", gp.tileSize, gp.tileSize);
        eat_left_6 = setup("/animals/chicken_eating_left6", gp.tileSize, gp.tileSize);
        eat_left_7 = setup("/animals/chicken_eating_left7", gp.tileSize, gp.tileSize);
        eat_left_8 = setup("/animals/chicken_eating_left8", gp.tileSize, gp.tileSize);

        eat_right_1 = setup("/animals/chicken_eating_right1", gp.tileSize, gp.tileSize);
        eat_right_2 = setup("/animals/chicken_eating_right2", gp.tileSize, gp.tileSize);
        eat_right_3 = setup("/animals/chicken_eating_right3", gp.tileSize, gp.tileSize);
        eat_right_4 = setup("/animals/chicken_eating_right4", gp.tileSize, gp.tileSize);
        eat_right_5 = setup("/animals/chicken_eating_right5", gp.tileSize, gp.tileSize);
        eat_right_6 = setup("/animals/chicken_eating_right6", gp.tileSize, gp.tileSize);
        eat_right_7 = setup("/animals/chicken_eating_right7", gp.tileSize, gp.tileSize);
        eat_right_8 = setup("/animals/chicken_eating_right8", gp.tileSize, gp.tileSize);
    }


    public void setAction() {

        //  HIT REACTION
        if (inHitReaction) {

            hitReactionCounter++;
            speed = defaultSpeed + 1;

            if (hitReactionCounter > 300) {
                inHitReaction = false;
                hitReactionCounter = 0;
                speed = defaultSpeed;
            }
        }

        //  EATING
        if (eating) {

            eatingCounter++;
            speed = 0;

            maxSpriteNum = 8;

            if (eatingCounter >= maxSpriteNum * 12) {
                eating = false;
                eatingCounter = 0;
                speed = defaultSpeed;
                spriteNum = 1;
                maxSpriteNum = 4;
            }

            // Eating should stop movement, but NOT collision logic
        }

        // RANDOM MOVEMENT (only if not reacting or eating)
        if (!inHitReaction && !eating) {

            actionLockCounter++;

            if (actionLockCounter >= 120) {

                if (random.nextInt(100) < 20) {
                    eating = true;
                    spriteNum = 1;
                    eatingCounter = 0;
                    maxSpriteNum = 8;
                    actionLockCounter = 0;
                    return;
                }

                int i = random.nextInt(100);

                if (i < 25) direction = "up";
                else if (i < 50) direction = "down";
                else if (i < 75) {
                    direction = "left";
                    facing = "left";
                }
                else {
                    direction = "right";
                    facing = "right";
                }

                actionLockCounter = 0;
            }
        }

        // COLLISION RESPONSE
        if (collisionOn) {

            switch(direction) {
                case "up":    direction = "down"; break;
                case "down":  direction = "up"; break;
                case "left":  direction = "right"; facing = "right"; break;
                case "right": direction = "left";  facing = "left";  break;
            }

            collisionOn = false;
        }
    }



    public void damageReaction() {

        actionLockCounter = 0;

        if(gp.player.worldX < worldX) {
            direction = "right";
            facing = "right";
        } else {
            direction = "left";
            facing = "left";
        }

        inHitReaction = true;
        hitReactionCounter = 0;
        speed = defaultSpeed + 1;


    }


    public void checkDrop() {

        int i = random.nextInt(100) + 1;

        if(i <= 60) {
            dropItem(new OBJ_Exp(gp));
        }
        else {
            dropItem(new OBJ_HungerBar(gp));
        }

    }


    public void draw(Graphics2D g2) {

        BufferedImage image = null;



        if(eating) {

            if(facing.equals("left")) {
                if(spriteNum == 1) image = eat_left_1;
                if(spriteNum == 2) image = eat_left_2;
                if(spriteNum == 3) image = eat_left_3;
                if(spriteNum == 4) image = eat_left_4;
                if(spriteNum == 5) image = eat_left_5;
                if(spriteNum == 6) image = eat_left_6;
                if(spriteNum == 7) image = eat_left_7;
                if(spriteNum == 8) image = eat_left_8;
            } else {
                if(spriteNum == 1) image = eat_right_1;
                if(spriteNum == 2) image = eat_right_2;
                if(spriteNum == 3) image = eat_right_3;
                if(spriteNum == 4) image = eat_right_4;
                if(spriteNum == 5) image = eat_right_5;
                if(spriteNum == 6) image = eat_right_6;
                if(spriteNum == 7) image = eat_right_7;
                if(spriteNum == 8) image = eat_right_8;
            }

        } else {

            if(facing.equals("left")) {
                if(spriteNum == 1) image = left1;
                if(spriteNum == 2) image = left2;
                if(spriteNum == 3) image = left3;
                if(spriteNum == 4) image = left4;
            }
            else {
                if(spriteNum == 1) image = right1;
                if(spriteNum == 2) image = right2;
                if(spriteNum == 3) image = right3;
                if(spriteNum == 4) image = right4;
            }
        }



        int screenX = (int)(worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY - gp.player.worldY + gp.player.screenY);

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }


}
