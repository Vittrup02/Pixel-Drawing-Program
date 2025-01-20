package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EraserTest {

    private Eraser eraser;
    private Controller controller;

    @BeforeEach
    void setUp() {
        eraser = new Eraser();
        controller = new Controller();
        controller.setTool(eraser);
    }

    @Test
    @DisplayName("Eraser can be activated and deactivated")
    void eraserActivationAndDeactivation() {
        assertFalse(eraser.isActive(), "Eraser should not be active initially");

        Point point = new Point(5, 5);

        // Press to activate the eraser
        eraser.press(controller, point);
        assertTrue(eraser.isActive(), "Eraser should be active after pressing");

        // deactivate the eraser
        eraser.release(controller, point);
        assertFalse(eraser.isActive(), "Eraser should be inactive after releasing");

        // deactivate the eraser
        eraser.press(controller, point);
        eraser.abandon(controller);
        assertFalse(eraser.isActive(), "Eraser should be inactive after abandoning");
    }

    @Test
    @DisplayName("Eraser erases pixels in the image")
    void eraserErasesPixels() {
        Image image = new Image(new Rect(10, 10));
        controller.setImage(image);

        Point point = new Point(5, 5);

        // Set a pixel in the image
        controller.setPixel(point, Color.BLACK);

        eraser.press(controller, point);
        eraser.update(controller, point);
        eraser.release(controller, point);

        // Check if the pixel has been erased
        assertEquals(Color.TRANSPARENT, image.getPixel(point), "Pixel should be erased by the eraser");
    }

    @Test
    @DisplayName("Eraser has a default brush size of 1")
    void eraserDefaultBrushSize() {
        assertEquals(1, eraser.getBrushSize(), "Eraser should have a default brush size of 1");
    }

    @Test
    @DisplayName("Eraser can change brush size")
    void eraserChangeBrushSize() {
        int newSize = 5;
        eraser.setBrushSize(newSize);
        assertEquals(newSize, eraser.getBrushSize(), "Eraser should be able to change brush size");
    }
}
