package com.zz.bms.util.poi;

import com.zz.bms.util.base.data.DateKit;
import com.zz.bms.util.poi.cell.CellBuild;
import com.zz.bms.util.poi.cell.CellExport;
import com.zz.bms.util.poi.enums.EnumXlsFormat;
import com.zz.bms.util.poi.vo.Column;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
public abstract class AbstractXlsExport<T> extends AbstractXlsStyle {

	public static int defaultColumnWidth = 15;
	public static int titleColumnHeight = 400;


	CellExport cellExport = CellBuild.buildCellExport();

	protected Workbook workbook;

	/**
	 * 当前Sheet
	 */
	protected Sheet sheet;

	/**
	 * 当前行
	 */
	protected Row row;

	/**
	 * 头信息
	 */
	protected Row[] headerRow;

	/**
	 * 标题信息
	 */
	protected Row[] titleRow;

	/**
	 * 是否自定义标题信息
	 * 例如  标题的信息是从其它excel获取的
	 */
	public boolean isCustomTitleInfo = false ;

	public boolean isWriteTitle(){
		return true;
	}



	public String getNumberName(){
		return "序号";
	}





	public void createSheet(T t ) {
		createSheet(t , defaultColumnWidth);
	}


	public void createSheet(T t  , int columnWidth) {
		createSheet(t ,columnWidth , 0 , 1);
	}


	public void createSheet(T t,  int cols, int rows) {
		createSheet(t ,defaultColumnWidth , cols , rows);
	}

	public void createSheet(T t, int columnWidth , int cols, int rows) {
		this.sheet = workbook.createSheet();
		this.sheet.setDefaultColumnWidth(columnWidth);
		this.sheet.createFreezePane(cols, rows);
	}	


	/**
	 * 增加一行
	 * @param index 行号
	 */
	public void createRow( int index) {
		createRow(this.getCurrSheet(),index);
	}


	public void createRow(Sheet sheet, int index) {
		setCurrRow(sheet.createRow(index));
	}


	public void setTitleCell(int index, String value) {
		Cell cell = getCurrRow().createCell(index);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		CellStyle cellStyle = commonTitleStyle();
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	public void setCell(int index, String value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, int value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, long value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, double value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, float value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, BigDecimal value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, Date value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	public void setCell(int index, Timestamp value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}


	public void setCell(int index, Object value) {
		CellStyle cellStyle = commonStyle(value , cellExport.getOperationModel());
		cellExport.setCell(index , getCurrRow() , cellStyle , value);
	}

	


	public CellStyle firstCommonStyle(int cellIndex , Class<T> clz, Object val) {
		try{
			if(titleRow != null && isCustomTitleInfo()){
				CellStyle cellStyle =  this.titleRow[titleRow.length-1].getCell(cellIndex).getCellStyle();
				return firstCommonStyle(cellStyle  ,cellIndex ,  clz ,  val , cellExport.getOperationModel());
			}
		}catch(Exception e){
			;
		}
		return commonStyle();
	}



	
	
	
	
	/**
	 * 设置单元格内容
	 * @param index
	 * @param value
	 * @param alignment
	 * @param boldweight
	 */
	public void setCell(int index, String value, short alignment, boolean boldweight) {
		
		Cell cell = getCurrRow().createCell(index);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		CellStyle cellStyle = getCellStyle(alignment, boldweight);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}



	/**
	 * 设置单元格内容
	 * @param index
	 * @param value
	 * @param alignment
	 * @param boldweight
	 * @param formatEm
	 */
	public void setCell(int index, Double value, short alignment, boolean boldweight, EnumXlsFormat formatEm) {
		
		Cell cell = getCurrRow().createCell(index);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		CellStyle cellStyle = getCellStyle(alignment, boldweight, formatEm);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}


	public void setNumberCell(int index, Double value, short alignment, boolean boldweight) {
		this.setCell(index, value, alignment,boldweight , EnumXlsFormat.NUMBER);
	}

	public void setCurrencyCell(int index, Double value, short alignment, boolean boldweight) {
		this.setCell(index, value, alignment,boldweight , EnumXlsFormat.CURRENCY);
	}

	public void setPercentCell(int index, Double value, short alignment, boolean boldweight) {
		this.setCell(index, value, alignment,boldweight , EnumXlsFormat.PERCENT);
	}


	/**
	 * 处理特殊行
	 * @param t
	 * @param columns
	 * @param addNumber
	 * @param index			数据记录号 , 从1开始
	 * @param <T>
	 * @return
	 */
	public <T> boolean specialHand(T t, List<Column> columns, boolean addNumber , int index){
		return false;
	}


	public Sheet getCurrSheet() {
		return this.sheet;
	}

	public void setCurrSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Row getCurrRow() {
		return this.row;
	}

	public void setCurrRow(Row row) {
		this.row = row;
	}

	@Override
	public Workbook getWorkbook() {
		return workbook;
	}

	public Row[] getTitleRow() {
		return titleRow;
	}

	public void setTitleRow(Row[] titleRow) {
		this.titleRow = titleRow;
	}

	public Row[] getHeaderRow() {
		return headerRow;
	}

	public void setHeaderRow(Row[] headerRow) {
		this.headerRow = headerRow;
	}

	public boolean isCustomTitleInfo() {
		return isCustomTitleInfo;
	}
}
