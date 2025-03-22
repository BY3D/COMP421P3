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
                    findDeliveryDate(statement, userInput, input);
                    System.out.print("Press any key to return to the menu");
                    input.nextLine();
                    System.out.println();
                    continue;
                case 2:
                    int speed;
                    System.out.print("Enter 1 for a priority delivery or 2 for an economical delivery");
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
                    findDeliveryTime(statement, origin, destination, speed, input);
                    System.out.print("Press any key to return to the menu");
                    input.nextLine();
                    System.out.println();
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
                        String[] values = info.split(" ");
                        orderID = Integer.parseInt(values[0]);
                        employeeID = Integer.parseInt(values[1]);
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input, returning to main menu");
                        continue;
                    }
                    reassignEmployeeOrder(statement, orderID, employeeID ,input);
                    System.out.print("Press any key to return to the menu");
                    input.nextLine();
                    continue;
                case 6:
                    keepLooping = false;
                    break;
                default:
                    System.out.println("Invalid option, try again");
                    System.out.print("Press any key to continue");
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
        in.nextLine();
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

    /*
     * NOTE
     * For option 2, the user inputs an origin and destination
     * Example: "Stuttgart, Germany", "Dubai, UAE"
     * If user selected slow
     * International route = boat
     * Domestic route = van
     * Inner City route = scooter
     * If user selected fast
     * International route = airplane
     * Domestic route = train
     * Inner City route = Lorry
     * Return a query that gives the number of hours a vehicle will travel over the route
     */
    // Option 2. Calculate the travel time of a package across a route
    private static void findDeliveryTime(Statement stm, String o, String d, int s, Scanner in) throws SQLException {
        if (o.equalsIgnoreCase("quit")) return;
        // First, find the distance of the route
        int dist = 0;
        String query = "SELECT distance FROM Route WHERE origin = " + o + " AND destination = " + d + ";";
        try {
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            dist = rs.getInt("distance");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.print("Invalid input locations. Enter quit to exit. Otherwise, enter a valid origin");
            o = in.nextLine();
            System.out.print("Enter a valid destination");
            d = in.nextLine();
            findDeliveryTime(stm, o, d, s, in);
            return;
        }
        int speed = 0;
        String vehicle = "";
        // Second, check if the route is inner-city, domestic, or international
        if (o.equals(d)) { // inner-city
            if (s == 1) vehicle = "'Lorry'";
            else if (s == 2) vehicle = "'Scooter'";
        }
        if (!o.contains(",")) { // City state (domestic) such as Singapore or Hong Kong
            if (s == 1) vehicle = "'Train'";
            else if (s == 2) vehicle = "'Van'";
        }
        String originCountry = o.substring(o.indexOf(",") + 1);
        String destCountry = d.substring(d.indexOf(",") + 1);
        if (originCountry.equals(destCountry)) { // domestic
            if (s == 1) vehicle = "'Train'";
            else if (s == 2) vehicle = "'Van'";
        }
        if (!originCountry.equals(destCountry)) { // international
            if (s == 1) vehicle = "'Airplane'";
            if (s == 2) vehicle = "'Boat'";
        }
        query = "SELECT speed FROM Transportation WHERE type LIKE " + vehicle + ";";
        try {
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            speed = rs.getInt("speed");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        // Third, return the travel time
        int travelTime = dist / speed;
        String typeOfDelivery = "";
        if (s == 1) typeOfDelivery = "priority";
        else typeOfDelivery = "economical";
        System.out.println("From " + o + " to " + d + " the distance is " + dist);
        System.out.println("A " + typeOfDelivery + " delivery done by a " + vehicle + " would take " + travelTime + "hours");

    }

    // Option 3. Add a new Order to the Order table
    private static void addNewOrder(Statement stm, int oID, Scanner in) throws SQLException {
        //
    }

    // Option 4. Update the unit price of goods by the percentage given by the user
    private static void updateGoodsPricing(Statement stm, double priceChange, Scanner in) throws SQLException {
        //
    }

    // Option 5. Reassign an employee to an order
    private static void reassignEmployeeOrder(Statement stm, int oID, int eID, Scanner in) throws SQLException {
        if (oID == -1 || eID == -1) return;
        String getOriginalOrder = "SELECT oId, clientId, employeeId, quantity, summary FROM Order WHERE oId = " + oID + ";";
        String getoldEmpName = "SELECT name FROM Employee WHERE eId IN (SELECT employeeId FROM Order WHERE oId = " + oID + ");";
        String getnewEmpName = "SELECT name FROM Employee WHERE eId = " + eID + ";";
        String oldEmpName = "";
        String newEmpName = "";
        String desc = "";
        // First, get the original order record
        try {
            ResultSet rs = stm.executeQuery(getOriginalOrder);
            rs.next();
            int oid = rs.getInt("oId");
            int cid = rs.getInt("clientId");
            int eid = rs.getInt("employeeId");
            int q = rs.getInt("quantity");
            String sum = rs.getString("summary");
            System.out.println("The order to update: " + oid + " " + cid + " " + eid + " " + q + " " + sum);
        } catch (SQLException sqle) {
            System.out.print("Invalid order number. Enter -1 to exit, valid order number otherwise ");
            oID = in.nextInt();
            in.nextLine();
            reassignEmployeeOrder(stm, oID, eID, in);
            return;
        }
        // Second, get the old employee's first name
        try {
            ResultSet rs = stm.executeQuery(getoldEmpName);
            rs.next();
            oldEmpName = rs.getString("name");
            int mid = oldEmpName.indexOf(" ");
            oldEmpName = oldEmpName.substring(0, mid + 2);
            /*
             * Some employees have the same first name
             * Capture the first letter of their last name
             * In case we reassign employees with similar names
             */
        } catch (SQLException sqle) {
            System.out.println("Current employee of order could not be found. Returning to main menu");
            return;
        }
        // Third, get the new employee's first name
        try {
            ResultSet rs = stm.executeQuery(getnewEmpName);
            rs.next();
            newEmpName = rs.getString("name");
            int mid = newEmpName.indexOf(" ");
            newEmpName = newEmpName.substring(0, mid + 2);
        } catch (SQLException sqle) {
            System.out.println("Invalid employee ID. Enter -1 to exit, valid employee ID otherwise ");
            eID = in.nextInt();
            in.nextLine();
            reassignEmployeeOrder(stm, oID, eID, in);
            return;
        }
        // Fourth, edit the order's description to have the new employee
        try {
            String getDesc = "SELECT summary FROM Order WHERE oId = " + oID + ";";
            ResultSet rs = stm.executeQuery(getDesc);
            rs.next();
            desc = rs.getString("summary");
            int space = oldEmpName.indexOf(" ");
            // If first names are equal, then include first letter of last name
            if (oldEmpName.substring(0, space).equals(newEmpName.substring(0, space))
            && oldEmpName.charAt(oldEmpName.length() - 1) != newEmpName.charAt(newEmpName.length() - 1)) {
                int index = desc.indexOf("by ") + 3;
                desc = desc.substring(0, index);
                desc = desc + newEmpName;
            }
            else {
                space = newEmpName.indexOf(" ");
                newEmpName = newEmpName.substring(0, space);
                int index = desc.indexOf("by ") + 3;
                desc = desc.substring(0, index);
                desc = desc + newEmpName;
            }
        } catch (SQLException sqle) {
            System.out.println("Order summary could not be found. Returning to main menu");
            return;
        }
        // Fifth, update the order record to have the new employee ID
        try {
            String updateOrder = "UPDATE Order SET employeeId = " + eID + ", summary = '" + desc + "' WHERE oId = " + oID + ";";
            stm.executeUpdate(updateOrder);
        } catch (SQLException sqle) {
            System.out.println("Order could not updated. Returning to main menu");
            return;
        }
        // Sixth, output updated order record
        try {
            String updatedOrder = "SELECT oId, clientId, employeeId, quantity, summary FROM Order WHERE oId = " + oID + ";";
            ResultSet rs = stm.executeQuery(updatedOrder);
            rs.next();
            int oid = rs.getInt("oId");
            int cid = rs.getInt("clientId");
            int eid = rs.getInt("employeeId");
            int q = rs.getInt("quantity");
            String sum = rs.getString("summary");
            System.out.println("The updated order: " + oid + " " + cid + " " + eid + " " + q + " " + sum);
        } catch (SQLException sqle) {
            System.out.println("The modified order could not be found. Returning to main menu");
        }
    }

}
