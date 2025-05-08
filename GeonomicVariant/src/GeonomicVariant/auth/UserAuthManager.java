package GeonomicVariant.auth;

import java.io.*;
import java.util.*;

public class UserAuthManager {
    private static final String USERS_FILE = "data/users.txt";
    private final Scanner scanner = new Scanner(System.in);

    public String startSession() {
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    String username = loginUser();
                    if (username != null) return username;
                    break;
                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void registerUser() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        System.out.print("Choose a password: ");
        String password = scanner.nextLine();

        if (isUserExists(username)) {
            System.out.println("Username already exists. Try logging in.");
            return;
        }

        try (FileWriter fw = new FileWriter(USERS_FILE, true)) {
            fw.write("username: " + username + "\n");
            fw.write("password: " + password + "\n\n");
            System.out.println("Registration successful.");
        } catch (IOException e) {
            System.out.println("Error saving user.");
        }
    }

    private String loginUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            String storedUser = null;
            String storedPass = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("username: ")) {
                    storedUser = line.substring(10).trim();
                } else if (line.startsWith("password: ")) {
                    storedPass = line.substring(10).trim();

                    if (username.equals(storedUser) && password.equals(storedPass)) {
                        System.out.println("Login successful.");
                        return username;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }

        System.out.println("Invalid username or password.");
        return null;
    }

    private boolean isUserExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("username: " + username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }
}
