package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;

import java.util.ArrayList;
import java.util.List;

public class PolygonTool implements Tool {
    private List<Point> points;
    private boolean drawing = false;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_THRESHOLD = 300; // milliseconds
    private static final int CLOSENESS_THRESHOLD = 1; // Pixels, adjust as needed

    public PolygonTool() {
        points = new ArrayList<>();
    }

    @Override
public void press(Controller ctrl, Point point) {
    if (!drawing) {
        // Start drawing the polygon
        points.clear();
        points.add(point);
        drawing = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
    } else {
        if (isCloseToFirstPoint(point)) {
            // If the clicked point is close to the first point, complete the polygon
            completePolygon(ctrl);
        } else {
            // Otherwise, add a new point to the polygon
            points.add(point);
        }
    }
}

private boolean isCloseToFirstPoint(Point point) {
    if (points.isEmpty()) {
        return false;
    }
    Point firstPoint = points.get(0);
    return Math.abs(firstPoint.x() - point.x()) <= CLOSENESS_THRESHOLD &&
           Math.abs(firstPoint.y() - point.y()) <= CLOSENESS_THRESHOLD;
}

    @Override
    public void update(Controller ctrl, Point point) {
        if (drawing && !points.isEmpty()) {
            ctrl.getScratch().reset(Color.TRANSPARENT); // Clear previous preview
            drawPolygon(ctrl.getScratch(), points, point, ctrl.getActiveColor());
            ctrl.notifyChange();
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
    }

    @Override
    public void abandon(Controller ctrl) {
        drawing = false;
        points.clear();
    }

    @Override
    public boolean isActive() {
        return drawing;
    }

    private void completePolygon(Controller ctrl) {
        if (drawing && points.size() > 2) {
            drawPolygon(ctrl.getImage(), points, null, ctrl.getActiveColor());
            ctrl.commitScratch();
        }
        abandon(ctrl);
    }

    private void drawPolygon(Image target, List<Point> points, Point currentPoint, Color color) {
        for (int i = 0; i < points.size() - 1; i++) {
            target.drawLine(points.get(i), points.get(i + 1), color);
        }
        if (currentPoint != null) {
            target.drawLine(points.get(points.size() - 1), currentPoint, color);
        }
    }
}

