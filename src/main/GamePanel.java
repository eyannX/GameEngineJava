package main;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;
import tile_interactive.TallInteractiveObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 4;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;



    //WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;
    public boolean debug = false;

    //FOR FULLSCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;


    //game loop
    int FPS = 60;
    double drawInterval = (double) 1000000000 / FPS;
    public double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public EventHandler eHandler = new EventHandler(this);
    public Sound music = new Sound(); // Created 2 different objects for Sound Effect and Music. If you use 1 object SE or Music stops sometimes.
    public Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter  aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    EnvironmentManager eManager = new EnvironmentManager(this);
    Map map = new Map(this);
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator eGenerator = new EntityGenerator(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    public MouseHandler mouseH = new MouseHandler();
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public Entity[][] obj = new Entity[maxMap][50]; // display 10 objects same time
    public Entity[][] npc = new Entity[maxMap][50];
    public Entity[][] monster = new Entity[maxMap][50];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];
    public TallInteractiveObject[][] tallObj = new TallInteractiveObject[maxMap][50];
    public Entity[][] projectile = new Entity[maxMap][20]; // cut projectile
    //public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();


    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;
    public final int cutsceneState = 11;
    public final int chestState = 12;

    //OTHERS
    public boolean bossBattleOn = false;

    //AREA
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // JPanel size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // improve game's rendering performance
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.requestFocusInWindow();
        this.setFocusable(true);
    }
    public void setupGame() {

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        aSetter.setTallObject();
        eManager.setup();


        gameState = titleState;

        //FOR FULLSCREEN
        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB); //blank screen
        if(fullScreenOn) {setFullScreen();}

    }
    public void resetGame(boolean restart) {
        stopMusic();
        currentArea = outside;
        removeTempEntity();
        bossBattleOn = false;
        player.setDefaultPositions();
        player.restoreStatus();
        aSetter.setMonster();
        aSetter.setNPC();
        player.resetCounter();

        if(restart) {
            player.setDefaultValues();
            aSetter.setObject();
            aSetter.setInteractiveTile();
            aSetter.setTallObject();
            eManager.lighting.resetDay();
        }

    }
    public void setFullScreen() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // Make window undecorated BEFORE fullscreen
        Main.window.dispose();
        Main.window.setUndecorated(true);
        Main.window.setResizable(false);

        // Enter exclusive fullscreen mode
        gd.setFullScreenWindow(Main.window);

        // Apply the display mode if supported
        DisplayMode dm = gd.getDisplayMode();
        screenWidth2 = dm.getWidth();
        screenHeight2 = dm.getHeight();

        Main.window.createBufferStrategy(3);  // OR 3
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {


        while (gameThread != null) {
            currentTime = System.nanoTime();

            long elapsed = currentTime - lastTime;
            lastTime = currentTime;

            // Clamp elapsed time to prevent huge delta spikes
            if (elapsed > drawInterval * 5) {
                elapsed = (long) (drawInterval * 5);  // Max 5 frames worth of time per update
            }

            delta += (double) elapsed / drawInterval;

            if (delta >= 1) {
                update();
                /*repaint(); COMMENTED FOR FULL SCREEN*/
                drawToTempScreen(); //FOR FULL SCREEN - Draw everything to the buffered image
                drawToScreen();     //FOR FULL SCREEN - Draw the buffered image to the screen
                delta--;
                //drawCount++;
            }
        }
    }

    public void update() {

        if(gameState == playState) {

            //PLAYER
            player.update();

            //NPC
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER
            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop(); //when monster dies, I check its drop
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILE
            for(int i = 0; i < projectile[1].length; i++) {
                if(projectile[currentMap][i] != null) {
                    if(projectile[currentMap][i].alive) {
                        projectile[currentMap][i].update();
                    }
                    if(!projectile[currentMap][i].alive) {
                        projectile[currentMap][i] = null;
                    }
                }
            }

            //PARTICLE
            for(int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i)!= null) {
                    if(particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if(!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

            //INTERACTIVE TILE
            for(int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }

            eManager.update();
        }


    }

    //FOR FULL SCREEN (FIRST DRAW TO TEMP SCREEN INSTEAD OF J PANEL)
    public void drawToTempScreen() {

        Graphics2D g2 = tempScreen.createGraphics();

        long drawStart = 0;
        if (keyH.showDebugText) drawStart = System.nanoTime();

        // TITLE
        if (gameState == titleState) {
            ui.draw(g2);
            return;
        }

        // MAP
        if (gameState == mapState) {
            map.drawFullMapScreen(g2);
            return;
        }


        // WORLD RENDER


        // GROUND TILES
        tileM.draw(g2);


        // BUILD ENTITY LIST
        entityList.add(player);

        // NPCs
        for (int i = 0; i < npc[currentMap].length; i++) {
            if (npc[currentMap][i] != null) {
                entityList.add(npc[currentMap][i]);
            }
        }

        // OBJECTS
        for (int i = 0; i < obj[currentMap].length; i++) {
            if (obj[currentMap][i] != null) {
                entityList.add(obj[currentMap][i]);
            }
        }

        // MONSTERS
        for (int i = 0; i < monster[currentMap].length; i++) {
            if (monster[currentMap][i] != null) {
                entityList.add(monster[currentMap][i]);
            }
        }

        // PROJECTILES
        for (int i = 0; i < projectile[currentMap].length; i++) {
            if (projectile[currentMap][i] != null) {
                entityList.add(projectile[currentMap][i]);
            }
        }

        // PARTICLES
        for (int i = 0; i < particleList.size(); i++) {
            if (particleList.get(i) != null) {
                entityList.add(particleList.get(i));
            }
        }


//        for (int i = 0; i < iTile[currentMap].length; i++) {
//            if (iTile[currentMap][i] != null) {
//                entityList.add(iTile[currentMap][i]);
//            }
//        }
        //            //INTERACTIVE TILE
            for(int i = 0; i < iTile[1].length; i++) {

                if(iTile[currentMap][i] != null) {iTile[currentMap][i].draw(g2);}
            }

        // TALL OBJECT TRUNKS
        for (int i = 0; i < tallObj[currentMap].length; i++) {
            if (tallObj[currentMap][i] != null) {
                entityList.add(tallObj[currentMap][i]);
            }
        }


        // SORT & DRAW ENTITIES
        Collections.sort(entityList,
                Comparator.comparingInt(e -> (int) e.worldY)
        );

        for (Entity e : entityList) {
            e.draw(g2);
        }


        // DRAW CANOPIES (OVERLAY)
        for (int i = 0; i < tallObj[currentMap].length; i++) {
            if (tallObj[currentMap][i] != null) {
                tallObj[currentMap][i].drawCanopy(g2);
            }
        }


        // SOLID AREA DEBUG
        if (keyH.showCollisionBox) {

            // PLAYER
            player.drawSolidArea(g2, this);

            // NPCs
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].drawSolidArea(g2, this);
                }
            }

            // MONSTERS
            for (int i = 0; i < monster[currentMap].length; i++) {
                if (monster[currentMap][i] != null) {
                    monster[currentMap][i].drawSolidArea(g2, this);
                }
            }

            // OBJECTS
            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null) {
                    obj[currentMap][i].drawSolidArea(g2, this);
                }
            }

            // INTERACTIVE TILES (1x1)
            for (int i = 0; i < iTile[currentMap].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].drawSolidArea(g2, this);
                }
            }

            // TALL OBJECT TRUNKS
            for (int i = 0; i < tallObj[currentMap].length; i++) {
                if (tallObj[currentMap][i] != null) {
                    tallObj[currentMap][i].drawSolidArea(g2, this);
                }
            }
        }


        entityList.clear();


        // ENVIRONMENT / UI

        eManager.draw(g2);
        map.drawMiniMap(g2);
        csManager.draw(g2);
        ui.draw(g2);


        // DEBUG
        if (keyH.showDebugText) {

            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);

            int x = 10;
            int y = 200;
            int lh = 20;

            g2.drawString("WorldX " + (int) player.worldX, x, y); y += lh;
            g2.drawString("WorldY " + (int) player.worldY, x, y); y += lh;
            g2.drawString("Col " + (int) (player.worldX + player.solidArea.x) / tileSize, x, y); y += lh;
            g2.drawString("Row " + (int) (player.worldY + player.solidArea.y) / tileSize, x, y); y += lh;
            g2.drawString("Map " + currentMap, x, y); y += lh;
            g2.drawString("Draw time: " + passed, x, y);

            //drawing path
            g2.setColor(new Color(111,140,150,70));
            for(int i = 0; i < pFinder.pathList.size(); i++)
            {
                int worldX = pFinder.pathList.get(i).col * tileSize;
                int worldY = pFinder.pathList.get(i).row * tileSize;
                int screenX = (int) (worldX - player.worldX + player.screenX);
                int screenY = (int) (worldY - player.worldY + player.screenY);

                g2.fillRect(screenX,screenY,tileSize, tileSize);
            }


            int screenCenterX = screenWidth / 2;
            int screenCenterY = screenHeight / 2;

            int size = 120;

            int drawX = screenCenterX - size / 2;
            int drawY = screenCenterY - size / 2;

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(drawX, drawY, size, size);

            g2.setColor(Color.YELLOW);
            g2.drawLine(screenCenterX - 10, screenCenterY, screenCenterX + 10, screenCenterY);
            g2.drawLine(screenCenterX, screenCenterY - 10, screenCenterX, screenCenterY + 10);
        }
    }

    private int lastXOffset = 0;
    private int lastYOffset = 0;

    public void drawToScreen() {

        BufferStrategy bs = Main.window.getBufferStrategy();
        if (bs == null) {
            // Try to create one if missing (should usually be created in toggleFullScreen or on startup)
            try {
                Main.window.createBufferStrategy(3);
            } catch (IllegalStateException ise) {
                // window not ready yet; skip this frame
                return;
            }
            return;
        }

        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        int xOffset = 0;
        int yOffset = 0;

        if (fullScreenOn) {
            // ensure no leftover windowed offsets
            lastXOffset = 0;
            lastYOffset = 0;
            xOffset = 0;
            yOffset = 0;
        } else {
            // Windowed mode — read Insets, but only accept them when non-zero
            Insets in = Main.window.getInsets();
            int left = in.left;
            int top  = in.top;

            if (left != 0 || top != 0) {
                // valid insets — update last-known good
                lastXOffset = left;
                lastYOffset = top;
            }
            // if insets are zero (very transient), we hold onto the last known good values
            xOffset = lastXOffset;
            yOffset = lastYOffset;
        }

        // finally draw scaled image
        g2.drawImage(tempScreen, xOffset, yOffset, screenWidth2, screenHeight2, null);

        g2.dispose();
        bs.show();
        Toolkit.getDefaultToolkit().sync();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    public void changeArea() {
        if(nextArea != currentArea)
        {
            stopMusic();

            if(nextArea == outside)
            {
                playMusic(0);
            }
            if(nextArea == indoor)
            {
                playMusic(18);
            }
            if(nextArea == dungeon)
            {
                playMusic(19);
            }
            aSetter.setNPC(); //reset for at the dungeon puzzle's stuck rocks.
        }

        currentArea = nextArea;
        aSetter.setMonster();
    }
    public void removeTempEntity() {
        for(int mapNum = 0; mapNum < maxMap; mapNum++) {
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[mapNum][i] != null && obj[mapNum][i].temp) {
                    obj[mapNum][i] = null;
                }
            }
        }
    }
}
