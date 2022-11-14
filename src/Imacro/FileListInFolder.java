package Imacro;

import java.io.File;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FileListInFolder {
	static String fileDate="13072022";
	static String Cat="BS";
	public static void main(String args[]){
		
		Instant now = Instant.now();
		DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.now());
		 String[] pathnames;
		 List<String> list1=new ArrayList<String>(); 
		Instant yesterday = now.minus(0,ChronoUnit.DAYS);
		System.out.println(fileDate);
		//File directoryGS=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\"+Cat+"");
		File directoryGS=new File("D:\\F04Invoices");
		//int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\"+Cat+"").list().length;
		int totalCount=new File("D:\\F04Invoices").list().length;
		 pathnames = directoryGS.list();
			System.out.println("Total number "+Cat+ "="+totalCount);
        // For each pathname in the pathnames array
			System.out.println();
        for (String pathname : pathnames) {
            // Print the names of files and directories
           System.out.println(pathname.substring(0,11));
        	//list1.add(pathname.substring(0,11));
        	
        }
       // System.out.println(list1);
      	
      
}
}
