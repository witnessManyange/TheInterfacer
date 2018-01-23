 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theinterfacer;

import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.newJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

//--------------------------------------------------------

public class TheScheduler {
    
   public TheScheduler() throws Exception{
//-----instantiating the scheduler       
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    JobDetail job = newJob(MyScheduling.class)
   .withIdentity("job1", "group1")
   .build(); 
    
//-----trigger   
    CronTrigger trigger = newTrigger()
    .withIdentity("trigger1", "group1")
    .withSchedule(dailyAtHourAndMinute(12,07))
    //.withSchedule(cronSchedule("0 0 0/2 * * ?"))
    .build();
//-----start scheduler
    sched.scheduleJob(job, trigger);
    sched.start();
    }
   
//----main
   public static void main(String args[]){
  try{
  new TheScheduler();
  }
  catch(Exception e){
      
  }
  }//end main
    
}


