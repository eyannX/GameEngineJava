package animals;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;
import java.util.Random;

public class rat extends Entity {

    GamePanel gp;
    Random random = new Random();

    enum RatState {
        IDLE,
        RUN,
        DAMAGED,
        DEAD
    }

    RatState state = RatState.IDLE;

    int actionCounter = 0;
    int idleTime = 260;      // frames
    int runTime = 120;

    int damageBoostCounter = 0;

    public rat(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "rat";

        defaultSpeed = 3;
        speed = defaultSpeed;

        maxLife = 3;
        life = maxLife;

        attack = 0;
        defense = 0;
        exp = 1;
        hostile = false;

        direction = "right";

        solidArea.x = 6 ;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        getIdleImage();
    }

    public void getImage() {

        int scale = 32;

        up1 = setup("/animals/rat/rat_run_up1", scale, scale);
        up2 = setup("/animals/rat/rat_run_up2", scale, scale);
        up3 = setup("/animals/rat/rat_run_up3", scale, scale);
        up4 = setup("/animals/rat/rat_run_up4", scale, scale);

        left1  = setup("/animals/rat/rat_left_run1",  scale, scale);
        left2  = setup("/animals/rat/rat_left_run2",  scale, scale);
        left3  = setup("/animals/rat/rat_left_run3",  scale, scale);
        left4  = setup("/animals/rat/rat_left_run4",  scale, scale);

        right1 = setup("/animals/rat/rat_right_run1", scale, scale);
        right2 = setup("/animals/rat/rat_right_run2", scale, scale);
        right3 = setup("/animals/rat/rat_right_run3", scale, scale);
        right4 = setup("/animals/rat/rat_right_run4", scale, scale);
    }
    public void getIdleImage() {

        int scale = 32;

        up_idle1 = setup("/animals/rat/idle_up1", scale, scale);
        up_idle2 = setup("/animals/rat/idle_up2", scale, scale);

        left_idle1 = setup("/animals/rat/idle_left1",  scale, scale);
        left_idle2 = setup("/animals/rat/idle_left2",  scale, scale);

        right_idle1 = setup("/animals/rat/idle_right1", scale, scale);
        right_idle2 = setup("/animals/rat/idle_right2", scale, scale);
    }

    @Override
    public void setAction() {

        if (avoidPlayerIfInRange(5)) {

            if (collisionOn) {
                collisionOn = false;

                // Try perpendicular escape directions
                if (fleeDirection.equals("left") || fleeDirection.equals("right")) {
                    direction = random.nextBoolean() ? "up" : "down";
                } else {
                    direction = random.nextBoolean() ? "left" : "right";
                }

                fleeDirection = direction;
                fleeDirectionLock = 20;
            }

            state = RatState.RUN;
            speed = defaultSpeed;
            actionCounter = 0;
            return;
        }



        if (collisionOn) {
            collisionOn = false;

            // List of allowed directions to pick from when collision happens
            String[] possibleDirections;

            // Exclude current direction and pick from others, prioritizing left/right/up/down as needed
            switch (direction) {
                case "left", "right" -> {
                    // If moving left or right, try up or down first, then opposite horizontal
                    possibleDirections = new String[]{"up", "down", direction.equals("left") ? "right" : "left"};
                }
                case "up", "down" -> {
                    // If moving up or down, try left or right
                    possibleDirections = new String[]{"left", "right"};
                }
                default -> {
                    possibleDirections = new String[]{"left", "right", "up", "down"};
                }
            }

            // Randomly pick a new direction from possible directions
            direction = possibleDirections[random.nextInt(possibleDirections.length)];

            // Set to RUN state and reset counters to start moving immediately
            state = RatState.RUN;
            speed = defaultSpeed;
            actionCounter = 0;

            return; // skip rest of logic this frame
        }

        switch (state) {
            case IDLE -> {
                speed = 0;
                actionCounter++;

                if (actionCounter > idleTime) {
                    actionCounter = 0;
                    state = RatState.RUN;

                    // Pick only allowed directions on run start (no down)
                    int dirPick = random.nextInt(3);
                    switch (dirPick) {
                        case 0 -> direction = "left";
                        case 1 -> direction = "right";
                        case 2 -> direction = "up";
                    }
                }
            }

            case RUN -> {
                speed = defaultSpeed;
                actionCounter++;

                if (actionCounter > runTime) {
                    actionCounter = 0;

                    // 35% chance to idle, otherwise keep moving
                    if (random.nextInt(100) < 35) {
                        state = RatState.IDLE;
                    } else {

                        // Pick a new allowed direction randomly (left, right, up, or down)
                        int dirPick = random.nextInt(4);
                        switch (dirPick) {
                            case 0 -> direction = "left";
                            case 1 -> direction = "right";
                            case 2 -> direction = "up";
                            case 3 -> direction = "down";
                        }
                    }
                }
            }

            case DAMAGED -> {
                speed = defaultSpeed + 2;
                // Continue moving in current direction (no change)
            }

            case DEAD -> {
                speed = 0;
            }
        }
    }



    @Override
    public void damageReaction() {

        if (state == RatState.DEAD) return;

        state = RatState.DAMAGED;
        damageBoostCounter = 0;


    }

    @Override
    protected int getMaxFrame() {

        boolean moving = (state == RatState.RUN || state == RatState.DAMAGED);

        if (moving) {
            return 4; // run frames
        } else {
            return 2; // idle frames
        }
    }

    @Override
    protected int getAnimationSpeed() {

        return switch (state) {
            case IDLE -> 15;
            case RUN, DAMAGED -> 6;
            default -> 8;
        };
    }

    public void checkDrop() {
        // No drops yet, implement if needed
    }

    @Override
    public BufferedImage getCurrentImage() {

        boolean moving = (state == RatState.RUN || state == RatState.DAMAGED);

        if (moving) {
            return switch (direction) {
                case "up" -> switch (spriteNum) {
                    case 1 -> up1;
                    case 2 -> up2;
                    case 3 -> up3;
                    default -> up4;
                };
                case "left" -> switch (spriteNum) {
                    case 1 -> left1;
                    case 2 -> left2;
                    case 3 -> left3;
                    default -> left4;
                };
                case "right" -> switch (spriteNum) {
                    case 1 -> right1;
                    case 2 -> right2;
                    case 3 -> right3;
                    default -> right4;
                };
                // down uses left/right sprites (let's pick left for consistency)
                default -> switch (spriteNum) {
                    case 1 -> left1;
                    case 2 -> left2;
                    case 3 -> left3;
                    default -> left4;
                };
            };
        }

        // IDLE frames
        return switch (direction) {
            case "up" -> (spriteNum == 1 ? up_idle1 : up_idle2);
            case "left" -> (spriteNum == 1 ? left_idle1 : left_idle2);
            case "right" -> (spriteNum == 1 ? right_idle1 : right_idle2);
            // down reuses left idle sprites
            default -> (spriteNum == 1 ? left_idle1 : left_idle2);
        };
    }
}
