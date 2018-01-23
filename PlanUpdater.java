/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planupdater;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rudyard
 */
public class PlanUpdater {

   // static Connection con = null;
    private static int currentDay;
    private static int currentMonth;
    private static int currentYear;

    private static double processingFee = 4.25;
    private static double latePayment = 5.0;
    private static double insurance = 0.01;
    
    static Connection con = null;
    private static DatabaseConnection db = new DatabaseConnection();
    
    /**
     * @param args the command line arguments
     */
    public static void updatePlan() throws SQLException {
        con = db.connect();
        
        updateZ65EpmtyPLans(con);
        
        String query = " SELECT * FROM `statements` WHERE  (Plan1 = '0' ) ORDER BY id";
        Statement stat = (Statement)con.createStatement();
        ResultSet res = stat.executeQuery(query);

        String accnum = "";  
         String txncode = "";
         int id = 0;
         double amount = 0.0;
         
         double runningbalance = 0.0;
         
        while(res.next())
        {
            accnum = res.getString("accountnumber");
            txncode = res.getString("txncode");
            id = res.getInt("id");
            amount = res.getDouble("amount");
            
            fetchPlans(id,accnum,  txncode,  amount,con);
        }
        
    //    con.close();

    }

   public static void fetchPlans(int id, String accnum, String txncode, double amount,Connection con) throws SQLException
   {
       if(con == null)
       {
           con = db.connect();
       }
                        double plan1 = 0.0;
                        double plan2 = 0.0;
       
                        String query1 = " SELECT * FROM `statements` WHERE accountnumber = '"+accnum+"' AND (Plan1 <> '0')    ORDER BY id DESC LIMIT 1 ";
                                //" SELECT * FROM `statements` WHERE accountnumber = '"+accnum+"' AND txncode <> 'z65' AND ((plan1 = '') OR (plan2 = '') ) ORDER BY accountnumber "; //or the lastbalance such tht for tht row plan1 + plan2 = running balance
                        Statement stat1 = (Statement) con.createStatement();
                        ResultSet res1 = stat1.executeQuery(query1);
                        
                        double takeOnPlan1 = 0.0;
                        double takeOnPlan2 = 0.0;
                        String plan1String = "";
                        String plan2String = "";
                        
                         if(res1.next())
                        {
                           
                            plan1String = res1.getString("Plan1").trim();
                            plan2String = res1.getString("Plan2").trim();
                            //runningbalance = res.getDouble("runningbalance");
                            
                            try{    takeOnPlan1 = Double.parseDouble(plan1String);  }
                            
                            catch(NumberFormatException ex)
                            {
                                //System.out.println(id + ".Not amounts :" + accnum + " Plan 1:\t " + plan1String + " \tPlan 2 \t"  + plan2String );
                                
                            }
                            try 
                            {
                                takeOnPlan2 = Double.parseDouble(plan2String);
                            }
                            catch(NumberFormatException ex)
                            {
                            
                            }
                            
                            
                            
                             if(txncode.equalsIgnoreCase("52"))
                                {
                                    plan2 = takeOnPlan2 - amount;

                                    if(plan2 < 0)
                                    {
                                       plan1 = takeOnPlan1 + plan2 ;
                                       plan2 = 0;
                                    }
                                    else
                                    {
                                        plan1 = takeOnPlan1;
                                    }

                                   
                                }
                             else if(txncode.equalsIgnoreCase("2"))
                             {
                                 plan1 = takeOnPlan1 + amount;
                                 plan2 = takeOnPlan2;
                             }
                             else if(txncode.equalsIgnoreCase("4"))
                             {
                                 plan2 = takeOnPlan2 ;
                                 plan1 = takeOnPlan1- amount;
                             }
                             else if(txncode.equalsIgnoreCase("53"))
                             {
                                 plan1 = takeOnPlan1 + amount;
                                 plan2 = takeOnPlan2 ;//runningbalance - plan1;
                                 
                             }
                             else if(txncode.equalsIgnoreCase("20"))
                             {
                                 plan1 =   takeOnPlan1 - amount; 
                                 plan2 = takeOnPlan2;
                             }
                             else if(txncode.equalsIgnoreCase("21")||txncode.equalsIgnoreCase("100"))
                             {
                                 plan1 =   takeOnPlan1 + amount; 
                                 plan2 = takeOnPlan2;
                                 
                             }
                             else if(txncode.equalsIgnoreCase("12")||txncode.equalsIgnoreCase("105")||txncode.equalsIgnoreCase("25"))
                             {
                                plan1 = takeOnPlan1;
                                plan2 = takeOnPlan2 + amount;
                                
                               
                                
                             }
                              System.out.println("Updated " + accnum + " with id " + id);
                               updatePlans(id, plan1, plan2,con);
                            
                        }
                         else
                         {
                             
                          takeOnPlan1 = 0.0;
                        takeOnPlan2 = 0.0;
                        
                        if(txncode.equalsIgnoreCase("52"))
                                {
                                    plan2 = takeOnPlan2 - amount;

                                    if(plan2 < 0)
                                    {
                                       plan1 = takeOnPlan1 + plan2 ;
                                       plan2 = 0;
                                    }
                                    else
                                    {
                                        plan1 = takeOnPlan1;
                                    }

                                   
                                }
                             else if(txncode.equalsIgnoreCase("2"))
                             {
                                 plan1 = takeOnPlan1 + amount;
                                 plan2 = takeOnPlan2;
                             }
                             else if(txncode.equalsIgnoreCase("4"))
                             {
                                 plan2 = takeOnPlan2 ;
                                 plan1 = takeOnPlan1- amount;
                             }
                             else if(txncode.equalsIgnoreCase("53"))
                             {
                                 plan1 = takeOnPlan1 + amount;
                                 plan2 = takeOnPlan2 ;//runningbalance - plan1;
                                 
                             }
                             else if(txncode.equalsIgnoreCase("20"))
                             {
                                 plan1 =   takeOnPlan1 - amount; 
                                 plan2 = takeOnPlan2;
                             }
                             else if(txncode.equalsIgnoreCase("21")||txncode.equalsIgnoreCase("100"))
                             {
                                 plan1 =   takeOnPlan1 + amount; 
                                 plan2 = takeOnPlan2;
                                 
                             }
                             else if(txncode.equalsIgnoreCase("12")||txncode.equalsIgnoreCase("105")||txncode.equalsIgnoreCase("25"))
                             {
                                plan1 = takeOnPlan1;
                                plan2 = takeOnPlan2 + amount;
                                
                               
                                
                             }
                              System.out.println("Updated " + accnum + " with id " + id);
                               updatePlans(id, plan1, plan2,con);
                        
                        
                        
                            // System.out.println("No match");
                         }

   }
    public static void updatePlans(int id, double plan1, double plan2,Connection con)
    {
        try 
        {
            if(con == null)
            {
                con = db.connect();
            }
            String query = " UPDATE `statements` SET plan1 = '" + plan1 + "', plan2 ='" + plan2 + "'WHERE id = '" + id + "' ";
            Statement stat = (Statement) con.createStatement();
            stat.executeUpdate(query);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(PlanUpdater.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Could not update plans :" + ex);
        }
    }

    
    
    public static void updateZ65EpmtyPLans(Connection con) throws SQLException
    {
        if(con == null)
        {
            con = db.connect();
        }
        String query = " UPDATE `statements` SET plan1 = 0.0 WHERE txncode LIKE '%z65%' AND plan1 = ''  ";
        Statement stat = (Statement)con.createStatement();
        stat.executeUpdate(query);
        
    }

}
