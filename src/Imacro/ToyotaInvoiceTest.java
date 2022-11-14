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

public class ToyotaInvoiceTest {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String args[]){
	
		ToyotaInvoiceTest iid=new ToyotaInvoiceTest();
		iid.downloadInvoices();
		//}
	}
	
	
	public void downloadInvoices(){
		ActiveXComponent iim = new ActiveXComponent("imacros"); 				
		 System.out.println("Calling iimInit"); 
	     iim.invoke("iimInit"); 
	     System.out.println("Calling iimPlay");

		
		       iim.invoke("iimPlay", "CODE:TAB T=1");
		       iim.invoke("iimPlay", "CODE:TAB CLOSEALLOTHERS");
		       iim.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&locale=en_US");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1807640");
		       iim.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Msaril@2021");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
		       iim.invoke("iimPlay", "CODE:TAB T=2");
		       iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv058?NAVIGATION=MENU&modulename=srv&formName=fsrv058" );
		       iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT=22062021");
		       
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT=22062021");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
		       iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		       //iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:clsDivMain EXTRACT=TXT");
		      // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=TABLE ATTR=ID:DataTable EXTRACT=TXT");

		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");
				
				//manually cast return values to correct type
				   String iret = iim.invoke("iimGetLastExtract").toString();
				   String strNoOfRecord=iret.replace("[EXTRACT]", "");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
				   System.out.println(strNoOfRecord);
				  // int noofrecInt=0;
				   int noofrecInt = Integer.parseInt(strNoOfRecord);
				   //update insert in countlohg with 0 value count 
		       
		     for(int i=1; i<=(noofrecInt/10)+1 ; i++) {
		       
		       
		   	int k=10;
		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

		   	String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		   while(!invo.equalsIgnoreCase("#EANF#")) {

			   
			   if(k>10){
				   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(k-1)+"].selected CONTENT=NO");
			   }
			   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+k+"].selected CONTENT=YES"); 
			    
				/*
				 * int index=invoices.indexOf(invo); if(index==-1) { j++; continue; }
				 */
			    System.out.println("invo"+(k+1)+"="+invo);
			    String invFirst2Char=invo.substring(0, 2);
			    String finalchar="T";
			    if(invFirst2Char.equalsIgnoreCase("TX")) {
			    finalchar="T";
			    }else if(invFirst2Char.equalsIgnoreCase("IN")) {
			    finalchar="P";
			    }else if(invFirst2Char.equalsIgnoreCase("BS")) {
			    finalchar="W";
			    }else if(invFirst2Char.equalsIgnoreCase("SS")) {
			    finalchar="S";
			   // if(invo.substring(0, 3).equalsIgnoreCase("SSB")) {
			    //	invFirst2Char="SSB";			    
			   // }
			    }else if(invFirst2Char.equalsIgnoreCase("EW")) {
			    finalchar="E";
			    }else if(invFirst2Char.equalsIgnoreCase("AM")) {
			    finalchar="A";
			    }else if(invFirst2Char.equalsIgnoreCase("ID")) {
			    finalchar="I";
			    }else {
			    //j++;
			    //continue;
			    }
			    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:invoiceType CONTENT=%"+finalchar+"");
			    System.out.println(finalchar);
			   // DBTransactions dbt = new DBTransactions();
			   // dbt.updateSubCategory(unit, yesterday, invFirst2Char,invo);
			  // iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro8965432345 FILE="+invo+".pdf WAIT=YES");
			    //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
			  //  iim.invoke("iimPlay", "CODE:WAIT SECONDS=8");
			//update download count +1()
			    k++;
				iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

			    invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
			    
		   }
		System.out.println(i);
		   iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:Next");
		   iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
	 }
		       
		       
		  
  
	}

}
	