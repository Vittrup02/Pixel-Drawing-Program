package dtu.compute.pixels.controller;

import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.model.ActionState;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Controller {

  private final List<Observer> observers;
  private Image image;
  private Image scratch;
  private Tool tool;
  private Color color;
  private Color color2;
  private Color activeColor;

  private Point previewPoint;
  private static final int MAX_HISTORY_SIZE = 50; // Max size for undo history storing
  private Stack<ActionState> actionHistory = new Stack<>(); // Undo History
  private Tool lastUsedTool;

  public Controller() {
    observers = new ArrayList<>();
    setImage(new Image(new Rect(32, 32)));
    color = Color.BLACK;
    color2 = Color.WHITE;
  }

  public Image getImage() {
    return image;
  }

  public Controller setImage(Image image) {
    this.image = image;
    scratch = new Image(image.getSize());
    notifyChange();
    return this;
  }

  public Color getColor() {
    return color;
  }

  public Controller setColor(Color color) {
    this.color = color;
    notifyChange();
    return this;
  }

  public Color getColor2() {
    return color2;
  }

  public Controller setColor2(Color color) {
    this.color2 = color;
    notifyChange();
    return this;
  }

  public Controller press(Point point) {
    tool.press(this, point);
    return this;
  }

  public Controller abandon() {
    tool.abandon(this);
    return this;
  }

  public Controller release(Point point) {
    tool.release(this, point);
    return this;
  }

  public Controller update(Point point) {
    tool.update(this, point);
    return this;
  }

  public Controller click(Point point) {
    tool.press(this, point);
    tool.release(this, point);
    return this;
  }

  public void updatePreview(Point point) {
    this.previewPoint = point;
    notifyChange(); // This will trigger the observer's onChange method
  }

  public Point getPreviewPoint() {
    return previewPoint;
  }

  /**
   * Paint the scratch over the current image.
   */
  public void commitScratch() {
    this.image.paintOverWith(this.scratch);
    this.resetScratch(true);
  }

  /**
   * Set the scratch image all transparent.
   *
   * @param notify notify observers that stuff has changed.
   */
  public void resetScratch(boolean notify) {
    this.scratch.reset(Color.TRANSPARENT);
    if (notify) {
      this.notifyChange();
    }
  }

  /**
   * Sets a pixel on the scratch image.
   *
   * @param point
   * @param color
   */
  public void setScratchPixel(Point point, Color color) {
    this.scratch.setPixel(point, color);
    this.notifyChange();
  }

  /**
   * Part of the observer pattern Adds an observer from the list of observers.
   *
   * @param observer, the observer to be notified on change.
   */
  public Controller addObserver(Observer observer) {
    observers.add(observer);
    return this;
  }

  /**
   * Part of the observer pattern. Removes an observer from the list of observers.
   *
   * @param observer, the observer to be no longer notified on change.
   * @return a boolean indicating if the observer was in the list.
   */
  public boolean removeObserver(Observer observer) {
    return observers.remove(observer);
  }

  /**
   * Part of the observer pattern. Notify all observers that the state was
   * changed.
   */
  public void notifyChange() {
    for (Observer o : observers) {
      o.onChange();
    }
  }

  public Image getScratch() {
    return scratch;
  }

  public Tool getTool() {
    return tool;
  }

  public Controller setTool(Tool t) {
    this.tool = t;
    notifyChange();
    return this;
  }

  // Undo Start
  public void addToHistoryBasedOnTool() {
    if (actionHistory.size() >= MAX_HISTORY_SIZE) {
      actionHistory.remove(0); // Remove the oldest entry
    }
    actionHistory.push(new ActionState(getImage().clone(), tool));
  }

  public void addToHistoryBeforeLoadingImage() {
    actionHistory.push(new ActionState(getImage().clone(), lastUsedTool));

    // Ensure the history size limit is respected
    if (actionHistory.size() >= MAX_HISTORY_SIZE) {
      actionHistory.remove(0);
    }
  }

  public Controller undoLastAction() {
    if (!actionHistory.isEmpty()) {
      ActionState lastAction = actionHistory.pop();
      setImage(lastAction.getImageState());
      lastUsedTool = lastAction.getTool(); // Update last used tool if needed
    }
    notifyChange();
    return this;
  }

  public void setLastUsedTool(Tool tool) {
    this.lastUsedTool = tool;
  }
  // Undo End

  public void erasePixelAt(Point point) {
    this.image.setPixel(point, Color.TRANSPARENT);
    notifyChange();
  }

  public void setPixel(Point point, Color color) {
    this.image.setPixel(point, color);
    this.notifyChange();
  }

  // Grid start
  private boolean gridVisible = false;

  public boolean isGridVisible() {
    return gridVisible;
  }

  public void setGridVisible(boolean gridVisible) {
    this.gridVisible = gridVisible;
    notifyChange();
  }

  // Grid end
  public void cropImage(int top, int bottom, int left, int right) {
    // Save current state for undo functionality
    addToHistoryBasedOnTool();
    Image currentImage = getImage();
    Rect currentSize = currentImage.getSize();

    // Calculate new size after cropping
    int newWidth = currentSize.width() - left - right;
    int newHeight = currentSize.height() - top - bottom;
    if (newWidth <= 0 || newHeight <= 0) {
      throw new IllegalArgumentException("Invalid crop dimensions.");
    }

    // Create a new image with the cropped size
    Image croppedImage = new Image(new Rect(newWidth, newHeight));

    // Copy pixels from the original image to the cropped image
    for (int y = top; y < currentSize.height() - bottom; y++) {
      for (int x = left; x < currentSize.width() - right; x++) {
        Color pixelColor = currentImage.getPixel(new Point(x, y));
        croppedImage.setPixel(new Point(x - left, y - top), pixelColor);
      }
    }

    // Set the cropped image as the current image
    setImage(croppedImage);
    notifyChange();
  }

  public boolean isToolActive() {
    return tool != null && tool.isActive();
  }

  public void setActiveColor(MouseButton button) {
    if (button == MouseButton.PRIMARY) {
      activeColor = color;
    } else if (button == MouseButton.SECONDARY) {
      activeColor = color2;
    }
  }

  public Color getActiveColor() {
    return activeColor;
  }
}