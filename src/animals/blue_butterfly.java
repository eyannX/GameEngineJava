package animals;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class blue_butterfly extends Entity {

    GamePanel gp;
    Random random = new Random();






    public blue_butterfly(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Butterfly";

        defaultSpeed = 1;
        speed = defaultSpeed;

        maxLife = 2;
        life = maxLife;

        attack = 0;
        defense = 0;
        exp = 1;
        hostile = false;

        direction = "right";

        solidArea.x = 6 ;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();

//                            :)
    }

    public void getImage() {

        left1  = setup("/animals/butterfly/butterfly_left1",  48, 48);
        left2  = setup("/animals/butterfly/butterfly_left2",  48, 48);
        left3  = setup("/animals/butterfly/butterfly_left3",  48, 48);
        left4  = setup("/animals/butterfly/butterfly_left4",  48, 48);
        left5  = setup("/animals/butterfly/butterfly_left5",  48, 48);


        right1 = setup("/animals/butterfly/butterfly_right1", 48, 48);
        right2 = setup("/animals/butterfly/butterfly_right2", 48, 48);
        right3 = setup("/animals/butterfly/butterfly_right3", 48, 48);
        right4 = setup("/animals/butterfly/butterfly_right4", 48, 48);
        right5 = setup("/animals/butterfly/butterfly_right5", 48, 48);
    }
    @Override
    protected int getMaxFrame() {
       return 5;
    }
    @Override
    protected int getAnimationSpeed() {

        return 8;
    }


    @Override
    public void setAction() {


       //DYING SAFETY
        if(dying) {
            speed = 0;
            return;
        }


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



        //RANDOM WANDER
        actionLockCounter++;

        if(actionLockCounter >= 120) {


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




    public void damageReaction() {

        actionLockCounter = 0;

        if(gp.player.worldX < worldX) {
            direction = "right";
            facing = "right";
        } else {
            direction = "left";
            facing = "left";
        }

    }



    public void checkDrop() {



    }

    @Override
    public BufferedImage getCurrentImage() {


        if(facing.equals("left")) {
            return switch(spriteNum) {
                case 1 -> left1;
                case 2 -> left2;
                case 3 -> left3;
                case 4 -> left4;
                default -> left5;
            };
        } else {
            return switch(spriteNum) {
                case 1 -> right1;
                case 2 -> right2;
                case 3 -> right3;
                case 4 -> right4;
                default -> right5;
            };
        }
    }


}
