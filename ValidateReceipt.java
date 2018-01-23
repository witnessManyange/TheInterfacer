/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;

public class ValidateReceipt {
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;
    private static PreparedStatement pstmt;
    
    //Check if length is exactly equal to 19 excluding leading and trailing whitespace
    
    public static void openFiles(String rejects) throws IOException{
        if(fileWriter==null){
            fileWriter = new FileWriter(rejects);
            printWriter = new PrintWriter(fileWriter);
        }
    }
    
    public static void closeFiles() throws IOException{
        fileWriter.close();
        printWriter.close();
    }
    
    public static boolean validateAccountNumber(String accountNumber){
        return ((accountNumber.trim().length() == 19)&&(accountNumber.matches("[0-9]+")))?true:false;
    }
    
    /*Check if Date adheres to the yyyy-MM-dd format
    * 1) First catch block deals with non-null parameters
    * 2) Second catch block deals with other exceptional scenarios e.g NullPointerExceptions
    */
    
    public static boolean validateReceiptDate(String receiptDate){
        
        if(receiptDate.equals("0000-00-00"))
        {
            return false;
        }
        //Transaction Date must be after the day of operation commencement of Leons (01 August 2012)
        
        final java.time.LocalDate startOperation = java.time.LocalDate.of(2012,Month.AUGUST,01);
        java.time.LocalDate paramDate = java.time.LocalDate.parse(receiptDate);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
		dateFormat.parse(receiptDate.trim());
            } catch(ParseException parseException) {
		return false;
            } catch(Exception exception){
                return false;
            }            
            
        if(paramDate.isBefore(startOperation)){
            return false;
        } 
        
        if(receiptDate.equalsIgnoreCase("0000-00-00"))
        {
            return false;
        }
	return true;
    }
    
    public static boolean validateAmount(String amount){
        return (amount.matches("[0-9]+"))? true : false;
    }
    
    public static boolean validateReceipt(String id, String receiptNumber, String accountNumber, String amount, String receiptDate) throws SQLException, IOException{
        
        StringBuilder reason = new StringBuilder();
        int count = 0;
        if(!validateAccountNumber(accountNumber)){
            reason.append("[ Invalid Account Number ]");
            count ++;
        }
        if(!validateReceiptDate(receiptDate)){
            reason.append("[ Receipt Date Format Error or Date is before 01 June 2012 ]");
            count ++;
        }

        if((count != 0) || (reason.toString().trim().length() != 0)){
            return false;
        }else{
            return true;
        }
    }
}

