/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package motorphgui;

import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;




/**
 *
 * @author admin
 */
public class Employee_PayrollResult extends javax.swing.JDialog {
    
    private final String empID;
    private final String cutoffPeriod;    

    public Employee_PayrollResult(java.awt.Frame parent, boolean modal, String empID, String cutoff) {
        super(parent, modal);
        this.empID = empID;
        this.cutoffPeriod = cutoff;
        initComponents();
        processPayroll();
    }

private void processPayroll() {
    List<String[]> attendanceData = CSVUtil.readCSV("src/main/resources/attendance_data.csv");

    if (attendanceData == null || attendanceData.size() <= 1) {
        JOptionPane.showMessageDialog(this, "Attendance data not found or empty.");
        return;
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    LocalDate cutoffDate;
    try {
        cutoffDate = LocalDate.parse(cutoffPeriod, DateTimeFormatter.ofPattern("MMMM d yyyy"));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid cutoff period format: " + cutoffPeriod);
        return;
    }

    double totalWorkHours = 0.0;
    double totalRegularHours = 0.0;
    double totalOvertimeHours = 0.0;
    long totalLateMinutes = 0;
    boolean nameSet = false;

    for (int i = 1; i < attendanceData.size(); i++) {
        String[] row = attendanceData.get(i);
        if (row.length < 6) continue;
        if (!row[0].trim().equals(empID)) continue;

        if (!nameSet) {
            lbl_resName.setText(row[1].trim() + ", " + row[2].trim());
            nameSet = true;
        }

        try {
            LocalDate date = LocalDate.parse(row[3].trim(), dateFormatter);
            if (date.isAfter(cutoffDate)) continue;

            LocalTime timeIn = LocalTime.parse(row[4].trim(), timeFormatter);
            LocalTime timeOut = LocalTime.parse(row[5].trim(), timeFormatter);

            Duration workDuration = Duration.between(timeIn, timeOut);
            double workHours = workDuration.toMinutes() / 60.0;
            totalWorkHours += workHours;

            LocalTime lateThreshold = LocalTime.of(9, 10);
            if (timeIn.isAfter(lateThreshold)) {
                totalLateMinutes += Duration.between(lateThreshold, timeIn).toMinutes();
            }

            LocalTime regStart = LocalTime.of(9, 0);
            LocalTime regEnd = LocalTime.of(17, 0);
            LocalTime actualStart = timeIn.isAfter(regStart) ? timeIn : regStart;
            LocalTime actualEnd = timeOut.isBefore(regEnd) ? timeOut : regEnd;

            if (actualEnd.isAfter(actualStart)) {
                totalRegularHours += Duration.between(actualStart, actualEnd).toMinutes() / 60.0;
            }

            LocalTime overtimeStart = LocalTime.of(17, 10);
            if (timeOut.isAfter(overtimeStart)) {
                totalOvertimeHours += Duration.between(overtimeStart, timeOut).toMinutes() / 60.0;
            }

        } catch (Exception ex) {
            System.out.println("Skipping row due to parse error: " + String.join(",", row));
            ex.printStackTrace();
        }
    }

        lbl_resID.setText(empID);
        lbl_cutoffperiod.setText(cutoffPeriod);
        lbl_Att_TWHours.setText(String.format("%.2f", totalWorkHours) + " hrs");
        lbl_Att_RegHours.setText(String.format("%.2f", totalRegularHours) + " hrs");
        lbl_Att_OT.setText(String.format("%.2f", totalOvertimeHours) + " hrs");
        lbl_Att_LMin.setText(totalLateMinutes + " min");

        List<String[]> employeeData = CSVUtil.readCSV("src/main/resources/employee-data.csv");
        if (employeeData == null || employeeData.size() <= 1) {
            JOptionPane.showMessageDialog(this, "Employee data not found.");
            return;
        }

        double grossPay = 0.0;
        double overtimePay = 0.0;
        double lateDeduction = 0.0;
        double sss = 0.0;
        double pagibig = 0.0;
        double philhealth = 0.0;
        double tax = 0.0;
        double takeHomePay = 0.0;

        for (int i = 1; i < employeeData.size(); i++) {
            String[] data = employeeData.get(i);
            if (data.length >= 19 && data[0].trim().equals(empID)) {
                lbl_resRiceSub.setText(formatCurrency(data[14]));
                lbl_resPhoneAllowance.setText(formatCurrency(data[15]));
                lbl_resClothing.setText(formatCurrency(data[16]));
                lbl_resHourly.setText(formatCurrency(data[18]));

                double rice = parseAmount(data[14]) / 2;
                double phone = parseAmount(data[15]) / 2;
                double clothing = parseAmount(data[16]) / 2;
                double hourlyRate = parseAmount(data[18]);

                double proratedTotal = rice + phone + clothing;
                lbl_totalBen.setText(formatCurrency(proratedTotal));

                grossPay = totalRegularHours * hourlyRate;
                overtimePay = totalOvertimeHours * hourlyRate * 1.25;
                lateDeduction = (hourlyRate / 60.0) * totalLateMinutes;

                lbl_grosspay.setText(formatCurrency(grossPay));
                lbl_overtime.setText(formatCurrency(overtimePay));
                lbl_latededuction.setText(formatCurrency(lateDeduction));

                // Government Deductions & Tax
                GovernmentDeduction gov = new GovernmentDeduction(grossPay);
                sss = gov.getSSS();
                pagibig = gov.getPagIbig();
                philhealth = gov.getPhilHealth();
                tax = TaxComputation.computeTax(grossPay);

                lbl_SSS.setText(formatCurrency(sss));
                lbl_pagibig.setText(formatCurrency(pagibig));
                lbl_philhealth.setText(formatCurrency(philhealth));
                lbl_tax.setText(formatCurrency(tax));

                // Take-home Pay
                takeHomePay = grossPay + overtimePay + proratedTotal - lateDeduction - sss - pagibig - philhealth - tax;
                lbl_takehome.setText(formatCurrency(takeHomePay));
                break;
            }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lbl_resID = new javax.swing.JLabel();
        lbl_resName = new javax.swing.JLabel();
        lbl_resRiceSub = new javax.swing.JLabel();
        lbl_resPhoneAllowance = new javax.swing.JLabel();
        lbl_resClothing = new javax.swing.JLabel();
        lbl_resHourly = new javax.swing.JLabel();
        btn_Results_Home = new javax.swing.JButton();
        btn_Results_Home1 = new javax.swing.JButton();
        btn_Results_UpdateRec = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        lbl_cutoffperiod = new javax.swing.JLabel();
        lbl_main1 = new javax.swing.JLabel();
        lbl_ED4 = new javax.swing.JLabel();
        lbl_ED5 = new javax.swing.JLabel();
        lbl_Att_TWHours = new javax.swing.JLabel();
        lbl_Att_RegHours = new javax.swing.JLabel();
        lbl_Att_OT = new javax.swing.JLabel();
        lbl_Att_LMin = new javax.swing.JLabel();
        lbl_ED2 = new javax.swing.JLabel();
        lbl_ED3 = new javax.swing.JLabel();
        lbl_main2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lbl_grosspay = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lbl_latededuction = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lbl_main3 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lbl_SSS = new javax.swing.JLabel();
        lbl_philhealth = new javax.swing.JLabel();
        lbl_pagibig = new javax.swing.JLabel();
        lbl_tax = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lbl_takehome = new javax.swing.JLabel();
        lbl_overtime = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        lbl_totalBen = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Employee ID:");

        jLabel2.setText("Name:");

        jLabel14.setText("Rice Subsidy:");

        jLabel15.setText("Phone Allowance:");

        jLabel16.setText("Clothing Allowance:");

        jLabel17.setText("Hourly Rate:");

        lbl_resID.setText("EMP_ID_RESULT");

        lbl_resName.setText("EMP_NAME_RESULT");

        lbl_resRiceSub.setText("EMP_RiceSub_RESULT");

        lbl_resPhoneAllowance.setText("EMP_PhoneAllowance_RESULT");

        lbl_resClothing.setText("EMP_Clothing_RESULT");

        lbl_resHourly.setText("EMP_HRate_RESULT");

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

        btn_Results_UpdateRec.setForeground(new java.awt.Color(51, 102, 0));
        btn_Results_UpdateRec.setText("Export as PDF");
        btn_Results_UpdateRec.setToolTipText("");
        btn_Results_UpdateRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Results_UpdateRecActionPerformed(evt);
            }
        });

        jLabel20.setText("Cut-off Period:");

        lbl_cutoffperiod.setText("lbl_Cutoff");

        lbl_main1.setBackground(new java.awt.Color(204, 255, 255));
        lbl_main1.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lbl_main1.setText("Payroll");

        lbl_ED4.setText("Overtime Hours");

        lbl_ED5.setText("Late Minutes:");

        lbl_Att_TWHours.setText("Att_Res_TWHours");

        lbl_Att_RegHours.setText("Att_Res_RegHours");

        lbl_Att_OT.setText("Att_Res_OT");

        lbl_Att_LMin.setText("Att_Res_LMin");

        lbl_ED2.setText("Total Work Hours:");

        lbl_ED3.setText("Regular Hours");

        lbl_main2.setBackground(new java.awt.Color(204, 255, 255));
        lbl_main2.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        lbl_main2.setText("Payroll Details:");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Gross Income:");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_grosspay.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lbl_grosspay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_grosspay.setText("lbl_GrossInc");
        lbl_grosspay.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("Late Deduction:");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_latededuction.setForeground(new java.awt.Color(255, 0, 51));
        lbl_latededuction.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_latededuction.setText("lbl_Deduction");
        lbl_latededuction.setToolTipText("");
        lbl_latededuction.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Gov't Deductions:");
        jLabel24.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_main3.setBackground(new java.awt.Color(204, 255, 255));
        lbl_main3.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        lbl_main3.setText("Payroll Summary");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("SSS:");
        jLabel26.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("PhilHealth:");
        jLabel27.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Pag-Ibig:");
        jLabel28.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("WithHolding Tax:");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_SSS.setForeground(new java.awt.Color(255, 0, 51));
        lbl_SSS.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_SSS.setText("lbl_Deduction");
        lbl_SSS.setToolTipText("");
        lbl_SSS.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_philhealth.setForeground(new java.awt.Color(255, 0, 51));
        lbl_philhealth.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_philhealth.setText("lbl_Deduction");
        lbl_philhealth.setToolTipText("");
        lbl_philhealth.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_pagibig.setForeground(new java.awt.Color(255, 0, 51));
        lbl_pagibig.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_pagibig.setText("lbl_Deduction");
        lbl_pagibig.setToolTipText("");
        lbl_pagibig.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_tax.setForeground(new java.awt.Color(255, 0, 51));
        lbl_tax.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_tax.setText("lbl_Deduction");
        lbl_tax.setToolTipText("");
        lbl_tax.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Overtime Pay:");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel34.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("NET INCOME (Take Home Pay):");
        jLabel34.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_takehome.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbl_takehome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_takehome.setText("lbl_GrossInc");
        lbl_takehome.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_overtime.setForeground(new java.awt.Color(0, 102, 51));
        lbl_overtime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_overtime.setText("lbl_GrossInc");
        lbl_overtime.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Total Benefits:");
        jLabel35.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbl_totalBen.setForeground(new java.awt.Color(0, 102, 51));
        lbl_totalBen.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_totalBen.setText("lbl_GrossInc");
        lbl_totalBen.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(278, 278, 278)
                        .addComponent(lbl_main1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jLabel20)
                        .addGap(6, 6, 6)
                        .addComponent(lbl_cutoffperiod))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbl_main2)
                        .addGap(232, 232, 232)
                        .addComponent(lbl_main3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbl_ED2)
                        .addGap(55, 55, 55)
                        .addComponent(lbl_Att_TWHours, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(btn_Results_Home)
                        .addGap(31, 31, 31)
                        .addComponent(btn_Results_UpdateRec)
                        .addGap(43, 43, 43)
                        .addComponent(btn_Results_Home1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl_ED3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(lbl_Att_RegHours, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl_ED4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(lbl_Att_OT, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl_ED5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(lbl_Att_LMin, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_SSS, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_philhealth, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_pagibig, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_tax, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_overtime, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(lbl_resName, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel22)
                        .addGap(125, 125, 125)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_grosspay, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_latededuction, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67)
                                .addComponent(lbl_resID, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(jLabel19))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(49, 49, 49)
                                        .addComponent(lbl_resPhoneAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(lbl_resClothing, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67)
                                        .addComponent(lbl_resRiceSub, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(85, 85, 85)
                                        .addComponent(lbl_resHourly, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(57, 57, 57)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34))))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_totalBen, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_takehome))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_main1)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(lbl_cutoffperiod))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_main2)
                    .addComponent(lbl_main3))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lbl_resID)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(lbl_grosspay)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(lbl_resName)
                    .addComponent(jLabel22)
                    .addComponent(lbl_latededuction))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ED2)
                    .addComponent(lbl_Att_TWHours)
                    .addComponent(jLabel24))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ED3)
                    .addComponent(lbl_Att_RegHours)
                    .addComponent(jLabel26)
                    .addComponent(lbl_SSS))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ED4)
                    .addComponent(lbl_Att_OT)
                    .addComponent(jLabel27)
                    .addComponent(lbl_philhealth))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ED5)
                    .addComponent(lbl_Att_LMin)
                    .addComponent(jLabel28)
                    .addComponent(lbl_pagibig))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(lbl_tax))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(lbl_overtime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(lbl_totalBen)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(lbl_resRiceSub))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(lbl_resPhoneAllowance))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(lbl_resClothing))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(lbl_resHourly))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_takehome)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Results_Home)
                    .addComponent(btn_Results_UpdateRec)
                    .addComponent(btn_Results_Home1))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_Results_HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Results_HomeActionPerformed
        this.dispose();
        MainDialog mainDialog = new MainDialog(new javax.swing.JFrame(), true);
        mainDialog.setVisible(true);
    }//GEN-LAST:event_btn_Results_HomeActionPerformed

    private void btn_Results_Home1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Results_Home1ActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Results_Home1ActionPerformed

    private void btn_Results_UpdateRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Results_UpdateRecActionPerformed
        this.dispose();
        exportPayrollToPDF();
    }//GEN-LAST:event_btn_Results_UpdateRecActionPerformed

    
private void exportPayrollToPDF() {
    Document document = new Document();

    try {
        // Build default filename: cutoff_empID_lastname.pdf
        String fullName = lbl_resName.getText();  // e.g., "Dela Cruz, Juan"
        String lastName = fullName.split(",")[0].trim();  // Get last name
        String safeCutoff = cutoffPeriod.replaceAll("[^a-zA-Z0-9]", "_"); // e.g., "July_15_2025"
        String defaultFileName = safeCutoff + "_" + empID + "_" + lastName + ".pdf";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(defaultFileName));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Employee Payroll Result", titleFont));
            document.add(new Paragraph(" "));  // Spacer

            document.add(new Paragraph("Employee ID: " + empID, labelFont));
            document.add(new Paragraph("Employee Name: " + lbl_resName.getText(), valueFont));
            document.add(new Paragraph("Cut-off Period: " + cutoffPeriod, valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Work Hours", labelFont));
            document.add(new Paragraph("Total Work Hours: " + lbl_Att_TWHours.getText(), valueFont));
            document.add(new Paragraph("Regular Hours: " + lbl_Att_RegHours.getText(), valueFont));
            document.add(new Paragraph("Overtime Hours: " + lbl_Att_OT.getText(), valueFont));
            document.add(new Paragraph("Late Minutes: " + lbl_Att_LMin.getText(), valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Allowances", labelFont));
            document.add(new Paragraph("Rice Subsidy: " + lbl_resRiceSub.getText(), valueFont));
            document.add(new Paragraph("Phone Allowance: " + lbl_resPhoneAllowance.getText(), valueFont));
            document.add(new Paragraph("Clothing Allowance: " + lbl_resClothing.getText(), valueFont));
            document.add(new Paragraph("Hourly Rate: " + lbl_resHourly.getText(), valueFont));
            document.add(new Paragraph("Total Benefits: " + lbl_totalBen.getText(), valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Earnings and Deductions", labelFont));
            document.add(new Paragraph("Gross Pay: " + lbl_grosspay.getText(), valueFont));
            document.add(new Paragraph("Overtime Pay: " + lbl_overtime.getText(), valueFont));
            document.add(new Paragraph("Late Deduction: " + lbl_latededuction.getText(), valueFont));
            document.add(new Paragraph("SSS: " + lbl_SSS.getText(), valueFont));
            document.add(new Paragraph("Pag-IBIG: " + lbl_pagibig.getText(), valueFont));
            document.add(new Paragraph("PhilHealth: " + lbl_philhealth.getText(), valueFont));
            document.add(new Paragraph("Tax: " + lbl_tax.getText(), valueFont));
            document.add(new Paragraph("Take Home Pay: " + lbl_takehome.getText(), valueFont));

            document.close();
            JOptionPane.showMessageDialog(this, "Payroll exported to PDF successfully!");
        }

    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error exporting to PDF: " + e.getMessage());
    }
}
    
    
    
    
    private String formatCurrency(String value) {
    try {
        double amount = Double.parseDouble(value);
        return String.format("₱%,.2f", amount);
    } catch (NumberFormatException e) {
        return "₱0.00";
    }
}

    private String formatCurrency(double value) {
        return String.format("₱%,.2f", value);
}

    private double parseAmount(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
}
    

    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Employee_PayrollResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Employee_PayrollResult dialog = new Employee_PayrollResult(new JFrame(), true, "10001", "July 15 2024");
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    

    }

    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Results_Home;
    private javax.swing.JButton btn_Results_Home1;
    private javax.swing.JButton btn_Results_UpdateRec;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel lbl_Att_LMin;
    private javax.swing.JLabel lbl_Att_OT;
    private javax.swing.JLabel lbl_Att_RegHours;
    private javax.swing.JLabel lbl_Att_TWHours;
    private javax.swing.JLabel lbl_ED2;
    private javax.swing.JLabel lbl_ED3;
    private javax.swing.JLabel lbl_ED4;
    private javax.swing.JLabel lbl_ED5;
    private javax.swing.JLabel lbl_SSS;
    private javax.swing.JLabel lbl_cutoffperiod;
    private javax.swing.JLabel lbl_grosspay;
    private javax.swing.JLabel lbl_latededuction;
    private javax.swing.JLabel lbl_main1;
    private javax.swing.JLabel lbl_main2;
    private javax.swing.JLabel lbl_main3;
    private javax.swing.JLabel lbl_overtime;
    private javax.swing.JLabel lbl_pagibig;
    private javax.swing.JLabel lbl_philhealth;
    private javax.swing.JLabel lbl_resClothing;
    private javax.swing.JLabel lbl_resHourly;
    private javax.swing.JLabel lbl_resID;
    private javax.swing.JLabel lbl_resName;
    private javax.swing.JLabel lbl_resPhoneAllowance;
    private javax.swing.JLabel lbl_resRiceSub;
    private javax.swing.JLabel lbl_takehome;
    private javax.swing.JLabel lbl_tax;
    private javax.swing.JLabel lbl_totalBen;
    // End of variables declaration//GEN-END:variables
}

