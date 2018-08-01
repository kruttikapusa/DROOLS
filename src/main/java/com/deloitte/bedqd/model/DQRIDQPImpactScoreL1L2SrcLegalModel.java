package com.deloitte.bedqd.model;

public class DQRIDQPImpactScoreL1L2SrcLegalModel extends GenericModel {

	private String level1ProcessDQP;
	private String level2ProcessDQP;
	private String sourceSystem;
	private String sourceLob;
	private String dqriScore;
	private String dqpScore;
	private String ecdeCnt;
	private String impactScore;
	
	
	public String getLevel1ProcessDQP() {
		return level1ProcessDQP;
	}
	public void setLevel1ProcessDQP(String level1ProcessDQP) {
		this.level1ProcessDQP = level1ProcessDQP;
	}
	public String getLevel2ProcessDQP() {
		return level2ProcessDQP;
	}
	public void setLevel2ProcessDQP(String level2ProcessDQP) {
		this.level2ProcessDQP = level2ProcessDQP;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getSourceLob() {
		return sourceLob;
	}
	public void setSourceLob(String sourceLob) {
		this.sourceLob = sourceLob;
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
}
