package entity;// Put this in Player or as a separate class
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class WeaponAttackManager {

    public static class AttackFrames {
        public BufferedImage[][] standingFrames; // [direction][frameIndex]
        public BufferedImage[][] movingFrames;
        public int totalFrames;

        public AttackFrames(int frames, BufferedImage[][] standing, BufferedImage[][] moving) {
            totalFrames = frames;
            standingFrames = standing;
            movingFrames = moving;
        }
    }

    private HashMap<Integer, AttackFrames> weaponAnimations = new HashMap<>();

    public void addWeaponAnimation(int weaponType, AttackFrames frames) {
        weaponAnimations.put(weaponType, frames);
    }

    public AttackFrames getWeaponFrames(int weaponType) {
        return weaponAnimations.get(weaponType);
    }
}
