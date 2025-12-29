package animals;

import entity.Entity;
import main.GamePanel;
import main.Sound;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Parrot extends Entity {

    GamePanel gp;
    Random random = new Random();

    public Sound sound;
    public int soundRange;

    // AI timing
    int stateCounter = 0;
    int decisionInterval = 120; // 2 seconds at 60 FPS




    private boolean damageDealtThisAttack = false;

    public BufferedImage scream_right_1,scream_right_2, scream_right_3, scream_right_4, scream_right_5, scream_right_6, scream_right_7;
    public BufferedImage scream_left_1,scream_left_2, scream_left_3, scream_left_4, scream_left_5, scream_left_6, scream_left_7;

    public BufferedImage fly_right1, fly_right2, fly_right3, fly_right4, fly_right5, fly_right6, fly_right7, fly_right8;
    public BufferedImage fly_left1, fly_left2, fly_left3, fly_left4, fly_left5, fly_left6, fly_left7, fly_left8;

    public Parrot(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_monster;          // reuse monster systems (HP, drops, damage)
        name = "Parrot";

        defaultSpeed = 1;
        speed = defaultSpeed;

        maxLife = 4;
        life = maxLife;

        attack = 2;
        defense = 0;
        exp = 1;
        hostile = false;

        direction = "right";

        solidArea.x = 20;
        solidArea.y = 20;
        solidArea.width = 40;
        solidArea.height = 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = gp.tileSize;      // or some width that fits the parrot's attack reach
        attackArea.height = gp.tileSize;     // height, maybe smaller if attack is only horizontal
        attackArea.x = 0;                    // local offset if needed
        attackArea.y = 0;

        sound = new Sound();
        sound.setFile(38); // scream sound
        sound.looping = false;

        soundRange = gp.tileSize * 5;

        getImage();
        getScreamImage();
        getAttackImage();
        getFlyingImage();

//                            :)
    }

    public void getImage() {

        left1  = setup("/animals/parrot/walk_left8",  gp.tileSize, gp.tileSize);
        left2  = setup("/animals/parrot/walk_left7",  gp.tileSize, gp.tileSize);
        left3  = setup("/animals/parrot/walk_left6",  gp.tileSize, gp.tileSize);
        left4  = setup("/animals/parrot/walk_left5",  gp.tileSize, gp.tileSize);
        left5  = setup("/animals/parrot/walk_left4",  gp.tileSize, gp.tileSize);
        left6  = setup("/animals/parrot/walk_left3",  gp.tileSize, gp.tileSize);
        left7  = setup("/animals/parrot/walk_left2",  gp.tileSize, gp.tileSize);
        left8  = setup("/animals/parrot/walk_left1",  gp.tileSize, gp.tileSize);

        right1 = setup("/animals/parrot/walk_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/animals/parrot/walk_right2", gp.tileSize, gp.tileSize);
        right3 = setup("/animals/parrot/walk_right3", gp.tileSize, gp.tileSize);
        right4 = setup("/animals/parrot/walk_right4", gp.tileSize, gp.tileSize);
        right5 = setup("/animals/parrot/walk_right5", gp.tileSize, gp.tileSize);
        right6 = setup("/animals/parrot/walk_right6", gp.tileSize, gp.tileSize);
        right7 = setup("/animals/parrot/walk_right7", gp.tileSize, gp.tileSize);
        right8 = setup("/animals/parrot/walk_right8", gp.tileSize, gp.tileSize);
    }
    public void getScreamImage() {

        scream_left_1 = setup("/animals/parrot/scream_left1", gp.tileSize, gp.tileSize);
        scream_left_2 = setup("/animals/parrot/scream_left2", gp.tileSize, gp.tileSize);
        scream_left_3 = setup("/animals/parrot/scream_left3", gp.tileSize, gp.tileSize);
        scream_left_4 = setup("/animals/parrot/scream_left4", gp.tileSize, gp.tileSize);
        scream_left_5 = setup("/animals/parrot/scream_left5", gp.tileSize, gp.tileSize);
        scream_left_6 = setup("/animals/parrot/scream_left6", gp.tileSize, gp.tileSize);
        scream_left_7 = setup("/animals/parrot/scream_left7", gp.tileSize, gp.tileSize);

        scream_right_1 = setup("/animals/parrot/scream_right1", gp.tileSize, gp.tileSize);
        scream_right_2 = setup("/animals/parrot/scream_right2", gp.tileSize, gp.tileSize);
        scream_right_3 = setup("/animals/parrot/scream_right3", gp.tileSize, gp.tileSize);
        scream_right_4 = setup("/animals/parrot/scream_right4", gp.tileSize, gp.tileSize);
        scream_right_5 = setup("/animals/parrot/scream_right5", gp.tileSize, gp.tileSize);
        scream_right_6 = setup("/animals/parrot/scream_right6", gp.tileSize, gp.tileSize);
        scream_right_7 = setup("/animals/parrot/scream_right7", gp.tileSize, gp.tileSize);

    }
    public void getAttackImage(){

        attackLeft1 = setup("/animals/parrot/attack_left1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/animals/parrot/attack_left2", gp.tileSize*2, gp.tileSize);
        attackLeft3 = setup("/animals/parrot/attack_left3", gp.tileSize*2, gp.tileSize);
        attackLeft4 = setup("/animals/parrot/attack_left4", gp.tileSize*2, gp.tileSize);

        attackRight1 = setup("/animals/parrot/attack_right1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/animals/parrot/attack_right2", gp.tileSize*2, gp.tileSize);
        attackRight3 = setup("/animals/parrot/attack_right3", gp.tileSize*2, gp.tileSize);
        attackRight4 = setup("/animals/parrot/attack_right4", gp.tileSize*2, gp.tileSize);

    }
    public void getFlyingImage(){

        fly_left1 = setup("/animals/parrot/fly_left1", gp.tileSize, gp.tileSize);
        fly_left2 = setup("/animals/parrot/fly_left2", gp.tileSize, gp.tileSize);
        fly_left3 = setup("/animals/parrot/fly_left3", gp.tileSize, gp.tileSize);
        fly_left4 = setup("/animals/parrot/fly_left4", gp.tileSize, gp.tileSize);
        fly_left5 = setup("/animals/parrot/fly_left5", gp.tileSize, gp.tileSize);
        fly_left6 = setup("/animals/parrot/fly_left6", gp.tileSize, gp.tileSize);
        fly_left7 = setup("/animals/parrot/fly_left7", gp.tileSize, gp.tileSize);
        fly_left8 = setup("/animals/parrot/fly_left8", gp.tileSize, gp.tileSize);

        fly_right1 = setup("/animals/parrot/fly_right1", gp.tileSize, gp.tileSize);
        fly_right2 = setup("/animals/parrot/fly_right2", gp.tileSize, gp.tileSize);
        fly_right3 = setup("/animals/parrot/fly_right3", gp.tileSize, gp.tileSize);
        fly_right4 = setup("/animals/parrot/fly_right4", gp.tileSize, gp.tileSize);
        fly_right5 = setup("/animals/parrot/fly_right5", gp.tileSize, gp.tileSize);
        fly_right6 = setup("/animals/parrot/fly_right6", gp.tileSize, gp.tileSize);
        fly_right7 = setup("/animals/parrot/fly_right7", gp.tileSize, gp.tileSize);
        fly_right8 = setup("/animals/parrot/fly_right8", gp.tileSize, gp.tileSize);


    }

    @Override
    public void update() {
        super.update();  // calls Entity update


        checkAttackHit();
        updateSound();
    }

    @Override
    public void setAction() {


       //COLLISION RESOLUTION
        if (collisionOn && !attacking) {
            switch (direction) {
                case "up"    -> { direction = "down";  facing = "down";  }
                case "down"  -> { direction = "up";    facing = "up";    }
                case "left"  -> { direction = "right"; facing = "right"; }
                case "right" -> { direction = "left";  facing = "left";  }
            }
            collisionOn = false;
        }


       // HARD-LOCK STATES
        if (attacking) {
            speed = 0;
            onPath = false;
            flying = false;
            return;
        }

        if (screaming) {
            speed = 0;
            onPath = false;
            return;
        }


       // ATTACK CHECK
        checkAttackLeftRight(5, gp.tileSize + 10, 10);

        if (attacking) {
            spriteNum = 1;
            spriteCounter = 0;
            speed = 0;
            onPath = false;
            flying = false;
            return;
        }


       //HOSTILE BEHAVIOR
        if (hostile) {
            flying = true;
            speed = 3;
            onPath = true;
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
            return;
        }


       // PASSIVE AI DECISIONS
        onPath = false;
        stateCounter++;

        if (stateCounter >= decisionInterval) {
            stateCounter = 0;
            int roll = random.nextInt(100);

            if (!flying) {

                // 10% scream
                if (roll < 10) {
                    screaming = true;
                    spriteNum = 1;
                    spriteCounter = 0;
                    speed = 0;
                    sound.setVolumeByDistance((int) gp.player.worldX, (int) gp.player.worldY, (int) worldX, (int) worldY, soundRange);

                    sound.play();
                    return;
                }

                // 40% take off
                if (roll < 50) {
                    flying = true;
                    speed = 2;
                    return;
                }

            } else {

                // 10% chance to land
                if (roll < 10) {
                    flying = false;
                    speed = defaultSpeed;
                    return;
                }
            }
        }


       // MOVEMENT
        if (flying) {
            speed = 2;
            getRandomDirection(80);
        } else {
            speed = defaultSpeed;
            getRandomDirection(120);
        }
    }


    @Override
    public void damageReaction() {

        hostile = true;
        flying = true;
        speed = 3;
        facing = direction;
        onPath = true;


        sound.setFile(39); // screech
        sound.setVolumeByDistance((int) gp.player.worldX, (int) gp.player.worldY, (int) worldX, (int) worldY, soundRange);
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
    protected int getMaxFrame() {
        return attacking ? 4 :
                screaming ? 7 :
                        flying ? 8 : 8;
    }
    @Override
    protected int getAnimationSpeed() {

        // Set animation speed depending on current state
        if (attacking) {
            return 6;
        } else if (screaming) {
            return 5;
        } else if (flying) {
            return 10;
        } else {
            return 8;
        }
    }

    public void checkAttackHit() {

        if (!damageDealtThisAttack && attacking) {

            // Only apply damage on specific attack frames (e.g., 2 or 3)
            if (spriteNum == 2 || spriteNum == 3) {

                int currentWorldX = (int) worldX;
                int currentWorldY = (int) worldY;
                int solidAreaWidth = solidArea.width;
                int solidAreaHeight = solidArea.height;

                // Only handle left and right directions
                if (direction.equals("left")) {
                    worldX -= attackArea.width;
                } else if (direction.equals("right")) {
                    worldX += gp.tileSize;
                }

                // Adjust solidArea to attackArea size
                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;

                if (gp.cChecker.checkPlayer(this) ) {
                    damagePlayer(attack);
                }

                //debug
                System.out.println("Attacking frame: " + spriteNum);
                System.out.println("Player detected in attack area? " + gp.cChecker.checkPlayer(this));


                // Restore original position and solidArea
                worldX = currentWorldX;
                worldY = currentWorldY;
                solidArea.width = solidAreaWidth;
                solidArea.height = solidAreaHeight;

                damageDealtThisAttack = true;
            }
        }

        if (!attacking) {
            damageDealtThisAttack = false;
        }
    }
    public void updateSound() {

        if (sound == null) return;

        sound.setVolumeByDistance(
                (int) gp.player.worldX,
                (int) gp.player.worldY,
                (int) this.worldX,
                (int) this.worldY,
                gp.tileSize * 5 // max hearing rangea
        );


    }




    @Override
    public BufferedImage getCurrentImage() {

        BufferedImage image = null;


        // ATTACKING
        if(attacking) {

            if(direction.equals("left")) {
                image = switch(spriteNum) {
                    case 1 -> attackLeft1;
                    case 2 -> attackLeft2;
                    case 3 -> attackLeft3;
                    case 4 -> attackLeft4;
                    default -> attackLeft1;
                };
            } else {
                image = switch(spriteNum) {
                    case 1 -> attackRight1;
                    case 2 -> attackRight2;
                    case 3 -> attackRight3;
                    case 4 -> attackRight4;
                    default -> attackRight1;
                };
            }
            return image;
        }



        // SCREAMING
        if(screaming) {

            if(direction.equals("left")) {
                image = switch(spriteNum) {
                    case 1 -> scream_left_1;
                    case 2 -> scream_left_2;
                    case 3 -> scream_left_3;
                    case 4 -> scream_left_4;
                    case 5 -> scream_left_5;
                    case 6 -> scream_left_6;
                    case 7 -> scream_left_7;
                    default -> scream_left_1;
                };
            }
            else {
                image = switch(spriteNum) {
                    case 1 -> scream_right_1;
                    case 2 -> scream_right_2;
                    case 3 -> scream_right_3;
                    case 4 -> scream_right_4;
                    case 5 -> scream_right_5;
                    case 6 -> scream_right_6;
                    case 7 -> scream_right_7;
                    default -> scream_right_1;
                };
            }
            return image;
        }
        // Chance to LAND while flying (return to walking)
        if(flying && random.nextInt(100) < 1) { // 20% chance
            flying = false;
            speed = defaultSpeed;
        }

        // FLYING
        if(flying) {

            if(direction.equals("left")) {
                image = switch(spriteNum) {
                    case 1 -> fly_left1;
                    case 2 -> fly_left2;
                    case 3 -> fly_left3;
                    case 4 -> fly_left4;
                    case 5 -> fly_left5;
                    case 6 -> fly_left6;
                    case 7 -> fly_left7;
                    case 8 -> fly_left8;
                    default -> fly_left1;
                };
            }
            else {
                image = switch(spriteNum) {
                    case 1 -> fly_right1;
                    case 2 -> fly_right2;
                    case 3 -> fly_right3;
                    case 4 -> fly_right4;
                    case 5 -> fly_right5;
                    case 6 -> fly_right6;
                    case 7 -> fly_right7;
                    case 8 -> fly_right8;
                    default -> fly_right1;
                };
            }
            return image;
        }

        // WALKING
        if(direction.equals("left")) {
            image = switch(spriteNum) {
                case 1 -> left1;
                case 2 -> left2;
                case 3 -> left3;
                case 4 -> left4;
                case 5 -> left5;
                case 6 -> left6;
                case 7 -> left7;
                case 8 -> left8;
                default -> left1;
            };
        }
        else {
            image = switch(spriteNum) {
                case 1 -> right1;
                case 2 -> right2;
                case 3 -> right3;
                case 4 -> right4;
                case 5 -> right5;
                case 6 -> right6;
                case 7 -> right7;
                case 8 -> right8;
                default -> right1;
            };
        }

        return image;
    }
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);  // Draw the parrot normally

        if (gp.debug && attacking && (spriteNum == 2 || spriteNum == 3)) {
            // Calculate the position of the attackArea relative to the screen

            // Get screen coordinates of parrot
            int screenX = getScreenX();
            int screenY = getScreenY();

            int attackX = screenX;
            int attackY = screenY;

            if (direction.equals("left")) {
                attackX = screenX - attackArea.width;
            } else if (direction.equals("right")) {
                attackX = screenX + gp.tileSize;
            }

            // Draw semi-transparent red rectangle for attackArea
            Color oldColor = g2.getColor();
            g2.setColor(new Color(255, 0, 0, 100));  // Red with alpha
            g2.fillRect(attackX, attackY, attackArea.width, attackArea.height);
            g2.setColor(oldColor);

            // Optionally, draw a border to make it clearer
            g2.setColor(Color.RED);
            g2.drawRect(attackX, attackY, attackArea.width, attackArea.height);
            g2.setColor(oldColor);
        }
    }

}
