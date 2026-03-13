/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import model.EmployeeStatus;

/**
 *
 * @author Amir
 */
public class EmployeeDAO {
    private static final String CSV_FILE_PATH = "employees.csv";
    private static final Logger logger = Logger.getLogger(EmployeeDAO.class.getName());

    public List<Employee> load() {
        List<Employee> employeeList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

           
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length >= 19) {
                    try {
                        for (int i = 0; i < data.length; i++) {
                            data[i] = data[i].replace("\"", "").trim();
                        }

                       
                        if (data[0].contains(".")) {
                            data[0] = data[0].split("\\.")[0];
                        }

                        Employee emp = EmployeeStatus.createFromCsv(data);
                        if (emp != null) {
                            employeeList.add(emp);
                        }
                    } catch (Exception e) {
                        logger.warning("Skipping row: " + data[0] + " due to: " + e.getMessage());
                    }
                }
            }
            logger.info("Successfully loaded " + employeeList.size() + " employees.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File error: " + e.getMessage());
        }
        return employeeList;
    }
   
       public void save(List<Employee> employeeList) {
   
       if (employeeList == null) {
           logger.warning("Attempted to save a null employee list. Operation cancelled.");
           return; 
       }

          try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH))) {
        
            writer.println("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,PhilHealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");

             for (Employee emp : employeeList) {
               StringBuilder sb = new StringBuilder();
            
          
               sb.append(emp.getEmployeeId()).append(",");
               sb.append(emp.getLastName()).append(",");
               sb.append(emp.getFirstName()).append(",");
               sb.append(emp.getBirthday()).append(",");
            
         
               sb.append("\"").append(emp.getAddress()).append("\",");
            
          
               sb.append(emp.getPhoneNumber()).append(",");
               sb.append(emp.getSssNumber()).append(",");
               sb.append(emp.getPhilHealth()).append(",");
               sb.append(emp.getTinNumber()).append(",");
               sb.append(emp.getPagIbig()).append(",");
               sb.append(emp.getStatus()).append(",");
               sb.append(emp.getPosition()).append(",");
            
            
              sb.append("\"").append(emp.getSupervisor()).append("\",");
            
           
              sb.append(emp.getBasicSalary()).append(",");
              sb.append(emp.getRiceSubsidy()).append(",");
              sb.append(emp.getPhoneAllowance()).append(",");
              sb.append(emp.getClothingAllowance()).append(",");
              sb.append(emp.getGrossSemiMonthlyRate()).append(",");
              sb.append(emp.getHourlyRate());

            writer.println(sb.toString());
        }
        logger.info("Successfully saved " + employeeList.size() + " employees to CSV.");
    } catch (IOException e) {
        logger.log(Level.SEVERE, "File error while saving: " + e.getMessage());
    }
}
    
    
    
}

