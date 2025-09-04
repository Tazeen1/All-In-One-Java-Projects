import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// CLASS NAME RENAMED HERE
public class NumberGuessingGame extends JFrame {

    // Game Logic Variables
    private int numberToGuess;
    private int attempts;
    private final int maxAttempts = 5;

    // UI Components
    private JTextField guessField;
    private JButton guessButton;
    private JButton playAgainButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;

    // --- UI Styling for a "Fun Game" Feel ---
    private static final Font gameFont = new Font("Comic Sans MS", Font.BOLD, 18);
    private static final Font titleFont = new Font("Comic Sans MS", Font.BOLD, 32);
    private static final Color buttonColor = new Color(255, 120, 30); // Vibrant Orange
    private static final Color buttonHoverColor = new Color(255, 150, 50); // Lighter Orange for hover
    private static final Color playAgainColor = new Color(50, 205, 50); // Lime Green

    // CONSTRUCTOR NAME RENAMED HERE
    public NumberGuessingGame() {
        // --- Window Setup ---
        setTitle("🎉 Number Guessing Bonanza! 🎉");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // --- Gradient Background Panel ---
        JPanel gradientPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(70, 130, 180); // Steel Blue
                Color color2 = new Color(138, 43, 226); // Blue Violet
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        setContentPane(gradientPanel);
        setLayout(new GridBagLayout());

        // --- Main Content Panel ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Title ---
        JLabel titleLabel = new JLabel("GUESS THE SECRET NUMBER!", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Feedback ---
        feedbackLabel = new JLabel("Enter a number between 1 & 100", SwingConstants.CENTER);
        feedbackLabel.setFont(gameFont);
        feedbackLabel.setForeground(Color.YELLOW);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Input Field ---
        guessField = new JTextField(5);
        guessField.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setMaximumSize(new Dimension(150, 60));
        guessField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Guess Button ---
        guessButton = createStyledButton("👉 GUESS!");
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Attempts Label ---
        attemptsLabel = new JLabel("You have 5 attempts left", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
        attemptsLabel.setForeground(Color.WHITE);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Play Again Button ---
        playAgainButton = createStyledButton("Play Again? 🔄");
        playAgainButton.setBackground(playAgainColor);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setVisible(false);

        // --- Add Components with Spacing ---
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(feedbackLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(guessField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(guessButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(attemptsLabel);
        contentPanel.add(playAgainButton);
        
        add(contentPanel);

        // --- Action Listeners ---
        guessButton.addActionListener(e -> checkGuess());
        guessField.addActionListener(e -> checkGuess());
        playAgainButton.addActionListener(e -> resetGame());

        startNewGame();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(gameFont);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(button.getBackground().brighter());
                }
            }
            public void mouseExited(MouseEvent evt) {
                 if (button.isEnabled()) {
                    button.setBackground(button.getBackground().darker());
                }
            }
        });
        return button;
    }

    private void checkGuess() {
        String guessText = guessField.getText();
        if (guessText.trim().isEmpty()) {
            feedbackLabel.setText("Oops! Please enter a number! 🤔");
            return;
        }

        try {
            int userGuess = Integer.parseInt(guessText);
            attempts++;

            if (userGuess == numberToGuess) {
                feedbackLabel.setText("🎉 CORRECT! YOU ARE A WINNER! 🎉");
                endGame();
            } else {
                if (attempts >= maxAttempts) {
                    feedbackLabel.setText("😥 Game Over! The number was " + numberToGuess);
                    endGame();
                } else {
                    feedbackLabel.setText(userGuess < numberToGuess ? "Too Low! Try a bit higher! ⬆️" : "Too High! Try a bit lower! ⬇️");
                    if (attempts == 2) {
                        int hintStart = Math.max(1, numberToGuess - 5);
                        int hintEnd = hintStart + 9;
                         if (hintEnd > 100) {
                            hintEnd = 100;
                            hintStart = hintEnd - 9;
                        }
                        feedbackLabel.setText("<html>" + feedbackLabel.getText() + "<br><i><font color='yellow'>Hint: It's between " + hintStart + " and " + hintEnd + "!</font></i></html>");
                    }
                }
            }
            attemptsLabel.setText("You have " + (maxAttempts - attempts) + " attempts left");
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("That's not a real number! 🤪");
        } finally {
            guessField.setText("");
            guessField.requestFocusInWindow();
        }
    }
    
    private void resetGame() {
        startNewGame();
        feedbackLabel.setText("New game! Guess a number from 1 to 100");
        attemptsLabel.setText("You have " + maxAttempts + " attempts left");
        guessField.setEditable(true);
        guessButton.setVisible(true);
        playAgainButton.setVisible(false);
    }
    
    private void endGame() {
        guessField.setEditable(false);
        guessButton.setVisible(false);
        playAgainButton.setVisible(true);
    }

    private void startNewGame() {
        Random rand = new Random();
        numberToGuess = rand.nextInt(100) + 1;
        attempts = 0;
    }
}
