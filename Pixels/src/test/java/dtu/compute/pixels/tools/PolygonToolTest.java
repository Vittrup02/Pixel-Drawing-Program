package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.PolygonTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonToolTest {

    private Controller controller;
    private PolygonTool polygonTool;
    private Image img;

    @BeforeEach
    void setUp() {
        img = new Image(new Rect(20, 20));
        controller = new Controller().setImage(img);
        polygonTool = new PolygonTool();
        controller.setTool(polygonTool);
    }

    @Test
    @DisplayName("Test completing a polygon")
    void testCompletingPolygon() {
        // Set a default drawing color and activate it for drawing
        controller.setColor(Color.RED);
        controller.setActiveColor(MouseButton.PRIMARY);
    
        // Simulate drawing a polygon
        controller.press(new Point(1, 1)); // Start
        controller.press(new Point(5, 1)); // Second point
        controller.press(new Point(5, 5)); // Third point
        controller.press(new Point(1, 5)); // Fourth point
        controller.press(new Point(1, 1)); // Close to the first point to complete
    
        // Check if the polygon is completed (isActive should be false)
        assertFalse(polygonTool.isActive(), "Polygon drawing should be complete");
    
        // Optionally, check the image to see if the polygon appears as expected
        // You can use a method similar to goldenTest or compare image buffers
    }

@Test
@DisplayName("Test update method changes state")
void testUpdate() {
    // Set a default drawing color and activate it for drawing
    controller.setColor(Color.RED);
    controller.setActiveColor(MouseButton.PRIMARY);

    // Start drawing a polygon
    controller.press(new Point(1, 1)); 
    controller.update(new Point(5, 1)); // Simulate dragging to a new point

    // Assertions depend on how the update affects the state
    // For instance, you might check if the scratch image or some preview state changes
    // Example (adapt as needed):
    Image expectedImage = new Image(new Rect(20, 20)); // Expected state of the image
    expectedImage.drawLine(new Point(1, 1), new Point(5, 1), Color.RED);
    assertArrayEquals(expectedImage.getBuffer(), controller.getScratch().getBuffer(), "Scratch image should have updated line after update");
}


@Test
@DisplayName("Test isActive method")
void testIsActive() {
    // Set a default drawing color and activate it for drawing
    controller.setColor(Color.RED);
    controller.setActiveColor(MouseButton.PRIMARY);

    // Initially, isActive should be false
    assertFalse(polygonTool.isActive(), "Tool should not be active initially");

    // Start drawing a polygon
    controller.press(new Point(1, 1)); // First point
    assertTrue(polygonTool.isActive(), "Tool should be active after first press");
    
    // Add more points to the polygon
    controller.press(new Point(5, 1)); // Second point
    controller.press(new Point(5, 5)); // Third point
    controller.press(new Point(1, 5)); // Fourth point
    
    // Complete the polygon by pressing near the first point
    // Note: Adjust the point to match your completion logic
    controller.press(new Point(1, 1)); 
    
    // Check if the polygon drawing is completed
    assertFalse(polygonTool.isActive(), "Tool should not be active after completing polygon");
}    

    @Test
    @DisplayName("can draw a polygon on the image")
    void canDrawPolygonOnImage() throws IOException {
        Image img = new Image(new Rect(20, 20));

        // Set up the controller with the PolygonTool
        Controller ctrl = new Controller().setTool(new PolygonTool()).setImage(img);

        // Set the active color and draw a polygon
        ctrl.setColor(Color.RED); // Set the active color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to draw the polygon

        // Draw the polygon
        ctrl.press(new Point(2, 2)); // First point
        ctrl.press(new Point(10, 2)); // Second point
        ctrl.press(new Point(10, 10)); // Third point
        ctrl.press(new Point(2, 10)); // Fourth point
        // Simulate clicking near the first point to complete the polygon
        ctrl.press(new Point(2, 2)); 

        // Ensure that the polygon is drawn on the image
        TestUtils.goldenTest("polygonTool", img);
    }
}

