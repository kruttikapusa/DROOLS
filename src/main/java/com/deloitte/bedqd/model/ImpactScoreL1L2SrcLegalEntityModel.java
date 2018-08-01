package com.deloitte.bedqd.model;

public class ImpactScoreL1L2SrcLegalEntityModel extends GenericModel {

	private String dimension;
	private String sourceSytem;
	private String sourceLOB;
	private String level1ProcessDqp;
	private String ecde;
	private String impactScore;
	private String completeness;
	private String confirmity;
	private String validity;
	private String accuracy;
	
	
	public String getSourceSytem() {
		return sourceSytem;
	}
	public void setSourceSytem(String sourceSytem) {
		this.sourceSytem = sourceSytem;
	}
	public String getSourceLOB() {
		return sourceLOB;
	}
	public void setSourceLOB(String sourceLOB) {
		this.sourceLOB = sourceLOB;
	}
	
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	public String getLevel1ProcessDqp() {
		return level1ProcessDqp;
	}
	public void setLevel1ProcessDqp(String level1ProcessDqp) {
		this.level1ProcessDqp = level1ProcessDqp;
	}
	public String getEcde() {
		return ecde;
	}
	public void setEcde(String ecde) {
		this.ecde = ecde;
	}
	public String getImpactScore() {
		return impactScore;
	}
	public void setImpactScore(String impactScore) {
		this.impactScore = impactScore;
	}
	public String getCompleteness() {
		return completeness;
	}
	public void setCompleteness(String completeness) {
		this.completeness = completeness;
	}
	public String getConfirmity() {
		return confirmity;
	}
	public void setConfirmity(String confirmity) {
		this.confirmity = confirmity;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
}
