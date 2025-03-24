/*
 * COMP 421 - Project 3 - Group 4
 *
 * Option 3: Add a new order to the order table. Update associated tables as well
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
         
         String orderCheckSQL = "SELECT oId FROM Order WHERE oId = " + orderId + ";";
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
             String clientCheckSQL = "SELECT cId FROM Client WHERE cId = " + clientId + ";";
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
             String goodsCheckSQL = "SELECT gId FROM Goods WHERE gId = " + goodsId + ";";
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
             String employeeCheckSQL = "SELECT eId FROM Employee WHERE eId = " + employeeId + ";";
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
                 if (quantity < 0) throw new NumberFormatException();
                 break;
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Quantity. Please try again.");
             }
         }
         
         System.out.print("Enter Order Summary: ");
         String summary = in.nextLine();
 
         String insertSQL = "INSERT INTO Order (oId, clientId, goodsId, employeeId, quantity, summary) "
                          + "VALUES (" + orderId + ", " + clientId + ", " + goodsId + ", " + employeeId + ", " 
                          + quantity + ", '" + summary + "');";
         try {
             int result = stm.executeUpdate(insertSQL);
             if (result > 0) {
                 System.out.println("New order added successfully.");
             } else {
                 System.out.println("Failed to add new order.");
                 return;
             }
         } catch (SQLException e) {
             System.out.println("Error inserting new order: " + e.getMessage());
             return;
         }
         

         
         // Check/Prompt for Tracking ID
         // Use the orderId as the default trackingId.
         int trackingId = orderId;
         String trackingCheckSQL = "SELECT tId FROM Tracking WHERE tId = " + trackingId + ";";
         rs = stm.executeQuery(trackingCheckSQL);
         while (rs.next()) {  // if trackingId is taken, prompt for a new one
             System.out.print("Tracking ID " + trackingId + " is already taken. Enter a new Tracking ID (or -1 to exit): ");
             try {
                 trackingId = Integer.parseInt(in.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid Tracking ID. Aborting tracking record creation.");
                 return;
             }
             if (trackingId == -1) return;
             trackingCheckSQL = "SELECT tId FROM Tracking WHERE tId = " + trackingId + ";";
             rs = stm.executeQuery(trackingCheckSQL);
         }
         
         // Insert a Tracking record 
         String insertTrackingSQL = 
             "INSERT INTO Tracking (tId, oId, currentLocation, ETA) "
           + "VALUES (" + trackingId + ", " + orderId + ", 'Warehouse', CURRENT DATE + 3 DAYS);";
         try {
             int trackingResult = stm.executeUpdate(insertTrackingSQL);
             if (trackingResult > 0) {
                 System.out.println("Tracking record added successfully with Tracking ID " + trackingId);
             } else {
                 System.out.println("Failed to add tracking record.");
             }
         } catch (SQLException e) {
             System.out.println("Error inserting tracking record: " + e.getMessage());
         }
         
         // Insert into Contains relationship 
         String insertContainsSQL = "INSERT INTO Contains (oId, gId) VALUES (" + orderId + ", " + goodsId + ");";
         try {
             int containsResult = stm.executeUpdate(insertContainsSQL);
             if (containsResult > 0) {
                 System.out.println("Contains record added successfully.");
             } else {
                 System.out.println("Failed to add Contains record.");
             }
         } catch (SQLException e) {
             System.out.println("Error inserting Contains record: " + e.getMessage());
         }
         
         // Determine the fastest transportation method 
         String fastestTransportSQL = "SELECT tId FROM Transportation ORDER BY speed DESC FETCH FIRST 1 ROWS ONLY;";
         int transportId = -1;
         try {
             rs = stm.executeQuery(fastestTransportSQL);
             if (rs.next()) {
                 transportId = rs.getInt("tId");
             } else {
                 System.out.println("No transportation record found.");
             }
         } catch (SQLException e) {
             System.out.println("Error retrieving transportation record: " + e.getMessage());
         }
         
         if (transportId != -1) {
             // Insert into Ships relationship 
             String insertShipsSQL = "INSERT INTO Ships (tId, oId, distanceCovered) "
                                   + "VALUES (" + transportId + ", " + orderId + ", 0.00);";
             try {
                 int shipsResult = stm.executeUpdate(insertShipsSQL);
                 if (shipsResult > 0) {
                     System.out.println("Ships record added successfully.");
                 } else {
                     System.out.println("Failed to add Ships record.");
                 }
             } catch (SQLException e) {
                 System.out.println("Error inserting Ships record: " + e.getMessage());
             }
             
             // Prompt the user for a Route ID 
             int routeId;
             while (true) {
                 System.out.print("Enter Route ID: ");
                 try {
                     routeId = Integer.parseInt(in.nextLine());
                 } catch (NumberFormatException e) {
                     System.out.println("Invalid Route ID. Please try again.");
                     continue;
                 }
                 String routeCheckSQL = "SELECT rId FROM Route WHERE rId = " + routeId + ";";
                 rs = stm.executeQuery(routeCheckSQL);
                 if (rs.next()) {
                     break;
                 } else {
                     System.out.println("Route ID does not exist. Please try again.");
                 }
             }
             
             // Insert into Follows relationship 
             String insertFollowsSQL = "INSERT INTO Follows (tId, rId, oId) "
                                     + "VALUES (" + transportId + ", " + routeId + ", " + orderId + ");";
             try {
                 int followsResult = stm.executeUpdate(insertFollowsSQL);
                 if (followsResult > 0) {
                     System.out.println("Follows record added successfully.");
                 } else {
                     System.out.println("Failed to add Follows record.");
                 }
             } catch (SQLException e) {
                 System.out.println("Error inserting Follows record: " + e.getMessage());
             }
         }
         
         System.out.println("Returning to main menu.\n");
     }
 }
 