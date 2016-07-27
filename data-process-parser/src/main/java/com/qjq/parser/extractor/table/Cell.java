package com.qjq.parser.extractor.table;

import java.util.ArrayList;
import java.util.List;

public class Cell {

	private String mark; // /
	private int rows;
	private int cols;
	private String text;
	private List<Table> tables = new ArrayList<Table>();

	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	public void addCellTable(Table table) {
		this.tables.add(table);
	}

}
