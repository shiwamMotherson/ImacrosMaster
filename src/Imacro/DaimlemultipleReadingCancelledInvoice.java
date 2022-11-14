package Imacro;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;
public class DaimlemultipleReadingCancelledInvoice {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String[] args) {
		DaimlemultipleReadingCancelledInvoice re= new DaimlemultipleReadingCancelledInvoice();
		Instant now = Instant.now();
		Instant yesterday = now.minus(6, ChronoUnit.DAYS);
		String yesterday1= yesterday.toString();
		System.out.println("NOW=========="+now);
		System.out.println("yesterday======"+yesterday1);
		DBTransactions dbt = new DBTransactions();
       re.unitIterationDownload(yesterday1);
	//new Thread() { public void run() { IMacroAutomailer IMA= new
	//IMacroAutomailer(); IMA.sendMail(); }; }.start();
}
	public void unitIterationDownload(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<2){//3/2
		  List<UnitDTO>  unitlist=dbt.getUnitConfigurationDaimler2();
		  System.out.println(unitlist);
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
			List<String> invL;
			int y=0;
			int yp=0;
			 if(unit.getCategory().equalsIgnoreCase("JS")) {
				 bbzDmsJSDownload(yesterday.toString(),unit);
					
				}
			/*if(unit.getCategory().equalsIgnoreCase("SW")) {
				//bbzDmsJSDownload(yesterday.toString(),unit);
				bbzDmsSWDownload(yesterday.toString(),unit);
			}
			
				else if(unit.getCategory().equalsIgnoreCase("JS")) {
			 bbzDmsJSDownload(yesterday.toString(),unit);
				
			}
			else if(unit.getCategory().equalsIgnoreCase("PRD")) {
					bbzDmsDeadStockDownload(yesterday.toString(),unit);
			}
			else if(unit.getCategory().equalsIgnoreCase("CS")) {
					bbzDmsCSDownload(yesterday.toString(),unit);
			}*/
		  }}
		}
public void bbzDmsJSDownload(String yesterday, UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
    	dmsJSInvoiceList(ut, yesterday1);
}	
public void bbzDmsDeadStockDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
    	dmsDeadStockInvoiceList(ut, yesterday1);
}

public void bbzDmsSWDownload(String yesterday,UnitDTO ut){
    String yesterday1=yesterday.substring(0,10);
      dmsSWInvoiceList(ut, yesterday1);
}   

public void bbzDmsSPDownload(String yesterday,UnitDTO ut){
    String yesterday1=yesterday.substring(0,10);
      dmsSP1InvoiceList(ut, yesterday1);
} 
public void bbzDmsCSDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
    	dmsCS1InvoiceList(ut, yesterday1);
  
}

int clogin=0;

public void dmsJSInvoiceListCheck(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("js invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=8" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	 
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ID:s_sma21 EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if(iretCheckLogin.trim().equalsIgnoreCase("Helpdesk") && clogin<10) {
	  
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card Invoices\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_1_1_16_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	
	int strNoOfRecord0=45;
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	int j=1;
	
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
			 
		   while(!iret.equalsIgnoreCase("#EANF#") && j<45) {
			   
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    j++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	   iret = iim.invoke("iimGetLastExtract").toString();
	  	    
	  	   }

//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}else {
	
	System.out.println("Login failed");
	clogin++;
	System.out.println(clogin+"--------------------------clogin");
	iim.invoke("iimExit");
	dmsJSInvoiceList(ut, yesterday1);
	
}
	   
}


public void dmsJSInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("js invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=8" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	 
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ID:s_sma21 EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if(iretCheckLogin.trim().equalsIgnoreCase("Helpdesk") && clogin<10) {
			  
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card Invoices\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_1_1_16_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	
	int strNoOfRecord0=0;
	 if(!(dbt2.checkCountLog1("JS",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	 }
	int j=1;
	int k=1;
	int v=1;
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );//1_s_1_l_Payment_Type
		   while(!(iret.equalsIgnoreCase("#EANF#")) && j<22) {
			   iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//1_s_1_l_DICV_Insurer_Name
			   //TAG POS=1 TYPE=TD ATTR=ID:11_s_1_l_DICV_Insurer_Name EXTRACT=TXT
			   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+v+"_s_1_l_Status EXTRACT=TXT" );//invoice Status
			 //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Status EXTRACT=TXT" );
			    String InvoiceStatus=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "").trim();
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
				   System.out.println("Insurer name "+InvoiceStatus+"======"+v);
			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_DICV_Insurer_Name EXTRACT=TXT" );//extract insurer name
			    String iretInsName = iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "").trim();
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
				   System.out.println("Insurer name "+iretInsName+"======"+k);
				   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
				   if(iretInsName.equalsIgnoreCase("#EANF#") || iretInsName.equalsIgnoreCase("")) {
					   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim())) && !InvoiceStatus.contains("Cancelled")) {
	    dbt.recordDateWiseInvoiceNumbersJSPRINTBUTTONEnabled(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

					   }
	    
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    
				   }else {
					   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim())) && !InvoiceStatus.contains("Cancelled")) {
					   dbt.recordDateWiseInvoiceNumbersJSPRINTBUTTON(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
					   dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

					   }
					   if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
					    	
					    }
					    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
					    	System.out.println("NODATA is extracted"+"  "+iret);
					    }
					    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
					    	System.out.println("Null-------------------------------"+"  "+iret);
					    }
					    else {
					    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
					    }   
				   }
	    
				   dbt2.updateUploadFileCount2(ut.getUnitCode(),"JS",yesterday1);
	    j++;
	    k++;
	    v++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	   iret = iim.invoke("iimGetLastExtract").toString();
	   System.out.println("invoice number at end of the loop   "+ iret);
				  
	  	   }
		  
//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}else {
	
	System.out.println("Login failed");
	clogin++;
	System.out.println(clogin+"--------------------------clogin");
	iim.invoke("iimExit");
	dmsJSInvoiceList(ut, yesterday1);
	
}
	   
}

public void dmsJSPRINTBUTTONInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card Invoices\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_1_1_16_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:URL GOTO=https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=GotoView&SWEView=Change+Position+View&SWERF=1&SWEHo=dms.daimler-indiacv.com&SWEBU=1" );
	//iim.invoke("iimPlay","CODE:URL GOTO=https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=GotoView&SWEView=Change+Position+View&SWERF=1&SWEHo=dms.daimler-indiacv.com&SWEBU=1" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:Tools" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"User Preferences\"" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Change Position\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:5_s_1_l_Organization" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_1_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}

public void dmsCS1InvoiceList(UnitDTO ut, String yesterday1) {//working function
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit");
	
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("cs invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	//.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Retail Invoices - Parts\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_1_l_Invoice_Date EXTRACT=TXT" );
	String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	
	   if(iretCheckLogin.trim().equalsIgnoreCase("Invoice Date") ) {
	
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	 
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_12_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:siebui-icon-bttns_more" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	int strNoOfRecord0=30;
	DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	 if(!(dbt2.checkCountLog1("CS",yesterday1,ut.getUnitCode()))){
			dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
			 }
	for(int g1=1; g1<=10; g1++) {
		 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+g1+"_s_1_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
		    String iret = iim.invoke("iimGetLastExtract").toString();
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+g1 );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if((!iret.equalsIgnoreCase("#EANF#")) && (iret.substring(0,2).contains("IN")) && (!(iret.substring(0,3).contains("ROI")))) {
				   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
			   dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
			   dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

				   }
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
			    	g1=100;
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
			    	System.out.println("NODATA is extracted"+"  "+iret);
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
			    	System.out.println("Null-------------------------------"+"  "+iret);
			    }
			    else  {
				    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
				    }
			   if(g1<10) {
			    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
			   }
			  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   }
	}
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	
	int j1=10;
	int k1=10;
	
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j1+"_s_1_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   
		   System.out.println("invoice number "+iret+"j= "+j1 );
		   if(!(iret.substring(0,3).contains("ROI"))) {
		   while(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) && k1<30) {
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

			   }

	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	j1=100;
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    //j++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j1+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	   iret = iim.invoke("iimGetLastExtract").toString();
	    k1++;
	   
	  	   }}
	

		   dbt2.updateUploadFileCount2(ut.getUnitCode(),"CS",yesterday1);
//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	 }
	   else {
		   System.out.println("Login Failed......");
			iim.invoke("iimExit");
			clogin++;
			dmsCS1InvoiceList(ut,yesterday1);
			
	   }
}



/*public void dmsCS1InvoiceList(UnitDTO ut, String yesterday1) {//working function
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("cs invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	//.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Retail Invoices - Parts\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_1_l_Invoice_Date EXTRACT=TXT" );
	String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	
	   if(iretCheckLogin.trim().equalsIgnoreCase("Invoice Date") ) {
	
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	 
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_12_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:siebui-icon-bttns_more" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	int strNoOfRecord0=30;
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	int j=1;
	// iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	   // iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	  //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Invoice_Number EXTRACT=TXT" );
	    //extract invoice number TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
		   while(!iret.equalsIgnoreCase("#EANF#") && j<30) {
			   
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else if(!(iret.substring(0,3).contains("ROI"))) {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }else {}
	    //j++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	   iret = iim.invoke("iimGetLastExtract").toString();
	  	 j++;   
	  	   }

//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	 }
	   else {
		   System.out.println("Login Failed......");
			iim.invoke("iimExit");
			clogin++;
			dmsCS1InvoiceList(ut,yesterday1);
			
	   }
}*/



public void dmsSP1InvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay");
	System.out.println("sp invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_1_l_Invoice_Date EXTRACT=TXT" );
	String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if((iretCheckLogin.trim().equalsIgnoreCase("Invoice Date") || iretCheckLogin.trim().equalsIgnoreCase("#EANF#")) && clogin<10) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=">=10/08/2021 AND <=10/08/2021"
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	

	int strNoOfRecord0=30;
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	int j=1;
	// iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	   // iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	  //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Invoice_Number EXTRACT=TXT" );
	    //extract invoice number TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
		   while(!iret.equalsIgnoreCase("#EANF#") && j<30) {
			   
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	   
	  
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	j=100;
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    //j++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	   iret = iim.invoke("iimGetLastExtract").toString();
	  	 j++;   
	  	   }

//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	   }
	   else {
		   System.out.println("Login Failed......");
		   
			iim.invoke("iimExit");
			clogin++;
			dmsSP1InvoiceList(ut,yesterday1);
		
	   }
}

public void dmsDeadStockInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("deadstock invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getCategoryLandingURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_1_l_Invoice_Date EXTRACT=TXT" );
	String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");


	   if(iretCheckLogin.trim().equalsIgnoreCase("Invoice Date") ) {
	
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	 
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_12_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:siebui-icon-bttns_more" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	int strNoOfRecord0=30;
	DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	 if(!(dbt2.checkCountLog1("PRD",yesterday1,ut.getUnitCode()))){
			dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
			 }
	for(int g1=1; g1<=10; g1++) {
		 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+g1+"_s_1_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
		    String iret = iim.invoke("iimGetLastExtract").toString();
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+g1 );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if((!iret.equalsIgnoreCase("#EANF#")) && ((iret.substring(0,3).contains("ROI")))) {
				   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
			   dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
			   dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
				   }
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
			    	g1=100;
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
			    	System.out.println("NODATA is extracted"+"  "+iret);
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
			    	System.out.println("Null-------------------------------"+"  "+iret);
			    }
			    else  {
				    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
				    }
			   if(g1<10) {
			    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
			   }
			  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   }
	}
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	
	int j1=10;
	int k1=10;
	
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j1+"_s_1_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   
		   System.out.println("invoice number "+iret+"j= "+j1 );
		   if((iret.substring(0,3).contains("ROI"))) {
		   while(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) && k1<30) {
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

			   }
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	j1=100;
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    //j++;
	  
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j1+"_s_1_l_Invoice_Number EXTRACT=TXT" );
	   iret = iim.invoke("iimGetLastExtract").toString();
	    k1++;
			  
	  	   }}
	
		   dbt2.updateUploadFileCount2(ut.getUnitCode(),"PRD",yesterday1);
//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}
else {
	   System.out.println("Login Failed......");
	   clogin++;
		iim.invoke("iimExit");
		dmsSP1InvoiceList(ut,yesterday1);
		
}
}

public void dmsSWInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	System.out.println("sw invoice listing starts;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Claims" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_2_l_Claim_Type EXTRACT=TXT" );
	String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	  // if(iretCheckLogin.trim().equalsIgnoreCase("Claim Type") && clogin<10) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_12_0" );
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_9_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:siebui-icon-bttns_more" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	int strNoOfRecord0=30;
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	 if(!(dbt2.checkCountLog1("SW",yesterday1,ut.getUnitCode()))){
			dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
			 }
	for(int g=1; g<=10; g++) {
		 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+g+"_s_2_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
		    String iret = iim.invoke("iimGetLastExtract").toString();
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+g );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(!iret.equalsIgnoreCase("#EANF#")) {
				   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
			    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
			    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

				   }
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
			    	g=100;
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
			    	System.out.println("NODATA is extracted"+"  "+iret);
			    }
			    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
			    	System.out.println("Null-------------------------------"+"  "+iret);
			    }
			    else {
			    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
			    }
			   if(g<10) {
			    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
			   }
			  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   }
	}
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	
	int j=10;
	int k=10;
	// iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	   // iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	  //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Invoice_Number EXTRACT=TXT" );
	    //extract invoice number TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number EXTRACT=TXT" ); //earlier 
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_2_l_Invoice_Number EXTRACT=TXT" ); //now attribute changed from 1_s_1_l_Invoice_Number to 1_s_2_l_Invoice_Number
	    String iret = iim.invoke("iimGetLastExtract").toString();
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
		   while(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) && k<30) {
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(!(invL2.contains(iret.replace("[EXTRACT]", "").trim()))) {
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);	
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());

			   }
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	j=100;
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("")) {
	    	System.out.println("Null-------------------------------"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
	    //j++;
	    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	  //TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
	  iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
	  // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_2_l_Invoice_Number EXTRACT=TXT" );
	   iret = iim.invoke("iimGetLastExtract").toString();
	    k++;
	   
	  	   }
	
		   dbt2.updateUploadFileCount2(ut.getUnitCode(),"SW",yesterday1);

//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	 /*  }
	   else {
		   System.out.println("Login Failed......");
		  // clogin++;
			iim.invoke("iimExit");
			dmsSWInvoiceList(ut,yesterday1);
			
	}*/
}

public void dmsMCLMInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=4" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=A ATTR=TXT:Claims" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Campaign, Parts and Misc Claims\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_DICV_Invoice_Submission_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_12_0" );
	//iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Submission_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">=04/08/2021 AND <=04/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	//No.of entries and number of pages
	//no of pages loop 
	//nested loop no of entries in page loop
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_at_m_1" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	// iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Record Count [Ctrl+Shift+3]\"" );
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	// iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=TD ATTR=CLASS:scField EXTRACT=TXT" );//extract records number only 
	// iim.invoke("iimPlay","CODE:TAG POS=49 TYPE=TR ATTR=* EXTRACT=TXT" );// extract all text from records count.
	//iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	
	// String iret0 = iim.invoke("iimGetLastExtract").toString();
	//  String iret1=iret0.replace("[EXTRACT]", "");//.trim();
	//  System.out.println(iret1+"extracted");
	//  String strNoOfRecord0="0";
	//  if(iret0.length()>0) {
	//  strNoOfRecord0=iret0.replace("[EXTRACT]", "");
	String strNoOfRecord0="30";
	//}
	//System.out.println(strNoOfRecord0);//no of records extracted for eg 13 and record per page =10
	// iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	//iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	// iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
	// iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_3_1_93_0_Ctrl" );//TAG POS=1 TYPE=BUTTON ATTR=NAME:s_4_1_93_0
	//  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_4_1_93_0" );
	int noofrecInt=0;
	try {
	noofrecInt=Integer.parseInt(strNoOfRecord0);
	}catch(Exception e) {}
	
	dbt.insertDateWiseCount(ut, "INV", noofrecInt, yesterday1);
	int recPerPageInt=10;
	int lastpagedata=0;
	int outerIteration=1;
	if(noofrecInt>recPerPageInt) {
	outerIteration=noofrecInt/recPerPageInt;
	lastpagedata=noofrecInt%recPerPageInt;
	if(lastpagedata>0) {
		outerIteration++;	
	}
	
	}else {
	outerIteration=1;
	recPerPageInt=noofrecInt;
	}
	
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=A ATTR=TXT:Claims" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Campaign, Parts and Misc Claims\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_DICV_Invoice_Submission_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">=04/08/2021 AND <=04/08/2021\"" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	
	int i=0;
	while(i<outerIteration) {
	int j=0;
	while(j<recPerPageInt) {
	  if(j>=0){//
		  

	  
	  if(i>0 && j==0 && i!=outerIteration-1) {
		  j+=5;
	  }
	  else if(i>0 && j==0 && i==outerIteration-1) {
		  j+=9-lastpagedata;
	  }
	 // print invoice,enter search query goto page number i+1
	 
	    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );//"+(j+1)+"
	    //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_DICV_Invoice_Number EXTRACT=TXT" );//extract invoice number
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j+1)+"_s_1_l_DICV_Invoice_Number EXTRACT=TXT" );
	    String iret = iim.invoke("iimGetLastExtract").toString();
	   System.out.println("invoice number "+iret+" i= "+i+" j= "+j );
	   
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			
			  String iretRecPerPage3= iim.invoke("iimGetLastExtract").toString(); 
			  String strRecPerPage3=iretRecPerPage3.replace("[EXTRACT]", "");
			 System.out.println(strRecPerPage3);
			 
	    //attributes changes according to page and entries number
			/*
			 * iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j+1)+
			 * "_s_1_l_Total_Paid" ); iim.invoke(
			 * "iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Total_Paid EXTRACT=TXT");
			 * //iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
			 * iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=ID:s_1_rc EXTRACT=TXT");
			 * String iret3= iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]",
			 * "") ; System.out.println(iret3);
			 * //String iret1 = iim.invoke("iimGetLastExtract").toString();
			 * //System.out.println(iret1); iim.invoke(
			 * "iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:jqgh_s_1_l_Invoice_Number" );
			 * iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:"+iret.replace(
			 * "[EXTRACT]", "") ); iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
			 * iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_59_0" );
			 * iim.invoke(
			 * "iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"GST Service Invoice BB Customer - Place Of Supply\""
			 * ); iim.invoke("iimPlay","CODE:SET !USE_DOWNLOADMANAGER NO" );
			 * iim.invoke("iimPlay","CODE:WAIT SECONDS=10" ); // iim.invoke("iimPlay",
			 * "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(yesterday)+"\\
			 * "+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf
			 * WAIT=YES"); iim.invoke(
			 * "iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\DAIMLER\\"+imformat(yesterday1,3)+"\\
			 * JO1\\Invoices\\JCI FILE="+iret.replace("[EXTRACT]", "").trim()+".pdf
			 * WAIT=YES" );//give the file name =invoice number
			 * //while(!checkStatus(ut,yesterday1,iret.replace("[EXTRACT]", "").trim())) {
			 * iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Download" );
			 * iim.invoke("iimPlay","CODE:WAIT SECONDS=10" );
			 */
	    
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    	j=100;
	    }
	    else if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("NODATA")) {
	    	System.out.println("NODATA is extracted"+"  "+iret);
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:\"Site Map\"" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=A ATTR=TXT:Claims" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Campaign, Parts and Misc Claims\"" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
		iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_DICV_Invoice_Submission_Date" );
	
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">=04/08/2021 AND <=04/08/2021\"" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:DICV_Invoice_Submission_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_7_0" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	    for(int k=0; k<i; k++) {
	    	iim.invoke("iimPlay","CODE:WAIT SECONDS=4" );
	    	iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-end\"" );
	    	System.out.println("next page");
	    }
	  
  }	
  j++;
  }//for inner loop used for row change
i++;

}//for outer loop used for page change
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
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

/**
 * Method to check if file has been successfully downloaded
 */	
	public boolean checkStatus( UnitDTO unit, String yesterday, String invo) {
		//String location = "D:\\Daimler11\\"+imformat(yesterday,3)+"\\JO1\\Invoices\\JCI\\"+invo+".pdf";
		String location = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+"\\"+invo+".pdf";
		//String location ="D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+"\\"+invo+".pdf";
		System.out.println("called 1");
		boolean status= false;
		Path path = Paths.get(location);
		DBTransactions dbt2 = new DBTransactions();
		if (Files.exists(path)) {
			File file = new File(location);
			System.out.println(file.length());
			if(file.length()>1024) {
			System.out.println("called 2 "+location);
			dbt2.updateStatus(unit, invo, yesterday,"Y");
			status=true;
			}else {
				file.delete();
			}
			
		}
		System.out.println(status);
		return status;
	}


	/**
	 * Method to Write csv file
	 */	
	public  void writeDataLineByLine(String filePath,String inv, String loc, String Username ,String Password,String downloadDate )
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] data1 = { inv, loc, Username ,Password,downloadDate};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}


	/**
	 * Method to return date in the DDMMYYYY format from the YYYY-MM-DD format
	 */	
	public String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}
	
	public String imformat2(String date) {
		String d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
		return d;
	}
	
	
	/**
	 * Method to check if all unit's downloading and DB details are complete
	 */	
	public boolean anyInvoiceOrCountInAnyUnitPending(String date){
		DBTransactions dbt = new DBTransactions(); 
		List<UnitDTO>  unitlist=dbt.getUnitConfigurationDaimler2();
		boolean flag =false;
		for(UnitDTO unit:unitlist){
			if(anyInvoiceOrCountOfMentionUnitIsPending(unit,date)) {
				flag = true;
			}
		}
		return flag;	
	}
	
	/**
	 * Method to check if unit downloading and DB details are complete
	 */	
	public boolean anyInvoiceOrCountOfMentionUnitIsPending(UnitDTO unit,String date){
		DBTransactions dbt = new DBTransactions();
		boolean flag =false;
		if((!dbt.flowCountCheck(unit, date, unitListAttempt, unitAttempt)) || (dbt.isInvoiceMissing(unit, date, unitListAttempt, unitAttempt))) {
			flag = true;
		}
		if (flag==true && unitAttempt==5) {
			List<UnitDTO> unitlist1=new ArrayList<UnitDTO>();
			unitlist1.add(unit);
			if(!dbt.flowCountCheck(unit, date, unitListAttempt, unitAttempt)) {
				//dbt.insertMailBoxDaimler(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 2,""); 
			}
			else {
				//dbt.insertMailBoxDaimler(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 3,""); 
			}
		}
		return flag;	
	}


}
