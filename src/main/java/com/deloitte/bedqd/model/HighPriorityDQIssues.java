package com.deloitte.bedqd.model;

public class HighPriorityDQIssues {
	private String header;
	private String nbrDaysOpen;
	private String agingBucket;
	private String nbrOfIssues;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getNbrOfIssues() {
		return nbrOfIssues;
	}
	public void setNbrOfIssues(String nbrOfIssues) {
		this.nbrOfIssues = nbrOfIssues;
	}
	public String getNbrDaysOpen() {
		return nbrDaysOpen;
	}
	public void setNbrDaysOpen(String nbrDaysOpen) {
		this.nbrDaysOpen = nbrDaysOpen;
	}
	public String getAgingBucket() {
		return agingBucket;
	}
	public void setAgingBucket(String agingBucket) {
		this.agingBucket = agingBucket;
	}
	
}
