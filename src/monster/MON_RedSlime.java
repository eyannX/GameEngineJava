package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Dinar;
import object.OBJ_Heart;
import object.OBJ_HungerBar;
import object.OBJ_Rock;

import java.awt.image.BufferedImage;
import java.util.Random;

public class MON_RedSlime extends Entity {

    GamePanel gp; // cuz of different package
    public MON_RedSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;
        name = "Red Slime";
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 8;
        life = maxLife;
        attack = 4;
        defense = 0;
        exp = 4;
        projectile = new OBJ_Rock(gp);
        hostile = true;


        solidArea.x = 10;
        solidArea.y = 25;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage()
    {
        up1 = setup("/monster/redslime_down_1",gp.tileSize,gp.tileSize);
        up2 = setup("/monster/redslime_down_2",gp.tileSize,gp.tileSize);
        down1 = setup("/monster/redslime_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/monster/redslime_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/monster/redslime_down_1",gp.tileSize,gp.tileSize);
        left2 = setup("/monster/redslime_down_2",gp.tileSize,gp.tileSize);
        right1 = setup("/monster/redslime_down_1",gp.tileSize,gp.tileSize);
        right2 = setup("/monster/redslime_down_2",gp.tileSize,gp.tileSize);
    }
    public void setAction()
    {
        if(onPath)
        {

            //Check if it stops chasing
            checkStopChasingOrNot(gp.player,15,100);

            //Search the direction to go
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

            //Check if it shoots a projectile
            checkShootOrNot(200, 30);
        }
        else
        {
            //Check if it starts chasing
            checkStartChasingOrNot(gp.player, 5, 100);

            //Get a random direction
            getRandomDirection(120);
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        //direction = gp.player.direction;
        onPath = true; // gets aggro
    }
    public void checkDrop()
    {
        //CAST A DIE
        int i = new Random().nextInt(100)+1;

        //SET THE MONSTER DROP
        if(i < 50)
        {
            dropItem(new OBJ_Dinar(gp));
        }
        if(i >= 50 && i < 75)
        {
            dropItem(new OBJ_Heart(gp));
        }
        if(i >= 75 && i < 100)
        {
            dropItem(new OBJ_HungerBar(gp));
        }
    }
    @Override
    public BufferedImage getCurrentImage() {

        if(attacking) {
            return switch(direction) {
                case "up"    -> (spriteNum == 1 ? attackUp1 : attackUp2);
                case "down"  -> (spriteNum == 1 ? attackDown1 : attackDown2);
                case "left"  -> (spriteNum == 1 ? attackLeft1 : attackLeft2);
                case "right" -> (spriteNum == 1 ? attackRight1 : attackRight2);
                default -> null;
            };
        }

        return getDirectionalImage(
                up1, up2,
                down1, down2,
                left1, left2,
                right1, right2
        );
    }
}
