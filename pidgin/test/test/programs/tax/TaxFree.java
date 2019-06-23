package test.programs.tax;

import java.io.IOException;
import java.util.Scanner;

public class TaxFree {

    private static final double TAX_RATE = 0.1;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void start() {
        System.out.println("Are you an existing user? (Y/N): ");
        String existing = scanner.next();
        while (!existing.equals("Y") && !existing.equals("N")) {
            System.out.println("Are you an existing user? (Y/N): ");
            existing = scanner.next();
        }

        PasswordManager pm = new PasswordManager();

        String username = null;
        if (existing.equals("Y")) {
            username = pm.userLogin();
        } else {
            try {
                username = pm.addNewUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        if (username != null) {
            UserData data = UserData.loadRecord(username);
            TaxFree.computeTaxes(data);
        } else {
            System.out.println("Too many guesses, shutting down.");
        }
    }

    private static void computeTaxes(UserData data) {
        System.out.println("Application Started\n");
        if (data.hasSalary()) {
            int salary = data.getSalary();
            System.out.println("You make:  " + salary + ". You owe  " + salary*TAX_RATE + ". ");
        } else {
            System.out.println("No salary information found please enter a number: ");
            String newSal = scanner.next();
            if (validateSalary(newSal)) {
                Integer sal = Integer.parseInt(newSal);
                data.setSalary(Integer.parseInt(newSal));
                System.out.println("You make: " + sal + ". You owe " + sal*TAX_RATE + ".");
            }
        }

    }

    private static boolean validateSalary(String newSal) {
        try {
            Integer.parseInt(newSal);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
