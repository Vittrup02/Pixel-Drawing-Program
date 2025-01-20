package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;
import javafx.scene.input.MouseButton;

public class PipetteTool implements Tool {

    private MouseButton activeMouseButton;

    // Constructor
    public PipetteTool(MouseButton activeMouseButton) {
        this.activeMouseButton = activeMouseButton;
    }

    @Override
    public void press(Controller ctrl, Point point) {
        Color pickedColor = ctrl.getImage().getPixel(point);

        if (activeMouseButton == MouseButton.PRIMARY) {
            ctrl.setColor(pickedColor); // Set primary color
        } else if (activeMouseButton == MouseButton.SECONDARY) {
            ctrl.setColor2(pickedColor); // Set secondary color
        }
        ctrl.notifyChange();
    }

    @Override
    public void abandon(Controller controller) {
    }

    @Override
    public void release(Controller controller, Point point) {
    }

    @Override
    public void update(Controller controller, Point point) {
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
