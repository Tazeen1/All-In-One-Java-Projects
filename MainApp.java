import java.awt.*;
import javax.swing.*;

public class MainApp {

    public MainApp() {
        JFrame frame = new JFrame("Application Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        // frame.setResizable(false); // This line has been removed
        frame.setResizable(true); // Or changed to true

        StarryBackgroundPanel panel = new StarryBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Explorer Hub");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(40, 10, 20, 10);

        JButton workButton = new StyledButton("💼 Work");
        workButton.addActionListener(e -> new WorkFrame());
        panel.add(workButton, gbc);

        gbc.insets = new Insets(20, 10, 20, 10);
        JButton gamesButton = new StyledButton("🎮 Games");
        gamesButton.addActionListener(e -> new GamesFrame());
        panel.add(gamesButton, gbc);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
