package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Entity {

    GamePanel gp;
    public BufferedImage up1,up2,up3,up4,up5,up6,up7,up8;
    public BufferedImage down1,down2,down3,down4,down5,down6,down7,down8;
    public BufferedImage left1,left2,left3,left4,left5,left6,left7,left8;
    public BufferedImage right1,right2,right3,right4,right5,right6,right7, right8;

    public BufferedImage up_idle1, up_idle2;
    public BufferedImage down_idle1, down_idle2;
    public BufferedImage left_idle1, left_idle2;
    public BufferedImage right_idle1, right_idle2;

    public BufferedImage eat_left_1, eat_left_2, eat_left_3, eat_left_4, eat_left_5, eat_left_6, eat_left_7, eat_left_8;
    public BufferedImage eat_right_1, eat_right_2, eat_right_3, eat_right_4, eat_right_5, eat_right_6, eat_right_7, eat_right_8;



    public BufferedImage attackUp1,attackUp2,attackUp3, attackUp4,
            attackDown1, attackDown2, attackDown3, attackDown4
            ,attackLeft1,attackLeft2, attackLeft3, attackLeft4,
            attackRight1,attackRight2, attackRight3, attackRight4,
            guardUp,guardDown,guardLeft,guardRight;

    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0,0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public String[][] dialogues = new String[20][20];
    public Entity attacker;
    public Entity linkedEntity; //link big rock and metal plate
    public boolean temp = false;

    //STATE
    public double worldX,worldY; // player's position on the map
    public String direction = "down";
    public String facing = "right";
    public int spriteNum = 1;
    public int dialogueSet = 0;
    public int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean playerInvincible = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public String knockBackDirection;
    public boolean guarding = false;
    public boolean transparent = false; //invincible when only gets damage
    public boolean offBalance = false;
    public Entity loot;
    public boolean opened = false;
    public boolean inRage = false;
    public boolean sleep = false;
    public boolean drawing = true;
    public boolean eating = false;
    public boolean hostile = false;
    public boolean screaming = false;
    public boolean flying = false;
    public int animationSpeed;
    int attackCooldown = 0;
    int attackCooldownMax = 60;
    protected String fleeDirection = null;
    protected int fleeDirectionLock = 0;






    //COUNTER

    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int playerInvincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    public int hpBarCounter = 0;
    int knockBackCounter = 0;
    public int guardCounter = 0;
    int offBalanceCounter = 0;
    public int regenCounter = 0;
    public int starvationCounter;
    public int movingCounter;

    //CHARACTER ATTRIBUTES
    public String name;
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int maxHunger;
    public int currentHunger;
    public int ammo;
    public int level;
    public int defaultStrength;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int motion1_duration;
    public int motion2_duration;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public Projectile projectile;
    public boolean boss;
    //Saturation
    public float saturation = 0;
    public float maxSaturation = 20;
    // Timers (ticks)
    public int isHungry;
    public boolean isStarving = false;
    public int maxSpriteNum = 2;


    //ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public int maxInventorySize = 20;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int value;
    public int price;
    public int knockBackPower;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;


    //TYPE
    public int type; // 0=player, 1=npc, 2=monster etc.
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;
    public final int type_pickaxe = 10;
    public final int type_edible = 11;






    public Entity(GamePanel gp) {

        this.gp = gp;

    }
    public int getScreenX() {

        return (int) (worldX - gp.player.worldX + gp.player.screenX);
    }
    public int getScreenY() {

        return (int) (worldY - gp.player.worldY + gp.player.screenY);
    }
    public int getLeftX() {

        return (int) (worldX + solidArea.x);
    }
    public int getRightX() {

        return (int) (worldX + solidArea.width + solidArea.width);
    }
    public int getTopY() {

        return (int) (worldY + solidArea.y);
    }
    public int getBottomY() {

        return (int) (worldY + solidArea.y + solidArea.height);
    }
    public int getCol() {

        return (int) ((worldX + solidArea.x) / gp.tileSize);
    }
    public int getRow() {

        return (int) ((worldY + solidArea.y) / gp.tileSize);
    }
    public int getCenterX() {
        return (int) (worldX + solidArea.x + solidArea.width / 2);
    }
    public int getCenterY() {
        return (int) (worldY + solidArea.y + solidArea.height / 2);
    }

    public int getXDistance(Entity target) {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;
    }
    public int getDistance(Entity target) {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;
    }
    public int getTileDistance(Entity target) {
        int tileDistance = (getXDistance(target) + getDistance(target))/gp.tileSize;
        return tileDistance;
    }
    public int getGoalCol(Entity target) {
        int goalCol = (int) ((target.worldX + target.solidArea.x) / gp.tileSize);
        return goalCol;

    }
    public int getGoalRow(Entity target) {
        int goalRow = (int) ((target.worldY + target.solidArea.y) / gp.tileSize);
        return goalRow;
    }
    public void resetCounter() {
        spriteCounter = 0;
        actionLockCounter = 0;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        dyingCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
    }
    public void setDialogue() {

    }
    public void setLoot(Entity loot) {

    }
    public void setAction() {

    }
    public void move(String direction) {

    }
    public void damageReaction() {

    }
    public void speak() {

    }
    public void facePlayer() {
        switch (gp.player.direction)
        {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }
    public void startDialogue(Entity entity, int setNum) {
        gp.gameState = gp.dialogueState;
        gp.ui.npc = entity;
        dialogueSet = setNum;
    }
    public void interact() {

    }
    public boolean use(Entity entity) {
        return false;
        //return "true" if you used the item and "false" if you failed to use it.
    }
    public void checkDrop() {

    }
    public void dropItem(Entity droppedItem) {
        for(int i = 0; i < gp.obj[1].length; i++)
        {
            if(gp.obj[gp.currentMap][i] == null)
            {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;  //the dead monster's worldX
                gp.obj[gp.currentMap][i].worldY = worldY;  //the dead monster's worldY
                break; //end loop after finding empty slot on array
            }
        }
    }
    public Color getParticleColor() {
        Color color = null;
        //Subclass specifications
        return color;
    }
    public int getParticleSize() {
        int size = 0; //pixels
        //Subclass specifications
        return size;
    }
    public int getParticleSpeed() {
        int speed = 0;
        //Subclass specifications
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 0;
        //Subclass specifications
        return maxLife;
    }
    public BufferedImage getCurrentImage() {

        return null; // subclasses decide
    }
    protected BufferedImage getDirectionalImage(

            BufferedImage up1, BufferedImage up2,
            BufferedImage down1, BufferedImage down2,
            BufferedImage left1, BufferedImage left2,
            BufferedImage right1, BufferedImage right2) {

        switch (direction) {
            case "up":    return (spriteNum == 1) ? up1 : up2;
            case "down":  return (spriteNum == 1) ? down1 : down2;
            case "left":  return (spriteNum == 1) ? left1 : left2;
            case "right": return (spriteNum == 1) ? right1 : right2;
        }
        return null;
    }


    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        //generator becomes target so particles appear where the monster is.
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);    //TOP-LEFT
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);     //TOP-RIGHT
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);     //DOWN-LEFT
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);      //DOWN-RIGHT

        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }
    public void checkCollision() {

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this,gp.iTile);
        gp.cChecker.checkTallObject(this, gp.tallObj[gp.currentMap]);;
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        //debug
        if(this.type == type_monster && hostile && contactPlayer) {
            damagePlayer(attack);
            System.out.println(name + " hostile=" + hostile + " attack=" + attack);
        }
    }
    public void update() {

        if (sleep) return;


       //KNOCKBACK
        if (knockBack) {
            checkCollision();

            if (collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else {
                switch (knockBackDirection) {
                    case "up"    -> worldY -= speed;
                    case "down"  -> worldY += speed;
                    case "left"  -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            knockBackCounter++;
            if (knockBackCounter >= 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
    /* =========================
       2. ATTACK LOGIC IS HANDLED IN setAction() AND SPRITE ANIMATION
       SO NO CALL TO attacking() METHOD
       ========================= */
        else {
            setAction();
            checkCollision();

            if (!collisionOn) {
                switch (direction) {
                    case "up"    -> worldY -= speed;
                    case "down"  -> worldY += speed;
                    case "left"  -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }
        }


       //SPRITE ANIMATION (ALWAYS)
        spriteCounter++;
        if (spriteCounter >= getAnimationSpeed()) {
            spriteCounter = 0;
            spriteNum++;

            int maxFrame = getMaxFrame();

            if (spriteNum > maxFrame) {

                if (attacking) {
                    attacking = false;
                    attackCooldown = attackCooldownMax; // IMPORTANT: reset cooldown here
                }

                spriteNum = 1;

                if (screaming) {
                    screaming = false;
                    speed = defaultSpeed;
                }
            }
        }

    /* =========================
       4. TIMERS
       ========================= */
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (offBalance) {
            offBalanceCounter++;
            if (offBalanceCounter > 60) {
                offBalance = false;
                offBalanceCounter = 0;
            }
        }
    }


    protected int getMaxFrame() {

        return 2;
    }
    protected int getAnimationSpeed() {

        //default animation speed
        return 22;
    }
    /**
     * Steers entity away from player while handling collisions.
     * If the primary escape direction is blocked, tries lateral directions.
     *
     * @param tileRange detection radius in tiles
     * @return true if flee logic was applied
     */
    protected boolean avoidPlayerIfInRange(int tileRange) {

        int maxDistance = tileRange * gp.tileSize;

        int dx = (int) (worldX - gp.player.worldX);
        int dy = (int) (worldY - gp.player.worldY);

        if (Math.abs(dx) > maxDistance && Math.abs(dy) > maxDistance) {
            fleeDirection = null;
            fleeDirectionLock = 0;
            return false;
        }

        // If we are locked into a flee direction, keep using it
        if (fleeDirectionLock > 0) {
            direction = fleeDirection;
            fleeDirectionLock--;
            return true;
        }

        // Pick primary flee direction (away from player)
        if (Math.abs(dx) > Math.abs(dy)) {
            fleeDirection = (dx > 0) ? "right" : "left";
        } else {
            fleeDirection = (dy > 0) ? "down" : "up";
        }

        direction = fleeDirection;
        fleeDirectionLock = 20; // ~0.33s at 60 FPS
        return true;
    }

    public void checkAttackOrNot(int rate, int straight, int horizontal) {

        boolean tartgetInRange = false;
        int xDis = getXDistance(gp.player);
        int yDis = getDistance(gp.player);

        switch (direction)
        {
            case "up":
                if(gp.player.getCenterY() < getCenterY()  && yDis < straight && xDis < horizontal)
                {
                    tartgetInRange = true;
                }
                break;
            case "down":
                if(gp.player.getCenterY()  > getCenterY()  && yDis < straight && xDis < horizontal)
                {
                    tartgetInRange = true;
                }
                break;
            case "left":
                if(gp.player.getCenterX()  < getCenterX() && xDis < straight && yDis < horizontal)
                {
                    tartgetInRange = true;
                }
                break;
            case "right":
                if(gp.player.getCenterX() > getCenterX() && xDis < straight && yDis < horizontal)
                {
                    tartgetInRange = true;
                }
                break;
        }

        if(tartgetInRange) {

            //Check if it initiates an attack
            int i = new Random().nextInt(rate);
            if(i == 0)
            {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }

    }
    public void checkAttackLeftRight(int attackChance, int maxHorizontalDistance, int maxVerticalDistance) {


        if (attacking) return;

        boolean targetInRange = false;

        int xDis = getXDistance(gp.player); // horizontal distance between entity and player
        int yDis = getDistance(gp.player);  // overall distance (can be diagonal)

        switch (direction) {
            case "left":
                // Player must be to the left, close horizontally and vertically within limits
                if (gp.player.getCenterX() < getCenterX() && xDis <= maxHorizontalDistance && yDis <= maxVerticalDistance) {
                    targetInRange = true;
                }
                break;
            case "right":
                // Player must be to the right, close horizontally and vertically within limits
                if (gp.player.getCenterX() > getCenterX() && xDis <= maxHorizontalDistance && yDis <= maxVerticalDistance) {
                    targetInRange = true;
                }
                break;
        }

        if (targetInRange) {
            // Random chance to attack based on rate
            int i = new Random().nextInt(attackChance);
            if (i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0; // if used
            }
        }
    }





    public void checkShootOrNot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);
        if(i == 0 && !projectile.alive && shotAvailableCounter == shotInterval) {

            projectile.set((int) worldX,(int) worldY,direction,true,this);
            //gp.projectileList.add(projectile);
            //CHECK VACANCY
            for(int ii = 0; ii < gp.projectile[1].length;ii++) {

                if(gp.projectile[gp.currentMap][ii] == null) {

                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }
            shotAvailableCounter = 0;
        }
    }
    public void checkStartChasingOrNot(Entity target, int distance, int rate) {
        if(getTileDistance(target) < distance)
        {
            int i = new Random().nextInt(rate);
            if(i == 0)
            {
                onPath = true;
            }
        }
    }
    public void checkStopChasingOrNot(Entity target, int distance, int rate) {

        if(getTileDistance(target) > distance)
        {
            int i = new Random().nextInt(rate);
            if(i == 0)
            {
                onPath = false;
            }
        }
    }
    public void getRandomDirection(int interval) {
        actionLockCounter++;

        if(actionLockCounter > interval)
        {
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
            if(i <= 25){direction = "up";}
            if(i>25 && i <= 50){direction = "down";}
            if(i>50 && i <= 75){direction = "left";}
            if(i>75 && i <= 100){direction = "right";}
            actionLockCounter = 0; // reset
        }
    }
    public void moveTowardPlayer(int interval) {
        actionLockCounter++;

        if(actionLockCounter > interval)
        {
            if(getXDistance(gp.player) > getDistance(gp.player)) //if entity far to the player on X axis moves right or left
            {
                if(gp.player.getCenterX() < getCenterX()) //Player is left side, entity moves to left
                {
                    direction = "left";
                }
                else
                {
                    direction = "right";
                }
            }
            else if(getXDistance(gp.player) < getDistance(gp.player))  //if entity far to the player on Y axis moves up or down
            {
                if(gp.player.getCenterY() < getCenterY()) //Player is up side, entity moves to up
                {
                    direction = "up";
                }
                else
                {
                    direction = "down";
                }
            }
            actionLockCounter = 0;
        }
    }
    public String getOppositeDirection(String direction) {
        String oppositeDirection = "";

        switch (direction)
        {
            case "up": oppositeDirection = "down";break;
            case "down": oppositeDirection = "up";break;
            case "left": oppositeDirection = "right";break;
            case "right": oppositeDirection = "left";break;
        }

        return oppositeDirection;
    }
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i).name.equals(itemName))
            {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public void attacking() {

        spriteCounter++;

        if(spriteCounter <= motion1_duration) {

            spriteNum = 1;
        }
        if(spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {

            spriteNum = 2;

            //Save the current worldX, worldY, solidArea
            int currentWorldX = (int)  worldX;
            int currentWorldY = (int)  worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/worldY for the attackArea
            switch (direction)
            {
                case "up": worldY -= attackArea.height; break;                 //attackArea's size
                case "down" : worldY += (gp.tileSize); break;                    //gp.tileSize(player's size)
                case "left" : worldX -= attackArea.width; break;               //attackArea's size
                case "right" : worldX += gp.tileSize; break;                   //gp.tileSize(player's size)
            }

            //attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            if(type == type_monster && hostile) {

                if(gp.cChecker.checkPlayer(this) && hostile) { //This means attack is hitting player
                    damagePlayer(attack);
                }
            }
            else // Player
            {
                //Check monster collision with the updated worldX, worldY and solidArea
                int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
                gp.player.damageInteractiveTile(iTileIndex);

                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }

            //After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > motion2_duration)
        {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void damagePlayer(int attack) {


        if(!gp.player.playerInvincible) {

            int damage = attack - gp.player.defense;

            //Get an opposite direction of this attacker
            String canGuardDirection = getOppositeDirection(direction);

            if(gp.player.guarding && gp.player.direction.equals(canGuardDirection)) {
                //Parry
                // If you press guard key less then 10 frames before the attack you receive 0 damage, and you get critical chance
                if(gp.player.guardCounter < 10) {

                    damage = 0;

                    gp.playSE(16);
                    setKnockBack(this, gp.player, knockBackPower); //Knockback attacker //You can use shield's knockBackPower!
                    offBalance = true;
                    spriteCounter =- 60; //Attacker's sprites returns to motion1//like a stun effect
                }
                else {
                    //Normal Guard
                    damage =0;
                    gp.playSE(15);
                }
            }
            else { //Not guarding


                //Random sound effect
                Random random = new Random();
                int sound = random.nextInt(0, 2);
                gp.playSE(27 + sound);


                if(damage < 1 && hostile) {
                    damage = 1;
                }
            }
            if(damage != 0) {

                gp.player.transparent = true;
                setKnockBack(gp.player, this, knockBackPower);
            }

            //We can give damage
            gp.player.life -= damage;
            gp.player.playerInvincible = true;
        }
    }
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {

        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }
    public boolean inCamera() {

        boolean inCamera = false;
        if(     worldX + gp.tileSize*5 > gp.player.worldX - gp.player.screenX && //*5 because skeleton lord disappears when the top left corner isn't on the screen
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize*5 > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
        {
            inCamera = true;
        }
        return inCamera;
    }

    public void drawSolidArea(Graphics2D g2, GamePanel gp) {

        int screenX = (int)(worldX + solidArea.x - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(worldY + solidArea.y - gp.player.worldY + gp.player.screenY);

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(screenX, screenY, solidArea.width, solidArea.height);
    }



    public void draw(Graphics2D g2) {

        if(!inCamera()) return;

        BufferedImage image = getCurrentImage();
        if(image == null) return;

        int screenX = getScreenX();
        int screenY = getScreenY();

        int drawX = screenX;
        int drawY = screenY;

        // ATTACK OFFSET (SAFE)
        if(attacking) {

            // Horizontal attackers (Parrot, etc.)
            if(direction.equals("left")) {
                drawX -= image.getWidth() - gp.tileSize;
            }

            // Vertical attackers (Player, swords, etc.)
            if(direction.equals("up")) {
                drawY -= image.getHeight() - gp.tileSize;
            }
        }


        // INVINCIBILITY FLASH
        if(invincible || playerInvincible) {
            hpBarOn = true;
            hpBarCounter = 0;
            changeAlpha(g2, 0.4f);
        }


        // DYING ANIMATION
        if(dying) {
            dyingAnimation(g2);
        }

        // DRAW ENTITY
        g2.drawImage(image, drawX, drawY, null);


        // DEBUG HITBOX (OPTIONAL)
        if(gp.debug) {
            g2.setColor(Color.RED);
            g2.drawRect(
                    drawX + solidArea.x,
                    drawY + solidArea.y,
                    solidArea.width,
                    solidArea.height
            );
        }


        // RESET ALPHA
        changeAlpha(g2, 1f);
    }
    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;

        // Check if item is equipped (only if Player)
        if (this == gp.player) {
            if (item == gp.player.currentWeapon || item == gp.player.currentShield) {
                // Can't put equipped items into chest or inventory again
                return false;
            }
        }

        Entity newItem = gp.eGenerator.getObject(item.name);

        //CHECK IF STACKABLE
        if(newItem.stackable)
        {
            int index = searchItemInInventory(newItem.name);

            if(index != 999)
            {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else
            {
                //New item, so need to check vacancy
                if(inventory.size() != maxInventorySize)
                {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        //NOT STACKABLE so check vacancy
        else
        {
            if(inventory.size() != maxInventorySize)
            {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return  canObtain;
    }
    public Entity copy() {
        Entity clone = new Entity(gp);  // or however you instantiate your Entity

        // Copy all relevant fields
        clone.name = this.name;
        clone.down1 = this.down1;  // assuming these are images or resources shared among all copies
        clone.stackable = this.stackable;
        clone.amount = this.amount;
        clone.maxInventorySize = this.maxInventorySize;
        // copy other necessary fields here, like description, type, stats etc.

        return clone;
    }

    public boolean canObtainItem(Entity item, int amountToAdd) {
        boolean canObtain = false;

        Entity newItem = gp.eGenerator.getObject(item.name);

        if(newItem.stackable) {
            int index = searchItemInInventory(newItem.name);

            if(index != 999) {
                // Increase existing stack by the full amountToAdd
                inventory.get(index).amount += amountToAdd;
                canObtain = true;
            } else {
                // New item - check space
                if(inventory.size() < maxInventorySize) {
                    newItem.amount = amountToAdd; // set amount correctly
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        } else {
            // Non-stackable, just check space for 1 item (amount ignored)
            if(inventory.size() < maxInventorySize) {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return canObtain;
    }

    // Every 5 frames switch alpha between 0 and 1
    public void dyingAnimation(Graphics2D g2) {

        dyingCounter++;
        int i = 5;    //interval

        if(dyingCounter <= i) {changeAlpha(g2,0f);}                             //If you want add death animation or something like that, you can use your sprites instead of changing alpha inside of if statements
        if(dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2,1f);}
        if(dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2,0f);}
        if(dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2,1f);}
        if(dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2,0f);}
        if(dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2,1f);}
        if(dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2,0f);}
        if(dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2,1f);}
        if(dyingCounter > i*8)
        {
            alive = false;
        }
    }
    public void changeAlpha(Graphics2D g2, float alphaValue) {

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
    }
    public BufferedImage setup(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image,width,height);   //it scales to tile size , will fix for player attack(16px x 32px) by adding width and height
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    public void searchPath(int goalCol, int goalRow) {

        int startCol = (int)  (worldX + solidArea.x) / gp.tileSize;
        int startRow = (int)  (worldY + solidArea.y) / gp.tileSize;
        gp.pFinder.setNodes(startCol,startRow,goalCol,goalRow,this);
        if(gp.pFinder.search())
        {
            //Next WorldX and WorldY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //Entity's solidArea position
            int enLeftX = (int) worldX + solidArea.x;
            int enRightX =(int)  worldX + solidArea.x + solidArea.width;
            int enTopY = (int) worldY + solidArea.y;
            int enBottomY = (int)  worldY + solidArea.y + solidArea.height;

            // TOP PATH
            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize)
            {
                direction = "up";
            }
            // BOTTOM PATH
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize)
            {
                direction = "down";
            }
            // RIGHT - LEFT PATH
            else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize)
            {
                //either left or right
                // LEFT PATH
                if(enLeftX > nextX)
                {
                    direction = "left";
                }
                // RIGHT PATH
                if(enLeftX < nextX)
                {
                    direction = "right";
                }
            }
            //OTHER EXCEPTIONS
            else if(enTopY > nextY && enLeftX > nextX)
            {
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn)
                {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX)
            {
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn)
                {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX)
            {
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn)
                {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX)
            {
                // down or right
                direction = "down";
                checkCollision();
                if(collisionOn)
                {
                    direction = "right";
                }
            }
            // for following player, disable this. It should be enabled when npc walking to specified location
//            int nextCol = gp.pFinder.pathList.get(0).col;
//            int nextRow = gp.pFinder.pathList.get(0).row;
//            if(nextCol == goalCol && nextRow == goalRow)
//            {
//                onPath = false;
//            }
        }
    }
    public int getDetected(Entity user, Entity[][] target, String targetName) {

        int index = 999;

        //Check the surrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction)
        {
            case "up" : nextWorldY = user.getTopY() - gp.player.speed; break;
            case "down": nextWorldY = user.getBottomY() + gp.player.speed; break;
            case "left": nextWorldX = user.getLeftX() - gp.player.speed; break;
            case "right": nextWorldX = user.getRightX() + gp.player.speed; break;
        }
        int col = nextWorldX/gp.tileSize;
        int row = nextWorldY/gp.tileSize;

        for(int i = 0; i < target[1].length; i++)
        {
            if(target[gp.currentMap][i] != null)
            {
                if (target[gp.currentMap][i].getCol() == col                                //checking if player 1 tile away from target (key etc.) (must be same direction)
                        && target[gp.currentMap][i].getRow() == row
                            && target[gp.currentMap][i].name.equals(targetName))
                {
                    index = i;
                    break;
                }
            }

        }
        return  index;
    }
    public BufferedImage[] loadSpriteSheet(String imagePath, int frameWidth, int frameHeight, int frameCount, int scale) {

        BufferedImage spriteSheet;
        BufferedImage[] frames = new BufferedImage[frameCount];

        try {
            InputStream is = getClass().getResourceAsStream(imagePath + ".png");
            if (is == null) throw new IOException("Image not found: " + imagePath + ".png");

            spriteSheet = ImageIO.read(is);

            for (int i = 0; i < frameCount; i++) {
                int x = i * frameWidth;
                int y = 0;

                // Extract subimage
                BufferedImage frame = spriteSheet.getSubimage(x, y, frameWidth, frameHeight);

                // Scale frame
                BufferedImage scaledFrame = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledFrame.createGraphics();
                g2.drawImage(frame, 0, 0, scale, scale, null);
                g2.dispose();

                frames[i] = scaledFrame;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return frames;
    }
}
