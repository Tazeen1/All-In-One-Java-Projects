import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ElectricityBillGUI extends JFrame {

    // Defines a customer. Placing this class inside makes the component self-contained.
    private class Customer {
        String name;
        String meterNumber;
        int unitsConsumed;

        public Customer(String name, String meterNumber) {
            this.name = name;
            this.meterNumber = meterNumber;
            this.unitsConsumed = 0;
        }
    }

    private ArrayList<Customer> customerList = new ArrayList<>();
    private JTable customerTable;
    private DefaultTableModel tableModel;

    // --- Custom Colors and Fonts ---
    private static final Color PRIMARY_COLOR = new Color(30, 50, 100);
    private static final Color BACKGROUND_COLOR = new Color(255, 253, 240);
    private static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);

    public ElectricityBillGUI() {
        // --- Window Setup ---
        setTitle("Electricity Billing System");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensures only this window closes
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- Main Panel ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // --- Headline ---
        JLabel headlineLabel = new JLabel("Electricity Billing Management", SwingConstants.CENTER);
        headlineLabel.setFont(HEADING_FONT);
        headlineLabel.setForeground(PRIMARY_COLOR);
        mainPanel.add(headlineLabel, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton addCustomerButton = createStyledButton("ADD NEW CUSTOMER");
        JButton calculateBillButton = createStyledButton("CALCULATE & SHOW BILL");
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(calculateBillButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Customer Table ---
        String[] columnNames = {"Customer Name", "Meter Number", "Units Consumed", "Bill Amount (₹)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        customerTable.getTableHeader().setBackground(new Color(220, 220, 220));
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customer Database"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // --- Action Listeners ---
        addCustomerButton.addActionListener(e -> showAddCustomerDialog());
        calculateBillButton.addActionListener(e -> showCalculateBillDialog());

        refreshTable();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        return button;
    }

    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField(20);
        JTextField meterField = new JTextField(20);
        nameField.addActionListener(e -> meterField.requestFocusInWindow());

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Meter Number:"));
        panel.add(meterField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String meterNumber = meterField.getText();
            if (!name.trim().isEmpty() && !meterNumber.trim().isEmpty()) {
                customerList.add(new Customer(name, meterNumber));
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCalculateBillDialog() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table first.", "No Customer Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Customer customer = customerList.get(selectedRow);
        String unitsStr = JOptionPane.showInputDialog(this, "Enter units consumed for " + customer.name + ":", "Enter Units", JOptionPane.PLAIN_MESSAGE);
        if (unitsStr != null && !unitsStr.trim().isEmpty()) {
            try {
                int units = Integer.parseInt(unitsStr);
                customer.unitsConsumed = units;
                refreshTable();
                
                double billAmount = calculateBill(units);
                String billDetails = String.format(
                    "<html><body><h3>Electricity Bill</h3><p>Customer: %s<br>Meter No: %s<br>Units: %d</p><hr><h3>Total: ₹%.2f</h3></body></html>",
                    customer.name, customer.meterNumber, customer.unitsConsumed, billAmount
                );
                JOptionPane.showMessageDialog(this, billDetails, "Bill Details", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input for units.", "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Customer c : customerList) {
            double bill = calculateBill(c.unitsConsumed);
            Object[] row = {c.name, c.meterNumber, c.unitsConsumed, String.format("₹%.2f", bill)};
            tableModel.addRow(row);
        }
    }
    
    private double calculateBill(int units) {
        if (units <= 0) return 0.0;
        double amount;
        if (units <= 100) amount = units * 5.0;
        else if (units <= 200) amount = (100 * 5.0) + (units - 100) * 7.0;
        else amount = (100 * 5.0) + (100 * 7.0) + (units - 200) * 10.0;
        return amount + 50.0; // Add fixed charge
    }
}
