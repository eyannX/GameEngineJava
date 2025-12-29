package object;

import main.GamePanel;
import main.Sound;

import java.awt.image.BufferedImage;

public class OBJ_Campfire extends AnimatedObject {

    GamePanel gp;
    public static final String objName = "Campfire";


    public OBJ_Campfire(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = objName;
        type = type_solidLight;

        collision = true;

        solidArea.x = 20;
        solidArea.y = 30;
        solidArea.width = 50;
        solidArea.height = 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        ambientSound = new Sound();
        ambientSound.setFile(40);
        ambientSound.looping = true;

        soundRange = gp.tileSize *4;


        lightRadius = 350;


        getImage();
    }

    public void getImage() {

        int scale = 80;

        up1 = setup("/objects/campfire/campfire1", scale, scale);
        up2 = setup("/objects/campfire/campfire2", scale, scale);
        down1 = setup("/objects/campfire/campfire3", scale, scale);
        down2 = setup("/objects/campfire/campfire4", scale, scale);
        left1 = setup("/objects/campfire/campfire5", scale, scale);
        left2 = setup("/objects/campfire/campfire6", scale, scale);
        right1 = setup("/objects/campfire/campfire7", scale, scale);
        right2 = setup("/objects/campfire/campfire8", scale, scale);
    }

    @Override
    public int getMaxFrame() {
        return 8;  // total frames for campfire animation
    }

    @Override
    public int getAnimationSpeed() {
        return 5;  // adjust to control animation speed
    }
    @Override
    public BufferedImage getCurrentImage() {
        switch(spriteNum) {
            case 1: return up1;
            case 2: return up2;
            case 3: return down1;
            case 4: return down2;
            case 5: return left1;
            case 6: return left2;
            case 7: return right1;
            case 8: return right2;
            default: return up1;
        }
    }
}
