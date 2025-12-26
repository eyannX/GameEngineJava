package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {

    public int mouseX, mouseY;

    public boolean leftPressed, rightPressed;
    public boolean leftJustClicked, rightJustClicked;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !leftPressed) {
            leftPressed = true;
            leftJustClicked = true;
        }
        if (e.getButton() == MouseEvent.BUTTON3 && !rightPressed) {
            rightPressed = true;
            rightJustClicked = true;
        }
    }



    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) leftPressed = false;
        if (e.getButton() == MouseEvent.BUTTON3) rightPressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // Unused but required
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
