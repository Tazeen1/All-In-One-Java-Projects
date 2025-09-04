import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class StarryBackgroundPanel extends JPanel {
    private final ArrayList<Point> stars = new ArrayList<>();
    private final ArrayList<Integer> starSizes = new ArrayList<>();
    private final Random rand = new Random();

    public StarryBackgroundPanel() {
        // Add a listener that triggers when the component (our panel) is resized
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // When resized, generate a new set of stars for the new dimensions
                generateStars();
                repaint(); // Redraw the panel with the new stars
            }
        });
    }

    private void generateStars() {
        // Clear the old stars
        stars.clear();
        starSizes.clear();
        
        // Get the current width and height of the panel
        int width = getWidth();
        int height = getHeight();

        // Generate 100 stars within the new bounds
        if (width > 0 && height > 0) {
            for (int i = 0; i < 100; i++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                stars.add(new Point(x, y));
                starSizes.add(rand.nextInt(3) + 1);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // The gradient now always fills the entire panel, regardless of size
        g2d.setPaint(new GradientPaint(0, 0, new Color(22, 26, 48), 0, getHeight(), new Color(49, 48, 77)));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the stars
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < stars.size(); i++) {
            Point star = stars.get(i);
            int size = starSizes.get(i);
            g2d.fillOval(star.x, star.y, size, size);
        }
    }
}
