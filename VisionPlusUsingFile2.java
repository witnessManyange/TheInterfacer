/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author OpenSource
 */
public class VisionPlusUsingFile2 {

    static double tempOpeningBalance = 0.0;
    private static Connection con = null;
    private static  DatabaseConnection db = new DatabaseConnection();

    /**
     * @param args the command line arguments
     */
    public static void processReceipts() throws SQLException, Exception {
        // TODO code application logic here
        con = db.connect();
        if(con ==  null)
            con = db.connect();

        String receiptNumber = "";
        final double PROCESSING_FEE = 5.25;
        String id = "";
        String accnum = "";
        String amount2 = "";
        String rdate = "";
        double amount = 0;
        String refnum = "";
        String shop = "";
        String status = "";
        double new_running_bal = 0;
        double new_running_bal2 = 0;
        double reversed_interest = 0;
        double loan_amount = 0;
        double[] valuesFromStatment = new double[4];
        double payoff = 0;
        double arears = 0.0;
        double amountdue = 0.0;
        boolean valid = false;
        boolean is_current = false;
        String shopnum = "";
        String isvalid = "";
        String iscurrent = "";
        String plan = "0";
        String txncode = "00052";
        String transactions = "C:/Users/Administrator/Documents/NetBeansProjects/VisionPlusUsingFile/" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH.mm ss ").format(new Date())) + " transactions.xls";//"C:/Users/Rudyard/Documents/NetBeansProjects/VisionPlusUsingFile/transactions.xls"; //"C:/Users/OpenSource/Documents/NetBeansProjects/VisionPlusUsingFile/transactions.xls ";
        String statements = "C:/Users/Administrator/Documents//NetBeansProjects/VisionPlusUsingFile/" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH.mm ss ").format(new Date())) + " statements.xls";//"C:/Users/Rudyard/Documents/NetBeansProjects/VisionPlusUsingFile/statements.xls";//C:/Users/OpenSource/Documents/NetBeansProjects/VisionPlusUsingFile/statements.xls ";
        String rejects = "C:/Users/Administrator/Documents//NetBeansProjects/VisionPlusUsingFile/" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH.mm  ss ").format(new Date())) + " rejects.xls";//"C:/Users/Rudyard/Documents/NetBeansProjects/VisionPlusUsingFile/rejects.xls"; //"C:/Users/OpenSource/Documents/NetBeansProjects/VisionPlusUsingFile/rejects.xls ";
        String matchPrevAccNum = "";
        String interfacedStatus = "";

        Statement stat = (Statement) con.createStatement();

        ResultSet res = stat.executeQuery("SELECT * FROM  receipt WHERE (((interfaced = 'NO') AND (checked = 'YES') AND (status = 'Confirmed')) OR ((interfaced='YES') AND (checked = 'YES') AND (status = 'Reversed') and (reversal_interfaced='NO')) ) ORDER BY accnum");
       //ResultSet res = stat.executeQuery("SELECT * from receipt ");
        
        int num = 0;
        //ValidateReceipt.openFiles(rejects);
        //WriteFiles.openFiles(statements);
        //WriteFiles.openTransFiles(transactions);

        while (res.next()) 
        {
            num++;
            id = res.getString("id");
            receiptNumber = res.getString("receiptnum");
            accnum = res.getString("accnum");
            amount2 = res.getString("amount");
            rdate = res.getString("rdate");
            shopnum = res.getString("shopnum");
            shop = res.getString("shop");
            interfacedStatus = res.getString("interfaced");
            status = res.getString("status");
            
           
            valid = ValidateReceipt.validateReceipt(id, receiptNumber, accnum, amount2, rdate);

            if (valid) {

                isvalid = "yes";
                String dateparts[] = rdate.split("-");

                int year = Integer.parseInt(dateparts[0]);
                int month = Integer.parseInt(dateparts[1]);
                int day = Integer.parseInt(dateparts[2]);

                is_current = CurrentReceipt.isCurrent(year, month, day);
                loan_amount = getLoanAmount(accnum.trim(), con);
                refnum = res.getString("receiptnum");

                amount = Double.parseDouble(amount2);

                if (is_current) {

                    iscurrent = "yes";

                    if (status.equalsIgnoreCase("Confirmed"))
                    {

                        //WriteFiles.writeTransations(id, accnum, shopnum, plan, txncode, amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                        // the mysql insert statement
      
                        Utils.insertTransation(id, accnum, shopnum, plan, txncode, amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);
                        
                        if (matchPrevAccNum.equalsIgnoreCase(accnum)) {
                            new_running_bal = tempOpeningBalance;

                            tempOpeningBalance -= amount;
                            arears -= amount;

                            amountdue -= amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {

                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0;
                            }

                            Utils.writeStatement(accnum, shopnum, plan, "52",
                                    amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears,
                                    amountdue);

                        } else {
                            matchPrevAccNum = accnum;

                            valuesFromStatment = CurrentReceipt.getRunningBalance(accnum, con);

                            tempOpeningBalance = valuesFromStatment[0];
                            arears = valuesFromStatment[2];
                            amountdue = valuesFromStatment[3];

                            new_running_bal = tempOpeningBalance;
                            tempOpeningBalance -= amount;
                            arears = arears - amount;

                            amountdue = amountdue - amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {

                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0;
                            }

                            Utils.writeStatement(accnum, shopnum, plan,"52",
                                    amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears,
                                    amountdue);
                        }
                    }
                    else if (status.equalsIgnoreCase("Reversed"))// if status equal yes ---> the receipt has been reversed
                    {

                        //-------------------------------------------------------------------------
                        Utils.insertTransation(id, accnum, shopnum, plan, "00053", amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                        if (matchPrevAccNum.equalsIgnoreCase(accnum)) {

                            new_running_bal = tempOpeningBalance;
                            tempOpeningBalance += amount;
                            //new_running_bal -= amount;
                            arears += amount;

                            amountdue += amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {

                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0;
                            }

                            Utils.writeStatement(accnum, shopnum, plan, "53",
                                    amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears,
                                    amountdue);

                        } else {
                            matchPrevAccNum = accnum;

                            valuesFromStatment = CurrentReceipt.getRunningBalance(accnum, con);

                            tempOpeningBalance = valuesFromStatment[0];
                            arears = valuesFromStatment[2];
                            amountdue = valuesFromStatment[3];

                            new_running_bal = tempOpeningBalance;
                            tempOpeningBalance += amount;
                            arears += amount;

                            amountdue += amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {

                                payoff = (tempOpeningBalance * 1.09) ;
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0;
                            }

                            Utils.writeStatement(accnum, shopnum, plan, "53",
                                    amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears,
                                    amountdue);

                        }
                        
                    setReversalInterfaced(id, "YES", accnum, con);
                        //-------------------------------------------------------------------------

                    }

                } else {

                    iscurrent = "no";

                    reversed_interest = BackDateReceipt.getReversedInterest(amount, rdate, rdate,shop);

                    if (status.equalsIgnoreCase("Confirmed")) 
                    {

                        if (matchPrevAccNum.equalsIgnoreCase(accnum)) {

                            new_running_bal = tempOpeningBalance;

                            tempOpeningBalance -= amount;

                            amountdue = amountdue - amount;
                            arears = arears - amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "52", amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, txncode, amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                            new_running_bal = tempOpeningBalance;

                            tempOpeningBalance -= reversed_interest;
                            amountdue = amountdue - reversed_interest;
                            arears = arears - reversed_interest;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "21", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00021", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                        } else {

                            matchPrevAccNum = accnum;
                            valuesFromStatment = CurrentReceipt.getRunningBalance(accnum, con);

                            tempOpeningBalance = valuesFromStatment[0];
                            payoff = valuesFromStatment[1];
                            arears = valuesFromStatment[2];
                            amountdue = valuesFromStatment[3];
                            new_running_bal = tempOpeningBalance;

                            arears -= amount;

                            amountdue -= amount;
                            tempOpeningBalance = tempOpeningBalance - amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "52", amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, txncode, amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                            new_running_bal = tempOpeningBalance;
                            tempOpeningBalance = tempOpeningBalance - reversed_interest;
                            arears -= reversed_interest;

                            amountdue = amountdue - reversed_interest;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }
                            Utils.writeStatement(accnum, shopnum,
                                    plan, "20", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00020", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                        }
                    } 
                    else if (status.equalsIgnoreCase("Reversed"))// not current and reversed 
                    {

                        //------------------------------------------------------------------------
                        if (matchPrevAccNum.equalsIgnoreCase(accnum)) {

                            new_running_bal = tempOpeningBalance;

                            tempOpeningBalance += amount;

                            amountdue += amount;
                            arears += amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "53", amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00053", amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                            new_running_bal = tempOpeningBalance;

                            tempOpeningBalance += reversed_interest;
                            amountdue += reversed_interest;
                            arears += reversed_interest;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "21", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00021", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                           
                        } else {

                            matchPrevAccNum = accnum;
                            valuesFromStatment = CurrentReceipt.getRunningBalance(accnum, con);

                            tempOpeningBalance = valuesFromStatment[0];
                            payoff = valuesFromStatment[1];
                            arears = valuesFromStatment[2];
                            amountdue = valuesFromStatment[3];
                            new_running_bal = tempOpeningBalance;

                            arears += amount;

                            amountdue += amount;
                            tempOpeningBalance += amount;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "53", amount, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00053", amount, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                            new_running_bal = tempOpeningBalance;
                            tempOpeningBalance += reversed_interest;
                            arears += reversed_interest;

                            amountdue += reversed_interest;

                            if (shop.contains("Electro") || shop.contains("Enbee")) {
                                payoff = (tempOpeningBalance * 1.09);
                            } else {
                                payoff = (tempOpeningBalance * 1.08) + (loan_amount * 0.01) + PROCESSING_FEE;
                            }
                            if (payoff < 0) {
                                payoff = 0.0;
                            }

                            Utils.writeStatement(accnum, shopnum,
                                    plan, "21", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(),
                                    refnum, tempOpeningBalance, payoff, new_running_bal, arears, amountdue);

                            Utils.insertTransation(id, accnum, shopnum, plan, "00021", reversed_interest, java.sql.Date.valueOf(rdate).toLocalDate(), refnum);

                           
                        }

                         setReversalInterfaced(id, "YES", accnum, con);
                        //------------------------------------------------------------------------
                    }

                }

                setInterfacedStatus(id, "YES", accnum, con);
            } else {

                setInterfacedStatus(id, "REJ", accnum, con);
                isvalid = "no";

            }
            System.out.println("\n**********************************************" + num + "*****************************************************************");
            //System.out.println("Closing balance If current....." + new_running_bal);
            System.out.println("account number........." + accnum);
            System.out.println("Valid........." + isvalid);
            System.out.println("Current........." + iscurrent);
            System.out.println("Reversed Interest........." + reversed_interest);

            System.out.println("Rdate........." + rdate);
            System.out.println("receiptnum........." + receiptNumber);
            System.out.println("Opening runing balance....." + valuesFromStatment[0]);

            System.out.println("amount........." + amount);
            System.out.println("Closing balance is not current....." + new_running_bal2);
            System.out.println("Closing balance If current....." + new_running_bal);
            System.out.println("Loan amount....." + loan_amount);
            System.out.println("Payoff....." + payoff);
            System.out.println("***************************************************************************************************************");

        }
        //ValidateReceipt.closeFiles();
        //WriteFiles.closeFiles();
        //WriteFiles.closeTransFiles();

        if( num > 0)
        {
            //WriteFiles.uploadFiles(transactions, statements, rejects, con);
        }
        else
        {
            System.out.print("Nothing has been interfaced");
        }
//con.close();
        //   System.out.println("There was no data to upload");
    }

   

    public static double getLoanAmount(String accnum, Connection con) throws SQLException {
        double loan_amount = 0;
        if(con == null)
            con = db.connect();

        Statement stat5 = (Statement) con.createStatement();
        // System.out.println("Account number in leon accounts : " + accnum.substring(10));

        ResultSet res = stat5.executeQuery("SELECT * FROM  leon_accounts where accnum='" + accnum + "'");

        if (res.next()) {

            String la = res.getString("loan_amount");
            try {
                // if (la.matches("[0-9.]+")) {
                loan_amount = Double.parseDouble(la);
            } catch (NumberFormatException ex) {
                loan_amount = 0.0;
            }
            //}
        }

        return loan_amount;
    }

    public static void setInterfacedStatus(String id, String status, String accnum, Connection con) throws SQLException {
        PreparedStatement pst = null;
        try {
            if(con == null)
            con = db.connect();
            
            Calendar cal=Calendar.getInstance();
            java.util.Date ddd=cal.getTime();
            SimpleDateFormat sdf1= new  SimpleDateFormat("yyyy-MM-dd");
            String interfacedate=sdf1.format(ddd);
            
            String query = " UPDATE receipt SET interfaced = ?,interfacedate=? WHERE id = ? AND accnum = ?";
            pst = con.prepareStatement(query);

            pst.setString(1, status);
            pst.setString(2, interfacedate);
            pst.setString(3, id);
            pst.setString(4, accnum);
            
            pst.executeUpdate();

        } catch (SQLException ex) {
            // Logger.getLogger(VisionPlusUsingFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Error in preparing to set interface status " + ex);
        } 

    }

    public static void setReversalInterfaced(String id, String status, String accnum, Connection con) throws SQLException {
        PreparedStatement pst = null;
        try {
            if(con == null)
            con = db.connect();

            
            Calendar cal=Calendar.getInstance();
            java.util.Date ddd=cal.getTime();
            SimpleDateFormat sdf1= new  SimpleDateFormat("yyyy-MM-dd");
            String interfacedate=sdf1.format(ddd);
            String query = " UPDATE receipt SET reversal_interfaced = ?,interfacedate=? WHERE id = ? AND accnum = ?";
            pst = con.prepareStatement(query);

            pst.setString(1, "YES");
            pst.setString(2, interfacedate);
            pst.setString(3, id);
            pst.setString(4, accnum);
            
             pst.executeUpdate();
            
        } catch (SQLException ex) {
            // Logger.getLogger(VisionPlusUsingFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Error in preparing to set interface status " + ex);
        } 

    }

}
