package com.triyasoft.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookStyleUtil {

	public static void decorateHeaderCell(HSSFWorkbook workbook) {}

	public static void decorateBuyerSummaryHeaderCell(XSSFWorkbook workbook, XSSFSheet sheet, Cell headerCellUuid , int headerCharwidth , int columnNumber) {

		 CellStyle style = workbook.createCellStyle();
 	    style.setFillBackgroundColor(IndexedColors.BROWN.getIndex());
 	    style.setFillPattern(CellStyle.BIG_SPOTS);

 	    
 	    XSSFFont font = workbook.createFont();
 	    font.setColor(HSSFColor.WHITE.index);
 	    font.setBold(true);
 	    font.setItalic(true);
 	    CellUtil.setAlignment(headerCellUuid, workbook, CellStyle.ALIGN_CENTER);

 	    style.setFont(font);
 	   
 	    
 	    sheet.setColumnWidth(columnNumber, headerCharwidth*256);
 	    headerCellUuid.setCellStyle(style);

		
	
		
	}
	
	public static void decorateBuyerSummaryFooterCell(XSSFWorkbook workbook, XSSFSheet sheet, Cell headerCellUuid , int headerCharwidth , int columnNumber, boolean alightRightcenter)
		{
		


			CellStyle style = workbook.createCellStyle();
	 	    style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
	 	    style.setFillPattern(CellStyle.BIG_SPOTS);

	 	    XSSFFont font = workbook.createFont();
	 	    font.setColor(HSSFColor.WHITE.index);
	 	    font.setBold(true);
	 	    CellUtil.setAlignment(headerCellUuid, workbook, CellStyle.ALIGN_CENTER);

	 	    style.setFont(font);
	 	    
	 	    if(alightRightcenter) {
	 	    style.setAlignment(HorizontalAlignment.RIGHT);
	 	    style.setVerticalAlignment(VerticalAlignment.CENTER);
	 	    }

	 	    
	 	    headerCellUuid.setCellStyle(style);

			
			
		}
		
	
		
	
		public static void decorateSourceSummaryHeaderCell(XSSFWorkbook workbook, XSSFSheet sheet, Cell headerCellUuid , int headerCharwidth , int columnNumber) {

			 CellStyle style = workbook.createCellStyle();
	 	    style.setFillBackgroundColor(IndexedColors.BROWN.getIndex());
	 	    style.setFillPattern(CellStyle.BIG_SPOTS);

	 	    
	 	    
	 	    XSSFFont font = workbook.createFont();
	 	    font.setColor(HSSFColor.WHITE.index);
	 	    font.setBold(true);
	 	    font.setItalic(true);
	 	    CellUtil.setAlignment(headerCellUuid, workbook, CellStyle.ALIGN_CENTER);

	 	    style.setFont(font);
	 	   
	 	    //style.setAlignment(HorizontalAlignment.RIGHT);
	 	    //style.setVerticalAlignment(VerticalAlignment.CENTER);
	 	    
	 	    

	 	    if(headerCharwidth >0)
	 	 	    sheet.setColumnWidth(columnNumber, headerCharwidth*256);

	 	    
	 	    headerCellUuid.setCellStyle(style);

				
		}
		
		
		public static void decorateSourceSummaryFooterCell(XSSFWorkbook workbook, XSSFSheet sheet, Cell headerCellUuid , int headerCharwidth , int columnNumber)
		{
		


			CellStyle style = workbook.createCellStyle();
	 	    style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
	 	    style.setFillPattern(CellStyle.BIG_SPOTS);

	 	    
	 	    XSSFFont font = workbook.createFont();
	 	    font.setColor(HSSFColor.WHITE.index);
	 	    font.setBold(true);
	 	    CellUtil.setAlignment(headerCellUuid, workbook, CellStyle.ALIGN_CENTER);

	 	    style.setFont(font);
	 	//    style.setAlignment(HorizontalAlignment.RIGHT);
	 	 //   style.setVerticalAlignment(VerticalAlignment.CENTER);

	 	    
	 	    headerCellUuid.setCellStyle(style);

			
			
		}
		
		
		public static void decorateGeneralCell(XSSFWorkbook workbook,  Cell cell ) {
			
		
			CellStyle style = workbook.createCellStyle();
	 	    style.setAlignment(HorizontalAlignment.RIGHT);
	 	    style.setVerticalAlignment(VerticalAlignment.CENTER);
	 	    cell.setCellStyle(style);
	
		

		}
	}





