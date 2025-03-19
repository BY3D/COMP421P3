// COMP 421 - Project 3 - Group 04

import java.sql.*;
import java.util.Scanner;

// Include the db2jcc4.jar file as a library file to make the remote SQL methods work
public class MainMenu {

    // A basic program. Change it
    public static void main(String[] args) throws SQLException {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("Main Menu of KABY 🚚");
            // There should be at least 5 options for the user, excluding quit
            System.out.println("1. Hi");
            System.out.println("6. Quit");
            System.out.print("Enter an Option: ");
            int userInput = input.nextInt();
            if (userInput == 6) {
                /*
                 * Close SQL statement and database connection
                 * statement.close() ;
                 * con.close() ;
                 */
                System.exit(0); // close program
            }
        }
    }

}
