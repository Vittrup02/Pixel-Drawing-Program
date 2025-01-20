package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class Eraser implements Tool {

    private boolean pressed = false;
    private int brushSize = 1; // Default brush size

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
        update(ctrl, point);
    }

    public void update(Controller ctrl, Point point) {
        if (pressed) {
            int halfBrushSize = brushSize / 2;
    
            // Adjust the start point to center the brush around the cursor
            int startX = point.x() - halfBrushSize + (brushSize % 2 == 0 ? 1 : 0);
            int startY = point.y() - halfBrushSize + (brushSize % 2 == 0 ? 1 : 0);
    
            for (int dx = 0; dx < brushSize; dx++) {
                for (int dy = 0; dy < brushSize; dy++) {
                    ctrl.erasePixelAt(new Point(startX + dx, startY + dy));
                }
            }
        }
    }    

    @Override
    public void release(Controller ctrl, Point point) {
    update(ctrl, point);
    pressed = false;
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
    }

    public void setBrushSize(int size) {
        this.brushSize = Math.max(1, size);
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    @Override
    public boolean isActive() {
        return pressed;
    }
}
