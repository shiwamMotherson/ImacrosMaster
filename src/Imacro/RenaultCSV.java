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

public class RenaultCSV {
	public static void main(String args[]) {
		RenaultCSV rcsv=new RenaultCSV();
		Instant now = Instant.now();
		//Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		Instant yesterday = now.plus(60, ChronoUnit.DAYS);
		Instant yesterdayoneyearback = yesterday.minus(365, ChronoUnit.DAYS);
		Instant yesterdayoneyearback1 = yesterdayoneyearback.minus(20, ChronoUnit.DAYS);
		Instant yesterdayoneyearback2 = yesterdayoneyearback.plus(10, ChronoUnit.DAYS);
		System.out.println(yesterday+"****"+yesterdayoneyearback+"****"+yesterdayoneyearback1+"****"+yesterdayoneyearback2);
		String yesterday1=imformatMonth(now.toString(),1);
		System.out.println(yesterday1);
		//String filepath="D:\\iMacroDeployment\\invoicedownloader\\RR.csv";
		String filepath="D:\\RENO\\RR.csv";
		DBTransactions dbtNrfsi=new DBTransactions();
		rcsv.writeDataLineByLine(filepath,yesterday.toString().substring(0,10),yesterdayoneyearback.toString().substring(0,10),yesterdayoneyearback1.toString().substring(0,10),yesterdayoneyearback2.toString().substring(0,10));

 /*     try {
                String[] command = {"cmd.exe", "/C", "Start", "D:\\renaultbatfile.bat"};
                 Process p = Runtime.getRuntime().exec(command);
                } catch (IOException ex) {
	            System.out.println(ex);
            }
     try {
    Thread.sleep(30000);
    } catch (InterruptedException ie) {
    Thread.currentThread().interrupt();
    }*/
		
	    try {
        String[] command = {"cmd.exe", "/C", "Start", "D:\\renaultbatfilePolicyDetails.bat"};
         Process p = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
        System.out.println(ex);
    }
try {
Thread.sleep(30000);
} catch (InterruptedException ie) {
Thread.currentThread().interrupt();
}
//post csv data to salesforce
        String loc="D:\\Renault\\RenewalPolicyList.csv";
         //String loc="D:\\Renault\\RenewalPolicyList_21-7-2021.csv";
         System.out.println(loc);
  /*       xlRW rw=new xlRW();
      try {
      rw.csvRead("", loc, 4,"Dera - Prime Auto CA","Renault","Portal - Renault NRFS","RenewalPolicyList_"+yesterday1+".csv");
      File myObj = new File("D:\\Renault\\RenewalPolicyList.csv");
	  if (myObj.delete()) { 
       System.out.println("Deleted the file: " + myObj.getName());
      } else {
       System.out.println("Failed to delete the file.");
      } 
	 } catch (Exception e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
    }*/
  }
	public  void writeDataLineByLine(String filePath,String yesterday,String yesterdayoneyearback ,String yesterdayoneyearback1 ,String yesterdayoneyearback2)
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
	        String[] data1 = { imformat(yesterday,6), imformat(yesterday,6),imformat(yesterdayoneyearback1,6),imformat(yesterdayoneyearback2,6) };
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
				month="Jan";			
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
	public static String imformatMonth(String date, int t) {
		String d1="";
		if(t==1) {
			String month=(date.substring(5,7));
			System.out.println(month);
			int month1=Integer.parseInt(month);
			if(month1==1) {
				month="1";			
			}else if(month1==2) {
				month="2";
			}else if(month1==3) {
				month="3";
			}else if(month1==4) {
				month="4";
			}else if(month1==5) {
				month="5";
			}else if(month1==6) {
				month="6";
			}else if(month1==7) {
				month="7";
			}else if(month1==8) {
				month="8";
			}else if(month1==9) {
				month="9";
			}else if(month1==10) {
				month="10";
			}else if(month1==11) {
				month="11";
			}else if(month1==12) {
				month="12";
			}
	d1=date.substring(8,10)+"-"+month+"-"+date.substring(0,4);
	System.out.println(d1);
	}
		return d1;
	}
	
}
