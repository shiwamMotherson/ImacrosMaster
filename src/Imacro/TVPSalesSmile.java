package Imacro;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
//import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.jacob.activeX.ActiveXComponent;
//import Imacro.DBTransactions;
public class TVPSalesSmile {
	public static void main(String[] args) {
		TVPSalesSmile re= new TVPSalesSmile();
	   Instant now = Instant.now();
		 Instant yesterday = now.minus(3, ChronoUnit.DAYS);
		 String yesterday1=yesterday.toString().substring(0,10);
			System.out.println(yesterday1);
			re.partsInvoiceDownload();
		 }
	
public void partsInvoiceDownload() {
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	 System.out.println("Calling iimInit"); 
     iim.invoke("iimInit"); 
     System.out.println("Calling iimPlay"); 
     iim.invoke("iimPlay","CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?&locale=en_US" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1901035" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Msaril@2021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"" );
     iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV" );
     iim.invoke("iimPlay","CODE:TAB T=2" );
     iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv254?NAVIGATION=MENU&modulename=srv&formName=fsrv254&reqSesId=h1ZF-R7V-u6h8CQ8d2Hky2t,1901035" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:smilesPackSalesFromDate CONTENT=17/06/2021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:smilesPackSalesToDate CONTENT=17/06/2021" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:ONDIALOG POS=1 BUTTON=YES" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\TVPSales1 FILE=* WAIT=YES" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:download" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
     //call for excel read
     iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv250?NAVIGATION=MENU&modulename=srv&formName=fsrv250&reqSesId=Gf3K3058WfSlyREL6xoJIMD,1901035" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:searchFor CONTENT=%SML" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:searchParam CONTENT=SLB2100005" );
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:taxType CONTENT=% *" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:taxType CONTENT=%I" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrint" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
     iim.invoke("iimPlay","CODE:TAB T=3" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
     iim.invoke("iimPlay","CODE:TAB T=2" );

}
}

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     