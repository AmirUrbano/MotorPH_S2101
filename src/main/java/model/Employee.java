/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mycompany.motorphpayroll_OOP.ValidationUtils;

/**
 *
 * @author Amir
 */
public abstract class Employee implements PayrollCalculations, EmployeePermissions {
    // Basic Info
    protected String employeeId;
   protected String lastName, firstName, birthday, address, phoneNumber;
    
    // Government number and Employment info
    protected String sssNumber, philHealth, tinNumber, pagIbig, status, position, supervisor;
    // Financials
   protected double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance;
    protected double grossSemiMonthlyRate, hourlyRate;

    public Employee(String employeeId, String lastName, String firstName, String birthday,
                    String address, String phoneNumber, String sssNumber, String philHealth,
                    String tinNumber, String pagIbig, String status, String position,
                    String supervisor, double basicSalary, double riceSubsidy,
                    double phoneAllowance, double clothingAllowance,
                    double grossSemiMonthlyRate, double hourlyRate) {
        
        // Validation
        validate(employeeId, lastName, firstName, basicSalary, hourlyRate);
        
        //  Sanitize and Assign Strings
        this.employeeId = sanitize(employeeId);
        this.lastName = sanitize(lastName);
        this.firstName = sanitize(firstName);
        this.birthday = sanitize(birthday);
        this.address = sanitize(address);
        this.phoneNumber = sanitize(phoneNumber);
        this.sssNumber = sanitize(sssNumber);
        this.philHealth = sanitize(philHealth);
        this.tinNumber = sanitize(tinNumber);
        this.pagIbig = sanitize(pagIbig);
        this.status = sanitize(status);
        this.position = sanitize(position);
        this.supervisor = sanitize(supervisor);
        
        // Assign Numerics
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }
    private void validate(String id, String last, String first, double salary, double rate) {
        if (!ValidationUtils.isValidEmployeeId(id)) {
            throw new IllegalArgumentException("Invalid employee ID format: " + id);
        }
        if (last == null || last.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (salary < 0 || rate < 0) {
            throw new IllegalArgumentException("Salary values cannot be negative");
        }
    }
    
    /**
     * 
     * @param benefitType
     * @return 
     */
    public double calculateBenefit(String benefitType) {
        return switch (benefitType.toLowerCase()) {
            case "rice" -> getRiceSubsidy();
            case "phone" -> getPhoneAllowance();
            case "clothing" -> getClothingAllowance();
            default -> 0.0;
        };
    }

    /**
     * 
     * @return 
     */
    public double calculateBenefit() {
        return getRiceSubsidy() + getPhoneAllowance() + getClothingAllowance();
    }
    
    @Override
       public double calculateSSS() {
        double salary = getBasicSalary();
        if (salary <= 3250) return 135.00;
        if (salary <= 3750) return 157.50;
        if (salary <= 4250) return 180.00;
        if (salary <= 4750) return 202.50;
        if (salary <= 5250) return 225.00;
        if (salary <= 5750) return 247.50;
        if (salary <= 6250) return 270.00;
        if (salary <= 6750) return 292.50;
        if (salary <= 7250) return 315.00;
        if (salary <= 7750) return 337.50;
        if (salary <= 8250) return 360.00;
        if (salary <= 8750) return 382.50;
        if (salary <= 9250) return 405.00;
        if (salary <= 9750) return 427.50;
        if (salary <= 10250) return 450.00;
        if (salary <= 10750) return 472.50;
        if (salary <= 11250) return 495.00;
        if (salary <= 11750) return 517.50;
        if (salary <= 12250) return 540.00;
        if (salary <= 12750) return 562.50;
        if (salary <= 13250) return 585.00;
        if (salary <= 13750) return 607.50;
        if (salary <= 14250) return 630.00;
        if (salary <= 14750) return 652.50;
        if (salary <= 15250) return 675.00;
        if (salary <= 15750) return 697.50;
        if (salary <= 16250) return 720.00;
        if (salary <= 16750) return 742.50;
        if (salary <= 17250) return 765.00;
        if (salary <= 17750) return 787.50;
        if (salary <= 18250) return 810.00;
        if (salary <= 18750) return 832.50;
        if (salary <= 19250) return 855.00;
        if (salary <= 19750) return 877.50;
        if (salary <= 20250) return 900.00;
        if (salary <= 20750) return 922.50;
        if (salary <= 21250) return 945.00;
        if (salary <= 21750) return 967.50;
        if (salary <= 22250) return 990.00;
        if (salary <= 22750) return 1012.50;
        if (salary <= 23250) return 1035.00;
        if (salary <= 23750) return 1057.50;
        if (salary <= 24250) return 1080.00;
        if (salary <= 24750) return 1102.50;
        return 1125.00;
    }
    
    @Override
    public double calculateGrossSalary(double hoursWorked) {
      
        double standardHours = 40.0; 
        double regularHours = Math.min(hoursWorked, standardHours);
        double overtimeHours = Math.max(hoursWorked - standardHours, 0);
    
         // In PayrollProcessor: (regularHours * rate) + (overtimeHours * rate * multiplier)
            return (regularHours * getHourlyRate()) + calculateAdjustment(overtimeHours);
    }

    @Override
    public double calculateAdjustment(double overtimeHours) {
    return overtimeHours * getHourlyRate() * 1.25;
    }

    @Override
    public double calculateAdjustment(int totalMinutesLate) {
    return totalMinutesLate * (getHourlyRate() / 60.0);
    }

    @Override
    public double calculatePhilHealth() {
        double premium = getBasicSalary() * 0.03;
     if (premium > 1800) premium = 1800; 
         return premium / 2; // Employee share
    }

    @Override
    public double calculatePagIbig() {
        double salary = getBasicSalary();
        double rate = (salary <= 1500) ? 0.01 : 0.02; // 1% for low earners, 2% otherwise
        double contribution = salary * rate;
        return Math.min(contribution, 100.00); // Cannot exceed 100
    }

    @Override
    public double calculateWithholdingTax(double grossSalary) {
        
    
    double sss = calculateSSS();
    double philHealth = calculatePhilHealth();
    double pagIbig = calculatePagIbig();
    
    // 
    double taxableIncome = grossSalary - (sss + philHealth + pagIbig);
    
    // 
    if (taxableIncome <= 20833) return 0;
    else if (taxableIncome <= 33332) return (taxableIncome - 20833) * 0.2;
    else if (taxableIncome <= 66666) return 2500 + (taxableIncome - 33333) * 0.25;
    else if (taxableIncome <= 166666) return 10833 + (taxableIncome - 66667) * 0.3;
    else if (taxableIncome <= 666666) return 40833.33 + (taxableIncome - 166667) * 0.32;
    else return 200833.33 + (taxableIncome - 666667) * 0.35;
}
    @Override public boolean canViewDatabase() { return false; }
    @Override public boolean canViewAllRecords() { return false; }
    @Override public boolean canAddEmployee() { return false; }
    @Override public boolean canDeleteEmployee() { return false; }
    @Override public boolean canEditBasicInfo() { return false; }
    @Override public boolean canEditFinancials() { return false; }
    @Override public boolean canFileLeave() { return false; }
    @Override public boolean canApproveLeave() { return false; }
    @Override public boolean canAccessSystemTools() {return false;}
    @Override public boolean canComputePayroll() {return false;}
    
    // Getters
    public double getHourlyRate() { return hourlyRate; }
    public String getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return birthday; }
    public String getPosition() { return position; }
    public String getStatus() { return status; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }

    // Additional getters for GUI components
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilHealth() { return philHealth; }
    public String getTinNumber() { return tinNumber; }
    public String getPagIbig() { return pagIbig; }
    public String getSupervisor() { return supervisor; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    
    // Setter methods for updating employee data
    public void setLastName(String lastName) { this.lastName = sanitize(lastName); }
    public void setFirstName(String firstName) { this.firstName = sanitize(firstName); }
    public void setBirthday(String birthday) { this.birthday = sanitize(birthday); }
    public void setAddress(String address) { this.address = sanitize(address); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = sanitize(phoneNumber); }
    public void setSssNumber(String sssNumber) { this.sssNumber = sanitize(sssNumber); }
    public void setPhilHealth(String philHealth) { this.philHealth = sanitize(philHealth); }
    public void setTinNumber(String tinNumber) { this.tinNumber = sanitize(tinNumber); }
    public void setPagIbig(String pagIbig) { this.pagIbig = sanitize(pagIbig); }
    public void setStatus(String status) { this.status = sanitize(status); }
    public void setPosition(String position) { this.position = sanitize(position); }
    public void setSupervisor(String supervisor) { this.supervisor = sanitize(supervisor); }
    
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public void calculateContributions() {
        // walang laman hehe
    }
    
    private String sanitize(String input) {
        if (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("null")) {
            return "N/A";
        }
        return input.trim();
    }
    
    public void displayInfo() {
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Position: " + position);
        System.out.println("Status: " + status);
        System.out.printf("Hourly Rate: PHP %.2f%n", hourlyRate);
    }

    @Override
    public String toString() {
        return String.format("%s - %s, %s (%s)", employeeId, lastName, firstName, position);
    }
}