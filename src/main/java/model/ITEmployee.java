/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public class ITEmployee extends Employee {
    
    public ITEmployee(String employeeId, String lastName, String firstName, String birthday, String address, String phoneNumber, String sssNumber, String philHealth, String tinNumber, String pagIbig, String status, String position, String supervisor, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate) {
        super(employeeId, lastName, firstName, birthday, address, phoneNumber, sssNumber, philHealth, tinNumber, pagIbig, status, position, supervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate);
    }
    @Override public boolean canAccessSystemTools() {return true;}
}
