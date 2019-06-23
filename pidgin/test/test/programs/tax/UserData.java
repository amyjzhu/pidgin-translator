package test.programs.tax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserData {
    
    public static Map<String, Integer> salaryMap = null;
    private Integer salary;
    private String username;

    private UserData(String username, Integer salary) {
        this.salary = salary;
        this.username = username;
    }

    public static UserData loadRecord(String username) {
        if (salaryMap == null) {
            try {
                loadAll();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new UserData(username, salaryMap.get(username));
    }
    
    public void setSalary(Integer salary) {
        salaryMap.put(username, salary);
        this.salary = salary;
        try {
            updateFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Append the user name and salary of the current user to the "database"
     * 
     * @throws IOException
     */
    private void updateFile() throws IOException {
        writeToDataFile("taxes/data.txt", username + ",");
        
        Integer secretSalary = encrypt(getSalary());
        writeToDataFile("taxes/data.txt", secretSalary + "\n");
    }

    private Integer encrypt(Integer salary2) {
        // Notional
        return salary2*2;
    }

    public Integer getSalary() {
        return salary;
    }
    
    public boolean  hasSalary() {
        return salary != null;
    }
    
    
    private static void loadAll() throws IOException{
        salaryMap = new LinkedHashMap<String, Integer>();
        FileReader fr;
        try {
            fr = new FileReader("taxes/data.txt");
        } catch(FileNotFoundException e) {
            return;
        }
        
        BufferedReader reader = new BufferedReader(fr);
        while (reader.ready()) {
            String s = reader.readLine();
            String[] userSal = s.split(",");
            String username = userSal[0];
            Integer salary = Integer.parseInt(userSal[1]);
            salaryMap.put(username, salary);
        }
        reader.close();
    }
    
    private static void writeToDataFile(String filename, String toWrite) throws IOException {
        Writer w = new BufferedWriter(new FileWriter(filename, true));
        w.write(toWrite);
        w.close();
    }
}
