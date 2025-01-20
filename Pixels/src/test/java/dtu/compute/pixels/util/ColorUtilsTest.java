package dtu.compute.pixels.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.util.ColorUtils;

public class ColorUtilsTest {

    @Test
    void testFromColor() {
        
        // Assuming 0x64 corresponds to 100 in decimal
        Color customColor = Color.fromARGB(0x64FFA07A);
        javafx.scene.paint.Color javafxColor = ColorUtils.fromColor(customColor);
        assertEquals(100 / 255.0, javafxColor.getOpacity(), 0.01, "Alpha values should match");
        
        // Test with a semi-transparent red
        Color customColorRed = Color.fromARGB(0x64FF0000);
        javafx.scene.paint.Color javafxColorRed = ColorUtils.fromColor(customColorRed);
        assertEquals(100 / 255.0, javafxColorRed.getOpacity(), 0.01, "Alpha values should match for red");
        assertEquals(1.0, javafxColorRed.getRed(), 0.01, "Red values should match");
        assertEquals(0.0, javafxColorRed.getGreen(), 0.01, "Green values should be 0 for red");
        assertEquals(0.0, javafxColorRed.getBlue(), 0.01, "Blue values should be 0 for red");

        // Test with a semi-transparent green
        Color customColorGreen = Color.fromARGB(0x6400FF00);
        javafx.scene.paint.Color javafxColorGreen = ColorUtils.fromColor(customColorGreen);
        assertEquals(100 / 255.0, javafxColorGreen.getOpacity(), 0.01, "Alpha values should match for green");
        assertEquals(0.0, javafxColorGreen.getRed(), 0.01, "Red values should be 0 for green");
        assertEquals(1.0, javafxColorGreen.getGreen(), 0.01, "Green values should match");
        assertEquals(0.0, javafxColorGreen.getBlue(), 0.01, "Blue values should be 0 for green");

        // Test with a semi-transparent blue
        Color customColorBlue = Color.fromARGB(0x640000FF);
        javafx.scene.paint.Color javafxColorBlue = ColorUtils.fromColor(customColorBlue);
        assertEquals(100 / 255.0, javafxColorBlue.getOpacity(), 0.01, "Alpha values should match for blue");
        assertEquals(0.0, javafxColorBlue.getRed(), 0.01, "Red values should be 0 for blue");
        assertEquals(0.0, javafxColorBlue.getGreen(), 0.01, "Green values should be 0 for blue");
        assertEquals(1.0, javafxColorBlue.getBlue(), 0.01, "Blue values should match");
    }

    @Test
    void testToColor() {
        // Test converting a javafx color to a custom Color object
        javafx.scene.paint.Color javafxColor = new javafx.scene.paint.Color(1.0, 0.0, 0.0, 1.0); // pure red with full opacity
        Color customColor = ColorUtils.toColor(javafxColor);
        assertEquals(0xFFFF0000, customColor.toARGB(), "Color should match full opacity red");

        // Example for a semi-transparent blue
        javafx.scene.paint.Color javafxColorBlue = new javafx.scene.paint.Color(0.0, 0.0, 1.0, 0.5); // semi-transparent blue
        Color customColorBlue = ColorUtils.toColor(javafxColorBlue);
        assertEquals(0x800000FF, customColorBlue.toARGB(), "Color should match semi-transparent blue");

    }
}
