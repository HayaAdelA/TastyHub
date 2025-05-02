/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author janasaleh
 */

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditStaff extends javax.swing.JFrame {
    
    private int staffId;

    /**
     * Creates new form EditStaff
     */
    
    public EditStaff() {} 

    public EditStaff(int id) {
        this.staffId = id;
        initComponents();
        loadStaffData(); // Optional: you can create this method to populate the fields
        jButton7.addActionListener(e -> updateStaffInDB());
    }
    
    private void loadStaffData() {
        try {
            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya");

            // Query to fetch staff data from USER and EMPLOYEE tables
            String query = "SELECT u.first_name, u.last_name, u.email, u.phone, e.salary " +
                           "FROM USER u " +
                           "JOIN EMPLOYEE e ON u.user_id = e.user_id " +
                           "WHERE e.employee_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, staffId);  // Use the employee ID to fetch the data
            ResultSet rs = pst.executeQuery();

            // If data is found, populate the text fields
            if (rs.next()) {
                jTextField1.setText(rs.getString("first_name"));
                jTextField2.setText(rs.getString("last_name"));
                jTextField4.setText(rs.getString("phone"));
                jTextField5.setText(rs.getString("email"));  // For email
                jTextField3.setText(rs.getString("salary"));
            }

            pst.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
  private void updateStaffInDB() {
        try {
            // Validate input fields
            String firstName = jTextField1.getText().trim();
            String lastName = jTextField2.getText().trim();
            String email = jTextField5.getText().trim();
            String phone = jTextField4.getText().trim();
            String salaryStr = jTextField3.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number format (must start with +9965 or +9665 and be followed by 8 digits)
            if (!phone.matches("\\+9965\\d{8}|\\+9665\\d{8}")) {
                JOptionPane.showMessageDialog(this, "Phone number must start with +9965 or +9665 and have 8 digits.", "Invalid Phone", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya");

            // Update USER table
            String updateUserQuery = "UPDATE USER SET first_name = ?, last_name = ?, email = ?, phone = ? WHERE user_id = (SELECT user_id FROM EMPLOYEE WHERE employee_id = ?)";
            PreparedStatement userPst = con.prepareStatement(updateUserQuery);
            userPst.setString(1, firstName);
            userPst.setString(2, lastName);
            userPst.setString(3, email);
            userPst.setString(4, phone);
            userPst.setInt(5, staffId); // Use the employee ID to find the corresponding user_id
            userPst.executeUpdate();

            // Update EMPLOYEE table
            String updateEmployeeQuery = "UPDATE EMPLOYEE SET salary = ? WHERE employee_id = ?";
            PreparedStatement empPst = con.prepareStatement(updateEmployeeQuery);
            empPst.setDouble(1, Double.parseDouble(salaryStr));
            empPst.setInt(2, staffId); // Use the employee ID to update the salary
            empPst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Staff updated successfully!");
            this.dispose(); // Close the edit window
            new AdminDash("admin").setVisible(true); // Go back to the admin dashboard

            userPst.close();
            empPst.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating staff data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 102));
        jLabel3.setText("@username");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(95, 65, 105));
        jLabel1.setText("Edit Staff Details");

        jLabel2.setForeground(new java.awt.Color(155, 93, 176));
        jLabel2.setText("First Name:");

        jLabel4.setForeground(new java.awt.Color(155, 93, 176));
        jLabel4.setText("Last Name:");

        jLabel6.setForeground(new java.awt.Color(155, 93, 176));
        jLabel6.setText("Email:");
        jLabel6.setToolTipText("");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(155, 93, 176));
        jLabel9.setText("Phone Number:");

        jButton7.setBackground(new java.awt.Color(204, 204, 255));
        jButton7.setFont(new java.awt.Font("Helvetica Neue", 1, 8)); // NOI18N
        jButton7.setText("Done");

        jButton8.setBackground(new java.awt.Color(204, 204, 255));
        jButton8.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jButton8.setText("Back");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(233, 233, 250));
        jButton9.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jButton9.setForeground(new java.awt.Color(204, 0, 51));
        jButton9.setText("Clear");

        jLabel12.setForeground(new java.awt.Color(155, 93, 176));
        jLabel12.setText("Salary");

        jLabel15.setForeground(new java.awt.Color(155, 93, 176));
        jLabel15.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel12)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1))
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(167, 352, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton7))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 490));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    this.dispose();
    new AdminDash("admin").setVisible(true);


    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) {
        new EditStaff(123444).setVisible(true);
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
