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
public class BaseXlsExport<T> extends AbstractXlsExport<T> implements ExcelExport<T> {


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

		int position = isAddNumber ? 1 : 0;


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


	/**
	 * 导出头信息
	 * @param headers
	 */
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


	/**
	 * 导出内容
	 * @param contents      内容
	 * @param rowIndex      行索引
	 * @param columns       列设置
	 * @param isAddNumber   是否增加序号
	 */
	@Override
	public void exportContent(List<T> contents, int rowIndex, List<Column> columns, boolean isAddNumber) {


		if(contents == null || contents.isEmpty()) {
			return ;
		}

		int position = isAddNumber ? 1 : 0;


		if(columns == null) {
			columns = ColumnUtil.getColumn(contents.get(0).getClass() , false);
		}

		int dataIndex = 0;

		for(T t : contents){
			if(t == null) {
				continue;
			}

			dataIndex ++;

			this.createRow(rowIndex++);

			if(this.specialHand(t, columns, isAddNumber , dataIndex)) {
				continue;
			}

			if(isAddNumber) {
				this.setCell(0 , dataIndex);
			}

			for (Column column : columns) {
				column.getField().setAccessible(true);
				try {
					Object value = column.getField().get(t);
					if(value == null) {
						this.setCell(column.getNumber() + position , "");
					}else {
						this.setCell(column.getNumber() + position, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}





	/**
	 * 导出Excel文件
	 *
	 * @throws RuntimeException
	 */
	@Override
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
	@Override
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