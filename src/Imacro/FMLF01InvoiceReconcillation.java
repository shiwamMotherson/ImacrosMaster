package Imacro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;
import com.sforce.ws.ConnectionException;
public class FMLF01InvoiceReconcillation {
	boolean isLoginFlag =false;
	ActiveXComponent iimCSReportDwnRead = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	
	public static void main(String[] args) {
		FMLF01InvoiceReconcillation re= new FMLF01InvoiceReconcillation();
		Instant now = Instant.now();
		Instant yesterday = now.minus(7, ChronoUnit.DAYS);//always less than yesterday3
		Instant yesterday3 = now.minus(14, ChronoUnit.DAYS);
		DBTransactions dbt = new DBTransactions();
		String datefilterto=imformat1(yesterday.toString(),3);
		String datefilterFrom=imformat1(yesterday3.toString(),3);
		System.out.println(now.toString().substring(8,10));
		String now1=now.toString().substring(8,10);
		//SendAttachment sdm=new SendAttachment();
		File f2=new File("D:\\CSReportF01");
		try {
		//deleteFolder(f2);
		}catch(Exception e) {}
		//re.runDownload2("F01","2202054", "Fmpl@20221", datefilterFrom, datefilterto,"D:\\CSReportF01\\", "CS1GS.xls","D:\\CSReportF01\\", "CS2BP.xls",  yesterday.toString(),yesterday3.toString());
		re.runDownload("F01","2202054", "Fmpl@20221", datefilterFrom, datefilterto,"D:\\CSReportF01\\", "CS1GS.xls","D:\\CSReportF01\\", "CS2BP.xls",  yesterday.toString(),yesterday3.toString());
		
	}

	/**
	 * Method to download CS Report
	 */	
	public  void runDownload(String ut, String Username, String Pwd, String d1, String d2,String folderLoc1, String fileName1,String folderLoc2, String fileName2, String yesterday,String yesterday2)
	{
		Instant now = Instant.now();
		 Map<String, String> BPList = new HashMap<String, String>();
		 Map<String, String> GSList = new HashMap<String, String>();
		 Map<String, String> invL3 = new HashMap<String, String>();
		System.out.println("Start 01:"+now.toString());    
		File f = new File(folderLoc1+fileName1);
	
		now = Instant.now();
		System.out.println("Start 02:"+now.toString());   
		if(f.exists()) {
			now = Instant.now();
		GSList=ROXLSSheetReadGSF01(folderLoc1+fileName1);
		System.out.println("F0111111111111111111111111111---------GS Excel");
		System.out.println("GS List = "+GSList.size());
		System.out.println(GSList);
		}else {}
		now = Instant.now();
		System.out.println("Start 04:"+now.toString());   
	

		now = Instant.now();
		System.out.println("Start 05:"+now.toString());   
		if(f.exists()) {
			System.out.println("F0111111111111111111111111111---------BP Excel");
			now = Instant.now();
			BPList=ROXLSSheetReadBPF01(folderLoc2+fileName2);	
			System.out.println(BPList+" \n"+"Total Size="+BPList.size());
			}
		else {}
		now = Instant.now();
		System.out.println("Start 07:"+now.toString());
		DBTransactionCTDMSInvoices dbt2=new DBTransactionCTDMSInvoices();
		invL3=dbt2.getInvoiceNameASFMSSW("F01",yesterday2.toString(),yesterday.toString());
		System.out.println("FMSSWAS\n"+invL3);
		//iimCSReportDwnRead.invoke("iimExit");
		 Map<String, String> MergeList = new HashMap<String, String>();
		 Map<String, String> MergeListFinal = new HashMap<String, String>();
		MergeList.putAll(GSList);
		MergeList.putAll(BPList);
		MergeList.putAll(invL3);
		System.out.println(invL3);
		System.out.println(MergeList+"     \n"+"Total Size="+MergeList.size());
		Map<String,String>invL2=dbt2.getInvoiceNameRCFlowlog("F01",yesterday2.toString(),yesterday.toString());
		Map<String,String>invL2a=dbt2.getInvoiceNameRCFlowlogTotal("F01",yesterday2.toString(),yesterday.toString());//flowlog Invoice which is not posted by any reason
		System.out.println("InvoiceList in Flowlog table \n"+invL2);
		for(String a:MergeList.keySet()) {
			if(!invL2a.containsKey(a)) {
				System.out.println(a+ "     "+"not in the list"+MergeList.get(a));
				MergeListFinal.put(a,MergeList.get(a));
			} else {System.out.println("No Mismatch");};
		}
		MergeListFinal.putAll(invL2);
		System.out.println(MergeListFinal);
		System.out.println("Before Sorting:");  
		Set set = MergeListFinal.entrySet();  
		Iterator iterator = set.iterator();  
		while(iterator.hasNext())   
		{  
		Map.Entry map = (Map.Entry)iterator.next();  
		System.out.println("Invoice:  "+map.getKey()+"     Date:   "+map.getValue());  
		}  
		Map<String, String> map = sortValues(MergeListFinal);   
		System.out.println("\n");  
		System.out.println("After Sorting:");  
		Set set2 = map.entrySet();  
		Iterator iterator2 = set2.iterator();  
		while(iterator2.hasNext())   
		{  
		Map.Entry me2 = (Map.Entry)iterator2.next();  
		System.out.println("Invoice:  "+me2.getKey()+"     Date:   "+me2.getValue());  
		//System.out.println(map);
		}  
		CreateErrLog(map,"F01");
		File gsFile= new File(folderLoc1+fileName1);
		File bpFile= new File(folderLoc2+fileName2);
		if(gsFile.exists() && bpFile.exists()) {
		SendAttachment sdm1=new SendAttachment();
		sdm1.sendmailF01("F01"+" from  "+yesterday2.toString().substring(0,10)+" to  "+yesterday.toString().substring(0, 10)+" .");
		}else {}
	}

	public  void runDownload2(String ut, String Username, String Pwd, String d1, String d2,String folderLoc1, String fileName1,String folderLoc2, String fileName2, String yesterday,String yesterday2)
	{
		Instant now = Instant.now();  
		File f = new File(folderLoc1+fileName1);
	     iimCSReportDwnRead = new ActiveXComponent("imacros");
		iimCSReportDwnRead.invoke("iimInit");
		System.out.println("Calling iimPlay");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB CLOSEALLOTHERS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:'SET !PLAYBACKDELAY 1.73");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&parammessage=MSC20002AINF");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+Username);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+Pwd);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=VALUE:Login");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=11");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
		//GS
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		if(!urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2")) {
		
		}
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workshopType2 CONTENT=%GS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=2 TYPE=INPUT:RADIO ATTR=NAME:period2");
		//iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:forDate2 CONTENT="+d1);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateFrom2 CONTENT="+d1);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateTo2 CONTENT="+d2);
		//TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateFrom2 CONTENT=01032022
		 //TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateTo2 CONTENT=07032022
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc1+" FILE="+fileName1+" WAIT=YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		}
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
			if(!f.exists()) {
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc1+" FILE="+fileName1+" WAIT=YES");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");				
			}
		}
		
		now = Instant.now();
		System.out.println("Start 04:"+now.toString());   
	
		iimCSReportDwnRead.invoke("iimExit");		
		iimCSReportDwnRead = new ActiveXComponent("imacros");
		iimCSReportDwnRead.invoke("iimInit");
		System.out.println("Calling iimPlay");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB CLOSEALLOTHERS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:'SET !PLAYBACKDELAY 1.73");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&parammessage=MSC20002AINF");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+Username);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+Pwd);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=VALUE:Login");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=11");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
		//BP
		f = new File(folderLoc2+fileName2);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		if(!urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2")) {
				
			//unitDownloader(yesterday);
		}
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workshopType2 CONTENT=%BP");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=2 TYPE=INPUT:RADIO ATTR=NAME:period2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateFrom2 CONTENT="+d1);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateTo2 CONTENT="+d2);
		//iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateFrom2 CONTENT=01032022");
		//iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:dateTo2 CONTENT=07032022");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc2+" FILE="+fileName2+" WAIT=YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		}
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
			if(!f.exists()) {
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc2+" FILE="+fileName2+" WAIT=YES");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");				
			}
		}
	
		iimCSReportDwnRead.invoke("iimExit");
		
		File gsFile= new File(folderLoc1+fileName1);
		File bpFile= new File(folderLoc2+fileName2);
		
		 int dLoop=0;
		    while(!(gsFile.exists() && bpFile.exists()) && dLoop<4) {
		    	dLoop++;
		    	
		    	runDownload2(ut, Username,  Pwd, d1,  d2, folderLoc1,  fileName1, folderLoc2,  fileName2,  yesterday, yesterday2);
		    }
		
		if((gsFile.exists() && bpFile.exists()) ) {
			runDownload(ut, Username,  Pwd, d1,  d2, folderLoc1,  fileName1, folderLoc2,  fileName2,  yesterday, yesterday2);
		}else {
			
		}
	
	}
	
	public void CreateErrLog(Map<String, String> mergeListFinal,String Unit) {
		String exPath="D:\\ReconcilationRepor";
		String excelFilePath =""+exPath;
		String excelFilePathN ="";
		FileSystem system = FileSystems.getDefault();
		excelFilePathN=excelFilePath.replaceAll(".xlsx", "")+".xlsx";
		Path original = system.getPath(excelFilePathN);
		excelFilePathN=excelFilePath.replaceAll(".xlsx", "")+"t.xlsx";
		Path target = system.getPath(excelFilePathN);
		try {
		// Throws an exception if the original file is not found.
		Files.copy(original, target, StandardCopyOption.REPLACE_EXISTING);
		excelFilePath = excelFilePathN;
		} catch (IOException ex) {
		System.out.println("ERROR");
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
		System.out.println(totNum+"totnum");
		for(int i1=row1+1;i1<=totNum;i1++) {
		if (my_worksheet.getRow(i1) != null) {
		my_worksheet.removeRow(my_worksheet.getRow(i1));
		}
		}
		XSSFRow sheetrow =null;
		int rownum=0;
		//for(InsErrorDTO ld:EqErrl) {
		for(String a:mergeListFinal.keySet()) {
		rownum++;
		sheetrow = my_worksheet.getRow(row1);
		if(sheetrow == null){
		sheetrow = my_worksheet.createRow(row1);
		}
		// Access the cell first to update the value
		for(int cN=0;cN<9;cN++) {
		cell = sheetrow.getCell(cN);
		if(cell == null){
		cell = sheetrow.createCell(cN);
		}
		//if(insTy.equalsIgnoreCase("1")) {
		if(cN==0) {
		cell.setCellValue((""+rownum).replaceAll("null", ""));
		}
		else if(cN==1) {
		cell.setCellValue((""+Unit).replaceAll("null", ""));
		}
		else if(cN==2) {
		cell.setCellValue((""+mergeListFinal.get(a).substring(0,10)).replaceAll("null", ""));
		}
		else if(cN==3) {
		cell.setCellValue((""+a).replaceAll("null", ""));
		}
		else if(cN==4) {
		cell.setCellValue((""+mergeListFinal.get(a).toString().substring(10).trim()).replaceAll("null", ""));
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
		System.out.println("Create Err Log=="+e+e.getMessage());
		}
		}


			/**
			 * Method to Return cell value
			 */	
			public String cellReturn(Cell cell) {
				String s="";
				try {
				switch (cell.getCellType()) {
			    case NUMERIC:
			       s = ""+cell.getNumericCellValue();
			       break;
			    
			    case STRING:
			       s = cell.getStringCellValue();
			       break;
				    
				case BLANK:
				       s = "";
				       break;
			       
			    default:
			    	   break;
			 }
				}catch(Exception e) {
					//e.printStackTrace();
					}
				
				return s;		
			}
			/**
			 * Method to return the current Tab url for iMacros
			 */	
			public String urlCheck() {
				String s="";
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iimCSReportDwnRead.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
				s=iimCSReportDwnRead.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
				return s;
			}
			
			public Map<String, String> ROXLSSheetReadBPF01(String location) {
			//List <String> ROl=new ArrayList<String>();
			 Map<String, String> ROl = new HashMap<String, String>();
			try {
			FileInputStream fis = new FileInputStream(new File(location));
			Path path = Paths.get(location);
			if (Files.exists(path)) {}
			else {
			//Log file not existing exception
			System.out.println("File not exist");
			}
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet spreadsheet = workbook.getSheetAt(0);
			Iterator < Row > rowIterator = spreadsheet.iterator();
			HSSFRow row;
			while (rowIterator.hasNext()) {
			
			row = (HSSFRow) rowIterator.next();
			
			try {
				if(row.getRowNum()>0) {
					if(cellReturn(row.getCell(21)).length()>5) {
					try {
						if(!ROl.containsKey(""+cellReturn(row.getCell(21)))){
							if(row.getCell(21).toString().substring(0,3).contains("INY")) {
							ROl.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"BS");
							}else if(row.getCell(21).toString().substring(0,3).contains("TXY")) {
								ROl.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"BP");
							}else {}
						}
					} catch (Exception e) {e.printStackTrace();}
					}
				}
			} catch (Exception e) {e.printStackTrace();}
			}
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
			System.out.println(ROl);
			return ROl;
			}
			public Map<String, String> ROXLSSheetReadGSF01(String location) {
			//List <String> ROl=new ArrayList<String>();
			 Map<String, String> ROl1;
			 ROl1 = new HashMap<String, String>();
			try {
			FileInputStream fis = new FileInputStream(new File(location));
			Path path = Paths.get(location);
			if (Files.exists(path)) {}
			else {
			//Log file not existing exception
			System.out.println("File not exist");
			}
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet spreadsheet = workbook.getSheetAt(0);
			Iterator < Row > rowIterator = spreadsheet.iterator();
			HSSFRow row;
			while (rowIterator.hasNext()) {
			
			row = (HSSFRow) rowIterator.next();
			
			try {
				if(row.getRowNum()>0) {
					if(cellReturn(row.getCell(21)).length()>5) {
					try {
						if(!ROl1.containsKey(""+cellReturn(row.getCell(21)))){
							//System.out.println(row.getCell(21));
							if(row.getCell(21).toString().substring(0,3).contains("SSY")) {
							ROl1.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"SSB");
							}else if(row.getCell(21).toString().substring(0,3).contains("TXY")) {
								ROl1.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"GS");
								
								//ROl.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"BS");
							//	System.out.println(ROl1);
							}else if(row.getCell(21).toString().substring(0,3).contains("BSY")) {
								ROl1.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"BSB");
							}else if(row.getCell(21).toString().substring(0,3).contains("EWY")) {
								ROl1.put(""+cellReturn(row.getCell(21)),imformat1(""+cellReturn(row.getCell(22)).trim(),6)+" "+"EW");
							}else {}
						}
					} catch (Exception e) {e.printStackTrace();}
					}
				}
			} catch (Exception e) {e.printStackTrace();}
			}
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
			System.out.println("===============================================================================================================================h");
			ROl1.entrySet().forEach(System.out::println);
			//System.out.println(ROl1+"/nMukesh");
			return ROl1;
			}
			
			public void unitDownloader(String yesterday){
				DBTransactions dbt = new DBTransactions();
				if(unitAttempt<10) {
					unitAttempt++;
					System.out.println("unit Attempt = "+unitAttempt);
					//F01 
				//	UnitDTO ut= dbt.getUnitConfig("F01", "GS", "Invoice");
					//ToyotaRODownload(yesterday, ut);
				    
					//E02
					//ut= dbt.getUnitConfig("E02", "GS", "Invoice");
					//ToyotaRODownload(yesterday, ut);
				}
				
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
			
		public void ToyotaRODownload(String yesterday, UnitDTO ut){
			
			//runDownload(ut,ut.getApplicationLoginID(), ut.getApplicationPassword(),imformat(yesterday1, 3), loc1, fn1, loc2, fn1, yesterday);
			
		}	

		//method to sort values  
		private static HashMap sortValues(Map<String, String> mergeListFinal)   
		{   
		List list = new LinkedList(mergeListFinal.entrySet());  
		//Custom Comparator  
		Collections.sort(list, new Comparator()   
		{  
		public int compare(Object o1, Object o2)   
		{  
		return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());  
		}  
		});  
		//copying the sorted list in HashMap to preserve the iteration order  
		HashMap sortedHashMap = new LinkedHashMap();  
		for (Iterator it = list.iterator(); it.hasNext();)   
		{  
		 Map.Entry entry = (Map.Entry) it.next();  
		sortedHashMap.put(entry.getKey(), entry.getValue());  
		}   
		return sortedHashMap;  
		}  



		public static String imformat1(String date, int f) {
			String d="";
			if(date.length()>=9) {
				if(f==1) {
					d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
				}
				else if(f==2) {
					d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
				}
				else if(f==3) {
					d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
				}
				else if(f==6) {
					d=date.substring(6, 10)+"-"+date.substring(0,2)+"-"+date.substring(3, 5);
				}
			}
			if(date.length()>8 && f==4) {
				d=date.substring(5, 7)+""+date.substring(0, 4);
			}
			else if(date.length()>8 && f==5) {
				d=""+date.substring(0, 4)+date.substring(5, 7);
			}
		    
			return d;
			}

		public String imformat(String date, int f) {
			String d="";
			if(date.length()>=9) {
				if(f==1) {
					d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
				}
				else if(f==2) {
					d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
				}
				else if(f==3) {
					d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
				}
			}
			if(date.length()>8 && f==4) {
				d=date.substring(5, 7)+""+date.substring(0, 4);
			}
			else if(date.length()>8 && f==5) {
				d=""+date.substring(0, 4)+date.substring(5, 7);
			}
		    
			return d;
			}

		/**
		 * Method to check if file has been successfully downloaded
		 */	
			public boolean checkStatus( String location) {
				System.out.println("called 1");
				boolean status= false;
				Path path = Paths.get(location);
				DBTransactions dbt2 = new DBTransactions();
				if (Files.exists(path)) {
					File file = new File(location);
					System.out.println(file.length());
					if(file.length()>102) {
					System.out.println("called 2 "+location);
					status=true;
					}			
				}
				System.out.println(status);
				return status;
			}


			 
		}
