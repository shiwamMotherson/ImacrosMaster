package Imacro;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TVPSalesMacro {
	public static void main(String args[]) {
		 String location = "D:\\SmileExcelsheet\\E01\\SmileExcel";
		 FileInputStream fis = new FileInputStream(new File(location));
		 Path path = Paths.get(location);
		   XSSFWorkbook workbook = new XSSFWorkbook(fis);
		   XSSFWorkbook spreadsheet = workbook.getSheetAt(0);
	      System.out.println("spreadsheet created");
	      TVPSalesMacro tv=new TVPSalesMacro();
	      tv.InvoiceListRead(spreadsheet);
		
	}
		//iMacro Code to download excel sheet
	

	
    // Method to read data from TVP excel sheet

       
public List <String> InvoiceListRead(XSSFSheet spreadsheet) {
    List <String> Ins=new ArrayList<String>();
    Iterator < Row >  rowIterator = spreadsheet.iterator();
    String In = null;
    XSSFRow row;
    //String p="";
    while (rowIterator.hasNext()) {
        In = "";
        row = (XSSFRow) rowIterator.next();
        try {
        if(row.getRowNum()>0) {
            In = (cellReturn(row.getCell(6)));
           
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(In.length()>1) {
            Ins.add(In);
                            
        }
    }
    return Ins;
    }
 
}


//code to download the tvp sales invoices



}