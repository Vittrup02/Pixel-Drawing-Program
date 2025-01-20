package dtu.compute.pixels.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RectTest {

    @Test
    void testArea() {
        Rect rect = new Rect(10, 20);
        assertEquals(200, rect.area(), "Area should be width multiplied by height");
    }

    @Test
    void testWithWidth() {
        Rect rect = new Rect(10, 20);
        Rect newRect = rect.withWidth(30);
        assertEquals(30, newRect.width(), "Width should be updated");
        assertEquals(20, newRect.height(), "Height should remain unchanged");
    }

    @Test
    void testWithHeight() {
        Rect rect = new Rect(10, 20);
        Rect newRect = rect.withHeight(30);
        assertEquals(10, newRect.width(), "Width should remain unchanged");
        assertEquals(30, newRect.height(), "Height should be updated");
    }

}
