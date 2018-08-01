package com.deloitte.bedqd.model;

public class ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB extends GenericModel{

	private String sourceSystem;
	private String sourceLOB;
	private String dqriScore;
	private String dqpScore;
	private String ecdeCnt;
	private String impactScore;
	private String legalEntityLOB;
	
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
	public String getDqriScore() {
		return dqriScore;
	}
	public void setDqriScore(String dqriScore) {
		this.dqriScore = dqriScore;
	}
	public String getDqpScore() {
		return dqpScore;
	}
	public void setDqpScore(String dqpScore) {
		this.dqpScore = dqpScore;
	}
	public String getEcdeCnt() {
		return ecdeCnt;
	}
	public void setEcdeCnt(String ecdeCnt) {
		this.ecdeCnt = ecdeCnt;
	}
	public String getImpactScore() {
		return impactScore;
	}
	public void setImpactScore(String impactScore) {
		this.impactScore = impactScore;
	}
	public String getLegalEntityLOB() {
		return legalEntityLOB;
	}
	public void setLegalEntityLOB(String legalEntityLOB) {
		this.legalEntityLOB = legalEntityLOB;
	}
}
