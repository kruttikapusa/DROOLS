package com.deloitte.bedqd.model;

import java.util.List;

public class DataQualityModel {

	private BcdeWithDQModel bcdeWithDQModel;
	private DQScoreModel dQScoreModel;
	private EcdeWithDQModel ecdeWithDQModel;
	private List<PerstOfAdsProfileModel> perstOfAdsProfileModel;
	private List<DQMonitoringDetailsbySourceSystem> dQMonitoringDetailsbySourceSystem;
	private List<ECDEandBCDEwithDQmonitoringbyADS> eCDEandBCDEwithDQmonitoringbyADS;
	
	
	public List<ECDEandBCDEwithDQmonitoringbyADS> geteCDEandBCDEwithDQmonitoringbyADS() {
		return eCDEandBCDEwithDQmonitoringbyADS;
	}
	public void seteCDEandBCDEwithDQmonitoringbyADS(
			List<ECDEandBCDEwithDQmonitoringbyADS> eCDEandBCDEwithDQmonitoringbyADS) {
		this.eCDEandBCDEwithDQmonitoringbyADS = eCDEandBCDEwithDQmonitoringbyADS;
	}
	public List<DQMonitoringDetailsbySourceSystem> getdQMonitoringDetailsbySourceSystem() {
		return dQMonitoringDetailsbySourceSystem;
	}
	public void setdQMonitoringDetailsbySourceSystem(
			List<DQMonitoringDetailsbySourceSystem> dQMonitoringDetailsbySourceSystem) {
		this.dQMonitoringDetailsbySourceSystem = dQMonitoringDetailsbySourceSystem;
	}
	public BcdeWithDQModel getBcdeWithDQModel() {
		return bcdeWithDQModel;
	}
	public void setBcdeWithDQModel(BcdeWithDQModel bcdeWithDQModel) {
		this.bcdeWithDQModel = bcdeWithDQModel;
	}
	public DQScoreModel getdQScoreModel() {
		return dQScoreModel;
	}
	public void setdQScoreModel(DQScoreModel dQScoreModel) {
		this.dQScoreModel = dQScoreModel;
	}
	public EcdeWithDQModel getEcdeWithDQModel() {
		return ecdeWithDQModel;
	}
	public void setEcdeWithDQModel(EcdeWithDQModel ecdeWithDQModel) {
		this.ecdeWithDQModel = ecdeWithDQModel;
	}
	public List<PerstOfAdsProfileModel> getPerstOfAdsProfileModel() {
		return perstOfAdsProfileModel;
	}
	public void setPerstOfAdsProfileModel(
			List<PerstOfAdsProfileModel> perstOfAdsProfileModel) {
		this.perstOfAdsProfileModel = perstOfAdsProfileModel;
	}
}

