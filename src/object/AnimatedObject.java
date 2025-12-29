package object;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class AnimatedObject extends Entity {

    public AnimatedObject(GamePanel gp) {
        super(gp);

        // Setup any default properties here
        collision = false;  // Typically objects like fire don’t block player
    }

    @Override
    public void update() {
        super.update();  // calls the sprite animation update logic in Entity
        // Add any custom logic if needed
    }

    @Override
    public BufferedImage getCurrentImage() {
        // Return the correct animation frame based on spriteNum
        // You must override this in subclasses or here if you load images dynamically
        return up1; // fallback image
    }
}
