package entity;

import java.awt.image.BufferedImage;

public class animation {

    private final BufferedImage[] frames;
    private final int frameDelay;
    private int frameIndex = 0;
    private int counter = 0;
    private boolean finished = false;

    public animation(BufferedImage[] frames, int frameDelay) {
        this.frames = frames;
        this.frameDelay = frameDelay;
    }

    public BufferedImage update() {
        if (finished) return frames[frames.length - 1];

        counter++;
        if (counter >= frameDelay) {
            counter = 0;
            frameIndex++;
            if (frameIndex >= frames.length) {
                frameIndex = frames.length - 1;
                finished = true;
            }
        }
        return frames[frameIndex];
    }

    public void reset() {
        frameIndex = 0;
        counter = 0;
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }
}

