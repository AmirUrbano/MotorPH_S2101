/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AttendanceRecord;
import model.Employee;
import service.EmployeeService;

/**
 *
 * @author Amir
 */
public class AttendanceDAO {
    private static final Logger logger = Logger.getLogger(AttendanceDAO.class.getName());
    private static final String FILE_PATH = "Attendance.csv";

    
    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    private static final DateTimeFormatter INTERNAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<AttendanceRecord> load() {
        List<AttendanceRecord> records = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) return records;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                try {
                   
                    String[] data = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                    
                    if (data.length >= 6) {
                    
                        String employeeId = data[0].replace("\"", "").trim();
                        if (employeeId.contains(".")) {
                            employeeId = employeeId.split("\\.")[0];
                        }

                        
                        LocalDate date = LocalDate.parse(data[3].trim(), CSV_DATE_FORMAT);
                        String cleanDate = date.format(INTERNAL_DATE_FORMAT);

                     
                        String timeIn = data[4].trim();
                        String timeOut = data[5].trim();

                       
                        records.add(new AttendanceRecord(employeeId, cleanDate, timeIn, timeOut));
                    }
                } catch (Exception e) {
                    logger.warning("Skipping messy row: " + line + " Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Critical error reading attendance file", e);
        }
        return records;
    }
    
    public void save(List<AttendanceRecord> records) {
        EmployeeService service = EmployeeService.getInstance();
            
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
        writer.println("Employee #,Last Name,First Name,Date,Log In,Log Out");

        for (AttendanceRecord record : records) {
            
            Employee emp = service.findEmployeeById(record.getEmployeeId());
            String lastName = (emp != null) ? emp.getLastName() : "Unknown";
            String firstName = (emp != null) ? emp.getFirstName() : "Unknown";

          
            LocalDate date = LocalDate.parse(record.getDate());
            String csvDate = date.format(CSV_DATE_FORMAT);

         
            writer.printf("%s,%s,%s,%s,%s,%s%n",
                    record.getEmployeeId(),
                    lastName,
                    firstName,
                    csvDate,
                    record.getLogInTime().format(TIME_FORMATTER),
                    record.getLogOutTime().format(TIME_FORMATTER));
        }
    } catch (IOException e) {
        logger.log(Level.SEVERE, "Failed to save Attendance CSV", e);
    }
  }
}
