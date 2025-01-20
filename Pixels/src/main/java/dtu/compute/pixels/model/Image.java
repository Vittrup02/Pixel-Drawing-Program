package dtu.compute.pixels.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class Image implements Cloneable {

    private final Rect size;
    private final int[] buffer;

    public Image(Rect size) {
        this.size = size;
        this.buffer = new int[size.area()];
    }

    private Image(Rect size, int[] buffer) {
        this.size = size;
        this.buffer = buffer;
    }

    public static Image pretty(Rect rect) {
        Image image = new Image(rect);
        int i = 0;
        for (int y = 0; y < rect.height(); y++) {
            for (int x = 0; x < rect.width(); x++) {
                image.buffer[i++] = Color.fromInts(0xff, x * 255 / rect.width(), y * 255 / rect.height(), 0x00)
                        .toARGB();
            }
        }
        return image;
    }

    public void setPixel(Point point, Color color) {
        if (isPointInBounds(point)) {
            int i = size.width() * point.y() + point.x();
            buffer[i] = color.toARGB();
        }
    }

    public Color getPixel(Point point) {
        int i = size.width() * point.y() + point.x();
        return Color.fromARGB(buffer[i]);
    }

    private boolean isPointInBounds(Point point) {
        return point.x() >= 0 && point.x() < size.width() && point.y() >= 0 && point.y() < size.height();
    }

    public int[] getBuffer() {
        return this.buffer;
    }

    public Rect getSize() {
        return size;
    }

    public void reset(Color color) {
        Arrays.fill(this.buffer, color.toARGB());
    }

    public void fill(Color color) {
        Arrays.fill(this.buffer, color.toARGB());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(size, image.size) && Arrays.equals(buffer, image.buffer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(buffer);
        return result;
    }

    public void override(Image other) {
        if (this.size.equals(other.size)) {
            System.arraycopy(other.buffer, 0, this.buffer, 0, other.buffer.length);
        } else {
            throw new IllegalArgumentException("Image sizes do not match.");
        }
    }

    public void paintOverWith(Image ontop) {
        if (this.size.equals(ontop.size)) {
            for (int i = 0; i < this.buffer.length; i++) {
                int topPixel = ontop.buffer[i];
                if (Color.fromARGB(topPixel).alpha() != 0) {
                    this.buffer[i] = topPixel;
                }
            }
        } else {
            throw new IllegalArgumentException("Image sizes do not match.");
        }
    }

    @Override
    public Image clone() {
        return new Image(this.size, this.buffer.clone());
    }

    public void drawLine(Point start, Point end, Color color) {
        // Bresenham's line algorithm
        int x1 = start.x();
        int y1 = start.y();
        int x2 = end.x();
        int y2 = end.y();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            setPixel(new Point(x1, y1), color);

            if (x1 == x2 && y1 == y2) {
                break;
            }

            e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    public void drawSquare(Point start, Point end, Color color) {
        // Calculate the min and max points to create the square
        int minX = Math.min(start.x(), end.x());
        int maxX = Math.max(start.x(), end.x());
        int minY = Math.min(start.y(), end.y());
        int maxY = Math.max(start.y(), end.y());

        // Draw the square
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                setPixel(new Point(x, y), color);
            }
        }
    }

    public void drawOvalOutline(Point start, Point end, Color color) {
        int minX = Math.min(start.x(), end.x());
        int maxX = Math.max(start.x(), end.x());
        int minY = Math.min(start.y(), end.y());
        int maxY = Math.max(start.y(), end.y());

        int rx = (maxX - minX) / 2;
        int ry = (maxY - minY) / 2;
        int centerX = minX + rx;
        int centerY = minY + ry;

        int x = 0;
        int y = ry;
        int rxSq = rx * rx;
        int rySq = ry * ry;
        int xSq, ySq;
        int d1, d2;

        // Decision parameter for region 1
        d1 = rySq - rxSq * ry + rxSq / 4;
        xSq = 0;
        ySq = 2 * rxSq * y;

        // For region 1
        while (xSq < ySq) {
            setPixel(new Point(centerX + x, centerY - y), color);
            setPixel(new Point(centerX - x, centerY - y), color);
            setPixel(new Point(centerX + x, centerY + y), color);
            setPixel(new Point(centerX - x, centerY + y), color);

            if (d1 < 0) {
                x++;
                xSq += 2 * rySq;
                d1 += xSq + rySq;
            } else {
                x++;
                y--;
                xSq += 2 * rySq;
                ySq -= 2 * rxSq;
                d1 += xSq - ySq + rySq;
            }
        }

        // Decision parameter for region 2
        d2 = rySq * ((x * x) + (2 * x) + 1) + rxSq * (y - 1) * (y - 1) - rxSq * rySq;

        // For region 2
        while (y >= 0) {
            setPixel(new Point(centerX + x, centerY - y), color);
            setPixel(new Point(centerX - x, centerY - y), color);
            setPixel(new Point(centerX + x, centerY + y), color);
            setPixel(new Point(centerX - x, centerY + y), color);

            if (d2 > 0) {
                y--;
                ySq -= 2 * rxSq;
                d2 += rxSq - ySq;
            } else {
                y--;
                x++;
                xSq += 2 * rySq;
                ySq -= 2 * rxSq;
                d2 += xSq - ySq + rxSq;
            }
        }
    }

    public void drawSquareOutline(Point start, Point end, Color color) {
        int minX = Math.min(start.x(), end.x());
        int maxX = Math.max(start.x(), end.x());
        int minY = Math.min(start.y(), end.y());
        int maxY = Math.max(start.y(), end.y());

        for (int x = minX; x <= maxX; x++) {
            setPixel(new Point(x, minY), color);
            setPixel(new Point(x, maxY), color);
        }

        for (int y = minY + 1; y < maxY; y++) {
            setPixel(new Point(minX, y), color);
            setPixel(new Point(maxX, y), color);
        }
    }

    public void drawStar(Point start, Point end, Color activeColor) {
        int rx = (start.x());
        int ry = (start.y());

        // Inner and outer radius
        int outerRadius = Math.abs(end.x() - start.x()) / 2;
        int innerRadius = outerRadius / 2;

        // star koordinater
        Point[] outerPoints = new Point[5];
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72 - 18); // 72 degrees mellem hver point, starter fra 18 degrees
            int x = (int) Math.round(rx + outerRadius * Math.cos(angle));
            int y = (int) Math.round(ry + outerRadius * Math.sin(angle));
            outerPoints[i] = new Point(x, y);
        }

        // star hjørne koordinater
        Point[] innerPoints = new Point[5];
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72 + 18); // 72 degrees mellem hver point, starter fra 18 degrees
            int x = (int) Math.round(rx + innerRadius * Math.cos(angle));
            int y = (int) Math.round(ry + innerRadius * Math.sin(angle));
            innerPoints[i] = new Point(x, y);
        }

        // connecter points
        for (int i = 0; i < 5; i++) {
            drawLine(outerPoints[i], innerPoints[i], activeColor);
            drawLine(innerPoints[i], outerPoints[(i + 1) % 5], activeColor);
        }
    }
}
