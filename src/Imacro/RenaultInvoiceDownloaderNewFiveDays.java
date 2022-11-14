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

//import sftpUpload.SFTPFileTransfer;
public class RenaultInvoiceDownloaderNewFiveDays {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String[] args) {
		RenaultInvoiceDownloaderNewFiveDays re= new RenaultInvoiceDownloaderNewFiveDays();
		Instant now = Instant.now();
		Instant yesterday = now.minus(7, ChronoUnit.DAYS);
		String yesterday1= yesterday.toString();
		System.out.println(yesterday1);
		//re.RenoInsuranceInvoiceDownload(yesterday.toString());
		//re.RenoPartsInvoiceDownload(yesterday.toString());
		//re.RenoEasyCareInvoiceDownload(yesterday.toString());
	//re.RenoConsolidateInvoiceDownload(yesterday.toString());
	//re.RenoWarrantyInvoiceDownload(yesterday.toString());
	re.unitIterationDownload(yesterday1);
	//new Thread() { public void run() { IMacroAutomailer IMA= new
	//IMacroAutomailer(); IMA.sendMail(); }; }.start();
}
	
	public void unitIterationDownload(String yesterday)
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
			List<String> invL;
			int y=0;
			if(unit.getCategory().equalsIgnoreCase("SEC")) {
			//unitWiseDownload( unit,yesterday);
				RenoEasyCareInvoiceDownload(yesterday.toString(),unit);
				while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
					RenoEasyCareInvoiceDownload(yesterday.toString(),unit);		
					y++;
				}
			}
			else if(unit.getCategory().equalsIgnoreCase("WSW")) {
				 
				RenoWarrantyInvoiceDownload(yesterday.toString(),unit);
					while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
						RenoWarrantyInvoiceDownload(yesterday.toString(),unit);		
						y++;
					}
			}
			
			else if(unit.getCategory().equalsIgnoreCase("WSP")) {
				 
				RenoPartsInvoiceDownload(yesterday.toString(),unit);
					while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
						RenoPartsInvoiceDownload(yesterday.toString(),unit);		
						y++;
					}
			}
			
			else if(unit.getCategory().equalsIgnoreCase("BSI")) {
				 
				RenoInsuranceInvoiceDownload(yesterday.toString(),unit);
					while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
						RenoInsuranceInvoiceDownload(yesterday.toString(),unit);		
						y++;
					}
			}
			
			
			else if(unit.getCategory().equalsIgnoreCase("SEW")) {
				 
				ABCExtendedWarrantyInvoiceDownload(yesterday.toString(),unit);
					while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
						ABCExtendedWarrantyInvoiceDownload(yesterday.toString(),unit);		
						y++;
					}
			}
			
			else if(unit.getCategory().equalsIgnoreCase("WSC")) {
				 
				RenoConsolidateInvoiceDownload(yesterday.toString(),unit);
					while(dbt.missingInvoiceList(unit, yesterday.toString()).size()>0 && y<3) {
						RenoConsolidateInvoiceDownload(yesterday.toString(),unit);		
						y++;
					}
			}
		  }
		  boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday.toString());
		  if(isMissing){
			  if(isLoginFlag) {
				  iim.invoke("iimExit");
					isLoginFlag=false;
					//dbt.insertExceptionLog("NA", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
					//System.out.println("iMacro closed");
			  }
			  System.out.println("is missing true");
			unitIterationDownload(yesterday);
		  }
		  else {

				dbt.insertMailBoxRenault( "NA",  "",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfigurationRenoInvoice(), yesterday, 1,"");
				//iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
				//iim.invoke("iimExit");
				//isLoginFlag=false;
				System.out.println("iMacro closed");
		  }
		}else {
		   // dbt.insertExceptionLog("NA", "", "All attempts exceeded", yesterday);
			System.out.println("unitIterationDownload attempt exceeded");
			List<UnitDTO>  unitlist=dbt.getUnitConfigurationRenoInvoice();
			  //code to fetch unit list
			  for(UnitDTO unit:unitlist){
				  List<String> invoices=dbt.missingInvoiceList(unit, yesterday);
				  if(invoices.size()>0) {
					  for(String inv:invoices) {
						  if(inv.trim().length()>0) {
							 dbt.insertExceptionLog(inv, "IMACRO", "Download failed Unit iteration exceeded by"+unitListAttempt+""+unit.getUnitCode()+" "+unit.getCategory()+" "+yesterday);
							  
							  dbt.updateSFTPUploadStatus(unit, yesterday, "F", inv);
						  }
					  }
				  }
				  
			  }
			dbt.insertMailBoxRenault( "NA",  "",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1, "Unit iteration exceeded by "+unitListAttempt+"");
		}
		File directoryPath = null;
		File directoryPathactual = null;
if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 directoryPath = new File("D:\\imacro\\"+imformat(Instant.now().toString()));
	// directoryPathactual=new File("D:\\imacro\\"+imformat(yesterday));
	 SFTPFileTransferIteration sftpFileTransfer=new SFTPFileTransferIteration();
		//sftpFileTransfer.createRootDirectory(directoryPath,imformat(Instant.now().toString()));
		sftpFileTransfer.createRootDirectory(directoryPath,imformat(yesterday));
}else{
 directoryPath = new File("D:\\imacro\\"+imformat(yesterday));
 SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
		sftpFileTransfer.createRootDirectory(directoryPath);
}	
	}
	
	
	public void RSTRenoEasyCareInvoiceDownload(String yesterday,UnitDTO ut){
		String yesterday1=yesterday.substring(0,10);
		Instant now = Instant.now();
		System.out.println("Start:"+now.toString());      
		DBTransactions dbt=new DBTransactions();
	  //  UnitDTO ut=dbt.getUnitConfig("P01", "SEC");
		System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
		System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
	    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
	    	System.out.println("renodmsInvoiceList called");
	    	RSTRenoPartsInvoiceInvoiceList(ut, yesterday1);
	    }
	    
		dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
		String filePath="D:\\RenoConsolidated.csv";
		String loc="";
		if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
		 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}else {
			 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}
		//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
		System.out.println(invL);
		now = Instant.now();
		System.out.println("Looping start:"+now.toString()); 
		for(String inv1: invL) {
			System.out.println("\nInv = "+inv1);
			String inv =inv1;
		//	writeDataLineByLine(filePath, inv, loc);
			writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
			int l=0;
			try {
				String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoConsolidatedInvoiceFinal.bat"};
				Process p = Runtime.getRuntime().exec(command);
				try {
					int exitVal = p.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			catch (Exception ex) {
				System.out.println(ex);
			}
			l=0;
			try {
				Thread.sleep(70000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
				System.out.println("chk failed "+l);
				l++;
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	
	}	
	
	public void PQRRenoEasyCareInvoiceDownload(String yesterday,UnitDTO ut){
		String yesterday1=yesterday.substring(0,10);
		Instant now = Instant.now();
		System.out.println("Start:"+now.toString());      
		DBTransactions dbt=new DBTransactions();
	  //  UnitDTO ut=dbt.getUnitConfig("P01", "SEC");
		System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
		System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
	    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
	    	System.out.println("renodmsInvoiceList called");
	    	PQRRenoPartsInvoiceInvoiceList(ut, yesterday1);
	    }
	    
		dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
		String filePath="D:\\RenoConsolidated.csv";
		String loc="";
		if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
		 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}else {
			 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}
		//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
		System.out.println(invL);
		now = Instant.now();
		System.out.println("Looping start:"+now.toString()); 
		for(String inv1: invL) {
			System.out.println("\nInv = "+inv1);
			String inv =inv1;
			//writeDataLineByLine(filePath, inv, loc);
			writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
			int l=0;
			try {
				String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoConsolidatedInvoiceFinal.bat"};
				Process p = Runtime.getRuntime().exec(command);
				try {
					int exitVal = p.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			catch (Exception ex) {
				System.out.println(ex);
			}
			l=0;
			try {
				Thread.sleep(70000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
				System.out.println("chk failed "+l);
				l++;
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	
	}	
	
	public void ABCExtendedWarrantyInvoiceDownload(String yesterday,UnitDTO ut){
		String yesterday1=yesterday.substring(0,10);
		Instant now = Instant.now();
		System.out.println("Start:"+now.toString());      
		DBTransactions dbt=new DBTransactions();
	  //  UnitDTO ut=dbt.getUnitConfig("P01", "SEC");
		System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
		System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
	    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
	    	System.out.println("renodmsInvoiceList called");
	    	ABCExtendedWarrantyInvoiceList(ut, yesterday1);
	    }
	    
		dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
		String filePath="D:\\RenoExtendedWarranty.csv";
		String loc="";
		if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
		 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}else {
			 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}
		//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
		System.out.println(invL);
		now = Instant.now();
		System.out.println("Looping start:"+now.toString()); 
		for(String inv1: invL) {
			System.out.println("\nInv = "+inv1);
			String inv =inv1;
			//writeDataLineByLine(filePath, inv, loc);
			writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
			int l=0;
			try {
				String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoExtendedWarranty1.bat"};
				Process p = Runtime.getRuntime().exec(command);
				try {
					int exitVal = p.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			catch (Exception ex) {
				System.out.println(ex);
			}
			l=0;
			try {
				Thread.sleep(70000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
				System.out.println("chk failed "+l);
				l++;
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	
	}	
	
	public void RenoEasyCareInvoiceDownload(String yesterday,UnitDTO ut){
		String yesterday1=yesterday.substring(0,10);
		Instant now = Instant.now();
		System.out.println("Start:"+now.toString());      
		DBTransactions dbt=new DBTransactions();
	  //  UnitDTO ut=dbt.getUnitConfig("P01", "SEC");
		System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
		System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
	    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
	    	System.out.println("renodmsInvoiceList called");
	    	RenoEasyCareInvoiceList(ut, yesterday1);
	    }
	    
		dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
		String filePath="D:\\RenoEasyCare.csv";
		String loc="";
		if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
		 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}else {
			 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		}
		//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
		List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
		System.out.println(invL);
		now = Instant.now();
		System.out.println("Looping start:"+now.toString()); 
		for(String inv1: invL) {
			System.out.println("\nInv = "+inv1);
			String inv =inv1;
			//writeDataLineByLine(filePath, inv, loc);
			writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
			int l=0;
			try {
				String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoEasyCare1.bat"};
				Process p = Runtime.getRuntime().exec(command);
				try {
					int exitVal = p.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			catch (Exception ex) {
				System.out.println(ex);
			}
			l=0;
			try {
				Thread.sleep(70000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
				System.out.println("chk failed "+l);
				l++;
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	
	}	
	
public void RenoWarrantyInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
	Instant now = Instant.now();
	System.out.println("Start:"+now.toString());      
	DBTransactions dbt=new DBTransactions();
   // UnitDTO ut=dbt.getUnitConfig("P01", "WSW");
	System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
	System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
    	System.out.println("renodmsInvoiceList called");
    	RenoWarrantyInvoiceList(ut, yesterday1);
    }
    
	dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
	String filePath="D:\\RenoWarranty.csv";
	String loc="";
	if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}else {
		 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}
	//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
	System.out.println(invL);
	now = Instant.now();
	System.out.println("Looping start:"+now.toString()); 
	for(String inv1: invL) {
		System.out.println("\nInv = "+inv1);
		String inv =inv1;
		//writeDataLineByLine(filePath, inv, loc);
		writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
		int l=0;
		try {
			String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoWarranty1.bat"};
			Process p = Runtime.getRuntime().exec(command);
			try {
				int exitVal = p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		l=0;
		try {
			Thread.sleep(70000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
			System.out.println("chk failed "+l);
			l++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
   
}	

public void RenoPartsInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
	Instant now = Instant.now();
	System.out.println("Start:"+now.toString());      
	DBTransactions dbt=new DBTransactions();
    //UnitDTO ut=dbt.getUnitConfig("P01", "WSP");
	System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
	System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
    	System.out.println("renodmsInvoiceList called");
    	RenoPartsInvoiceInvoiceList(ut, yesterday1);
    }
    
	dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
	String filePath="D:\\RenoPartsInvoice.csv";
	String loc="";
	if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}else {
		 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}
	//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
	System.out.println(invL);
	now = Instant.now();
	System.out.println("Looping start:"+now.toString()); 
	for(String inv1: invL) {
		System.out.println("\nInv = "+inv1);
		String inv =inv1;
		//writeDataLineByLine(filePath, inv, loc);
		writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
		int l=0;
		try {
			String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoPartsInvoice1.bat"};
			Process p = Runtime.getRuntime().exec(command);
			try {
				int exitVal = p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		l=0;
		try {
			Thread.sleep(70000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
			System.out.println("chk failed "+l);
			l++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}	



public void RenoInsuranceInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
	Instant now = Instant.now();
	System.out.println("Start:"+now.toString());      
	DBTransactions dbt=new DBTransactions();
    //UnitDTO ut=dbt.getUnitConfig("P01", "BSI");
	System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
	System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
    	System.out.println("renodmsInvoiceList called");
    	RenoInsuranceInvoiceList(ut, yesterday1);
    }
    
	dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
	String filePath="D:\\RenoInsuranceInvoice.csv";
	String loc="";
	if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}else {
		 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}
	//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
	System.out.println(invL);
	now = Instant.now();
	System.out.println("Looping start:"+now.toString()); 
	for(String inv1: invL) {
		System.out.println("\nInv = "+inv1);
		String inv =inv1;
		//writeDataLineByLine(filePath, inv, loc);
		writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
		int l=0;
		try {
			String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoInsuranceInvoice1.bat"};
			Process p = Runtime.getRuntime().exec(command);
			try {
				int exitVal = p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		l=0;
		try {
			Thread.sleep(70000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
			System.out.println("chk failed "+l);
			l++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
   
}
public void RenoConsolidateInvoiceDownload(String yesterday,UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
	Instant now = Instant.now();
	System.out.println("Start:"+now.toString());      
	DBTransactions dbt=new DBTransactions();
    //UnitDTO ut=dbt.getUnitConfig("P01", "WSC");
	System.out.println("dbt.checkCountLog(ut, yesterday1):"+dbt.checkCountLog(ut, yesterday1));    
	System.out.println("dbt.flowCountCheck(ut, yesterday1, 1, 1):"+dbt.flowCountCheck(ut, yesterday1, 1, 1));     
    if( !(dbt.checkCountLog(ut, yesterday1)) && (dbt.flowCountCheck(ut, yesterday1, 1, 1))) {
    	System.out.println("renodmsInvoiceList called");
    	dmsConsolidatedInvoiceList(ut, yesterday1);
    }
    
	dbt.delFlowLogFNGSC(ut, yesterday1, "TX");
	String filePath="D:\\RenoConsolidated.csv";
	String loc="";
	if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}else {
		 loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	}
	//String loc = "D:\\iMacro\\"+imformat(yesterday1)+"\\"+ut.getUnitCode()+"\\Invoices\\"+ut.getCategory()+"\\";
	List<String> invL = dbt.missingInvoiceList(ut, yesterday1);
	System.out.println(invL);
	now = Instant.now();
	System.out.println("Looping start:"+now.toString()); 
	for(String inv1: invL) {
		System.out.println("\nInv = "+inv1);
		String inv =inv1;
		//writeDataLineByLine(filePath, inv, loc);
		writeDataLineByLine(filePath, inv, loc,ut.getApplicationLoginID(),ut.getApplicationPassword());
		int l=0;
		try {
			String[] command = {"cmd.exe", "/C", "Start", "D:\\RenoConsolidatedInvoiceFinal.bat"};
			Process p = Runtime.getRuntime().exec(command);
			try {
				int exitVal = p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		l=0;
		try {
			Thread.sleep(70000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(	!(checkStatus(ut,yesterday1,inv)) && l<4) {
			System.out.println("chk failed "+l);
			l++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * now = Instant.now(); System.out.println("End:"+now.toString()); File
	 * directoryPath = new File("D:\\imacro\\"+imformat(yesterday1));
	 * SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
	 * sftpFileTransfer.createRootDirectory(directoryPath);
	 */
}	
int clogin=0;

public void RenoPartsInvoiceInvoiceList(UnitDTO ut, String yesterday1) {
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
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Parts Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
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
	   }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			RenoPartsInvoiceInvoiceList(ut, yesterday1);
			
		}
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
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_3_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Extended Warranty Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
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




public void dmsConsolidatedInvoiceList(UnitDTO ut, String yesterday1) {
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
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Consolidated Invoice\"" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:s_1_1_0_0_Ctrl" );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
	//No.of entries and number of pages
	
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
	   }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			dmsConsolidatedInvoiceList(ut, yesterday1);
			
		}
}


public void RenoInsuranceInvoiceList(UnitDTO ut, String yesterday1) {
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
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Insurance Invoice\"" );
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
	   }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			RenoInsuranceInvoiceList(ut, yesterday1);
			
		}

}

public void RenoWarrantyInvoiceList(UnitDTO ut, String yesterday1) {
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
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Warranty Invoice\"" );
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
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
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
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
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


public void RenoEasyCareInvoiceList(UnitDTO ut, String yesterday1) {
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
	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Invoice_Date" );
	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT==12/08/2021" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Invoice_Date CONTENT=="+imformat(yesterday1,2) );
	iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_IBM_Invoice_Type" );
	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:IBM_Invoice_Type CONTENT=\"=Easy Care\"" );
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
	   }else {
			
			System.out.println("Login failed");
			clogin++;
			System.out.println(clogin+"--------------------------clogin");
			iim.invoke("iimExit");
			RenoEasyCareInvoiceList(ut, yesterday1);
			
		}

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