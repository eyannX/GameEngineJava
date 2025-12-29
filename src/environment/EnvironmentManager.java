package environment;
import entity.Entity;
import main.GamePanel;
import java.awt.*;
import java.util.ArrayList;

public class EnvironmentManager extends Entity{

    GamePanel gp;
    public Lighting lighting;


    public EnvironmentManager(GamePanel gp) {
        super(gp);
        this.gp = gp;
    }
    public void setup() {

        for (Entity e : gp.entityList) {
            e.update();

        }

        lighting = new Lighting(gp);
    }
    public void update() {

        lighting.update();
    }
    public void draw(Graphics2D g2) {

        lighting.draw(g2);
    }
}
