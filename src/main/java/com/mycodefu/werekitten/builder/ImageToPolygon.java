package com.mycodefu.werekitten.builder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Point
{
    double x,y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class Wall {
    String name;
    Point point1;
    Point point2;

    public Wall(String name, Point point1, Point point2) {
        this.name = name;
        this.point1 = point1;
        this.point2 = point2;
    }

    public static List<Wall> listFromImage(BufferedImage image) {
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall("Top", new Point(0, 0), new Point(image.getWidth(), 0)));
        walls.add(new Wall("Left", new Point(0,0), new Point(0, image.getHeight())));
        walls.add(new Wall("Bottom", new Point(0, image.getHeight()), new Point(image.getWidth(), image.getHeight())));
        walls.add(new Wall("Right", new Point(image.getWidth(), 0), new Point(image.getWidth(), image.getHeight())));
        return walls;
    }
}

public class ImageToPolygon {
    public static void main(String[] args) throws IOException {
        if (args.length != 2 || !Files.exists(Path.of(args[0]))) {
            System.out.println("Run this program with two arguments, the first being a valid file path!");
            return;
        }
        if (!isInteger(args[1])) {
            System.out.println("The second argument must be a number of polygon points!");
            return;
        }
        int polygonPoints = Integer.parseInt(args[1]);
        if (polygonPoints < 3) {
            System.out.println("Must have 3 or more points on the polygon!");
        }

        double increment_angle = 360d / (double) polygonPoints;

        BufferedImage image = ImageIO.read(Path.of(args[0]).toFile());
        Graphics2D graphics = (Graphics2D)image.getGraphics();

        List<Point> points = new ArrayList<>();

        for (double angle = 0; angle < 360; angle += increment_angle) {

            Point point = edgeLocationFromAngle(image, angle);

            double startX = point.x;
            double startY = point.y;

            double currentX = startX;
            double currentY = startY;

            boolean hitEdge = false;
            boolean hitAlpha = false;
            while(!hitEdge && !hitAlpha) {
                currentX = moveXAngle(currentX, angle, -1d);
                currentY = moveYAngle(currentY, angle, -1d);

                if (currentX < 0) {
                    currentX = 0;
                    hitEdge = true;
                } else if (currentX > image.getWidth()) {
                    currentX = image.getWidth();
                    hitEdge = true;
                } else if (currentY < 0) {
                    currentY = 0;
                    hitEdge = true;
                } else if (currentY > image.getHeight()) {
                    currentY = image.getHeight();
                    hitEdge = true;
                } else {
                    int pixel = image.getRGB((int)currentX, (int)currentY);
                    int alpha = (pixel >> 24) & 0xff;
//                    System.out.println(String.format("x: %f, y: %f, alpha: %d", currentX, currentY, alpha));

                    if (alpha==255) {
                        hitAlpha = true;
                    }
                }
            }

            if (hitAlpha) {
                points.add(new Point((int)currentX, (int)currentY));

                graphics.setColor(Color.BLACK);
                graphics.setStroke(new BasicStroke(3));
                graphics.drawLine((int)startX, (int)startY, (int)currentX, (int)currentY);
            }

        }

        int[] xs = points.stream().map(point -> (int)point.x).mapToInt(Integer::intValue).toArray();
        int[] ys = points.stream().map(point -> (int)point.y).mapToInt(Integer::intValue).toArray();

        Polygon polygon = new Polygon(xs, ys, points.size());
        graphics.setColor(Color.YELLOW);
        graphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawPolygon(polygon);

        ImageIO.write(image, "PNG", Path.of("test.png").toFile());

        BufferedImage justPolygon = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D justPolygonGraphics = (Graphics2D)justPolygon.getGraphics();
        justPolygonGraphics.setColor(Color.BLACK);
        justPolygonGraphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        justPolygonGraphics.fill(polygon);

        ImageIO.write(justPolygon, "PNG", Path.of("testJustPolygon.png").toFile());
    }

    private static double moveXAngle(double x, double angle, double distance) {
        return (x + distance * Math.cos(angle * Math.PI / 180));
    }

    private static double moveYAngle(double y, double angle, double distance) {
        return ((double) y + distance * Math.sin(angle * Math.PI / 180));
    }

    private static boolean isInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static Point edgeLocationFromAngle(BufferedImage image, double angleDegrees) {
        double centerX = (double)image.getWidth() / 2d;
        double centerY = (double)image.getHeight() / 2d;
        Point center = new Point(centerX, centerY);

        double length = image.getHeight() + image.getWidth() + 1;

        double edgeX = moveXAngle(centerX, angleDegrees, length);
        double edgeY = moveYAngle(centerY, angleDegrees, length);
        Point edge = new Point(edgeX, edgeY);

        List<Wall> walls = Wall.listFromImage(image);
        for (Wall wall : walls) {
            Point point = lineIntersection(wall.point1, wall.point2, center, edge);
            if (point.x != Double.MAX_VALUE && point.x >= 0 && point.x <= image.getWidth() && point.y >= 0 && point.y <= image.getHeight()) {
                switch (wall.name) {
                    case "Top":
                        if (angleDegrees > 180) {
                            return point;
                        }
                        break;
                    case "Left":
                        if (angleDegrees > 90 && angleDegrees < 270) {
                            return point;
                        }
                        break;
                    default:
                        return point;
                }
            }
        }
        throw new RuntimeException("Failed to find an intersection!");
    }

    static Point lineIntersection(Point A, Point B, Point C, Point D)
    {
        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1*(A.x) + b1*(A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2*(C.x)+ b2*(C.y);

        double determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        else
        {
            double x = (b2*c1 - b1*c2)/determinant;
            double y = (a1*c2 - a2*c1)/determinant;
            return new Point(x, y);
        }
    }
}
