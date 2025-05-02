
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author shahadadel
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Dish extends javax.swing.JFrame {

    /**
     * Creates new form Dish
     */
    private int userId;  // Add userId for order context if necessary

public Dish(int userId) {
    this.userId = userId;  // Store userId for future use like placing orders
    initComponents();
    jTable1.getTableHeader().setForeground(new java.awt.Color(130, 80, 156));
    jTable1.getTableHeader().setFont(new java.awt.Font("Segoe Print", java.awt.Font.BOLD, 14));

    // Load data from database
    if (!java.beans.Beans.isDesignTime()) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        loadDishData(model);  // Load dishes from the database
    }

    // Action for the back button
    jButton1.addActionListener(evt -> {
        HomePage home = new HomePage(userId);
        home.setVisible(true);
        dispose();
    });

    // Action for the Submit button
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


private double parsePrice(Object priceObj) {
    try {
        return Double.parseDouble(priceObj.toString());
    } catch (NumberFormatException e) {
        return -1;
    }
}

    
private void loadDishData(DefaultTableModel model) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "SELECT dishId, dishName, dishSize, price FROM dish";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        model.setRowCount(0);  // Clear any existing data in the table

        while (rs.next()) {
            // Ensure price is properly fetched as a double or decimal
            Object[] row = {
                    rs.getInt("dishId"),
                    rs.getString("dishName"),
                    rs.getDouble("price"),  // This should ensure proper numeric value is fetched
                    rs.getString("dishSize")
            };
            model.addRow(row);
        }

        rs.close();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading dishes: " + e.getMessage());
    }
}

private void addToOrder(String itemName, double price) {
    int customerId = getCustomerIdForUser(userId);  // Get the customer_id based on the user_id
    if (customerId == -1) {
        JOptionPane.showMessageDialog(this, "Invalid customer. Please log in first.");
        return;
    }

    // Create order details and calculate total price
    String orderDetails = "Dish: " + itemName + " Price: " + price;
    double totalPrice = price;  // You can calculate total dynamically if there are multiple items

    // Insert the order into the customer_order table
    int orderId = insertOrderToDatabase(customerId, orderDetails, totalPrice);  // Pass customerId here

    // If order insertion is successful, show a success message
    if (orderId != -1) {
        // Insert order item into ORDER_ITEMS table for this specific dish
        insertOrderItems(orderId, itemName, price);

        // Show success pop-up message
        JOptionPane.showMessageDialog(this, "Item added successfully to your order.");
    }
}


public int insertOrderToDatabase(int customerId, String orderDetails, double totalPrice) {
    int orderId = -1;
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "INSERT INTO customer_order (customer_id, order_date, status, total_amount) VALUES (?, CURRENT_TIMESTAMP, 'Pending', ?)";
        PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
        String query = "SELECT menu_item_id FROM menu WHERE menuName = ?";
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

private boolean isCustomerValid(int userId) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        // Query the customer table to check if the userId matches a valid customer_id
        String query = "SELECT COUNT(*) FROM customer WHERE user_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return true;  // Customer exists for the given userId
        } else {
            return false;  // No customer found with the provided userId
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error checking customer: " + e.getMessage());
        return false;
    }
}





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
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dish");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 80, 20));

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, -1));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        HomePage homePage = new HomePage(userId);
        homePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Use the actual userId when launching the frame
        int userId = 12345; // Replace with actual userId from the login system
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dish(userId).setVisible(true);
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
