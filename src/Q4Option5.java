/*
 * COMP 421 - Project 3 - Group 4
 * Option 5. Reassign an employee to an order
 * Input: Order ID and the replacement Employee ID
 * Output: The order record in its previous and updated state
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Q4Option5 {

    public static void reassignEmployeeOrder(Statement stm, int oID, int eID, Scanner in) throws SQLException {
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
