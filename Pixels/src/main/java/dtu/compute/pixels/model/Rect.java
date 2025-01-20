package dtu.compute.pixels.model;

public record Rect(int width, int height) {

    public int area() {
        return width * height;
    }

    public Point into(Rect from, Point p) {
        int px = Math.round(p.x() / from.width * width);
        int py = Math.round(p.y() / from.height * height);
        return new Point(px, py);
    }

    public Rect withWidth(int width) {
        return new Rect(width, height);
    }

    public Rect withHeight(int height) {
        return new Rect(width, height);
    }


}
