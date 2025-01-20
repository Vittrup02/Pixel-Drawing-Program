package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;
import java.util.LinkedList;
import java.util.Queue;

public class FillTool implements Tool {
    private boolean drawing = false;
    @Override
    public void press(Controller ctrl, Point point) {
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
        Color targetColor = ctrl.getImage().getPixel(point);
        Color replacementColor = ctrl.getActiveColor();

        if (!targetColor.equals(replacementColor)) {
            fill(ctrl, point, targetColor, replacementColor);
        }
    }

    private void fill(Controller ctrl, Point start, Color targetColor, Color replacementColor) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Point p = queue.remove();
            if (withinBounds(ctrl, p) && ctrl.getImage().getPixel(p).equals(targetColor)) {
                ctrl.getImage().setPixel(p, replacementColor);

                // Add adjacent points to the queue
                queue.add(new Point(p.x() + 1, p.y()));
                queue.add(new Point(p.x() - 1, p.y()));
                queue.add(new Point(p.x(), p.y() + 1));
                queue.add(new Point(p.x(), p.y() - 1));
            }
        }
        ctrl.notifyChange(); // Notify observers after the fill operation is complete
    }

    private boolean withinBounds(Controller ctrl, Point p) {
        return p.x() >= 0 && p.x() < ctrl.getImage().getSize().width() && 
               p.y() >= 0 && p.y() < ctrl.getImage().getSize().height();
    }

    @Override
    public void update(Controller ctrl, Point point) {
    }

    @Override
    public void release(Controller ctrl, Point point) {
    }

    @Override
    public void abandon(Controller ctrl) {
    }

    @Override
    public boolean isActive() {
        return drawing;
    }
}
