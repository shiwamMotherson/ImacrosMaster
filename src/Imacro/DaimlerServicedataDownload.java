package Imacro;

import com.jacob.activeX.ActiveXComponent;

public class DaimlerServicedataDownload {
	public static void main(String args[]) {
		
		
	}
public void DMSreporDownloader() {
	

	ActiveXComponent iim = new ActiveXComponent("imacros");
	iim.invoke("iimInit");
	System.out.println("Calling iimInit");
	iim.invoke("iimPlay", "CODE:'New tab opened");
	iim.invoke("iimPlay","CODE:URL GOTO=https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=Login&SWECM=S&SRN=&SWEHo=dms.daimler-indiacv.com" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=D8AMIGAR" );
     iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=dicvdms@123" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=4" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" );
     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
     
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT==12/04/2022" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_at_m_1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=NAME:_sweview" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_at_m_1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Export..." );
     
     iim.invoke("iimPlay","CODE:ONSCRIPTERROR CONTINUE=YES" );
     iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=* FILE=* WAIT=YES" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Close" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
}
// Read Excel column

//automation to get number of each account

// write that excel

iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );

}
