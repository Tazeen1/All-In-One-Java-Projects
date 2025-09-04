import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

// --- Custom Button with Hover Effects and Rounded Corners ---
public class StyledButton extends JButton {
    private Color hoverBackgroundColor = new Color(113, 122, 175);
    private Color normalBackgroundColor = new Color(83, 92, 145);

    public StyledButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        setBackground(normalBackgroundColor);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 22));
        setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        setPreferredSize(new Dimension(250, 70));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBackgroundColor);
            }
             @Override
            public void mousePressed(MouseEvent e) {
                setBackground(new Color(70, 80, 130)); // A slightly darker color for press
            }
             @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    setBackground(hoverBackgroundColor);
                } else {
                    setBackground(normalBackgroundColor);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        // 30 is the radius for the rounded corners
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();
        
        super.paintComponent(g);
    }
}
