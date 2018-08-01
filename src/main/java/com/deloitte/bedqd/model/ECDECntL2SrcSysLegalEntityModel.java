package com.deloitte.bedqd.model;

public class ECDECntL2SrcSysLegalEntityModel extends GenericModel {

	private String level2ProcessDqp;
	private String sourceSystem;
	private String sourceLOB;
	private String ecdeCnt;
	private String ecdeRcrdsTstd;
	
	
	
	public String getLevel2ProcessDqp() {
		return level2ProcessDqp;
	}
	public void setLevel2ProcessDqp(String level2ProcessDqp) {
		this.level2ProcessDqp = level2ProcessDqp;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getSourceLOB() {
		return sourceLOB;
	}
	public void setSourceLOB(String sourceLOB) {
		this.sourceLOB = sourceLOB;
	}
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
