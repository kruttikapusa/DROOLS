package com.deloitte.bedqd.model;

public class OpenDQIussuesPriorVsCurntQutr {

	private String header;
	private String priorQuarter;
	private String currentQuarter;
	private String change;
	private String legalEntityLob;
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getPriorQuarter() {
		return priorQuarter;
	}
	public void setPriorQuarter(String priorQuarter) {
		this.priorQuarter = priorQuarter;
	}
	public String getCurrentQuarter() {
		return currentQuarter;
	}
	public void setCurrentQuarter(String currentQuarter) {
		this.currentQuarter = currentQuarter;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getLegalEntityLob() {
		return legalEntityLob;
	}
	public void setLegalEntityLob(String legalEntityLob) {
		this.legalEntityLob = legalEntityLob;
	}
	@Override
	public String toString() {
		return "OpenDQIussuesPriorVsCurntQutr [header=" + header + ", priorQuarter=" + priorQuarter
				+ ", currentQuarter=" + currentQuarter + ", change=" + change + ", legalEntityLob=" + legalEntityLob
				+ "]";
	}
	
	
}
