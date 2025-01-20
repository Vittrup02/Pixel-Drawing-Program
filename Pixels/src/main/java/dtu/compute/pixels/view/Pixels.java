package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.Observer;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ColorUtils;
import dtu.compute.pixels.util.ImageUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Pixels extends Application implements Observer {

  private final static Rect START_SIZE = new Rect(1000, 700);
  private final static Rect CANVAS_SIZE = new Rect(600, 600);
  private final Controller ctrl;
  private Canvas canvas;
  private Scene scene;

  public Pixels() {
    ctrl = new Controller()
        .setColor(Color.fromARGB(0xff000000))
        .setTool(new Pen())
        .setImage(new Image(new Rect(32, 32)));
  }

  public static void main(String[] args) {
    // Calls the Javafx launch method, which in turn will call start()
    launch(args);
  }

  /**
   * The start method, which sets up the scene.
   *
   * @param stage the primary stage for this application, onto which the
   *              application scene can be
   *              set. Applications may create other stages, if needed, but they
   *              will not be primary
   *              stages.
   */
  @Override
  public void start(Stage stage) {
    final BorderPane layout = new BorderPane();

    layout.setTop(MenuBarFactory.create(stage, ctrl));

    canvas = createCanvas(CANVAS_SIZE);
    layout.setCenter(canvas);

    // Setup toolbar
    layout.setBottom(ToolBarFactory.create(ctrl, scene, canvas));

    Scene scene = new Scene(layout, START_SIZE.width(), START_SIZE.height(),
        javafx.scene.paint.Color.GRAY);

    stage.setTitle("Pixels");
    stage.setScene(scene);
    stage.show();

    ctrl.addObserver(this);
  }

  private Canvas createCanvas(Rect size) {
    Canvas view = new Canvas(size.width(), size.height());
    final var context = view.getGraphicsContext2D();
    context.setImageSmoothing(false);

    view.setOnMousePressed(e -> {
      ctrl.setActiveColor(e.getButton());
      ctrl.press(getPointFromEvent(size, e));
    });

    view.setOnMouseReleased(e -> {
      ctrl.release(getPointFromEvent(size, e));
    });

    // In your mouse event handlers
    view.setOnMouseDragged(e -> {
      Point point = getPointFromEvent(size, e);
      ctrl.update(point);
      ctrl.updatePreview(point);
    });

    view.setOnMouseMoved(e -> ctrl.update(getPointFromEvent(size, e)));

    return view;
  }

  private Point getPointFromEvent(Rect size, MouseEvent e) {
    Rect bufferSize = ctrl.getImage().getSize();
    double scaleX = size.width() / (double) bufferSize.width();
    double scaleY = size.height() / (double) bufferSize.height();

    int canvasX = (int) Math.floor(e.getX());
    int canvasY = (int) Math.floor(e.getY());

    // Convert canvas coordinates back to image coordinates
    int imageX = (int) Math.floor(canvasX / scaleX);
    int imageY = (int) Math.floor(canvasY / scaleY);

    return new Point(imageX, imageY);
  }

  public void redraw() {
    GraphicsContext ctx = canvas.getGraphicsContext2D();
    ctx.setFill(Paint.valueOf("white"));
    ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    javafx.scene.image.Image mainImg = ImageUtils.asJavaFXImage(ctrl.getImage());
    double scale = Math.min(canvas.getWidth() / mainImg.getWidth(), canvas.getHeight() / mainImg.getHeight());
    double scaledWidth = mainImg.getWidth() * scale;
    double scaledHeight = mainImg.getHeight() * scale;
    double x = (canvas.getWidth() - scaledWidth) / 2;
    double y = (canvas.getHeight() - scaledHeight) / 2;
    ctx.drawImage(mainImg, x, y, scaledWidth, scaledHeight);

    // Draw the scratch image for tool preview
    if (ctrl.isToolActive()) {
      javafx.scene.image.Image scratchImg = ImageUtils.asJavaFXImage(ctrl.getScratch());
      ctx.drawImage(scratchImg, x, y, scaledWidth, scaledHeight);
    }

 // Draw the grid if it's enabled
 if (ctrl.isGridVisible()) {
  drawGrid(ctx, canvas.getWidth() / ctrl.getImage().getSize().width());
}

    // Draw the border around the image
    double borderWidth = 1; // Set the width of the border
    ctx.setStroke(javafx.scene.paint.Color.BLACK); // Set border color
    ctx.setLineWidth(borderWidth);
    ctx.strokeRect(x - borderWidth / 2, y - borderWidth / 2, scaledWidth + borderWidth, scaledHeight + borderWidth);
  }
  private void drawGrid(GraphicsContext gc, double zoom) {
    gc.setStroke(javafx.scene.paint.Color.GRAY);
    double canvasWidth = canvas.getWidth();
    double canvasHeight = canvas.getHeight();

    int cellSize = 1; // Fixed cell size, representing 1 pixel

    // Adjust cellSize based on zoom level if needed
    double adjustedCellSize = cellSize * zoom;

    // Draw the grid lines
    for (double x = 0; x <= canvasWidth; x += adjustedCellSize) {
      gc.strokeLine(x, 0, x, canvasHeight);
    }
    for (double y = 0; y <= canvasHeight; y += adjustedCellSize) {
      gc.strokeLine(0, y, canvasWidth, y);
    }
    if (ctrl.getPreviewPoint() != null) {
    }
  }


  @Override
  public void onChange() {
    redraw();
  }
}
