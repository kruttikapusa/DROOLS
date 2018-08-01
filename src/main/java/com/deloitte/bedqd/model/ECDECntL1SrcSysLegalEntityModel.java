package com.deloitte.bedqd.model;

public class ECDECntL1SrcSysLegalEntityModel extends GenericModel {

	private String level1ProcessDqp;
	private String sourceSystem;
	private String sourceLOB;
	private String ecdeCnt;
	private String ecdeRcrdsTstd;
	
	public String getLevel1ProcessDqp() {
		return level1ProcessDqp;
	}
	public void setLevel1ProcessDqp(String level1ProcessDqp) {
		this.level1ProcessDqp = level1ProcessDqp;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getsourceLOB() {
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
