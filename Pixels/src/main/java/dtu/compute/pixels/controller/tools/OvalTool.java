package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;

public class OvalTool implements Tool {
    private Point start;
    private boolean drawing = false;

    @Override
    public void press(Controller ctrl, Point point) {
        start = point;
        drawing = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (drawing) {
            ctrl.getScratch().reset(Color.TRANSPARENT); // Clear previous preview
            ctrl.getScratch().drawOvalOutline(start, point, ctrl.getActiveColor()); // Draw the outline on scratch
            ctrl.notifyChange();
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (drawing) {
            ctrl.getImage().drawOvalOutline(start, point, ctrl.getActiveColor()); // Draw the final oval on the image
            ctrl.commitScratch();
            drawing = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        drawing = false;
        ctrl.resetScratch(true);
    }

    @Override
    public boolean isActive() {
        return drawing;
    }
}
