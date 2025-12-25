package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Player extends Entity{

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;
    int maxSpriteFrames = 8;
    int idleSpriteNum = 1;        // 1 or 2
    int idleCounter = 0;
    final int idleDelay = 30;     // higher = slower (30–60 feels good)
    int standStillCounter = 0;
    final int IDLE_START_DELAY = 200; // frames before idle anim starts
    int counter = 0;

    BufferedImage[] up = loadSpriteSheet("/player/UpRun", 16, 16, 8, gp.tileSize);
    BufferedImage[] down = loadSpriteSheet("/player/DownRun", 16, 16, 8, gp.tileSize);
    BufferedImage[] left = loadSpriteSheet("/player/LeftRun", 16, 16, 8, gp.tileSize);
    BufferedImage[] right = loadSpriteSheet("/player/RightRun", 16, 16, 8, gp.tileSize);


    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public enum PlayerState {
        IDLE,
        RUN,
        ATTACK,
        GUARD
    }

    public enum WeaponType {
        SWORD,
        AXE,
        PICKAXE
    }


    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp); // calling constructor of super class(from entity class)
        this.keyH=keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 22;
        solidArea.y = 28;
        solidArea.width = 18;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

//      attackArea.width = 36;  //For test sword
//      attackArea.height = 36;

        setDefaultValues(); // when u create Player object, initialize with default values
    }

    public void setDefaultValues() {

        //Default Starting Positions
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        gp.currentMap = 0;
        gp.currentArea = gp.outside;

        //Blue Gem Start Position, mapNum = 3;
//         worldX = gp.tileSize *25;
//        worldY = gp.tileSize * 9;
//        gp.currentMap = 3;

        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";

        //PLAYER STATUS
        level = 1;
        maxLife = 16;
        life = maxLife;
        maxHunger = 10;
        currentHunger = maxHunger;
        ammo = 10;
        defaultStrength = 1;
        strength = defaultStrength;           // The more strength he has, the more damage he gives.
        dexterity = 1;          // The more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 4;
        coin = 40;
        invincible = false;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = null;
        projectile = new OBJ_Fireball(gp);
        //projectile = new OBJ_Rock(gp);
        attack = getAttack();   // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield

        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
        getIdleImage();
        //setDialogue();
    }

    public void setDefaultPositions() {

        gp.currentMap = 0;
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }
    public void setDialogue() {

        dialogues[0][0] = "You are level " + level + " now!\n" + "You feel stronger!";
    }
    public void restoreStatus() {

        life = maxLife;
        currentHunger = maxHunger;
        speed = defaultSpeed;
        playerInvincible = false;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
    }

    public void setItems() {

        inventory.clear(); //cuz if game restarts inventory must be cleared first
        inventory.add(currentWeapon);
        inventory.add(currentShield);

        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));

        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));

        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Boots(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Croissant(gp));





        /*
        inventory.add(new OBJ_Lantern(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Pickaxe(gp));*/

    }

    public int getAttack() {

        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense() {

        return defense = dexterity * currentShield.defenseValue;
    }
    public int getCurrentWeaponSlot() {

        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i) == currentWeapon)
            {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }
    public int getCurrentShieldSlot() {

        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.get(i) == currentShield)
            {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }
    public void getImage() {

        int up_i = 0;
        up1 = up[up_i];
        up_i++;
        up2 = up[up_i];
        up_i++;
        up3 = up[up_i];
        up_i++;
        up4 = up[up_i];
        up_i++;
        up5 = up[up_i];
        up_i++;
        up6 = up[up_i];
        up_i++;
        up7 = up[up_i];
        up_i++;
        up8 = up[up_i];


        int down_i = 0;
        down1 = down[down_i];
        down_i ++;
        down2 = down[down_i];
        down_i ++;
        down3 = down[down_i];
        down_i ++;
        down4 = down[down_i];
        down_i ++;
        down5 = down[down_i];
        down_i ++;
        down6 = down[down_i];
        down_i ++;
        down7 = down[down_i];
        down_i ++;
        down8 = down[down_i];

        int left_i = 0;
        left1 = left[left_i];
        left_i++;
        left2 = left[left_i];
        left_i++;
        left3 = left[left_i];
        left_i++;
        left4 = left[left_i];
        left_i++;
        left5 = left[left_i];
        left_i++;
        left6 = left[left_i];
        left_i++;
        left7 = left[left_i];
        left_i++;
        left8 = left[left_i];

        int right_i = 0;
        right1 = right[right_i];
        right_i++;
        right2 = right[right_i];
        right_i++;
        right3 = right[right_i];
        right_i++;
        right4 = right[right_i];
        right_i++;
        right5 = right[right_i];
        right_i++;
        right6 = right[right_i];
        right_i++;
        right7 = right[right_i];
        right_i++;
        right8 = right[right_i];

    }
    public void getIdleImage(){

        up_idle1 = setup("/player/up_idle1", gp.tileSize, gp.tileSize);
        up_idle2 = setup("/player/up_idle2", gp.tileSize, gp.tileSize);

        down_idle1 = setup("/player/down_idle1", gp.tileSize, gp.tileSize);
        down_idle2 = setup("/player/down_idle2", gp.tileSize, gp.tileSize);

        left_idle1 = setup("/player/left_idle1", gp.tileSize, gp.tileSize);
        left_idle2 = setup("/player/left_idle2", gp.tileSize, gp.tileSize);

        right_idle1 = setup("/player/right_idle1", gp.tileSize, gp.tileSize);
        right_idle2 = setup("/player/right_idle2", gp.tileSize, gp.tileSize);

    }
    public void getSleepingImage(BufferedImage image) {


        up_idle1 = image;
        up_idle2 = image;

        down_idle1 = image;
        down_idle2 = image;

        left_idle1 = image;
        left_idle2 = image;

        right_idle1 = image;
        right_idle2 = image;
    }
    public void getAttackImage() {

        if(currentWeapon.type == type_sword)
        {
            attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }
        else if(currentWeapon.type == type_axe)
        {
            attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_axe_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }
        else if(currentWeapon.type == type_pickaxe)
        {
            attackUp1 = setup("/player/boy_pick_up_1",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackUp2 = setup("/player/boy_pick_up_2",gp.tileSize, gp.tileSize * 2);         // 16x32 px
            attackDown1 = setup("/player/boy_pick_down_1",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackDown2 = setup("/player/boy_pick_down_2",gp.tileSize, gp.tileSize * 2);     // 16x32 px
            attackLeft1 = setup("/player/boy_pick_left_1",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackLeft2 = setup("/player/boy_pick_left_2",gp.tileSize * 2, gp.tileSize);      // 32x16 px
            attackRight1 = setup("/player/boy_pick_right_1",gp.tileSize * 2, gp.tileSize);    // 32x16 px
            attackRight2 = setup("/player/boy_pick_right_2",gp.tileSize * 2, gp.tileSize);    // 32x16 px
        }

    }
    public void getGuardImage() {

        guardUp = setup("/player/boy_guard_up",gp.tileSize,gp.tileSize);
        guardDown = setup("/player/boy_guard_down",gp.tileSize,gp.tileSize);
        guardLeft = setup("/player/boy_guard_left",gp.tileSize,gp.tileSize);
        guardRight = setup("/player/boy_guard_right",gp.tileSize,gp.tileSize);
    }
    public void update() {

        // =====================
        // KNOCKBACK LOGIC
        // =====================
        if(knockBack) {

            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this,true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);
            gp.cChecker.checkTallObject(this, gp.tallObj[gp.currentMap]);


            if(collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            else {
                switch (knockBackDirection) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            knockBackCounter++;
            if(knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            return; // important so knockback overrides movement
        }

        // ATTACKING
        if(attacking) {
            attacking();
            return;
        }


        // GUARDING
        if(keyH.spacePressed) {
            guarding = true;
            guardCounter++;
            return;
        }


        // MOVEMENT INPUT
        boolean moving =
                keyH.upPressed || keyH.downPressed ||
                        keyH.leftPressed || keyH.rightPressed;

        if(moving || keyH.enterPressed) {

            movingCounter++;
            standStillCounter = 0;


            // NEW: 2-axis movement vector
            double dx = 0;
            double dy = 0;

            if(keyH.upPressed)    { dy -= 1; }
            if(keyH.downPressed)  { dy += 1; }
            if(keyH.leftPressed)  { dx -= 1; }
            if(keyH.rightPressed) { dx += 1; }

            // SET LAST FACED DIRECTION (unchanged)
            if(dy < 0) direction = "up";
            else if(dy > 0) direction = "down";
            else if(dx < 0) direction = "left";
            else if(dx > 0) direction = "right";

            // NORMALIZE DIAGONAL SPEED
            double length = Math.sqrt(dx*dx + dy*dy);
            if(length != 0) {
                dx /= length;
                dy /= length;
            }

            // COLLISIONS
            collisionOn = false;
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this,true);
            pickUpObject(objIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            gp.cChecker.checkTallObject(this, gp.tallObj[gp.currentMap]);


            gp.eHandler.checkEvent();

            // MOVE
            if(!collisionOn && !keyH.enterPressed) {
                worldX +=  (dx * speed) ;
                worldY +=  (dy * speed) ;
            }

            // ATTACK KEY
            if(keyH.enterPressed && !attackCanceled) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            keyH.enterPressed = false;
            guarding = false;
            guardCounter = 0;

            // ANIMATION
            spriteCounter++;
            if(spriteCounter > 4) {   // adjust this number for speed (lower = faster)
                spriteNum++;
                if(spriteNum > maxSpriteFrames) {
                    spriteNum = 1;

                }
                spriteCounter = 0;

            }

            //moving SE
            if(movingCounter >= 22){
                movingCounter = 0;
                gp.playSE(23);
            }

            // you have to eat every 5 min of moving
            isHungry++;
            if(isHungry >= 1800){
                isHungry = 0;
                currentHunger -= 1;
            }


        }
        else {
            //idle animation
            standStillCounter++;

            if(standStillCounter >= IDLE_START_DELAY) {
                idleCounter++;

                if(idleCounter > idleDelay) {
                    idleSpriteNum++;
                    if(idleSpriteNum > 2) {
                        idleSpriteNum = 1;
                    }
                    idleCounter = 0;
                }

            }
            guarding = false;
            guardCounter = 0;


            // you have to eat every day at least once to survive
            isHungry++;
            if(isHungry >= 3600){
                isHungry = 0;
                currentHunger -= 1;
            }
        }



        // PROJECTILE SHOOTING
        if(keyH.shotKeyPressed && !projectile.alive &&
                shotAvailableCounter == 30 &&
                projectile.haveResource(this))
        {
            projectile.set((int) worldX, (int) worldY, direction, true, this);
            projectile.subtractResource(this);

            for(int i = 0; i < gp.projectile[1].length; i++) {
                if(gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }
            shotAvailableCounter = 0;
            gp.playSE(10);
        }


        // INVINCIBILITY TIMER
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 2) {
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }

        //player has more invincibility than monsters
        if(playerInvincible){
            playerInvincibleCounter++;
            if(playerInvincibleCounter > 60) {
                playerInvincible = false;
                transparent = false;
                playerInvincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if(life > maxLife) life = maxLife;
        if(currentHunger > maxHunger) currentHunger = maxHunger;

        if(!keyH.cheat && life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12);
        }


        if (life < maxLife && currentHunger >= maxHunger -2) {

            regenCounter++;
            if (regenCounter >= 120) { // ~2 seconds at 60 FPS

                life++;

                // Consume saturation first
                if (saturation > 0) {
                    saturation -= 1;
                } else {
                    currentHunger--;
                }
                regenCounter = 0;
            }
        } else {
            regenCounter = 0;
        }


        if (currentHunger == 0) {

            isStarving = true;
            speed = 2;
            strength -= 1;

            starvationCounter++;
            if (starvationCounter > 180) {
                life--;
                starvationCounter = 0;
                int sound = new Random().nextInt(3);
                gp.playSE(27 + sound);
            }
        }
        else if(currentHunger > 0){
            speed = defaultSpeed;
            isStarving = false;
            strength = defaultStrength;
        }
    }
    public void pickUpObject(int i) {

        if(i != 999) {
            // PICKUP ONLY ITEMS
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly ) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            //OBSTACLE
            else if(gp.obj[gp.currentMap][i].type == type_obstacle) {
                if(keyH.enterPressed) {
                    attackCanceled = true;
                    gp.obj[gp.currentMap][i].interact();
                }
            }
            // INVENTORY ITEMS
            else{
                String text;
                if(canObtainItem(gp.obj[gp.currentMap][i])) //if inventory is not full can pick up object
                {
                    //inventory.add(gp.obj[gp.currentMap][i]); //canObtainItem() already adds item
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                    gp.obj[gp.currentMap][i] = null;
                }
                else{

                    text = "Inventory full!";
                }
                gp.ui.addMessage(text);

            }
        }
    }
    public void interactNPC(int i) {

        if(i != 999)
        {
            if(gp.keyH.enterPressed)
            {
                attackCanceled = true;
                gp.npc[gp.currentMap][i].speak();
            }

            gp.npc[gp.currentMap][i].move(direction);
        }
    }
    public void contactMonster(int i) {
        // CollisionChecker Method Implement //checkPlayer() : Checks who touches to player
        // checkEntity() : Checks if player touches to an entity;

        if(i != 999) {
            if(!playerInvincible && !gp.monster[gp.currentMap][i].dying && hostile) {

                //Random sound effect
                int sound = new Random().nextInt(3);
                gp.playSE(27 + sound);

                int damage = gp.monster[gp.currentMap][i].attack - defense;

                if(damage < 1) {
                    damage = 1;
                }

                life -= damage;
                playerInvincible = true;
                transparent = true;
            }
        }
    }
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {

        if(i != 999) {

            if(!gp.monster[gp.currentMap][i].invincible) {

                if(knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }
                if(gp.monster[gp.currentMap][i].offBalance) {
                    attack *= 2;
                }
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage <= 0 ) {
                    damage = 1;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();



                if(gp.monster[gp.currentMap][i].life <= 0) {

                    gp.monster[gp.currentMap][i].eating = false;
                    gp.monster[gp.currentMap][i].dying = true;

                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("Exp +" + gp.monster[gp.currentMap][i].exp + "!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void damageInteractiveTile(int i) {
        if(i != 999 && gp.iTile[gp.currentMap][i].destructible
                && gp.iTile[gp.currentMap][i].isCorrectItem(this)
                && !gp.iTile[gp.currentMap][i].invincible)
        {
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            //Generate Particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if(gp.iTile[gp.currentMap][i].life == 0)
            {
                //gp.iTile[gp.currentMap][i].checkDrop();
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }
    public void damageProjectile(int i) {
        if(i != 999)
        {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile,projectile);
        }
    }
    public void checkLevelUp() {

         while(exp >= nextLevelExp) {

             level++;
             exp = exp - nextLevelExp;          //Example: Your exp is 4 and nextLevelExp is 5. You killed a monster and receive 2exp. So, your exp is now 6. Your 1 extra xp will be recovered for the next level.
             if(level <= 4)
             {
                 nextLevelExp += 100;   //Level 2 to 6: 4xp- 8xp- 12xp- 16xp- 20xp
             }
             else
             {
                 nextLevelExp += 500;  //After Level 6: 28xp- 36xp- 44xp- 52xp- 60xp
             }
             maxLife += 2;
             defaultStrength += 1;
             dexterity++;
             attack = getAttack();
             defense = getDefense();
             gp.playSE(8); //level up.wav


             gp.ui.addMessage("You are level " + level + " now!");

         }
    }
    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
        if(itemIndex < inventory.size())
        {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword ||
                    selectedItem.type == type_axe || selectedItem.type == type_pickaxe) {

                currentWeapon = selectedItem;
                attack = getAttack();   //update player attack
                getAttackImage(); //update player attack image (sword/axe)

                //equip sound effect
                switch (selectedItem.type){
                    case type_sword : gp.playSE(33); break;
                    case type_axe : gp.playSE(34); break;
                }
            }
            if(selectedItem.type == type_shield)
            {
                currentShield = selectedItem;
                defense = getDefense(); //update player defense
            }
            if(selectedItem.type == type_light) {
                if(currentLight == selectedItem) {
                    currentLight = null;
                }
                else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
                gp.playSE(35);
            }
            if(selectedItem.type == type_consumable) {

                if(selectedItem.use(this)) {

                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    }
                    else {
                        inventory.remove(itemIndex);
                    }
                }
            }
            if(selectedItem.type == type_edible) {
                if(!(gp.player.currentHunger >= gp.player.maxHunger)) {
                    if (selectedItem.use(this)) {

                        if (selectedItem.amount > 1) {
                            selectedItem.amount--;
                        } else {
                            inventory.remove(itemIndex);
                        }
                    }
                }
            }


        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Determine if player is moving (used only for draw decision)
        boolean moving =
                keyH.upPressed || keyH.downPressed ||
                        keyH.leftPressed || keyH.rightPressed;

        // PICK SPRITE ---------------------------------------------------
        switch (direction)
        {
            case "up":
                if(attacking) {
                    tempScreenY = screenY - gp.tileSize;
                    image = (spriteNum == 1 ? attackUp1 : attackUp2);
                }
                else if(moving) {
                    image = up[spriteNum - 1];
                }
                else {
                    image = (idleSpriteNum == 1 ? up_idle1 : up_idle2);
                }

                if(guarding) image = guardUp;
                break;

            case "down":
                if(attacking) {
                    image = (spriteNum == 1 ? attackDown1 : attackDown2);
                }
                else if(moving) {
                    image = down[spriteNum - 1];
                }
                else {
                    image = (idleSpriteNum == 1 ? down_idle1 : down_idle2);
                }

                if(guarding) image = guardDown;
                break;

            case "left":
                if(attacking) {
                    tempScreenX = screenX - gp.tileSize;
                    image = (spriteNum == 1 ? attackLeft1 : attackLeft2);
                }
                else if(moving) {
                    image = left[spriteNum - 1];
                }
                else {
                    image = (idleSpriteNum == 1 ? left_idle1 : left_idle2);
                }

                if(guarding) image = guardLeft;
                break;

            case "right":
                if(attacking) {
                    image = (spriteNum == 1 ? attackRight1 : attackRight2);
                }
                else if(moving) {
                    image = right[spriteNum - 1];
                }
                else {
                    image = (idleSpriteNum == 1 ? right_idle1 : right_idle2);
                }

                if(guarding) image = guardRight;
                break;
        }

        // INVINCIBILITY TRANSPARENCY
        if(transparent) {
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.4f));
        }

        if(drawing) {
            g2.drawImage(image, tempScreenX, tempScreenY, null);
        }

        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1f));

        // DEBUG ---------------------------------------------------------
        if(gp.debug){
            g2.setFont(new Font("Arial", Font.PLAIN, 26));
            g2.setColor(Color.white);
            g2.drawString("Invincible:" + invincibleCounter, 10, 400);

            g2.setColor(Color.RED);
            g2.drawRect(
                    screenX + solidArea.x,
                    screenY + solidArea.y,
                    solidArea.width,
                    solidArea.height
            );

            tempScreenX = screenX + solidArea.x;
            tempScreenY = screenY + solidArea.y;

            switch(direction) {
                case "up":    tempScreenY = screenY - attackArea.height; break;
                case "down":  tempScreenY = screenY + gp.tileSize; break;
                case "left":  tempScreenX = screenX - attackArea.width; break;
                case "right": tempScreenX = screenX + gp.tileSize; break;
            }

            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(
                    tempScreenX,
                    tempScreenY,
                    attackArea.width,
                    attackArea.height
            );
        }
        }


}
