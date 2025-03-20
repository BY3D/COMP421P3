// COMP 421 - Project 3 - Group 04

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Include the db2jcc4.jar file as a library file to make the remote SQL methods work
public class MainMenu {

    // A basic program. Change it
    public static void main(String[] args) throws SQLException {
        // Register the DB2 Driver
        /*
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }
        // Connect to DB2
        String url = "jdbc:db2://winter2025-comp421.cs.mcgill.ca:50000/comp421";
        String groupId = "cs421g04";
        String groupPassword = System.getenv("SOCSPASSWD"); // This can be given as an environment variable for now
        Connection connectDB2 = DriverManager.getConnection (url, groupId, groupPassword);
        Statement statement = connectDB2.createStatement();
         */
        // The primary loop which the user interacts with
        Scanner input = new Scanner(System.in);
        boolean keepLooping = true;
        System.out.println("Main Menu of KABY 🚚");
        int userInput;
        while (keepLooping) {
            // There should be at least 5 options for the user, excluding quit
            System.out.println("The following options are available");
            System.out.println("1. Find the delivery date of an order");
            System.out.println("2. Estimate the fastest delivery time of a package");
            System.out.println("3. Add a new order");
            System.out.println("4. Update the price of goods to the inflation rate");
            System.out.println("5. Reassign an employee to an order");
            System.out.println("6. Quit");
            System.out.print("Enter an Option: ");
            try {
                userInput = input.nextInt();
            } catch (InputMismatchException ime) {
                userInput = -1;
            }
            switch (userInput) {
                case 1:
                    //
                    continue;
                case 2:
                    //
                    continue;
                case 3:
                    //
                    continue;
                case 4:
                    //
                    continue;
                case 5:
                    //
                    continue;
                case 6:
                    keepLooping = false;
                    break;
                default:
                    System.out.println("\nInvalid option, try again\n");
            }
        }
        // Close the database connections
        /*
        statement.close();
        connectDB2.close();
         */
    }

}
