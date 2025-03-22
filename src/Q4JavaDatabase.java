/*
 * COMP 421 - Project 3 - Group 04
 * Written by: Belal Yousofzai & Kassem Yassine
 */

import com.ibm.db2.jcc.DB2Driver;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Include the db2jcc4.jar file as a library file to make the remote SQL methods work
public class Q4JavaDatabase {

    public static void main(String[] args) throws SQLException {
        // Register the DB2 Driver
        try {DriverManager.registerDriver(new DB2Driver());}
        catch (Exception cnfe){ System.out.println("Driver Class not found"); }
        // Connect to DB2
        String url = "jdbc:db2://winter2025-comp421.cs.mcgill.ca:50000/comp421";
        String groupId = "cs421g04";
        String groupPassword = System.getenv("SOCSPASSWD"); // This can be given as an environment variable for now
        Connection connectDB2 = DriverManager.getConnection (url, groupId, groupPassword);
        Statement statement = connectDB2.createStatement();
        // The primary loop which the user interacts with
        Scanner input = new Scanner(System.in);
        boolean keepLooping = true;
        System.out.println("Main Menu of KABY 🚚");
        while (keepLooping) {
            int userInput = 6;
            // There should be at least 5 options for the user, excluding quit
            System.out.println("The following options are available");
            System.out.println("1. Find the delivery date of an order");
            System.out.println("2. Estimate the delivery time of a package");
            System.out.println("3. Add a new order");
            System.out.println("4. Update the price of goods to the inflation rate");
            System.out.println("5. Reassign an employee to an order");
            System.out.println("6. Quit");
            System.out.print("Enter an Option: ");
            try {
                userInput = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException ime) {
                userInput = -1;
            }
            switch (userInput) {
                case 1:
                    System.out.print("Enter your tracking number: ");
                    try {
                        userInput = input.nextInt();
                    } catch (InputMismatchException ime) {
                        userInput = -1;
                    }
                    if (userInput < 0) {
                        System.out.println("Invalid input, returning to main menu");
                        input.nextLine();
                        continue;
                    }
                    Q4Option1.findDeliveryDate(statement, userInput, input);
                    System.out.print("Press any key to return to the menu");
                    input.nextLine();
                    System.out.println();
                    continue;
                case 2:
                    int speed;
                    System.out.print("Enter 1 for a priority delivery or 2 for an economical delivery ");
                    try {
                        speed = input.nextInt();
                        input.nextLine();
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid speed preference, default to priority");
                        speed = 1;
                    }
                    System.out.print("Enter the origin: ");
                    String origin = "";
                    String destination = "";
                    try {
                        origin = input.nextLine();
                        System.out.print("Enter the destination: ");
                        destination = input.nextLine();
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input, returning to main menu");
                        input.nextLine();
                        continue;
                    }
                    Q4Option2.findDeliveryTime(statement, origin, destination, speed, input);
                    System.out.print("Press any key to return to the menu ");
                    input.nextLine();
                    System.out.println();
                    continue;
                case 3:
                    // Q4Option3.addNewOrder(statement, oID, input);
                    continue;
                case 4:
                    // Q4Option4.updateGoodsPricing(statement, priceChange, input);
                    continue;
                case 5:
                    // User input should be "# #"
                    System.out.print("Enter the order ID and employee ID: ");
                    int orderID;
                    int employeeID;
                    try {
                        String info = input.nextLine();
                        String[] values = info.split(" ");
                        orderID = Integer.parseInt(values[0]);
                        employeeID = Integer.parseInt(values[1]);
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input, returning to main menu");
                        continue;
                    }
                    Q4Option5.reassignEmployeeOrder(statement, orderID, employeeID ,input);
                    System.out.print("Press any key to return to the menu");
                    input.nextLine();
                    continue;
                case 6:
                    keepLooping = false;
                    break;
                default:
                    System.out.println("Invalid option, try again");
                    System.out.print("Press any key to continue ");
                    input.nextLine();
                    input.nextLine();
            }
        }
        // Close the database connections
        input.close();
        statement.close();
        connectDB2.close();
    }

    // Originally, all methods for the options were stored here. They have since been moved to dedicated files


}
