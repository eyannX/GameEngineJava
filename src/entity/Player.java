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
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;
    int maxSpriteFrames = 6;
    int idleSpriteNum = 1;
    int idleCounter = 0;
    final int idleDelay = 30;     // higher = slower (30–60 feels good)
    int standStillCounter = 0;
    final int IDLE_START_DELAY = 200; // frames before idle anim starts
    // Footstep system
    private float stepDistanceCounter = 0f;
    private final float STEP_DISTANCE = gp.tileSize * 1.3f; // tune this

    public OBJ_Quiver equippedQuiver;



    int scale = 120;

    BufferedImage[] up = loadSpriteSheet("/player/player_run/run_up", 32, 32, 6, scale);
    BufferedImage[] down = loadSpriteSheet("/player/player_run/run_down", 32, 32, 6, scale);
    BufferedImage[] left = loadSpriteSheet("/player/player_run/run_left", 32, 32, 6, scale);
    BufferedImage[] right = loadSpriteSheet("/player/player_run/run_right", 32, 32, 6, scale);



    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH=keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 22;
        solidArea.y = 20;
        solidArea.width = 22;
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
        projectile = new OBJ_Arrow(gp);
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

        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));

        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));

        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_cookie(gp));
        inventory.add(new OBJ_Quiver(gp));
        inventory.add(new OBJ_Bow(gp));
        inventory.add(new OBJ_Lantern(gp));
        inventory.add(new OBJ_Pickaxe(gp));
        inventory.add(new OBJ_Axe(gp));





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


    }
    public void getIdleImage(){

        up_idle1 = setup("/player/player_idle/idle_up1", scale, scale);
        up_idle2 = setup("/player/player_idle/idle_up2", scale, scale);

        down_idle1 = setup("/player/player_idle/idle_down1", scale, scale);
        down_idle2 = setup("/player/player_idle/idle_down2", scale, scale);

        left_idle1 = setup("/player/player_idle/idle_left1", scale, scale);
        left_idle2 = setup("/player/player_idle/idle_left2", scale, scale);

        right_idle1 = setup("/player/player_idle/idle_right1", scale, scale);
        right_idle2 = setup("/player/player_idle/idle_right2", scale, scale);

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
            attackUp1 = setup("/player/player_attack/attack_up1",scale, scale);
            attackUp2 = setup("/player/player_attack/attack_up2",scale, scale);
            attackDown1 = setup("/player/player_attack/attack_down1",scale, scale);
            attackDown2 = setup("/player/player_attack/attack_down2",scale, scale);
            attackLeft1 = setup("/player/player_attack/attack_left1",scale, scale);
            attackLeft2 = setup("/player/player_attack/attack_left2",scale, scale);
            attackRight1 = setup("/player/player_attack/attack_right1",scale, scale);
            attackRight2 = setup("/player/player_attack/attack_right2",scale, scale);
        }
        else if(currentWeapon.type == type_axe)
        {





            attackUp1 = setup("/player/player_attack_axe/up1",scale, scale);
            attackUp2 = setup("/player/player_attack_axe/up2",scale, scale);
            attackDown1 = setup("/player/player_attack_axe/down1",scale, scale );
            attackDown2 = setup("/player/player_attack_axe/down2",scale, scale );
            attackLeft1 = setup("/player/player_attack_axe/left1",scale , scale);
            attackLeft2 = setup("/player/player_attack_axe/left2",scale , scale);
            attackRight1 = setup("/player/player_attack_axe/right1",scale , scale);
            attackRight2 = setup("/player/player_attack_axe/right2",scale, scale);
        }
        else if(currentWeapon.type == type_pickaxe)
        {


            attackUp1 = setup("/player/player_attack_pickaxe/up1",scale, scale);
            attackUp2 = setup("/player/player_attack_pickaxe/up2",scale, scale);
            attackDown1 = setup("/player/player_attack_pickaxe/down1",scale, scale);
            attackDown2 = setup("/player/player_attack_pickaxe/down2",scale, scale);
            attackLeft1 = setup("/player/player_attack_pickaxe/left1",scale, scale);
            attackLeft2 = setup("/player/player_attack_pickaxe/left2",scale, scale);
            attackRight1 = setup("/player/player_attack_pickaxe/right1",scale, scale);
            attackRight2 = setup("/player/player_attack_pickaxe/right2",scale, scale);
        }
        else if(currentWeapon.type == type_bow)
        {
            attackUp1 = setup("/player/player_bow/bowUp1",scale, scale);
            attackUp2 = setup("/player/player_bow/bowUp2",scale, scale);
            attackUp3 = setup("/player/player_bow/bowUp3",scale, scale);
            attackUp4 = setup("/player/player_bow/bowUp4",scale, scale);

            attackDown1 = setup("/player/player_bow/bowDown1",scale, scale);
            attackDown2 = setup("/player/player_bow/bowDown2",scale, scale);
            attackDown3 = setup("/player/player_bow/bowDown3",scale, scale);
            attackDown4 = setup("/player/player_bow/bowDown4",scale, scale);

            attackLeft1 = setup("/player/player_bow/bowLeft1",scale, scale);
            attackLeft2 = setup("/player/player_bow/bowLeft2",scale, scale);
            attackLeft3 = setup("/player/player_bow/bowLeft3",scale, scale);
            attackLeft4 = setup("/player/player_bow/bowLeft4",scale, scale);

            attackRight1 = setup("/player/player_bow/bowRight1",scale, scale);
            attackRight2 = setup("/player/player_bow/bowRight2",scale, scale);
            attackRight3 = setup("/player/player_bow/bowRight3",scale, scale);
            attackRight4 = setup("/player/player_bow/bowRight4",scale, scale);



        }

    }
    public void getGuardImage() {

        guardUp = setup("/player/player_guard/up",scale,scale);
        guardDown = setup("/player/player_guard/down",scale,scale);
        guardLeft = setup("/player/player_guard/left",scale,scale);
        guardRight = setup("/player/player_guard/right",scale,scale);
    }
    public void update() {


        // KNOCKBACK LOGIC
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


        // BOW CHARGING (RMB)
        if(currentWeapon != null && currentWeapon.type == type_bow) {

            // Start charging
            if(gp.mouseH.rightPressed && !chargingBow) {
                chargingBow = true;
                bowCharge = 0;
                spriteCounter = 0;
            }

            // Charging
            if(gp.mouseH.rightPressed && chargingBow) {
                bowCharge++;
                if(bowCharge > MAX_BOW_CHARGE) {
                    bowCharge = MAX_BOW_CHARGE;
                }
            }

            // Release → shoot
            if(!gp.mouseH.rightPressed && chargingBow) {

                if(bowCharge >= MIN_BOW_CHARGE) {
                    shootArrow();   // we define this below
                }

                chargingBow = false;
                bowCharge = 0;
            }
        }


        if(gp.keyH.quiverKeyPressed) {

            OBJ_Quiver q = getQuiverFromInventory();

            if(q != null) {
                equippedQuiver = q;
                gp.ui.quiverSlotRow = 0;
                gp.gameState = gp.quiverState;
            }

            gp.keyH.quiverKeyPressed = false;
            return;
        }



        if(gp.gameState == gp.quiverState) {

            if(gp.keyH.dropKeyPressed) {
                gp.ui.dropArrowFromQuiver();
                gp.keyH.dropKeyPressed = false;
            }

            return;
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

        if(moving || keyH.enterPressed || gp.mouseH.leftJustClicked) {

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

                float moveSpeed = speed;

                // Bow slow
                if(currentWeapon.type == type_bow && chargingBow) {

                    moveSpeed = BOW_CHARGE_SPEED_MULT;
                }

                float moveX = (float)(dx * moveSpeed);
                float moveY = (float)(dy * moveSpeed);

                worldX += moveX;
                worldY += moveY;

                // Footstep distance tracking
                stepDistanceCounter += Math.abs(moveX) + Math.abs(moveY);

                if(stepDistanceCounter >= STEP_DISTANCE) {
                    stepDistanceCounter = 0;
                    gp.playSE(23);
                }
            }


            // ATTACK KEY
            if((keyH.enterPressed || gp.mouseH.leftJustClicked)
                    && !attackCanceled
                    && !attacking
                    && currentWeapon.type != type_bow) {

                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            gp.mouseH.leftJustClicked = false;
            keyH.enterPressed = false;
            attackCanceled = false;

            guarding = false;
            guardCounter = 0;


            // ANIMATION
            spriteCounter++;
            if(spriteCounter > 5) {   // adjust this number for speed (lower = faster)
                spriteNum++;
                if(spriteNum > maxSpriteFrames) {
                    spriteNum = 1;

                }
                spriteCounter = 0;

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
            if(playerInvincibleCounter > 30) {
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
                int sound = RNG.nextInt(3);
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

        if(i == 999) return;

        Entity obj = gp.obj[gp.currentMap][i];
        String text;

        // PICKUP ONLY 
        if(obj.type == type_pickupOnly) {
            obj.use(this);
            gp.obj[gp.currentMap][i] = null;
            gp.renderListDirty = true;
            return;
        }

        // OBSTACLE / INTERACTABLE
        if(obj.type == type_obstacle) {
            if(keyH.enterPressed || keyH.ePressed) {
                attackCanceled = true;
                obj.interact();
            }
            return;
        }

        if(obj.type == type_solidLight){
            return;
        }

        // ARROWS → QUIVER
        if(obj.type == type_arrow) {
            if(canObtainArrow(obj)) {
                gp.playSE(1);
                text = "Picked up arrows";
                gp.obj[gp.currentMap][i] = null;
                gp.renderListDirty = true;

            } else {
                text = "No quiver space!";
            }
            gp.ui.addMessage(text);
            return;
        }

        // CONTAINERS (QUIVER, BAGS, ETC.)
        if(obj.type == type_container) {
            if(canObtainContainer(obj)) {
                gp.playSE(1);
                text = "Picked up " + obj.name;
                inventory.add(obj); // same instance
                gp.obj[gp.currentMap][i] = null;
                gp.renderListDirty = true;

            } else {
                text = "Inventory full!";
            }
            gp.ui.addMessage(text);
            return;
        }

        // NORMAL ITEMS
        if(canObtainItem(obj)) {
            gp.playSE(1);
            text = "Got a " + obj.name + "!";
            gp.obj[gp.currentMap][i] = null;
            gp.renderListDirty = true;

        } else {
            text = "Inventory full!";
        }

        gp.ui.addMessage(text);
    }

    public boolean canObtainArrow(Entity arrow) {

        OBJ_Quiver quiver = getEquippedQuiver();
        if(quiver == null) return false;

        for(Entity a : quiver.inventory) {
            if(a.name.equals(arrow.name)) {
                a.amount += arrow.amount;
                return true;
            }
        }

        if(quiver.inventory.size() < quiver.maxInventorySize) {
            quiver.inventory.add(arrow);
            return true;
        }

        return false;
    }
    public OBJ_Quiver getEquippedQuiver() {

        if(equippedQuiver != null) return equippedQuiver;

        for(Entity e : inventory) {
            if(e instanceof OBJ_Quiver) {
                equippedQuiver = (OBJ_Quiver)e;
                return equippedQuiver;
            }
        }
        return null;
    }




    public void shootArrow() {

        // 1. Must have a quiver
        if(equippedQuiver == null) return;

        // 2. Quiver must have arrows
        if(equippedQuiver.inventory.isEmpty()) return;

        // 3. Get current arrow type (top slot for now)
        Entity arrowItem = equippedQuiver.inventory.get(0);

        for(int i = 0; i < gp.projectile[gp.currentMap].length; i++) {

            if(gp.projectile[gp.currentMap][i] == null ||
                    !gp.projectile[gp.currentMap][i].alive) {

                // 4. Create projectile
                Projectile arrow = new OBJ_Arrow(gp);
                gp.renderListDirty = true;

                float ratio = (float) bowCharge / MAX_BOW_CHARGE;

                arrow.speed = (int)(6 + ratio * 10);
                arrow.maxLife = (int)(30 + ratio * 60);
                arrow.attack = (int)(1 + ratio * 4);
                arrow.knockBackPower = (int)(1 + ratio * 3);

                arrow.set(
                        (int) worldX,
                        (int) worldY,
                        direction,
                        true,
                        this
                );

                gp.projectile[gp.currentMap][i] = arrow;

                // 5. Consume ONE arrow
                arrowItem.amount--;

                if(arrowItem.amount <= 0) {
                    equippedQuiver.inventory.remove(0);
                }

                gp.playSE(10);
                break;
            }
        }

        // 6. Reset charge after shot
        bowCharge = 0;
        chargingBow = false;
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
                    selectedItem.type == type_axe || selectedItem.type == type_pickaxe || selectedItem.type == type_bow) {

                currentWeapon = selectedItem;
                attack = getAttack();   //update player attack
                getAttackImage(); //update player attack image (sword/axe)

                //equip sound effect
                switch (selectedItem.type){
                    case type_sword : gp.playSE(33); break;
                    case type_axe : gp.playSE(34); break;
                    case type_bow : gp.playSE(10); break;
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
    public OBJ_Quiver getQuiverFromInventory() {

        for(Entity item : inventory) {
            if(item instanceof OBJ_Quiver) {
                return (OBJ_Quiver) item;
            }
        }
        return null;
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

                if(currentWeapon.type == type_bow && chargingBow) {

                    int frame;
                    float ratio = (float) bowCharge / MAX_BOW_CHARGE;

                    if(ratio < 0.25f) frame = 1;
                    else if(ratio < 0.5f) frame = 2;
                    else if(ratio < 0.75f) frame = 3;
                    else frame = 4;

                    image = frame == 1 ? attackUp1 : frame == 2 ? attackUp2 : frame == 3 ? attackUp3 : attackUp4;
                }

                else if(attacking) {

                    if(currentWeapon.type == type_pickaxe){

                    }
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

                if(currentWeapon.type == type_bow && chargingBow) {

                    int frame;
                    float ratio = (float) bowCharge / MAX_BOW_CHARGE;

                    if(ratio < 0.25f) frame = 1;
                    else if(ratio < 0.5f) frame = 2;
                    else if(ratio < 0.75f) frame = 3;
                    else frame = 4;

                    image = frame == 1 ? attackDown1 : frame == 2 ? attackDown2 : frame == 3 ? attackDown3 : attackDown4;
                }
                else if(attacking) {
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

                if(currentWeapon.type == type_bow && chargingBow) {

                    int frame;
                    float ratio = (float) bowCharge / MAX_BOW_CHARGE;

                    if(ratio < 0.25f) frame = 1;
                    else if(ratio < 0.5f) frame = 2;
                    else if(ratio < 0.75f) frame = 3;
                    else frame = 4;

                    image = frame == 1 ? attackLeft1 : frame == 2 ? attackLeft2 : frame == 3 ? attackLeft3 : attackLeft4;
                }
                else if(attacking) {

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

                if(currentWeapon.type == type_bow && chargingBow) {

                    int frame;
                    float ratio = (float) bowCharge / MAX_BOW_CHARGE;

                    if(ratio < 0.25f) frame = 1;
                    else if(ratio < 0.5f) frame = 2;
                    else if(ratio < 0.75f) frame = 3;
                    else frame = 4;

                    image = frame == 1 ? attackRight1 : frame == 2 ? attackRight2 : frame == 3 ? attackRight3 : attackRight4;
                }
                else if(attacking) {
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
            g2.drawImage(image, tempScreenX - 28, tempScreenY - 28, null);
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
