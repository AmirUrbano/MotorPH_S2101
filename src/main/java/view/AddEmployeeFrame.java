/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import service.EmployeeService;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Amir
 */
public class AddEmployeeFrame extends JFrame {
    private final MainFrame parentFrame;
    private final EmployeeService employeeDetails;
    private EmployeeFormPanel formPanel; 
    private JButton saveBtn, cancelBtn;

    public AddEmployeeFrame(MainFrame parent, EmployeeService employeeDetails) {
        this.parentFrame = parent;
        this.employeeDetails = employeeDetails;
        
        initializeGUI();
        formPanel.setEmployeeId(employeeDetails.getNextEmployeeId());
    }

    private void initializeGUI() {
        setTitle("MotorPH - Add New Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 750);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parentFrame);

    
        formPanel = new EmployeeFormPanel();
        add(formPanel, BorderLayout.CENTER);

    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        
      
        saveBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setPreferredSize(new Dimension(100, 35));

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            String[] data = formPanel.validateAndGetFormData();
            
            if (employeeDetails.findEmployeeById(data[0]) != null) {
                JOptionPane.showMessageDialog(this, 
                    "Employee Number " + data[0] + " already exists!", 
                    "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean success = employeeDetails.createAndAddEmployee(data);
            
            if (success) {
                parentFrame.refreshTable();
                JOptionPane.showMessageDialog(this, "Employee successfully registered!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
         
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage());
        }
    }
}