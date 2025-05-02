/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Shahad Adel
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class AddMenuItem extends javax.swing.JFrame {
    private String loggedInUsername;

    private JTextField nameField, categoryField, priceField, descriptionField;
    private JButton submitButton;

public AddMenuItem() {
    this("defaultUser"); // Call your main constructor with a default value
}
    
    public AddMenuItem(String username) {
        initComponents();
        this.loggedInUsername = username;

        jButton5.addActionListener(evt -> {
            try {
                handleAddMenuItem();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        jButton1.addActionListener(this::jButton1ActionPerformed); // Cancel button
    }


   private void addItem() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String price = priceField.getText();
        String description = descriptionField.getText();

        JOptionPane.showMessageDialog(this, "Item added:\n" + name + " - " + category + " - $" + price);
    }
    
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));
        jPanel1.setPreferredSize(new java.awt.Dimension(115, 24));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(95, 65, 105));
        jLabel1.setText("Add New Menu Item ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(155, 93, 176));
        jLabel2.setText("Name:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(155, 93, 176));
        jLabel4.setText("Category: ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(155, 93, 176));
        jLabel5.setText("Price: ");

        jButton5.setBackground(new java.awt.Color(204, 204, 255));
        jButton5.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton5.setText("Add Item");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dish", "Deesert", "Drink" }));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(155, 93, 176));
        jLabel6.setText("Disscount:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(155, 93, 176));
        jLabel7.setText("Size:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(155, 93, 176));
        jLabel8.setText("Quantity");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Small", "Medium", "Large" }));

        jButton1.setBackground(new java.awt.Color(204, 204, 255));
        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton1)
                        .addGap(78, 78, 78)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel5)
                        .addGap(59, 59, 59)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel6)
                        .addGap(29, 29, 29)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(80, 80, 80))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(37, 37, 37)
                .addComponent(jButton5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
private void handleAddMenuItem() throws SQLException {
    // Input validation
    String name = jTextField1.getText().trim();  // Menu Item Name
    String category = jComboBox1.getSelectedItem().toString();  // Category: Dish, Drink, Dessert
    String priceText = jTextField2.getText().trim();  // Price
    String discountText = jTextField3.getText().trim();  // Discount
    int quantity = (Integer) jSpinner1.getValue();  // Quantity
    String selectedSize = jComboBox2.getSelectedItem().toString();  // Size: Small, Medium, Large

    // Validate empty fields
    if (name.isEmpty() || priceText.isEmpty() || discountText.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Please fill in all required fields!", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Validate numeric inputs
        double price = Double.parseDouble(priceText);  // Price for the menu item
        double discount = Double.parseDouble(discountText);  // Discount for the menu item

        if (price <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Price must be greater than 0", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate that name contains only letters (optional: allow spaces)
        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, 
                "Menu name can only contain letters and spaces (no numbers or symbols).", 
                "Name Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (discount < 0) {
            JOptionPane.showMessageDialog(this, 
                "Discount cannot be negative", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Quantity must be greater than 0", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database handling code
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
            con.setAutoCommit(false);  // Start transaction

            String sql = "INSERT INTO menu (menuName, menuCategory, price, discount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, name);
                pst.setString(2, category);
                pst.setDouble(3, price);
                pst.setDouble(4, discount);
                pst.executeUpdate();

                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        int menuItemId = rs.getInt(1);  // Get the auto-generated menu_item_id

                        // Insert based on category
                        switch (category) {
                            case "Dish":
                                String dishName = jTextField1.getText().trim();  // Get the dish name
                                insertDish(con, menuItemId, quantity, selectedSize, dishName, price);  // Pass price
                                break;
                            case "Drink":
                                String drinkName = jTextField1.getText().trim();  // Get the drink name
                                insertDrink(con, menuItemId, quantity, selectedSize, drinkName, price);  // Pass price
                                break;
                            case "Dessert":
                                String dessertName = jTextField1.getText().trim();  // Get the dessert name
                                insertDessert(con, menuItemId, quantity, selectedSize, dessertName, price);  // Pass price
                                break;
                            default:
                                throw new SQLException("Invalid category selected.");
                        }
                    }
                }
            }

            con.commit();  // Commit transaction
            JOptionPane.showMessageDialog(this, "Menu item added successfully!");
            this.dispose();
            new ManageMenu().setVisible(true);

        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "Invalid numeric input. Please enter valid numbers for price and discount.", 
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Database error: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        throw e;
    }
}

private void insertDish(Connection con, int menuItemId, int quantity, String dishSize, String dishName, double price) throws SQLException {
    String sql = "INSERT INTO DISH (menu_item_id, quantity, dishSize, dishName, price) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, menuItemId);   // Set menu_item_id
        pst.setInt(2, quantity);     // Set quantity
        pst.setString(3, dishSize);  // Set dish size
        pst.setString(4, dishName);  // Set dish name
        pst.setDouble(5, price);     // Set price for dish
        pst.executeUpdate();
    }
}

private void insertDrink(Connection con, int menuItemId, int quantity, String drinkSize, String drinkName, double price) throws SQLException {
    String sql = "INSERT INTO DRINK (menu_item_id, quantity, drinkSize, drinkName, price) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, menuItemId);   // Set menu_item_id
        pst.setInt(2, quantity);     // Set quantity
        pst.setString(3, drinkSize); // Set drink size
        pst.setString(4, drinkName); // Set drink name
        pst.setDouble(5, price);     // Set price for drink
        pst.executeUpdate();
    }
}

private void insertDessert(Connection con, int menuItemId, int quantity, String dessertSize, String dessertName, double price) throws SQLException {
    String sql = "INSERT INTO DESSERT (menu_item_id, quantity, dessertSize, dessertName, price) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, menuItemId);   // Set menu_item_id
        pst.setInt(2, quantity);     // Set quantity
        pst.setString(3, dessertSize); // Set dessert size
        pst.setString(4, dessertName); // Set dessert name
        pst.setDouble(5, price);      // Set price for dessert
        pst.executeUpdate();
    }
}





    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.dispose(); // Close current window
    // Reopen AdminDash with the stored username
    new ManageMenu().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : 
             javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(AddMenuItem.class.getName())
            .log(java.util.logging.Level.SEVERE, null, ex);
    }

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // For testing purposes, provide a default username
            new AddMenuItem().setVisible(true);
        }
    });

}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
