/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;

import Connection.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author User1
 */
public class Utils {
    private static DatabaseConnection db = new DatabaseConnection();
    private static Connection con = null;

    public static void insertTransation(String id, String accnum, String shopnum, String plan, String txncode, double amount, java.time.LocalDate date, String refnum) throws IOException, SQLException {

       if(con == null)
           con = db.connect();
        //java.sql.Connection con = DriverManager.getConnection(url + dbName, "arthur", "adventistworld2013");
       
        Statement stat = (Statement) con.createStatement();

        String query = "insert into transcations (accountnumber, storenumber, plan, txncode, amount, effdate, ref)"
                + " values (?, ?, ?, ?, ?, ?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = con.prepareStatement(query);
       // preparedStmt.setString(1, id);
        preparedStmt.setString(1, accnum);
        preparedStmt.setString(2, shopnum);
        preparedStmt.setString(3, plan);
        preparedStmt.setString(4, txncode);
        preparedStmt.setDouble(5, amount);
        preparedStmt.setString(6, "" + date);
        preparedStmt.setString(7, refnum);
        // execute the preparedstatement
        preparedStmt.execute();
    }

    public static void writeStatement(String accnum, String shopnum,
            String plan, String txncode, Double amount, LocalDate date,
            String refnum, double runningBal, double payoff,
            double runningBal0, double arears,
            double amountdue) throws IOException, SQLException {
        
       if(con == null)
           con = db.connect();

        Statement stat = (Statement) con.createStatement();

        String query = " insert into statements (accountnumber, storenumber, plan, txncode, amount, effdate, ref,runningbalance,payoff,openningbalance,arrears,amountdue)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setString(1,accnum);
        preparedStmt.setString(2,shopnum);
        preparedStmt.setString(3,plan);
        preparedStmt.setString(4,txncode);
        preparedStmt.setDouble(5,amount);
        preparedStmt.setString(6,""+date);
        preparedStmt.setString(7,refnum);
        preparedStmt.setDouble(8,runningBal);
        preparedStmt.setDouble(9,payoff);
        preparedStmt.setDouble(10,runningBal0);
        preparedStmt.setDouble(11,arears);
        preparedStmt.setDouble(12,amountdue);
        // execute the preparedstatement
        preparedStmt.execute();
   
    }
}
