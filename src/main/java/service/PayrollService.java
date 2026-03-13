/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import com.mycompany.motorphpayroll_OOP.Config;
import com.mycompany.motorphpayroll_OOP.ValidationUtils;
import model.Employee;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import model.PayrollResult;


public class PayrollService {
    private static PayrollService instance;
    private final AttendanceService attendanceService = AttendanceService.getInstance();
    private final EmployeeService employeeDetails = EmployeeService.getInstance();
    
   private PayrollService() {
   
   }
   
   public static PayrollService getInstance() {
        if (instance == null) {
            instance = new PayrollService();
        }
        return instance;
    }
   
    public String processMonthlyPayroll(String employeeId, int month) {
        // Validation
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID format: " + employeeId);
        }
        if (!ValidationUtils.isValidMonth(month)) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        Employee employee = employeeDetails.findEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

      
        StringBuilder report = new StringBuilder();
        report.append("========================================\n");
        report.append("      MOTORPH PAYROLL REPORT\n");
        report.append("========================================\n");
        report.append(String.format("Employee ID: %s\n", employeeId));
        report.append(String.format("Name:        %s %s\n", employee.getFirstName(), employee.getLastName()));
        report.append(String.format("Month:       %s 2024\n", getMonthName(month)));
        report.append("========================================\n");

        LocalDate startOfMonth = LocalDate.of(2024, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        
        double totalMonthlyGrossSalary = 0;
        double totalLateDeductions = 0;
        int totalLateDays = 0;
        int weekCount = 1;

        LocalDate currentWeekStart = startOfMonth.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());

        while (!currentWeekStart.isAfter(endOfMonth)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            LocalDate effectiveStart = currentWeekStart.isBefore(startOfMonth) ? startOfMonth : currentWeekStart;
            LocalDate effectiveEnd = currentWeekEnd.isAfter(endOfMonth) ? endOfMonth : currentWeekEnd;

            double totalHoursWorked = attendanceService.getHoursWorked(employeeId, effectiveStart, effectiveEnd);

            if (totalHoursWorked > 0) {
                int totalMinutesLate = attendanceService.getMinutesLate(employeeId, effectiveStart, effectiveEnd);
                int daysLate = attendanceService.getDaysLate(employeeId, effectiveStart, effectiveEnd);

                double overtimeHours = Math.max(totalHoursWorked - Config.STANDARD_WORK_HOURS, 0);
                double regularHours = Math.min(totalHoursWorked, Config.STANDARD_WORK_HOURS);
                
                
             
                double overtimePay = employee.calculateAdjustment(overtimeHours);
                double weeklyLateDeduction = employee.calculateAdjustment(totalMinutesLate);
                double weeklyGross = (regularHours * employee.getHourlyRate()) + overtimePay;
                // Add weekly details to report
                report.append(getWeeklyReportString(weekCount, effectiveStart, effectiveEnd, regularHours, 
                              overtimeHours, totalHoursWorked, daysLate, totalMinutesLate, 
                              weeklyGross, weeklyLateDeduction));

                totalMonthlyGrossSalary += weeklyGross;
                totalLateDeductions += weeklyLateDeduction;
                totalLateDays += daysLate;
                weekCount++;
            }
            currentWeekStart = currentWeekStart.plusDays(7);
        }

        // Deductions Calculations
        double totalSSS = employee.calculateSSS();
        double totalPhilHealth = employee.calculatePhilHealth();
        double totalPagIbig = employee.calculatePagIbig();
        double totalAllowances = employee.calculateBenefit();
        double totalWithholdingTax = employee.calculateWithholdingTax(totalMonthlyGrossSalary);

        double totalMonthlyNetSalary = (totalMonthlyGrossSalary - totalLateDeductions - 
                                       totalSSS - totalPhilHealth - totalPagIbig - totalWithholdingTax) + totalAllowances;

        // Final Summary
        report.append(getMonthlySummaryString(totalMonthlyGrossSalary, totalLateDeductions, totalSSS, 
                      totalPhilHealth, totalPagIbig, totalWithholdingTax,
                      totalAllowances, totalLateDays, totalMonthlyNetSalary));

        return report.toString();
    }
    
   
    public PayrollResult getPayrollResultForGUI(String employeeId, int month) {
    Employee employee = employeeDetails.findEmployeeById(employeeId);
    if (employee == null) return null;

   
    LocalDate startOfMonth = LocalDate.of(2024, month, 1);
    LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
    
    double totalMonthlyGrossSalary = 0;
    double totalLateDeductions = 0;
    int totalLateDays = 0;

    LocalDate currentWeekStart = startOfMonth.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());

    while (!currentWeekStart.isAfter(endOfMonth)) {
        LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
        LocalDate effectiveStart = currentWeekStart.isBefore(startOfMonth) ? startOfMonth : currentWeekStart;
        LocalDate effectiveEnd = currentWeekEnd.isAfter(endOfMonth) ? endOfMonth : currentWeekEnd;

        double totalHoursWorked = attendanceService.getHoursWorked(employeeId, effectiveStart, effectiveEnd);

        if (totalHoursWorked > 0) {
            int totalMinutesLate = attendanceService.getMinutesLate(employeeId, effectiveStart, effectiveEnd);
            int daysLate = attendanceService.getDaysLate(employeeId, effectiveStart, effectiveEnd);

            double overtimeHours = Math.max(totalHoursWorked - Config.STANDARD_WORK_HOURS, 0);
            double regularHours = Math.min(totalHoursWorked, Config.STANDARD_WORK_HOURS);
            
            double overtimePay = employee.calculateAdjustment(overtimeHours);
            double weeklyLateDeduction = employee.calculateAdjustment(totalMinutesLate);
            double weeklyGross = (regularHours * employee.getHourlyRate()) + overtimePay;

            totalMonthlyGrossSalary += weeklyGross;
            totalLateDeductions += weeklyLateDeduction;
            totalLateDays += daysLate;
        }
        currentWeekStart = currentWeekStart.plusDays(7);
    }

    
    double totalSSS = employee.calculateSSS();
    double totalPhilHealth = employee.calculatePhilHealth();
    double totalPagIbig = employee.calculatePagIbig();
    double totalAllowances = employee.calculateBenefit();
    double totalWithholdingTax = employee.calculateWithholdingTax(totalMonthlyGrossSalary);

    double totalMonthlyNetSalary = (totalMonthlyGrossSalary - totalLateDeductions - 
                                   totalSSS - totalPhilHealth - totalPagIbig - totalWithholdingTax) + totalAllowances;

  
    return new PayrollResult(
        employee.getEmployeeId(),
        employee.getLastName(),
        employee.getFirstName(),
        employee.getSssNumber(),
        employee.getPhilHealth(),
        employee.getTinNumber(),
        employee.getPagIbig(),
        employee.getStatus(),
        employee.getPosition(),
        employee.getBasicSalary(),
        employee.getRiceSubsidy(),
        employee.getPhoneAllowance(),
        employee.getClothingAllowance(),
        employee.getGrossSemiMonthlyRate(),
        employee.getHourlyRate(),
        totalMonthlyGrossSalary,
        totalSSS,
        totalPhilHealth,
        totalPagIbig,
        totalWithholdingTax,
        totalLateDeductions,
        totalAllowances,
        totalMonthlyNetSalary,
        totalLateDays
    );
}

    private String getWeeklyReportString(int week, LocalDate start, LocalDate end, double reg, double ot, 
                                        double total, int lateD, int lateM, double gross, double ded) {
        return String.format("""
                             
                             WEEK %d: %s to %s
                             -> Reg Hours: %.2f | OT Hours: %.2f
                             -> Late: %d days (%d mins)
                             -> Weekly Gross: PHP %.2f
                             -> Late Deduction: PHP %.2f
                             """, 
                week, start, end, reg, ot, lateD, lateM, gross, ded);
    }

    private String getMonthlySummaryString(double gross, double lateDed, double sss, double ph, 
                                          double pi, double tax,
                                          double allowances, int lateDays, double net) {
        return String.format("""
                             
                             ========================================
                             TOTAL MONTHLY SUMMARY:
                             -> Total Gross Salary:    PHP %.2f
                             -> Total Late Deductions: PHP %.2f
                             -> SSS Deduction:         PHP %.2f
                             -> PhilHealth Deduction:  PHP %.2f
                             -> Pag-IBIG Deduction:    PHP %.2f
                             -> Withholding Tax:       PHP %.2f
                             -> Total Allowances:      PHP %.2f (Non-Taxable)
                             -> Total Days Late:       %d
                             ----------------------------------------
                             -> TOTAL NET SALARY:      PHP %.2f
                             ========================================
                             """,
                gross, lateDed, sss, ph, pi, tax, allowances, lateDays, net);
    }

    private String getMonthName(int month) {
        return LocalDate.of(2024, month, 1).getMonth().getDisplayName(
            java.time.format.TextStyle.FULL, Locale.ENGLISH);
    }
}


