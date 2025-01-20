package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.controller.Controller;


import java.util.Random;

public class Pen implements Tool {

    private boolean pressed = false;
    private int brushSize = 1;
    private BrushType brushType = BrushType.NORMAL;

    public enum BrushType {
        NORMAL,
        SPRAY,
        DOTTED,
        TEXTURED
    }

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
        update(ctrl, point);
    }

    public void update(Controller ctrl, Point point) {
        if (!pressed) {
            ctrl.resetScratch(false);
            return;
        }

        switch (brushType) {
            case SPRAY:
                drawSpray(ctrl, point);
                break;
            case DOTTED:
                drawDotted(ctrl, point);
                break;
            case TEXTURED:
                drawTextured(ctrl, point);
                break;
            case NORMAL:
            default:
                drawNormal(ctrl, point);
                break;
        }

        // Display the scratch layer as a preview
        ctrl.updatePreview(point);
    }

    @Override
    public void release(Controller ctrl, Point point) {
        update(ctrl, point);
        pressed = false;
        ctrl.commitScratch(); // Commit scratch to image
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        ctrl.resetScratch(true);
    }

    // Setter and getter for brush size and type
    public void setBrushSize(int size) {
        this.brushSize = Math.max(1, size);
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    public void setBrushType(BrushType type) {
        this.brushType = type;
    }

    public BrushType getBrushType() {
        return this.brushType;
    }

    @Override
    public boolean isActive() {
        return pressed;
    }

    // Define brush drawing methods for each type
    private void drawNormal(Controller ctrl, Point point) {
        int halfBrushSize = brushSize / 2;
        int startX = point.x() - halfBrushSize + (brushSize % 2 == 0 ? 1 : 0);
        int startY = point.y() - halfBrushSize + (brushSize % 2 == 0 ? 1 : 0);
    
        // Draw a square of size brushSize x brushSize
        for (int dx = 0; dx < brushSize; dx++) {
            for (int dy = 0; dy < brushSize; dy++) {
                ctrl.setScratchPixel(new Point(startX + dx, startY + dy), ctrl.getActiveColor());
            }
        }
    }
    
    private void drawSpray(Controller ctrl, Point point) {
        Random rand = new Random();
        int radius = brushSize * 5; // Radius of the spray effect
        int dots = brushSize * 20; // Number of dots to draw, increases with brush size
    
        for (int i = 0; i < dots; i++) {
            // Generate a random angle and distance
            double angle = Math.random() * 2 * Math.PI;
            double distance = radius * Math.sqrt(rand.nextDouble());
            int dx = (int) (distance * Math.cos(angle));
            int dy = (int) (distance * Math.sin(angle));
    
            // Set pixel in the spray area
            ctrl.setScratchPixel(new Point(point.x() + dx, point.y() + dy), ctrl.getActiveColor());
        }
    }
    
    private void drawDotted(Controller ctrl, Point point) {
    // Define the dot size (radius) and spacing between dots
    int dotRadius = 2; // You can adjust this value
    int dotSpacing = 2; // You can adjust this value

    // Get the active color
    Color activeColor = ctrl.getActiveColor();

    // Loop through the canvas and draw dots
    for (int x = point.x() - dotRadius; x <= point.x() + dotRadius; x += dotSpacing) {
        for (int y = point.y() - dotRadius; y <= point.y() + dotRadius; y += dotSpacing) {
            // Calculate the distance from the center
            double distance = Math.sqrt(Math.pow(x - point.x(), 2) + Math.pow(y - point.y(), 2));

            // Draw a dot if it's within the dotRadius
            if (distance <= dotRadius) {
                ctrl.setScratchPixel(new Point(x, y), activeColor);
            }
        }
    }
}

    

private void drawTextured(Controller ctrl, Point point) {
    int textureSize = brushSize; // Size of the texture

    for (int dx = -textureSize; dx <= textureSize; dx++) {
        for (int dy = -textureSize; dy <= textureSize; dy++) {
            if (isCheckerboardPattern(dx, dy)) {
                ctrl.setScratchPixel(new Point(point.x() + dx, point.y() + dy), ctrl.getActiveColor());
            }
        }
    }
}
    
    private boolean isCheckerboardPattern(int dx, int dy) {
        // Checkerboard pattern logic: alternate between true and false
        return (dx % 2 == 0) ^ (dy % 2 == 0);
    }
    
    
}
