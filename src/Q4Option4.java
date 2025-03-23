/*
 * COMP 421 - Project 3 - Group 4
 *
 * Option 4: Update the unit price of the Goods associated with a specific Order
 * by applying a percentage change.
 * The method takes a Statement, an Order ID, and a Scanner.
 */

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.Scanner;
 
 public class Q4Option4 {
 
     public static void updateGoodsPricing(Statement stm, int orderID, Scanner in) throws SQLException {
         in.nextLine();
         if (orderID == -1) return;
         
         String query = "SELECT goodsId FROM Order WHERE oId = " + orderID + ";";
         try {
             ResultSet rs = stm.executeQuery(query);
             if (!rs.next()) {//no row is returned
                 System.out.print("Invalid order ID. Enter -1 to exit, or a valid order ID: ");
                 orderID = in.nextInt();
                 updateGoodsPricing(stm, orderID, in);
                 return;
             }
             int goodsId = rs.getInt("goodsId");
             
             double percentage = 0.0;
             boolean valid = false;
             while (!valid) {
                 System.out.print("Enter the percentage change (e.g., 5 for 5% increase, -10 for 10% decrease): ");
                 try {
                     percentage = Double.parseDouble(in.nextLine());
                     if (percentage < 0 && percentage < -100) {
                         System.out.println("Invalid percentage decrease. It must be between 0 and -100. Please try again.");
                     } else {
                         valid = true;
                     }
                 } catch (NumberFormatException e) {
                     System.out.println("Invalid percentage input. Please try again.");
                 }
             }
             
             double factor = 1 + (percentage / 100.0);
             
             String updateSQL = "UPDATE Goods SET unitPrice = unitPrice * " + factor + " WHERE gId = " + goodsId;
             int rowsAffected = stm.executeUpdate(updateSQL);
             System.out.println("Updated unit price for Goods with ID " + goodsId + " using a factor of " + factor +
                                ". Rows affected: " + rowsAffected);
         } catch (SQLException sqle) {
             System.out.println("SQL error: " + sqle.getMessage());
         }
     }
 }
 