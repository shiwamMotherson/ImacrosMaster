package Imacro;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RepeatFMSSWIteration {
  public static void main(String[] args) {
	  int i=2;
	  int j=1;
	 while(j<=5) {
		    Instant now = Instant.now();
			Instant yesterday = now.minus(i,ChronoUnit.DAYS);
			if(i>6) {
				break;
			}
		 ConnectIntegration iid=new ConnectIntegration();
		boolean isMissing=iid.anyInvoiceOrCountInAnyUnitPending(yesterday.toString());
			System.out.println(yesterday);
			if(isMissing) {
			//  iid.unitIterationDownload(yesterday.toString());
		    //  new Thread() { 
		    	//public void run() { 
		         // IMacroAutomailer IMA= new IMacroAutomailer(); 
		         // IMA.sendMail(); 
		      //  }; 
	         // }.start();
	          j++;
	          System.out.println("value of  j "+j);
			}
			i++;	
			System.out.println("value of i  "+i);
	 }
  }
}
