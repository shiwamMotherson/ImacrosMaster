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
import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;
public class BatchRunner {

	
	public static void main(String args[]){
	
		try {
			System.out.println("Batch FIle Started");   
			String[] command = {"cmd.exe", "/C", "Start", "D:\\ETInvoiceDownload.bat"};//ETInvoiceDownload.bat
			System.out.println("Batch Stage 0");
			Process p = Runtime.getRuntime().exec(command);
			System.out.println("Batch Stage 1");
			try {
				System.out.println("Batch Stage 3");
				int exitVal = p.waitFor();
				System.out.println("Batch last stage");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
	}
}