package dtu.compute.pixels.model;

import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImageTest {

  @Test
    @DisplayName("Test equality of two identical images")
    void testEqualImages() {
        Image image1 = new Image(new Rect(3, 3));
        Image image2 = new Image(new Rect(3, 3));
        assertEquals(image1, image2, "Identical images should be equal");
    }

    @Test
    @DisplayName("Test equality with different images")
    void testNonEqualImages() {
        Image image1 = new Image(new Rect(3, 3));
        Image image2 = new Image(new Rect(4, 4));
        assertNotEquals(image1, image2, "Images with different sizes should not be equal");
    }

    @Test
    @DisplayName("Test override with matching sizes")
    void testOverrideMatchingSizes() {
        Image image1 = new Image(new Rect(3, 3));
        Image image2 = new Image(new Rect(3, 3));
        // Assuming some method to modify image2
        // ...
        image1.override(image2);
        assertEquals(image1, image2, "Images should be equal after override with matching sizes");
    }

    @Test
    @DisplayName("Test override with mismatched sizes")
    void testOverrideMismatchedSizes() {
        Image image1 = new Image(new Rect(3, 3));
        Image image2 = new Image(new Rect(4, 4));
        assertThrows(IllegalArgumentException.class, () -> image1.override(image2),
            "Should throw IllegalArgumentException for different sized images");
    }


  @Test
  @DisplayName("can paint a specific pixel")
  void canPaintASpecificPixel() throws IOException {
    Image image = new Image(new Rect(3, 3));

    image.reset(Color.WHITE);

    // set the middle pixel red.
    image.setPixel(new Point(1, 1), Color.RED);

    TestUtils.goldenTest("single-pixel", image);
  }

  @Test
  @DisplayName("can fill the entire image with a color")
  void canFillEntireImageWithColor() throws IOException {
    Image image = new Image(new Rect(4, 4));
    Color fillColor = Color.GREEN;

    image.fill(fillColor);

    for (int x = 0; x < image.getSize().width(); x++) {
      for (int y = 0; y < image.getSize().height(); y++) {
        assertEquals(fillColor, image.getPixel(new Point(x, y)));
      }
    }

    TestUtils.goldenTest("fill-entire-image", image);
  }

  @Test
  @DisplayName("can draw a line on the image")
  void canDrawLineOnImage() throws IOException {
    Image image = new Image(new Rect(5, 5));
    Color lineColor = Color.BLUE;

    image.drawLine(new Point(1, 1), new Point(4, 4), lineColor);

    // Check if the pixels on the line have the expected color
    for (int i = 1; i <= 4; i++) {
      assertEquals(lineColor, image.getPixel(new Point(i, i)));
    }

    TestUtils.goldenTest("draw-line", image);
  }

  @Test
    @DisplayName("Test drawSquare method")
    void testDrawSquare() throws IOException {
        // Assuming the existence of a constructor for Image that takes width and height
        Image image = new Image(new Rect(10, 10)); 
        Color squareColor = Color.BLUE;
        Point start = new Point(2, 2);
        Point end = new Point(5, 5);

        // Drawing the square
        image.drawSquare(start, end, squareColor);

        // Asserting that the square is drawn correctly
        for (int x = start.x(); x <= end.x(); x++) {
            for (int y = start.y(); y <= end.y(); y++) {
                assertEquals(squareColor, image.getPixel(new Point(x, y)), 
                    String.format("Pixel at (%d, %d) should be set to square color", x, y));
            }
          }
        TestUtils.goldenTest("draw-square", image);
    }

  @Test
  @DisplayName("can draw a square outline on the image")
  void canDrawSquareOutlineOnImage() throws IOException {
    Image image = new Image(new Rect(5, 5));
    Color outlineColor = Color.RED;

    image.drawSquareOutline(new Point(1, 1), new Point(3, 3), outlineColor);

    // Check if the pixels on the square outline (1-pixel thick) have the expected
    // color
    for (int i = 1; i <= 3; i++) {
      assertEquals(outlineColor, image.getPixel(new Point(i, 1)));
      assertEquals(outlineColor, image.getPixel(new Point(i, 3)));
      assertEquals(outlineColor, image.getPixel(new Point(1, i)));
      assertEquals(outlineColor, image.getPixel(new Point(3, i)));
    }
    TestUtils.goldenTest("draw-square-outline", image);
  }

  @Test
    @DisplayName("Test drawOvalOutline method")
    void testDrawOvalOutline() throws IOException {
      Image image = new Image(new Rect(40, 40)); // Assuming a constructor for Image
      Color ovalColor = Color.BLUE;
      Point start = new Point(10, 10);
      Point end = new Point(30, 30);

      // Drawing the larger oval outline
      image.drawOvalOutline(start, end, ovalColor);

      // Asserting certain properties of the larger oval
      // Note: This is still a basic check. Real tests would need more thorough checks.
      assertNotEquals(ovalColor, image.getPixel(new Point(20, 20)), "Center of the oval should not be colored");
      assertEquals(ovalColor, image.getPixel(new Point(20, 10)), "Top middle of the oval should be colored");
      assertEquals(ovalColor, image.getPixel(new Point(20, 30)), "Bottom middle of the oval should be colored");
      assertEquals(ovalColor, image.getPixel(new Point(10, 20)), "Left middle of the oval should be colored");
      assertEquals(ovalColor, image.getPixel(new Point(30, 20)), "Right middle of the oval should be colored");

        // Additional assertions can be added to ensure the shape of the oval is correct
        TestUtils.goldenTest("draw-oval-outline", image);
    }

}