package Imacro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVWriter;
//import com.opencsv.CSVWriter;
import com.sforce.async.CSVReader;

public class RenaultCSV123 {
	//int i=2;
	public static void main(String args[]) {
		RenaultCSV123 rcsv=new RenaultCSV123();
		Instant now = Instant.now();
		 //  YearMonth.now().minusMonths( i ).atEndOfMonth();
		Instant yesterday = now.minus(19, ChronoUnit.DAYS);//31 RUNNING FOR TEST
		System.out.println(yesterday);
		String yesterday1=now.toString().substring(8, 10)+"-"+now.toString().substring(5, 7)+"-"+now.toString().substring(0, 4);
		System.out.println(yesterday1);
		String filepath="D:/RR.csv";
rcsv.writeDataLineByLine(filepath,yesterday.toString().substring(0,10));

try {
String[] command = {"cmd.exe", "/C", "Start", "D:\\test.bat"};
Process p = Runtime.getRuntime().exec(command);
} catch (IOException ex) {
	System.out.println(ex);
}

//post csv data to salesforce
String loc="D:\\Renault\\RenewalPolicyList_"+yesterday1+".csv";
System.out.println(loc);
/*xlRW rw=new xlRW();
try {
rw.csvRead("", loc, 4);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}*/
	    }
	public  void writeDataLineByLine(String filePath,String yesterday)
	{
	    // first create file object for file placed at location
	    // specified by filepath
	    File file = new File(filePath);
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(file);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        // add data to csv
	        String[] data1 = { imformat(yesterday,6), imformat(yesterday,6), };
	       // String[] data1 = { imformat(yesterday,6), imformat(yesterday,6), };
	        writer.writeNext(data1);
	       
	        // closing writer connection
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	
	}
	
	public static String imformat(String date, int f) {
		String d="";
		if(f==1) {
			d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
		}
		else if(f==2) {
			d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
			System.out.println("2nd"+d);
		}
		else if(f==3) {
			d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		}else if(f==4) {
			d=date.substring(5, 7)+""+date.substring(0, 4);
		}else if(f==5) {
		
		}else if(f==6) {
			String month=(date.substring(5,7));
			System.out.println(month);
			int month1=Integer.parseInt(month);
			if(month1==1) {
				month="";			
			}else if(month1==2) {
				month="Feb";
			}else if(month1==3) {
				month="Mar";
			}else if(month1==4) {
				month="Apr";
			}else if(month1==5) {
				month="May";
			}else if(month1==6) {
				month="Jun";
			}else if(month1==7) {
				month="Jul";
			}else if(month1==8) {
				month="Aug";
			}else if(month1==9) {
				month="Sep";
			}else if(month1==10) {
				month="Oct";
			}else if(month1==11) {
				month="Nov";
			}else if(month1==12) {
				month="Dec";
			}
	d=date.substring(8)+"/"+month+"/"+date.substring(0,4);
	System.out.println(d);
	}
		return d;
		}
}
