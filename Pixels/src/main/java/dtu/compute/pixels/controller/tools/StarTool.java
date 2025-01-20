package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.*;

public class StarTool implements Tool {

    private Point start;
    private Point release;
    private boolean drawing = false;

    public StarTool() {
    }

    @Override
    public void press(Controller ctrl, Point point) {
        start = point;
        drawing = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
    }

    public void update(Controller ctrl, Point point) {
        release = point;
        if (drawing) {
            ctrl.getScratch().reset(Color.TRANSPARENT);
            ctrl.getScratch().drawStar(start, release, ctrl.getActiveColor());
            ctrl.updatePreview(point);
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (drawing) {
            ctrl.getScratch().drawStar(start, release, ctrl.getActiveColor());
            ctrl.commitScratch();
            drawing = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        drawing = false;
        ctrl.resetScratch(true);
    }

    /*
    private void drawLine(Point from, Point to, Color color) {
        int lineThickness = (int) ((thickness - 1) / 2);

        int startX = Math.min(start.x(), to.x());
        int startY = Math.min(start.y(), to.y());
        int endX = Math.max(start.x(), to.x());
        int endY = Math.max(start.y(), to.y());

        for (int i = startX - lineThickness; i <= endX + lineThickness; i++) {
            for (int j = startY; j <= endY; j++) {
                Point currentPoint = new Point(i, j);
                double distanceToLine = distanceToLine(from, to, currentPoint);
                if (distanceToLine <= thickness / 2) {
                    // Set the pixel color in the scratch area

                }
            }
        }
    }
*/

    @Override
    public boolean isActive() {
        return drawing;
    }
}

/*
Her er star figuren med linjerne indeni:

 * @Override
 * public void press(Controller ctrl, Point point) {
 * starPoint = point;
 * update(ctrl, point);
 * }
 * 
 * @Override
 * public void update(Controller ctrl, Point point) {
 * if (starPoint == null)
 * return;
 * 
 * ctrl.resetScratch(false);
 * 
 * int rx = starPoint.x() - point.x();
 * int ry = starPoint.y() - point.y();
 * double r = Math.sqrt(rx * rx + ry * ry); // Radius of the circumscribed
 * circle
 * 
 * double startAngle = Math.atan2(ry, rx);
 * 
 * // Inner and outer radius
 * int outerRadius = Math.abs(point.x() point.x()) / 2;
 * int innerRadius = outerRadius / 2;
 * 
 * Point[] points = new Point[5];
 * for (int i = 0; i < 5; i++) {
 * double angle = 2 * Math.PI * i / 5 + startAngle;
 * points[i] = new Point(
 * (int) Math.round(starPoint.x() + Math.cos(angle) * r),
 * (int) Math.round(starPoint.y() + Math.sin(angle) * r));
 * }
 * 
 * Point[] innterPoints = new Point[5];
 * for (int i = 0; i < 5; i++) {
 * double angle = Math.toRadians(i * 72 + 18); // 72 degrees mellem hver point,
 * starter fra 18 degrees
 * int x = (int) Math.round(rx + innerRadius * Math.cos(angle));
 * int y = (int) Math.round(ry + innerRadius * Math.sin(angle));
 * innerRadius[i] = new Point(x,y);
 * }
 * 
 * for (int i = 0; i < 5; i++) {
 * Point from = points[i];
 * Point to = points[(i + 2) % 5]; // Connect every second point
 * ctrl.getScratch().drawLine(from, to, ctrl.getColor()); // Use the current
 * color
 * }
 * 
 * ctrl.notifyChange();
 * }
 * 
 * @Override
 * public void release(Controller ctrl, Point point) {
 * update(ctrl, point);
 * center = null;
 * ctrl.commitScratch();
 * }
 * 
 * @Override
 * public void abandon(Controller ctrl) {
 * if (isActive()) {
 * center = null; // Reset the center point
 * ctrl.resetScratch(true); // Clear any temporary drawing
 * }
 * }
 * 
 * @Override
 * public boolean isActive() {
 * return center != null; // If center is set, we are in the middle of drawing a
 * star.
 * }
 * }
 */
