package Imacro;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RenaultFiveDaysdata {
  public static void main(String[] args) {
	  int i=1;
	  int j=1;
	 while(j<=5) {
		    Instant now = Instant.now();
			Instant yesterday = now.minus(i,ChronoUnit.DAYS);
			if(i>60) {
				break;
			}
			 //Imacro iid=new Imacro();
			RenaultInvoiceDownloaderNewFiveDays re= new RenaultInvoiceDownloaderNewFiveDays();
			// try {
						// re.unitIterationDownload(yesterday.toString());
						// }catch(Exception e) {};
			boolean isMissing=re.anyInvoiceOrCountInAnyUnitPending(yesterday.toString());
			System.out.println(yesterday+" : "+isMissing);
			if(isMissing) {
			//  re.unitIterationDownload(yesterday.toString());
		     // new Thread() { 
		    	//public void run() { 
		        //  IMacroAutomailer IMA= new IMacroAutomailer(); 
		       //   IMA.sendMail(); 
		      //  }; 
	         // }.start();
	          j++;
			}
			i++;	
	 }
  }
}
