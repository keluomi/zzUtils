package com.zz.bms.util.poi;

import com.zz.bms.util.poi.util.ColumnUtil;
import com.zz.bms.util.poi.vo.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 */
public class BaseXlsExport<T> extends AbstractXlsExport<T> implements IExport<T> {


	/**
	 * 导出标题	
	 * @param headers		头信息行数
	 * @param t  			数据类型
	 * @param isAddNumber	是否增加序号
	 */
	@Override
	public void exportTitles(int headers ,  T t,boolean isAddNumber)  {
		

		int rowIndex = 0;
		int headLength = headers;

		rowIndex = headLength;

		this.createSheet(t , 0 , headLength+1);
		List<Column> columns = null;

		int position = isAddNumber ? 0 : 1;


		Row[] titleRows = null;

		if(this.isWriteTitle()) {

			// header
			this.createRow(this.getCurrSheet(),rowIndex++);

			this.getCurrRow().setHeight((short)titleColumnHeight);
			if(isAddNumber) {
				this.setTitleCell(0, getNumberName());
			}

			columns = ColumnUtil.getColumn(t.getClass() , false);
			for (Column column : columns) {
				this.setTitleCell(column.getNumber() + position , column.getName());
			}

			titleRows = new Row[1];
			titleRows[0] =this.getCurrRow();

		}else {
			rowIndex = this.getCurrSheet().getLastRowNum() + 1;
			if(rowIndex > 0){
				titleRows = new Row[rowIndex-headLength];
				for(int i=0;i<rowIndex-headLength ; i++){
					titleRows[i] = this.getCurrSheet().getRow(i+headLength);
				}
			}
		}

	}

	@Override
	public void exportHeaders(List<String> headers) {

		if(headers == null || headers.isEmpty() ) {
			return ;
		}

		CellStyle cellStyle1 = getHeaderCellStyle1();
		CellStyle cellStyle2 = getHeaderCellStyle2();
		Row row = this.getCurrSheet().getRow(this.getCurrSheet().getLastRowNum());
		int cellLength = 13 ;
		if(row != null) {
			cellLength = row.getLastCellNum();
		}
		if(cellLength > 13) {
			cellLength = 13;
		}


		for(int i=0;i<headers.size();i++){
			this.createRow(this.getCurrSheet(),i);
			Cell cell = this.getCurrRow().createCell(0);
			if(i == 0) {
				cell.setCellStyle(cellStyle1);
				this.getCurrRow().setHeightInPoints(20);
				this.getCurrRow().setHeight((short)700);
			} else {
				cell.setCellStyle(cellStyle2);
			}
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(headers.get(i));

			for(int index = 1 ; index<cellLength;index ++){
				cell = this.getCurrRow().createCell(index);
				cell.setCellStyle(cellStyle2);
			}

			CellRangeAddress cellRangeAddress = new CellRangeAddress(i,i,0,(short)(cellLength-1));
			this.getCurrSheet().addMergedRegion(cellRangeAddress);

		}
	}

	@Override
	public void exportContent(List<T> contents, int rowIndex, List<Column> columns, boolean isAddNumber) {

	}



	@Override
	public CellStyle commonStyle(int cellIndex) {
		return commonStyle();
	}


	/**
	 * 导出Excel文件
	 *
	 * @throws RuntimeException
	 */
	public void exportXls(String xlsFileName) throws RuntimeException {
		exportXls(xlsFileName,workbook);
	}
	public void exportXls(String xlsFileName, Workbook workbook) throws RuntimeException {
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(xlsFileName);
			workbook.write(fOut);
			fOut.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("生成导出Excel文件出错!", e);
		} catch (IOException e) {
			throw new RuntimeException("写入Excel文件出错!", e);
		} finally {
			try {
				if (fOut != null) {
					fOut.close();
				}
			} catch (IOException e) {

			}


		}
	}


	/**
	 * 下载文件
	 * @param response
	 * @throws RuntimeException
	 */
	public void exportXls(HttpServletResponse response) throws RuntimeException {
		exportXls(response,workbook);
	}
	public void exportXls(HttpServletResponse response, Workbook workbook) throws RuntimeException {
		ServletOutputStream os = null;
		try {
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("导出Excel文件出错!", e);
		} catch (IOException e) {
			throw new RuntimeException("导出Excel文件出错!", e);
		} finally {
			try{
				if(workbook != null) {
					workbook.close();
				}
			}catch(Exception e){

			}
			try {
				if (os != null)	{
					os.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
