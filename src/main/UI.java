package main;

import entity.Entity;
import object.OBJ_Dinar;
import object.OBJ_Heart;
import object.OBJ_HungerBar;
import entity.Player;
import object.OBJ_Quiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    public Font maruMonica, GhostTheory;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin, SettingImage, rPointer, inventory;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0 : Main Menu, 1 : the second screen
    //Player Inventory
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    //Merchant NPC Inventory
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;

    int subState = 0;
    int counter = 0; // transition
    public Entity npc;
    int charIndex = 0;
    String combinedText = "";


    //chest ui
    public Entity openedChest;
    public int chestSlotCol = 0;
    public int chestSlotRow = 0;
    public boolean cursorOnChest = true;

    //quiver
    public int quiverSlotRow;

    // ss
    public String notificationMessage = "";
    public int notificationCounter = 0;
    public final int notificationDuration = 120; //2 sec at 60fps



    public Rectangle[] menuBounds = new Rectangle[3]; // 3 menu options



    public UI(GamePanel gp) {

        this.gp = gp;

        try{
            InputStream is = getClass().getResourceAsStream("/font/GhostTheory.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/GhostTheory.ttf");

        }
        catch (FontFormatException | IOException e) {
            System.out.println("something wrong with font thing");

        }

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_HungerBar(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        Entity bronzeCoin = new OBJ_Dinar(gp);
        coin = bronzeCoin.down1;


    }
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        String text = "II";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text,x,y);

    }
    public void drawDialogueScreen() {
        // WINDOW
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;

        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null)
        {
            //currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];//For display text once, enable this and disable letter by letter.(Letter by letter: The if statement below there)

            char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

            if(charIndex < characters.length)
            {
                gp.playSE(17);//Speak sound
                String s = String.valueOf(characters[charIndex]);
                combinedText = combinedText + s; //every loop add one character to combinedText
                currentDialogue = combinedText;

                charIndex++;
            }
            if(gp.keyH.enterPressed )
            {
                charIndex = 0;
                combinedText = "";
                if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState)
                {
                    npc.dialogueIndex++;
                    gp.keyH.enterPressed = false;
                }
            }
        }
        else //If no text is in the array
        {
            npc.dialogueIndex = 0;
            if(gp.gameState == gp.dialogueState)
            {
                gp.gameState = gp.playState;
            }
            if(gp.gameState == gp.cutsceneState)
            {
                gp.csManager.scenePhase++;
            }
        }


        for(String line : currentDialogue.split("\n"))   // splits dialogue until "\n" as a line
        {
            g2.drawString(line,x,y);
            y += 40;
        }

    }
    public void  drawCharacterScreen() {


        //LOADING IMAGE
        BufferedImage subWindow;
        try {
            subWindow = ImageIO.read(getClass().getResourceAsStream("/UI/SubWindowVer.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // CREATE A FRAME
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize *10;
//        drawSubWindow(frameX,frameY,frameWidth,frameHeight);
        g2.drawImage(subWindow, frameX - 15,frameY, frameWidth + 30,frameHeight, null);

        // TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        // NAMES
        g2.drawString("Level", textX,textY);
        textY += lineHeight;
        g2.drawString("Life", textX,textY);
        textY += lineHeight;
        g2.drawString("Mana", textX,textY);
        textY += lineHeight;
        g2.drawString("Strength", textX,textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX,textY);
        textY += lineHeight;
        g2.drawString("Attack", textX,textY);
        textY += lineHeight;
        g2.drawString("Defence", textX,textY);
        textY += lineHeight;
        g2.drawString("Exp", textX,textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX,textY);
        textY += lineHeight;
        g2.drawString("Coin", textX,textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX,textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX,textY);


        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        // Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.currentHunger + "/" + gp.player.maxHunger);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize + 5, textY - 24, null);
        textY += gp.tileSize;
        if(gp.player.currentShield!= null)
        {
            g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize + 5, textY - 24, null);
        }
    }
    public void drawInventory(Entity entity, boolean cursor) {


        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if (entity == gp.player) {
            frameX = gp.tileSize * 12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else if (entity == openedChest) {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = chestSlotCol;
            slotRow = chestSlotRow;
        } else if(entity == npc) {

            frameX = gp.tileSize*2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        } else{
            frameX = 0;
            frameY = 0;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = 0;
            slotRow = 0;
        }

        BufferedImage inventory;
        BufferedImage subWindow;

        try {
            inventory = ImageIO.read(getClass().getResourceAsStream("/UI/inventoryM.png"));
            subWindow = ImageIO.read(getClass().getResourceAsStream("/UI/SubWindowDes.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //DRAW FRAME
        g2.drawImage(inventory,frameX -gp.tileSize,frameY + + 32,   frameWidth + gp.tileSize * 2,(frameHeight + gp.tileSize *2) -20, null);
//        drawSubWindow(frameX,frameY,frameWidth,frameHeight);


        //SLOT
        int slotXstart = frameX + 20;
        int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 6;


        //DRAW PLAYER'S ITEMS
        for(int i = 0; i < entity.inventory.size(); i++)
        {

            //EQUIP CURSOR
            if(entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield || entity.inventory.get(i) == entity.currentLight)
            {
//                g2.setColor(new Color(63, 36, 16));
//                g2.fillRoundRect(slotX +10,slotY + gp.tileSize, gp.tileSize, gp.tileSize,10,10 );
            }

            //DROP ITEM
            if(gp.keyH.dropKeyPressed ) {
                gp.ui.dropItem(gp.player);
                gp.keyH.dropKeyPressed = false;
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX,slotY + gp.tileSize + 32, gp.tileSize, gp.tileSize, null);  //draw item

            //DISPLAY AMOUNT
            if((entity == gp.player || entity == openedChest) && entity.inventory.get(i).amount > 1) {

                //merchant npc's inventory cannot stack items
                g2.setFont(g2.getFont().deriveFont(32f));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXforAlignToRight(s, slotX + gp.tileSize);
                amountY = slotY + (gp.tileSize*2 + 30 );

                //SHADOW
                g2.setColor(new Color(60,60,60));
                g2.drawString(s,amountX,amountY);
                //NUMBER
                g2.setColor(Color.white);
                g2.drawString(s,amountX-3,amountY-1);

            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14)
            {
                //reset slotX
                slotX = slotXstart;
                //next row
                slotY += slotSize;
            }
        }

        //CURSOR
        if(cursor) {

             slotXstart = frameX + 20;
             slotYstart = frameY + 20;
             slotSize = gp.tileSize + 6;

            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow) + gp.tileSize + 32;
            int cursorWidth = gp.tileSize + 5;
            int cursorHeight = gp.tileSize + 5;

            //DRAW CURSOR
            g2.setColor(new Color(63, 36, 16));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

            //DESCRIPTION FRAME
            int dFrameX = frameX;
            int dFrameY = (frameY + frameHeight) + gp.tileSize + 62;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;

            //DRAW DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if(itemIndex < entity.inventory.size())
            {
//                drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
                g2.drawImage(subWindow, dFrameX - 13, dFrameY, dFrameWidth + 13, dFrameHeight, null);

                for(String line : entity.inventory.get(itemIndex).description.split("\n"))
                {
                    g2.drawString(line,textX,textY);
                    textY += 32;
                }
            }


        }


    }

    public void drawQuiver() {

        Entity quiver = gp.player.equippedQuiver;
        if(quiver == null) {
            gp.gameState = gp.playState;
            return;
        }

        int frameX = gp.tileSize * 12;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 2;
        int frameHeight = gp.tileSize * 5;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        int slotX = frameX + 20;
        int slotY = frameY + 20;
        int slotSize = gp.tileSize + 6;

        // Draw arrows
        for(int i = 0; i < quiver.inventory.size(); i++) {

            Entity arrow = quiver.inventory.get(i);

            g2.drawImage(
                    arrow.down1,
                    slotX,
                    slotY + (i * slotSize),
                    gp.tileSize,
                    gp.tileSize,
                    null
            );

            if(arrow.amount > 1) {
                g2.setFont(g2.getFont().deriveFont(28f));
                g2.setColor(Color.white);
                g2.drawString(
                        String.valueOf(arrow.amount),
                        slotX + gp.tileSize - 10,
                        slotY + (i * slotSize) + gp.tileSize - 5
                );
            }
        }

        // ---- FIX: clamp FIRST ----

            if(quiverSlotRow < 0) quiverSlotRow = 0;
            if(quiverSlotRow > 4) quiverSlotRow = 4;

        //DROP ITEM
        if(gp.keyH.dropKeyPressed ) {
            gp.ui.dropArrowFromQuiver();
            gp.keyH.dropKeyPressed = false;
        }


        // Cursor (now safe)
        int cursorY = slotY + (quiverSlotRow * slotSize);

        g2.setColor(new Color(63,36,16));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(
                slotX,
                cursorY,
                gp.tileSize + 5,
                gp.tileSize + 5,
                10,
                10
        );
    }


    public void dropItem(Entity entity) {

        if(entity.inventory == null || entity.inventory.isEmpty()) return;
        if(playerSlotCol < 0 || playerSlotRow < 0) return;

        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < 0 || itemIndex >= entity.inventory.size()) return;

        Entity item = entity.inventory.get(itemIndex);

        // Do not drop equipped gear
        if(item == entity.currentWeapon ||
                item == entity.currentShield ||
                item == entity.currentLight) {
            return;
        }

        int worldX = (int) gp.player.worldX;
        int worldY = (int) gp.player.worldY;

        switch(gp.player.direction) {
            case "up":    worldY -= gp.tileSize; break;
            case "down":  worldY += gp.tileSize; break;
            case "left":  worldX -= gp.tileSize; break;
            case "right": worldX += gp.tileSize; break;
        }

        Entity droppedItem;

        // 🔴 CONTAINER ITEMS (QUIVER, BAG, ETC.)
        if(item.type == gp.player.type_container) {

            droppedItem = item;                // SAME INSTANCE
            entity.inventory.remove(itemIndex);

            // If player was using this quiver, unequip it
            if(item == gp.player.equippedQuiver) {
                gp.player.equippedQuiver = null;
            }

        }
        // 🟢 NORMAL ITEMS
        else {
            droppedItem = gp.eGenerator.getObject(item.name);

            if(item.amount > 1) {
                item.amount--;
            } else {
                entity.inventory.remove(itemIndex);
            }
        }

        droppedItem.worldX = worldX;
        droppedItem.worldY = worldY;

        for(int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if(gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.renderListDirty = true;
                break;
            }
        }
    }

    public void dropArrowFromQuiver() {

        if(gp.gameState != gp.quiverState) return;

        if(gp.player.equippedQuiver == null ||
                gp.player.equippedQuiver.inventory == null) return;

        int index = quiverSlotRow;

        if(index < 0 || index >= gp.player.equippedQuiver.inventory.size()) {
            return;
        }

        Entity arrow = gp.player.equippedQuiver.inventory.get(index);

        Entity droppedArrow = gp.eGenerator.getObject(arrow.name);
        droppedArrow.amount = 1;
        droppedArrow.type = 14;
        droppedArrow.stackable = true;

        int worldX = (int) gp.player.worldX;
        int worldY = (int) gp.player.worldY;

        switch(gp.player.direction) {
            case "up":    worldY -= gp.tileSize; break;
            case "down":  worldY += gp.tileSize; break;
            case "left":  worldX -= gp.tileSize; break;
            case "right": worldX += gp.tileSize; break;
        }

        droppedArrow.worldX = worldX;
        droppedArrow.worldY = worldY;

        for(int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if(gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedArrow;
                break;
            }
        }

        arrow.amount--;

        if(arrow.amount <= 0) {
            gp.player.equippedQuiver.inventory.remove(index);
        }
    }



    public void drawTransition() {
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenWidth2,gp.screenHeight2); // screen gets darker

        if(counter == 50) //the transition is done
        {
            counter = 0;
            gp.gameState = gp.playState;
            gp.player.worldX =  gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.currentMap = gp.eHandler.tempMap;
            gp.eHandler.previousEventX = (int) gp.player.worldX;
            gp.eHandler.previousEventY = (int) gp.player.worldY;
            gp.changeArea();
        }
    }
    public void drawChestScreen() {

        // LEFT: CHEST
        drawInventory(openedChest, cursorOnChest);

        // RIGHT: PLAYER
        drawInventory(gp.player, !cursorOnChest);


        BufferedImage subWindow;
        try {
            subWindow = ImageIO.read(getClass().getResourceAsStream("/UI/SubWindowDes.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        // HINT
//        int x = gp.tileSize * 6;
//        int y = gp.tileSize * 10;
//        int width = gp.tileSize * 8;
//        int height = gp.tileSize * 2;
//        g2.drawImage(subWindow,x, y, width, height, null);
//        g2.setColor(new Color(1,1,1));
//        g2.drawString("[ENTER] Move Item   [E] Close", x + gp.tileSize, y + (gp.tileSize + 20));
    }
    public void transferItem() {

        Entity from;
        Entity to;
        int index;

        if (cursorOnChest) {
            from = openedChest;
            to = gp.player;
            index = getItemIndexOnSlot(chestSlotCol, chestSlotRow);
        } else {
            from = gp.player;
            to = openedChest;
            index = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        }

        if (index >= from.inventory.size()) return;

        Entity item = from.inventory.get(index);
        boolean transferred = false;

        // Determine how many items to transfer
        int amountToTransfer = gp.keyH.shiftPressed ? item.amount : 1;

        // Prevent putting equipped items into chest
        if (to != gp.player && (item == gp.player.currentWeapon || item == gp.player.currentShield)) {
            addMessage("You cannot store equipped items!");
            return;
        }

        // Prepare a new Entity to transfer with the amount
        Entity transferItem = item.copy(); // Make a copy so we can set a custom amount
        transferItem.amount = amountToTransfer;

        int transferAmount = gp.keyH.shiftPressed ? item.amount : 1;

        if (to == gp.player) {
            // Try to add to player inventory (player.canObtainItem should handle stacking and capacity)
            transferred = gp.player.canObtainItem(item, transferAmount);

            if (transferred) {
                // Reduce amount from chest
                item.amount -= amountToTransfer;

                // Remove item from chest if amount is 0 or less
                if (item.amount <= 0) {
                    from.inventory.remove(index);
                }

                gp.playSE(11);
            }
        } else {
            // Transfer to chest inventory

            if (transferItem.stackable) {
                int chestIndex = to.searchItemInInventory(transferItem.name);
                if (chestIndex != 999) {
                    // Increase existing stack
                    to.inventory.get(chestIndex).amount += amountToTransfer;
                    transferred = true;
                } else {
                    // Add new stack if space available
                    if (to.inventory.size() < to.maxInventorySize) {
                        to.inventory.add(transferItem);
                        transferred = true;
                    } else {
                        addMessage("Chest inventory is full!");
                        transferred = false;
                    }
                }
            } else {
                // Non-stackable items just add if space
                if (to.inventory.size() < to.maxInventorySize) {
                    to.inventory.add(transferItem);
                    transferred = true;
                } else {
                    addMessage("Chest inventory is full!");
                    transferred = false;
                }
            }

            if (transferred) {
                // Reduce amount from player inventory
                item.amount -= amountToTransfer;

                // Remove if no more left
                if (item.amount <= 0) {
                    from.inventory.remove(index);
                }

                gp.playSE(11);
            }
        }
    }



    public void drawTradeScreen() {
        switch(subState)
        {
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false;
    }
    public void trade_select() {
        npc.dialogueSet = 0;
        drawDialogueScreen();

        //DRAW WINDOW
        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize *3;
        int height = (int)(gp.tileSize*3.5);
        drawSubWindow(x,y,width,height);

        //DRAW TEXTS
        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Buy",x,y);
        if(commandNum == 0)
        {
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed)
            {
                subState = 1;
            }
        }
        y += gp.tileSize;
        g2.drawString("Sell",x,y);
        if(commandNum == 1)
        {
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed)
            {
                subState = 2;
            }
        }
        y += gp.tileSize;
        g2.drawString("Leave",x,y);
        if(commandNum == 2)
        {
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed)
            {
                //leave trade
                commandNum = 0;
                npc.startDialogue(npc,1);
            }
        }
    }
    public void trade_buy() {


        // DRAW PLAYER INVENTORY
        drawInventory(gp.player, false); // I want to move cursor on merchant's inventory so cursor = false.
        // DRAW PLAYER INVENTORY
        drawInventory(npc, true);

        // DRAW HINT WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x,y,width,height);
        g2.drawString("[ESC] Back", x+24,y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your Coin: " + gp.player.coin, x+24,y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol,npcSlotRow);
        if(itemIndex < npc.inventory.size())
        {
            x = (int)(gp.tileSize * 5.5);
            y = (int)(gp.tileSize * 5.5);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8, 32,32,null );

            int price = npc.inventory.get(itemIndex).price;
            String text = String.valueOf(price);
            x = getXforAlignToRight(text,(gp.tileSize * 8) - 20);
            g2.drawString(text, x, y+34);

            //BUY AN ITEM
            if(gp.keyH.enterPressed)
            {
                if(npc.inventory.get(itemIndex).price > gp.player.coin) //not enough coin
                {
                    subState = 0;
                    npc.startDialogue(npc,2);
                }
                else
                {
                    if(gp.player.canObtainItem(npc.inventory.get(itemIndex)))
                    {
                        gp.player.coin -= npc.inventory.get(itemIndex).price;  //-price
                    }
                    else
                    {
                        subState = 0;
                        npc.startDialogue(npc,3);
                    }
                }
            }
        }
    }
    public void trade_sell() {
        //DRAW PLAYER INVENTORY
        drawInventory(gp.player, true);
        int x;
        int y;
        int width;
        int height;

        // DRAW HINT WINDOW
        x = gp.tileSize * 2;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x,y,width,height);
        g2.drawString("[ESC] Back", x+24,y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your Coin: " + gp.player.coin, x+24,y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol,playerSlotRow);
        if(itemIndex < gp.player.inventory.size())
        {
            x = (int)(gp.tileSize * 15.5);
            y = (int)(gp.tileSize * 5.5);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8, 32,32,null );

            int price = gp.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);
            x = getXforAlignToRight(text,(gp.tileSize * 18) - 20);
            g2.drawString(text, x, y+34);

            //SELL AN ITEM
            if(gp.keyH.enterPressed)
            {
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield) //equipped items cant sell
                {
                    commandNum = 0;
                    subState = 0;
                    npc.startDialogue(npc,4);
                }
                else
                {
                    if(gp.player.inventory.get(itemIndex).amount > 1)
                    {
                        gp.player.inventory.get(itemIndex).amount--;
                    }
                    else
                    {
                        gp.player.inventory.remove(itemIndex);
                    }
                    gp.player.coin += price;
                }
            }
        }

    }
    public void drawSleepScreen() {

        counter++;
        if(counter < 120) {

            gp.eManager.lighting.filterAlpha += 0.01f;

            if(gp.eManager.lighting.filterAlpha > 1f) {
                gp.eManager.lighting.filterAlpha = 1f;
            }
        }
        //if day then night
        if(counter >= 120 && gp.eManager.lighting.dayState == 2) {

            gp.eManager.lighting.filterAlpha -= 0.01f;
            if(gp.eManager.lighting.filterAlpha <= 0f) {

                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;

                gp.eManager.lighting.toggleDayState();

                gp.gameState = gp.playState;
                gp.player.getIdleImage();
            }
        }
        if(counter >= 120 && gp.eManager.lighting.dayState == 0) {


            if(gp.eManager.lighting.filterAlpha == 1f) {

                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;

                gp.eManager.lighting.toggleDayState();

                gp.gameState = gp.playState;
                gp.player.getIdleImage();
            }
        }
        if(counter >= 120 && gp.eManager.lighting.dayState == 1) {


            if(gp.eManager.lighting.filterAlpha == 1f) {

                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;

                gp.eManager.lighting.toggleDayState();

                gp.gameState = gp.playState;
                gp.player.getIdleImage();
            }
        }
        if(counter >= 120 && gp.eManager.lighting.dayState == 3) {


            if(gp.eManager.lighting.filterAlpha == 1f) {

                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;

                gp.eManager.lighting.toggleDayState();

                gp.gameState = gp.playState;
                gp.player.getIdleImage();
            }
        }
    }
    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }
    public void drawPlayerLife() {
        //gp.player.life = 5;
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        int iconSize = 32;
        int manaStartX = (gp.tileSize/2);
        int manaStartY = 0;

        //DRAW MAX LIFE (BLANK)
        while(i < gp.player.maxLife/2)
        {
            g2.drawImage(heart_blank, x, y, iconSize, iconSize, null);
            i++;
            x += iconSize;
            manaStartY = y + 32;

            if(i % 8 == 0)
            {
                x = gp.tileSize / 2;
                y += iconSize;
            }
        }
        //reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;
        //DRAW CURRENT HEART // ITS LIKE COLORING THE BLANK HEARTS
        while(i < gp.player.life)
        {
            g2.drawImage(heart_half,x,y,iconSize, iconSize, null);
            i++;
            if(i < gp.player.life)
            {
                g2.drawImage(heart_full,x,y,iconSize, iconSize, null);
            }
            i++;
            x += iconSize;

            if(i % 16 == 0)
            {
                x = gp.tileSize / 2;
                y += iconSize;
            }
        }

        //DRAW MAX MANA (BLANK)
        x = manaStartX;
        y = manaStartY;
        i = 0;
        while(i < gp.player.maxHunger)
        {
            g2.drawImage(crystal_blank,x ,y, iconSize, iconSize, null);
            i++;
            x += 25;

            if(i % 10 == 0)
            {
                x = manaStartX;
                y += iconSize;
            }
        }
        //reset
        x = manaStartX;
        y = manaStartY;
        i = 0;
        //DRAW MANA
        while(i < gp.player.currentHunger)
        {
            g2.drawImage(crystal_full,x,y,iconSize,iconSize,null);
            i++;
            x += 25;
            if(i % 10 == 0)
            {
                x = manaStartX;
                y += iconSize;
            }
        }
    }
    public void drawMonsterLife() {
        //Monster HP Bar
        for(int i = 0; i < gp.monster[1].length; i++)
        {
            Entity monster = gp.monster[gp.currentMap][i];

            if(monster != null && monster.inCamera())
            {
                if(monster.hpBarOn && !monster.boss)
                {
                    double oneScale = (double)gp.tileSize/monster.maxLife; // (bar length / maxlife) Ex: if monster hp = 2, tilesize = 48px. So, 1 hp = 24px
                    double hpBarValue = oneScale * monster.life;

                    if(hpBarValue < 0)//Ex: You attack 5 hp to monster which has 3 hp. Monster's hp will be -2 and bar will offset to left. To avoid that check if hpBarValue less than 0.
                    {
                        hpBarValue = 0;
                    }

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(monster.getScreenX()-1,monster.getScreenY()-16,gp.tileSize+2,12);

                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(monster.getScreenX(),monster.getScreenY() - 15, (int)hpBarValue,10);

                    monster.hpBarCounter++;
                    if(monster.hpBarCounter > 600)  // 10
                    {
                        monster.hpBarCounter = 0;
                        monster.hpBarOn = false;
                    }
                }
                else if(monster.boss)
                {
                    double oneScale = (double)gp.tileSize*8/monster.maxLife; // (bar lenght / maxlife) Ex: if monster hp = 2, tilesize = 48px. So, 1 hp = 24px
                    double hpBarValue = oneScale * monster.life;
                    int x = gp.screenWidth/2 - gp.tileSize*4;
                    int y = gp.tileSize * 10;

                    if(hpBarValue < 0)  //Ex: You attack 5 hp to monster which has 3 hp. Monster's hp will be -2 and bar will ofset to left. To avoid that check if hpBarValue less than 0.
                    {
                        hpBarValue = 0;
                    }

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(x-1,y-1,gp.tileSize*8 + 2,22);

                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(x,y, (int)hpBarValue,20);

                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
                    g2.setColor(Color.white);
                    g2.drawString(monster.name, x+4, y-10);
                }
            }
        }

    }
    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,24F));

        for(int i = 0; i < message.size(); i++)
        {
            if(message.get(i) != null)
            {
                //Shadow
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2,messageY+2);
                //Text
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX,messageY);

                int counter = messageCounter.get(i) + 1; //messageCounter++
                messageCounter.set(i,counter);           //set the counter to the array
                messageY += 50;

                if(messageCounter.get(i) > 150)          //display 2.5 seconds
                {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }

        }
    }

    public void drawTitleScreen() {

        // FILL BACKGROUND BLACK
        g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);



            //ICON
            ImageIcon icon = new ImageIcon("ImageContent/sword2.gif");
            g2.drawImage(icon.getImage(), 800, 30 + 20,32, 128, null);

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80));
            String text = "TALE OF JAFAR\n";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            //SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text,x+5,y+5);

            //MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);


            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26));

            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize*4;
            g2.drawString(text, x, y)
            ;
            //I can use Draw Image instead of DrawString to make it look better later
            if(commandNum == 0){
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y+5);
            if(commandNum == 1){
                g2.drawString(">", x - gp.tileSize, y + 3);
            }


            text = "QUIT";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y+10);
            if(commandNum == 2){
                g2.drawString(">", x - gp.tileSize, y + 10);
            }


    }
    public void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150)); //Half-black
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
        text = "Game Over";

        //Shadow
        g2.setColor(Color.BLACK);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text,x,y);
        //Text
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        //RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text,x,y);
        if(commandNum == 0)
        {
            g2.drawString(">", x-40, y);
        }

        //BACK TO THE TITLE SCREEN
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text,x,y);
        if(commandNum == 1)
        {
            g2.drawString(">", x-40, y);
        }

    }
    public void drawOptionsScreen() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;

//        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //LOADING UI IMAGES
        try {
             SettingImage = ImageIO.read(getClass().getResourceAsStream("/UI/settings1.png"));
            rPointer = ImageIO.read(getClass().getResourceAsStream("/UI/rightPointer.gif"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(SettingImage,frameX, 50, frameWidth, frameHeight , null);

        switch(subState)
        {
            case 0: options_top(frameX,frameY); break;
            case 1: options_fullScreenNotification(frameX,frameY); break;
            case 2: options_control(frameX,frameY); break;
            case 3: options_endGameConfirmation(frameX,frameY);

        }
        gp.keyH.enterPressed = false;
    }
    public void options_top(int frameX, int frameY) {
        int textX;
        int textY;

        //TITLE
        g2.setColor(new Color(59,48,60));
        String text = "Settings";
        textX = getXforCenteredText(text);
        textY = frameY + 70;
        g2.drawString(text,textX,textY);

        //FULL SCREEN ON/OFF
        textX = frameX + gp.tileSize + 55;
        textY += gp.tileSize + 20;
        g2.drawString("Fullscreen", textX, textY);
        if(commandNum == 0)
        {
            g2.drawImage(rPointer, textX-46 ,textY -30 , 40, 40, null);
            if(gp.keyH.enterPressed) {
                if(!gp.fullScreenOn) {
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        //MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1)
        {
            g2.drawImage(rPointer, textX-46,textY -30, 40, 40, null);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("Sound", textX, textY);
        if(commandNum == 2)
        {
            g2.drawImage(rPointer, textX-46,textY -30, 40, 40, null);
        }

        //CONTROLS
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if(commandNum == 3)
        {
            g2.drawImage(rPointer, textX-46,textY -30, 40, 40, null);
            if(gp.keyH.enterPressed)
            {
                subState = 2;
                commandNum = 0;
            }
        }

        //Quit
        textY += gp.tileSize;
        g2.drawString("Quit", textX, textY);
        if(commandNum == 4)
        {
            g2.drawImage(rPointer, textX-46,textY -30, 40, 40, null);
            if(gp.keyH.enterPressed)
            {
                subState = 3;
                commandNum = 0;
            }
        }

        //BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5)
        {
            g2.drawImage(rPointer, textX-46,textY -30, 40, 40, null);
            if(gp.keyH.enterPressed)
            {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        //FULL SCREEN CHECK BOX
        textX = frameX + (int)(gp.tileSize * 5);
        textY = frameY + gp.tileSize * 2 + 4;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX,textY,24,24);
        if(gp.fullScreenOn)
        {
            g2.fillRect(textX,textY,24,24);
        }

        //MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX,textY,120, 24); //120/5 = 24px = 1 scale
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,24);

        //SE VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX,textY,120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,24);

        //SAVE OPTIONS
        gp.config.saveConfig();
    }
    public void options_fullScreenNotification(int frameX, int frameY) {

        int textX = frameX + gp.tileSize * 2;
        int textY = frameY + gp.tileSize * 3;

        g2.setColor(new Color(59,48,60));
        currentDialogue = "The change will take \neffect after restarting \nthe game.";
        for(String line: currentDialogue.split("\n"))
        {
            g2.drawString(line,textX,textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gp.tileSize * 7;
        g2.drawString("Back", textX + 20,textY);
        if(commandNum == 0)
        {

            g2.drawImage(rPointer, textX-20 ,textY -30 , 40, 40, null);
            if(gp.keyH.enterPressed)
            {
                subState = 0;
            }
        }
    }
    public void options_control(int frameX,int frameY) {
        int textX;
        int textY;
        g2.setColor(new Color(59,48,60));

        //TITLE

        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text,textX,textY);

        textX = frameX +gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX,textY); textY += gp.tileSize;
        g2.drawString("Confirm/Attack", textX,textY); textY += gp.tileSize;
        g2.drawString("Shoot/Cast", textX,textY); textY += gp.tileSize;
        g2.drawString("Inventory", textX,textY); textY += gp.tileSize;
        g2.drawString("Pause", textX,textY); textY += gp.tileSize;
        g2.drawString("Options", textX,textY); textY += gp.tileSize;

        //KEYS
        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX,textY); textY += gp.tileSize;
        g2.drawString("ENTER", textX,textY); textY += gp.tileSize;
        g2.drawString("F", textX,textY); textY += gp.tileSize;
        g2.drawString("C", textX,textY); textY += gp.tileSize;
        g2.drawString("P", textX,textY); textY += gp.tileSize;
        g2.drawString("ESC", textX,textY); textY += gp.tileSize;


        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX,textY);
        if(commandNum == 0)
        {
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed)
            {
                subState = 0;
                commandNum = 3; //back to control row
            }
        }
    }
    public void options_endGameConfirmation(int frameX, int frameY) {

        g2.setColor(new Color(59,48,60));

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the game and \nreturn to the title screen?";
        for(String line: currentDialogue.split("\n"))
        {
            g2.drawString(line,textX,textY);
            textY += 40;
        }
        //YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text,textX,textY);
        if(commandNum == 0)
        {
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed)
            {
                subState = 0;
                gp.ui.titleScreenState = 0;
                gp.gameState = gp.titleState;
                gp.resetGame(true);
                gp.stopMusic();
            }
        }

        //NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text,textX,textY);
        if(commandNum == 1)
        {
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed)
            {
                subState = 0;
                commandNum = 4; //back to end row
            }
        }
    }
    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0,0,0,210);  // R,G,B, alfa(opacity)
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));    // 5 = width of outlines of graphics
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);

    }
    public int getXforCenteredText(String text) {
        int textLenght;
        textLenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); // Gets width of text.
        int x = gp.screenWidth / 2 - textLenght/2;
        return x;
    }
    public int getXforAlignToRight(String text, int tailX) {
        int textLenght;
        textLenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); // Gets width of text.
        int x = tailX - textLenght;
        return x;
    }
    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    public void showNotification(String message) {
        notificationMessage = message;
        notificationCounter = notificationDuration;
    }
    public void drawNotification(Graphics2D g2) {
        if(notificationCounter > 0 && !notificationMessage.isEmpty()) {
            // Set your custom font and size (deriveFont to set size)
            g2.setFont(maruMonica.deriveFont(Font.BOLD, 20f));
            g2.setColor(Color.WHITE);
            int x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(notificationMessage) / 2;
            int y = gp.screenHeight / 5;  // near top




            g2.setColor(Color.WHITE);
            g2.drawString(notificationMessage, x, y);
        }
    }


    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // Anti Aliasing // Smoothes the text
        g2.setColor(Color.white);
        drawNotification(g2);

        //TITLE STATE
        if(gp.gameState == gp.titleState)
        {
            drawTitleScreen();
        }
        //OTHERS
        else
        {
            //PLAY STATE
            if(gp.gameState == gp.playState)
            {
                drawPlayerLife();
                drawMonsterLife();
                drawMessage();
            }
            //PAUSE STATE
            if(gp.gameState == gp.pauseState)
            {
                drawPlayerLife();
                drawPauseScreen();
            }
            //DIALOGUE STATE
            if(gp.gameState == gp.dialogueState)
            {
                drawDialogueScreen();
            }
            //CHARACTER STATE
            if(gp.gameState == gp.characterState)
            {
                drawCharacterScreen();
                drawInventory(gp.player, true);
            }
            //OPTIONS STATE
            if(gp.gameState == gp.optionsState)
            {
                drawOptionsScreen();
            }
            //GAME OVER STATE
            if(gp.gameState == gp.gameOverState)
            {
                drawGameOverScreen();
            }
            //TRANSITION STATE
            if(gp.gameState == gp.transitionState)
            {
                drawTransition();
            }
            //TRADE STATE
            if(gp.gameState == gp.tradeState)
            {
                drawTradeScreen();
            }
            //SLEEP STATE
            if(gp.gameState == gp.sleepState)
            {
                drawSleepScreen();
            }
            if(gp.gameState == gp.chestState){

                drawChestScreen();
            }
            if(gp.gameState == gp.quiverState){

                drawQuiver();
            }
        }
    }
}
