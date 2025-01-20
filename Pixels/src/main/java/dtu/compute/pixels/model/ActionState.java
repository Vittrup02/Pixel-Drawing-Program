package dtu.compute.pixels.model;

import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.controller.tools.Pen;


public class ActionState {
    private final Image imageState;
    private final Tool tool;

    public ActionState(Image imageState, Tool tool) {
        this.imageState = imageState;
        this.tool = tool;
    }

    public Image getImageState() {
        return imageState;
    }

    public Tool getTool() {
        return tool;
    }
}
