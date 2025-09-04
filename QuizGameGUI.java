import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class QuizGameGUI extends JFrame {

    // --- Master Question Bank (20 questions) ---
    private final String[] fullQuestions = {
        "What is the capital of India?", "Which gas do plants absorb from the atmosphere?",
        "What is the chemical symbol for Gold?", "What does CPU stand for in computer terms?",
        "Which planet is known as the Red Planet?", "What is the powerhouse of the cell?",
        "What is the most abundant gas in Earth's atmosphere?", "Which company developed the Java programming language?",
        "Who is known as the 'Father of the Computer'?", "What part of the plant is responsible for photosynthesis?",
        "What does 'H2O' represent in chemistry?", "What is the main circuit board in a computer called?",
        "Which of these is a primary color?", "What does RAM stand for?",
        "How many continents are there on Earth?", "Which animal is known as the King of the Jungle?",
        "What is the boiling point of water in Celsius?", "Which element is essential for strong bones?",
        "Who was the first person to walk on the Moon?", "What is the largest mammal in the world?"
    };
    private final String[][] fullOptions = {
        {"a) Mumbai", "b) New Delhi", "c) Kolkata", "d) Chennai"}, {"a) Oxygen", "b) Nitrogen", "c) Carbon Dioxide", "d) Hydrogen"},
        {"a) Ag", "b) Au", "c) Gd", "d) Go"}, {"a) Central Processing Unit", "b) Computer Personal Unit", "c) Central Processor Unit", "d) Central Process Unit"},
        {"a) Earth", "b) Jupiter", "c) Mars", "d) Venus"}, {"a) Nucleus", "b) Ribosome", "c) Mitochondrion", "d) Golgi apparatus"},
        {"a) Oxygen", "b) Carbon Dioxide", "c) Argon", "d) Nitrogen"}, {"a) Microsoft", "b) Apple", "c) Sun Microsystems", "d) IBM"},
        {"a) Alan Turing", "b) Charles Babbage", "c) Ada Lovelace", "d) John von Neumann"}, {"a) Root", "b) Stem", "c) Flower", "d) Leaf"},
        {"a) Hydrogen Peroxide", "b) Water", "c) Salt", "d) Sugar"}, {"a) RAM", "b) Hard Drive", "c) Motherboard", "d) CPU"},
        {"a) Green", "b) Orange", "c) Blue", "d) Purple"}, {"a) Random Access Memory", "b) Read-Only Memory", "c) Real-time Access Memory", "d) Run-time Access Memory"},
        {"a) 5", "b) 6", "c) 7", "d) 8"}, {"a) Elephant", "b) Tiger", "c) Lion", "d) Bear"},
        {"a) 90°C", "b) 100°C", "c) 110°C", "d) 120°C"}, {"a) Iron", "b) Potassium", "c) Calcium", "d) Sodium"},
        {"a) Neil Armstrong", "b) Edwin Buzz Aldrin", "c) Yuri Gagarin", "d) Michael Collins"}, {"a) African Elephant", "b) Giraffe", "c) Blue Whale", "d) Hippopotamus"}
    };
    private final char[] fullAnswers = {'b', 'c', 'b', 'a', 'c', 'c', 'd', 'c', 'b', 'd', 'b', 'c', 'c', 'a', 'c', 'c', 'b', 'c', 'a', 'c'};
    
    // --- Session-specific arrays for the current game ---
    private String[] questions;
    private String[][] options;
    private char[] answers;
    private final int questionsPerGame = 10;

    // --- Game Logic Variables ---
    private int currentQuestionIndex;
    private int score;

    // --- UI Components ---
    private JLabel questionLabel;
    private JButton[] optionButtons = new JButton[4];
    private JButton nextButton;
    private JProgressBar progressBar;
    private JLabel feedbackLabel;

    // --- UI Styling ---
    private static final Font gameFont = new Font("Comic Sans MS", Font.BOLD, 18);
    private static final Font questionFont = new Font("Comic Sans MS", Font.BOLD, 22);
    private static final Color correctColor = new Color(40, 180, 99);
    private static final Color wrongColor = new Color(231, 76, 60);
    private static final Color optionColor = new Color(52, 152, 219);
    private static final Color optionHoverColor = new Color(82, 172, 239);

    public QuizGameGUI() {
        // --- Window Setup ---
        setTitle("🎉 Fun Quiz Challenge! 🎉");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // MODIFIED
        setLocationRelativeTo(null);

        // --- Gradient Background ---
        JPanel gradientPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(24, 40, 72), 0, getHeight(), new Color(75, 108, 183));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(gradientPanel);

        // --- Top Panel (Progress Bar) ---
        progressBar = new JProgressBar(0, questionsPerGame);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(gameFont);
        progressBar.setForeground(new Color(243, 156, 18));
        add(progressBar, BorderLayout.NORTH);

        // --- Center Panel (Question) ---
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(questionFont);
        questionLabel.setForeground(Color.WHITE);
        add(questionLabel, BorderLayout.CENTER);

        // --- Bottom Panel (Options, Feedback, Next Button) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        // Options Grid
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setOpaque(false);
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = createStyledButton("");
            optionsPanel.add(optionButtons[i]);
            final int buttonIndex = i;
            optionButtons[i].addActionListener(e -> checkAnswer(buttonIndex));
        }
        bottomPanel.add(optionsPanel, BorderLayout.CENTER);

        // Feedback and Next Button Panel
        JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        feedbackPanel.setOpaque(false);
        feedbackLabel = new JLabel("Choose an answer!");
        feedbackLabel.setFont(gameFont);
        feedbackLabel.setForeground(Color.YELLOW);
        
        nextButton = createStyledButton("Next ➡️");
        nextButton.setVisible(false);
        nextButton.addActionListener(e -> nextQuestion());
        
        feedbackPanel.add(feedbackLabel);
        feedbackPanel.add(nextButton);
        bottomPanel.add(feedbackPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Start the Game ---
        startNewGame();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(gameFont);
        button.setBackground(optionColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(button.isEnabled()) button.setBackground(optionHoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                if(button.isEnabled()) button.setBackground(optionColor);
            }
        });
        return button;
    }
    
    private void startNewGame() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < fullQuestions.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        
        questions = new String[questionsPerGame];
        options = new String[questionsPerGame][4];
        answers = new char[questionsPerGame];

        for (int i = 0; i < questionsPerGame; i++) {
            int randomIndex = indices.get(i);
            questions[i] = fullQuestions[randomIndex];
            options[i] = fullOptions[randomIndex];
            answers[i] = fullAnswers[randomIndex];
        }

        currentQuestionIndex = 0;
        score = 0;
        loadQuestion();
        nextButton.setText("Next ➡️");
        
        for(java.awt.event.ActionListener al : nextButton.getActionListeners()) {
            nextButton.removeActionListener(al);
        }
        nextButton.addActionListener(e -> nextQuestion());
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionLabel.setText("<html><div style='text-align: center;'>" + questions[currentQuestionIndex] + "</div></html>");
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[currentQuestionIndex][i]);
                optionButtons[i].setEnabled(true);
                optionButtons[i].setBackground(optionColor);
            }
            feedbackLabel.setText("Choose an answer!");
            nextButton.setVisible(false);
            progressBar.setValue(currentQuestionIndex);
        } else {
            showFinalScore();
        }
    }

    private void checkAnswer(int selectedButtonIndex) {
        char selectedOption = (char) ('a' + selectedButtonIndex);
        
        if (selectedOption == answers[currentQuestionIndex]) {
            score++;
            optionButtons[selectedButtonIndex].setBackground(correctColor);
            feedbackLabel.setText("✅ Correct!");
        } else {
            optionButtons[selectedButtonIndex].setBackground(wrongColor);
            feedbackLabel.setText("❌ Wrong!");
            int correctIndex = answers[currentQuestionIndex] - 'a';
            optionButtons[correctIndex].setBackground(correctColor);
        }

        for (JButton button : optionButtons) {
            button.setEnabled(false);
        }
        nextButton.setVisible(true);
    }
    
    private void nextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }

    private void showFinalScore() {
        progressBar.setValue(questions.length);
        questionLabel.setText("Quiz Finished!");
        
        String finalMessage;
        if (score == questions.length) {
            finalMessage = "🎉 Excellent! You're a genius! 🎉";
        } else if (score >= 7) {
            finalMessage = "👍 Great job! You really know your stuff.";
        } else if (score >= 5) {
            finalMessage = "🤔 Good effort! Keep practicing.";
        } else {
            finalMessage = "😥 Better luck next time!";
        }
        feedbackLabel.setText("You scored " + score + " out of " + questions.length + ". " + finalMessage);

        for (JButton button : optionButtons) {
            button.setVisible(false);
        }
        nextButton.setText("Play Again? 🔄");
        
        for(java.awt.event.ActionListener al : nextButton.getActionListeners()) {
            nextButton.removeActionListener(al);
        }
        nextButton.addActionListener(e -> {
            for (JButton button : optionButtons) {
                button.setVisible(true);
            }
            startNewGame();
        });
    }
}
