import main.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * @author David Voigt
 */
public class GUI extends JPanel implements MouseMotionListener, MouseListener {
    private Vector2d mousePos = new Vector2d(); // Containing the current mouse position.
    private static final int NUM_RAYS = 180; // Number of rays to be drawn.

    // Variables for drawing line segments:
    private boolean isDrawing = false; // Determines whether or not a line segment is currently being drawn.
    private Vector2d firstClick = mousePos; // First position of a line segment drawn by mouse.
    private Vector2d secondClick = new Vector2d(); // Second position of a line segment drawn by mouse.

    // List containing the rays and line segments:
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private ArrayList<Ray> rays = new ArrayList<>();

    // Constants for windows window width and height:
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public GUI() {
        this.setBackground(Color.BLACK);

        this.setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);

        initBorders();
    }

    /**
     * Adds a line at each window border for the rays to hit if no other line is closer.
     */
    private void initBorders() {
        this.lineSegments.add(new LineSegment(
                new Vector2d(0, 0),
                new Vector2d(0, HEIGHT)
        ));
        this.lineSegments.add(new LineSegment(
                new Vector2d(0, 0),
                new Vector2d(WIDTH, 0)
        ));
        this.lineSegments.add(new LineSegment(
                new Vector2d(HEIGHT, WIDTH),
                new Vector2d(0, HEIGHT)
        ));
        this.lineSegments.add(new LineSegment(
                new Vector2d(HEIGHT, WIDTH),
                new Vector2d(WIDTH, 0)
        ));
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("Intersection Visualizer");
        window.setSize(HEIGHT, WIDTH);
        GUI rcv = new GUI();

        window.add(rcv);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
    }

    /**
     * This function is responsible for creating a specified number of rays around the source point p.
     *
     * @return A list containing all currently casted rays with their directional vectors pointing at the nearest
     * intersection with a line segment.
     */
    public ArrayList<Ray> castRays() {
        Vector2d p = this.mousePos;
        // List to store all rays
        ArrayList<Ray> rays = new ArrayList<>(NUM_RAYS);
        // Divide 360 degrees / 2 PI by the number of rays to cast
        double angle = (2 * Math.PI) / NUM_RAYS;
        // Vector representing the standard directional vector.
        final Vector2d stdDir = new Vector2d(1, 0);

        for (int i = 0; i < NUM_RAYS; i++) {
            // Create vector line: (x_1, x_2) = (p_1, p_2) + s * (u_1, u_2)
            // u = q - p
            // q is a point on the line
            // p is a vector pointing at the line
            // u is the directional vector of the line
            Vector2d q = Vector2d.sub(
                    p,
                    Vector2d.rotate(stdDir, angle * i)
            );
            Vector2d u = q.sub(p);
            // Calculate nearest intersection with one of the line segments
            Ray ray = new Ray(
                    p,
                    Intersection.getClosestIntersection(
                            new Ray(p, u),
                            this.lineSegments
                    )
            );
            rays.add(ray);
        }
        return rays;
    }

    /**
     * Responsible for drawing all the line segments and rays onto the canvas.
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Show current line to be drawn
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        if (this.isDrawing) {
            g.setColor(Color.WHITE);
            g2.drawLine(
                    (int) this.firstClick.x,
                    (int) this.firstClick.y,
                    (int) this.mousePos.x,
                    (int) this.mousePos.y
            );
        }
        // Draw all created lineSegments
        g.setColor(Color.WHITE);
        for (LineSegment l : lineSegments) {
            g2.drawLine((int) l.a.x, (int) l.a.y, (int) l.b.x, (int) l.b.y);
        }
        // Draw rays
        g.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        for (Ray r : this.rays) {
            Vector2d rPos = r.getPos();
            Vector2d rDir = r.getDir();
            g.drawLine(
                    (int) rPos.x,
                    (int) rPos.y,
                    (int) (rDir.x),
                    (int) (rDir.y)
            );
        }
    }

    /**
     * Called when the mouse is being moved. The position of the mouse will be updated and stored in the instance
     * variable 'mousePos'. Furthermore rays will be casted and updated at each move of the mouse by calling the
     * method 'castRays'.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        this.mousePos = new Vector2d(mouseEvent.getX(), mouseEvent.getY());
        this.rays = castRays();
        repaint();
    }

    /**
     * Responsible for saving two mouse click position for creating a line segment between these two positions.
     *
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (!this.isDrawing) {
            this.firstClick = new Vector2d(mouseEvent.getX(), mouseEvent.getY());
            this.isDrawing = true;
        } else {
            this.secondClick = new Vector2d(mouseEvent.getX(), mouseEvent.getY());
            this.lineSegments.add(new LineSegment(this.firstClick, this.secondClick));
            this.isDrawing = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }
}