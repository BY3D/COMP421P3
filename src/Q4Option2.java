/*
 * COMP 421 - Project 3 - Group 4
 * Written by Belal Yousofzai
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Q4Option2 {

    // Option 2. Calculate the travel time of a package across a route
    public static void findDeliveryTime(Statement stm, String o, String d, int s, Scanner in) throws SQLException {
        if (o.equalsIgnoreCase("quit") || d.equalsIgnoreCase("quit")) return;
        if (s < 1 || s > 2) {
            System.out.println("Invalid speed preference, default to priority");
            s = 1;
        }
        // First, find the distance of the route
        int dist = 0;
        String query = "SELECT distance FROM Route WHERE origin = '" + o + "' AND destination = '" + d + "';";
        try {
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            dist = rs.getInt("distance");
        } catch (SQLException sqle) {
            System.out.print("A route does not exist between these locations. Enter quit to exit. Otherwise, enter a valid origin ");
            o = in.nextLine();
            System.out.print("Now enter a valid destination ");
            d = in.nextLine();
            findDeliveryTime(stm, o, d, s, in);
            return;
        }
        int speed = 0;
        // Second, find the correct vehicle for the route and speed preference
        String vehicle = getVehicle(o, d, s);
        query = "SELECT speed FROM Transportation WHERE type LIKE '" + vehicle + "';";
        try {
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            speed = rs.getInt("speed");
        } catch (SQLException sqle) {
            System.out.println("The delivery vehicle or vessel could not be found. Returning to main menu");
        }
        // Third, return the travel time
        String typeOfDelivery = "";
        if (s == 1) typeOfDelivery = "priority";
        else typeOfDelivery = "economical";
        System.out.println("From " + o + " to " + d + " the distance is " + dist + "km");
        if (speed > dist) {
            double spd = speed / 60.0;
            double travelMinutes = Math.ceil(dist / spd);
            System.out.println("A " + typeOfDelivery + " delivery done by a " + vehicle + " would take approximately " + travelMinutes + " minutes");
        }
        else {
            int travelTime = dist / speed;
            System.out.println("A " + typeOfDelivery + " delivery done by a " + vehicle + " would take " + travelTime + " hours");
        }
    }

    // Helper method for Option 2.
    private static String getVehicle(String o, String d, int s) {
        String vehicle = "";
        boolean notLocal = true;
        // check if the route is inner-city, domestic, or international
        if (!o.contains(",") && o.equals(d)) { // City state (domestic) such as Singapore or Hong Kong
            if (s == 1) vehicle = "Train";
            else if (s == 2) vehicle = "Van";
            notLocal = false;
        }
        else if (o.equals(d)) { // inner-city
            if (s == 1) vehicle = "Lorry";
            else if (s == 2) vehicle = "Scooter";
            notLocal = false;
        }
        String originCountry = o.substring(o.indexOf(",") + 2);
        String destCountry = d.substring(d.indexOf(",") + 2);
        if (notLocal && originCountry.equals(destCountry)) { // domestic
            if (s == 1) vehicle = "Train";
            else if (s == 2) vehicle = "Van";
        }
        else if (notLocal) { // international
            if (s == 1) vehicle = "Airplane";
            if (s == 2) vehicle = "Boat";
        }
        return vehicle;
    }

}
