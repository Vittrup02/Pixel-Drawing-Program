package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import javafx.scene.input.MouseButton;
import java.io.IOException;

public class UndoTest {

    @Test
    @DisplayName("can undo the last action")
    void canUndoLastAction() throws IOException {
    Image img = new Image(new Rect(20, 20));
        // Set up the controller with the Pen tool
        Controller ctrl = new Controller().setTool(new Pen()).setImage(img);

        // Draw something on the image
        ctrl.setColor(Color.RED); // Set the active color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to draw
        ctrl.press(new Point(2, 2));
        ctrl.release(new Point(2, 2));
    
        // Clone the image buffer before undoing
        int[] bufferBeforeUndo = img.getBuffer().clone();
    
        // Perform the undo action
        ctrl.undoLastAction();
            // Ensure that the image buffer is reverted to its previous state
    assertArrayEquals(bufferBeforeUndo, img.getBuffer(), "The image buffer should be reverted to its previous state after undoing");
}
}
    
