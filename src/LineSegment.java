import main.Vector2d;

public class LineSegment {

    public Vector2d a;
    public Vector2d b;
    public Vector2d dir;

    public LineSegment(Vector2d a, Vector2d b){
        this.a = a;
        this.b = b;
        dir = new Vector2d(b.x - a.x, b.y - a.y);
    }

    @Override
    public String toString() {
        return "(" + this.a + " -> " + this.b + ")";
    }
}
