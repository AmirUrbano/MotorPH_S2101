/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import model.PayrollResult;

/**
 *
 * @author Amir
 */

    public class PayslipFrame extends JFrame {

    public PayslipFrame(PayrollResult result) {
        setTitle("MotorPH - Official Payslip [" + result.employeeId + "]");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // main
        JPanel paper = new JPanel();
        paper.setLayout(new BoxLayout(paper, BoxLayout.Y_AXIS));
        paper.setBackground(Color.WHITE);
        paper.setBorder(new EmptyBorder(25, 25, 25, 25));

        // header
        JLabel companyName = new JLabel("MOTORPH");
        companyName.setFont(new Font("SansSerif", Font.BOLD, 20));
        companyName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel address = new JLabel("123-B, Hindi matagpuang street, Cubao, Quezon City");
        address.setFont(new Font("SansSerif", Font.PLAIN, 10));
        address.setAlignmentX(Component.CENTER_ALIGNMENT);

        // employee info
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Employee Details", 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.ITALIC, 11)));

        addInfoRow(infoPanel, "Employee ID:", result.employeeId);
        addInfoRow(infoPanel, "Name:", result.lastName + ", " + result.firstName);
        addInfoRow(infoPanel, "Position:", result.position);
        addInfoRow(infoPanel, "Status:", result.status);

        // Earnings and deducts
        JPanel tablePanel = new JPanel(new GridLayout(0, 2, 15, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Earnings Section
        addTableLabel(tablePanel, "<b>EARNINGS</b>", "");
        addTableLabel(tablePanel, "Monthly Basic Salary", String.format("PHP %,.2f", result.basicSalary));
        addTableLabel(tablePanel, "Total Allowances", String.format("PHP %,.2f", result.totalAllowances));
        addTableLabel(tablePanel, "<b>Gross Salary</b>", String.format("<b>PHP %,.2f</b>", result.totalMonthlyGrossSalary));

        // Spacing
        tablePanel.add(new JLabel(" ")); tablePanel.add(new JLabel(" "));

        // Deductions Section
        addTableLabel(tablePanel, "<b>DEDUCTIONS</b>", "");
        addTableLabel(tablePanel, "SSS Deduction", String.format("(%,.2f)", result.sssDeduction));
        addTableLabel(tablePanel, "PhilHealth", String.format("(%,.2f)", result.philHealthDeduction));
        addTableLabel(tablePanel, "Pag-IBIG", String.format("(%,.2f)", result.pagIbigDeduction));
        addTableLabel(tablePanel, "Withholding Tax", String.format("(%,.2f)", result.taxDeduction));
        addTableLabel(tablePanel, "Lateness (" + result.totalLateDays + " days)", String.format("(%,.2f)", result.totalLateDeductions));

        //net pay
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 245, 245)); // Light gray bar
        footer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel netLabel = new JLabel("TOTAL NET PAY:");
        netLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel netValue = new JLabel(String.format("PHP %,.2f", result.totalMonthlyNetSalary));
        netValue.setFont(new Font("SansSerif", Font.BOLD, 18));
        netValue.setForeground(new Color(0, 102, 0)); // Dark green for money

        footer.add(netLabel, BorderLayout.WEST);
        footer.add(netValue, BorderLayout.EAST);

        // Assembly
        paper.add(companyName);
        paper.add(address);
        paper.add(Box.createVerticalStrut(20));
        paper.add(infoPanel);
        paper.add(Box.createVerticalStrut(20));
        paper.add(tablePanel);
        paper.add(Box.createVerticalGlue());
        paper.add(footer);

        add(paper);
    }

    private void addInfoRow(JPanel p, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        p.add(l);
        p.add(new JLabel(value));
    }

    private void addTableLabel(JPanel p, String left, String right) {
        p.add(new JLabel("<html>" + left + "</html>"));
        JLabel r = new JLabel("<html>" + right + "</html>");
        r.setHorizontalAlignment(JLabel.RIGHT);
        p.add(r);
    }
 }

