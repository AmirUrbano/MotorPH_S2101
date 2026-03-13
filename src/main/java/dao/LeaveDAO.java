/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.LeaveRequest;
import java.io.*;
import java.util.*;
import java.util.logging.*;
/**
 *
 * @author Amir
 */
public class LeaveDAO {
    private static final String FILE_PATH = "leaves.csv";
    private static final Logger logger = Logger.getLogger(LeaveDAO.class.getName());
    
    public List<LeaveRequest> load () {
        
        List<LeaveRequest> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            logger.warning("leave csv not found returning empty list");
            return list;   
        }
          try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length == 7) {
                    LeaveRequest request = new LeaveRequest(
                        data[0], data[1], data[2], data[3], data[4], data[5], data[6]
                    );
                    list.add(request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void save(List<LeaveRequest> list) {
        if (list == null){
            logger.warning("Attempted to save a null employee list operation cancelled.");
            return;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
          
            writer.println("RequestID,EmployeeID,FullName,LeaveType,StartDate,EndDate,Status");
            
            for (LeaveRequest lr : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(lr.getRequestId()).append(",");
                sb.append(lr.getEmployeeId()).append(",");
                sb.append("\"").append(lr.getEmployeeName()).append("\","); 
                sb.append(lr.getLeaveType()).append(",");
                sb.append(lr.getStartDate()).append(",");
                sb.append(lr.getEndDate()).append(",");
                sb.append(lr.getStatus());
                
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving leaves file", e);
        }
    }
}
