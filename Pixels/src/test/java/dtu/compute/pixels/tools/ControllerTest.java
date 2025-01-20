package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.Observer;
import dtu.compute.pixels.controller.tools.*;
import dtu.compute.pixels.model.*;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    @DisplayName("Full Interaction Scenario with Multiple Tools and Undo")
    void fullInteractionScenarioWithMultipleToolsAndUndo() {
        // Initialize the controller with an image
        Controller ctrl = new Controller();
        Image img = new Image(new Rect(10, 10));
        ctrl.setImage(img);

        // Add an observer to the controller
        TestObserver observer = new TestObserver();
        ctrl.addObserver(observer);

        // Set up the pen tool and draw
        Pen pen = new Pen();
        ctrl.setTool(pen);
        ctrl.setColor(Color.BLACK);
        ctrl.setActiveColor(MouseButton.PRIMARY);
        ctrl.click(new Point(1, 1));

        // Set up the eraser tool and erase
        Eraser eraser = new Eraser();
        ctrl.setTool(eraser);
        ctrl.click(new Point(1, 1));

        // Test cropping the image
        ctrl.cropImage(2, 2, 2, 2);

        // Test undo action after crop
        ctrl.undoLastAction();

        // Verify the observer was notified of changes
        assertTrue(observer.hasBeenNotified(), "Observer should be notified after changes");

        // Verify the image size after undoing the crop
        assertEquals(new Rect(10, 10), ctrl.getImage().getSize(), "Image should be original size after undo");

        // Verify the pixel is transparent after erase and undo crop
        assertEquals(Color.TRANSPARENT, ctrl.getImage().getPixel(new Point(1, 1)),
                "Pixel should be transparent after erase");

        // Test setting and getting active color
        ctrl.setColor(Color.RED);
        ctrl.setActiveColor(MouseButton.SECONDARY);
        assertEquals(Color.RED, ctrl.getColor(), "Active color should be red after setting.");

        // Test grid visibility toggle
        assertFalse(ctrl.isGridVisible(), "Initially, the grid should not be visible.");
        ctrl.setGridVisible(true);
        assertTrue(ctrl.isGridVisible(), "Grid should be visible after setting it to true.");

        // Ensure that the 'abandon' method of the current tool is covered by simulating
        // an incomplete action
        ctrl.press(new Point(3, 3));
        ctrl.abandon(); // Simulate abandoning the drawing action
        assertFalse(pen.isActive(), "Pen tool should not be active after abandon.");

        // Remove observer and ensure it's no longer notified
        assertTrue(ctrl.removeObserver(observer), "Observer should be successfully removed.");

        // Reset the observer notification status
        observer.resetNotified();

        // Trigger a change that should not notify the removed observer
        ctrl.setColor(Color.GREEN);

        // Verify that the observer was not notified after being removed
        assertFalse(observer.hasBeenNotified(), "Observer should not be notified after being removed.");

        // Test additional methods for coverage
        ctrl.setLastUsedTool(eraser);
        ctrl.addToHistoryBasedOnTool();
        assertEquals(eraser, ctrl.getTool(), "Eraser should be the last used tool after setting.");
    }

    // Auxiliary class to test observer notifications
    private static class TestObserver implements Observer {
        private boolean notified = false;

        @Override
        public void onChange() {
            notified = true;
        }

        public boolean hasBeenNotified() {
            return notified;
        }

        public void resetNotified() {
            notified = false;
        }
    }

    @Test
    @DisplayName("Test add to history before loading image")
    void testAddToHistoryBeforeLoadingImage() {
        Controller ctrl = new Controller();
        // Create an image and set it to the controller
        Image originalImage = new Image(new Rect(10, 10));
        ctrl.setImage(originalImage);

        // Simulate adding to history before loading a new image
        ctrl.addToHistoryBeforeLoadingImage();

        // Load a new image to the controller
        Image newImage = new Image(new Rect(20, 20));
        ctrl.setImage(newImage);

        // Undo the action of setting the new image
        ctrl.undoLastAction();

        // Check if the image size after undoing is the same as the original image
        assertEquals(originalImage.getSize(), ctrl.getImage().getSize(),
                "Image should revert to original size after undo.");
    }

    @Test
    @DisplayName("Get preview point after updating with tool")
    void getPreviewPointAfterUpdate() {
        Controller ctrl = new Controller();
        ctrl.setImage(new Image(new Rect(10, 10)));
        Pen pen = new Pen();
        ctrl.setTool(pen);
        Point expectedPoint = new Point(5, 5);

        // Simulate the action that would set the preview point
        ctrl.updatePreview(expectedPoint);

        // Verify that getPreviewPoint returns the correct value
        assertEquals(expectedPoint, ctrl.getPreviewPoint(), "Preview point should match the last updated point.");
    }
/* 
    @Test
    @DisplayName("Tool activity status")
    void toolActivityStatus() {
        Controller ctrl = new Controller();
        Image img = new Image(new Rect(10, 10));
        ctrl.setImage(img);
        
        // Ensure Color.BLACK is not null
        assertNotNull(Color.BLACK, "Color.BLACK should not be null.");
        
        // Set the color to a non-null value
        ctrl.setColor(Color.BLACK);
    
        // Initialize and set the pen tool
        Pen pen = new Pen();
        ctrl.setTool(pen);
        
        // Simulate the tool being used
        ctrl.press(new Point(1, 1));
        
        // Now, the tool should be active
        assertTrue(ctrl.isToolActive(), "Tool should be active after being pressed.");
    
        // Simulate releasing the tool
        ctrl.release(new Point(1, 1));
    
        // After release, the tool should not be active
        assertFalse(ctrl.isToolActive(), "Tool should not be active after release.");
    }  
    */  
}
