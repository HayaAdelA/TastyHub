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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;


public class ManageStaff extends javax.swing.JFrame {
    
    

    /**
     * Creates new form ManageStaff
     */
    public ManageStaff() {
        initComponents();
        loadStaffData(); // Call this to load data into the table

        // Action listeners for buttons
        jButton1.addActionListener(e -> {
            new AddStaff().setVisible(true);
            this.dispose();
        });
        
        jButton2.addActionListener(e -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff to edit.");
            } else {
                int id = (Integer) jTable1.getValueAt(selectedRow, 0);
                new EditStaff(id).setVisible(true); // Open EditStaff frame
                this.dispose();
            }
        });

        jButton3.addActionListener(e -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff to remove.");
            } else {
                int id = (Integer) jTable1.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    removeStaffFromDatabase(id);
                }
            }
        });

        jButton4.addActionListener(e -> {
            new AdminDash("admin").setVisible(true);
            this.dispose();
        });
    }
    

    private void loadStaffData() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya");
        String sql = "SELECT e.employee_id, u.first_name, u.last_name, u.email, u.phone, e.salary " +
                     "FROM USER u " +
                     "JOIN EMPLOYEE e ON u.user_id = e.user_id";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        // Create a table model and populate it with the database results
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Email");
        model.addColumn("Phone");
        model.addColumn("Salary");

        // Add each row of the result to the table model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("employee_id"),  // Correct field name
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDouble("salary")
            });
        }

        // Set the model to the table
        jTable1.setModel(model);

        rs.close();
        pst.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading staff data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


private void removeStaffFromDatabase(int id) {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tastyhub", "root", "queen haya");
        
        // Begin transaction
        con.setAutoCommit(false);

        // Step 1: Retrieve the user_id associated with the employee_id
        String getUserIdSQL = "SELECT user_id FROM EMPLOYEE WHERE employee_id = ?";
        PreparedStatement pstGetUserId = con.prepareStatement(getUserIdSQL);
        pstGetUserId.setInt(1, id);
        ResultSet rs = pstGetUserId.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("user_id"); // Get the user_id associated with the employee_id
            
            // Step 2: Delete from the EMPLOYEE table
            String deleteEmployeeSQL = "DELETE FROM EMPLOYEE WHERE employee_id = ?";
            PreparedStatement pstEmployee = con.prepareStatement(deleteEmployeeSQL);
            pstEmployee.setInt(1, id);
            int deletedEmployee = pstEmployee.executeUpdate();
            
            if (deletedEmployee > 0) {
                // Step 3: Delete from the USER table
                String deleteUserSQL = "DELETE FROM USER WHERE user_id = ?";
                PreparedStatement pstUser = con.prepareStatement(deleteUserSQL);
                pstUser.setInt(1, userId);  // Use the retrieved user_id
                int deletedUser = pstUser.executeUpdate();
                
                if (deletedUser > 0) {
                    con.commit();  // Commit transaction if both deletes are successful
                    JOptionPane.showMessageDialog(this, "Staff and associated user removed successfully.");
                    loadStaffData();  // Reload the staff data in your table
                } else {
                    con.rollback();  // Rollback transaction if deletion from USER table fails
                    JOptionPane.showMessageDialog(this, "Failed to remove user with ID: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
                }
                pstUser.close();
            } else {
                con.rollback();  // Rollback if deletion from EMPLOYEE table fails
                JOptionPane.showMessageDialog(this, "No staff found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
            pstEmployee.close();
        } else {
            JOptionPane.showMessageDialog(this, "No employee found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        pstGetUserId.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
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

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(242, 230, 248));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(95, 65, 105));
        jLabel1.setText("Admin-Manage Staff");

        jButton1.setBackground(new java.awt.Color(185, 123, 248));
        jButton1.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jButton1.setText("Add New Staff");

        jButton2.setBackground(new java.awt.Color(185, 123, 248));
        jButton2.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jButton2.setText("Edit Staff Details");

        jButton3.setBackground(new java.awt.Color(185, 123, 248));
        jButton3.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jButton3.setText("Remove Staff");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 102));
        jLabel2.setText("@username");

        jButton4.setBackground(new java.awt.Color(204, 204, 255));
        jButton4.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        jButton4.setText("Back");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(12345678), "Hatem", "Saad", "Abcd@Email.com",  new Integer(12345),  new Integer(5000)},
                { new Integer(87654321), "Nasser", "Abdullah", "Dcba@Email.com",  new Integer(54321),  new Integer(6000)},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "First Name", "Last Name", "Email", "Phone No", "Salary"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(283, 283, 283)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addGap(55, 55, 55))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ManageStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStaff().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
