/*
 * COMP 421 - Project 3 - Group 04
 * Written by: Belal Yousofzai (so far)
 */

import com.ibm.db2.jcc.DB2Driver;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Include the db2jcc4.jar file as a library file to make the remote SQL methods work
public class Q4JavaDatabase {

    public static void main(String[] args) throws SQLException {
        // Register the DB2 Driver
        try { DriverManager.registerDriver ( new DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }
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
            System.out.println("2. Estimate the fastest delivery time of a package");
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
                    findDeliveryDate(statement, userInput, input);
                    System.out.println("Press any key to return to the menu");
                    input.nextLine();
                    input.nextLine();
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
                    // User input should be "# #"
                    System.out.print("Enter the order ID and employee ID: ");
                    int orderID;
                    int employeeID;
                    try {
                        String info = input.nextLine();
                        orderID = Character.getNumericValue(info.charAt(0));
                        employeeID = Character.getNumericValue(info.charAt(2));
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input, returning to main menu");
                        break;
                    }
                    reassignEmployeeOrder(statement, orderID, employeeID);
                    System.out.println("Press any key to return to the menu");
                    input.nextLine();
                    input.nextLine();
                    continue;
                case 6:
                    keepLooping = false;
                    break;
                default:
                    System.out.println("Invalid option, try again");
                    System.out.println("Press any key to continue");
                    input.nextLine();
                    input.nextLine();
            }
        }
        // Close the database connections
        input.close();
        statement.close();
        connectDB2.close();
    }

    // For simplicity, all methods will be contained in this Java file

    // Option 1. Find the delivery date of an order
    private static void findDeliveryDate(Statement stm, int tID, Scanner in) throws SQLException {
        if (tID == -1) return;
        String query = "SELECT currentLocation, ETA FROM Tracking WHERE tId = "
                + tID + ";";
        try {
            // System.out.println(query);
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            String location = rs.getString(1);
            String eta = rs.getString(2);
            System.out.println("Parcel Info for Order " + tID);
            System.out.println("Current location of order: " + location);
            System.out.println("Expected arrival date of order: " + eta);
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            if (sqlCode == -4470) { // if tracking number is not in database
                System.out.print("Invalid tracking number. Enter -1 to exit, valid tracking number otherwise ");
                tID = in.nextInt();
                findDeliveryDate(stm, tID, in);
            }
        }
    }

    // Option 5. Reassign an employee to an order
    private static void reassignEmployeeOrder(Statement stm, int oID, int eID) throws SQLException {
        String getOldEmpId = "SELECT eId FROM Order WHERE oId = " + oID + ";";
        String getoldEmpName = "SELECT name FROM Employee WHERE eId = " + eID + ";";
        String getnewEmpName = "SELECT name FROM Employee WHERE eId = " + eID + ";";
        int oldEmpId = 0;
        String oldEmpName = "";
        String newEmpName = "";
        String desc = "";
        // First, get the old employee's ID
        try {
            ResultSet rs = stm.executeQuery(getOldEmpId);
            rs.next();
            oldEmpId = rs.getInt("eId");
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            System.out.println("Current employee could not be found " + sqlCode);
        }
        // Second, get the old employee's name
        try {
            ResultSet rs = stm.executeQuery(getoldEmpName);
            rs.next();
            oldEmpName = rs.getString("name");
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            System.out.println("Current employee could not be found " + sqlCode);
        }
        // Third, get the new employee's name
        try {
            ResultSet rs = stm.executeQuery(getnewEmpName);
            rs.next();
            newEmpName = rs.getString("name");
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            System.out.println("New employee could not be found " + sqlCode);
        }
        // Fourth, edit the order's description to have the new employee
        try {
            String getDesc = "SELECT description FROM Order WHERE oId = " + oID + ";";
            ResultSet rs = stm.executeQuery(getDesc);
            rs.next();
            desc = rs.getString("description");
            desc = desc.replace(oldEmpName, newEmpName);
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            System.out.println("Order could not be found " + sqlCode);
        }
        // Fifth, update the order record to have the new employee ID
        try {
            String updateOrder = "UPDATE Order SET eId = " + eID + ", description = " + desc + " WHERE oId = " + oID + ";";
            stm.executeUpdate(updateOrder);
        } catch (SQLException sqle) {
            int sqlCode = sqle.getErrorCode();
            System.out.println("Order could not updated " + sqlCode);
        }
    }

}
