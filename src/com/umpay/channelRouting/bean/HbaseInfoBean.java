package com.umpay.channelRouting.bean;

/**
 * 通道路由查询hbase的信息
 * 
 * @author xuxiaojia
 */
public class HbaseInfoBean {
//	private Table table;

	private static final String SEP_DOT = ",";
	private String selectedDatasource; // 选择的数据源

	private String mainDatasource;// 主数据源

	private String minorDatasource;// 从数据源

	public HbaseInfoBean(String mainHbaseDs, String minorDatasource) {
		this.mainDatasource = mainHbaseDs;
		this.minorDatasource = minorDatasource;
		this.selectedDatasource = mainHbaseDs;
//		this.table = table;
	}
//	public HbaseInfoBean(String mainHbaseDs, String minorDatasource, Table table) {
//		this.mainDatasource = mainHbaseDs;
//		this.minorDatasource = minorDatasource;
//		this.selectedDatasource = mainHbaseDs;
//		this.table = table;
//	}

	public void setMainDatasource(String mainDatasource) {
		this.mainDatasource = mainDatasource;
	}

	public void setMinorDatasource(String minorDatasource) {
		this.minorDatasource = minorDatasource;
	}

	public String getSelectedDatasource() {
		return selectedDatasource;
	}

	public void setSelectedDatasource(String selectedDatasource) {
		this.selectedDatasource = selectedDatasource;
		this.mainDatasource = (this.selectedDatasource != null && !this.selectedDatasource.equals("null")
				? this.selectedDatasource.split(SEP_DOT)[0] : null);
		this.minorDatasource = (this.selectedDatasource != null && !this.selectedDatasource.equals("null"))
				? (this.selectedDatasource.split(SEP_DOT).length >= 2 ? this.selectedDatasource.split(SEP_DOT)[1]
						: this.selectedDatasource.split(SEP_DOT)[0])
				: null;
	}

	public String getMainDatasource() {
		return this.mainDatasource;
	}

	public String getMinorDatasource() {
		return this.minorDatasource;
	}

//	public Table getTable() {
//		return table;
//	}
//
//	public void setTable(Table table) {
//		this.table = table;
//	}

}
