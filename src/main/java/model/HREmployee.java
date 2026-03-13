/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public class HREmployee  extends Employee {

    public HREmployee(String employeeId, String lastName, String firstName, String birthday, String address, String phoneNumber, String sssNumber, String philHealth, String tinNumber, String pagIbig, String status, String position, String supervisor, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate) {
        super(employeeId, lastName, firstName, birthday, address, phoneNumber, sssNumber, philHealth, tinNumber, pagIbig, status, position, supervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate);
    }
    @Override public boolean canViewDatabase() { return true; }
    @Override public boolean canViewAllRecords() { return true; }
    @Override public boolean canAddEmployee() { return true; }
    @Override public boolean canDeleteEmployee() { return true; }
    @Override public boolean canEditBasicInfo() { return true; } 
    @Override public boolean canApproveLeave() { return true; }  
}
