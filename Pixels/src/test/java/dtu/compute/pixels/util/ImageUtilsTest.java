package dtu.compute.pixels.util;

import static org.junit.jupiter.api.Assertions.*;

import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;


class ImageUtilsTest {

  @Test
  @DisplayName("writing and reading is the same")
  void writingAndReadingIsTheSame() throws IOException {
    Image image = Image.pretty(new Rect(32,32));

    TestUtils.goldenTest("pretty", image);
  }

  @Test
@DisplayName("Convert to BufferedImage and verify")
void convertToBufferedImageAndVerify() {
    Image image = Image.pretty(new Rect(32, 32));
    BufferedImage bufferedImage = ImageUtils.asBufferedImage(image);

    assertEquals(image.getSize().width(), bufferedImage.getWidth());
    assertEquals(image.getSize().height(), bufferedImage.getHeight());

    // Additional checks for pixel data could be added here
}

@Test
@DisplayName("Convert to JavaFX Image and verify")
void convertToJavaFXImageAndVerify() {
    Image image = Image.pretty(new Rect(32, 32));
    javafx.scene.image.Image javafxImage = ImageUtils.asJavaFXImage(image);

    assertEquals(image.getSize().width(), (int) javafxImage.getWidth());
    assertEquals(image.getSize().height(), (int) javafxImage.getHeight());

    // Additional checks for pixel data could be added here
}

}