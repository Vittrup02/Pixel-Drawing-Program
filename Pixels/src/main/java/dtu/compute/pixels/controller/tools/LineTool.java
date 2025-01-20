package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class LineTool implements Tool {

    private Point start;
    private boolean drawing = false;

    // viser i terminalen hvilke koordinater der bruges når der laves lige linjer
    // med LineTool.
    @Override
    public void press(Controller ctrl, Point point) {
        start = point;
        drawing = true;
        ctrl.setLastUsedTool(this);
        ctrl.addToHistoryBasedOnTool();
        System.out.println("Press - Start Point: " + point);
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (drawing) {
            ctrl.resetScratch(false);
            ctrl.getScratch().drawLine(start, point, ctrl.getActiveColor());
            System.out.println("Update - Current Point: " + point);
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        update(ctrl, point);
        ctrl.commitScratch();
        drawing = false;
        System.out.println("Release - End Point: " + point);
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
