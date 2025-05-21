//InventoryGUI
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class InventoryGUI {
    private Inventory inventory;
    private JFrame frame;
    private JTextField skuField, nameField, quantityField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private final int LOW_STOCK_THRESHOLD = 10;

    public InventoryGUI(Inventory inventory) {
        this.inventory = inventory;
        initialize();
        
    }

    private void initialize() {
        frame = new JFrame("Warehouse Inventory System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
         frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Top Panel with Buttons
        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton updateButton = new JButton("Update Quantity");
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        topPanel.add(clearButton);
        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(updateButton);
        topPanel.add(searchButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Middle Panel with Input Fields
        JPanel middlePanel = new JPanel(new GridLayout(2, 4, 10, 10));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        middlePanel.add(new JLabel("SKU:"));
        skuField = new JTextField();
        middlePanel.add(skuField);
        middlePanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        middlePanel.add(nameField);
        middlePanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        middlePanel.add(quantityField);
        middlePanel.add(new JLabel("Search by SKU/Name:"));
        searchField = new JTextField();
        middlePanel.add(searchField);
        frame.add(middlePanel, BorderLayout.CENTER);

        // Table for Inventory Display
        String[] columnNames = {"SKU", "Name", "Quantity", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        frame.add(tableScroll, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addItem());
        deleteButton.addActionListener(e -> deleteItem());
        updateButton.addActionListener(e -> updateQuantity());
        searchButton.addActionListener(e -> searchItem());
        clearButton.addActionListener(e -> clearFields());

        frame.setVisible(true);
    }

    private void addItem() {
        String sku = skuField.getText().trim();
        String name = nameField.getText().trim();
        String qtyText = quantityField.getText().trim();
        if (sku.isEmpty() || name.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.");
            return;
        }
        try {
            int quantity = Integer.parseInt(qtyText);
            Item item = new Item(sku, name, quantity);
            if (inventory.addItem(item)) {
                JOptionPane.showMessageDialog(frame, "Item added successfully.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Item with this SKU already exists.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Quantity must be a number.");
        }
    }
private void clearFields() {
    skuField.setText("");
    nameField.setText("");
    quantityField.setText("");
    searchField.setText("");
}

    private void deleteItem() {
        String sku = skuField.getText().trim();
        if (sku.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter SKU to delete.");
            return;
        }
        if (inventory.deleteItem(sku)) {
            JOptionPane.showMessageDialog(frame, "Item deleted successfully.");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Item not found.");
        }
    }

    private void updateQuantity() {
        String sku = skuField.getText().trim();
        String qtyText = quantityField.getText().trim();
        if (sku.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter SKU and Quantity.");
            return;
        }
        try {
            int quantity = Integer.parseInt(qtyText);
            if (inventory.updateQuantity(sku, quantity)) {
                JOptionPane.showMessageDialog(frame, "Quantity updated successfully.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Item not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Quantity must be a number.");
        }
    }

    private void searchItem() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter SKU or Name to search.");
            return;
        }
        Item item = inventory.searchItem(query);
        if (item != null) {
            JOptionPane.showMessageDialog(frame, "Item Found:\nSKU: " + item.getSku() + "\nName: " + item.getName() + "\nQuantity: " + item.getQuantity());
        } else {
            JOptionPane.showMessageDialog(frame, "Item not found.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Item item : inventory.getAllItems()) {
            String status = item.getQuantity() < LOW_STOCK_THRESHOLD ? "Low Stock" : "In Stock";
            tableModel.addRow(new Object[]{item.getSku(), item.getName(), item.getQuantity(), status});
        }
    }
}