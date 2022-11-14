package Imacro;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ETNoExecutionMailTrigger {
	public static void main(String args[]) {
		Instant now = Instant.now();
		Instant yesterday2 = now.minus(0,ChronoUnit.DAYS);
		System.out.println(yesterday2.toString().substring(0,10));
		DBTransactions dbtET = new DBTransactions();
		dbtET.ETCycleExeCheck(yesterday2.toString());
	new Thread() { public void run() { IMacroAutomailer IMA= new
IMacroAutomailer(); IMA.sendMail(); }; }.start();
System.out.println("Mail of no execution is triggered for "+yesterday2);
	}

}
