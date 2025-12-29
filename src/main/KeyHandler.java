package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed,rightPressed,enterPressed,
            shotKeyPressed, spacePressed, dropKeyPressed, ePressed, shiftPressed,
              quiverKeyPressed, f2Pressed;

    //DEBUG
    public boolean showDebugText = false;
    public boolean showCollisionBox = false;
    public boolean cheat = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }
    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_F2) {f2Pressed = true;}

        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            titleState(code);
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState)
        {
            playState(code);
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState)
        {
            pauseState(code);
        }
        //DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState)
        {
            dialogueState(code);
        }
        // CHARACTER STATE
        else if(gp.gameState == gp.characterState)
        {
            characterState(code);
        }
        // OPTIONS STATE
        else if(gp.gameState == gp.optionsState)
        {
            optionsState(code);
        }
        // GAMEOVER STATE
        else if(gp.gameState == gp.gameOverState)
        {
            gameOverState(code);
        }
        // TRADE STATE
        else if(gp.gameState == gp.tradeState)
        {
            tradeState(code);
        }
        // MAP STATE
        else if(gp.gameState == gp.mapState)
        {
            mapState(code);
        }
        else if (gp.gameState == gp.chestState) {
            chestState(code);
            //System.out.println("Cursor on chest? " + gp.ui.cursorOnChest + " chestSlot: " + gp.ui.chestSlotRow + "," + gp.ui.chestSlotCol + " playerSlot: " + gp.ui.playerSlotRow + "," + gp.ui.playerSlotCol);
        }
        else if (gp.gameState == gp.quiverState) {
            quiverState(code);
        }

    }

    public void titleState(int code) {

        //MAIN MENU
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum --;
            gp.playSE(1);
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 2;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.playSE(1);
            gp.ui.commandNum ++;
            if(gp.ui.commandNum > 2){
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            switch (gp.ui.commandNum){
                case 0:
                    gp.gameState = gp.playState; //gp.playMusic
                    gp.playMusic(0);
                    break;
                case 1:
                    gp.saveLoad.save(); // to save progress
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                    break;
                case 2:
                    System.exit(0);
                    break;
            }
        }
    }
    public void playState(int code) {

        if(code == KeyEvent.VK_W) {upPressed = true;}
        if(code == KeyEvent.VK_S) {downPressed = true;}
        if(code == KeyEvent.VK_A) {leftPressed = true;}
        if(code == KeyEvent.VK_D) {rightPressed = true;}
        if(code == KeyEvent.VK_P) {gp.gameState = gp.pauseState;}
        if(code == KeyEvent.VK_C) {gp.playSE(26);gp.gameState = gp.characterState;}
        if(code == KeyEvent.VK_ENTER) {enterPressed = true;}
        if(code == KeyEvent.VK_F) {shotKeyPressed = true;}
        if(code == KeyEvent.VK_ESCAPE) {gp.gameState = gp.optionsState;}
        if(code == KeyEvent.VK_M) {gp.gameState = gp.mapState;}
        if(code == KeyEvent.VK_X) {
            if(!gp.map.miniMapOn) {
                gp.map.miniMapOn = true;
            } else {
                gp.map.miniMapOn = false;
            }
        }
        if(code == KeyEvent.VK_SPACE) {spacePressed = true;}
        if(code == KeyEvent.VK_Z) {quiverKeyPressed = true;}


        //DEBUG
        if(code == KeyEvent.VK_F3) {

            if(!showCollisionBox) {showCollisionBox = true;}
            else {showCollisionBox = false;}

            if(!showDebugText) {showDebugText = true;}
            else {showDebugText = false;}
        }

        if(code == KeyEvent.VK_R)   //Refresh Map without restarting game // Save Map File : in IntellijIDE "Ctrl + F9", in Eclipce "Ctrl + S"
        {
            switch (gp.currentMap)
            {
                case 0: gp.tileM.loadMap("/maps/worldV3.txt",0); break;
                case 1: gp.tileM.loadMap("/maps/interior01.txt",1); break;
            }
        }
        if(code == KeyEvent.VK_L) {
            if(!cheat)
            {
                cheat = true;
            }
            else {
                cheat = false;
            }
        }
    }
    public void pauseState(int code)
    {
        if(code == KeyEvent.VK_ESCAPE)
        {
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code)
    {
        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = true;
        }
    }
    public void characterState(int code)
    {
        if(code == KeyEvent.VK_C) {

            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_Q){
            dropKeyPressed = true;
        }
        if(code == KeyEvent.VK_ENTER) {

            gp.player.selectItem();
        }
        playerInventory(code);
    }
    public void optionsState(int code)
    {
        if(code == KeyEvent.VK_ESCAPE)
        {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = true;
        }
        int maxCommandNum = 0;
        switch (gp.ui.subState)
        {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }
        if(code == KeyEvent.VK_W)
        {
            gp.ui.commandNum--;
            gp.playSE(9);
            if(gp.ui.commandNum < 0)
            {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S)
        {
            gp.ui.commandNum++;
            gp.playSE(9);
            if(gp.ui.commandNum > maxCommandNum)
            {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_A)
        {
            if(gp.ui.subState == 0)
            {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) //music
                {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();  //check for music maybe a song is already being played, but you dont need it for SE, when set a sound checkVolume will be execute.
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) //SE
                {
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D)
        {
            if(gp.ui.subState == 0)
            {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) //music
                {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) //SE
                {
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }
    public void gameOverState(int code)
    {
        if(code == KeyEvent.VK_W)
        {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0)
            {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_S)
        {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1)
            {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_ENTER)
        {
            if(gp.ui.commandNum == 0) //RETRY, reset position, life, mana, monsters, npcs...
            {
                gp.gameState = gp.playState;
                gp.resetGame(false);
                gp.playMusic(0);
            }
            else if(gp.ui.commandNum == 1) //QUIT, reset everything
            {
                gp.ui.titleScreenState = 0;
                gp.gameState = gp.titleState;
                gp.resetGame(true);
            }
        }
    }
    public void tradeState(int code)
    {
        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = true;
        }
        if(gp.ui.subState == 0)
        {
            if(code == KeyEvent.VK_W)
            {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0)
                {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            if(code == KeyEvent.VK_S)
            {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2)
                {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
        }
        if(gp.ui.subState == 1)
        {
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE)
            {
                gp.ui.subState = 0;
            }
        }
        if(gp.ui.subState == 2)
        {
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE)
            {
                gp.ui.subState = 0;
            }
        }
    }
    public void mapState(int code)
    {
       if(code == KeyEvent.VK_M)
       {
           gp.gameState = gp.playState;
       }
    }
    public void playerInventory(int code)
    {
        if(code == KeyEvent.VK_W)
        {
            if(gp.ui.playerSlotRow != 0)
            {
                gp.ui.playerSlotRow--;
                gp.playSE(9);   //cursor.wav
            }
        }
        if(code == KeyEvent.VK_A)
        {
            if(gp.ui.playerSlotCol !=0)
            {
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S)
        {
            if(gp.ui.playerSlotRow != 3)
            {
                gp.ui.playerSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D)
        {
            if(gp.ui.playerSlotCol != 4)
            {
                gp.ui.playerSlotCol++;
                gp.playSE(9);
            }
        }
    }
    public void quiverState(int code) {

        // Move cursor (1 column, 5 rows)
        if(code == KeyEvent.VK_W) {
            if(gp.ui.quiverSlotRow > 0) {
                gp.ui.quiverSlotRow--;
                gp.playSE(9);
            }
        }

        if(code == KeyEvent.VK_S) {
            if(gp.ui.quiverSlotRow < 4) {
                gp.ui.quiverSlotRow++;
                gp.playSE(9);
            }
        }

        // Drop arrow
        if(code == KeyEvent.VK_Q) {
            dropKeyPressed = true;
        }

        // Close quiver
        if(code == KeyEvent.VK_Z || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    public void chestState(int code) {

        // MOVE CURSOR
        if (code == KeyEvent.VK_W) moveChestCursor(-1, 0);
        if (code == KeyEvent.VK_S) moveChestCursor(1, 0);
        if (code == KeyEvent.VK_A) moveChestCursor(0, -1);
        if (code == KeyEvent.VK_D) moveChestCursor(0, 1);

        // TRANSFER
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.transferItem();
        }
        if(code == KeyEvent.VK_SHIFT){
            shiftPressed= true;
        }
        // CLOSE
        if (code == KeyEvent.VK_E) {
            ((object.OBJ_StorageChest) gp.ui.openedChest).closeChest();
        }
    }
    private void moveChestCursor(int rowDelta, int colDelta) {

        // Snapshot previous state (for sound)
        boolean prevCursorOnChest = gp.ui.cursorOnChest;
        int prevChestRow = gp.ui.chestSlotRow;
        int prevChestCol = gp.ui.chestSlotCol;
        int prevPlayerRow = gp.ui.playerSlotRow;
        int prevPlayerCol = gp.ui.playerSlotCol;

        int maxCol = 4;

        if (gp.ui.cursorOnChest) {

            int newCol = gp.ui.chestSlotCol + colDelta;

            // If trying to move past horizontal edge → switch inventory
            if (newCol < 0 || newCol > maxCol) {
                gp.ui.cursorOnChest = false;
                gp.ui.playerSlotCol = (newCol < 0) ? maxCol : 0;
                gp.ui.playerSlotRow = Math.min(gp.ui.chestSlotRow, 3);
            } else {
                gp.ui.chestSlotCol = newCol;
                gp.ui.chestSlotRow += rowDelta;
            }

        } else {

            int newCol = gp.ui.playerSlotCol + colDelta;

            // If trying to move past horizontal edge → switch inventory
            if (newCol < 0 || newCol > maxCol) {
                gp.ui.cursorOnChest = true;
                gp.ui.chestSlotCol = (newCol < 0) ? maxCol : 0;
                gp.ui.chestSlotRow = Math.min(gp.ui.playerSlotRow, 3);
            } else {
                gp.ui.playerSlotCol = newCol;
                gp.ui.playerSlotRow += rowDelta;
            }
        }

        // Clamp rows only (columns already handled)
        gp.ui.chestSlotRow = clamp(gp.ui.chestSlotRow, 0, 3);
        gp.ui.playerSlotRow = clamp(gp.ui.playerSlotRow, 0, 3);

        // Play sound only if movement actually occurred
        boolean moved =
                prevCursorOnChest != gp.ui.cursorOnChest ||
                        prevChestRow != gp.ui.chestSlotRow ||
                        prevChestCol != gp.ui.chestSlotCol ||
                        prevPlayerRow != gp.ui.playerSlotRow ||
                        prevPlayerCol != gp.ui.playerSlotCol;

        if (moved) {
            gp.playSE(9);
        }
    }





    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public void npcInventory(int code)
    {
        if(code == KeyEvent.VK_W)
        {
            if(gp.ui.npcSlotRow != 0)
            {
                gp.ui.npcSlotRow--;
                gp.playSE(9);   //cursor.wav
            }
        }
        if(code == KeyEvent.VK_A)
        {
            if(gp.ui.npcSlotCol !=0)
            {
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S)
        {
            if(gp.ui.npcSlotRow != 3)
            {
                gp.ui.npcSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D)
        {
            if(gp.ui.npcSlotCol != 4)
            {
                gp.ui.npcSlotCol++;
                gp.playSE(9);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W)
        {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S)
        {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A)
        {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_F)
        {
            shotKeyPressed = false;
        }
        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
        if(code == KeyEvent.VK_Q){
            dropKeyPressed = false;
        }
        if(code == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
        if(code == KeyEvent.VK_E){
            ePressed = false;
        }
        if(code == KeyEvent.VK_F2){
            f2Pressed = false;
        }
    }
}
