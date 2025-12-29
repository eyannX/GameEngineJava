package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip clip;
    URL[] soundURL = new URL[50];
    FloatControl fc;
    int volumeScale = 3;
    float volume;
    public boolean looping = false;
    boolean active = false;


    public Sound() {

        soundURL[0] = getClass().getResource("/sound/wind.wav");
        soundURL[1] = getClass().getResource("/sound/pop.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/damageOrc.wav");
        soundURL[6] = getClass().getResource("/sound/NightSE.wav");
        soundURL[7] = getClass().getResource("/sound/swing3.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/arrow.wav");
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");
        soundURL[12] = getClass().getResource("/sound/gameover.wav");
        soundURL[13] = getClass().getResource("/sound/stairs.wav");
        soundURL[14] = getClass().getResource("/sound/sleep.wav");
        soundURL[15] = getClass().getResource("/sound/blocked.wav");
        soundURL[16] = getClass().getResource("/sound/parry.wav");
        soundURL[17] = getClass().getResource("/sound/speak.wav");
        soundURL[18] = getClass().getResource("/sound/Merchant.wav");
        soundURL[19] = getClass().getResource("/sound/Dungeon.wav");
        soundURL[20] = getClass().getResource("/sound/chipwall.wav");
        soundURL[21] = getClass().getResource("/sound/dooropen.wav");
        soundURL[22] = getClass().getResource("/sound/FinalBattle.wav");
        soundURL[23] = getClass().getResource("/sound/footstepGrass.wav");
        soundURL[24] = getClass().getResource("/sound/Footstep_Right_Grass.wav");
        soundURL[25] = getClass().getResource("/sound/slimeDamage.wav");
        soundURL[26] = getClass().getResource("/sound/Inventory.wav");
        soundURL[27] = getClass().getResource("/sound/hurt1.wav");
        soundURL[28] = getClass().getResource("/sound/hurt2.wav");
        soundURL[29] = getClass().getResource("/sound/hurt3.wav");
        soundURL[30] = getClass().getResource("/sound/eat1.wav");
        soundURL[31] = getClass().getResource("/sound/eat2.wav");
        soundURL[32] = getClass().getResource("/sound/eat3.wav");
        soundURL[33] = getClass().getResource("/sound/swordUnsheathe.wav");
        soundURL[34] = getClass().getResource("/sound/equipAxe.wav");
        soundURL[35] = getClass().getResource("/sound/equipLantern.wav");
        soundURL[36] = getClass().getResource("/sound/chickenScream.wav");
        soundURL[37] = getClass().getResource("/sound/chickenEat.wav");
        soundURL[38] = getClass().getResource("/sound/parrotScream.wav");
        soundURL[39] = getClass().getResource("/sound/parrotHurt.wav");
        soundURL[40] = getClass().getResource("/sound/fireSound.wav");


        /*sound = new Sound();
        //ound.setFile(36); // chicken scream
        //sound.loop();      // start looping once */

    }

    public void setFile(int i) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); //pass value for clip // -80f to 6f // 6 is max. -80f = 0
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        checkVolume();
    }

    public void play() {
        if (clip == null) return;
        looping = false;
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        looping = true;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void stop() {
        if (clip == null) return;
        clip.stop();
        active = false;
    }


    public void checkVolume() {

        switch (volumeScale) {
            case 0 : volume = -80f; break;
            case 1 : volume = -20f; break;
            case 2 : volume = -12f; break;
            case 3 : volume = -5f; break;
            case 4 : volume = 1f; break;
            case 5 : volume = 6f; break;
        }
        fc.setValue(volume);
    }
    public void setVolumeByDistance(
            int listenerX, int listenerY,
            int sourceX, int sourceY,
            int maxDistance
    ) {
        if (clip == null || fc == null) return;

        int dx = listenerX - sourceX;
        int dy = listenerY - sourceY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // OUT OF RANGE → stop sound completely
        if (distance >= maxDistance) {
            if (clip.isRunning()) {
                clip.stop();
                active = false;
            }
            return;
        }

        // IN RANGE → ensure playing
        if (!clip.isRunning() && looping) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            active = true;
        }

        // Logarithmic falloff (realistic)
        double normalized = 1.0 - (distance / maxDistance);
        normalized = Math.max(0.0, Math.min(1.0, normalized));

        // Prevent log(0)
        float volume = (float)(20.0 * Math.log10(normalized));

        // Clamp volume
        volume = Math.max(volume, -80f);
        volume = Math.min(volume, 0f);

        fc.setValue(volume);
    }

}
