import main.Vector2d;

public class Ray {
    private Vector2d pos;
    private Vector2d dir;

    public Ray(Vector2d pos, Vector2d dir) {
        this.pos = pos;
        this.dir = dir;
    }

    public Vector2d getPos() {
        return pos;
    }

    public Vector2d getDir() {
        return dir;
    }

    public Vector2d getLine(double scalar) {
        return Vector2d.add(this.pos, Vector2d.mul(dir, scalar));
    }
}
