package com.deloitte.bedqd.model;

public class DQMonitoringDetailsbySourceSystem {
	
	private String databaseName;
	private String ads;
	private String bucfName;
	private String dataQualityScore;
	private String chgFrmPrQtr;
	private String rcrdsPsd;
	private String rowsTstd;
	private String header;
	private String dimensionFltr;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getAds() {
		return ads;
	}
	public void setAds(String ads) {
		this.ads = ads;
	}
	public String getBucfName() {
		return bucfName;
	}
	public void setBucfName(String bucfName) {
		this.bucfName = bucfName;
	}
	public String getDataQualityScore() {
		return dataQualityScore;
	}
	public void setDataQualityScore(String dataQualityScore) {
		this.dataQualityScore = dataQualityScore;
	}
	public String getChgFrmPrQtr() {
		return chgFrmPrQtr;
	}
	public void setChgFrmPrQtr(String chgFrmPrQtr) {
		this.chgFrmPrQtr = chgFrmPrQtr;
	}
	public String getRcrdsPsd() {
		return rcrdsPsd;
	}
	public void setRcrdsPsd(String rcrdsPsd) {
		this.rcrdsPsd = rcrdsPsd;
	}
	public String getRowsTstd() {
		return rowsTstd;
	}
	public void setRowsTstd(String rowsTstd) {
		this.rowsTstd = rowsTstd;
	}
	public String getDimensionFltr() {
		return dimensionFltr;
	}
	public void setDimensionFltr(String dimensionFltr) {
		this.dimensionFltr = dimensionFltr;
	}

}
