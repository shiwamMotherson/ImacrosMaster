package Imacro;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;

//import sftpUpload.SFTPFileTransfer;
//import sftpUpload.SFTPFileTransferDaimler;

public class DaimerSWtest {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String[] args) {
		DaimerSWtest re= new DaimerSWtest();
		Instant now = Instant.now();
		Instant yesterday = now.minus(10, ChronoUnit.DAYS);
		//re.bbzDmsJSDownload(yesterday.toString());  
		re.dmsSW1InvoiceList();
		
}
	public void dmsSW1InvoiceList() {
		DBTransactions dbt=new DBTransactions();
		ActiveXComponent iim = new ActiveXComponent("imacros"); 				
		System.out.println("Calling iimInit"); 
		iim.invoke("iimInit"); 
		System.out.println("Calling iimPlay"); 
		//System.out.println();
		iim.invoke("iimPlay","CODE:URL GOTO=https://dms.i.daimler.com/edealer_enu/start.swe?SWECmd=Login&SWEBHWND=1&SWECM=S&SRN=&SWEHo=dms.i.daimler.com&SWETS=1628570705" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=D8SHUKRA" );
		iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT="+ut.getApplicationPassword() );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=dicvdms@123" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Claims" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_12_0" );
		iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Submission_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
		//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_9_0" );
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
		String strNoOfRecord0="7";
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
		
	//	dbt.insertDateWiseCount(ut, "INV", noofrecInt, yesterday1);
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
		
		
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Claims" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_12_0" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Submission_Date" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_9_0" );
		iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		
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
		  //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Invoice_Number EXTRACT=TXT" );//extract invoice number
		    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+(j+1)+"_s_2_l_Invoice_Number EXTRACT=TXT" );
		    String iret = iim.invoke("iimGetLastExtract").toString();
		   System.out.println("invoice number "+iret+" i= "+i+" j= "+j );
		   
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
			   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
				
				  String iretRecPerPage3= iim.invoke("iimGetLastExtract").toString(); 
				  String strRecPerPage3=iretRecPerPage3.replace("[EXTRACT]", "");
				 System.out.println(strRecPerPage3+"..................");
				 
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
		    
		/*    dbt.recordDateWiseInvoiceNumbers(ut, "INV", iret.replace("[EXTRACT]", "").trim(), yesterday1);
		    if(iret.replace("[EXTRACT]", "").trim().equalsIgnoreCase("#EANF#")) {}
		    else {
		    dbt.updateSubCategory(ut, yesterday1, "TX", iret.replace("[EXTRACT]", "").trim());
		    }*/
		    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Claims" );
		    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_12_0" );
			iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_2_l_Submission_Date" );
			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">=02/08/2021 AND <=02/08/2021\"" );
			//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Submission_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
			//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=\">="+imformat(yesterday1,2)+" AND <="+imformat(yesterday1,2)+"\"");
			iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_2_1_9_0" );
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
	}