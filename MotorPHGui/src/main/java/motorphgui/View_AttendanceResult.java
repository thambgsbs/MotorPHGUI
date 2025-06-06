/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package motorphgui;

/**
 *
 * @author admin
 */
public class View_AttendanceResult extends javax.swing.JDialog {

    /**
     * Creates new form Employee_Details
     */
    public View_AttendanceResult(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_main = new javax.swing.JLabel();
        lbl_ED = new javax.swing.JLabel();
        lbl_ED1 = new javax.swing.JLabel();
        lbl_Att_EmpID = new javax.swing.JLabel();
        lbl_Att_COP = new javax.swing.JLabel();
        lbl_ED2 = new javax.swing.JLabel();
        lbl_ED3 = new javax.swing.JLabel();
        lbl_ED4 = new javax.swing.JLabel();
        lbl_ED5 = new javax.swing.JLabel();
        lbl_Att_TWHours = new javax.swing.JLabel();
        lbl_Att_RegHours = new javax.swing.JLabel();
        lbl_Att_OT = new javax.swing.JLabel();
        lbl_Att_LMin = new javax.swing.JLabel();
        btn_Results_Home = new javax.swing.JButton();
        btn_Results_Home1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbl_main.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lbl_main.setText("Attendance");

        lbl_ED.setText("Enter Employee ID (10001-100034):");

        lbl_ED1.setText("Cut-off Period");

        lbl_Att_EmpID.setText("Att_Res_EmpID");

        lbl_Att_COP.setText("Att_Res_COP");

        lbl_ED2.setText("Total Work Hours:");

        lbl_ED3.setText("Regular Hours");

        lbl_ED4.setText("Overtime Hours");

        lbl_ED5.setText("Late Minutes:");

        lbl_Att_TWHours.setText("Att_Res_TWHours");

        lbl_Att_RegHours.setText("Att_Res_RegHours");

        lbl_Att_OT.setText("Att_Res_OT");

        lbl_Att_LMin.setText("Att_Res_LMin");

        btn_Results_Home.setText("Back to Main");
        btn_Results_Home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Results_HomeActionPerformed(evt);
            }
        });

        btn_Results_Home1.setText("Exit");
        btn_Results_Home1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Results_Home1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_RegHours, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_TWHours, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_EmpID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_main)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_COP, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_OT, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_ED5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Att_LMin, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btn_Results_Home)
                        .addGap(84, 84, 84)
                        .addComponent(btn_Results_Home1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_main)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED)
                    .addComponent(lbl_Att_EmpID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED1)
                    .addComponent(lbl_Att_COP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED2)
                    .addComponent(lbl_Att_TWHours))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED3)
                    .addComponent(lbl_Att_RegHours))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED4)
                    .addComponent(lbl_Att_OT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ED5)
                    .addComponent(lbl_Att_LMin))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Results_Home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Results_Home1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_Results_HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Results_HomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Results_HomeActionPerformed

    private void btn_Results_Home1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Results_Home1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Results_Home1ActionPerformed

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
            java.util.logging.Logger.getLogger(View_AttendanceResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_AttendanceResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_AttendanceResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_AttendanceResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                View_AttendanceResult dialog = new View_AttendanceResult(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Results_Home;
    private javax.swing.JButton btn_Results_Home1;
    private javax.swing.JLabel lbl_Att_COP;
    private javax.swing.JLabel lbl_Att_EmpID;
    private javax.swing.JLabel lbl_Att_LMin;
    private javax.swing.JLabel lbl_Att_OT;
    private javax.swing.JLabel lbl_Att_RegHours;
    private javax.swing.JLabel lbl_Att_TWHours;
    private javax.swing.JLabel lbl_ED;
    private javax.swing.JLabel lbl_ED1;
    private javax.swing.JLabel lbl_ED2;
    private javax.swing.JLabel lbl_ED3;
    private javax.swing.JLabel lbl_ED4;
    private javax.swing.JLabel lbl_ED5;
    private javax.swing.JLabel lbl_main;
    // End of variables declaration//GEN-END:variables
}
