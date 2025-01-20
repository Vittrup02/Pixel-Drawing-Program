package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.controller.tools.PolygonTool;
import dtu.compute.pixels.controller.tools.Pen.BrushType;
import dtu.compute.pixels.controller.tools.PipetteTool;
import dtu.compute.pixels.controller.tools.SquareTool;
import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.controller.tools.TriangleTool;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.controller.tools.FillTool;
import dtu.compute.pixels.controller.tools.LineTool;
import dtu.compute.pixels.controller.tools.OvalTool;
import dtu.compute.pixels.util.ColorUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import dtu.compute.pixels.controller.tools.StarTool;

public class ToolBarFactory {

    // Method to create the toolbar
    public static ToolBar create(Controller ctrl, Scene scene, Canvas canvas) {
        ToolBar tb = new ToolBar();

        // Create a MenuButton for shape tools
        MenuButton shapeToolsMenuButton = new MenuButton();
        shapeToolsMenuButton.setText("Shapes");

        MenuItem squareToolItem = createShapeMenuItem("Square",
                "https://icon-library.com/images/select-icon-png/select-icon-png-24.jpg", ctrl, new SquareTool());
        shapeToolsMenuButton.getItems().add(squareToolItem);

        MenuItem polygonToolItem = createShapeMenuItem("Polygon",
                "https://static-00.iconduck.com/assets.00/polygon-icon-512x512-lqhv11mq.png", ctrl, new PolygonTool());
        shapeToolsMenuButton.getItems().add(polygonToolItem);

        MenuItem ovalToolItem = createShapeMenuItem("Oval", "https://cdn-icons-png.flaticon.com/512/650/650140.png",
                ctrl, new OvalTool());
        shapeToolsMenuButton.getItems().add(ovalToolItem);

        MenuItem lineToolItem = createShapeMenuItem("Line", "https://cdn-icons-png.flaticon.com/512/92/92495.png", ctrl,
                new LineTool());
        shapeToolsMenuButton.getItems().add(lineToolItem);

        MenuItem triangleToolItem = createShapeMenuItem("Triangle",
                "https://cdn-icons-png.flaticon.com/512/33/33854.png", ctrl, new TriangleTool());
        shapeToolsMenuButton.getItems().add(triangleToolItem);

        MenuItem starToolItem = createShapeMenuItem("Star", "https://png.pngtree.com/png-vector/20190725/ourmid/pngtree-vector-star-icon-png-image_1577370.jpg", ctrl, new StarTool());
        shapeToolsMenuButton.getItems().add(starToolItem);

        MenuButton brushTypeMenu = new MenuButton();
        brushTypeMenu.setText("brushTypes");

        MenuItem sprayBrushTypeItem = createBrushTypeMenuItem("Spray",
        "https://cdn-icons-png.flaticon.com/512/96/96552.png", ctrl, Pen.BrushType.SPRAY);
        brushTypeMenu.getItems().add(sprayBrushTypeItem);

        MenuItem dottedBrushTypeItem = createBrushTypeMenuItem("Dotted",
        "https://cdn-icons-png.flaticon.com/512/17/17704.png", ctrl, Pen.BrushType.DOTTED);
        brushTypeMenu.getItems().add(dottedBrushTypeItem);

        MenuItem texturedBrushTypeItem = createBrushTypeMenuItem("Textured", 
        "https://cdn-icons-png.flaticon.com/512/2837/2837260.png", ctrl, Pen.BrushType.TEXTURED);
        brushTypeMenu.getItems().add(texturedBrushTypeItem);

        // Create color picker and add to toolbar
        ColorPicker picker = createColorPicker(ctrl);
        ColorPicker picker2 = createColorPicker2(ctrl);
        tb.getItems().add(picker);
        tb.getItems().add(picker2);

        // Effects
        Effect chosenEffect = new Bloom();

        // Creating toolbar buttons
        Image penImage = new Image("https://cdn-icons-png.flaticon.com/512/1860/1860115.png");
        ImageView penIconView = new ImageView(penImage);
        penIconView.setFitHeight(16); // Set icon size
        penIconView.setFitWidth(16);
        Button pen = new Button("", penIconView); // Pen button with icon

        Image eraserImage = new Image("https://cdn-icons-png.flaticon.com/512/1827/1827954.png");
        ImageView eraserIconView = new ImageView(eraserImage);
        eraserIconView.setFitHeight(16); // Set icon size
        eraserIconView.setFitWidth(16);
        Button eraser = new Button("", eraserIconView); // Eraser button with icon

        Image lineImage = new Image("https://cdn-icons-png.flaticon.com/512/92/92495.png");
        ImageView lineIconView = new ImageView(lineImage);
        lineIconView.setFitHeight(16);
        lineIconView.setFitWidth(16);
        Button line = new Button("", lineIconView);

        Button square = createSquareToolButton(ctrl);
        Button fillButton = createFillToolButton(ctrl);
        Button pipetteToolButton = createPipetteToolButton(ctrl);

        Button zoomOutButton = new Button();
        Image zoomOutIcon = new Image("https://simpleicon.com/wp-content/uploads/zoom-out.png");
        ImageView zoomOutIconView = new ImageView(zoomOutIcon);
        zoomOutIconView.setFitHeight(16);
        zoomOutIconView.setFitWidth(16);
        zoomOutButton.setGraphic(zoomOutIconView);

        Button zoomInButton = new Button();
        Image zoomInIcon = new Image("https://simpleicon.com/wp-content/uploads/zoom-in.png");
        ImageView zoomInIconView = new ImageView(zoomInIcon);
        zoomInIconView.setFitHeight(16);
        zoomInIconView.setFitWidth(16);
        zoomInButton.setGraphic(zoomInIconView);

        // Brush size slider
        Slider brushSizeSlider = new Slider(1, 10, 1); // min, max, initial value
        brushSizeSlider.setShowTickLabels(true);
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setMajorTickUnit(1);
        brushSizeSlider.setSnapToTicks(true);

        // Listener for brush size changes
        brushSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (ctrl.getTool() instanceof Pen) {
                ((Pen) ctrl.getTool()).setBrushSize(newVal.intValue());
            }
            if (ctrl.getTool() instanceof Eraser) {
                ((Eraser) ctrl.getTool()).setBrushSize(newVal.intValue());
            }
        });

        Image ovalImage = new Image("https://cdn-icons-png.flaticon.com/512/650/650140.png");
        ImageView ovalIconView = new ImageView(ovalImage);
        ovalIconView.setFitHeight(16);
        ovalIconView.setFitWidth(16);
        Button ovalToolButton = new Button("", ovalIconView);

        Image triangleImage = new Image("https://cdn-icons-png.flaticon.com/512/33/33854.png");
        ImageView triangleIconView = new ImageView(triangleImage);
        triangleIconView.setFitHeight(16);
        triangleIconView.setFitWidth(16);
        Button triangleToolButton = new Button("", triangleIconView);
        triangleToolButton.setOnAction(e -> {
            ctrl.setTool(new TriangleTool());
            ctrl.notifyChange();
        });

        Image polygonImage = new Image("https://static-00.iconduck.com/assets.00/polygon-icon-512x512-lqhv11mq.png");
        ImageView polygonIconView = new ImageView(polygonImage);
        polygonIconView.setFitHeight(16);
        polygonIconView.setFitWidth(16);
        Button polygonToolButton = new Button("", polygonIconView);
        polygonToolButton.setOnAction(e -> {
            ctrl.setTool(new PolygonTool());
            ctrl.notifyChange();
        });

        Image gridImage = new Image("https://static.thenounproject.com/png/185900-200.png");
        ImageView gridIconView = new ImageView(gridImage);
        gridIconView.setFitHeight(16);
        gridIconView.setFitWidth(16);
        Button gridButton = new Button("", gridIconView);


        tb.getItems().addAll(pipetteToolButton, pen, brushTypeMenu, eraser, brushSizeSlider, shapeToolsMenuButton, fillButton, gridButton, zoomInButton, zoomOutButton);

        // Action for pen button
        pen.setOnAction(e -> {
            ctrl.setTool(new Pen());
            pen.setEffect(chosenEffect);
            eraser.setEffect(null);
            line.setEffect(null);
            ctrl.notifyChange();
        });

        // Action for eraser button
        eraser.setOnAction(e -> {
            ctrl.setTool(new Eraser());
            eraser.setEffect(chosenEffect);
            pen.setEffect(null);
            line.setEffect(null);
            ctrl.notifyChange();
        });

        // Action for line button
        line.setOnAction(e -> {
            ctrl.setTool(new LineTool());
            line.setEffect(chosenEffect);
            pen.setEffect(null);
            eraser.setEffect(null);
            ctrl.notifyChange();
        });

        // Action for grid button
        gridButton.setOnAction(e -> {
            ctrl.setGridVisible(!ctrl.isGridVisible());
        });

        zoomOutButton.setOnAction(e -> {
            canvas.setScaleX(canvas.getScaleX() / 1.1);
            canvas.setScaleY(canvas.getScaleY() / 1.1);
        });

        zoomInButton.setOnAction(e -> {
            canvas.setScaleX(canvas.getScaleX() * 1.1);
            canvas.setScaleY(canvas.getScaleY() * 1.1);
        });

        // Observer for color change
        ctrl.addObserver(() -> {
            picker.setValue(ColorUtils.fromColor(ctrl.getColor()));
        });

        ctrl.addObserver(() -> {
            picker2.setValue(ColorUtils.fromColor(ctrl.getColor2()));
        });

        // Observer for tool change
        ctrl.addObserver(() -> {
            if (ctrl.getTool() instanceof Pen) {
                pen.setEffect(chosenEffect);
                eraser.setEffect(null);
            } else {
                if (ctrl.getTool() instanceof Eraser) {
                    eraser.setEffect(chosenEffect);
                    pen.setEffect(null);
                }
            }
        });

        ovalToolButton.setOnAction(e -> {
            ctrl.setTool(new OvalTool());
            ctrl.notifyChange();
        });
        return tb;
    }

    // Helper method to create a color picker
    private static ColorPicker createColorPicker(Controller ctrl) {
        ColorPicker picker = new ColorPicker();
        picker.getStyleClass().add("button");
        picker.setValue(ColorUtils.fromColor(ctrl.getColor()));
        picker.setOnAction(e -> {
            ctrl.setColor(ColorUtils.toColor(picker.getValue()));
            ctrl.notifyChange();
        });
        return picker;
    }

    private static ColorPicker createColorPicker2(Controller ctrl) {
        ColorPicker picker2 = new ColorPicker();
        picker2.getStyleClass().add("button");
        picker2.setValue(ColorUtils.fromColor(ctrl.getColor2())); // Use getColor2
        picker2.setOnAction(e -> {
            ctrl.setColor2(ColorUtils.toColor(picker2.getValue())); // Use setColor2
            ctrl.notifyChange();
        });
        return picker2;
    }

    private static Button createSquareToolButton(Controller ctrl) {
        Image squareImage = new Image("https://icon-library.com/images/select-icon-png/select-icon-png-24.jpg");
        ImageView squareIconView = new ImageView(squareImage);
        squareIconView.setFitHeight(16);
        squareIconView.setFitWidth(16);
        Button squareToolButton = new Button("", squareIconView);
        squareToolButton.setOnAction(e -> {
            ctrl.setTool(new SquareTool());
            ctrl.notifyChange();
        });
        return squareToolButton;
    }

    private static Button createFillToolButton(Controller ctrl) {
        Image fillImage = new Image("https://cdn-icons-png.flaticon.com/512/2708/2708370.png");
        ImageView fillIconView = new ImageView(fillImage);
        fillIconView.setFitHeight(16);
        fillIconView.setFitWidth(16);
        Button fillToolButton = new Button("", fillIconView);
        fillToolButton.setOnAction(e -> {
            ctrl.setTool(new FillTool());
            ctrl.notifyChange();
        });
        return fillToolButton;
    }

    private static Button createPipetteToolButton(Controller ctrl) {
        Image pipetteImage = new Image("https://cdn-icons-png.flaticon.com/512/483/483909.png");
        ImageView pipetteIconView = new ImageView(pipetteImage);
        pipetteIconView.setFitHeight(16);
        pipetteIconView.setFitWidth(16);
        Button pipetteToolButton = new Button("", pipetteIconView);

        // Action to set the pipette tool for the primary mouse button
        pipetteToolButton.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ctrl.setTool(new PipetteTool(MouseButton.PRIMARY));
            } else if (e.getButton() == MouseButton.SECONDARY) {
                ctrl.setTool(new PipetteTool(MouseButton.SECONDARY));
            }
            ctrl.notifyChange();
        });

        return pipetteToolButton;
    }

    private static MenuItem createShapeMenuItem(String label, String iconUrl, Controller ctrl, Tool tool) {
        MenuItem menuItem = new MenuItem(label);
        Image shapeImage = new Image(iconUrl);
        ImageView shapeIconView = new ImageView(shapeImage);
        shapeIconView.setFitHeight(16);
        shapeIconView.setFitWidth(16);
        menuItem.setGraphic(shapeIconView);
        menuItem.setOnAction(e -> {
            ctrl.setTool(tool);
            ctrl.notifyChange();
        });
        return menuItem;
    }

    private static MenuItem createBrushTypeMenuItem(String label, String iconUrl, Controller ctrl, BrushType brushType) {
        MenuItem menuItem = new MenuItem(label);
        Image shapeImage = new Image(iconUrl);
        ImageView shapeIconView = new ImageView(shapeImage);
        shapeIconView.setFitHeight(16);
        shapeIconView.setFitWidth(16);
        menuItem.setGraphic(shapeIconView);
        menuItem.setOnAction(e -> {
            Tool currentTool = ctrl.getTool();
            if (currentTool instanceof Pen) {
                ((Pen) currentTool).setBrushType(brushType);
                ctrl.notifyChange(); // Notify the UI to refresh
            }
        });
        return menuItem;
    }
    
}
