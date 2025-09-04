import javax.swing.*;
import java.awt.*;

public class WorkFrame extends JFrame {

    public WorkFrame() {
        setTitle("Work Tools");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        StarryBackgroundPanel panel = new StarryBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Work Tools");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);

        gbc.insets = new Insets(40, 10, 20, 10);

        // --- THIS PART IS UPDATED TO USE THE NEW CLASS NAME ---
        JButton calcButton = new StyledButton("Calculator");
        calcButton.addActionListener(e -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
        panel.add(calcButton, gbc);
        
        gbc.insets = new Insets(20, 10, 20, 10);
        
        JButton billButton = new StyledButton("Electricity Bill");
        billButton.addActionListener(e -> {
            ElectricityBillGUI billCalculator = new ElectricityBillGUI();
            billCalculator.setVisible(true);
        });
        panel.add(billButton, gbc);

        setVisible(true);
    }
}
