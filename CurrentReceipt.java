/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;

import Connection.DatabaseConnection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OpenSource
 */
public class CurrentReceipt {
    
    private Statement statement = null;
    private ResultSet resultSet = null;
    private static ResultSet resultSetRunningBal = null;
    private static PreparedStatement preparedStatement = null;
    private static PreparedStatement pst = null;
    private static int currentDay;
    private static int currentMonth;
    private static int currentYear;
    private static Calendar calendar, dbCalender;

    
    
     public static boolean isCurrent(int year, int month, int day)
    {
         calendar = new GregorianCalendar();
         currentDay = calendar.get(Calendar.DAY_OF_MONTH);
         currentMonth = calendar.get(Calendar.MONTH) + 1;
         currentYear = calendar.get(Calendar.YEAR);
         
        return ( (currentMonth == month) && (currentYear == year)) ; 
    }
     
      public static void insertIntoTransation(String id, String accnum, String shopnum, String  plan, String txncode, double amount, java.time.LocalDate date, String refnum ,Connection con)
    {
        
    
        if(con != null)
        {
            // System.out.println("Preparing to insert into transcations");
             try {
                 String query = " INSERT INTO `transcations` (accountnumber, storenumber, plan, txncode, amount,effdate, postingdate, ref) VALUES(?,?,?,?,?,?,?,?)";

                 pst = con.prepareStatement(query);

                 pst.setString(1, accnum);
                 pst.setString(2, shopnum);
                 pst.setString(3, plan);
                 pst.setString(4, txncode);
                 pst.setDouble(5, amount);
                 pst.setDate(6, java.sql.Date.valueOf( date));
                 pst.setDate(7, java.sql.Date.valueOf( date));
                 pst.setString(8, refnum);

                 pst.executeUpdate();
                 
                String queryUpdate = "UPDATE receipt SET interfaced = 'YES' WHERE id = ?";
                pst= con.prepareStatement(queryUpdate);
                pst.setString(1,id);
                pst.executeUpdate();

             } catch (SQLException ex) {
                // Logger.getLogger(Leon.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("Could not insert into transactions" + ex);
             }
        }
    }
      
       public static double[] getRunningBalance(String accnum,Connection con)
    {
        double[] values = new double[4];
        try {
            String query = " SELECT runningbalance AS RunningBalance, payoff AS Payoff, arrears AS Arrears, amountdue AS Amountdue FROM `statements` WHERE  accountnumber = ? ORDER BY id DESC LIMIT 1";
            
            pst= con.prepareStatement(query);
            pst.setString(1,accnum);
            resultSetRunningBal = pst.executeQuery();
            double runningbalance= 0.0;
            double payoff = 0.0;
            double arrears = 0.0;
            double amountdue = 0.0;
            
            if(resultSetRunningBal.next())
            {
                runningbalance = resultSetRunningBal.getDouble("RunningBalance");
                payoff = resultSetRunningBal.getDouble("Payoff");
                arrears = resultSetRunningBal.getDouble("Arrears");
                amountdue = resultSetRunningBal.getDouble("Amountdue");
                values[0] = runningbalance;
                values[1] = payoff;
                values[2] = arrears;
                values[3] = amountdue;
                
                //return values;
            }
            
            
            
        } catch (SQLException ex) {
           System.out.println("Could not get running balance" + ex);
            //Logger.getLogger(Leon.class.getName()).log(Level.SEVERE, null, ex);
        //return values;
        }
         return values;
    }
       
       public static void insertIntoStatement(String accnum, String shopnum, String plan, String txncode, Double amount, LocalDate date, String refnum, double runningBal, double payoff, double runningBal0, double arears, double amountdue,Connection con) {
        try {
                    String query = " INSERT INTO `statements` (accountnumber, storenumber, plan, txncode, amount,effdate, ref, runningbalance, payoff, openningbalance, arrears, amountdue) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                    pst = con.prepareStatement(query);

                    pst.setString(1, accnum);
                    pst.setString(2, shopnum);
                    pst.setString(3, plan);
                    pst.setString(4, txncode);
                    pst.setDouble(5, amount);
                    pst.setDate(6, java.sql.Date.valueOf( date));
                    pst.setString(7, refnum);
                    pst.setDouble(8, runningBal);
                    pst.setDouble(9, payoff);
                    pst.setDouble(10, runningBal0);
                    pst.setDouble(11, arears);
                    pst.setDouble(12, amountdue);
      

                    pst.executeUpdate();
                } catch (SQLException ex) {
                    System.out.print("Could not fetch running balance : " + ex);
                    //Logger.getLogger(Leon.class.getName()).log(Level.SEVERE, null, ex);
               
               }

    }
}
