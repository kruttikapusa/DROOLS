package com.deloitte.bedqd.model;

public class OpenDQIssueCount {
	
	private String header;
	private String opnDQCount;
	public String getOpnDQCount() {
		return opnDQCount;
	}
	public void setOpnDQCount(String opnDQCount) {
		this.opnDQCount = opnDQCount;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	
	@Override
	public String toString() {
		return "OpenDQIssuesMeasureDQ [header=" + header + ", opnDQCount=" + opnDQCount + "]";
	}

}
