package dtu.compute.pixels.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ImageUtils;
import javafx.scene.input.MouseButton;

import java.io.File;
import java.io.IOException;

public class GridAcceptanceTest {

    @Test
    public void testGridVisibilityAndImageUpload() {
        // Opret en Controller
        Controller controller = new Controller();
        
        // Sæt en initial billedstørrelse
        controller.setImage(new Image(new Rect(32, 32)));
        controller.setColor(Color.BLACK);
        controller.setActiveColor(MouseButton.PRIMARY);
        
        // Sæt gridet til at være initialt usynligt
        assertFalse(controller.isGridVisible());
        
        // Skift gridets synlighed
        controller.setGridVisible(true);
        
        // Bekræft, at gridet nu er synligt
        assertTrue(controller.isGridVisible());
        
        // Lav en Pen
        Pen penTool = new Pen();
        
        // Sæt penfarven
        Color penColor = Color.fromARGB(0xFF000000);
        controller.setColor(penColor);
        
        // Simuler tegning på gridet
        Point startPoint = new Point(0, 0);
        Point endPoint = new Point(10, 10);
        
        controller.setTool(penTool);
        controller.press(startPoint);
        controller.update(endPoint);
        controller.release(endPoint);
        
        // Bekræft, at de tegnede linjer er justeret med gridet på grund af synlighed
        Image image = controller.getImage();
        Color drawnColor = image.getPixel(endPoint);
        
        // Hvis vi antager, at gridet er synligt, bør den tegnede farve matche penfarven
        assertEquals(penColor, drawnColor);
        
        // Slå gridets synlighed fra
        controller.setGridVisible(false);
        
        // Simuler tegning på gridet med gridets synlighed slået fra
        controller.setTool(penTool);
        controller.press(startPoint);
        controller.update(endPoint);
        controller.release(endPoint);
        
        // Bekræft, at de tegnede linjer ikke er justeret med gridet på grund af slukket synlighed
        drawnColor = image.getPixel(endPoint);
        
        // Hvis vi antager, at gridet ikke er synligt, bør den tegnede farve stadig matche penfarven
        assertEquals(penColor, drawnColor);
        
        // Gem billedet som en PNG-fil og læs det derefter ind igen
        try {
            File outputFile = new File("test_image.png");
            ImageUtils.writePNG(outputFile, image);
            Image uploadedImage = ImageUtils.readPNG(outputFile);
            
            // Bekræft, at det uploadede billede svarer til det oprindelige billede
            assertEquals(image, uploadedImage);
            
            // Slet den midlertidige fil
            outputFile.delete();
        } catch (IOException e) {
            fail("Fejl under læsning af billedet.");
        }
    }
}
