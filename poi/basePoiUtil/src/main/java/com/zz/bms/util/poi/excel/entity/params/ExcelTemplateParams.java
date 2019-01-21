package com.zz.bms.util.poi.excel.entity.params;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 模板遍历时的参数
 * @author Administrator
 */
public class ExcelTemplateParams implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * key
     */
    private String            name;
    /**
     * 模板的cellStyle
     */
    private CellStyle         cellStyle;
    /**
     * 行高
     */
    private short            height;

    public ExcelTemplateParams() {

    }

    public ExcelTemplateParams(String name, CellStyle cellStyle, short height) {
        this.name = name;
        this.cellStyle = cellStyle;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

}
