/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.io.File;
import java.util.List;
import model.Employee;
import java.util.stream.Collectors;
/**
 *
 * @author Amir
 */
public class ITService {
    private static final Logger logger = Logger.getLogger(ITService.class.getName());
    private static ITService instance;

    private ITService() {}

    public static ITService getInstance() {
        if (instance == null) {
            instance = new ITService();
        }
        return instance;
    }

    //
    public boolean resetUserPassword(String empId) {
        
        logger.info("IT Security: Password reset triggered for ID: " + empId);
        return true; 
    }
    
    public List<Employee> searchEmployees(String query) {
    List<Employee> all = EmployeeService.getInstance().getAllEmployees();
    if (query == null || query.isEmpty()) return all;

    String lowerQuery = query.toLowerCase();
    return all.stream()
        .filter(emp -> emp.getEmployeeId().contains(query) || 
                       (emp.getFirstName() + " " + emp.getLastName()).toLowerCase().contains(lowerQuery))
        .collect(Collectors.toList());
}
    
    public String getTimestampedMessage(String message) {
        // The Service handles the time formatting
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "[" + time + "] " + message;
    }
 
    public String runSystemDiagnostics() {
        File empFile = new File("employees.csv");
        File leaveFile = new File("leaves.csv");
        File attendanceFile = new File("Attendance.csv");
        
        StringBuilder report = new StringBuilder();
        report.append(empFile.exists() ? "[PASS] Employee Database Connected\n" : "[FAIL] Employee Database Missing\n");
        report.append(leaveFile.exists() ? "[PASS] Leave Database Connected\n" : "[FAIL] Leave Database Missing\n");
        report.append(attendanceFile.exists() ? "[PASS] Attendance Log Found\n" : "[FAIL] Attendance Log Missing\n");
        if (empFile.exists() && attendanceFile.exists()) {
        report.append("[INFO] All systems operational.");
    } else {
        report.append("[WARN] System degradation detected.");
    }
        return report.toString();
    }
}
