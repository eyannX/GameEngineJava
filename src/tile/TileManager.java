package tile;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager  {
    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;  //[StoreMapNumber][col][row] // For Transition Between Maps
    public boolean drawPath = false; // for debug(true = draws the path)
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();


    public TileManager(GamePanel gp)
    {
        this.gp = gp;

        //READ TILE DATA FILE
        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;
        try {
            while((line = br.readLine()) != null)
            {
                fileNames.add(line);
                collisionStatus.add(br.readLine()); //read next line
            }
            br.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        //INITIALIZE THE TILE ARRAY BASED ON THE fileNames size
        tile = new Tile[fileNames.size()]; // grass, wall, water00, water01...
        getTileImage();

        //GET THE maxWorldCol & Row
        is = getClass().getResourceAsStream("/maps/dungeon02.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try
        {
            String line2 = br.readLine();
            String[] maxTile = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;

            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();
        }
        catch(IOException e)
        {
            System.out.println("Exception!");
        }


        loadMap("/maps/worldmap.txt",0); // To change maps easily.
        loadMap("/maps/indoor01.txt",1);
        loadMap("/maps/dungeon01.txt",2);
        loadMap("/maps/dungeon02.txt",3);

    }
    public void getTileImage()
    {
        for(int i = 0; i < fileNames.size(); i++)
        {
            String fileName;
            boolean collision;

            //Get a file name
            fileName = fileNames.get(i);

            //Get a collision status
            if(collisionStatus.get(i).equals("true"))
            {
                collision = true;
            }
            else
            {
                collision = false;
            }

            setup(i, fileName, collision);

        }

    }
    public void setup(int index, String imageName, boolean collision)
    {                                                                       // IMPROVING RENDERING // Scaling with uTool
        UtilityTool uTool = new UtilityTool();                              // With uTool I'm not using anymore like: g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize,null);
        try                                                                 // I use g2.drawImage(tile[tileNum].image, screenX, screenY,null);
        {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imageName));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;


        }
        catch (IOException e)
        {
            System.out.println("BUG BUG BUG TILE BUG");
        }
    }
    public void loadMap(String filePath,int map)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // to read from txt

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow)
            {
                String line = br.readLine();

                while(col < gp.maxWorldCol)
                {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {

        int tileSize = gp.tileSize;

        int camLeft   = (int)(gp.player.worldX - gp.player.screenX);
        int camTop    = (int)(gp.player.worldY - gp.player.screenY);
        int camRight  = camLeft + gp.screenWidth;
        int camBottom = camTop  + gp.screenHeight;

        int colStart = Math.max(0, camLeft / tileSize);
        int colEnd   = Math.min(gp.maxWorldCol - 1, camRight / tileSize + 1);
        int rowStart = Math.max(0, camTop / tileSize);
        int rowEnd   = Math.min(gp.maxWorldRow - 1, camBottom / tileSize + 1);

        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {

                int tileNum = mapTileNum[gp.currentMap][col][row];

                int worldX = col * tileSize;
                int worldY = row * tileSize;

                int screenX = worldX - camLeft;
                int screenY = worldY - camTop;

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }
    }

}
