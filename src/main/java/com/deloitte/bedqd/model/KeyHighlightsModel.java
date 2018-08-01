package com.deloitte.bedqd.model;

import java.util.List;

public class KeyHighlightsModel {
	private List<HighPriorityDQIssues> listOfHighPriorityDQIssues;
	private List<DQMoniteringStats> listOfDQMoniteringStats;
	private OpenDQIssues OpenDQIssues;
	private DataQualityMonitering dataQualityMonitering;
	private DQRIAndDQPScores dQRIAndDQPScores;
	
	public List<HighPriorityDQIssues> getListOfHighPriorityDQIssues() {
		return listOfHighPriorityDQIssues;
	}
	public void setListOfHighPriorityDQIssues(List<HighPriorityDQIssues> listOfHighPriorityDQIssues) {
		this.listOfHighPriorityDQIssues = listOfHighPriorityDQIssues;
	}
	public List<DQMoniteringStats> getListOfDQMoniteringStats() {
		return listOfDQMoniteringStats;
	}
	public void setListOfDQMoniteringStats(List<DQMoniteringStats> listOfDQMoniteringStats) {
		this.listOfDQMoniteringStats = listOfDQMoniteringStats;
	}
	public OpenDQIssues getOpenDQIssues() {
		return OpenDQIssues;
	}
	public void setOpenDQIssues(OpenDQIssues openDQIssues) {
		OpenDQIssues = openDQIssues;
	}
	public DataQualityMonitering getDataQualityMonitering() {
		return dataQualityMonitering;
	}
	public void setDataQualityMonitering(DataQualityMonitering dataQualityMonitering) {
		this.dataQualityMonitering = dataQualityMonitering;
	}
	public DQRIAndDQPScores getdQRIAndDQPScores() {
		return dQRIAndDQPScores;
	}
	public void setdQRIAndDQPScores(DQRIAndDQPScores dQRIAndDQPScores) {
		this.dQRIAndDQPScores = dQRIAndDQPScores;
	}
	
	
}
