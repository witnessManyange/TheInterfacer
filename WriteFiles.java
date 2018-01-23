/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author OpenSource
 */
public class WriteFiles {
    
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;
    private static FileWriter fileTransWriter;
    private static PrintWriter printTransWriter;
    
    public static void openFiles(String statements) throws IOException{
        if(fileWriter==null){
            fileWriter = new FileWriter(statements);
            printWriter = new PrintWriter(fileWriter);
        }
    }
    
    public static void closeFiles() throws IOException{
        fileWriter.close();
        printWriter.close();
    }
    
    public static void openTransFiles(String transcations) throws IOException{
        if(fileTransWriter==null){
            fileTransWriter = new FileWriter(transcations);
            printTransWriter = new PrintWriter(fileTransWriter);
        }
    }
    
    public static void closeTransFiles() throws IOException{
        fileTransWriter.close();
        printTransWriter.close();
    }
    
    public static void writeStatements(String accnum, String shopnum, 
            String plan, String txncode, Double amount, LocalDate date, 
            String refnum, double runningBal, double payoff,
         double runningBal0, double arears,
         double amountdue) throws IOException{
        
            printWriter.print(accnum+",");
            printWriter.print(shopnum+",");
            printWriter.print(plan+",");
            printWriter.print(txncode+",");
            printWriter.print(amount+",");
            printWriter.print(date+",");
            printWriter.print(refnum+",");
            printWriter.print(runningBal+",");
            printWriter.print(payoff+",");
            printWriter.print(runningBal0+",");
            printWriter.print(arears+",");
            printWriter.print(amountdue+"\n");
            //printWriter.print(reversed_interest+"\n");
            
           
            
    }
    
    public static void writeTransations(String id, String accnum, String shopnum, String  plan, String txncode, double amount, java.time.LocalDate date, String refnum) throws IOException{
            
            printTransWriter.print(accnum+",");
            printTransWriter.print(shopnum+",");
            printTransWriter.print(plan+",");
            printTransWriter.print(txncode+",");
            printTransWriter.print(amount+",");
            printTransWriter.print(date+",");
            printTransWriter.print(java.sql.Date.valueOf(java.time.LocalDate.now())+",");
            printTransWriter.print(refnum+"\n");
            
            
    }
    
    public static void uploadFiles(String transactions, String statements, String rejects, Connection con)
    {
        Statement statement  = null;
          String query = "";
        try 
        {
            
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            query = "LOAD DATA INFILE '"+transactions+"' INTO TABLE transcations FIELDS TERMINATED BY ',' (accountnumber, storenumber, plan, txncode, amount,effdate,postingdate, ref)";
            statement.executeQuery(query);
            
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            query = "LOAD DATA INFILE '"+statements +"' INTO TABLE statements FIELDS TERMINATED BY ',' (accountnumber, storenumber, plan, txncode, amount,effdate, ref, runningbalance, payoff, openningbalance, arrears, amountdue)";
            statement.executeQuery(query);
            
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            query = "LOAD DATA INFILE '"+rejects +"' INTO TABLE rejects_table FIELDS TERMINATED BY ',' (accnum,reason,refnum,rejectiontype,amount)";
            statement.executeQuery(query);
        }
        catch (SQLException ex)
        {
           System.out.println("Could not upload files " + ex);
            //Logger.getLogger(WriteFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
