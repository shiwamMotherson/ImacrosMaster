package Imacro;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.jacob.activeX.ActiveXComponent;

public class FMLCTDMSInvoiceReading {
	ActiveXComponent iim = null;	
    public static void main(String args[]) {
	Instant now = Instant.now();
	for(int w =2; w<=9; w++) {
	Instant yesterday = now.minus(6, ChronoUnit.DAYS);
	System.out.println(yesterday.toString().substring(0, 10));
	FMLCTDMSInvoiceReading rt=new FMLCTDMSInvoiceReading();
	DBTransactionFML dbtr = new DBTransactionFML();
	UnitDTO ut=dbtr.getUnitConfigInvoiceReadOnly("F01","BP");
	try {
	           rt.insertInvoices(ut,yesterday.toString());
	         }catch (Exception e) {System.out.println("Portal is not responding in F01 BP category");}
	          try {
			      Thread.sleep(30000);
		          } catch (InterruptedException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
		       };
	try {
			 ut=dbtr.getUnitConfigInvoiceReadOnly("F01","GS");
		rt.insertInvoices(ut,yesterday.toString()); 
		}catch (Exception e) {System.out.println("Portal is not responding in F01 GS category");}
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F01","AS");
		rt.insertInvoicesAS(ut,yesterday.toString()); 
		}catch (Exception e) {System.out.println("Portal is not responding in F01 AS category");}
		
	try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F04","BP");
		rt.insertInvoices(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in F04 BP category");}
		

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F04","GS");
		rt.insertInvoices(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in F04 GS category");}
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F04","AS");
		rt.insertInvoicesAS(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in E02 AS category");}
		
	try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		try {	
		ut=dbtr.getUnitConfigInvoiceReadOnly("F03","BP");
		rt.insertInvoices(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in F03 BP category");}
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F03","GS");
		rt.insertInvoices(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in F03 GS category");}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		try {
			ut=dbtr.getUnitConfigInvoiceReadOnly("F03","BP");
			rt.insertInvoicesAS(ut,yesterday.toString());
			}catch (Exception e) {System.out.println("Portal is not responding in F03 AS category");}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		
		try {
		ut=dbtr.getUnitConfigInvoiceReadOnly("F02","AS");
		rt.insertInvoicesAS(ut,yesterday.toString());
		}catch (Exception e) {System.out.println("Portal is not responding in F03 AS category");}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		//FMlInvoiceMultipleDownloader iid=new FMlInvoiceMultipleDownloader();
		//iid.unitIterationDownload(yesterday.toString());
	}
}
public void insertInvoices(UnitDTO unit, String date) {

	iim = new ActiveXComponent("imacros");
	iim.invoke("iimInit");
	System.out.println("Calling iimInit");
	iim.invoke("iimPlay", "CODE:'New tab opened");
	iim.invoke("iimPlay", "CODE:TAB T=1");
	iim.invoke("iimPlay", "CODE:TAB CLOSEOTHERS");
	String URL1 = "CODE:URL GOTO=" + unit.getApplicationURL();
	iim.invoke("iimPlay", URL1);
	// iim.invoke("iimPlay", "CODE:URL
	// GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&locale=");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
	//System.out.println("login note" + urlCheck());
	String URL2 = "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=" + unit.getApplicationLoginID();
	iim.invoke("iimPlay", URL2);
	// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username
	// CONTENT=1806943");
	iim.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
	String URL3 = "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="
			+ unit.getApplicationPassword();
	iim.invoke("iimPlay", URL3);
	// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password
	// CONTENT=Msaril@2021");
	iim.invoke("iimPlay",
			"CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=25");
	iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
	iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
	// System.out.println(urlCheck());
    iim.invoke("iimPlay", "CODE:TAB T=2");
	iim.invoke("iimPlay", "CODE:URL GOTO=" + unit.getCategoryLandingURL() + "");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
	//System.out.println("insert invoices note" + urlCheck());
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
	iim.invoke("iimPlay",
			"CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT=" + imformat(date) + "");
	iim.invoke("iimPlay",
			"CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT=" + imformat(date) + "");
	iim.invoke("iimPlay",
			"CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

//manually cast return values to correct type
	String iret = iim.invoke("iimGetLastExtract").toString();
	String strNoOfRecord = iret.replace("[EXTRACT]", "");

	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
	String strRecPerPage = iretRecPerPage.replace("[EXTRACT]", "");

	System.out.println("strNoOfRecord=" + strNoOfRecord + ",strRecPerPage=" + strRecPerPage);

	int noofrecInt = Integer.parseInt(strNoOfRecord);
	int recPerPageInt = Integer.parseInt(strRecPerPage);
	int lastpagedata = 0;
	int outerIteration = 1;
	if (noofrecInt > recPerPageInt) {
		outerIteration = noofrecInt / recPerPageInt;
		lastpagedata = noofrecInt % recPerPageInt;
		if (lastpagedata > 0) {
			outerIteration++;
		} else {
			lastpagedata = recPerPageInt;
		}
	} else {
		outerIteration = 1;
		recPerPageInt = noofrecInt;
		lastpagedata = noofrecInt;
	}

	int i = 0;
	FMLDBTransactionCTDMSInvoices dbt2=new FMLDBTransactionCTDMSInvoices();
	List<String>invL2=dbt2.getInvoiceName(unit,date);
	while (i < outerIteration) {
		int j = 0;
		if (i == (outerIteration - 1)) {
			recPerPageInt = lastpagedata;
		}
		while (j < recPerPageInt) {
			if (j > 0) {
				iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iim.invoke("iimPlay",
						"CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value[" + (j - 1) + "].selected CONTENT=NO");
				iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
			}

			iim.invoke("iimPlay",
					"CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value[" + j + "].selected CONTENT=YES");
			
			iim.invoke("iimPlay",
					"CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value[" + j + "].documentNo EXTRACT=VALUE");

			String invnumber = iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
			System.out.println(invnumber + "1111111111111");
			//FMLDBTransactionCTDMSInvoices dbt2=new FMLDBTransactionCTDMSInvoices();
			//dbt.recordDateWiseInvoiceNumbers(unit, "INV", invnumber, date);
			 
			 if(invL2.contains(invnumber)) {
				 System.out.println(invnumber+"--------------Invoice already exist in database");
			 }else {
			if(unit.getCategory().equalsIgnoreCase("BP") || unit.getCategory().equalsIgnoreCase("BS") ) {
			    if(invnumber.substring(0,3).contains("TXY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BP", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BP", date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BP",0,date);
		      		   dbt2.updateUploadFileCount2("F01","BP",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F01","BP",date);
		      		       }
			    }
			    if(invnumber.substring(0,3).contains("INY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BS", "IN",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BS",date,"F01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BS",0,date);
			      		 dbt2.updateUploadFileCount2("F01","BS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","BS",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("TXD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BP", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BP",date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BP",0,date);
		      		   dbt2.updateUploadFileCount2("F04","BP",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F04","BP",date);
		      		       }
			    }
			    if(invnumber.substring(0,3).contains("IND")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BS", "IN",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BS", date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BS",0,date);
			      		 dbt2.updateUploadFileCount2("F04","BS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F04","BS",date);
			      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("TXA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BP", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BP", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BP",0,date);
		      		   dbt2.updateUploadFileCount2("F03","BP",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F03","BP",date);
		      		       }
			    }
			
			    if(invnumber.substring(0,3).contains("INA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BS", "IN",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("BS", date,"F03"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BS",0,date);
			      		 dbt2.updateUploadFileCount2("F03","BS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F03","BS",date);
			      		       }
			    }
			    
			}
			
			if(unit.getCategory().equalsIgnoreCase("BSB") || unit.getCategory().equalsIgnoreCase("SSB") || unit.getCategory().equalsIgnoreCase("EW") || unit.getCategory().equalsIgnoreCase("AMC")|| unit.getCategory().equalsIgnoreCase("GS")) {
			    if(invnumber.substring(0,3).contains("TXY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","GS", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("GS", date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","GS",0,date);
		      		   dbt2.updateUploadFileCount2("F01","GS",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F01","GS",date);
		      		       }
			    }
			
			    if(invnumber.substring(0,3).contains("EWY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","EW", "EW",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("EW", date,"F01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","EW",0,date);
			      		 dbt2.updateUploadFileCount2("F01","EW",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","EW",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("ASY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","AMC", "AM",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("AMC", date,"F01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","AMC",0,date);
			      		 dbt2.updateUploadFileCount2("F01","AMC",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","AMC",date);
			      		       }
			    }
			    
			    
			    if(invnumber.substring(0,3).contains("SSY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","SSB", "SS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("SSB", date,"E01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","SSB",0,date);
			      		 dbt2.updateUploadFileCount2("F01","SSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","SSB",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("BSY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BSB", "BS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("BSB",date,"F01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BSB",0,date);
			      		 dbt2.updateUploadFileCount2("F01","BSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","BSB",date);
			      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("TXD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","GS", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("GS", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","GS",0,date);
		      		   dbt2.updateUploadFileCount2("F04","GS",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F04","GS",date);
		      		       }
			    }
			    if(invnumber.substring(0,3).contains("EWD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","EW", "EW",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("EW", date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","EW",0,date);
			      		 dbt2.updateUploadFileCount2("F04","EW",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("E04","EW",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("ASD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","EW", "AMC",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("EW", date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","AMC",0,date);
			      		 dbt2.updateUploadFileCount2("F04","AMC",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("E04","AMC",date);
			      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("SSD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","SSB", "SS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("SSB", date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","SSB",0,date);
			      		 dbt2.updateUploadFileCount2("F04","SSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F04","SSB",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("BSD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BSB", "BS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("BSB", date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BSB",0,date);
			      		 dbt2.updateUploadFileCount2("F04","BSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F04","BSB",date);
			      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("TXA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","GS", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("GS",date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","GS",0,date);
		      		   dbt2.updateUploadFileCount2("F03","GS",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F03","GS",date);
		      		       }
			    }
			    if(invnumber.substring(0,3).contains("ASA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","AMC", "TX",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("AMC", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","AMC",0,date);
		      		   dbt2.updateUploadFileCount2("F03","AMC",date);
			    	 }else {
		      		      dbt2.updateUploadFileCount2("F03","AMC",date);
		      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("EWA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","EW", "EW",invnumber,date);
			    	 if(!(dbt2.checkCountLog1("EW", date,"F03"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","EW",0,date);
			      		 dbt2.updateUploadFileCount2("F03","EW",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F03","EW",date);
			      		       }
			    }
			    
			    if(invnumber.substring(0,3).contains("SSA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","SSB", "SS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("SSB", date,"F03"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","SSB",0,date);
			      		 dbt2.updateUploadFileCount2("F03","SSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F03","SSB",date);
			      		       }
			    }
			    if(invnumber.substring(0,3).contains("BSA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BSB", "BS",invnumber,date);
			    	if(!(dbt2.checkCountLog1("BSB",date,"F03"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BSB",0,date);
			      		 dbt2.updateUploadFileCount2("F03","BSB",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F03","BSB",date);
			      		       }
			    }
			    
			}
			 }
			j++;
			System.out.println(recPerPageInt);
		}
		// Extra invoice insertrd
		ExtraInvoiceinsert(unit, date, recPerPageInt);
		if (outerIteration > 1) {
			System.out.println("index change " + (i + 2));

			iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT=" + (i + 2) + "");
			iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo");
			iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		}
		i++;
	}

	System.out.println("All Invoices added to DB  successfully.");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	 iim.invoke("iimExit");
	// isLoginFlag=false;
}
/**
 * Method to return date in the DDMMYYYY format from the YYYY-MM-DD format
 */
public String imformat(String date) {
	String d = date.substring(8, 10) + "" + date.substring(5, 7) + "" + date.substring(0, 4);
	return d;
}

/**
 * Method to insert Invoice details into Flow_Log
 */	
public void insertInvoicesAS(UnitDTO unit,String date){
	//DBTransactionFML dbt = new DBTransactionFML();

	ActiveXComponent iim = new ActiveXComponent("imacros");
	iim.invoke("iimInit");
	System.out.println("Calling iimInit");
	iim.invoke("iimPlay", "CODE:'New tab opened");
    iim.invoke("iimPlay", "CODE:TAB T=1");
    // iim.invoke("iimPlay","CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?&locale=en_US" );
    iim.invoke("iimPlay","CODE:URL GOTO="+unit.getApplicationURL() );
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1901035" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+unit.getApplicationLoginID() );
    iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Toyota@12345" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+unit.getApplicationPassword() );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"" );
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=12");
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV" );
    iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
	iim.invoke("iimPlay", "CODE:'New tab opened");
	iim.invoke("iimPlay","CODE:TAB T=2" );
    // iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/sprt032?NAVIGATION=MENU&modulename=prt&formName=fprt032" );

     iim.invoke("iimPlay","CODE:URL GOTO="+unit.getCategoryLandingURL() );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    // System.out.println("inset count note"+urlCheck());
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT=31052021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT="+imformatx(date,3)+"" );
     System.out.println("Date"+imformatx(date,3));
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT=31052021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT="+imformatx(date,3)+"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );

 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

 //manually cast return values to correct type
 	String iret = iim.invoke("iimGetLastExtract").toString();
 	String strNoOfRecord=iret.replace("[EXTRACT]", "");
 	System.out.println(strNoOfRecord);
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

 	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
 	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");
 	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);

 	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
 	 int noofrecInt=0;
	 int recPerPageInt=0;
	 noofrecInt = Integer.parseInt(strNoOfRecord);
	// DBTransactionFML dbt = new DBTransactionFML();
		//dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
	 recPerPageInt = Integer.parseInt(strRecPerPage);
	 
		
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
	    lastpagedata=noofrecInt;
	   }
	   
	   int k=1;
	   FMLDBTransactionCTDMSInvoices dbt2=new FMLDBTransactionCTDMSInvoices();
	     // dbt.recordDateWiseInvoiceNumbers(unit, "INV",strNoOfRecord1 , date);
	 	 List<String>invL2=dbt2.getInvoiceName(unit,date);
	while(k<=outerIteration) {
	    int j=0;
	    if(k==(outerIteration-1)) {
	    recPerPageInt=lastpagedata;
	    }
	    while(j<recPerPageInt) {
	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link(0)\" EXTRACT=TXT" ); 
	      iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link("+(j)+")\" EXTRACT=TXT" ); 
	      System.out.println("J value after extracting"+(j));
	      String iret1 = iim.invoke("iimGetLastExtract").toString();
	        
	      String strNoOfRecord1=iret1.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
	      System.out.println(strNoOfRecord1.substring(0,1));
		  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		 	
			 if(invL2.contains(strNoOfRecord1)) {
				 System.out.println(strNoOfRecord1+"-------AS-------Invoice already exist in database");
			 }else {
		      if(strNoOfRecord1.substring(0,2).contains("AY")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","AS", "AY",strNoOfRecord1,date);
			    	 if(!(dbt2.checkCountLog1("AS",date,"F01"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","AS",0,date);
			      		 dbt2.updateUploadFileCount2("F01","AS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F01","AS",date);
			      		       }
			    }else  if(strNoOfRecord1.substring(0,2).contains("AD")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","AS", "AD",strNoOfRecord1,date);
			    	 if(!(dbt2.checkCountLog1("AS",date,"F04"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","AS",0,date);
			      		 dbt2.updateUploadFileCount2("F04","AS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F04","AS",date);
			      		       }
			    }else  if(strNoOfRecord1.substring(0,2).contains("AA")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","AS", "AA",strNoOfRecord1,date);
			    	 if(!(dbt2.checkCountLog1("AS",date,"F03"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","AS",0,date);
			      		 dbt2.updateUploadFileCount2("F03","AS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F03","AS",date);
			      		       }
			    }else  if(strNoOfRecord1.substring(0,2).contains("AC")) {
			    	dbt2.recordDateWiseInvoiceNumbers1("F02", "INV","AS", "AC",strNoOfRecord1,date);
			    	 if(!(dbt2.checkCountLog1("AS",date,"F02"))){
			      		   dbt2.insertDateWiseCount1("CTDMS", "F02", "INV","AS",0,date);
			      		 dbt2.updateUploadFileCount2("F02","AS",date);
			      	     }else {
			      		      dbt2.updateUploadFileCount2("F02","AS",date);
			      		       }
			    }
			 }
		      System.out.println(unit.getUnitCode());
		      j++;
	    	System.out.println("Value of j "+j);
	  }
	    k++;
	    if(outerIteration>1) {
	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
	    System.out.println("Page change successful"+(k));
	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
	   iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNoSPRT032 EXTRACT=TXT");
	   String iret100 = iim.invoke("iimGetLastExtract").toString();
       
	      String strNoOfRecord100=iret100.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
	      System.out.println(strNoOfRecord100);
		  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	   if(strNoOfRecord100.contains("null") || strNoOfRecord100.contains("NULL")) {
		 iim.invoke("iimExit");
			
	   }
	    }
	    System.out.println(k);
	   }
   System.out.println("All Invoices added to DB  successfully.");
	iim.invoke("iimExit");
	
}

/**
 * Method to return date in the DDMMYYYY format from the YYYY-MM-DD format
 */	

public String imformatx(String date, int f) {
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
 * Method to return the current Tab url for iMacros
 */
public String urlCheck() {
	String s = "";
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	iim.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
	s = iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
	return s;
}

// Extra invoice insert
public void ExtraInvoiceinsert(UnitDTO unit, String date, int recPerPageInta) {

	//ActiveXComponent iim = new ActiveXComponent("imacros");
	//iim.invoke("iimInit");
	//System.out.println("Calling iimInit");
	//iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value[" + (recPerPageInta - 1) + "].selected CONTENT=NO");
	int k = recPerPageInta;
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value[" + k + "].documentNo EXTRACT=VALUE");

	String invo = iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");

	while (!invo.equalsIgnoreCase("#EANF#")) {

		if (k > recPerPageInta) {
			iim.invoke("iimPlay",
					"CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value[" + (k - 1) + "].selected CONTENT=NO");
		}
		iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value[" + k + "].selected CONTENT=YES");

		System.out.println(invo + "1111111111111");
		DBTransactionFML dbt = new DBTransactionFML();
		//dbt.recordDateWiseInvoiceNumbers(unit, "INV", invo, date);
		 FMLDBTransactionCTDMSInvoices  dbt2=new  FMLDBTransactionCTDMSInvoices ();
	
		 List<String>invL2=dbt2.getInvoiceName(unit,date);
		 if(invL2.contains(invo)) {
			 System.out.println(invo+"--------------Invoice already exist in database");
		 }else {
		if(unit.getCategory().equalsIgnoreCase("BP") || unit.getCategory().equalsIgnoreCase("BS") ) {
		    if(invo.substring(0,3).contains("TXY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BP", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BP", date,"F01"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BP",0,date);
	      		   dbt2.updateUploadFileCount2("F01","BP",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F01","BP",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("INY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BS",date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BS",0,date);
		      		 dbt2.updateUploadFileCount2("F01","BS",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F01","BS",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("TXD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BP", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BP",date,"F04"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BP",0,date);
	      		   dbt2.updateUploadFileCount2("F04","BP",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F04","BP",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("IND")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BS", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BS",0,date);
		      		 dbt2.updateUploadFileCount2("F04","BS",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F04","BS",date);
		      		       }
		    }
		    
		    if(invo.substring(0,3).contains("TXA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BP", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BP", date,"F03"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BP",0,date);
	      		   dbt2.updateUploadFileCount2("F03","BP",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F03","BP",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("INA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("BS", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BS",0,date);
		      		 dbt2.updateUploadFileCount2("F03","BS",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F03","BS",date);
		      		       }
		    }
		    
		}
		
		if(unit.getCategory().equalsIgnoreCase("BSB") || unit.getCategory().equalsIgnoreCase("SSB") || unit.getCategory().equalsIgnoreCase("EW") || unit.getCategory().equalsIgnoreCase("GS") || unit.getCategory().equalsIgnoreCase("AMC")) {
		    if(invo.substring(0,3).contains("TXY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","GS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("GS", date,"F01"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","GS",0,date);
	      		   dbt2.updateUploadFileCount2("F01","GS",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F01","GS",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("EWY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","EW", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("EW", date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","EW",0,date);
		      		 dbt2.updateUploadFileCount2("F01","EW",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F01","EW",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("ASY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","AMC", "AM",invo,date);
		    	 if(!(dbt2.checkCountLog1("AMC", date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","AMC",0,date);
		      		 dbt2.updateUploadFileCount2("F01","AMC",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F01","AMC",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("SSY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","SSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("SSB", date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","SSB",0,date);
		      		 dbt2.updateUploadFileCount2("F01","SSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F01","SSB",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("BSY")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F01", "INV","BSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("BSB",date,"F01"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F01", "INV","BSB",0,date);
		      		 dbt2.updateUploadFileCount2("F01","BSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F01","BSB",date);
		      		       }
		    }
		    
		    if(invo.substring(0,3).contains("TXD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","GS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("GS", date,"F04"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","GS",0,date);
	      		   dbt2.updateUploadFileCount2("F04","GS",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F04","GS",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("EWD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","EW", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("EW", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","EW",0,date);
		      		 dbt2.updateUploadFileCount2("F04","EW",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F04","EW",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("ASD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","AMC", "AM",invo,date);
		    	 if(!(dbt2.checkCountLog1("AMC", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","AMC",0,date);
		      		 dbt2.updateUploadFileCount2("F04","AMC",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F04","AMC",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("SSD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","SSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("SSB", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","SSB",0,date);
		      		 dbt2.updateUploadFileCount2("F04","SSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F04","SSB",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("BSD")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F04", "INV","BSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("BSB", date,"F04"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F04", "INV","BSB",0,date);
		      		 dbt2.updateUploadFileCount2("F04","BSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F04","BSB",date);
		      		       }
		    }
		    
		    if(invo.substring(0,3).contains("TXA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","GS", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("GS",date,"F03"))){
	      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","GS",0,date);
	      		   dbt2.updateUploadFileCount2("F03","GS",date);
		    	 }else {
	      		      dbt2.updateUploadFileCount2("F03","GS",date);
	      		       }
		    }
		    if(invo.substring(0,3).contains("EWA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","EW", "TX",invo,date);
		    	 if(!(dbt2.checkCountLog1("EW", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","EW",0,date);
		      		 dbt2.updateUploadFileCount2("F03","EW",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F03","EW",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("ASA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","AMC", "AM",invo,date);
		    	 if(!(dbt2.checkCountLog1("AMC", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","AMC",0,date);
		      		 dbt2.updateUploadFileCount2("F03","AMC",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F03","AMC",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("SSA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","SSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("SSB", date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","SSB",0,date);
		      		 dbt2.updateUploadFileCount2("F03","SSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F03","SSB",date);
		      		       }
		    }
		    if(invo.substring(0,3).contains("BSA")) {
		    	dbt2.recordDateWiseInvoiceNumbers1("F03", "INV","BSB", "TX",invo,date);
		    	if(!(dbt2.checkCountLog1("BSB",date,"F03"))){
		      		   dbt2.insertDateWiseCount1("CTDMS", "F03", "INV","BSB",0,date);
		      		 dbt2.updateUploadFileCount2("F03","BSB",date);
		      	     }else {
		      		      dbt2.updateUploadFileCount2("F03","BSB",date);
		      		       }
		    }
		    
		}
		 }
		
		
		k++;
		iim.invoke("iimPlay",
				"CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value[" + k + "].documentNo EXTRACT=VALUE");

		invo = iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");

	}
}

}
