/*
 * COMP 421 - Project 3 - Group 4
 *
 * Option 3: Add a new Order to the Order table.
 * This method takes a Statement, an Order ID, and a Scanner.
 */

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.Scanner;
 
 public class Q4Option3 {
 
     public static void addNewOrder(Statement stm, int orderId, Scanner in) throws SQLException {
         in.nextLine();
         if (orderId == -1) return;
         
         String orderCheckSQL = "SELECT oId FROM \"Order\" WHERE oId = " + orderId;
         ResultSet rs = stm.executeQuery(orderCheckSQL);
         if (rs.next()) {
             System.out.print("Order ID already exists. Enter a new Order ID (or -1 to exit): ");
             try {
                 orderId = Integer.parseInt(in.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Order ID. Aborting.");
                 return;
             }
             addNewOrder(stm, orderId, in);
             return;
         }
         
         int clientId;
         while (true) {
             System.out.print("Enter Client ID: ");
             try {
                 clientId = Integer.parseInt(in.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Client ID. Please try again.");
                 continue;
             }
             String clientCheckSQL = "SELECT cId FROM Client WHERE cId = " + clientId;
             rs = stm.executeQuery(clientCheckSQL);
             if (rs.next()) {
                 break;
             } else {
                 System.out.println("Client ID does not exist. Please try again.");
             }
         }
         
         int goodsId;
         while (true) {
             System.out.print("Enter Goods ID: ");
             try {
                 goodsId = Integer.parseInt(in.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Goods ID. Please try again.");
                 continue;
             }
             String goodsCheckSQL = "SELECT gId FROM Goods WHERE gId = " + goodsId;
             rs = stm.executeQuery(goodsCheckSQL);
             if (rs.next()) {
                 break;
             } else {
                 System.out.println("Goods ID does not exist. Please try again.");
             }
         }
         
         int employeeId;
         while (true) {
             System.out.print("Enter Employee ID: ");
             try {
                 employeeId = Integer.parseInt(in.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Employee ID. Please try again.");
                 continue;
             }
             String employeeCheckSQL = "SELECT eId FROM Employee WHERE eId = " + employeeId;
             rs = stm.executeQuery(employeeCheckSQL);
             if (rs.next()) {
                 break;
             } else {
                 System.out.println("Employee ID does not exist. Please try again.");
             }
         }
         
         int quantity;
         while (true) {
             System.out.print("Enter Quantity: ");
             try {
                 quantity = Integer.parseInt(in.nextLine());
                 break;
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Quantity. Please try again.");
             }
         }
         
         System.out.print("Enter Order Summary: ");
         String summary = in.nextLine();
         

         String insertSQL = "INSERT INTO \"Order\" (oId, clientId, goodsId, employeeId, quantity, summary) " +
                            "VALUES (" + orderId + ", " + clientId + ", " + goodsId + ", " + employeeId + ", " + quantity + ", '" + summary + "')";
         try {
             int result = stm.executeUpdate(insertSQL);
             if (result > 0) {
                 System.out.println("New order added successfully.");
             } else {
                 System.out.println("Failed to add new order.");
             }
         } catch (SQLException e) {
             System.out.println("Error inserting new order: " + e.getMessage());
             return;
         }
         
         System.out.println("Returning to main menu.\n");
     }
 }
 