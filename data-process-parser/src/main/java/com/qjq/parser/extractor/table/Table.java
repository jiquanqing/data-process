package com.qjq.parser.extractor.table;

import java.util.HashMap;
import java.util.Map;

public class Table {

	private boolean isSuccess;
	private String uid;
	private Map<String, String> meta; // 页码，表名，
	private Cell[][] cells;
	private int contentRow;
	private int rows;
	private int cols;

	public void initCell(int row, int col) {
		this.cells = new Cell[row][col];
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
	public void init() {
		isSuccess = true;
		meta = new HashMap<String, String>();
	}
	public Table() {
		this.init();
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Map<String, String> getMeta() {
		return meta;
	}
	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}
	public Cell[][] getCells() {
		return cells;
	}
	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}
	public int getContentRow() {
		return contentRow;
	}
	public void setContentRow(int contentRow) {
		this.contentRow = contentRow;
	}

}
