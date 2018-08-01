package com.deloitte.bedqd.model;

public class EcdeCountEcdeRecords extends GenericModel{
	
	private String sourceSystem;
	private String sourceLOB;
	private String ecdeCnt;
	private String ecdeRcrdsTstd;
	
	public String getSourceLOB() {
		return sourceLOB;
	}
	public void setSourceLOB(String sourceLOB) {
		this.sourceLOB = sourceLOB;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;	}
	
	public String getEcdeCnt() {
		return ecdeCnt;
	}
	public void setEcdeCnt(String ecdeCnt) {
		this.ecdeCnt = ecdeCnt;
	}
	public String getEcdeRcrdsTstd() {
		return ecdeRcrdsTstd;
	}
	public void setEcdeRcrdsTstd(String ecdeRcrdsTstd) {
		this.ecdeRcrdsTstd = ecdeRcrdsTstd;
	}


}
