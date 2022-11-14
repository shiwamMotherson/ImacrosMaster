package xlReadWrite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.ContentDocument;
import com.sforce.soap.enterprise.sobject.ContentDocumentLink;
import com.sforce.soap.enterprise.sobject.ContentVersion;
import com.sforce.soap.enterprise.sobject.Insurance__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Upload_Details__c;
import com.sforce.ws.ConnectionException;

import logs.LogMST;

//import imacro.ConnectionUtil;

public class InsxlRW2 {
	public String St11b; 
	public String St12b; 
	public String St13b="<table style=\"border: 1px solid black; border-collapse:collapse\">"; 
	public static String n112="";
	public String Site123="";
	public String Source123="";
	public String Unit123="";
	public List<InsErrorDTO> InErrL= new ArrayList<InsErrorDTO>();
	public static void main(String []args) throws Exception {
		InsxlRW2 w = new InsxlRW2();
		//w.xlRead("Digital Lead Master Sheet Mar2021.xlsx");	
		//w.xlInsRead("trial1.xlsx");
		Calendar cal  = Calendar.getInstance();
		int y =2010;
			int m = 0;
			int d =21;
			cal.set(y,m,d);
			//System.out.println(cal.toInstant().toString().substring(8,10)+"-"+cal.toInstant().toString().substring(5,7)+"-"+cal.toInstant().toString().substring(0,4));
			/*
			 * InsErrorDTO InErrL1= new InsErrorDTO(); InErrL1.setSite__c("Def");
			 * InErrL1.setType__c("d"); InErrL1.setInsType("1"); w.InErrL.add(InErrL1);
			 * InErrL1= new InsErrorDTO(); InErrL1.setSite__c("Def2");
			 * InErrL1.setInsType("1"); w.InErrL.add(InErrL1);
			 * //System.out.println("inerrl="+w.InErrL.size()); w.CreateErrLog(w.InErrL);
			 */
	}
	
	/**
	 * Method to read xls file for Ins from the required folder
	 */	
	public String xlInsReadCall(Workbook workbookExcel, String InsType, String fName, String exPath) throws Exception {
		 String status="";
		 String status1="<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
		 String status2="";
		LogMST lm = new LogMST();
		Double Prospects=(double) 0; Double Success_Count=(double) 0; Double Error_Count=(double) 0;
		 List<InsuranceDTO> EqList1 = null;
		 //System.out.println("started"+InsType);
		 try {
			 //System.out.println("xlRead called");
			 XSSFWorkbook workbook = (XSSFWorkbook) (workbookExcel);
		      XSSFSheet spreadsheet = null;
		      String t="";
		      String imacrostatus="";
		      String recNo="";
		      int recNo1=0;
		      int IcSize =0;
		      Instant now = Instant.now();
				String now1 = now.toString().replace("T", " ");
		      for(int w=0;w<workbook.getNumberOfSheets();w++)
		      {
			      spreadsheet = workbook.getSheetAt(w);
			      EqList1 = new ArrayList<InsuranceDTO>();
			      if(InsType.equalsIgnoreCase("All")){
			    	  EqList1=AllInsSheetRead(spreadsheet);
			    	  recNo=lm.insertlogMST("INS", "Salesfroce API", "Bulk", "Common bulk upload"+fName, now1, EqList1.size(), 0, 0, "", "Pending");
			    	  recNo1=Integer.parseInt(recNo);
			    	  //System.out.println("All "+EqList1.size());
			    	  St13b=St13b+"<colgroup><col span=\"41\" style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\"> </colgroup>";
			    	  IcSize = EqList1.size();
			  		  if(IcSize<1) {}
			  		  else {
			  		  int chunkSize =200;
			  		  int noOfChunk=(int) IcSize/chunkSize;
			  		  int lastChunkSize=IcSize%chunkSize;
			  		  List<InsuranceDTO> Ic21 = null;
			  		  for(int i1 =0; i1<=noOfChunk; i1++) {
			  			Ic21 = null;
			  			if(i1<noOfChunk) {
			  				Ic21 = EqList1.subList(i1*chunkSize, (i1+1)*chunkSize);
			  			}
			  			else {
			  				if(lastChunkSize>0) {
			  					Ic21 = EqList1.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
			  				}
			  			}
			  		  if(Ic21!=null && Ic21.size()!=0) {
			  			  //System.out.println("Ic21 size="+Ic21.size());
			  			sfUpload(Ic21,1, recNo1);
				    	t=""+(Integer.parseInt("0"+St11b.split("##$##")[0])+Integer.parseInt("0"+t.split("##$##")[0]));
			  		  }
			  		  }			    	  
			  		  }
			  		  
			    	  if( EqList1.size()>0 && ((EqList1.size())==Integer.parseInt(t.split("##$##")[0])) ) {
			    		  imacrostatus="Success";
			    	  }
			    	  else {
			    		  imacrostatus="Fail";
			    	  }
			    	  lm.updatelogMST("INS", "Salesfroce API", "Bulk", "Common bulk upload"+fName, now1, EqList1.size(), Integer.parseInt(t.split("##$##")[0]), (EqList1.size())-Integer.parseInt(t.split("##$##")[0]), "", imacrostatus);
			    	  if(EqList1.size()==0) {
			    		  lm.insertLogDetails(recNo1, "Common bulk upload"+fName, "No Records detected");
			    	  }
			    	  if(EqList1.size()>0) {
			    		  Prospects=Prospects+EqList1.size();  
			    		  Success_Count=Success_Count+Integer.parseInt(t.split("##$##")[0]);  
			    		  Error_Count=Error_Count+(EqList1.size())-Integer.parseInt(t.split("##$##")[0]);
			    		  status=status+status1+"<td>"+EqList1.size()+"</td><td>"+t.split("##$##")[0]+"</td></tr>";
			    		  status2=status2+St12b+"</br>";
			    	  }
			    	  ////System.out.println("1t="+t+"\nstatus="+status+"\nstatus2="+status2);
			      }
			      if(InsType.equalsIgnoreCase("Audi Delhi")){
			    	  EqList1=AudiDelInsSheetRead(spreadsheet);
			    	  recNo=lm.insertlogMST("INS", "Salesfroce API", "Bulk", "Audi Delhi bulk upload", now1, EqList1.size(), 0, 0, "", "Pending");
			    	  recNo1=Integer.parseInt(recNo);			    	  
			    	  //System.out.println("All"+EqList1.size());
			    	  St13b=St13b+"<colgroup><col span=\"33\" style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\"> </colgroup>";
			    	  IcSize = EqList1.size();
			  		  if(IcSize<1) {}
			  		  else {
			  		  int chunkSize =200;
			  		  int noOfChunk=(int) IcSize/chunkSize;
			  		  int lastChunkSize=IcSize%chunkSize;
			  		  List<InsuranceDTO> Ic21 = null;
			  		  for(int i1 =0; i1<=noOfChunk; i1++) {
			  			Ic21 = null;
			  			if(i1<noOfChunk) {
			  				Ic21 = EqList1.subList(i1*chunkSize, (i1+1)*chunkSize);
			  			}
			  			else {
			  				if(lastChunkSize>0) {
			  					Ic21 = EqList1.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
			  				}
			  			}
			  		  if(Ic21!=null && Ic21.size()!=0) {
			  			sfUpload(EqList1,2, recNo1);
				    	t=""+(Integer.parseInt("0"+St11b.split("##$##")[0])+Integer.parseInt("0"+t.split("##$##")[0]));
			  		  }
			  		  }
			  		  }
			    	  if( EqList1.size()>0 && ((EqList1.size())==Integer.parseInt(t.split("##$##")[0])) ) {
			    		  imacrostatus="Success";
			    	  }
			    	  else {
			    		  imacrostatus="Fail";
			    	  }
			    	  lm.updatelogMST("INS", "Salesfroce API", "Bulk", "Audi Delhi bulk upload", now1, EqList1.size(), Integer.parseInt(t.split("##$##")[0]), (EqList1.size())-Integer.parseInt(t.split("##$##")[0]), "", imacrostatus);
			    	  if(EqList1.size()==0) {
			    		  lm.insertLogDetails(recNo1, "Audi Delhi bulk upload", "No Records detected");
			    	  }
			    	  
			    	  if(EqList1.size()>0) {
			    		  Prospects=Prospects+EqList1.size();  
			    		  Success_Count=Success_Count+Integer.parseInt(t.split("##$##")[0]);  
			    		  Error_Count=Error_Count+(EqList1.size())-Integer.parseInt(t.split("##$##")[0]);
			    	  status=status+status1+"<td>"+EqList1.size()+"</td><td>"+t.split("##$##")[0]+"</td></tr>";
			    	  status2=status2+St12b+"</br>";
			    	  }
			    	  ////System.out.println("1t="+t+"\nstatus="+status+"\nstatus2="+status2);
			      }
			      if(InsType.equalsIgnoreCase("Audi Kolkata")){
			    	  EqList1=AudiKolInsSheetRead(spreadsheet);
			    	  //System.out.println("All"+EqList1.size());
			    	  recNo=lm.insertlogMST("INS", "Salesfroce API", "Bulk", "Audi Kolkata bulk upload", now1, EqList1.size(), 0, 0, "", "Pending");
			    	  recNo1=Integer.parseInt(recNo);			    	  
			    	 
			    	  St13b=St13b+"<colgroup><col span=\"22\" style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\"> </colgroup>";
			    	  IcSize = EqList1.size();
			  		  if(IcSize<1) {}
			  		  else {
			  		  int chunkSize =200;
			  		  int noOfChunk=(int) IcSize/chunkSize;
			  		  int lastChunkSize=IcSize%chunkSize;
			  		  List<InsuranceDTO> Ic21 = null;
			  		  for(int i1 =0; i1<=noOfChunk; i1++) {
			  			Ic21 = null;
			  			if(i1<noOfChunk) {
			  				Ic21 = EqList1.subList(i1*chunkSize, (i1+1)*chunkSize);
			  			}
			  			else {
			  				if(lastChunkSize>0) {
			  					Ic21 = EqList1.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
			  				}
			  			}
			  		  if(Ic21!=null && Ic21.size()!=0) {
			  			sfUpload(EqList1,3, recNo1);
				    	t=""+(Integer.parseInt("0"+St11b.split("##$##")[0])+Integer.parseInt("0"+t.split("##$##")[0]));
			  		  }
			  		  }
			  		  }
			    	  if( EqList1.size()>0 && ((EqList1.size())==Integer.parseInt(t.split("##$##")[0])) ) {
			    		  imacrostatus="Success";
			    	  }
			    	  else {
			    		  imacrostatus="Fail";
			    	  }
			    	  lm.updatelogMST("INS", "Salesfroce API", "Bulk", "Audi Kolkata bulk upload", now1, EqList1.size(), Integer.parseInt(t.split("##$##")[0]), (EqList1.size())-Integer.parseInt(t.split("##$##")[0]), "", imacrostatus);
			    	  if(EqList1.size()==0) {
			    		  lm.insertLogDetails(recNo1, "Audi Kolkata bulk upload", "No Records detected");
			    	  }
			    	  
			    	  if(EqList1.size()>0) {
			    		  Prospects=Prospects+EqList1.size();  
			    		  Success_Count=Success_Count+Integer.parseInt(t.split("##$##")[0]);  
			    		  Error_Count=Error_Count+(EqList1.size())-Integer.parseInt(t.split("##$##")[0]);
			    	  status=status+status1+"<td>"+EqList1.size()+"</td><td>"+t.split("##$##")[0]+"</td></tr>";
			    	  status2=status2+St12b+"</br>";
			    	  }
			    	  ////System.out.println("1t="+t+"\nstatus="+status+"\nstatus2="+status2);
			      }
		    	  ////System.out.println("4t="+t+"\nstatus="+status+"\nstatus2="+status2);
		      }
		      //fis.close();
		     }
		 catch(Exception e) {
			 //Log excpetion for Cell iteration encountered error
			 //System.out.println(e);
			 ////insertExceptionLog("NA","xlToSalesForce","Cell iteration encountered error for excel on date - "+Mdate);
		 }
		 ////System.out.println("\n"+EqList.size());
		 //sfUpload(EqList);
		 ////System.out.println("5status="+status+"\nstatus2="+status2);		
		 ////System.out.println(status2);
		 if(status2==null || status2=="")
		 {
			 status2="No Errors";
		 }
		 //System.out.println("inerrl="+InErrL.size());
		 try {
		 CreateErrLog(InErrL, exPath);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 try {
			 FileInputStream fout1=new FileInputStream(getLivePath()+"\\BulkUploadTemplateInsAll2.xlsx");
			 if(InsType.equalsIgnoreCase("Audi Delhi")) {
				fout1=new FileInputStream(getLivePath()+"BulkUploadTemplateInsAD2.xlsx");
			}
			else if(InsType.equalsIgnoreCase("Audi Kolkata")) {
				fout1=new FileInputStream(getLivePath()+"\\BulkUploadTemplateInsAK2.xlsx");
			}
			byte [] outArray = fout1.readAllBytes();
			System.out.println("fName"+fName);
			setUpload_Details__c(Site123, Unit123, Source123, Prospects, Success_Count, Error_Count, "Insurance Manager", fName.replace(".xlsx", ""),fName, fName.replace(".xlsx", ""), outArray);
				 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return status+"##1##"+status2+"##1##"+St13b;
	}

	/**
	 * Method to read data from All Insurance sheet
	 */	
	public List <InsuranceDTO> AllInsSheetRead(XSSFSheet spreadsheet) {
		List <InsuranceDTO> Ins=new ArrayList<InsuranceDTO>();
		Iterator < Row >  rowIterator = spreadsheet.iterator();
		InsuranceDTO In = null;
	    XSSFRow row;
	    String p="";
	    int r111=1;
	    while (rowIterator.hasNext()) {
	    	In = new InsuranceDTO();
	    	row = (XSSFRow) rowIterator.next();
	    	try {
	    		if(row.getRowNum()==0) {
	    			if(cellReturn(row.getCell(0)).trim().equalsIgnoreCase("Site")) {
	    				r111=0;
	    			}
	    		}
	    	if(row.getRowNum()>r111) {
	    		////System.out.println(cellReturn(row.getCell(18)));
	    		In.setSite__c(cellReturn(row.getCell(0)));
	    		In.setType__c(cellReturn(row.getCell(1)));
	    		In.setName(cellReturn(row.getCell(2)));
	    		In.setCustomer_Type__c(cellReturn(row.getCell(3)));
	    		In.setCompany_Name__c(cellReturn(row.getCell(4)));
	    		In.setEmail__c(cellReturn(row.getCell(5)));
	    		In.setPhone__c(cellReturn(row.getCell(6)));
	    		In.setAlternate_Phone_No__c(cellReturn(row.getCell(7)));
	    		In.setLandline__c(cellReturn(row.getCell(8)));
	    		In.setAddress__c(cellReturn(row.getCell(9)));
	    		if(cellReturn(row.getCell(10)).length()>0) {
		    		In.setCity__c(cellReturn(row.getCell(10)));
	    		}
	    		if(cellReturn(row.getCell(11)).length()>0) {
		    		In.setState__c(cellReturn(row.getCell(11)));
	    		}
	    		if(cellReturn(row.getCell(12)).length()>0) {
		    		In.setPinCode__c(cellReturn(row.getCell(12)));
	    		}
	    		if(cellReturn(row.getCell(13)).length()>0) {
		    		In.setMake__c(cellReturn(row.getCell(13)));
	    		}
	    		if(cellReturn(row.getCell(14)).length()>0) {
		    		In.setModel1__c(cellReturn(row.getCell(14)));
	    		}
	    		if(cellReturn(row.getCell(15)).length()>3){
	    			if(cellReturn(row.getCell(15)).length()>4){
	    				In.setModel_Year__c(cellReturn(row.getCell(15)).substring(cellReturn(row.getCell(15)).length()-4));
	    			}else {
	    				In.setModel_Year__c(cellReturn(row.getCell(15)));
	    			}
	    		}
	    		if(cellReturn(row.getCell(16)).length()>0) {
		    		In.setVarient__c(cellReturn(row.getCell(16)));
	    		}
	    		if(cellReturn(row.getCell(17)).length()>0) {
		    		In.setReg_No__c(cellReturn(row.getCell(17)));
	    		}
	    		if(cellReturn(row.getCell(18)).length()>0) {
		    		In.setChassis_No__c(cellReturn(row.getCell(18)));
	    		}
	    		if(cellReturn(row.getCell(19)).length()>0) {
		    		In.setEngine_No__c(cellReturn(row.getCell(19)));
	    		}
	    		if(cellReturn(row.getCell(20)).length()>0) {
		    		In.setFuel_Type__c(cellReturn(row.getCell(20)));
	    		}
	    		int format=0;
	    		if(cellReturn(row.getCell(21)).length()==10) {
	    			format=3;
	    		}
	    		else if(cellReturn(row.getCell(21)).length()==11) {
	    			format=2;
	    		}
	    		if(cellReturn(row.getCell(21)).length()>0) {
		    		In.setSold_Date__c(cellReturn(row.getCell(21)).replaceAll("/", "-"), format);
	    		}
	    		if(cellReturn(row.getCell(22)).length()>0) {
		    		In.setInsurer__c(getInsurer(cellReturn(row.getCell(22))));
	    		}
	    		if(cellReturn(row.getCell(23)).length()>0) {
		    		In.setProposal_No__c(cellReturn(row.getCell(23)));
	    		}
	    		if(cellReturn(row.getCell(24)).length()>0) {
		    		In.setPolicy_Number__c(cellReturn(row.getCell(24)));
	    		}
	    		if(cellReturn(row.getCell(25)).length()>0) {
		    		In.setCLAIM_STATUS_c(cellReturn(row.getCell(25)));
	    		}
	    		p=cellReturn(row.getCell(27));
	    		if(p.length()==10) {
	    			format=3;
	    		}
	    		else if(p.length()==11) {
	    			format=1;
	    		}
	    		if(p.length()>0) {
		    		In.setInsurance_Renewal_Date__c(p.replaceAll("/", "-"), format);
	    		}
	    		else if(cellReturn(row.getCell(26)).length()>8) {
	    			In.setInsurance_Renewal_Date__c(cellReturn(row.getCell(26)), 4);
	    		}
	    		else if(cellReturn(row.getCell(21)).length()>8) {
	    			In.setInsurance_Renewal_Date__c(cellReturn(row.getCell(21)), 4);
	    		}
	    		

	    		p=cellReturn(row.getCell(27));
	    		if(cellReturn(row.getCell(26)).length()==10) {
	    			format=2;
	    		}
	    		else if(cellReturn(row.getCell(26)).length()==11) {
	    			format=1;
	    		}
	    		if(cellReturn(row.getCell(26)).length()==10) {
		    		In.setPolicy_Issued_On__c(cellReturn(row.getCell(26)), format);
	    		}
	    		else if(p.length()>0) {
	    			In.setPolicy_Issued_On__c(p.replaceAll("/", "-"),5);
	    		}
	    		if(cellReturn(row.getCell(28)).length()>0) {
	    			In.setIDV__c(Double.parseDouble(cellReturn(row.getCell(28))));
	    		}
	    		if(cellReturn(row.getCell(29)).length()>0) {
		    		In.setOwn_Damage__c(cellReturn(row.getCell(29)));
	    		}
	    		else {
	    			In.setOwn_Damage__c("0");
	    		}
	    		if(cellReturn(row.getCell(30)).length()>0) {		    		
	    			In.setNCB__c(cellReturn(row.getCell(30)));
	    		}
	    		if(cellReturn(row.getCell(31)).length()>0) {
		    		In.setADDON__c(cellReturn(row.getCell(31)));
	    		}
	    		else {
	    			In.setADDON__c("0");
	    		}
	    		if(cellReturn(row.getCell(32)).length()>0) {
		    		In.setTP__c(Double.parseDouble(cellReturn(row.getCell(32))));
	    		}
	    		if(cellReturn(row.getCell(33)).length()>0) {
		    		In.setGST__c(Double.parseDouble(cellReturn(row.getCell(33))));
	    		}
	    		else {
		    		In.setGST__c(Double.parseDouble("0"));
	    		}
	    		if(cellReturn(row.getCell(34)).length()>0) {
		    		In.setNET_PREMIUM__c(cellReturn(row.getCell(34)));
	    		}
	    		else {
	    			In.setNET_PREMIUM__c("0");
	    		}
	    		if(cellReturn(row.getCell(35)).length()>0) {
		    		In.setGROSS_Premium__c(cellReturn(row.getCell(35)));
	    		}
	    		else {
	    			In.setGROSS_Premium__c("0");
	    		}
	    		if(cellReturn(row.getCell(38)).length()>0) {
		    		In.setSource__c(cellReturn(row.getCell(38)));
	    		}
	    		if(cellReturn(row.getCell(39)).length()>0) {
		    		In.setStatus__c(cellReturn(row.getCell(39)));
	    		}
	    		}
	    	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        if(In.getSite__c().length()>5 && In.getChassis_No__c().length()>2) {
	        	Ins.add(In);
	        	if(Ins.size()==1) {
	        		Site123=In.getSite__c();
	        		Source123=In.getSource__c();
	        		Unit123=In.getMake__c();
	        	}
	        }
	    }
	    //System.out.print(Ins.size());
		return Ins;
	}
	
	/**
	 * Method to read data from Audi Delhi Insurance sheet
	 */	
	public List <InsuranceDTO> AudiDelInsSheetRead(XSSFSheet spreadsheet) {
		List <InsuranceDTO> Ins=new ArrayList<InsuranceDTO>();
		Iterator < Row >  rowIterator = spreadsheet.iterator();
		InsuranceDTO In = null;
	    XSSFRow row;
	    //String p="";
	    while (rowIterator.hasNext()) {
	    	In = new InsuranceDTO();
	    	row = (XSSFRow) rowIterator.next();
	    	try {
	    	if(row.getRowNum()>0) {
	    		In.setName(cellReturn(row.getCell(2))+cellReturn(row.getCell(3)));
	    		In.setCompany_Name__c(cellReturn(row.getCell(4)));
	    		In.setPhone__c(cellReturn(row.getCell(5)));
	    		In.setAlternate_Phone_No__c(cellReturn(row.getCell(6)));
	    		In.setLandline__c(cellReturn(row.getCell(7)));
	    		if(cellReturn(row.getCell(8)).contains("@")) {
	    			if(cellReturn(row.getCell(8)).contains(".com")) {
	    			In.setEmail__c(cellReturn(row.getCell(8)).substring(0,cellReturn(row.getCell(8)).indexOf(".com")+4));}
	    			else if(cellReturn(row.getCell(8)).contains(";")) {
		    			In.setEmail__c(cellReturn(row.getCell(8)).split(";",2)[0]);}
	    			else{In.setEmail__c(cellReturn(row.getCell(8)));}
	    			}
	    		In.setAddress__c(cellReturn(row.getCell(10)));
	    		In.setCity__c(cellReturn(row.getCell(11)));
	    		In.setPinCode__c(cellReturn(row.getCell(12)));
	    		In.setState__c(cellReturn(row.getCell(14)));
	    		In.setModel1__c(cellReturn(row.getCell(15)));
	    		In.setVarient__c(cellReturn(row.getCell(16)));
	    		In.setReg_No__c(cellReturn(row.getCell(17)));
	    		In.setChassis_No__c(cellReturn(row.getCell(18)));
	    		In.setEngine_No__c(cellReturn(row.getCell(19)));
	    		In.setModel_Year__c(cellReturn(row.getCell(20)).substring(6));
	    		String c =(cellReturn(row.getCell(21)));
	    		////System.out.println("C="+c+" l="+c.length());
	    		if(cellReturn(row.getCell(21)).length()>9) {
	    			In.setSold_Date__c(c, 4);
	    			In.setInsurance_Renewal_Date__c(c, 5);
	    		}
	    		}
	    	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        if(In.getName().length()>1) {
	        	Ins.add(In);
	        	if(Ins.size()==1) {
	        		Site123=In.getSite__c();
	        		Source123=In.getSource__c();
	        		Unit123=In.getMake__c();
	        	}
	        }
	    }
		return Ins;
	}
	
	/**
	 * Method to read data from Audi Kokata Insurance sheet
	 */	
	public List <InsuranceDTO> AudiKolInsSheetRead(XSSFSheet spreadsheet) {
		List <InsuranceDTO> Ins=new ArrayList<InsuranceDTO>();
		Iterator < Row >  rowIterator = spreadsheet.iterator();
		InsuranceDTO In = null;
	    XSSFRow row;
	    //String p="";
	    while (rowIterator.hasNext()) {
	    	In = new InsuranceDTO();
	    	row = (XSSFRow) rowIterator.next();
	    	try {
	    	if(row.getRowNum()>0) {
	    		In.setReg_No__c(cellReturn(row.getCell(1)));
	    		In.setChassis_No__c(cellReturn(row.getCell(2)));
	    		In.setEngine_No__c(cellReturn(row.getCell(3)));
	    		In.setModel1__c(cellReturn(row.getCell(4)));
	    		if(cellReturn(row.getCell(7)).length()>=9) {
	    			In.setSold_Date__c(cellReturn(row.getCell(7)), 4);
	    			In.setInsurance_Renewal_Date__c(cellReturn(row.getCell(7)), 5);
	    		}
				In.setName(cellReturn(row.getCell(9))+cellReturn(row.getCell(10))+cellReturn(row.getCell(11)));
	    		In.setCompany_Name__c(cellReturn(row.getCell(12)));
	    		In.setAddress__c(cellReturn(row.getCell(13)));
	    		In.setCity__c(cellReturn(row.getCell(14)));
	    		In.setPinCode__c(cellReturn(row.getCell(15)));
	    		In.setPhone__c(cellReturn(row.getCell(16)));
	    		In.setEmail__c(cellReturn(row.getCell(17)).replaceAll("'", ""));
	    		In.setLandline__c(cellReturn(row.getCell(18)));
	    		
	    		}
	    	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        if(In.getName().length()>1) {
	        	Ins.add(In);
	        	if(Ins.size()==1) {
	        		Site123=In.getSite__c();
	        		Source123=In.getSource__c();
	        		Unit123=In.getMake__c();
	        	}
			}
	    }
		return Ins;
	}
	

	/**
	 * Method to upload data to salesforce
	 */	
	public void sfUpload(List <InsuranceDTO> Ins, int calledBy, int recNo1) {
		Insurance__c Ic = new Insurance__c();
		List<Insurance__c> Ic1 = new ArrayList<Insurance__c>();
		List<InsuranceDTO> Ic11 = new ArrayList<InsuranceDTO>();
		LogMST lm = new LogMST();
		EnterpriseConnection serviceConn1 =null;
		int SucMsg=0;
		String st2="";
		try {	 
			 serviceConn1 = DestinationConnectionUtility.getConnection();
				if(serviceConn1==null) {
					//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "Exception in SalesForce Connection", resultSuccess);
					return;
				}
		}catch(Exception e) {
		    Instant now = Instant.now();
			String now1 = now.toString();
			lm.insertLogDetails(recNo1, "Bulk", "Salesforce Connection not established at "+now1+" due to "+e);
		}
		Map<String, String > s = getAccountMaster();
		Map<String, String > sIC = getAcIdChas();
		////System.out.println(s);
		Map<String, String > s1 = getId();
		if(calledBy==1) {
			//St13b="<br>"+St13b;
			if(!St13b.contains("<th>")) {
			St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
			St13b=St13b+"<th>Site</th><th>Type (New/Renew/Rollover)</th><th>Customer Name</th><th>Customer Type</th><th>Company Name</th><th>Email</th><th>Phone</th><th>Alternate Phone No.</th><th>Landline</th><th>Address</th><th>City</th><th>State</th><th>Pincode</th><th>Make</th><th>Model</th><th>Model Year</th><th>Variant</th><th>Reg. No.</th><th>Chassis/VIN No.</th><th>Engine No.</th><th>Fuel Type</th><th>Sold Date</th><th>Current Insurer</th><th>Proposal No.</th><th>Policy No.</th><th>Claim Status</th><th>Policy Issued On</th><th>Renewal Date</th><th>IDV</th><th>OD</th><th>NCB Percent</th><th>Add-On</th><th>TP</th><th>GST</th><th>Net Premium</th><th>Gross premium</th><th>Last Customer Response</th><th>Last Call DateTime</th><th>Source</th><th>Status</th><th>Error Message</th>"
					+"</tr>";
			}
			}
		else if(calledBy==2) {
			if(!St13b.contains("<th>")) {
			St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
			St13b=St13b+"<th>S.No.</th><th>Data Source</th><th>First Name</th><th>Last Name</th><th>Company Name</th><th>Mobile 1</th><th>Mobile 2</th><th>Landline</th><th>Email</th><th>Email 2</th><th>Address 1</th><th>City</th><th>Pincode</th><th>Area</th><th>State</th><th>Model</th><th>Version</th><th>Reg No.</th><th>Chassis No.</th><th>Engine No.</th><th>Model Year</th><th>Sold Date</th><th>Sold Dealer</th><th>Last Service Date</th><th>Last Service Details as per Qlikview</th><th>Next Service Date</th><th>Service Due Month</th><th>ADW Service Status</th><th>Insurance Status</th><th>Category</th><th>Status</th><th>Remarks</th><th>Error Message</th>"
					+"</tr>";
			}
			}
		else if(calledBy==3) {
			if(!St13b.contains("<th>")) {
			St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
			St13b=St13b+"<th>Sl. No.</th><th>	Vehicle Registration Number</th><th>Chassis (VIN) Number</th><th>Engine Number</th><th>Model</th><th>Colour</th><th>Odometer Reading (Kms)</th><th>Date of Sale </th><th>Dealer Invoice No</th><th>First name</th><th>Middle Name</th><th>Last name</th><th>Company Name</th><th> Address 1</th><th>CITY</th><th>Pin Code</th><th>	Mobile Phone Number</th><th>	Email id:</th><th>	Landline Phone Number</th><th>	Name of 2nd Contact Person</th><th>Preffered Contact Time Slot</th><th>Error Message</th>	"
					+ "</tr>";
			}
		}
		for (InsuranceDTO Ins1: Ins) {
		Ic = new Insurance__c();

		if (calledBy==1) {
			//called by AllInsSheetRead
			if(Ins1.getSite__c().trim().equalsIgnoreCase("Toyota Noida")|| Ins1.getSite__c().trim().equalsIgnoreCase("Toyota Delhi") || ((Ins1.getSite__c().contains("Toyota") || Ins1.getSite__c().contains("TOYOTA")) && (Ins1.getSite__c().contains("Noida") || Ins1.getSite__c().contains("NOIDA")) )) {
				Ic.setSite__c(Ins1.getSite__c());
				if(Ins1.getType__c().length()>2) {
					Ic.setType__c(Ins1.getType__c());
				}
				else {
					Ic.setType__c("New");
				}
				////System.out.println(Ins1.getSite__c()+" "+Ins1.getMake__c());
				Ic.setMake__c(Ins1.getMake__c());
				Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
				Ic.setModel_Year__c(Ins1.getModel_Year__c());
				Ic.setVarient__c(Ins1.getVarient__c());
				Ic.setReg_No__c(Ins1.getReg_No__c());
				Ic.setChassis_No__c(Ins1.getChassis_No__c());
				Ic.setEngine_No__c(Ins1.getEngine_No__c());
				////System.out.println("Name: "+Ins1.getName()+" ChassisNo: "+Ic.getChassis_No__c());
				Ic.setFuel_Type__c(Ins1.getFuel_Type__c());
				if(Ins1.getInsurer__c().length()>2) {
					Ic.setInsurer__c(Ins1.getInsurer__c());
				}
				Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
				Ic.setPolicy_Issued_On__c(Ins1.getPolicy_Issued_On__c());
				Ic.setSold_Date__c(Ins1.getSold_Date__c());
				Ic.setPolicy_Number__c(Ins1.getPolicy_Number__c());
				Ic.setIDV__c(Ins1.getIDV__c());
				Ic.setOwn_Damage__c(Double.parseDouble(Ins1.getOwn_Damage__c()));
				Ic.setADDON__c(Double.parseDouble(Ins1.getADDON__c()));
				Ic.setTP__c(Ins1.getTP__c());
				Ic.setGST__c(Ins1.getGST__c());
				if(Ins1.getNCB__c().length()>0)
				Ic.setNCB__c(Ins1.getNCB__c());

				//Ic.setGROSS_Premium__c(Double.parseDouble(Ins1.getGROSS_Premium__c()));
				Ic.setSource__c(Ins1.getSource__c().trim());
				//Ic.setSource__c("Bulk Upload");
				//System.out.println("S"+Ic.getSource__c()+" "+Ins1.getSource__c());
				//Ic.setStatus__c(Ins1.getStatus__c());
			}
			if(Ins1.getSite__c().trim().equalsIgnoreCase("Audi Delhi") || ((Ins1.getSite__c().contains("Audi") || Ins1.getSite__c().contains("AUDI")) && (Ins1.getSite__c().contains("Delhi") || Ins1.getSite__c().contains("DELHI")) )) {
				Ic.setSite__c(Ins1.getSite__c());
				if(Ins1.getType__c().length()>2) {
					Ic.setType__c(Ins1.getType__c());
				}
				else {
					Ic.setType__c("New");
				}
				Ic.setSource__c(Ins1.getSource__c());
				Ic.setMake__c(Ins1.getMake__c());
				if(Ins1.getModel1__c()!=null) {
					Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
				}
				if(Ins1.getVarient__c()!=null) {
					Ic.setVarient__c(Ins1.getVarient__c());
				}
				Ic.setReg_No__c(Ins1.getReg_No__c());
				Ic.setChassis_No__c(Ins1.getChassis_No__c());
				Ic.setSold_Date__c(Ins1.getSold_Date__c());
				Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
				Ic.setModel_Year__c(Ins1.getModel_Year__c());
				//System.out.println(Ins1.getChassis_No__c()+" "+getStrDate(Ins1.getSold_Date__c()));
				//Ic.setStatus__c(Ins1.getStatus__c());
			}
			if(Ins1.getSite__c().trim().equalsIgnoreCase("AUDI KOLKATTA") || ((Ins1.getSite__c().contains("Audi") || Ins1.getSite__c().contains("AUDI")) && (Ins1.getSite__c().contains("Kolkata") || Ins1.getSite__c().contains("KOLKATTA")) ) ) {
				Ic.setSite__c("Audi Kolkata");
				if(Ins1.getType__c().length()>2) {
					Ic.setType__c(Ins1.getType__c());
				}
				else {
					Ic.setType__c("New");
				}
				Ic.setSource__c("Bulk Upload");
				Ic.setMake__c(Ins1.getMake__c());
				Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());

				Ic.setReg_No__c(Ins1.getReg_No__c());
				Ic.setChassis_No__c(Ins1.getChassis_No__c());
				if(Ins1.getEngine_No__c().length()>2) {
					Ic.setEngine_No__c(Ins1.getEngine_No__c());
				}
				if(Ins1.getSold_Date__c()!=null) {
					Ic.setSold_Date__c(Ins1.getSold_Date__c());
					Ic.setPolicy_Issued_On__c(Ins1.getSold_Date__c());
				}
			}
			if(Ins1.getSite__c().trim().equalsIgnoreCase("BBz Ghaziabad") || Ins1.getSite__c().trim().equalsIgnoreCase("Bharat Benz Ghaziabad") || ((Ins1.getSite__c().contains("BBZ") || Ins1.getSite__c().contains("BBz")) && (Ins1.getSite__c().contains("Ghaziabad") || Ins1.getSite__c().contains("GHAZIABAD")) )) {
				Ic.setSite__c("Bharat Benz Ghaziabad");
				if(Ins1.getType__c().length()>2) {
					Ic.setType__c(Ins1.getType__c());
				}
				else {
					Ic.setType__c("New");
				}
				if(Ins1.getSource__c()!=null && !Ins1.getSource__c().equalsIgnoreCase("")) {
					Ic.setSource__c(Ins1.getSource__c());
				}
				else {
					Ic.setSource__c("Bulk Upload");
				}
				Ic.setMake__c("BharatBenz");
				Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
				Ic.setModel_Year__c(Ins1.getModel_Year__c());
				Ic.setVarient__c(Ins1.getVarient__c());
				Ic.setReg_No__c(Ins1.getReg_No__c());
				Ic.setChassis_No__c(Ins1.getChassis_No__c().replaceAll("\'", ""));
				//System.out.println(Ins1.getChassis_No__c());
				Ic.setEngine_No__c(Ins1.getEngine_No__c().replaceAll("\'", ""));
				Ic.setFuel_Type__c(Ins1.getFuel_Type__c());
				if(Ins1.getInsurer__c().length()>2) {
					Ic.setInsurer__c(Ins1.getInsurer__c());
				}
				Ic.setProposal_No__c(Ins1.getProposal_No__c());
				if(Ins1.getPolicy_Number__c().length()>3) {
					Ic.setPolicy_Number__c(Ins1.getPolicy_Number__c());
				}
				Ic.setCLAIM_STATUS__c(Ins1.getCLAIM_STATUS_c());
				if(Ins1.getPolicy_Issued_On__c()!=null) {
					Ic.setPolicy_Issued_On__c(Ins1.getPolicy_Issued_On__c());
				}
				if(Ins1.getInsurance_Renewal_Date__c()!=null) {
					Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
				}
				Ic.setIDV__c(Ins1.getIDV__c());
				Ic.setOwn_Damage__c(Double.parseDouble(Ins1.getOwn_Damage__c()));
				Ic.setNCB__c(Ins1.getNCB__c());
				Ic.setADDON__c(Double.parseDouble(Ins1.getADDON__c()));
				Ic.setTP__c(Ins1.getTP__c());
				Ic.setGST__c(Ins1.getGST__c());
				////Ic.setNET_PREMIUM__c(Double.parseDouble(Ins1.getNET_PREMIUM__c()));
				//Ic.setGROSS_Premium__c(Double.parseDouble(Ins1.getGROSS_Premium__c()));
				//Ic.setStatus__c(Ins1.getStatus__c());
			}
			if(Ins1.getSite__c().trim().equalsIgnoreCase("DERA - PRIME AUTO CA")) {
				Ic.setSite__c(Ins1.getSite__c());
				if(Ins1.getType__c().length()>2) {
					Ic.setType__c(Ins1.getType__c());
				}
				else {
					Ic.setType__c("New");
				}
				
				Ic.setMake__c(Ins1.getMake__c());
				Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
				Ic.setVarient__c(Ins1.getVarient__c());
				if(Ins1.getReg_No__c().length()>3) {
					Ic.setReg_No__c(Ins1.getReg_No__c());
				}
				Ic.setChassis_No__c(Ins1.getChassis_No__c());
				if(Ins1.getEngine_No__c().length()>3) {
				Ic.setEngine_No__c(Ins1.getEngine_No__c());
				}
				if(getStrDate(Ins1.getSold_Date__c()).length()>6) {
					Ic.setSold_Date__c(Ins1.getSold_Date__c());
				}
				if(Ins1.getInsurer__c().length()>2) {
					Ic.setInsurer__c(Ins1.getInsurer__c());
				}
				if(Ins1.getPolicy_Number__c().length()>3) {
				Ic.setPolicy_Number__c(Ins1.getPolicy_Number__c().replaceAll("\'", ""));
				}
				
				Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
				if(getStrDate(Ins1.getPolicy_Issued_On__c()).length()>2) {
				Ic.setPolicy_Issued_On__c(Ins1.getPolicy_Issued_On__c());}
				if(Ins1.getIDV__c()>0)
				Ic.setIDV__c(Ins1.getIDV__c());
				
				if(Ins1.getOwn_Damage__c().length()>1)
				Ic.setOwn_Damage__c(Double.parseDouble(Ins1.getOwn_Damage__c()));
				
				if(Ins1.getADDON__c().length()>1)
				Ic.setADDON__c(Double.parseDouble(Ins1.getADDON__c()));
				if(Ins1.getTP__c()>0)
				Ic.setTP__c(Ins1.getTP__c());
				if(Ins1.getGST__c()>0)
				Ic.setGST__c(Ins1.getGST__c());

				//Ic.setGROSS_Premium__c(Double.parseDouble(Ins1.getGROSS_Premium__c()));
				//Ic.setStatus__c(Ins1.getStatus__c());
			}
		if(Ins1.getSite__c()!=null && Ins1.getSite__c().length()>1) {Ic.setSite__c(Ins1.getSite__c());}
		if(Ins1.getType__c().length()>2) {	Ic.setType__c(Ins1.getType__c());}else {Ic.setType__c("New");}
		if(Ins1.getMake__c()!=null && Ins1.getMake__c().length()>1) {Ic.setMake__c(Ins1.getMake__c());}
		if(Ins1.getVarient__c()!=null && Ins1.getVarient__c().length()>1) {Ic.setVarient__c(Ins1.getVarient__c());}
		if(Ins1.getModel1__c()!=null && Ins1.getModel1__c().length()>1) {Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());}
		if(Ins1.getReg_No__c().length()>3) {Ic.setReg_No__c(Ins1.getReg_No__c());	}
		if(Ins1.getChassis_No__c()!=null && Ins1.getChassis_No__c().length()>1) {Ic.setChassis_No__c(Ins1.getChassis_No__c());}
		if(Ins1.getEngine_No__c()!=null && Ins1.getEngine_No__c().length()>3) {	Ic.setEngine_No__c(Ins1.getEngine_No__c());	}
		if(Ins1.getFuel_Type__c()!=null && Ins1.getFuel_Type__c().length()>3) {	Ic.setFuel_Type__c(Ins1.getFuel_Type__c());	}
		if(Ins1.getInsurer__c()!=null && Ins1.getInsurer__c().length()>2) {	Ic.setInsurer__c(Ins1.getInsurer__c());		}
		if(Ins1.getProposal_No__c()!=null && Ins1.getProposal_No__c().length()>2) {	Ic.setProposal_No__c(Ins1.getProposal_No__c());		}
		if(Ins1.getPolicy_Number__c()!=null && Ins1.getPolicy_Number__c().length()>3) {	Ic.setPolicy_Number__c(Ins1.getPolicy_Number__c().replaceAll("\'", ""));}
		if(Ins1.getIDV__c()>0) {Ic.setIDV__c(Ins1.getIDV__c());}		
		if(Ins1.getOwn_Damage__c()!=null && Ins1.getOwn_Damage__c().length()>1) {Ic.setOwn_Damage__c(Double.parseDouble(Ins1.getOwn_Damage__c()));}
		if(Ins1.getADDON__c()!=null && Ins1.getADDON__c().length()>1) {	Ic.setADDON__c(Double.parseDouble(Ins1.getADDON__c()));}
		if(Ins1.getTP__c()>0) {	Ic.setTP__c(Ins1.getTP__c());}
		if(Ins1.getGST__c()>0) { Ic.setGST__c(Ins1.getGST__c());}
		if(Ins1.getSource__c()!=null && Ins1.getSource__c().length()>1) {Ic.setSource__c(Ins1.getSource__c());}else {Ic.setSource__c("Bulk Upload");}
		
		
		
		
		if((""+Ins1.getModel_Year__c()).length()+1>1) {Ic.setModel_Year__c(Ins1.getModel_Year__c());}	
		if(getStrDate(Ins1.getSold_Date__c()).length()>4) {Ic.setSold_Date__c(Ins1.getSold_Date__c());}
		if(getStrDate(Ins1.getPolicy_Issued_On__c()).length()>4) {Ic.setPolicy_Issued_On__c(Ins1.getPolicy_Issued_On__c());}
		if(getStrDate(Ins1.getInsurance_Renewal_Date__c()).length()>4) {Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());}
		
		
		if( s1.containsKey(Ins1.getChassis_No__c()) ) {
		Ic.setId(s1.get(Ins1.getChassis_No__c()));
		
		  if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c()))
		  {} else { boolean primCon=false;
		  if(Ins1.getSource__c().equalsIgnoreCase("TP Data")) {primCon=true;}
		  insertContact(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() ,
		  Ins1.getEmail__c(),Ins1.getSTD__c(), Ins1.getLandline__c(),
		  Ins1.getAlternate_Phone_No__c(), sIC.get(Ins1.getChassis_No__c()), primCon);
		  }
		 
		Ic.setMake__c(Ic.getMake__c()+"Already present");
		}
		
		if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {
			//System.out.println(Ins1.getPhone__c());
			Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
		}
		else if(Ic.getCustomer__c()!=null) {}
		else {
			insertIntoAccountMaster(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getCity__c(),Ins1.getState__c(), Ins1.getPinCode__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAddress__c(), Ins1.getAlternate_Phone_No__c(), Ins1.getCustomer_Type__c());
			s = null;
			s = getAccountMaster();
			Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
		}
		}
		else if(calledBy==2) {
			//called by Audi Delhi
			Ic.setSite__c("Audi Delhi");
			Ic.setMake__c("Audi");
			Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
			Ic.setVarient__c(Ins1.getVarient__c());
			Ic.setReg_No__c(Ins1.getReg_No__c());
			Ic.setChassis_No__c(Ins1.getChassis_No__c());
			Ic.setEngine_No__c(Ins1.getEngine_No__c());
			Ic.setModel_Year__c(Ins1.getModel_Year__c());
			Ic.setSource__c("Bulk Upload");
			Ic.setSold_Date__c(Ins1.getSold_Date__c());
			Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
			
			if( s1.containsKey(Ins1.getChassis_No__c()) ) {
				Ic.setId(s1.get(Ins1.getChassis_No__c()));
				if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {}
				else {
					boolean primCon=false;
					if(Ins1.getSource__c().equalsIgnoreCase("TP Data")) {primCon=true;}
					insertContact(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAlternate_Phone_No__c(), sIC.get(Ins1.getChassis_No__c()), primCon);
				}
				Ic.setMake__c(Ic.getMake__c()+"Already present");
			}
				
			if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {
				//System.out.println(Ins1.getPhone__c());
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
			else if(Ic.getCustomer__c()!=null) {}
			else {
				insertIntoAccountMaster(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getCity__c(),Ins1.getState__c(), Ins1.getPinCode__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAddress__c(), Ins1.getAlternate_Phone_No__c(), Ins1.getCustomer_Type__c());
				s = null;
				s = getAccountMaster();
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
		}
		else if(calledBy==3) {
			//called by Audi Kolkata
			Ic.setSite__c("Audi Kolkata");
			Ic.setMake__c("Audi");
			Ic.setEngine_No__c(Ins1.getEngine_No__c());
			Ic.setChassis_No__c(Ins1.getChassis_No__c());
			Ic.setReg_No__c(Ins1.getReg_No__c());
			Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
			Ic.setSold_Date__c(Ins1.getSold_Date__c());
			Ic.setSource__c("Bulk Upload");
			Ic.setInsurance_Renewal_Date__c(Ins1.getInsurance_Renewal_Date__c());
			
			if( s1.containsKey(Ins1.getChassis_No__c()) ) {
				Ic.setId(s1.get(Ins1.getChassis_No__c()));
				if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {}
				else {
					boolean primCon=false;
					if(Ins1.getSource__c().equalsIgnoreCase("TP Data")) {primCon=true;}
					insertContact(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAlternate_Phone_No__c(), sIC.get(Ins1.getChassis_No__c()), primCon);
				}
				Ic.setMake__c(Ic.getMake__c()+"Already present");
			}
				
			if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {
				//System.out.println(Ins1.getPhone__c());
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
			else if(Ic.getCustomer__c()!=null) {}
			else {
				insertIntoAccountMaster(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getCity__c(),Ins1.getState__c(), Ins1.getPinCode__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAddress__c(), Ins1.getAlternate_Phone_No__c(), Ins1.getCustomer_Type__c());
				s = null;
				s = getAccountMaster();
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
		}
		else if(calledBy==7) {
            //called by BBz JobCard
			//System.out.println("called7");
            Ic.setSite__c(Ins1.getSite__c());
            Ic.setMake__c(Ins1.getMake__c());
            Ic.setModel1__c(Ins1.getModel1__c().toUpperCase());
            Ic.setReg_No__c(Ins1.getReg_No__c());
            Ic.setChassis_No__c(Ins1.getChassis_No__c());
            Ic.setSold_Date__c(Ins1.getSold_Date__c());
            
            if( s1.containsKey(Ins1.getChassis_No__c()) ) {
				Ic.setId(s1.get(Ins1.getChassis_No__c()));
				if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {}
				else {
					boolean primCon=false;
					if(Ins1.getSource__c().equalsIgnoreCase("TP Data")) {primCon=true;}
					insertContact(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAlternate_Phone_No__c(), sIC.get(Ins1.getChassis_No__c()), primCon);
				}
				Ic.setMake__c(Ic.getMake__c()+"Already present");
			}
				
			if(s.containsValue(Ins1.getPhone__c()) || s.containsKey(Ins1.getPhone__c())) {
				//System.out.println(Ins1.getPhone__c());
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
			else if(Ic.getCustomer__c()!=null) {}
			else {
				insertIntoAccountMaster(Ins1.getName(), Ins1.getPhone__c(), Ins1.getCompany_Name__c() , Ins1.getEmail__c(),Ins1.getCity__c(),Ins1.getState__c(), Ins1.getPinCode__c(),Ins1.getSTD__c(), Ins1.getLandline__c(), Ins1.getAddress__c(), Ins1.getAlternate_Phone_No__c(), Ins1.getCustomer_Type__c());
				s = null;
				s = getAccountMaster();
				Ic.setCustomer__c(s.get(Ins1.getPhone__c()));
			}
        }		

		Ic1.add(Ic);
		Ic11.add(Ins1);
		Ic11.get(Ic11.size()-1).setName(Ins1.getName());
		Ic11.get(Ic11.size()-1).setPhone__c(Ins1.getPhone__c());
		Ic11.get(Ic11.size()-1).setCompany_Name__c(Ins1.getCompany_Name__c());
		Ic11.get(Ic11.size()-1).setEmail__c(Ins1.getEmail__c());
		Ic11.get(Ic11.size()-1).setCity__c(Ins1.getCity__c());
		Ic11.get(Ic11.size()-1).setState__c(Ins1.getState__c());
		Ic11.get(Ic11.size()-1).setPinCode__c(Ins1.getPinCode__c());
		Ic11.get(Ic11.size()-1).setAddress__c(Ins1.getAddress__c());
		Ic11.get(Ic11.size()-1).setLandline__c(Ins1.getLandline__c());
		}
		UpsertResult[] sr = null;
		int IcSize = Ic1.size();
		//System.out.println("\n\n IcSize = "+IcSize+"\n\n");
		if(IcSize<1) {}
		else {
		int chunkSize =3;
		int noOfChunk=(int) IcSize/chunkSize;
		int lastChunkSize=IcSize%chunkSize;
		List<Insurance__c> Ic2 = null;
		List<InsuranceDTO> Ic21 = null;
		for(int i1 =0; i1<=noOfChunk; i1++) {
			Ic2 = null;
			Ic21 = null;
			if(i1<noOfChunk) {
				Ic2 = Ic1.subList(i1*chunkSize, (i1+1)*chunkSize);
				Ic21 = Ic11.subList(i1*chunkSize, (i1+1)*chunkSize);
			}
			else {
				if(lastChunkSize>0) {
					Ic2 = Ic1.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
					Ic21 = Ic11.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
				}
			}
		if(Ic2!=null && Ic2.size()!=0) {
			////System.out.println(Ic2.get(0).getInsurance_Renewal_Date__c().MONTH+"");
		try {
			SObject[] upserts = (Ic2).stream().toArray(Insurance__c[]::new);
			sr = serviceConn1.upsert("id", upserts);
			//System.out.println("sr length ="+sr.length);
		} catch (ConnectionException e) {
			//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "error in line no:100 serviceConn1.create(new SObject[] {billing}) ", resultSuccess);
			// insertExceptionLog("NA","uploadToSalesForce","Salesforce Connection could not be established while uploading Insurance values "+e);
			e.printStackTrace();
		}
		for (int i = 0; i < sr.length; i++) {
			try {
			//System.out.println("Ch no in list: "+Ic2.get(i).getChassis_No__c());
			if (sr[i].isSuccess()) {
				//System.out.println("Successfully updated Insurance with id of: " +sr[i].getId() + ".");
				//updateIsSynchedFlagFlag(bean.getInvoiceId());
				//resultSuccess="Success";
				SucMsg++;
				
				
			} else {
				 //String errorMsg = "Error while update Insurance: "+sr[i].toString()+" "  + sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
				 String errorMsg = ""+ sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
				 //System.out.println(errorMsg);
				 errorMsg=getErMsg(errorMsg, Ic21.get(i));			
				 if(Ic2.get(i).getMake__c().contains("Already")) {
					 Ic2.get(i).setMake__c(Ic2.get(i).getMake__c().replace("Already present", ""));
					 Ic21.get(i).setMake__c(Ic21.get(i).getMake__c().replace("Already present", ""));
				 }
				 InsErrorDTO InErr= new InsErrorDTO();
				 st2=st2+errorMsg+"<br>";
				 //if(Ic21.get(i))
					if(calledBy==1) {
						St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
						St13b=St13b+"<td>"+Ic2.get(i).getSite__c()+"</td><td>"+Ic2.get(i).getType__c()+"</td><td>"+Ic21.get(i).getName()+"</td><td>"+Ic21.get(i).getCustomer_Type__c()+"</td><td>"+Ic21.get(i).getCompany_Name__c()+"</td><td>"+Ic21.get(i).getEmail__c()+"</td><td>"+Ic21.get(i).getPhone__c()+"</td><td></td><td>"+Ic21.get(i).getLandline__c()+"</td><td>"+Ic21.get(i).getAddress__c()+"</td><td>"+Ic21.get(i).getCity__c()+"</td><td>"+Ic21.get(i).getState__c()+"</td><td>"+Ic21.get(i).getPinCode__c()+"</td><td>"+Ic2.get(i).getMake__c()+"</td><td>"+Ic2.get(i).getModel1__c()+"</td><td>"+Ic2.get(i).getModel_Year__c()+"</td><td>"+Ic2.get(i).getVarient__c()
								+"</td><td>"+Ic2.get(i).getReg_No__c()+"</td><td>"+Ic2.get(i).getChassis_No__c()+"</td><td>"+Ic2.get(i).getEngine_No__c()+"</td><td>"+Ic2.get(i).getFuel_Type__c()+"</td><td>"+getDateString(Ic2.get(i).getSold_Date__c())+"</td><td>"+Ic2.get(i).getInsurer__c()+"</td><td>"+Ic2.get(i).getProposal_No__c()+"</td><td>"+Ic2.get(i).getPolicy_Number__c()+"</td><td>"+Ic2.get(i).getCLAIM_STATUS__c()+"</td><td>"+getDateString(Ic2.get(i).getPolicy_Issued_On__c())+"</td><td>"+getDateString(Ic2.get(i).getInsurance_Renewal_Date__c())+"</td><td>"+Ic2.get(i).getIDV__c()+"</td><td>"+Ic2.get(i).getOwn_Damage__c()+"</td><td>"+Ic2.get(i).getNCB__c()+"</td><td>"+Ic2.get(i).getADDON__c()
								+"</td><td>"+Ic2.get(i).getTP__c()+"</td><td>"+Ic2.get(i).getGST__c()+"</td><td>"+Ic2.get(i).getNET_PREMIUM__c()+"</td><td>"+Ic2.get(i).getGROSS_Premium__c()+"</td><td></td><td></td><td>"+Ic2.get(i).getSource__c()+"</td><td>"+Ic2.get(i).getStatus__c()+"</td><td>"+errorMsg+"</td>"
								+"</tr>";
						lm.insertLogDetails(recNo1, "Name:"+Ic21.get(i).getName()+" Phone:"+Ic21.get(i).getPhone__c()+" Email:"+Ic21.get(i).getEmail__c()+" ChassisNo:"+Ic2.get(i).getChassis_No__c(), errorMsg);
						InErr.setInsType("1");
						}
					else if(calledBy==2) {
						St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
						St13b=St13b+"<td></td><td></td><td>"+Ic21.get(i).getName()+"</td><td></td><td>"+Ic21.get(i).getCompany_Name__c()+"</td><td>"+Ic21.get(i).getPhone__c()+"</td><td></td><td>"+Ic21.get(i).getLandline__c()+"</td><td>"+Ic21.get(i).getEmail__c()+"</td><td></td><td>"+Ic21.get(i).getAddress__c()+"</td><td>"+Ic21.get(i).getCity__c()+"</td><td>"+Ic21.get(i).getPinCode__c()+"</td><td></td><td>"+Ic21.get(i).getState__c()+"</td><td>"+Ic2.get(i).getModel1__c()+"</td><td>"+Ic2.get(i).getVarient__c()+"</td><td>"+Ic2.get(i).getReg_No__c()+"</td><td>"+Ic2.get(i).getChassis_No__c()+"</td><td>"+Ic2.get(i).getEngine_No__c()+"</td>"
								+"<td>"+Ic2.get(i).getModel_Year__c()+"</td><td>"+getDateString(Ic2.get(i).getSold_Date__c())+"</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>"+errorMsg+"</td>"+"</tr>";

						lm.insertLogDetails(recNo1, "Name:"+Ic21.get(i).getName()+" Phone:"+Ic21.get(i).getPhone__c()+" Email:"+Ic21.get(i).getEmail__c()+" ChassisNo:"+Ic2.get(i).getChassis_No__c(), errorMsg);
						InErr.setInsType("2");
						}
					else if(calledBy==3) {
						St13b=St13b+"<tr style=\"border: 1px solid black; border-collapse: collapse; text-align:center;\">";
						St13b=St13b+"<td></td><td>"+Ic2.get(i).getReg_No__c()+"</td><td>"+Ic2.get(i).getChassis_No__c()+"</td><td>"+Ic2.get(i).getEngine_No__c()+"</td><td>"+Ic2.get(i).getModel1__c()+"</td><td></td><td></td>"
								+"<td>"+getDateString(Ic2.get(i).getSold_Date__c())+"</td><td></td><td>"+Ic21.get(i).getName()+"</td><td></td><td></td><td>"+Ic21.get(i).getCompany_Name__c()+"</td><td>"+Ic21.get(i).getAddress__c()+"</td><td>"+Ic21.get(i).getCity__c()+"</td><td>"+Ic21.get(i).getPinCode__c()+"</td><td>"+Ic21.get(i).getPhone__c()+"</td><td>"+Ic21.get(i).getEmail__c()+"</td><td></td><td></td><td></td><td>"+errorMsg+"</td>"+"</tr>";

						lm.insertLogDetails(recNo1, "Name:"+Ic21.get(i).getName()+" Phone:"+Ic21.get(i).getPhone__c()+" Email:"+Ic21.get(i).getEmail__c()+" ChassisNo:"+Ic2.get(i).getChassis_No__c(), errorMsg);
						InErr.setInsType("3");
						}
					InErr.setSite__c(Ic2.get(i).getSite__c());
					InErr.setType__c(Ic2.get(i).getType__c());
					InErr.setName(Ic21.get(i).getName());
					InErr.setCustomer_Type__c(""+Ic21.get(i).getCustomer_Type__c());
					InErr.setCompany_Name__c(Ic21.get(i).getCompany_Name__c());
					InErr.setEmail__c(Ic21.get(i).getEmail__c());
					InErr.setPhone__c(Ic21.get(i).getPhone__c());
					InErr.setAlternate_Phone_No__c(Ic21.get(i).getAlternate_Phone_No__c());
					InErr.setLandline__c(Ic21.get(i).getLandline__c());
					InErr.setAddress__c(Ic21.get(i).getAddress__c());
					InErr.setCity__c(Ic21.get(i).getCity__c());
					InErr.setState__c(Ic21.get(i).getState__c());
					InErr.setPinCode__c(Ic21.get(i).getPinCode__c());
					InErr.setMake__c(Ic2.get(i).getMake__c());
					InErr.setModel1__c(Ic2.get(i).getModel1__c());
					InErr.setModel_Year__c(Ic2.get(i).getModel_Year__c());
					InErr.setVarient__c(Ic2.get(i).getVarient__c());
					InErr.setReg_No__c(Ic2.get(i).getReg_No__c());
					InErr.setChassis_No__c(Ic2.get(i).getChassis_No__c());
					InErr.setEngine_No__c(Ic2.get(i).getEngine_No__c());
					InErr.setFuel_Type__c(Ic2.get(i).getFuel_Type__c());
					InErr.setSold_Date__c(Ic2.get(i).getSold_Date__c());
					InErr.setInsurer__c(Ic2.get(i).getInsurer__c());
					InErr.setProposal_No__c(Ic2.get(i).getProposal_No__c());
					InErr.setPolicy_Number__c(Ic2.get(i).getPolicy_Number__c());
					InErr.setCLAIM_STATUS_c(Ic2.get(i).getCLAIM_STATUS__c());
					InErr.setPolicy_Issued_On__c(Ic2.get(i).getPolicy_Issued_On__c());
					InErr.setInsurance_Renewal_Date__c(Ic2.get(i).getInsurance_Renewal_Date__c());
					InErr.setIDV__c(Ic2.get(i).getIDV__c());
					InErr.setOwn_Damage__c(""+(Ic2.get(i).getOwn_Damage__c()));
					InErr.setNCB__c(""+Ic2.get(i).getNCB__c());
					InErr.setADDON__c(""+Ic2.get(i).getADDON__c());
					InErr.setTP__c(Ic2.get(i).getTP__c());
					InErr.setGST__c(Ic2.get(i).getGST__c());
					InErr.setNET_PREMIUM__c(""+Ic2.get(i).getNET_PREMIUM__c());
					InErr.setGROSS_Premium__c(""+Ic2.get(i).getGROSS_Premium__c());
					InErr.setSource__c(Ic2.get(i).getSource__c());
					InErr.setStatus__c(Ic2.get(i).getStatus__c());
					InErr.setError(errorMsg);
					InErrL.add(InErr);
			}
			}catch(Exception e) {e.printStackTrace();}
		}
		}
		//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), resultError, resultSuccess);
		}
		}
		 if(st2==null || st2=="")
		 {
			 st2="No Errors<br>";
		 }
		St11b = ""+SucMsg;
		St12b= st2;
	
	}
	
	/**
	 * Method to get User Details from Salesforce
	 */	
	public Map<String, String> getAccountMaster()
	{
		//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
		Map<String, String> list= new HashMap<String, String>();
		   EnterpriseConnection serviceConn1 =null;
		   try {
			serviceConn1 = ConnectionUtility.getConnection();
			//String soqlQuery = "select Id, Phone from Account where RecordTypeId='0122x000000g1GnAAI' order by Name "; 
			String soqlQuery = "select Id, Phone from Account order by Name"; 

	       try {
		         QueryResult qr = serviceConn1.query(soqlQuery);
		         boolean done = false;
		         if (qr.getSize() > 0) {
		            while (!done) {
		               SObject[] records = qr.getRecords();
		               for (int i = 0; i < records.length; i++) {
		            	   Account spm=(Account) records[i];
		            	   list.put(spm.getPhone(), spm.getId());
					    }
					
					    if (qr.isDone()) {
					       done = true;
					    } else {
					       qr = serviceConn1.queryMore(qr.getQueryLocator());
					    }
					 }
					}
		         else {
//					 //System.out.println("No records found.");
					}
					} 
	       catch (ConnectionException ce) {
	    	   //("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+ce);
	    	   ce.printStackTrace();
					}
					} 
		   catch (ConnectionException e) {
			   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+e);
	    	   e.printStackTrace();
					}
		return list;
	}
	
	/**
	 * Method to get User Details from Salesforce
	 */	
	public Map<String, String> getAccountMasterE()
	{
		//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
		Map<String, String> list= new HashMap<String, String>();
		   EnterpriseConnection serviceConn1 =null;
		   try {
			serviceConn1 = ConnectionUtility.getConnection();
			//String soqlQuery = "select Id, Email__c from Account where RecordTypeId='0122x000000g1GnAAI' order by Name "; 
			String soqlQuery = "select Id, Email__c from Account order by Name"; 

	       try {
		         QueryResult qr = serviceConn1.query(soqlQuery);
		         boolean done = false;
		         if (qr.getSize() > 0) {
		            while (!done) {
		               SObject[] records = qr.getRecords();
		               for (int i = 0; i < records.length; i++) {
		            	   Account spm=(Account) records[i];
		            	   list.put(spm.getEmail__c(), spm.getId());
					    }
					
					    if (qr.isDone()) {
					       done = true;
					    } else {
					       qr = serviceConn1.queryMore(qr.getQueryLocator());
					    }
					 }
					}
		         else {
//					 //System.out.println("No records found.");
					}
					} 
	       catch (ConnectionException ce) {
	    	   //("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+ce);
	    	   ce.printStackTrace();
					}
					} 
		   catch (ConnectionException e) {
			   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+e);
	    	   e.printStackTrace();
					}
		return list;
	}
	
	/**
	 * Method to get User Id and Chassis No from Salesforce
	 */	
	public Map<String, String>  getId()
	{
		//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
		Map<String, String>  list= new HashMap<String, String>();
		   EnterpriseConnection serviceConn1 =null;
		   try {
			serviceConn1 = ConnectionUtility.getConnection();
			String soqlQuery = "select Id, Chassis_No__c from Insurance__c "; 

	       try {
		         QueryResult qr = serviceConn1.query(soqlQuery);
		         boolean done = false;
		         if (qr.getSize() > 0) {
		            while (!done) {
		               SObject[] records = qr.getRecords();
		               for (int i = 0; i < records.length; i++) {
		            	   Insurance__c spm=(Insurance__c) records[i];
		            	   list.put( spm.getChassis_No__c(),spm.getId());
		            	   ////System.out.println("Id="+spm.getId()+" CNo="+spm.getChassis_No__c());
					    }
					
					    if (qr.isDone()) {
					       done = true;
					    } else {
					       qr = serviceConn1.queryMore(qr.getQueryLocator());
					    }
					 }
					}
		         else {
//					 //System.out.println("No records found.");
					}
					} 
	       catch (ConnectionException ce) {
	    	   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+ce);
	    	   ce.printStackTrace();
					}
					} 
		   catch (ConnectionException e) {
			   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+e);
	    	   e.printStackTrace();
					}
		return list;
	}
	
	/**
	 * Method to get Account Id and Chassis No from Salesforce
	 */	
	public Map<String, String>  getAcIdChas()
	{
		//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
		Map<String, String>  list= new HashMap<String, String>();
		   EnterpriseConnection serviceConn1 =null;
		   try {
			serviceConn1 = ConnectionUtility.getConnection();
			String soqlQuery = "select Customer__c, Chassis_No__c from Insurance__c "; 

	       try {
		         QueryResult qr = serviceConn1.query(soqlQuery);
		         boolean done = false;
		         if (qr.getSize() > 0) {
		            while (!done) {
		               SObject[] records = qr.getRecords();
		               for (int i = 0; i < records.length; i++) {
		            	   Insurance__c spm=(Insurance__c) records[i];
		            	   list.put( spm.getChassis_No__c(),spm.getCustomer__c());
		            	   ////System.out.println("Id="+spm.getId()+" CNo="+spm.getChassis_No__c());
					    }
					
					    if (qr.isDone()) {
					       done = true;
					    } else {
					       qr = serviceConn1.queryMore(qr.getQueryLocator());
					    }
					 }
					}
		         else {
//					 //System.out.println("No records found.");
					}
					} 
	       catch (ConnectionException ce) {
	    	   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+ce);
	    	   ce.printStackTrace();
					}
					} 
		   catch (ConnectionException e) {
			   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+e);
	    	   e.printStackTrace();
					}
		return list;
	}
	
	/**
	 * Method to insert new User into Salesforce
	 */
	public void insertIntoAccountMaster(String Name1, String Phone, String Company_Name__c, String Email__c, String BillingCity, String BillingState, String BillingPostalCode, String STD__c, String Landline__c, String BillingAddress, String Alternate_Phone_No__c1, String Customer_Type__c1)
	{
		EnterpriseConnection serviceConn1 =null;
		try {	 
			 serviceConn1 = DestinationConnectionUtility.getConnection();
				if(serviceConn1==null) {
					//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "Exception in SalesForce Connection", resultSuccess);
					return;
				}
		}catch(Exception e) {
			//insertExceptionLog("NA","uploadToSalesForce","Connection could not be esatblished with Salesforce wile creating new User "+e);
 	   }
		Account Ac = new Account();
		List<Account> Ac1 = new ArrayList<Account>();
		if(Phone.length()>9) {
		Ac.setPhone(Phone);
		}
		else if(Landline__c.length()>9 && Landline__c.length()<13) {
			Ac.setPhone(Landline__c);
		}
		Ac.setAlternate_Phone_No__c(Alternate_Phone_No__c1);
		Ac.setBillingStreet(BillingAddress);
		Ac.setBillingCity(BillingCity);
		Ac.setBillingState(BillingState);
		Ac.setBillingPostalCode(BillingPostalCode);
		
		if(STD__c.length()>1){
			Ac.setSTD__c(STD__c);
		}
		if(Landline__c.length()>5) {
			Ac.setLandline__c(Landline__c);
		}
		if(Customer_Type__c1==null || Customer_Type__c1=="") {
		if(Name1.trim().length()<3) {
			Ac.setCustomer_Type__c("Corporate");
			Ac.setName(Company_Name__c);
		}
		else if(Name1.trim().substring(0, 3).equalsIgnoreCase("M/S")) {
			Ac.setCustomer_Type__c("Corporate");
			Company_Name__c=Name1;
			Ac.setName(Company_Name__c);
		}
		else {
			Ac.setCustomer_Type__c("Individual");
			Ac.setName(Name1);
		}
		}
		else {
			Ac.setCustomer_Type__c(Customer_Type__c1);
			if(Customer_Type__c1.equalsIgnoreCase("Corporate")) {
				Name1=Company_Name__c;
			}
			Ac.setName(Name1);
		}
		if(Company_Name__c.length()>3) {
		Ac.setCompany_Name__c(Company_Name__c);}
		if(Email__c.length()<1) {
			Email__c="a"+Phone+"@gmail.com";
		}
		Ac.setEmail__c(Email__c.replaceAll(" ", ""));
		if(Ac.getCustomer_Type__c().length()<1) {
			Ac.setCustomer_Type__c("Individual");
		}
		//System.out.println(Ac.getEmail__c()+" "+Ac.getPhone()+" "+Ac.getCustomer_Type__c());
		//Ac.setRecordTypeId("0122x000000g1GnAAI");
		//Ac.setRecordType("Customer Account");
		Ac1.add(Ac);
		UpsertResult[] sr = null;
		try {
			SObject[] upserts = (Ac1).stream().toArray(Account[]::new);
			sr = serviceConn1.upsert("id", upserts);
			//System.out.println("sr length ="+sr.length);
		} 
		catch (ConnectionException e) {
			//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "error in line no:100 serviceConn1.create(new SObject[] {billing}) ", resultSuccess);
			//insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while inserting new User into SalesForce "+e);
	    	   e.printStackTrace();
	    	   n112=e.getMessage().toString().replaceAll("\n", "");
		}
		String st2="";
		for (int i = 0; i < sr.length; i++) {
			if (sr[i].isSuccess()) {
				//System.out.println("Successfully added new User with id of: " +sr[i].getId() + ".");
				//updateIsSynchedFlagFlag(bean.getInvoiceId());
				//resultSuccess="Success";
			} 
			else {
				//insertExceptionLog("NA","uploadToSalesForce","Failed to add new User "+sr[i].getErrors()[0].getMessage());
		    	//System.out.println("Error while adding new User: "+sr[i].toString()+" " +sr[i].getErrors()[0].getMessage()+" " );
				//resultError=sr[i].getErrors()[0].getMessage();
		    	   String errorMsg = ""+ sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
					n112=n112+sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
		    	   st2=st2+errorMsg+"<br>";
			}
		}
		
		St12b= st2;
	}

	/**
	 * Method to insert new User into Salesforce
	 */
	public void insertContact(String Name1, String Phone, String Company_Name__c, String Email__c,String STD__c, String Landline__c, String Alternate_Phone_No__c1, String AccountId1, boolean Primary_Contact__c1)
	{
		EnterpriseConnection serviceConn1 =null;
		//System.out.println("Contact add called"+AccountId1);
		try {	 
			 serviceConn1 = DestinationConnectionUtility.getConnection();
				if(serviceConn1==null) {
					//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "Exception in SalesForce Connection", resultSuccess);
					return;
				}
		}catch(Exception e) {
			e.printStackTrace();
 	   }
		try {
		Contact Co=new Contact();
		List<Contact> Co1 = new ArrayList<Contact>();
		Co.setAccountId(AccountId1);
		//Co.setName(Name1);
		//Name1=Name1+" ";
		Co.setLastName(Name1);
		if(Email__c.length()<1) {
			Email__c="a"+Phone+"@gmail.com";
		}
		Co.setEmail(Email__c);
		Co.setPhone(Phone);
		//Co.setMobilePhone(Alternate_Phone_No__c1);
		Co.setPrimary_Contact__c(Primary_Contact__c1);
		//local record type id = 0121m000000WWhLAAW
		//Co.setRecordTypeId("0121m000000WWhLAAW");
		
		//live record type id = 0125g000000liMpAAI
		Co.setRecordTypeId("0125g000000liMpAAI");
		//contact.HomePhone=Customer.Landline__c;
		Co1.add(Co);
		UpsertResult[] sr = null;
		try {
			SObject[] upserts = (Co1).stream().toArray(Contact[]::new);
			sr = serviceConn1.upsert("id", upserts);
			//System.out.println("sr length ="+sr.length);
		} 
		catch (ConnectionException e) {
			e.printStackTrace();
	    	//n112=e.getMessage().toString().replaceAll("\n", "");
		}
		for (int i = 0; i < sr.length; i++) {
			if (sr[i].isSuccess()) {
				//System.out.println("Successfully added contact with id of: " +	sr[i].getId() + ".");
			} 
			else {
		    	   //System.out.println("Error while adding new Contact: "+sr[i].toString()+" "+sr[i].getErrors()[0].getMessage()+" " );
		    	   //String errorMsg = ""+ sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
		    	   //n112=n112+sr[i].getErrors()[0].getMessage().toString().replaceAll("\n", "");
		    	   //st2=st2+errorMsg+"<br>";
			}
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method to Return cell value for numeric and Date types
	 */	
	 public String isNumberOrDate(Cell cell) {  
	        String retVal;  
	        if (HSSFDateUtil.isCellDateFormatted(cell)) {  
	            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	            retVal = formatter.format(cell.getDateCellValue());  
	 
	        } else {  
	            DataFormatter df = new DataFormatter();  
	            retVal = df.formatCellValue(cell);  
	        }  
	        return retVal;  
	    }  


		/**
		 * Method to Return default Date for empty date
		 */	
		 public String chkEmptyDate(String Date) {  
		        String retVal;  
		        if (Date.length()<5) {  
		        	retVal="01/01/1999";  
		 
		        } else {  
		            retVal = Date;  
		        }  
		        return retVal;  
		    }
	/**
	 * Method to Return cell value
	 */	
	public String cellReturn(Cell cell) {
		String s="";
		//cell.getNumericCellValue()
		if(cell==null) {}
		else {
		switch (cell.getCellType()) {
	    case 0:
	       s = ""+isNumberOrDate(cell);;
			break;
	    
		case 1:
	       s = cell.getStringCellValue();
			break;
		    
		case 2:
		   s = cell.getCellFormula();
			break;
		    
		case 3:
		   s = "";
			break;
		    
		case 4:
		   if(cell.getBooleanCellValue()) {
			   s="true";
		   }
		   else {
			   s="false";
		   }
			break;

		case 5:
	       s = ""+cell.getErrorCellValue();
			break;
	    default:
	    	s="";
	    	   break;
	 }
		}
		////System.out.println("C:"+c+" s:"+s);
		s=s+"";
		return s;		
	}

	public String splitStringS(String str)
    {
        StringBuffer alpha = new StringBuffer(), 
        num = new StringBuffer(), special = new StringBuffer();
        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
       return alpha.toString();
    }

	public String splitStringI(String str)
    {
        StringBuffer alpha = new StringBuffer(), 
        num = new StringBuffer(), special = new StringBuffer();
        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
       return num.toString();
    }
	
	public String getInsurer(String s)
	{
		String in="";
		String[] s1 = s.trim().split(" ",5);
		for (String s2: s1)
		{
			if(s2.equalsIgnoreCase("Bajaj")){
				in="Bajaj Allianz General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Bharti")){
				in="Bharti Axa General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Cholamandalam")){
				in="Cholamandalam MS General Ins. Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("HDFC")){
				in="HDFC ERGO General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("ICICI")){
				in="ICICI Lombard General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("IFFCO")){
				in="IFFCO Tokio General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("MAGMA")){
				in="MAGMA HDI GENERAL INSURANCE COMPANY LTD";
			}
			else if(s2.equalsIgnoreCase("Reliance")){
				in="Reliance General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("SBI")){
				in="SBI General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Tata")){
				in="Tata AIG General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("United")){
				in="United India Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("L")){
				in="L AND T INSURANCE";
			}
			else if(s2.equalsIgnoreCase("Digit")){
				in="Go Digit Car Insurance";
			}
			else if(s2.equalsIgnoreCase("Assurance")){
				in="The New India Assurance Co. Ltd";
			}
			else if(s2.equalsIgnoreCase("Edelweiss")){
				in="Edelweiss General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Acko")){
				in="Acko General Insurance Ltd.";
			}
			else if(s2.equalsIgnoreCase("Generali")){
				in="Future Generali India Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Kotak")){
				in="Kotak Mahindra General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Liberty")){
				in="Liberty General Insurance Ltd.";
			}
			else if(s2.equalsIgnoreCase("National")){
				in="National Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Navi")){
				in="Navi General Insurance Ltd.";
			}
			else if(s2.equalsIgnoreCase("Raheja")){
				in="Raheja QBE General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Sundaram")){
				in="Royal Sundaram General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Shriram")){
				in="Shriram General Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Oriental")){
				in="The Oriental Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("United")){
				in="United India Insurance Co. Ltd.";
			}
			else if(s2.equalsIgnoreCase("Sompo")){
				in="Universal Sompo General Insurance Co. Ltd.";
			}
		}
		return in;
	}

	/**
		 * Method to get User Id and Chassis No from Salesforce
		 */	
		public Map<String, String>  getVinNoModel(String loc,String type1)
		{
			//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
			Map<String, String>  list= new HashMap<String, String>();
			//System.out.println("getVinNoModel started");
			 String line = "";  
			 String splitBy = ",";  
			 String location = "C:/Users/Satvik.Singh/XlFiles/"+loc;
			 Path path = Paths.get(location);
			 
			 if (Files.exists(path)) {}
			 else {
				  ////insertExceptionLog("NA","csvToSalesForce","csv file does not exist at given location:"+loc+" on date - "+Mdate);
				  } 
			 String[] csvLine = null;
			 try   
			 {  
			 //parsing a CSV file into BufferedReader class constructor  
			 BufferedReader br = new BufferedReader(new FileReader(location));  
			 while ((line = br.readLine()) != null)   //returns a Boolean value  
			 {  
			 csvLine = line.split(splitBy);    // use comma as separator 
			 if(type1.equalsIgnoreCase("GS")) {
			 if(csvLine.length>=5) {
				 if(csvLine[0].trim().equalsIgnoreCase("Vin no")) {//done to ignore the first line
				 }
				 else {
					 if(csvLine[0].trim().length()>3 && csvLine[0].trim().length()<25) {
					     list.put(csvLine[0], csvLine[2]);
					 }
				 }			 
			 }
			 }
			 else if(type1.equalsIgnoreCase("BP")) {
				 if(csvLine.length>=5) {  
					 if(csvLine[3].trim().equalsIgnoreCase("Vin no")) {//done to ignore the first line
					 }
					 else {
						 if(csvLine[3].trim().length()>3 && csvLine[3].trim().length()<25) {
						     list.put(csvLine[3], csvLine[5]);
						 }
					 }			 
				 }
				 }
			 } 
			 br.close(); 
			 }   
			 catch (IOException e)   
			 {  
			 ////insertExceptionLog("NA","xlToSalesForce","Cell iteration encountered error for csv file on date - "+Mdate);
			 e.printStackTrace();  
			 }  
			 
			return list;
		}	
		
		
		/**
		 * Method to get DateString from Calendar
		 */	
		public String  getDateString(Calendar Cal)
		{
			String datef="";
			@SuppressWarnings("static-access")
			Date date = Cal.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
			String strDate = dateFormat.format(date).substring(0,11).trim();  
			int year = Integer.parseInt(strDate.split("-",3)[0]);
			int month = Integer.parseInt(strDate.split("-",3)[1]);
			String month1="Jan";
			if(month==1) {
				month1="Jan";
			}
			else if(month==2) {
				month1="Feb";
			}
			else if(month==3) {
				month1="Mar";
			}
			else if(month==2) {
				month1="Apr";
			}
			else if(month==2) {
				month1="May";
			}
			else if(month==2) {
				month1="Jun";
			}
			else if(month==2) {
				month1="Jul";
			}
			else if(month==2) {
				month1="Aug";
			}
			else if(month==2) {
				month1="Sep";
			}
			else if(month==2) {
				month1="Oct";
			}
			else if(month==2) {
				month1="Nov";
			}
			else if(month==2) {
				month1="Dec";
			}
			int day = Integer.parseInt(strDate.split("-",3)[2]);
			datef = day+"-"+month1+"-"+year;
			return datef;
		}
		
		/**
		 * Method to create Error Log File
		 */	
		public void  CreateErrLog(List<InsErrorDTO> InErrL1, String exPath)
		{
			if(InErrL1.size()>0) {
				//System.out.println("\n\nCreateErrLog called");
				String excelFilePath =""+exPath;
				String excelFilePathN ="";
				String insTy=InErrL1.get(0).getInsType();
				/*
				 * if(insTy.equalsIgnoreCase("1")) { //excelFilePath =
				 * "C:\\Users\\Satvik.Singh\\eclipse-workspace1Imacro\\salesForce 2\\sfdestination\\WebContent\\resource\\BulkUploadTemplateInsAll.xlsx"
				 * ; excelFilePath = getRealPath("resource/BulkUploadTemplateInsAll.xlsx"); }
				 * else if(insTy.equalsIgnoreCase("2")) { //excelFilePath =
				 * "C:\\Users\\Satvik.Singh\\eclipse-workspace1Imacro\\salesForce 2\\sfdestination\\WebContent\\resource\\BulkUploadTemplateInsAD.xlsx"
				 * ; excelFilePath = "resource/BulkUploadTemplateInsAD.xlsx"; } else
				 * if(insTy.equalsIgnoreCase("3")) { //excelFilePath =
				 * "C:\\Users\\Satvik.Singh\\eclipse-workspace1Imacro\\salesForce 2\\sfdestination\\WebContent\\resource\\BulkUploadTemplateInsAK.xlsx"
				 * ; excelFilePath = "resource/BulkUploadTemplateInsAK.xlsx"; }
				 */
				FileSystem system = FileSystems.getDefault();
				excelFilePathN=excelFilePath.replaceAll(".xlsx", "")+".xlsx";
		        Path original = system.getPath(excelFilePathN);
				excelFilePathN=excelFilePath.replaceAll(".xlsx", "")+"2.xlsx";
		        Path target = system.getPath(excelFilePathN);

		        try {
		            // Throws an exception if the original file is not found.
		            Files.copy(original, target, StandardCopyOption.REPLACE_EXISTING);
		            excelFilePath = excelFilePathN;					
		        } catch (IOException ex) {
		            //System.out.println("ERROR");
		        }
		       try {  
				//Read Excel document first
                FileInputStream input_document = new FileInputStream(new File(excelFilePath));
                // convert it into a POI object
                XSSFWorkbook my_xlsx_workbook = new XSSFWorkbook(input_document); 
                // Read excel sheet that needs to be updated
                XSSFSheet my_worksheet = my_xlsx_workbook.getSheetAt(0);
                // declare a Cell object
                Cell cell = null; 
                
                //clear the extra rows
                int row1=1;
                int totNum=my_worksheet.getLastRowNum();
                //System.out.println(totNum+"totnum");
                if(insTy.equalsIgnoreCase("1")) {
                	row1=2;      
				}
                for(int i1=row1+1;i1<=totNum;i1++) {
                	if (my_worksheet.getRow(i1) != null) {
                		my_worksheet.removeRow(my_worksheet.getRow(i1));
            		}
                }
                
                LogMST lm=new LogMST();
                XSSFRow sheetrow =null;
                for(InsErrorDTO InErr:InErrL1) {
                	sheetrow = my_worksheet.getRow(row1);
                	if(sheetrow == null){
                		sheetrow = my_worksheet.createRow(row1);
                	}

					lm.insertErrorlogMST(0, InErr);
                	// Access the cell first to update the value
                	for(int cN=0;cN<41;cN++) {
                	cell = sheetrow.getCell(cN);
                	if(cell == null){
                	    cell = sheetrow.createCell(cN);
                	}
                	if(insTy.equalsIgnoreCase("1")) {
                	if(cN==0) {
                		cell.setCellValue((""+InErr.getSite__c()).replaceAll("null", ""));
                        //System.out.println("Cell="+InErr.getSite__c());
                	}
                	else if(cN==1) {
                		cell.setCellValue((""+InErr.getType__c()).replaceAll("null", ""));
                	}
                	else if(cN==2) {
                		cell.setCellValue((""+InErr.getName()).replaceAll("null", ""));
                	}
                	else if(cN==3) {
                		cell.setCellValue((""+InErr.getCustomer_Type__c()).replaceAll("null", ""));
                	}
                	else if(cN==4) {
                		cell.setCellValue((""+InErr.getCompany_Name__c()).replaceAll("null", ""));
                	}
                	else if(cN==5) {
                		cell.setCellValue((""+InErr.getEmail__c()).replaceAll("null", ""));
                	}
                	else if(cN==6) {
                		cell.setCellValue((""+InErr.getPhone__c()).replaceAll("null", ""));
                	}
                	else if(cN==7) {
                		cell.setCellValue((""+InErr.getAlternate_Phone_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==8) {
                		cell.setCellValue((""+InErr.getLandline__c()).replaceAll("null", ""));
                	}
                	else if(cN==9) {
                		cell.setCellValue((""+InErr.getAddress__c()).replaceAll("null", ""));
                	}
                	else if(cN==10) {
                		cell.setCellValue((""+InErr.getCity__c()).replaceAll("null", ""));
                	}
                	else if(cN==11) {
                		cell.setCellValue((""+InErr.getState__c()).replaceAll("null", ""));
                	}
                	else if(cN==12) {
                		cell.setCellValue((""+InErr.getPinCode__c()).replaceAll("null", ""));
                	}
                	else if(cN==13) {
                		cell.setCellValue((""+InErr.getMake__c()).replaceAll("null", ""));
                	}
                	else if(cN==14) {
                		cell.setCellValue((""+InErr.getModel1__c()).replaceAll("null", ""));
                	}
                	else if(cN==15) {
                		cell.setCellValue((""+InErr.getModel_Year__c()).replaceAll("null", ""));
                	}
                	else if(cN==16) {
                		cell.setCellValue((""+InErr.getVarient__c()).replaceAll("null", ""));
                	}
                	else if(cN==17) {
                		cell.setCellValue((""+InErr.getReg_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==18) {
                		cell.setCellValue((""+InErr.getChassis_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==19) {
                		cell.setCellValue((""+InErr.getEngine_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==20) {
                		cell.setCellValue((""+InErr.getFuel_Type__c()).replaceAll("null", ""));
                	}
                	else if(cN==21) {
                		cell.setCellValue((""+getStrDate(InErr.getSold_Date__c())).replaceAll("null", ""));
                	}
                	else if(cN==22) {
                		cell.setCellValue((""+InErr.getInsurer__c()).replaceAll("null", ""));
                	}
                	else if(cN==23) {
                		cell.setCellValue((""+InErr.getProposal_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==24) {
                		cell.setCellValue((""+InErr.getPolicy_Number__c()).replaceAll("null", ""));
                	}
                	else if(cN==25) {
                		cell.setCellValue((""+InErr.getCLAIM_STATUS_c()).replaceAll("null", ""));
                	}
                	else if(cN==26) {
                		cell.setCellValue((""+getStrDate(InErr.getPolicy_Issued_On__c())).replaceAll("null", ""));
                	}
                	else if(cN==27) {
                		cell.setCellValue((""+getStrDate(InErr.getInsurance_Renewal_Date__c())).replaceAll("null", ""));
                	}
                	else if(cN==28) {
                		cell.setCellValue((""+InErr.getIDV__c()).replaceAll("null", ""));
                	}
                	else if(cN==29) {
                		cell.setCellValue((""+InErr.getOwn_Damage__c()).replaceAll("null", ""));
                	}
                	else if(cN==30) {
                		cell.setCellValue((""+InErr.getNCB__c()).replaceAll("null", ""));
                	}
                	else if(cN==31) {
                		cell.setCellValue((""+InErr.getADDON__c()).replaceAll("null", ""));
                	}
                	else if(cN==32) {
                		cell.setCellValue((""+InErr.getTP__c()).replaceAll("null", ""));
                	}
                	else if(cN==33) {
                		cell.setCellValue((""+InErr.getGST__c()).replaceAll("null", ""));
                	}
                	else if(cN==34) {
                		cell.setCellValue((""+InErr.getNET_PREMIUM__c()).replaceAll("null", ""));
                	}
                	else if(cN==35) {
                		cell.setCellValue((""+InErr.getGROSS_Premium__c()).replaceAll("null", ""));
                	}
                	else if(cN==36) {
                		cell.setCellValue((""+InErr.getCustomer_Response__c()).replaceAll("null", ""));
                	}
                	else if(cN==37) {
                		//cell.setCellValue(InErr.replaceAll("null", ""));
                	}
                	else if(cN==38) {
                		cell.setCellValue((""+InErr.getSource__c()).replaceAll("null", ""));
                	}
                	else if(cN==39) {
                		cell.setCellValue((""+InErr.getStatus__c()).replaceAll("null", ""));
                	}
                	else if(cN==40) {
                		cell.setCellValue((""+InErr.getError()).replaceAll("null", ""));
                	}
                	}
                	else if(insTy.equalsIgnoreCase("2")) {
                    if(cN==1) {
                    		cell.setCellValue((""+InErr.getSource__c()).replaceAll("null", ""));
                    }
                	else if(cN==2) {
                		cell.setCellValue((""+InErr.getName()).replaceAll("null", ""));
                	}
                	else if(cN==4) {
                		cell.setCellValue((""+InErr.getCompany_Name__c()).replaceAll("null", ""));
                	}
                	else if(cN==5) {
                		cell.setCellValue((""+InErr.getPhone__c()).replaceAll("null", ""));
                	}
                	else if(cN==6) {
                		cell.setCellValue((""+InErr.getAlternate_Phone_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==7) {
                		cell.setCellValue((""+InErr.getLandline__c()).replaceAll("null", ""));
                	}
                	else if(cN==8) {
                		cell.setCellValue((""+InErr.getEmail__c()).replaceAll("null", ""));
                	}
                	else if(cN==10) {
                		cell.setCellValue((""+InErr.getAddress__c()).replaceAll("null", ""));
                	}
                	else if(cN==11) {
                		cell.setCellValue((""+InErr.getCity__c()).replaceAll("null", ""));
                	}
                	else if(cN==12) {
                		cell.setCellValue((""+InErr.getPinCode__c()).replaceAll("null", ""));
                	}
                	else if(cN==14) {
                		cell.setCellValue((""+InErr.getState__c()).replaceAll("null", ""));
                	}
                	else if(cN==15) {
                		cell.setCellValue((""+InErr.getModel1__c()).replaceAll("null", ""));
                	}
                	else if(cN==16) {
                		cell.setCellValue((""+InErr.getVarient__c()).replaceAll("null", ""));
                	}
                	else if(cN==17) {
                		cell.setCellValue((""+InErr.getReg_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==18) {
                		cell.setCellValue((""+InErr.getChassis_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==19) {
                		cell.setCellValue((""+InErr.getEngine_No__c()).replaceAll("null", ""));
                	}
                	else if(cN==20) {
                		cell.setCellValue((""+InErr.getModel_Year__c()).replaceAll("null", ""));
                	}
                	else if(cN==21) {
                		cell.setCellValue((""+getStrDate(InErr.getSold_Date__c())).replaceAll("null", ""));
                	}
                	else if(cN==32) {
                		cell.setCellValue((""+InErr.getError()).replaceAll("null", ""));
                	}
                	}
                	else if(insTy.equalsIgnoreCase("3")) {
                    	if(cN==1) {
                    		cell.setCellValue((""+InErr.getReg_No__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==2) {
                    		cell.setCellValue((""+InErr.getChassis_No__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==3) {
                    		cell.setCellValue((""+InErr.getEngine_No__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==4) {
                    		cell.setCellValue((""+InErr.getModel1__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==7) {
                    		cell.setCellValue((""+getStrDate(InErr.getSold_Date__c())).replaceAll("null", ""));
                    	}
                    	else if(cN==9) {
                    		cell.setCellValue((""+InErr.getName()).replaceAll("null", ""));
                    	}
                    	else if(cN==12) {
                    		cell.setCellValue((""+InErr.getCompany_Name__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==13) {
                    		cell.setCellValue((""+InErr.getAddress__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==14) {
                    		cell.setCellValue((""+InErr.getCity__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==15) {
                    		cell.setCellValue((""+InErr.getPinCode__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==16) {
                    		cell.setCellValue((""+InErr.getPhone__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==17) {
                    		cell.setCellValue((""+InErr.getEmail__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==18) {
                    		cell.setCellValue((""+InErr.getLandline__c()).replaceAll("null", ""));
                    	}
                    	else if(cN==21) {
                    		cell.setCellValue((""+InErr.getError()).replaceAll("null", ""));
                    	}
                    	}
                	
                	
                	}
            		row1++;
				}
                
                //important to close InputStream
                input_document.close();
                //Open FileOutputStream to write updates
                FileOutputStream output_file =new FileOutputStream(new File(excelFilePath));
                //write changes
                my_xlsx_workbook.write(output_file);
                //close the stream
                output_file.close(); 
		       }
		       catch(Exception e) {
		    	   //System.out.println("Create Err Log=="+e+e.getMessage());
		       }
				
			}
		}


		/**
		 * Method to Return String Date from Calendar date
		 */	
		 public String getStrDate(Calendar cal1) {     
			 String inActiveDate = "null";
			 if(cal1!=null) {
				 Calendar cal = cal1;
				 Date date = cal.getTime();
				 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				 inActiveDate = format1.format(date);
				 inActiveDate = inActiveDate.substring(8)+"-"+inActiveDate.substring(5,7)+"-"+inActiveDate.substring(0,4); 
			 }
			 return inActiveDate;
		    }
		 


			/**
			 * Method to Return String Date from Calendar date
			 */	
			 public String getErMsg(String errorMsg, InsuranceDTO Ic21) {     
				 if( errorMsg.contains("Already present") || errorMsg.contains("Duplicate id specified")) {
					 errorMsg="Entry already exists";
				 }
				 else if(errorMsg.contains("Customer") && errorMsg.contains("Mandatory")) {
					 n112="";
					 InsxlRW w = new InsxlRW();
		             w.insertIntoAccountMaster(Ic21.getName(), Ic21.getPhone__c(), Ic21.getCompany_Name__c() , Ic21.getEmail__c(),Ic21.getCity__c(),Ic21.getState__c(), Ic21.getPinCode__c(),Ic21.getSTD__c(), Ic21.getLandline__c(), Ic21.getAddress__c(),Ic21.getAlternate_Phone_No__c(), Ic21.getCustomer_Type__c());
		             if(n112.length()>0) {
		             errorMsg=n112; }
		             else if(Ic21.getPhone__c().length()<10) {
		            	 errorMsg="Phone No is Required";
		             }
		             else if(Ic21.getPhone__c().length()>13) {
		            	 errorMsg="Phone No is Too large";
		             }
		             else if(!Ic21.getEmail__c().contains("@") || Ic21.getEmail__c().contains("..") || Ic21.getEmail__c().contains(",") || errorMsg.contains("email")) {
		            	 errorMsg="Email is required in correct format";
		             }
		             else if(Ic21.getLandline__c()!=null && !(Ic21.getLandline__c().equalsIgnoreCase("")) && (Ic21.getLandline__c().length()+Ic21.getSTD__c().length())>13) {
		            	 errorMsg="Landline No is Too large";
		             }
		             else if(Ic21.getLandline__c()!=null && !(Ic21.getLandline__c().equalsIgnoreCase("")) && (Ic21.getLandline__c().length()+Ic21.getSTD__c().length())<10) {
		            	 errorMsg="Landline No is too small";
		             }
		             else {
		            	 errorMsg="Some information is missing in Customer";
		             }
				 }
				 String errorMsg1 = errorMsg;				 
				 return errorMsg1;
			    }
			 
			 /**
				 * Method to set UploadSync data
				 */	
			 public void setUpload_Details__c(String Site, String Unit, String Source, Double Prospects, Double Success_Count, Double Error_Count, String Provided_by, String remarks,String fileTitle, String fileName,byte [] fileContentStr) {
				 EnterpriseConnection serviceConn1 =null;
				 try {
					 serviceConn1 = DestinationConnectionUtility.getConnection();
					 if(serviceConn1==null) {
						 return;
					 }
				 }catch(Exception e) {
					 e.printStackTrace();
				  }
				 try {
					 Upload_Details__c Ud=new Upload_Details__c();
					 List<Upload_Details__c> Udl = new ArrayList<Upload_Details__c>();
					 String leadId="";
					 String result="";
					 Ud.setSite__c(Site);
					 Ud.setUnit__c(Unit);
					 Ud.setSource__c(Source);
					 Ud.setTotal_Records__c(Prospects);
					 Ud.setSuccess_Count__c(Success_Count);
					 Ud.setError_Count__c(Error_Count);
					 Ud.setData_Provided_By__c(Provided_by);
					 Ud.setRemarks__c(remarks);
					 Calendar cal  = Calendar.getInstance();
					 Ud.setProvided_On__c(cal);
					 Udl.add(Ud);
					 UpsertResult[] sr = null;
					 try {
						SObject[] upserts = (Udl).stream().toArray(Upload_Details__c[]::new);
						sr = serviceConn1.upsert("id", upserts);
					 }catch (ConnectionException e) {
							e.printStackTrace();
					    	//n112=e.getMessage().toString().replaceAll("\n", "");
						}
						for (int i = 0; i < sr.length; i++) {
							if (sr[i].isSuccess()) {
								//System.out.println("Successfully added contact with id of: " +	sr[i].getId() + ".");
								leadId=sr[i].getId();
								if(Error_Count!=0) {
								result=saveContentVersionData(fileTitle, fileName, fileContentStr);
								if(result.equalsIgnoreCase("Success")) {
								 result=saveContentDocumentLink(leadId);
								}
								}
								result="Success";
							} 
							else {
						    	   
							}
						}
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					 
				 }
		
				public static String saveContentVersionData(String title,String fileName,byte[] fileContent) {
					String result="";
					EnterpriseConnection serviceConn1 =null;
					try {
						 serviceConn1 = DestinationConnectionUtility.getConnection();						 
					 }catch(Exception e) {
						 e.printStackTrace();
					  }
					try {
						ContentVersion contentv= new ContentVersion();
						
						contentv.setTitle(title);		//file name without extension
						
//						if(fileContent!=null) {
//							//String safeString = fileContent.replace("\\", "");
//							contentv.setVersionData(Base64.getDecoder().decode(fileContent));
//						}else {
							contentv.setVersionData(fileContent);
						//}
						
						byte[] data = fileContent;
						
						contentv.setPathOnClient(fileName); //fileName with extension
						SaveResult[] sr = null;
						try {
							sr = serviceConn1.create(new SObject[] {contentv});
						} catch (ConnectionException e) {
							//System.out.println("serviceConn1.create(obj) :"+e);
							e.printStackTrace();
						}
						for (int i = 0; i < sr.length; i++) {
							if (sr[i].isSuccess()) {
								//System.out.println("Successfully created content version with id of: " + sr[i].getId() + ".");
								result="Success";
							} else if("DUPLICATES_DETECTED".equals(sr[i].getErrors()[0].getStatusCode().toString())) {
								//System.out.println("Error insert data in contentversion object: " + sr[i].getErrors()[0].getStatusCode());
								result="Duplicate record";
							}else {
								//System.out.println("Error insert data in contentversion object " + sr[i].getErrors()[0].getMessage());
								result="Error please contact administrator for log.";
							}
						}
					}catch(Exception e) {
						 //System.out.println("Exception in saveContentversionData : "+e);
					}
					
					
					return result;
				}
				public static String saveContentDocumentLink(String leadId) {
					String result="";
					EnterpriseConnection serviceConn1 =null;
					try {
						 serviceConn1 = DestinationConnectionUtility.getConnection();						 
					 }catch(Exception e) {
						 e.printStackTrace();
					  }
					try {
						ContentDocumentLink contentDocLink = new ContentDocumentLink();
						String contentDocumnetId=getContentDocumentId();
						contentDocLink.setShareType("V");
						contentDocLink.setVisibility("AllUsers");
						contentDocLink.setContentDocumentId(contentDocumnetId);
						contentDocLink.setLinkedEntityId(leadId);
						SaveResult[] sr = null;
						try {
							sr = serviceConn1.create(new SObject[] {contentDocLink});
						} catch (ConnectionException e) {
							//System.out.println("serviceConn1.create(obj) :"+e);
							e.printStackTrace();
						}
						for (int i = 0; i < sr.length; i++) {
							if (sr[i].isSuccess()) {
								//System.out.println("Successfully created contentDocumentLink with id of: " + sr[i].getId() + ".");
								result="Success";
							} else if("DUPLICATES_DETECTED".equals(sr[i].getErrors()[0].getStatusCode().toString())) {
								//System.out.println("Error insert data in contentDocumentLink object: " +sr[i].getErrors()[0].getStatusCode());
								result="Duplicate record";
							}else {
								//System.out.println("Error insert data in contentDocumentLink object " +sr[i].getErrors()[0].getMessage());
								result="Error please contact administrator for log.";
							}
						}
					}catch (Exception e) {
						//System.out.println("Exception in saveContentversionData : "+e);
					}
					
					
					return result;
				}
				
				public static String getContentDocumentId() {
					 String contentDocumentId="";
					 EnterpriseConnection serviceConn1 =null;
						try {
							 serviceConn1 = DestinationConnectionUtility.getConnection();						 
						 }catch(Exception e) {
							 e.printStackTrace();
						  }
				      String soqlQuery = "SELECT Id, Title, LatestPublishedVersionId, CreatedDate FROM ContentDocument order by CreatedDate desc limit 1";
				      try {
				         QueryResult qr = serviceConn1.query(soqlQuery);
				         boolean done = false;

				         if (qr.getSize() > 0) {
				            //System.out.println("\nLogged-in user can see "+ qr.getRecords().length + " ContentDocument records.");

				            while (!done) {
				               SObject[] records = qr.getRecords();
				               for (int i = 0; i < records.length; ++i) {
				            	   ContentDocument con = (ContentDocument) records[records.length-1];
				                  contentDocumentId = con.getId();
				                  //System.out.println(contentDocumentId);

				               }
				               if (qr.isDone()) {
					                  done = true;
					               } else {
					                  qr = serviceConn1.queryMore(qr.getQueryLocator());
					               }
				               
				            }
				         } else {
				            //System.out.println("No records found.");
				         }
				      } catch (ConnectionException ce) {
				         ce.printStackTrace();
				      }
				      return contentDocumentId;
				   }

				public String getLivePath() throws FileNotFoundException, UnsupportedEncodingException
				{
					String path = getClass().getResource("/").getPath();
					path=path.substring(0, path.indexOf("WEB-INF"))+"\\resource";
					if (path.contains("%20")) {
						path = URLDecoder.decode(path, "utf-8");
						path = new File(path).getPath();
					}
					return path;
				}
}
