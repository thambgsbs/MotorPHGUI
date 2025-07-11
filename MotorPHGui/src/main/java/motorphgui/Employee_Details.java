package motorphgui;

import java.io.*;
import javax.swing.JOptionPane;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Employee_Details extends javax.swing.JDialog {

    public Employee_Details(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    
    
    private void initComponents() {
        lbl_main = new javax.swing.JLabel();
        lbl_ED = new javax.swing.JLabel();
        txt_EDEmpID = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbl_main.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); 
        lbl_main.setText("Employee Details");

        lbl_ED.setText("Enter Employee ID (10001-10034):");

        txt_EDEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_EDEmpIDActionPerformed(evt);
            }
        });

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_ED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_EDEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_main)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_main)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbl_ED)
                    .addComponent(txt_EDEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }

    private void txt_EDEmpIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Not used, can be left empty or removed
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose(); 
        String inputID = txt_EDEmpID.getText().trim();
        String[] empData = getEmployeeDataByID(inputID);

        if (empData != null) {
            Employee_DetailsResult resultDialog = new Employee_DetailsResult(null, true, empData);
            resultDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Employee ID not found!");
        }
    }

    
private String[] getEmployeeDataByID(String inputID) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("employee-data.csv");
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        if (inputStream == null) {
            System.out.println("❌ Couldn't find employee-data.csv in resources!");
            return null;
        }

        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            if (nextLine.length > 0 && nextLine[0].trim().equals(inputID)) {
                return nextLine;
            }
        }
    } catch (IOException | CsvValidationException e) {
        e.printStackTrace();
    }

    return null;
}
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Employee_Details dialog = new Employee_Details(new javax.swing.JFrame(), true);
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


    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lbl_ED;
    private javax.swing.JLabel lbl_main;
    private javax.swing.JTextField txt_EDEmpID;
    // End of variables declaration
}