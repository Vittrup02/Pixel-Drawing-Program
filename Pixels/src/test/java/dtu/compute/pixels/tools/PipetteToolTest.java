package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.PipetteTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;

public class PipetteToolTest {

    @Test
    @DisplayName("can pick existing colors from image pixels using different mouse buttons")
    void canPickExistingColorsFromImagePixelsUsingDifferentMouseButtons() throws IOException {
        // Setup the image with predefined colors
        Image img = new Image(new Rect(5, 5));
        img.setPixel(new Point(2, 2), Color.RED);
        img.setPixel(new Point(0, 0), Color.BLUE);

        // Initialize Controller with PipetteTool
        Controller ctrl = new Controller().setImage(img);

        // Test with PRIMARY button
        PipetteTool primaryTool = new PipetteTool(MouseButton.PRIMARY);
        ctrl.setTool(primaryTool);
        ctrl.press(new Point(2, 2));
        Color primaryPickedColor = ctrl.getColor(); // Assuming getColor retrieves the primary color
        assertEquals(Color.RED, primaryPickedColor, "Primary picked color should be red");

        // Test with SECONDARY button
        PipetteTool secondaryTool = new PipetteTool(MouseButton.SECONDARY);
        ctrl.setTool(secondaryTool);
        ctrl.press(new Point(0, 0));
        Color secondaryPickedColor = ctrl.getColor2(); // Assuming getColor2 retrieves the secondary color
        assertEquals(Color.BLUE, secondaryPickedColor, "Secondary picked color should be blue");

        TestUtils.goldenTest("pipette", img);
    }

    @Test
    @DisplayName("isActive method should correctly reflect the tool's state")
    void testIsActive() {
        // Instantiate PipetteTool
        PipetteTool tool = new PipetteTool(MouseButton.PRIMARY);

        // Verify initial state
        // Assuming the tool is active by default
        assertTrue(tool.isActive(), "Tool should be active by default");
}
}