/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theinterfacer;

import java.sql.Timestamp;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobKey;
/**
 *
 * @author OpenSource
 */
        
public class MyScheduling  implements Job{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobKey jobKey = context.getJobDetail().getKey();
            System.out.println("SimpleJob says: " + jobKey + " executing at " + new Date());
            Date d = new Date();
            Timestamp t = new Timestamp(d.getTime());
            TheInterfacer interfacer = new TheInterfacer();
            interfacer.theInterfacer();
        } catch (Exception ex) {
            Logger.getLogger(MyScheduling.class.getName()).log(Level.SEVERE, null, ex);
        }
  
   }
    
}


