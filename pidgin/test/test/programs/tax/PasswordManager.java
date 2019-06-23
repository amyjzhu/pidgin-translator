package test.programs.tax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {

    //Scanner scanner = new Scanner((java.io.InputStream) System.in);
    List<String> names;
    Map<String, String> passwords;

    public PasswordManager() {
        try {
            names = getNames();
            passwords = getPasswords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new File("taxes").mkdir();
    }

    public String addNewUser() throws IOException {
        System.out.println("Enter new username: ");
        Scanner scanner = new Scanner(System.in);
        
        // TODO check validity
        String username = scanner.next();
        while (checkUser(username)) {
            System.out.println("username already exists, try again: ");
            username = scanner.next();
        }

        writeToFile("taxes/users.txt", username + "\n");
        System.out.println("Enter new password: ");
        String password = scanner.next();
        // TODO check validity
        writeToFile("taxes/passwords.txt", username + ",");
        
        byte[] hash = computeHash(password);
        writeToFile("taxes/passwords.txt", hash  +"\n");
        // TODO Used to have to do this to get the policy to work hopefully not any more
//        writeToFile("taxes/passwords.txt", new String(computeHash(password)) + "\n");

        return username;
    }
    
    /**
     * Helper function to write data to a file
     * 
     * @param filename
     * @param toWrite
     */
    private static void writeToFile(String filename, String toWrite) {
        try {
            Writer w = new BufferedWriter(new FileWriter(filename, true));
            w.write(toWrite);
            w.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String userLogin() {
        System.out.println("Enter username: ");
        Scanner scanner = new Scanner(System.in);
        
        String username = scanner.next();

        while (!checkUser(username)) {
            System.out.println("Invalid username, try again: ");
            username = scanner.next();
        }

        System.out.println("Enter password: ");
        String password = scanner.next();
        boolean validPass = checkPass(username, password);
        int count = 1;
        while (!validPass && count < 10) {
            System.out.println("Invalid password try again (" + (10 - count) + " tries remaining) :" );
            password = scanner.next();
            validPass = checkPass(username, password);
            count++;
        }
        
        if (!validPass) {
            return null;
        }
        return username;
    }

    public boolean checkPass(String username, String password) {
        String real = passwords.get(username);
        String guess = new String(computeHash(password));
        return real.equals(guess);
    }

    private byte[] computeHash(String s) {
        byte[] bytes =  s.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] + i);
        }
        return bytes;
//        MessageDigest md = null;
//        try {
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//
//        return md.digest(s.getBytes());
    }

    private Map<String, String> getPasswords() throws IOException {
        Map<String, String> passes = new LinkedHashMap<String, String>();
        
        FileReader fr;
        try {
            fr = new FileReader("taxes/passwords.txt");
        } catch(FileNotFoundException e) {
            return passes;
        }
        
        BufferedReader reader = new BufferedReader(fr);
        while (reader.ready()) {
            String s = reader.readLine();
            String[] userPass = s.split(",");
            String username = userPass[0];
            String password = userPass[1];
            passes.put(username, password);
        }
        reader.close();
        return passes;
    }

    public boolean checkUser(String username) {
        return names.contains(username);
    }

    private List<String> getNames() throws IOException {
        names = new LinkedList<String>();
        FileReader fr;
        try {
            fr = new FileReader("taxes/users.txt");
        } catch(FileNotFoundException e) {
            return names;
        }
        
        BufferedReader reader = new BufferedReader(fr);
        while (reader.ready()) {
            names.add(reader.readLine());
        }
        reader.close();
        return names;
    }
}
