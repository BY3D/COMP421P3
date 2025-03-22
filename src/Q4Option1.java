import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Q4Option1 {

    // Option 1. Find the delivery date of an order
    public static void findDeliveryDate(Statement stm, int tID, Scanner in) throws SQLException {
        in.nextLine();
        if (tID == -1) return;
        String query = "SELECT currentLocation, ETA FROM Tracking WHERE tId = "
                + tID + ";";
        try {
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

}
