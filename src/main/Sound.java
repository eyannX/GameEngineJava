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
        soundURL[10] = getClass().getResource("/sound/fireball.wav");
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
            clip.start();
    }
    public void loop() {

            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
            clip.stop();
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
}
