/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author shahadadel
 */

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Dessert extends javax.swing.JFrame {

    /**
     * Creates new form Dessert
     */
    private int userId;

    // Constructor to initialize the Dessert frame with the userId
    public Dessert(int userId) {
    this.userId = userId;
    initComponents();
    jTable1.getTableHeader().setForeground(new java.awt.Color(130, 80, 156));
    jTable1.getTableHeader().setFont(new java.awt.Font("Segoe Print", java.awt.Font.BOLD, 14));

    if (!java.beans.Beans.isDesignTime()) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        loadDessertData(model);  // Load data from the dessert table
    }

    // Action for the back button
    jButton1.addActionListener(evt -> {
        HomePage home = new HomePage(userId);
        home.setVisible(true);
        dispose();
    });

    // Action for the Submit button (Add selected dish to the order)
    jButton2.addActionListener(evt -> {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String dishName = jTable1.getValueAt(selectedRow, 1).toString();
            double price = parsePrice(jTable1.getValueAt(selectedRow, 2));
            if (price != -1) {
                addToOrder(dishName, price);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid price format.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a dish to order.");
        }
    });
}

private double parsePrice(Object priceObj) {
    try {
        return Double.parseDouble(priceObj.toString());
    } catch (NumberFormatException e) {
        return -1;
    }
}

    
    
    // Method to load the dessert data from MySQL into the JTable
    private void loadDessertData(DefaultTableModel model) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
            String query = "SELECT dessertid , dessertName, price, dessertSize FROM dessert";  // Fetch dessert data
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);  // Clear existing rows from the table

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("dessertid"),
                    rs.getString("dessertName"),
                    rs.getDouble("price"),
                    rs.getString("dessertSize")
                };
                model.addRow(row);  // Add fetched data to the table
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading desserts: " + e.getMessage());
        }
    }
    
private void addToOrder(String dessertName, double price) {
    int customerId = getCustomerIdForUser(userId);  // Get the customer_id based on the user_id
    if (customerId == -1) {
        JOptionPane.showMessageDialog(this, "Invalid customer. Please log in first.");
        return;
    }

    // Create order details and calculate total price
    String orderDetails = "Dessert: " + dessertName + " Price: " + price;
    double totalPrice = price;  // You can calculate total dynamically if there are multiple items

    // Insert the order into the customer_order table
    int orderId = insertOrderToDatabase(customerId, orderDetails, totalPrice);  // Pass customerId here

    // If order insertion is successful, show a success message
    if (orderId != -1) {
        // Insert order item into ORDER_ITEMS table for this specific dish
        insertOrderItems(orderId, dessertName, price);

        // Show success pop-up message
        JOptionPane.showMessageDialog(this, "Item added successfully to your order.");
    }
}

public int getCustomerIdForUser(int userId) {
    int customerId = -1;
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "SELECT customer_id FROM customer WHERE user_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            customerId = rs.getInt("customer_id");  // Get the customer_id based on the user_id
        }
        rs.close();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching customer_id: " + e.getMessage());
    }
    return customerId;
}

public int insertOrderToDatabase(int customerId, String orderDetails, double totalPrice) {
    int orderId = -1;
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "INSERT INTO customer_order (customer_id, order_date, status, total_amount) VALUES (?, CURRENT_TIMESTAMP, 'Pending', ?)";
        PreparedStatement pst = con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, customerId);  // Use the correct customer_id here
        pst.setDouble(2, totalPrice);
        pst.executeUpdate();

        // Get the generated order ID
        ResultSet generatedKeys = pst.getGeneratedKeys();
        if (generatedKeys.next()) {
            orderId = generatedKeys.getInt(1);  // Fetch generated order ID
        }
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error submitting order: " + e.getMessage());
    }
    return orderId;
}



private void insertOrderItems(int orderId, String itemName, double price) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "INSERT INTO order_item (order_id, menu_item_id, quantity, time, status) VALUES (?, ?, 1, CURRENT_TIMESTAMP, 'Pending')";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, orderId);
        pst.setInt(2, getMenuItemIdFromName(itemName));  // Get menu item ID based on item name
        pst.executeUpdate();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding item to order: " + e.getMessage());
    }
}

private int getMenuItemIdFromName(String itemName) {
    int menuItemId = -1;
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "SELECT menu_item_id FROM MENU WHERE menuName = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, itemName);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            menuItemId = rs.getInt("menu_item_id");
        }
        rs.close();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching menu item ID: " + e.getMessage());
    }
    return menuItemId;
}


    // Method to handle adding the selected dish to the order

    // Method to insert the order into the orders table in the database



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dessert");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 110, 20));

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Price", "Size "
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 300, 190));

        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 350));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        HomePage homePage = new HomePage(userId);
        homePage.setVisible(true);
        this.dispose();    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int userId = 12345;  // Use actual userId
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dessert(userId).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
