package com.deloitte.bedqd.model;

import java.util.List;

public class MeasureRemediateDataQuality {

	private List<OpenDQIussuesPriorVsCurntQutr> listOpenDQIssuesPriorVsCurntQutr;
	private OpenDQIssueCount openDQIssueCount;
	
	@Override
	public String toString() {
		return "MeasureRemediateDataQuality [listOpenDQIssuesPriorVsCurntQutr=" + listOpenDQIssuesPriorVsCurntQutr
				+ ", openDQIssueCount=" + openDQIssueCount + "]";
	}
	public List<OpenDQIussuesPriorVsCurntQutr> getListOpenDQIssuesPriorVsCurntQutr() {
		return listOpenDQIssuesPriorVsCurntQutr;
	}
	public void setListOpenDQIssuesPriorVsCurntQutr(List<OpenDQIussuesPriorVsCurntQutr> listOpenDQIssuesPriorVsCurntQutr) {
		this.listOpenDQIssuesPriorVsCurntQutr = listOpenDQIssuesPriorVsCurntQutr;
	}
	public OpenDQIssueCount getOpenDQIssueCount() {
		return openDQIssueCount;
	}
	public void setOpenDQIssueCount(OpenDQIssueCount openDQIssueCount) {
		this.openDQIssueCount = openDQIssueCount;
	}
	
	
	
	
}
