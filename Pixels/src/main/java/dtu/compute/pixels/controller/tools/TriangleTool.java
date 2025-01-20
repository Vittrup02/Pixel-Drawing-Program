package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;

public class TriangleTool implements Tool {
    private Point firstPoint;
    private Point secondPoint;
    private boolean drawing = false;
    private boolean secondPointSet = false;

    @Override
    public void press(Controller ctrl, Point point) {
        if (!drawing) {
            // First click starts the drawing and sets the first point
            firstPoint = point;
            drawing = true;
            secondPointSet = false;
            ctrl.setLastUsedTool(this);
            ctrl.addToHistoryBasedOnTool();
        } else if (!secondPointSet) {
            // Second click sets the second point
            secondPoint = point;
            secondPointSet = true;
        } else {
            // Third click completes the triangle and draws it on the image
            drawTriangle(ctrl.getImage(), firstPoint, secondPoint, point, ctrl.getActiveColor());
            ctrl.commitScratch(); // Commit the triangle to the image
            resetTool();
        }
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (drawing) {
        ctrl.getScratch().reset(Color.TRANSPARENT); // Clear previous preview
        if (!secondPointSet) {
        // Update the second point as the user drags
        secondPoint = point;
        }
        // Draw the current state of the triangle on scratch
        drawTriangle(ctrl.getScratch(), firstPoint, secondPoint, point, ctrl.getActiveColor());
        ctrl.notifyChange();
        }
        }
        @Override
        public void release(Controller ctrl, Point point) {
        }
        
        @Override
        public void abandon(Controller ctrl) {
            resetTool();
        }
        
        @Override
        public boolean isActive() {
            return drawing;
        }
        
        private void resetTool() {
            drawing = false;
            secondPointSet = false;
            firstPoint = null;
            secondPoint = null;
        }
        
        private void drawTriangle(Image target, Point p1, Point p2, Point p3, Color color) {
            // Draw the triangle on the specified target (scratch or image)
            target.drawLine(p1, p2, color);
            target.drawLine(p2, p3, color);
            target.drawLine(p3, p1, color);
        }
    }        
