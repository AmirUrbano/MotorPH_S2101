/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Amir
 */
public class UserDAO {
    private static final String CREDENTIALS_FILE = "user_credentials.csv";
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public Map<String, String> loadCredentials() {
        Map<String, String> credentials = new HashMap<>();
        File file = new File(CREDENTIALS_FILE);
        
        if (!file.exists()) {
            LOGGER.log(Level.SEVERE, "Credentials file not found: {0}", CREDENTIALS_FILE);
            return credentials;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
               
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    credentials.put(parts[0].trim(), parts[1].trim());
                }
            }
            LOGGER.log(Level.INFO, "Successfully loaded {0} user credentials.", credentials.size());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading credentials file", e);
        }
        return credentials;
    }
    
    public void createAccount(String username, String password) {
    
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true)))) {
        out.println(username + "," + password);
        LOGGER.log(Level.INFO, "New account created for user: {0}", username);
    } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error writing to credentials file", e);
    }
}
    
}

