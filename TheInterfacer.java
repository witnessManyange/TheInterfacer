/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theinterfacer;

/**
 *
 * @author User1
 */
public class TheInterfacer {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public void theInterfacer() throws Exception {

        System.out.println("STARTING INTERFACING");

        //CALL RECEIPTING
        System.out.println("STARTING INTERFACING RECEIPTS");
         receipts.VisionPlusUsingFile2.processReceipts();

        //CALL INVOICING
        System.out.println("STARTING INTERFACING INVOICES");
        invoices.VisionPlusInvoicingInterfacing.processInvoice();

        //CALL PLAN UPDATOR
        System.out.println("STARTING PLAN UPDATES");
        planupdater.PlanUpdater.updatePlan();

    }
}
