package com.deloitte.bedqd.model;

import java.util.List;
import java.util.Map;

public class OpenDQPrioritySummary {
	
	private Map<String,List<OpenDQPrioritySummaryDetail>> openDQPrioritySummaryMap;

	public Map<String, List<OpenDQPrioritySummaryDetail>> getOpenDQPrioritySummaryMap() {
		return openDQPrioritySummaryMap;
	}

	public void setOpenDQPrioritySummaryMap(
			Map<String, List<OpenDQPrioritySummaryDetail>> openDQPrioritySummaryMap) {
		this.openDQPrioritySummaryMap = openDQPrioritySummaryMap;
	}
}
