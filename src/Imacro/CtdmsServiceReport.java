package Imacro;

//import java.time.Instant;
import java.time.YearMonth;

import com.jacob.activeX.ActiveXComponent;

public class CtdmsServiceReport {
	static int i=1;
	public static void main(String[] args) {
		CtdmsServiceReport re= new CtdmsServiceReport();
	   //Instant now = Instant.now();
	   YearMonth.now().minusMonths( i ).atEndOfMonth();
	   System.out.println(YearMonth.now().minusMonths( i ).atEndOfMonth());
       re.reportDownloadE01();
}
	
public void reportDownloadE01() {
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	 System.out.println("Calling iimInit"); 
     iim.invoke("iimInit"); 
     System.out.println("Calling iimPlay"); 
     iim.invoke("iimPlay","CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?&locale=en_US" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1901035" );
     iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Tkm@12345" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"" );
     iim.invoke("iimPlay","CODE:SET !ERRORIGNORE YES" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm" );
     iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SPAN ATTR=TXT:TOPSERV" );
     iim.invoke("iimPlay","CODE:ONERRORDIALOG CONTINUE=YES" );
     iim.invoke("iimPlay","CODE:TAB T=2" );
     iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv128?NAVIGATION=MENU&modulename=srv&formName=fsrv128" );
     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:RADIO ATTR=NAME:radioType1" );
     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workShopType CONTENT=%GS" );
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:forDate1 CONTENT=16/03/2022" );
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT FORM=NAME:fmst007 ATTR=NAME:serDateTo CONTENT={{!NOW:ddmmyyyy}}" );
     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT:RADIO ATTR=NAME:radioType1" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateFromPeriod CONTENT=01072022" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateToPeriod CONTENT=07072022" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workShopType CONTENT=%GS" );
  //   iim.invoke("iimPlay","CODE:SET !TIMEOUT_STEP 100" );
     iim.invoke("iimPlay","CODE:SET !USE_DOWNLOADMANAGER NO" );
     iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\ETSERVICEDATAqqqqq FILE=ABCDE.zip WAIT=YES" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrint" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
     //iim.invoke("iimPlay","CODE:TAB T=2" );
     //iim.invoke("iimPlay","CODE:ONDIALOG POS=1 BUTTON=YES" );
     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=STRONG ATTR=TXT:LOGOFF" );
    // iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     //iim.invoke("iimPlay","CODE:TAB T=1" );
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Logout" );
    // iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    // iim.invoke("iimExit");
    
}

public String imformat(String date, int f) {
	String d="";
	if(f==1) {
		d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
	}
	else if(f==2) {
		d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
	}
	else if(f==3) {
		d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
	}else if(f==4) {
		d=date.substring(5, 7)+""+date.substring(0, 4);
	}
    
	return d;
	}

}
