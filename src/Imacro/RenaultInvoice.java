package Imacro;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;

public class RenaultInvoice {
	
	public static void main(String[] args) {
		RenaultInvoice is= new RenaultInvoice();
	  Instant now = Instant.now();
		Instant yesterday = now.plus(60, ChronoUnit.DAYS);
		System.out.println(yesterday);
		//String yesterday1=imformatMonth(now.toString(),1);
	  
      is.InvoiceDownload();
}
	
public void InvoiceDownload() {
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	 System.out.println("Calling iimInit"); 
     iim.invoke("iimInit"); 
     System.out.println("Calling iimPlay"); 
     iim.invoke("iimPlay","CODE:TAB T=1" );
     iim.invoke("iimPlay","CODE:TAB CLOSEALLOTHERS" );
     iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=kh-XsqLA47xT5f1ndIq2NUvuEWauFkRUpVPZ8lHwDfSuWCS-itNpkK39PFV819vluhhZm7hEXQf3FqBfcXkNJhM7s9JWB5VeSLSgPte9tCnrbNXgJlcRqex63rTbdPvG2nSs8tAhbbbngK03s4NQCtcgBtOrrqTpDxioroHQ26wPZF-1ltmh1WWoIozZzjDW2uIMsnmmdTE_&SRN=&SWEHo=10.246.33.14&SWETS=1627962193" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
     iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">=31/07/2021 AND <=31/07/2021\"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:SERVDERB22001506" ); 
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=9 TYPE=IMG ATTR=CLASS:ToolbarButtonOn" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:s_reportNameField CONTENT=%\"GST Invoice without gate pass\"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:\"s_SS_OpenUIReportPane_1_1_20_0 \"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:\"s_SS_OpenUIReportPane_1_1_0_0 \"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =2" );
     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=IMG ATTR=CLASS:ToolbarButtonOn" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =5" );
     iim.invoke("iimPlay","CODE:ONSCRIPTERROR CONTINUE=YES" );
     iim.invoke("iimPlay","CODE:SET !USE_DOWNLOADMANAGER NO" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\REnault1 FILE=Abc1.pdf WAIT=YES" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"GST Invoice without gate pass\"" );
     iim.invoke("iimPlay","CODE: ONDIALOG POS=1 BUTTON=OK" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =15" );
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"GST Invoice without gate pass\"" );
}
public  void writeDataLineByLine(String filePath,String yesterday)
{
    File file = new File(filePath);
    try {
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
       // String[] data1 = { imformat(yesterday,6), imformat(yesterday,6), };
      // String[] data1 = { imformat(yesterday,6), imformat(yesterday,6), };
       //writer.writeNext(data1);
       
        // closing writer connection
        writer.close();
    }
    catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}


}