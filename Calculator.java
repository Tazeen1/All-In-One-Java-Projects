import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

// CLASS NAME RENAMED HERE
public class Calculator extends JFrame {
    private JTextField display;
    private DefaultListModel<String> historyModel;
    private ArrayList<String> history;

    private String operator = null;
    private Double operand1 = null;
    private boolean isNewInput = true;

    // CONSTRUCTOR NAME RENAMED HERE
    public Calculator() {
        setTitle("Calculator");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);

        history = new ArrayList<>();
        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);
        JScrollPane historyScroll = new JScrollPane(historyList);
        historyScroll.setPreferredSize(new Dimension(150, 0));

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "^", "/",
            "C", "=", "History", "Clear History"
        };

        for (String label : buttons) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(e -> onButtonClick(label));
            panel.add(button);
        }

        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(historyScroll, BorderLayout.EAST);
    }

    private void onButtonClick(String label) {
        if (label.matches("[0-9\\.]")) {
            if (isNewInput) {
                display.setText(label);
                isNewInput = false;
            } else {
                display.setText(display.getText() + label);
            }
        } else if ("+-*/^".contains(label)) {
            try {
                operand1 = Double.parseDouble(display.getText());
                operator = label;
                isNewInput = true;
            } catch (NumberFormatException e) {
                display.setText("Error");
                isNewInput = true;
            }
        } else if (label.equals("=")) {
            if (operator == null || operand1 == null || isNewInput) {
                return;
            }
            double operand2;
            try {
                operand2 = Double.parseDouble(display.getText());
            } catch (NumberFormatException e) {
                display.setText("Error");
                isNewInput = true;
                return;
            }
            
            double result;
            try {
                switch (operator) {
                    case "+": result = operand1 + operand2; break;
                    case "-": result = operand1 - operand2; break;
                    case "*": result = operand1 * operand2; break;
                    case "/":
                        if (operand2 == 0) throw new ArithmeticException("Division by zero");
                        result = operand1 / operand2;
                        break;
                    case "^": result = Math.pow(operand1, operand2); break;
                    default: throw new IllegalStateException("Unexpected operator");
                }
                String historyEntry = String.format("%.2f %s %.2f = %.2f", operand1, operator, operand2, result);
                history.add(historyEntry);
                historyModel.addElement(historyEntry);

                display.setText(String.valueOf(result));
                operand1 = result;
            } catch (Exception ex) {
                display.setText("Error");
            } finally {
                operator = null;
                isNewInput = true;
            }
        } else if (label.equals("C")) {
            display.setText("");
            operand1 = null;
            operator = null;
            isNewInput = true;
        } else if (label.equals("History")) {
            String historyText = history.isEmpty() ? "No calculations yet." : String.join("\n", history);
            JOptionPane.showMessageDialog(this, historyText, "Calculation History", JOptionPane.INFORMATION_MESSAGE);
        } else if (label.equals("Clear History")) {
            history.clear();
            historyModel.clear();
            JOptionPane.showMessageDialog(this, "Calculation history has been cleared.", "Clear History", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
