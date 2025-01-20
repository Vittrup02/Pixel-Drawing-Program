package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuBarFactory {

        public static MenuBar create(Stage stage, Controller ctrl) {
            final MenuBar bar = new MenuBar();

            MenuController mctrl = new MenuController(ctrl);

            Menu fileMenu = createFileMenu(stage, mctrl);
            Menu help = new Menu("Help");
            Menu undoMenu = new Menu("Undo");

            undoMenu.getItems().addAll(
            createUndoMenuItem(mctrl)
            );

            bar.getMenus().addAll(fileMenu, help, undoMenu);
            bar.setUseSystemMenuBar(true); 

            return bar;
        }

        private static Menu createFileMenu(Stage stage, MenuController mctrl) {
            Menu file = new Menu("File");
            file.getItems().setAll(
                createNewImageMenuItem(mctrl),
                createLoadImageMenuItem(stage, mctrl),
                createSaveImageMenuItem(mctrl),
                createCropImageMenuItem(mctrl));
            return file;
        }

        private static MenuItem createLoadImageMenuItem(Stage stage, MenuController mctrl) {
            MenuItem menuItem = new MenuItem("Load Image");
            menuItem.setOnAction(event -> mctrl.onLoadImage(stage, event));
            return menuItem;
        }

        private static MenuItem createNewImageMenuItem(MenuController mctrl) {
            MenuItem newImage = new MenuItem("New Image");
            newImage.setOnAction(event -> mctrl.onNewImage(event));
            return newImage;
        }

        private static MenuItem createSaveImageMenuItem(MenuController mctrl) {
            MenuItem saveImage = new MenuItem("Save Image");
            saveImage.setOnAction(event -> mctrl.onSaveImage(event));
            return saveImage;
        }

        private static MenuItem createUndoMenuItem(MenuController mctrl) {
            MenuItem undoAction = new MenuItem("Undo Action");
            undoAction.setOnAction(event -> mctrl.undoLastAction(event));
            return undoAction;
        }

        private static MenuItem createCropImageMenuItem(MenuController mctrl) {
            MenuItem cropImage = new MenuItem("Crop Image");
            cropImage.setOnAction(event -> mctrl.onCropImage(event));
            return cropImage;
        }
        
    
    private static class MenuController {
        final Controller ctrl;

            private MenuController(Controller ctrl) {
                this.ctrl = ctrl;
            }

            private void onNewImage(ActionEvent event) {
                var dialog = new Dialog<>();
                dialog.setTitle("New Image");
                dialog.setContentText("Create a new image.");
                dialog.getDialogPane()
                    .getButtonTypes()
                    .add(ButtonType.APPLY);
                dialog.getDialogPane()
                    .getButtonTypes()
                    .add(ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.add(new Label("Width:"), 0, 0);
                TextField widthInput = new TextField("32");
                gridPane.add(widthInput, 1, 0);

                gridPane.add(new Label("Height:"), 0, 1);
                TextField heightInput = new TextField("32");
                gridPane.add(heightInput, 1, 1);
                dialog.getDialogPane()
                    .setContent(gridPane);

                dialog.showAndWait()
                    .ifPresent(response -> {
                        if (response instanceof ButtonType
                            && ((ButtonType) response).getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                            return;
                        }
                        try {
                            int width = Integer.parseInt(widthInput.getText());
                            int height = Integer.parseInt(heightInput.getText());
                            ctrl.setImage(new Image(new Rect(width, height)));
                        } catch (NumberFormatException e) {
                            error("Width and Height can only be integers");
                        }
                    });
            }

            public void onLoadImage(Stage stage, ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load Image");
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    try {
                        Image newImage = ImageUtils.readPNG(selectedFile);
            
                        // Load image edited to be included in the undo function
                        ctrl.addToHistoryBeforeLoadingImage();
            
                        ctrl.setImage(newImage);
                    } catch (IOException e) {
                        error("Error loading image: " + e.getMessage());
                    }
                } 
            }
            

        public void onSaveImage(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                try {
                    ImageUtils.writePNG(selectedFile, ctrl.getImage());
                } catch (IOException e) {
                    error("Error saving image: " + e.getMessage());
                }
            }
        }

        private static void error(String s) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(s);
            alert.showAndWait();
        }
        
        public void undoLastAction(ActionEvent event) {
            ctrl.undoLastAction();
        }

        private void onCropImage(ActionEvent event) {
            var dialog = new Dialog<>();
            dialog.setTitle("Crop Image");
        
            // Display current image size
            Rect currentSize = ctrl.getImage().getSize();
            String sizeInfo = "Current Image Size: " + currentSize.width() + " x " + currentSize.height();
            Label sizeLabel = new Label(sizeInfo);
        
            // Inputs for crop values
            GridPane gridPane = new GridPane();
            TextField topInput = new TextField("0");
            TextField bottomInput = new TextField("0");
            TextField leftInput = new TextField("0");
            TextField rightInput = new TextField("0");
        
            gridPane.add(sizeLabel, 0, 0, 2, 1); // Span two columns
            gridPane.add(new Label("Top:"), 0, 1);
            gridPane.add(topInput, 1, 1);
            gridPane.add(new Label("Bottom:"), 0, 2);
            gridPane.add(bottomInput, 1, 2);
            gridPane.add(new Label("Left:"), 0, 3);
            gridPane.add(leftInput, 1, 3);
            gridPane.add(new Label("Right:"), 0, 4);
            gridPane.add(rightInput, 1, 4);
        
            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        int top = Integer.parseInt(topInput.getText());
                        int bottom = Integer.parseInt(bottomInput.getText());
                        int left = Integer.parseInt(leftInput.getText());
                        int right = Integer.parseInt(rightInput.getText());
        
                        // Call controller to crop the image
                        ctrl.cropImage(top, bottom, left, right);
                    } catch (NumberFormatException e) {
                        error("Invalid input. Please enter valid integers.");
                    } catch (IllegalArgumentException e) {
                        error(e.getMessage());
                    }
                }
            });
        }        
        
    }  
}