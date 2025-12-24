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


    @Override
    public void setAction() {

    /* =========================
       1. DYING SAFETY
       ========================= */
        if(dying) {
            speed = 0;
            return;
        }

    /* =========================
       2. COLLISION RESPONSE (TOP PRIORITY)
       ========================= */
        if (collisionOn) {

            switch(direction) {
                case "up":    direction = "down"; break;
                case "down":  direction = "up"; break;
                case "left":
                    direction = "right";
                    facing = "right";
                    break;
                case "right":
                    direction = "left";
                    facing = "left";
                    break;
            }

            collisionOn = false;
            // DO NOT return — allow hurt / movement to continue
        }

    /* =========================
       3. HIT REACTION
       ========================= */
        if(inHitReaction) {

            hitReactionCounter++;
            speed = defaultSpeed + 1;

            // move in current direction (DO NOT re-force facing)
            if(hitReactionCounter > 300) {
                inHitReaction = false;
                hitReactionCounter = 0;
                speed = defaultSpeed;
            }

            return;
        }

    /* =========================
       4. EATING STATE
       ========================= */
        if(eating) {

            speed = 0;
            direction = facing;
            maxSpriteNum = 8;


            spriteCounter++;
            if(spriteCounter > 12) {
                spriteNum++;
                if(spriteNum > maxSpriteNum){
                    spriteNum = 1;
                    gp.playSE(37);
                }
                spriteCounter = 0;
            }

            eatingCounter++;
            if(eatingCounter >= maxSpriteNum * 12) {
                eating = false;

                eatingCounter = 0;
                spriteNum = 1;
                maxSpriteNum = 4;
                speed = defaultSpeed;
            }

            return;
        }


      //RANDOM WANDER
        actionLockCounter++;

        if(actionLockCounter >= 120) {

            if(random.nextInt(100) < 20) {
                eating = true;
                spriteNum = 1;
                spriteCounter = 0;
                eatingCounter = 0;
                maxSpriteNum = 8;
                actionLockCounter = 0;
                return;
            }

            int i = random.nextInt(100);

            if(i < 25) {
                direction = "up";
            }
            else if(i < 50) {
                direction = "down";
            }
            else if(i < 75) {
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
    @Override
    protected int getAnimationSpeed() {

        if (eating) {
            animationSpeed = 12;
            return animationSpeed;
        }
        else {
            return 8;
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

        gp.playSE(36);

    }


    public void checkDrop() {

        int i = random.nextInt(100) + 1;

        if(i <= 60) {

            dropItem(new OBJ_Chicken(gp));
        }
        else {

            dropItem(new OBJ_Chicken(gp));
        }

    }

    @Override
    public BufferedImage getCurrentImage() {

        if(eating) {
            if(facing.equals("left")) {
                return switch(spriteNum) {
                    case 1 -> eat_left_1;
                    case 2 -> eat_left_2;
                    case 3 -> eat_left_3;
                    case 4 -> eat_left_4;
                    case 5 -> eat_left_5;
                    case 6 -> eat_left_6;
                    case 7 -> eat_left_7;
                    default -> eat_left_8;
                };
            } else {
                return switch(spriteNum) {
                    case 1 -> eat_right_1;
                    case 2 -> eat_right_2;
                    case 3 -> eat_right_3;
                    case 4 -> eat_right_4;
                    case 5 -> eat_right_5;
                    case 6 -> eat_right_6;
                    case 7 -> eat_right_7;
                    default -> eat_right_8;
                };
            }
        }

        if(facing.equals("left")) {
            return switch(spriteNum) {
                case 1 -> left1;
                case 2 -> left2;
                case 3 -> left3;
                default -> left4;
            };
        } else {
            return switch(spriteNum) {
                case 1 -> right1;
                case 2 -> right2;
                case 3 -> right3;
                default -> right4;
            };
        }
    }


}
