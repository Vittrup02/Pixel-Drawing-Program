package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.FillTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;

public class FillToolTest {

@Test
@DisplayName("can fill a region with a color")
void canFillRegionWithColor() throws IOException {
    // Create a 5x5 image
    Image img = new Image(new Rect(5, 5));

    // Set up the controller with the FillTool
    Controller ctrl = new Controller().setTool(new FillTool()).setImage(img);

    // Set the active color and perform a fill operation
    ctrl.setColor(Color.RED); // Target color
    ctrl.setColor(Color.BLUE); // Replacement color
    ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to the target color
    ctrl.click(new Point(2, 2)); // Click in the center to set a target color

    // Fill the region with the replacement color
    ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color back to default
    ctrl.click(new Point(0, 0)); // Click to perform the fill operation

    TestUtils.goldenTest("fillTool", img);
    }
}