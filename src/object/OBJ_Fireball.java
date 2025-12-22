package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OBJ_Fireball extends Projectile {

    GamePanel gp;
    public static final String objName = "Fireball";

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = objName;
        speed = 10;
        maxLife = 80;   //after 80 frames, projectile disappears
        life = maxLife;
        attack = 1;
        knockBackPower = 5;
        useCost = 1; //spend 1 mana
        alive = false;
        price = 75;
        getImage();


    }
    public void getImage()
    {
        up1 = setup("/projectile/fireball_up_1", gp.tileSize,gp.tileSize);
        up2 = setup("/projectile/fireball_up_2", gp.tileSize,gp.tileSize);
        down1 = setup("/projectile/fireball_down_1", gp.tileSize,gp.tileSize);
        down2 = setup("/projectile/fireball_down_2", gp.tileSize,gp.tileSize);
        left1 = setup("/projectile/fireball_left_1", gp.tileSize,gp.tileSize);
        left2 = setup("/projectile/fireball_left_2", gp.tileSize,gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize,gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize,gp.tileSize);
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
    public boolean haveResource(Entity user)
    {
        boolean haveResource = false;
        if(user.currentHunger >= useCost)
        {
            haveResource = true;
        }
        return haveResource;
    }
    public void subtractResource(Entity user)
    {
        user.currentHunger -= useCost;
    }
    public Color getParticleColor()
    {
        Color color = new Color(240,50,0);
        return color;
    }
    public int getParticleSize()
    {
        int size = 10; //pixels
        return size;
    }
    public int getParticleSpeed()
    {
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife()
    {
        int maxLife = 20;
        return maxLife;
    }

}
