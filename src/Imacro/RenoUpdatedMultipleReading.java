package Imacro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class RenoUpdatedMultipleReading {
	// this program is designed after 
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String[] args) {
		RenoUpdatedMultipleReading re= new RenoUpdatedMultipleReading();
		RenaultIDayTimeRepeatDownloader re1= new RenaultIDayTimeRepeatDownloader();
		
		Instant now = Instant.now();
		for(int k=6;k<9;k++) {
		Instant yesterday = now.minus(k, ChronoUnit.DAYS);
		String yesterday1= yesterday.toString();
		System.out.println(yesterday1);
		File f2=new File("D:\\RENOInvoiceReportCSVFileP01");
		try {
		//deleteFolder(f2);
		}catch(Exception e) {}
		f2=new File("D:\\RENOInvoiceReportCSVFileP05");
		try {
		//deleteFolder(f2);
		}catch(Exception e) {}
		re.EnqCSVSheetRead("D:\\RENOInvoiceReportCSVFileP05\\output.csv",yesterday1);
		
		//re.EnqCSVSheetRead("D:\\RENOInvoiceReportCSVFileP01\\output.csv",yesterday1);
	}
      // re.unitIterationDownloadingP01(yesterday1);
       //re.unitIterationDownloadingP05(yesterday1);
		//re1.unitIterationDownload(yesterday1);
}
	
	public void unitIterationDownloadingP01(String yesterday)
	{
		DBTransactions dbt = new DBTransactions();
		UnitDTO unit=dbt.getUnitConfig("P01", "WSW");
		if(unit.getUnitCode().contains("P01")) {
				String filePath="D:\\RenoCredentials.csv";
				String loc="D:\\RENOInvoiceReportCSVFileP01\\";
				String inv=" ";
				writeDataLineByLine(filePath, inv, loc,unit.getApplicationLoginID(),unit.getApplicationPassword(),imformat(yesterday,2));
				System.out.println("date after using imformat function"+imformat(yesterday,2));
				int l=0;
				try {
					String[] command = {"cmd.exe", "/C", "Start", "D:\\urlcheckreno.bat"};
					//urlcheckreno is a bat file to download invoices csv file
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
			}else {}
		
		try {
			Thread.sleep(250000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unitIterationReadingP01(yesterday);
	}
		
	public void unitIterationReadingP01(String yesterday) {
		File foutput=new File("D:\\RENOInvoiceReportCSVFileP01\\output.csv");
		int k=0;
		if(!foutput.exists() && k<5) {
			unitIterationDownloadingP01(yesterday);
		}else {
			
			EnqCSVSheetRead("D:\\RENOInvoiceReportCSVFileP01\\output.csv",yesterday);
		}
		
	}
	
	public void unitIterationReadingP05(String yesterday) {
		File foutput1=new File("D:\\RENOInvoiceReportCSVFileP05\\output.csv");
		int u=0;
		if(!foutput1.exists() && u<5 ) {
			unitIterationDownloadingP05(yesterday);
		
		}else {
			EnqCSVSheetRead("D:\\RENOInvoiceReportCSVFileP05\\output.csv",yesterday);
		}
	}
	
	public void unitIterationDownloadingP05(String yesterday)
	{
		DBTransactions dbt = new DBTransactions();
		UnitDTO unit=dbt.getUnitConfig("P05", "WSC");
		if(unit.getUnitCode().contains("P05")) {
				String filePath="D:\\RenoCredentials.csv";
				String loc="D:\\RENOInvoiceReportCSVFileP05\\";
				String inv=" ";
				writeDataLineByLine(filePath, inv, loc,unit.getApplicationLoginID(),unit.getApplicationPassword(),imformat(yesterday,2));
				System.out.println("date after using imformat function"+imformat(yesterday,2));
				int l=0;
				try {
					String[] command = {"cmd.exe", "/C", "Start", "D:\\urlcheckreno.bat"};
					//urlcheckreno is a bat file to download invoices csv file
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
				
			}else {}
		
		try {
			Thread.sleep(250000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unitIterationReadingP05( yesterday);
	
	}
		 
	
	public List<String> EnqCSVSheetRead(String location1,String yesterday) {
		List<String> list= new ArrayList<String>();
		
		try {
		String location=location1;
		FileInputStream fis = new FileInputStream(new File(location));
		Path path = Paths.get(location);
		if (Files.exists(path)) {}
		else {
		//Log file not existing exception
		System.out.println("File not exist");
		}
		
		String p="";
		String dateofExcel="";
		String dateofCSV;
		String DateToday="";
		String line = "";
		String splitBy = "\t";
		String[] csvLine = null;
		//BufferedReader br = new BufferedReader(new FileReader(location));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(location), "UTF-16"));
		int lineNo=0;
		while ((line = br.readLine()) != null) //returns a Boolean value
		{
		lineNo++;
		csvLine = line.replaceAll("\"","").replaceAll("", "").split(splitBy); // use comma as separator
		if(csvLine.length>=1) {
		if(lineNo>1) {
	
		if(csvLine[0].length()>5) {
		System.out.println("lineNo="+lineNo);
		System.out.println(csvLine[0].trim());
		System.out.println("Date in CCV    "+csvLine[1].trim());
		System.out.println("Date of program    "+imformat(yesterday,2));
		dateofCSV=csvLine[1].trim();
		p=(csvLine[0].trim());
		DateToday=imformat(yesterday,2);
		dateofExcel=csvLine[1].trim().substring(0,10);
		  DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		if(p.length()>0 && (dateofExcel.equalsIgnoreCase(DateToday))) {
			 if(p.substring(0,8).contains("INSUDERB")) {//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P01", "INV","BSI", "TX",p,yesterday);
           	    dbt.updateStatus1("P01","BSI",p,yesterday,"N");
           	 if(!(dbt.checkCountLog1("BSI", yesterday,"P01"))){
        		   dbt.insertDateWiseCount1("Siebel","P01", "INV","BSI",0,yesterday);
        		  // if(totalCount<2 && totalCount!=0) { 
        			   dbt.updateUploadFileCount1("P01","BSI",yesterday);
        			// dbt.updateDownloadCount("P01","BSI", yesterday);
        			   System.out.println("There is only one file in BSI");
        		   //}
        	     }else {
        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
        		      dbt1.updateUploadFileCount1("P01","BSI",yesterday);
        		       }
			 }else if(p.substring(0,8).contains("AMCIDERB")) {
				//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P01", "INV","SEC", "TX",p,yesterday);
           	    dbt.updateStatus1("P01","SEC",p,yesterday,"N");
           	 if(!(dbt.checkCountLog1("SEC", yesterday,"P01"))){
        		   dbt.insertDateWiseCount1("Siebel","P01", "INV","SEC",0,yesterday);
        		  // if(totalCount<2 && totalCount!=0) { 
        			   dbt.updateUploadFileCount1("P01","SEC",yesterday);
        			  //dbt.updateDownloadCount("P01","SEC", yesterday);
        			   System.out.println("There is only one file in SEC");
        		   //}
        	     }else {
        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
        		      dbt1.updateUploadFileCount1("P01","SEC",yesterday);
        		       }
			 
			 }else if(p.substring(0,8).contains("EWINDERB")) {
				//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P01", "INV","SEW", "TX",p,yesterday);
           	    dbt.updateStatus1("P01","SEW",p,yesterday,"N");
           	 if(!(dbt.checkCountLog1("SEW", yesterday,"P01"))){
        		   dbt.insertDateWiseCount1("Siebel","P01", "INV","SEW",0,yesterday);
        		  // if(totalCount<2 && totalCount!=0) { 
        			   dbt.updateUploadFileCount1("P01","SEW",yesterday);
        			 //dbt.updateDownloadCount("P01","SEW", yesterday);
        			   System.out.println("There is only one file in SEW");
        		   //}
        	     }else {
        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
        		      dbt1.updateUploadFileCount1("P01","SEW",yesterday);
        		       }
			 
			 }else if(p.substring(0,8).contains("SERVDERB")) {
				//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P01", "INV","WSC", "TX",p,yesterday);
           	    dbt.updateStatus1("P01","WSC",p,yesterday,"N");
           	 if(!(dbt.checkCountLog1("WSC", yesterday,"P01"))){
        		   dbt.insertDateWiseCount1("Siebel","P01", "INV","WSC",0,yesterday);
        		  // if(totalCount<2 && totalCount!=0) { 
        			   dbt.updateUploadFileCount1("P01","WSC",yesterday);
        			//dbt.updateDownloadCount("P01","WSC", yesterday);
        			   System.out.println("There is only one file in WSC");
        		   //}
        	     }else {
        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
        		      dbt1.updateUploadFileCount1("P01","WSC",yesterday);
        		       }
			 
			 }else if(p.substring(0,8).contains("WINVDERB")) {
				//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P01", "INV","WSW", "TX",p,yesterday);
           	    dbt.updateStatus1("P01","WSW",p,yesterday,"N");
           	 if(!(dbt.checkCountLog1("WSW", yesterday,"P01"))){
        		   dbt.insertDateWiseCount1("Siebel","P01", "INV","WSW",0,yesterday);
        		  // if(totalCount<2 && totalCount!=0) { 
        			   dbt.updateUploadFileCount1("P01","WSW",yesterday);
        			 // dbt.updateDownloadCount("P01","WSW", yesterday);
        			   System.out.println("There is only one file in WSW");
        		   //}
        	     }else {
        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
        		      dbt1.updateUploadFileCount1("P01","WSW",yesterday);
        		       }
			 //p05 started
			 }else  if(p.substring(0,8).contains("INSUDERG")) {//INSUDERB
				 dbt.recordDateWiseInvoiceNumbers1("P05", "INV","BSI", "TX",p,yesterday);
	           	    dbt.updateStatus1("P05","BSI",p,yesterday,"N");
	           	 if(!(dbt.checkCountLog1("BSI", yesterday,"P05"))){
	        		   dbt.insertDateWiseCount1("Siebel","P05", "INV","BSI",0,yesterday);
	        		  // if(totalCount<2 && totalCount!=0) { 
	        			   dbt.updateUploadFileCount1("P05","BSI",yesterday);
	        			//  dbt.updateDownloadCount("P05","BSI", yesterday);
	        			   System.out.println("There is only one file in BSI");
	        		   //}
	        	     }else {
	        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
	        		      dbt1.updateUploadFileCount1("P05","BSI",yesterday);
	        		       }
				 }else if(p.substring(0,8).contains("AMCIDERG")) {
					//INSUDERB
					 dbt.recordDateWiseInvoiceNumbers1("P05", "INV","SEC", "TX",p,yesterday);
	           	    dbt.updateStatus1("P05","SEC",p,yesterday,"N");
	           	 if(!(dbt.checkCountLog1("SEC", yesterday,"P05"))){
	        		   dbt.insertDateWiseCount1("Siebel","P05", "INV","SEC",0,yesterday);
	        		  // if(totalCount<2 && totalCount!=0) { 
	        			   dbt.updateUploadFileCount1("P05","SEC",yesterday);
	        			  // dbt.updateDownloadCount("P05","SEC", yesterday);
	        			   System.out.println("There is only one file in SEC");
	        		   //}
	        	     }else {
	        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
	        		      dbt1.updateUploadFileCount1("P05","SEC",yesterday);
	        		       }
				 
				 }else if(p.substring(0,8).contains("EWINDERG")) {
					//INSUDERB
					 dbt.recordDateWiseInvoiceNumbers1("P05", "INV","SEW", "TX",p,yesterday);
	           	    dbt.updateStatus1("P05","SEW",p,yesterday,"N");
	           	 if(!(dbt.checkCountLog1("SEW", yesterday,"P05"))){
	        		   dbt.insertDateWiseCount1("Siebel","P05", "INV","SEW",0,yesterday);
	        		  // if(totalCount<2 && totalCount!=0) { 
	        			   dbt.updateUploadFileCount1("P05","SEW",yesterday);
	        			   //dbt.updateDownloadCount("P05","SEW", yesterday);
	        			   System.out.println("There is only one file in SEW");
	        		   //}
	        	     }else {
	        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
	        		      dbt1.updateUploadFileCount1("P05","SEW",yesterday);
	        		       }
				 
				 }else if(p.substring(0,8).contains("SERVDERG")) {
					//INSUDERB
					 dbt.recordDateWiseInvoiceNumbers1("P05", "INV","WSC", "TX",p,yesterday);
	           	    dbt.updateStatus1("P05","WSC",p,yesterday,"N");
	           	 if(!(dbt.checkCountLog1("WSC", yesterday,"P05"))){
	        		   dbt.insertDateWiseCount1("Siebel","P05", "INV","WSC",0,yesterday);
	        		  // if(totalCount<2 && totalCount!=0) { 
	        			   dbt.updateUploadFileCount1("P05","WSC",yesterday);
	        			   //dbt.updateDownloadCount("P05","WSC", yesterday);
	        			   System.out.println("There is only one file in WSC");
	        		   //}
	        	     }else {
	        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
	        		      dbt1.updateUploadFileCount1("P05","WSC",yesterday);
	        		       }
				 
				 }else if(p.substring(0,8).contains("WINVDERG")) {
					//INSUDERB
					 dbt.recordDateWiseInvoiceNumbers1("P05", "INV","WSW", "TX",p,yesterday);
	           	    dbt.updateStatus1("P05","WSW",p,yesterday,"N");
	           	 if(!(dbt.checkCountLog1("WSW", yesterday,"P05"))){
	        		   dbt.insertDateWiseCount1("Siebel","P05", "INV","WSW",0,yesterday);
	        		  // if(totalCount<2 && totalCount!=0) { 
	        			   dbt.updateUploadFileCount1("P05","WSW",yesterday);
	        			  //dbt.updateDownloadCount("P05","WSW", yesterday);
	        			  
	        		   //}
	        	     }else {
	        		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
	        		      dbt1.updateUploadFileCount1("P05","WSW",yesterday);
	        		       }
				 //P05 started
				 }else {}
		list.add(p);
		}
		}
		}
		}
		}
		} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		System.out.println("list are:-\n"+list);
		System.out.println("list count:-\n"+list.size());
		return list;
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
	
public static void deleteFolder(File file){
	for (File subFile : file.listFiles()) {
		if(subFile.isDirectory()) {
			deleteFolder(subFile);
		} 
		else {
			subFile.delete();
		}
     }
	file.delete();
}




	/**
	 * Method to Write csv file
	 */	
	//public  void writeDataLineByLine(String filePath,String inv, String loc)
	public  void writeDataLineByLine(String filePath,String inv, String loc, String Username ,String Password,String dateofDownload)
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	       // String[] data1 = { inv, loc}; 
	        String[] data1 = { inv, loc, Username ,Password,dateofDownload};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}
}