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
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddStaff extends javax.swing.JFrame {

    /**
     * Creates new form AddStaff
     */
    private String loggedInUsername;

    public AddStaff() {
        initComponents();
        jButton5.addActionListener(e -> addStaffToDatabase());
        jButton4.addActionListener(e -> clearFields());
        jButton6.addActionListener(e -> {
            this.dispose();
            new AdminDash("admin").setVisible(true);
        });
    }
    

private void addStaffToDatabase() {
    String firstName = jTextField1.getText().trim();
    String lastName = jTextField2.getText().trim();
    String email = jTextField3.getText().trim();
    String phone = jTextField4.getText().trim();
    String salaryStr = jTextField5.getText().trim();
    String password = jTextField6.getText().trim();
    String username = jTextField7.getText().trim();  // Get the username from jTextField7

    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || salaryStr.isEmpty() || password.isEmpty() || username.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Info", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
        // First and last name validation
    if (!firstName.matches("[a-zA-Z ]+")) {
        JOptionPane.showMessageDialog(this, "First name can only contain letters.", "Invalid First Name", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!lastName.matches("[a-zA-Z ]+")) {
        JOptionPane.showMessageDialog(this, "Last name can only contain letters.", "Invalid Last Name", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Username validation
    if (!username.matches("[a-zA-Z0-9_]+")) {
        JOptionPane.showMessageDialog(this, "Username can only contain letters, numbers, and underscores.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double salary = Double.parseDouble(salaryStr);

        // Prompt user for Admin or Employee role
        String[] options = {"Admin", "Employee"};
        int roleChoice = JOptionPane.showOptionDialog(this, "Is this staff an Admin or Employee?", "Role Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (roleChoice == JOptionPane.CLOSED_OPTION) return;  // If the user closed the dialog, do nothing

        String userType = (roleChoice == 0) ? "Admin" : "Employee"; // Set the user type
        String department = "";
        
        if (salary <= 0) {
            JOptionPane.showMessageDialog(this, "Salary must be greater than 0.", "Invalid Salary", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If Employee, ask for department
        if (roleChoice == 1) {
            department = JOptionPane.showInputDialog(this, "Enter department for Employee:");
            if (department == null || department.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department is required for Employee.", "Missing Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
    

        // Database connection
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya");

        // Check for duplicate email or phone
        String checkQuery = "SELECT * FROM USER WHERE email = ? OR phone = ? OR username = ?";
        PreparedStatement checkPst = con.prepareStatement(checkQuery);
        checkPst.setString(1, email);
        checkPst.setString(2, phone);
        checkPst.setString(3, username);  // Check if username already exists
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Email, phone number, or username already exists.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            rs.close();
            checkPst.close();
            con.close();
            return;
        }

        rs.close();
        checkPst.close();

        // Insert into USER table with username
        String insertUserQuery = "INSERT INTO USER (first_name, last_name, email, phone, password, user_type, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement userPst = con.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        userPst.setString(1, firstName);
        userPst.setString(2, lastName);
        userPst.setString(3, email);
        userPst.setString(4, phone);
        userPst.setString(5, password);
        userPst.setString(6, userType);  // Admin or Employee
        userPst.setString(7, username);  // Insert the username

        int rowsInserted = userPst.executeUpdate();
        if (rowsInserted > 0) {
            // Get the generated user_id
            ResultSet generatedKeys = userPst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // If Employee, insert into EMPLOYEE table
                if (userType.equals("Employee")) {
                    String insertEmployeeQuery = "INSERT INTO EMPLOYEE (user_id, salary, department) VALUES (?, ?, ?)";
                    PreparedStatement employeePst = con.prepareStatement(insertEmployeeQuery);
                    employeePst.setInt(1, userId);
                    employeePst.setDouble(2, salary);
                    employeePst.setString(3, department);
                    employeePst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Employee added successfully!");
                }
                // If Admin, insert into ADMIN table
                else if (userType.equals("Admin")) {
                    String insertAdminQuery = "INSERT INTO ADMIN (user_id, role) VALUES (?, ?)";
                    PreparedStatement adminPst = con.prepareStatement(insertAdminQuery);
                    adminPst.setInt(1, userId);
                    adminPst.setString(2, "Admin");
                    adminPst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Admin added successfully!");
                }

                clearFields();  // Clear the form
            }
        }

        userPst.close();
        con.close();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number for salary.", "Invalid Salary", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void clearFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
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
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 102));
        jLabel3.setText("@username");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 4, -1, -1));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(95, 65, 105));
        jLabel1.setText("Add New Staff");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, -1, -1));

        jLabel2.setForeground(new java.awt.Color(155, 93, 176));
        jLabel2.setText("First Name:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 35, -1, -1));

        jLabel4.setForeground(new java.awt.Color(155, 93, 176));
        jLabel4.setText("Last Name:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, -1, -1));

        jLabel5.setForeground(new java.awt.Color(155, 93, 176));
        jLabel5.setText("Email:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 163, -1, -1));

        jLabel6.setForeground(new java.awt.Color(155, 93, 176));
        jLabel6.setText("Phone Number:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 227, -1, -1));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 58, 140, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 170, -1));
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 186, 399, -1));
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 250, 399, -1));

        jButton4.setBackground(new java.awt.Color(233, 233, 250));
        jButton4.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jButton4.setForeground(new java.awt.Color(204, 0, 51));
        jButton4.setText("Clear");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 406, 63, -1));

        jButton5.setBackground(new java.awt.Color(204, 204, 255));
        jButton5.setFont(new java.awt.Font("Helvetica Neue", 1, 8)); // NOI18N
        jButton5.setText("Add");
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 430, 63, -1));

        jButton6.setBackground(new java.awt.Color(204, 204, 255));
        jButton6.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jButton6.setText("Back");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 406, 63, -1));

        jLabel9.setForeground(new java.awt.Color(155, 93, 176));
        jLabel9.setText("Salary:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 330, -1, -1));
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 353, 138, -1));

        jLabel7.setForeground(new java.awt.Color(155, 93, 176));
        jLabel7.setText("Password");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 277, -1, -1));
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 400, -1));

        jLabel10.setForeground(new java.awt.Color(155, 93, 176));
        jLabel10.setText("Username");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 390, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddStaff().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
