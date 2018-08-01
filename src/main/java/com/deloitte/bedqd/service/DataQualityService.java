package com.deloitte.bedqd.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.bedqd.model.BcdeWithDQModel;
import com.deloitte.bedqd.model.DQMoniteringStats;
import com.deloitte.bedqd.model.DQMonitoringDetailsbySourceSystem;
import com.deloitte.bedqd.model.DQRIAndDQPScores;
import com.deloitte.bedqd.model.DQScoreModel;
import com.deloitte.bedqd.model.DataQualityModel;
import com.deloitte.bedqd.model.DataQualityMonitering;
import com.deloitte.bedqd.model.ECDEandBCDEwithDQmonitoringbyADS;
import com.deloitte.bedqd.model.EcdeWithDQModel;
import com.deloitte.bedqd.model.GenericModel;
import com.deloitte.bedqd.model.HighPriorityDQIssues;
import com.deloitte.bedqd.model.KeyHighlightsModel;
import com.deloitte.bedqd.model.MeasureOrRemidateDataQuantityModel;
import com.deloitte.bedqd.model.OpenDQIssues;
import com.deloitte.bedqd.model.OpenDQIssuesSummaryModel;
import com.deloitte.bedqd.model.OpenDQIssuesTypeDetails;
import com.deloitte.bedqd.model.OpenDQPrioritySummary;
import com.deloitte.bedqd.model.PerstOfAdsProfileModel;
import com.deloitte.bedqd.scheduler.JSONSchedulerService;
import com.deloitte.debdeep.dao.DataQualityDao;

@Service
public class DataQualityService {
	
	private DataQualityDao dao = null;
	
public DataQualityModel getDQMonitoringData(Date priorStartDate,Date priorEndDate,Date currentStartDate,Date currentEndDate) {
		
		DataQualityModel model = null;
		try{
			dao = new DataQualityDao();
			model = new DataQualityModel();
			
			BcdeWithDQModel bcdeWithDQModel = dao.getBcdeWithDQDetails();
			DQScoreModel dQScoreModel = dao.getDQScoreDetails(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			EcdeWithDQModel ecdeWithDQModel = dao.getEcdeWithDQDetails();
			List<PerstOfAdsProfileModel> list= dao.getPerstOfAdsProfileDetails(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			List<DQMonitoringDetailsbySourceSystem> sourceList = dao.getDQMonitoringDetailsbySourceSystem(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			List<ECDEandBCDEwithDQmonitoringbyADS> ecdeAndBcdeList = dao.getECDEandBCDEwithDQmonitoringbyADS();
			
			if(null != bcdeWithDQModel){ model.setBcdeWithDQModel(bcdeWithDQModel);	}
			if(null != dQScoreModel){ model.setdQScoreModel(dQScoreModel);	}
			if(null != ecdeWithDQModel){ model.setEcdeWithDQModel(ecdeWithDQModel);	}
			if(null != list){ model.setPerstOfAdsProfileModel(list);	}
			if(null != sourceList){ model.setdQMonitoringDetailsbySourceSystem(sourceList);	}
			if(null != ecdeAndBcdeList){ model.seteCDEandBCDEwithDQmonitoringbyADS(ecdeAndBcdeList);	}
			
		}catch(Exception ex){
			System.out.println("Getting error in service layer");
		}
		return model;
	}

	public KeyHighlightsModel getKeyHighlightsData(Date priorStartDate,Date priorEndDate,Date currentStartDate,Date currentEndDate) {
		KeyHighlightsModel keyHighlights = null;
		try{
			System.out.println("Inside getKeyHighlightsData");
			dao = new DataQualityDao();
			keyHighlights = new KeyHighlightsModel();
			List<HighPriorityDQIssues> listOfHPDQIssues = dao.getListOfHPDQIssues(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			List<DQMoniteringStats> listOfDQMoniteringStats = dao.getDQMoniteringStats();
			
			//BEDQD Drools part code changes 
			OpenDQIssues openDQIssues = dao.getOpenDQIssues(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			DataQualityMonitering dataQualityMonitering =  dao.getDataMonitering(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			
			DQRIAndDQPScores dQRIAndDQPScores = dao.getDQRIAndDQPScores();
			
			if(null != listOfDQMoniteringStats){ keyHighlights.setListOfDQMoniteringStats(listOfDQMoniteringStats);	}
			if(null != openDQIssues){ keyHighlights.setOpenDQIssues(openDQIssues);	}
			if(null != listOfHPDQIssues){ keyHighlights.setListOfHighPriorityDQIssues(listOfHPDQIssues);	}
			if(null != dataQualityMonitering){ keyHighlights.setDataQualityMonitering(dataQualityMonitering);	}
			if(null != dQRIAndDQPScores){ keyHighlights.setdQRIAndDQPScores(dQRIAndDQPScores);	}
			
		}catch(Exception e){
			System.out.println("Getting error in service layer");
		}
		return keyHighlights;
	}

	/*public Object getMeasureRemediateDQData() {
		MeasureRemediateDataQuality measureRemediateDataQuality = null;
		
		try {
			dao = new DataQualityDao();
			measureRemediateDataQuality = new MeasureRemediateDataQuality();
			
			OpenDQIssueCount openDQIssueCount = dao.getOpenDQIssueCount();
			List<OpenDQIussuesPriorVsCurntQutr> listOpenDQIssuesPriorVsCurr = dao.getDQIussuesPriorVsCurntQutrList();
			
			if(null != openDQIssueCount) {
				measureRemediateDataQuality.setOpenDQIssueCount(openDQIssueCount);
			}
			if(null != listOpenDQIssuesPriorVsCurr && listOpenDQIssuesPriorVsCurr.size() > 0) {
				measureRemediateDataQuality.setListOpenDQIssuesPriorVsCurntQutr(listOpenDQIssuesPriorVsCurr);
			}
			
		} catch (Exception e) {
			System.out.println("Error while fetching Measure/Remediate Data Quality Details");
		}
		return measureRemediateDataQuality;
	}*/
	
	
	public Object getMeasureRemediateDQData(Date priorStartDate,Date priorEndDate,Date currentStartDate,Date currentEndDate) {
		MeasureOrRemidateDataQuantityModel measureRemediateDataQuality = null;
		
		try {
			System.out.println("Inside getMeasureRemediateDQData");
			dao = new DataQualityDao();
			measureRemediateDataQuality = new MeasureOrRemidateDataQuantityModel();
			
			DQScoreModel dQScoreModel = dao.getDQScoreDetails(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			List<OpenDQIssuesSummaryModel> issueSummary = dao.getOpenDQIssuesSummaryModel(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			OpenDQIssuesTypeDetails issueTypeDetails = dao.getOpenDQIssuesTypeDetails(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
			OpenDQPrioritySummary openDQPrioritySummary = dao.getOpenDQPrioritySummary(currentStartDate,currentEndDate); 
			OpenDQPrioritySummary openDQPriorityDtlsLowPr = dao.openDQPriorityDtlsLowPr(currentStartDate,currentEndDate);
			OpenDQPrioritySummary openDQPriorityDtlsHighPr = dao.openDQPriorityDtlsHighPr(priorStartDate,priorEndDate);
			
			if(null != dQScoreModel){
				measureRemediateDataQuality.setDqScoreModel(dQScoreModel);
			}
			if(null != issueSummary) {
				measureRemediateDataQuality.setIssueSummaryLst(issueSummary);
			}
			if(null != issueTypeDetails) {
				measureRemediateDataQuality.setIssueTypeDetails(issueTypeDetails);
			}
			if(null != openDQPrioritySummary){
				measureRemediateDataQuality.setOpenDQPrioritySummary(openDQPrioritySummary);
			}
			if(null != openDQPriorityDtlsLowPr){
				measureRemediateDataQuality.setOpenDQPriorityDtlsLowPr(openDQPriorityDtlsLowPr);
			}
			if(null != openDQPriorityDtlsHighPr){
				measureRemediateDataQuality.setOpenDQPriorityDtlsHighPr(openDQPriorityDtlsHighPr);
			}
			
		} catch (Exception e) {
			System.out.println("Error while fetching Measure/Remediate Data Quality Details");
		}
		return measureRemediateDataQuality;
	}
	
	public Object getRefreshDashboardEndPoint() {
        
        GenericModel response= new GenericModel();
        try {
        JSONSchedulerService service = new JSONSchedulerService();
        service.writeJSONToFile();
        response.setHeader("SUCCESS");
        }catch(Exception e) {
        	response.setHeader("FAILED");
        	System.out.println(e.getMessage());
        }
        return response;
  }

	
}
