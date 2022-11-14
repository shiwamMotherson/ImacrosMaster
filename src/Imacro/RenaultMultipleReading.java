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
public class RenaultMultipleReading {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String[] args) {
		RenaultMultipleReading re= new RenaultMultipleReading();
		Instant now = Instant.now();
		Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		String yesterday1= yesterday.toString();
		System.out.println(yesterday1);
	 re.unitIterationReading(yesterday1);
}
	
	public void unitIterationReading(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<5){//3/2
		  List<UnitDTO>  unitlist=dbt.getUnitConfigurationRenoInvoice();
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
			
			/*if(unit.getCategory().equalsIgnoreCase("SEC")) {
				RenoEasyCareInvoiceDownload(yesterday.toString(),unit);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			 if(unit.getCategory().equalsIgnoreCase("WSW")) {
				RenoWarrantyInvoiceDownload(yesterday.toString(),unit);
			}
			 try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(unit.getCategory().equalsIgnoreCase("WSP")) {
				RenoPartsInvoiceDownload(yesterday.toString(),unit);
			}
			 try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(unit.getCategory().equalsIgnoreCase("BSI")) {
				RenoInsuranceInvoiceDownload(yesterday.toString(),unit);
			}
			 try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(unit.getCategory().equalsIgnoreCase("SEW")) {
				ABCExtendedWarrantyInvoiceDownload(yesterday.toString(),unit);
			}
			 try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			 if(unit.getCategory().equalsIgnoreCase("WSC")) {
				RenoConsolidateInvoiceDownload(yesterday.toString(),unit);
			}
			
		  }
		  
		}
	}
	
	
	
	public void ABCExtendedWarrantyInvoiceDownload(String yesterday,UnitDTO ut){
		String yesterday1=yesterday.substring(0,10);//SEW
		try {
	    	ABCExtendedWarrantyInvoiceList(ut, yesterday1);
		}catch(Exception e) {}
	}	
	
	public void RenoEasyCareInvoiceDownload(String yesterday,UnitDTO ut){
		try {
	    	RenoEasyCareInvoiceList(ut, yesterday.substring(0,10));//SEC
		}catch(Exception e) {}
	}	
	
public void RenoWarrantyInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);//WSW
	try {
    	RenoWarrantyInvoiceList(ut, yesterday1);
	}catch(Exception e) {}
    
}	

public void RenoPartsInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);//WSP
try {
    	RenoPartsInvoiceInvoiceList(ut, yesterday1);
}catch(Exception e) {}
   
   
}	


public void RenoInsuranceInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);//BSI
try {
    	RenoInsuranceInvoiceList(ut, yesterday1);
}catch(Exception e) {}
}
public void RenoConsolidateInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);//WSC
	try {
dmsConsolidatedInvoiceList(ut, yesterday1);
	}catch(Exception e) {}
}	
int clogin=0;

public void RenoPartsInvoiceInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();//WSP
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );

	/*iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=CLASS:siebui-input-align-left EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");*/
	 
	 //  if(iretCheckLogin.trim().equalsIgnoreCase("Welcome Back!:") && clogin<10) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Parts Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );

	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	int strNoOfRecord0=50;
	 if(!(dbt2.checkCountLog1("WSP",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	 }
	 for(int h=1;h<3;h++) {
	for(int j=1;j<=10;j++) {
		    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
		   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
		   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
		    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
		    String iret = iim.invoke("iimGetLastExtract").toString();
		    
		 if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
		    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
		   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
		   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
				 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------WSP-------Invoice already exist in database");
			 }else {
				 
				 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
	  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
				 }
	  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
		}
		   

	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	}

	iim.invoke("iimPlay","TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next siebui-mouse-down\"" );
	dbt2.updateUploadFileCount2(ut.getUnitCode(),"WSP",yesterday1);

		    
		   }
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	 /*  }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			RenoPartsInvoiceInvoiceList(ut, yesterday1);
			
		}*/
}


public void ABCExtendedWarrantyInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Extended Warranty Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
		int strNoOfRecord0=50;
		 if(!(dbt2.checkCountLog1("SEW",yesterday1,ut.getUnitCode()))){
			 dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
		 }
		 for(int h=1;h<=10;h++) {
			for(int j=1;j<=10;j++) {
				
			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
			    String iret = iim.invoke("iimGetLastExtract").toString();
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
			    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+j );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
					 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------SEW-------Invoice already exist in database");
				 }else {
					 
					 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
		  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
					 }
		  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
			}
			   
			}
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
		dbt2.updateUploadFileCount2(ut.getUnitCode(),"SEW",yesterday1);

			    
			   }
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}




public void dmsConsolidatedInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();//WSC
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	/*iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=CLASS:siebui-input-align-left EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if(iretCheckLogin.trim().equalsIgnoreCase("Welcome Back!:") && clogin<10) {*/
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_3_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Consolidated Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_0_0" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	//No.of entries and number of pages
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	int strNoOfRecord0=50;
/*	 if(!(dbt2.checkCountLog1("WSC",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	 }
	 for(int h=1;h<=5;h++) {
			
		for(int j=1;j<=10;j++) {
			
		    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
		   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
		   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
		    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
		    String iret = iim.invoke("iimGetLastExtract").toString();
		    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
		    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   System.out.println("invoice number "+iret+"j= "+j );
		   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
		   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
				 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------WSC-------Invoice already exist in database");
			 }else {
				 
				 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
	  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
				 }
	  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
		}
		   
		}
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
	iim.invoke("iimPlay","CODE:TAG POS=R1 TYPE=SPAN ATTR=CLASS:\"Next record set\"" );
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next siebui-mouse-down\"" );
	dbt2.updateUploadFileCount2(ut.getUnitCode(),"WSC",yesterday1);

		    
		   }
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	  /* }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			dmsConsolidatedInvoiceList(ut, yesterday1);
			
		}*/
}


public void RenoInsuranceInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();//BSI
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=CLASS:siebui-input-align-left EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if(iretCheckLogin.trim().equalsIgnoreCase("Welcome Back!:") && clogin<10) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Insurance Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
		int strNoOfRecord0=50;
		 if(!(dbt2.checkCountLog1("BSI",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
		 }
			for(int j=1;j<=25;j++) {
				
			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
			    String iret = iim.invoke("iimGetLastExtract").toString();
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
			    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+j );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
					 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------BSI-------Invoice already exist in database");
				 }else {
					 
					 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
		  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
					 }
		  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
			}
			   

		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
		dbt2.updateUploadFileCount2(ut.getUnitCode(),"BSI",yesterday1);

			    
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
			RenoInsuranceInvoiceList(ut, yesterday1);
			
		}

}

public void RenoWarrantyInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();//WSW
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=CLASS:siebui-input-align-left EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	   if(iretCheckLogin.trim().equalsIgnoreCase("Welcome Back!:") && clogin<10) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Warranty Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
		int strNoOfRecord0=50;
		 if(!(dbt2.checkCountLog1("WSW",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
		 }
			for(int j=1;j<=40;j++) {
				
			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
			    String iret = iim.invoke("iimGetLastExtract").toString();
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
			    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+j );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
					 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------WSW-------Invoice already exist in database");
				 }else {
					 
					 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
		  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
					 }
		  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
			}
			   

		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
		dbt2.updateUploadFileCount2(ut.getUnitCode(),"WSW",yesterday1);

			    
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
			RenoWarrantyInvoiceList(ut, yesterday1);
			
		}

}

public void RSTRenoPartsInvoiceInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Parts Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	
	int strNoOfRecord0=30;
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	int j=1;
	
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
	    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    String iret = iim.invoke("iimGetLastExtract").toString();
	  
	    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	   System.out.println("invoice number "+iret+"j= "+j );
	   
	   while(!iret.equalsIgnoreCase("#EANF#") && j<30) {
	   
		  
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    
	    }
j++;
iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
//TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
// TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
 iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
 iret = iim.invoke("iimGetLastExtract").toString();
	    
	   }
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}


public void  PQRRenoPartsInvoiceInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Parts Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	
	int strNoOfRecord0=30;
	
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
	int j=1;
	
	    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
	   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
	    String iret = iim.invoke("iimGetLastExtract").toString();
	    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	   System.out.println("invoice number "+iret+"j= "+j );

	   DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
	     // dbt.recordDateWiseInvoiceNumbers(unit, "INV",strNoOfRecord1 , date);
	 	 List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
	   while(!(iret.equalsIgnoreCase("#EANF#")) && j<30) {
	   

			 if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
				 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------AS-------Invoice already exist in database");
			 }else {
	    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
	    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {
	    }
	    else {
	    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
	    }
j++;
iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
//TAG POS=1 TYPE=SPAN ATTR=CLASS:"ui-icon ui-icon-seek-next"
iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j)+"_s_1_l_Invoice_Number" );
// TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
 iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
 iret = iim.invoke("iimGetLastExtract").toString();
	    
	   }
}
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
}


public void RenoEasyCareInvoiceList(UnitDTO ut, String yesterday1) {
	DBTransactions dbt=new DBTransactions();//SEC
	ActiveXComponent iim = new ActiveXComponent("imacros"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 
	//System.out.println();
	iim.invoke("iimPlay","CODE:URL GOTO="+ut.getApplicationURL() );
	//iim.invoke("iimPlay","CODE:URL GOTO=http://10.246.33.14/edealeroui_enu/start.swe?SWECmd=Login&_sn=1BYu2OdCSxBVMfzznLddzEEyYhg4HFQMZnm6t6EqW9t6D0KKx30IwF.mqViPu1nvbMTPAaXdD9.BUpv.WyyrS869f5V5ct9yvm4UKJMTmy0TGSTrdb9yxl6P7Py-FQXw.v4JPpqDcthSa76BoBCvHdCHz1D-3kEeZnC0VJ5iETMTyh6B..Wqyxrvellg8VC8ljll9FHrp4I_&SRN=&SWEHo=10.246.33.14&SWETS=1629785855" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT="+ut.getApplicationLoginID() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=DERBSM" );
	iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=DERBSM0603" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=12" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=CLASS:siebui-input-align-left EXTRACT=TXT" );
	  String iretCheck= iim.invoke("iimGetLastExtract").toString(); 
	  String iretCheckLogin=iretCheck.replace("[EXTRACT]", "");
	  System.out.println(iretCheckLogin);
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	  // if(iretCheckLogin.trim().equalsIgnoreCase("Welcome Back!:")) {
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Invoices" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=7" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/4021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Easy Care\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	 DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
		int strNoOfRecord0=50;
		 if(!(dbt2.checkCountLog1("SEC",yesterday1,ut.getUnitCode()))){
	dbt.insertDateWiseCount(ut, "INV", strNoOfRecord0, yesterday1);
		 }
			for(int j=1;j<=25;j++) {
				
			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+j+"_s_1_l_Invoice_Number" );
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			   // TAG POS=1 TYPE=TD ATTR=ID:2_s_1_l_Invoice_Number EXTRACT=TXT
			    //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Number EXTRACT=TXT" );//extract invoice number
			    String iret = iim.invoke("iimGetLastExtract").toString();
			    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {break;}
			    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   System.out.println("invoice number "+iret+"j= "+j );
			   List<String>invL2=dbt2.getInvoiceName(ut,yesterday1);
			   if(invL2.contains(iret.replace("[EXTRACT]", "").trim())) {
					 System.out.println(iret.replace("[EXTRACT]", "").trim()+"-------SEC-------Invoice already exist in database");
				 }else {
					 
					 if(!(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#"))){
		  dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
					 }
		  dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
			}
			   

		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-next\"" );
		dbt2.updateUploadFileCount2(ut.getUnitCode(),"SEC",yesterday1);

			    
			   }
//for outer loop used for page change
	
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=LI ATTR=NAME:Root" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON:SUBMIT ATTR=TXT:Logout" );
	iim.invoke("iimExit");
	/*   }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			RenoEasyCareInvoiceList(ut, yesterday1);
			
		}*/

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
		String location ="D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+"\\"+invo+".pdf";
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
	//public  void writeDataLineByLine(String filePath,String inv, String loc)
	public  void writeDataLineByLine(String filePath,String inv, String loc, String Username ,String Password)
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	       // String[] data1 = { inv, loc}; 
	        String[] data1 = { inv, loc, Username ,Password};
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
				//dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 2,""); 
			}
			else {
				//dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 3,""); 
			}
		}
		return flag;	
	}


}
