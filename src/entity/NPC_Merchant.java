package entity;

import main.GamePanel;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_Merchant extends Entity{
    public NPC_Merchant(GamePanel gp)
    {
        super(gp);
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
        setItems();

        solidArea = new Rectangle();
        solidArea.x = 32;
        solidArea.y = 50;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public void getImage()
    {
        int height = 76;

        up1 = setup("/npc/merchant_down_1",gp.tileSize,height);
        up2 = setup("/npc/merchant_down_2",gp.tileSize,height);
        down1 = setup("/npc/merchant_down_1",gp.tileSize,height);
        down2 = setup("/npc/merchant_down_2",gp.tileSize,height);
        left1 = setup("/npc/merchant_down_1",gp.tileSize,height);
        left2 = setup("/npc/merchant_down_2",gp.tileSize,height);
        right1 = setup("/npc/merchant_down_1",gp.tileSize,height);
        right2 = setup("/npc/merchant_down_2",gp.tileSize,height);
    }
    public void setDialogue()
    {
        dialogues[0][0] = "He he ha, so you found me.\nI have some good stuff. \nDo you want to trade?";
        dialogues[1][0] = "Come again, hehe!";
        dialogues[2][0] = "You need more coin to buy that!";
        dialogues[3][0] = "You can not carry any more!";
        dialogues[4][0] = "You can not sell an equipped item!";
    }
    public void setItems()
    {
        inventory.add(new OBJ_Croissant(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_Tent(gp));
    }
    public void speak()
    {
        facePlayer();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
    @Override
    public BufferedImage getCurrentImage() {



        return getDirectionalImage(
                up1, up2,
                down1, down2,
                left1, left2,
                right1, right2
        );
    }
}
