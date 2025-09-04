import java.awt.*;
import javax.swing.*;

public class GamesFrame extends JFrame {

    public GamesFrame() {
        setTitle("Game Zone");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        StarryBackgroundPanel panel = new StarryBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Game Zone");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(30, 10, 15, 10);

        // --- Hangman Button ---
        JButton hangmanButton = new StyledButton("Hangman");
        hangmanButton.addActionListener(e -> {
            FunHangmanGUI hangmanGame = new FunHangmanGUI();
            hangmanGame.setVisible(true);
        });
        panel.add(hangmanButton, gbc);
        
        gbc.insets = new Insets(15, 10, 15, 10);

        // --- Quiz Game Button (NOW FUNCTIONAL) ---
        JButton quizButton = new StyledButton("Quiz Game");
        quizButton.addActionListener(e -> {
            QuizGameGUI quizGame = new QuizGameGUI();
            quizGame.setVisible(true);
        });
        panel.add(quizButton, gbc);

        // --- Number Guessing Button ---
        JButton guessButton = new StyledButton("Number Guessing");
        guessButton.addActionListener(e -> {
            NumberGuessingGame guessingGame = new NumberGuessingGame();
            guessingGame.setVisible(true);
        });
        panel.add(guessButton, gbc);

        setVisible(true);
    }

    // This method is no longer needed but can be kept for future tools
    private void showPlaceholder(String featureName) {
        JOptionPane.showMessageDialog(this, featureName + " is coming soon!", "In Development", JOptionPane.INFORMATION_MESSAGE);
    }
}
