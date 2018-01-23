/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author OpenSource
 */
public class BackDateReceipt {
   
    
    public static double getReversedInterest(double amount,String invoiceDate,String rdate,String shop) throws Exception{
double daily_interest=0.0;
double compound_rate=0.0;


if (shop.contains("Electro") || shop.contains("Enbee")){
    daily_interest=0.0029589;
    compound_rate=1.09;
    
}else{

daily_interest=0.00263013;
 compound_rate=1.08;

}
double monthly_interest;
String last=getLastDay(invoiceDate);

String first_of_posting=getFirstDay();


String [] dateparts=last.split("-");
String [] datepartsfirst=first_of_posting.split("-");
String [] datepartsreceipt=rdate.split("-");


int days_to_billing=Integer.parseInt(dateparts[2])-Integer.parseInt(datepartsreceipt[2]);




Calendar date1=new GregorianCalendar(Integer.parseInt(dateparts[0]),Integer.parseInt(dateparts[1]),Integer.parseInt(dateparts[2]));


Calendar date2=new GregorianCalendar(Integer.parseInt(datepartsfirst[0]),Integer.parseInt(datepartsfirst[1]),Integer.parseInt(datepartsfirst[2]));

double months_between=monthsBetween(date2,date1);

//System.out.println(months_between);


double daily_interest_act=days_to_billing*daily_interest;


Double first_interest_comp=amount +(amount*daily_interest_act);



Double x=first_interest_comp*Math.pow(compound_rate,months_between);

Double I=x-amount;

 return I;



}
     public static int monthsBetween(Calendar date1,Calendar date2){
    
    int monthsbetween=0;
    monthsbetween=(date1.get(Calendar.YEAR)-date2.get(Calendar.YEAR))*12;
    monthsbetween+=(date1.get(Calendar.MONTH)-date2.get(Calendar.MONTH));
    
    if(date1.get(Calendar.DAY_OF_MONTH)!=date1.getActualMaximum(Calendar.DAY_OF_MONTH)&&
            date1.get(Calendar.DAY_OF_MONTH)!=date1.getActualMaximum(Calendar.DAY_OF_MONTH)){
    
    
    monthsbetween+=((date1.get(Calendar.DAY_OF_MONTH)-date2.get(Calendar.DAY_OF_MONTH))/31d);
    }
    
    return  monthsbetween;
    }
    

    
    
public static String getLastDay(String invoiceDate) throws Exception{


 String [] dateparts=invoiceDate.split("-");
 Calendar cal=Calendar.getInstance();
 cal.set(Calendar.DATE,Integer.parseInt(dateparts[2]));
 cal.set(Calendar.MONTH,Integer.parseInt(dateparts[1]));
 cal.set(Calendar.YEAR,Integer.parseInt(dateparts[0]));
 
cal.add(Calendar.MONTH,0);
 cal.set(Calendar.DAY_OF_MONTH,1);
 cal.add(Calendar.DATE,-1);
        java.util.Date ddd=cal.getTime();
        
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
        String b=sdf1.format(ddd);
 
        return b;


}


public static String getFirstDay() throws Exception{



 Calendar cal=Calendar.getInstance();
  java.util.Date d =cal.getTime();
 
 
 cal.set(Calendar.DAY_OF_MONTH,1);

        java.util.Date ddd=cal.getTime();
        
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
        String b=sdf1.format(ddd);
        
  
        return b;


}

   
}