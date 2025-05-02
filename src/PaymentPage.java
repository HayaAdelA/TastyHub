/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/**
 *
 * @author lolos
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
import java.awt.Desktop;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class PaymentPage extends javax.swing.JFrame {

    private int customerId; // Store the customer_id for the logged-in user
    private int userId;

    public PaymentPage(int userId) {
        this.userId = userId; 
        this.customerId = getCustomerIdForUser(userId); // Retrieve customer_id for the user
        if (this.customerId == -1) {
            JOptionPane.showMessageDialog(this, "Customer ID not found.");
            return;
        }
        initComponents();
        displayOrderDetails();  // Load orders for this customer
    }

    
 private void displayOrderDetails() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
            String query = "SELECT o.order_id, oi.menu_item_id, m.menuName, oi.quantity, oi.time, o.total_amount " +
                           "FROM CUSTOMER_ORDER o " +
                           "JOIN order_item oi ON o.order_id = oi.order_id " +
                           "JOIN menu m ON oi.menu_item_id = m.menu_item_id " +
                           "WHERE o.customer_id = ?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, customerId);  // Use the correct customer_id
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);  // Clear previous rows

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getString("menuName"),
                    rs.getInt("quantity"),
                    rs.getTimestamp("time"),
                    rs.getDouble("total_amount")
                };
                model.addRow(row);
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading order details: " + e.getMessage());
        }
    }
 
    private int getOrderIdForCustomer(int customerId) {
        int orderId = -1;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
            String query = "SELECT order_id FROM customer_order WHERE customer_id = ? AND status = 'Pending' LIMIT 1";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, customerId);  // Fetch order_id based on customer_id
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching order_id: " + e.getMessage());
        }
        return orderId;
    }

private void savePaymentToDatabase(double totalAmount, String paymentMethod) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "INSERT INTO payment (order_id, customer_id, payment_method, payment_status, total_amount) VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement pst = con.prepareStatement(query);
        
        int orderId = getOrderIdForCustomer(customerId); // Fetch the order ID for the customer
        if (orderId == -1) {
            JOptionPane.showMessageDialog(this, "No pending order found for this customer.");
            return;
        }
        
        pst.setInt(1, orderId);          // Set the order_id
        pst.setInt(2, customerId);       // Set the customer_id
        pst.setString(3, paymentMethod); // Set the payment method
        pst.setString(4, "Finished");    // Set payment status to "Finished"
        pst.setDouble(5, totalAmount);   // Set total_amount

        pst.executeUpdate();

        // Update the order status to "Finished"
        updateOrderStatus(orderId);

        JOptionPane.showMessageDialog(this, "Payment details saved successfully.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error saving payment details: " + e.getMessage());
    }
}

private void updateOrderStatus(int orderId) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        String query = "UPDATE customer_order SET status = 'Finished' WHERE order_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, orderId);
        pst.executeUpdate();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error updating order status: " + e.getMessage());
    }
}

    private void generateReceipt(double totalAmount, String paymentMethod) {
        try {
            String receiptFilePath = "receipt_" + System.currentTimeMillis() + ".txt"; // Use current timestamp as part of file name
            FileWriter writer = new FileWriter(receiptFilePath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("Tasty Hub - Payment Receipt\n");
            bufferedWriter.write("----------------------------\n");
            bufferedWriter.write("Total Amount: $" + totalAmount + "\n");
            bufferedWriter.write("Payment Method: " + paymentMethod + "\n");
            bufferedWriter.write("----------------------------\n");
            bufferedWriter.write("Thank you for your order!\n");
            bufferedWriter.write("Your receipt is saved as a .txt file.\n");

            bufferedWriter.close();

            JOptionPane.showMessageDialog(this, "Receipt saved successfully at " + receiptFilePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating receipt: " + e.getMessage());
        }
    }
    private int getCustomerIdForUser(int userId) {
        int customerId = -1;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
            String query = "SELECT customer_id FROM customer WHERE user_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, userId);  // Use user_id to retrieve customer_id
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                customerId = rs.getInt("customer_id");  // Get the customer_id
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching customer_id: " + e.getMessage());
        }
        return customerId;
    }




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setBackground(new java.awt.Color(157, 51, 210));
        jButton2.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(95, 65, 105));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tasty Hub - Payment");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11222", "25", "10", "0", "35"},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "OrderID", "Dish Price", "Desser Price", "Drink Price ", "Total "
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 330, 90));

        jRadioButton1.setText("Credit/Debit Card");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, -1, -1));

        jRadioButton2.setText("Cash");
        jPanel1.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        jButton3.setBackground(new java.awt.Color(157, 51, 210));
        jButton3.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Pay");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 310));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         // Go back to previous page (order selection page)
        HomePage orderPage = new HomePage(userId);
        orderPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    // Get selected rows
    int[] selectedRows = jTable2.getSelectedRows();  // This will get all selected rows
    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(this, "Please select at least one order to pay.");
        return;  // Exit the method if no row is selected
    }

    // Ensure the user selects a payment method
    String paymentMethod = "";
    if (jRadioButton1.isSelected()) {
        paymentMethod = "Credit/Debit Card";
    } else if (jRadioButton2.isSelected()) {
        paymentMethod = "Cash";
    } else {
        JOptionPane.showMessageDialog(this, "Please select a payment method.");
        return;  // Exit the method if no payment method is selected
    }

    // Calculate total amount for selected rows
    double totalAmount = 0.0;

    for (int i = 0; i < selectedRows.length; i++) {
        // Get the order_id and total amount for each selected row
        int orderId = (int) jTable2.getValueAt(selectedRows[i], 0);  // Get order_id from the first column
        totalAmount += (double) jTable2.getValueAt(selectedRows[i], 4); // Add the total amount (4th column)
    }

    // Process payment and save payment details
    if (processPayment(paymentMethod)) {
        savePaymentToDatabase(totalAmount, paymentMethod);  // Save payment for multiple orders
        JOptionPane.showMessageDialog(this, "Payment successful!");

        // Generate receipt as a text file
        generateReceipt(totalAmount, paymentMethod);

        // Remove the selected rows from the table after successful payment
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);  // Remove each selected row
        }
    } else {
        JOptionPane.showMessageDialog(this, "Payment failed. Please try again.");
    }                     

    }//GEN-LAST:event_jButton3ActionPerformed

private void deleteSelectedOrderFromDatabase() {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya")) {
        int orderId = (int) jTable2.getValueAt(jTable2.getSelectedRow(), 0); // Get order_id from the table

        // Delete the payment record first if exists
        String paymentQuery = "DELETE FROM payment WHERE order_id = ?";
        PreparedStatement pstPayment = con.prepareStatement(paymentQuery);
        pstPayment.setInt(1, orderId);
        pstPayment.executeUpdate();

        // Delete the order_item from the order_item table
        String query = "DELETE FROM order_item WHERE order_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, orderId);
        pst.executeUpdate();

        // Now delete the order from customer_order table
        String orderQuery = "DELETE FROM customer_order WHERE order_id = ?";
        PreparedStatement pstOrder = con.prepareStatement(orderQuery);
        pstOrder.setInt(1, orderId);
        pstOrder.executeUpdate();

        pst.close();
        pstPayment.close();
        pstOrder.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error deleting order: " + e.getMessage());
    }
}

    
 private boolean processPayment(String paymentMethod) {
        // Simulate payment logic (this is where you'd connect to a payment gateway)
        // For now, assume the payment is always successful
        return true;
    }
         

// Get the total amount from the table (last column)



     

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
