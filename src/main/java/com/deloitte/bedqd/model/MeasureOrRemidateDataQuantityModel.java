package com.deloitte.bedqd.model;

import java.util.List;

public class MeasureOrRemidateDataQuantityModel {
	
	private List<OpenDQIssuesSummaryModel> issueSummaryLst;
	
	private OpenDQIssuesTypeDetails issueTypeDetails;
	
	private OpenDQPrioritySummary openDQPrioritySummary;
	
	private OpenDQPrioritySummary openDQPriorityDtlsLowPr;
	
	private OpenDQPrioritySummary openDQPriorityDtlsHighPr;
	
	private DQScoreModel dqScoreModel;
	
	

	public OpenDQPrioritySummary getOpenDQPriorityDtlsLowPr() {
		return openDQPriorityDtlsLowPr;
	}

	public void setOpenDQPriorityDtlsLowPr(OpenDQPrioritySummary openDQPriorityDtlsLowPr) {
		this.openDQPriorityDtlsLowPr = openDQPriorityDtlsLowPr;
	}

	public OpenDQPrioritySummary getOpenDQPriorityDtlsHighPr() {
		return openDQPriorityDtlsHighPr;
	}

	public void setOpenDQPriorityDtlsHighPr(OpenDQPrioritySummary openDQPriorityDtlsHighPr) {
		this.openDQPriorityDtlsHighPr = openDQPriorityDtlsHighPr;
	}

	public OpenDQPrioritySummary getOpenDQPrioritySummary() {
		return openDQPrioritySummary;
	}

	public void setOpenDQPrioritySummary(OpenDQPrioritySummary openDQPrioritySummary) {
		this.openDQPrioritySummary = openDQPrioritySummary;
	}

	public List<OpenDQIssuesSummaryModel> getIssueSummaryLst() {
		return issueSummaryLst;
	}

	public void setIssueSummaryLst(List<OpenDQIssuesSummaryModel> issueSummaryLst) {
		this.issueSummaryLst = issueSummaryLst;
	}

	public OpenDQIssuesTypeDetails getIssueTypeDetails() {
		return issueTypeDetails;
	}

	public void setIssueTypeDetails(OpenDQIssuesTypeDetails issueTypeDetails) {
		this.issueTypeDetails = issueTypeDetails;
	}

	public DQScoreModel getDqScoreModel() {
		return dqScoreModel;
	}

	public void setDqScoreModel(DQScoreModel dqScoreModel) {
		this.dqScoreModel = dqScoreModel;
	}

	
}
