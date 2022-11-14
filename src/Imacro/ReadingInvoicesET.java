package Imacro;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;
import com.jacob.activeX.ActiveXComponent;
//import imacro.DBTransactions;
// This code include extra invoice download functionality ,running correctly for deployment
//login->topserve->Service GS/BP ->select generated ->date filter->search->select checkbox->invoice type->download
//for GS,BP,BS,BSB,SSB,EW
public class ReadingInvoicesET {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String args[]){
		Instant now = Instant.now();
		Instant yesterday = now.minus(1,ChronoUnit.DAYS);
		//Instant yesterday2 = now.minus(4,ChronoUnit.DAYS);
		System.out.println(yesterday.toString().substring(0,10));
		ReadingInvoicesET iid=new ReadingInvoicesET();
		//iid.unitIterationDownload(yesterday.toString());
		DBTransactions dbtET = new DBTransactions();
	    List<UnitDTO>  unitlist1=dbtET.getUnitConfiguration();
	   // for(UnitDTO unit:unitlist1) {
		//iid.insertInvoices(unit,yesterday.toString());
		//}
	    iid.unitIterationListing(yesterday.toString());
	}
	/**
	 * Method to insert Invoice details into Flow_Log
	 */	
	public void insertInvoices(UnitDTO unit,String date){
		DBTransactions dbt = new DBTransactions();
		iim = new ActiveXComponent("imacros"); 
	    iim.invoke("iimInit"); 
	    System.out.println("Calling iimInit"); 
		iim.invoke("iimPlay", "CODE:TAB T=1");
	    iim.invoke("iimPlay", "CODE:TAB CLOSEOTHERS");
	    String URL1 = "CODE:URL GOTO="+unit.getApplicationURL();
	    iim.invoke("iimPlay", URL1);
	    //iim.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&locale=");
	    iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
	    System.out.println("login note"+urlCheck());
	    String URL2="CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+unit.getApplicationLoginID();
	    iim.invoke("iimPlay", URL2);
	   // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1806943");
	    iim.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
	    String URL3="CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+unit.getApplicationPassword();
	    iim.invoke("iimPlay", URL3);
	  //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Msaril@2021");
	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"");
	    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	    iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
	    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
	    iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
	    iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
		
		iim.invoke("iimPlay", "CODE:'New tab opened");
	    iim.invoke("iimPlay", "CODE:TAB T=2");
		iim.invoke("iimPlay", "CODE:URL GOTO="+unit.getCategoryLandingURL()+"");
		iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		System.out.println("insert invoices note"+urlCheck());
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT="+imformat(date)+"");
		iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT="+imformat(date)+"");
		iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

	//manually cast return values to correct type
		String iret = iim.invoke("iimGetLastExtract").toString();
		String strNoOfRecord=iret.replace("[EXTRACT]", "");
	   
		iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

		String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
		String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");

		System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);

		int noofrecInt=Integer.parseInt(strNoOfRecord);
	    int recPerPageInt=Integer.parseInt(strRecPerPage);
		int lastpagedata=0;
		int outerIteration=1;
		if(noofrecInt>recPerPageInt) {
		outerIteration=noofrecInt/recPerPageInt;
		lastpagedata=noofrecInt%recPerPageInt;
		if(lastpagedata>0) {
		outerIteration++;
		}
		else {
		lastpagedata=recPerPageInt;
		}
		}
		else {
		outerIteration=1;
		recPerPageInt=noofrecInt;
		lastpagedata=noofrecInt;
		}

		int i=0;
		while(i<outerIteration) {
		int j=0;
		if(i==(outerIteration-1)) {
		recPerPageInt=lastpagedata;
		}
		while(j<recPerPageInt) {
		if(j>0){
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(j-1)+"].selected CONTENT=NO");
		}

		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+j+"].selected CONTENT=YES");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+j+"].documentNo EXTRACT=VALUE");

		String invnumber=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		System.out.println(invnumber+"1111111111111");
		try {
		dbt.recordDateWiseInvoiceNumbers(unit, "INV", invnumber,date);
		}catch(Exception e) {System.out.println("Invoice already Exists"+invnumber);}

		j++;
	System.out.println(recPerPageInt);
		}
		//Extra invoice insertrd
		//ExtraInvoiceinsert(unit,date,recPerPageInt);
		if(outerIteration>1) {
		System.out.println("index change "+(i+2));

		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(i+2)+"");
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo");
		}
		i++;
		}

	   System.out.println("All Invoices added to DB  successfully.");
	   //List<String> invoices=dbt.missingInvoiceList(unit, date);
		//downloadInvoices(invoices, date, unit);
		//iim.invoke("iimExit");
		//isLoginFlag=false;
	}

	
	public void unitIterationListing(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<5){//3/2
		  List<UnitDTO>  unitlist=dbt.getUnitConfiguration();
		  //code to fetch unit list
		  for(UnitDTO unit:unitlist){
			unitAttempt=0;
			String application=unit.getApplicationName();
			if(application==previousApplication && previousUnit==unit.getUnitCode()
					&& isLoginFlag==true) {
				isLoginFlag=true;
			}else if(isLoginFlag){
				iim.invoke("iimExit");
				isLoginFlag=false;				
			}
			else{
				isLoginFlag=false;
			}
			previousApplication=unit.getApplicationName();
			previousUnit=unit.getUnitCode();
			if(unit.getCategory().equalsIgnoreCase("BP") || unit.getCategory().equalsIgnoreCase("BS") || unit.getCategory().equalsIgnoreCase("GS") || unit.getCategory().equalsIgnoreCase("BSB") || unit.getCategory().equalsIgnoreCase("EW") ||  unit.getCategory().equalsIgnoreCase("SSB")) {
				insertInvoices(unit,yesterday);
			}
		
			else if(unit.getCategory().equalsIgnoreCase("AS")) {
				if(unitListAttempt==1 && unitAttempt==0) {
					PartsTest1 iid=new PartsTest1();
					insertInvoices(unit,yesterday);
				}
			}
		  }
		 }
		
	}
	

/**
 * Method to return date in the DDMMYYYY format from the YYYY-MM-DD format
 */	
	public String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}

/**
 * Method to return the current Tab url for iMacros
 */	
	public String urlCheck() {
		String s="";
		   iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		   iim.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
		   s=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		return s;
		}

}
	