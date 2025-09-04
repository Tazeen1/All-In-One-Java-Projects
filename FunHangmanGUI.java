import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FunHangmanGUI extends JFrame {

    // --- Game Data ---
    private final String[] wordBank = {
        "java", "computer", "hangman", "program", "keyboard", "developer", "challenge", "sunshine",
        "adventure", "celebrate", "xylophone", "jazz", "galaxy", "wizard", "oxygen"
    };
    private final String[] hints = {
        "A popular programming language", "An electronic data processor", "A classic word-guessing game",
        "A set of instructions", "An input device for typing", "Someone who writes code", "A difficult task",
        "Light from the sun", "An exciting experience", "To honor a special day", "A musical instrument",
        "A style of music", "A system of stars", "A man with magical powers", "A gas essential for life"
    };
    
    // --- Game Logic Variables ---
    private String wordToGuess;
    private StringBuilder guessedWord;
    private int wrongGuesses;
    private final int maxWrongGuesses = 6;
    private Set<Character> guessedLetters;

    // --- UI Components ---
    private JLabel wordLabel;
    private JLabel hintLabel;
    private JLabel statusLabel;
    private JPanel keyboardPanel;
    private HangmanDrawingPanel hangmanPanel;
    private JButton playAgainButton;

    // --- UI Styling ---
    private static final Font gameFont = new Font("Comic Sans MS", Font.BOLD, 18);
    private static final Font wordFont = new Font("Monospaced", Font.BOLD, 60);

    public FunHangmanGUI() {
        // --- Window Setup ---
        setTitle("🎉 Fun Hangman Challenge! 🎉");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes only this game window
        setLocationRelativeTo(null); // Center on screen
        
        // --- Main Panel with Gradient Background ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 250), 0, getHeight(), new Color(70, 130, 180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- Top Panel (Hangman Drawing and Word Display) ---
        JPanel topPanel = new JPanel(new BorderLayout(20, 10));
        topPanel.setOpaque(false);
        
        hangmanPanel = new HangmanDrawingPanel();
        hangmanPanel.setPreferredSize(new Dimension(200, 200));
        
        JPanel wordAndHintPanel = new JPanel();
        wordAndHintPanel.setLayout(new BoxLayout(wordAndHintPanel, BoxLayout.Y_AXIS));
        wordAndHintPanel.setOpaque(false);
        
        wordLabel = new JLabel();
        wordLabel.setFont(wordFont);
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel();
        hintLabel.setFont(gameFont);
        hintLabel.setForeground(Color.YELLOW);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statusLabel = new JLabel("Guess a letter!", SwingConstants.CENTER);
        statusLabel.setFont(gameFont);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wordAndHintPanel.add(Box.createVerticalGlue());
        wordAndHintPanel.add(wordLabel);
        wordAndHintPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wordAndHintPanel.add(hintLabel);
        wordAndHintPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        wordAndHintPanel.add(statusLabel);
        wordAndHintPanel.add(Box.createVerticalGlue());

        topPanel.add(hangmanPanel, BorderLayout.WEST);
        topPanel.add(wordAndHintPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.CENTER);

        // --- Bottom Panel (Keyboard and Play Again) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        keyboardPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        keyboardPanel.setOpaque(false);
        createKeyboard();

        playAgainButton = new JButton("Play Again 🔄");
        playAgainButton.setFont(gameFont);
        playAgainButton.setBackground(new Color(50, 205, 50));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> startNewGame());

        bottomPanel.add(keyboardPanel, BorderLayout.CENTER);
        bottomPanel.add(playAgainButton, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Start the Game ---
        startNewGame();
    }

    private void createKeyboard() {
        for (char c = 'a'; c <= 'z'; c++) {
            JButton button = new JButton(String.valueOf(c).toUpperCase());
            button.setFont(gameFont);
            button.setFocusPainted(false);
            button.addActionListener(keyboardListener);
            keyboardPanel.add(button);
        }
    }

    private final ActionListener keyboardListener = e -> {
        JButton button = (JButton) e.getSource();
        String letterStr = button.getText().toLowerCase();
        char letter = letterStr.charAt(0);

        button.setEnabled(false);
        checkGuess(letter);
    };

    private void startNewGame() {
        Random rand = new Random();
        int wordIndex = rand.nextInt(wordBank.length);
        wordToGuess = wordBank[wordIndex];
        String hint = hints[wordIndex];
        
        wrongGuesses = 0;
        guessedLetters = new HashSet<>();
        guessedWord = new StringBuilder();
        for (int i = 0; i < wordToGuess.length(); i++) {
            guessedWord.append("_");
        }
        
        wordLabel.setText(formatGuessedWord());
        hintLabel.setText("Hint: " + hint);
        statusLabel.setText("Guess a letter to start!");
        hangmanPanel.setWrongGuesses(0);
        playAgainButton.setVisible(false);
        
        for (Component comp : keyboardPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(true);
            }
        }
    }

    private void checkGuess(char letter) {
        guessedLetters.add(letter);
        
        if (wordToGuess.indexOf(letter) >= 0) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    guessedWord.setCharAt(i, letter);
                }
            }
            wordLabel.setText(formatGuessedWord());
            statusLabel.setText("Good guess! 👍");
            
            if (guessedWord.toString().equals(wordToGuess)) {
                endGame(true);
            }
        } else {
            wrongGuesses++;
            hangmanPanel.setWrongGuesses(wrongGuesses);
            statusLabel.setText("Wrong! ❌ Attempts left: " + (maxWrongGuesses - wrongGuesses));
            
            if (wrongGuesses >= maxWrongGuesses) {
                endGame(false);
            }
        }
    }
    
    private void endGame(boolean won) {
        for (Component comp : keyboardPanel.getComponents()) {
            comp.setEnabled(false);
        }
        
        if (won) {
            statusLabel.setText("🎉 Congratulations! You won! 🎉");
        } else {
            statusLabel.setText("😥 Game Over! The word was: " + wordToGuess.toUpperCase());
            wordLabel.setText(wordToGuess.toUpperCase().replace("", " ").trim());
        }
        playAgainButton.setVisible(true);
    }
    
    private String formatGuessedWord() {
        return guessedWord.toString().toUpperCase().replace("", " ").trim();
    }

    class HangmanDrawingPanel extends JPanel {
        private int wrongGuesses = 0;

        public HangmanDrawingPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(), "Status",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                gameFont, Color.WHITE)
            );
        }

        public void setWrongGuesses(int count) {
            this.wrongGuesses = count;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(84, 58, 29));

            g2.drawLine(40, 180, 120, 180);
            g2.drawLine(80, 180, 80, 20);
            g2.drawLine(80, 20, 160, 20);
            g2.drawLine(160, 20, 160, 40);
            
            g2.setColor(Color.BLACK);
            
            if (wrongGuesses > 0) g2.drawOval(145, 40, 30, 30);
            if (wrongGuesses > 1) g2.drawLine(160, 70, 160, 120);
            if (wrongGuesses > 2) g2.drawLine(160, 80, 130, 100);
            if (wrongGuesses > 3) g2.drawLine(160, 80, 190, 100);
            if (wrongGuesses > 4) g2.drawLine(160, 120, 130, 150);
            if (wrongGuesses > 5) g2.drawLine(160, 120, 190, 150);
        }
    }
}
