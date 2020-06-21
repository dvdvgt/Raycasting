import main.Vector2d;

import java.util.ArrayList;

public class RayCast {
    private static final double EPSILON = 0.0001;

    public static Vector2d getIntersection(Ray ray, LineSegment lineSegment){
        double x1 = ray.getPos().x;
        double y1 = ray.getPos().y;
        double x2 = ray.getPos().x + ray.getDir().x;
        double y2 = ray.getPos().y + ray.getDir().y;

        double x3 = lineSegment.a.x;
        double y3 = lineSegment.a.y;
        double x4 = lineSegment.b.x;
        double y4 = lineSegment.b.y;

        // Parallel or coincident
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = - (((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 -x3)) / denominator);
        //System.out.println("" + t + ", " + u);

        if (t > 0 && u >= 0 && u <= 1) {
            return new Vector2d(
                    x1 + t * (x2 - x1),
                    y1 + t * (y2 -y1)
            );
        }
        return null;
    }

    public static Vector2d getClosestIntersection(Ray ray, ArrayList<LineSegment> lineSegments) {
        Vector2d closestIntersection = null;
        double min = Double.MAX_VALUE;

        for (LineSegment l : lineSegments) {
            Vector2d candidate = getIntersection(ray, l);

            if (candidate == null) {
                continue;
            } else if (closestIntersection == null || Vector2d.dist(ray.getPos(), candidate)  < min) {
                closestIntersection = candidate;
                min = Vector2d.dist(ray.getPos(), candidate);
            }
        }
        return closestIntersection;
    }
}
