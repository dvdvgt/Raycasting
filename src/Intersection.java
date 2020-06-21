import main.Vector2d;

import java.util.ArrayList;

/**
 * This class contains functions to find the closest intersection point of a line and a line segment.
 *
 * @author David Voigt
 */
public class Intersection {

    /**
     * Finds the a intersection point between a ray and a line segment.
     * <p>
     * The math can be found here:
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Intersection_of_two_lines
     *
     * @param ray         Ray for which to find a intersection with the specified line segment
     * @param lineSegment Line segment for which to find an intersection point with the specified ray.
     * @return Either null if the ray and line segment don't intersect or the intersection point if they intersect.
     */
    public static Vector2d getIntersection(Ray ray, LineSegment lineSegment) {
        double x1 = ray.getPos().x;
        double y1 = ray.getPos().y;
        double x2 = ray.getPos().x + ray.getDir().x;
        double y2 = ray.getPos().y + ray.getDir().y;

        double x3 = lineSegment.a.x;
        double y3 = lineSegment.a.y;
        double x4 = lineSegment.b.x;
        double y4 = lineSegment.b.y;

        // Ray and line segment are parallel or coincident
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = -(((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator);

        // u is the scalar of the line segment. Since the line segment is only a segment of a line it must not be longer
        // than the it's length therefor only a scalar of <= 1 is allowed.
        //
        // t is the scalar of the ray. Since the ray is a line and not a line segment it may be multiplied with a scalar
        // >= 0 but not < 0 because that would mean reversing the direction which is not allowed.
        if (t >= 0 && u >= 0 && u <= 1) {
            return new Vector2d(
                    x1 + t * (x2 - x1),
                    y1 + t * (y2 - y1)
            );
        }
        // The intersection point does not fall within the length of the line segment.
        return null;
    }

    /**
     * Finds the closest intersection point of a ray and a line segment.
     *
     * @param ray          Ray for which to find the closest intersection point with one line segment
     * @param lineSegments List containing all active line segments for which to find the closest intersection point with
     *                     the specified ray.
     * @return A Vector pointing at the closest intersection point. Cannot be null since there always at least 4 line
     * segments (border line segments).
     */
    public static Vector2d getClosestIntersection(Ray ray, ArrayList<LineSegment> lineSegments) {
        Vector2d closestIntersection = null;
        double min = Double.MAX_VALUE;

        for (LineSegment l : lineSegments) {
            Vector2d candidate = getIntersection(ray, l);

            if (candidate != null && (closestIntersection == null || Vector2d.dist(ray.getPos(), candidate) < min)) {
                closestIntersection = candidate;
                min = Vector2d.dist(ray.getPos(), candidate);
            }
        }
        return closestIntersection;
    }
}
