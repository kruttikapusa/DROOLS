package com.deloitte.bedqd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.bedqd.model.BcdeWithDQModel;
import com.deloitte.bedqd.model.DQPScoreModel;
import com.deloitte.bedqd.model.DQRIDQPImpactScoreL1L2SrcLegalModel;
import com.deloitte.bedqd.model.DQRIScoreModel;
import com.deloitte.bedqd.model.DQScoreModel;
import com.deloitte.bedqd.model.DataQualityModel;
import com.deloitte.bedqd.model.ECDECntL1SrcSysLegalEntityModel;
import com.deloitte.bedqd.model.ECDECntL2SrcSysLegalEntityModel;
import com.deloitte.bedqd.model.ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB;
import com.deloitte.bedqd.model.EcdeCountEcdeRecords;
import com.deloitte.bedqd.model.EcdeWithDQModel;
import com.deloitte.bedqd.model.ImpactScoreL1L2SrcLegalEntityModel;
import com.deloitte.bedqd.model.ImpactScoreL1SrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScoreL2SrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScoreModel;
import com.deloitte.bedqd.model.ImpactScoreSrcSysSrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB;
import com.deloitte.bedqd.model.InternalControlsModel;
import com.deloitte.bedqd.model.PerstOfAdsProfileModel;
import com.deloitte.debdeep.dao.DataQualityDao;
import com.deloitte.debdeep.dao.InternalControlsDao;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.api.KieServices;
import org.kie.api.command.Command;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InternalControlService {
	
	private InternalControlsDao dao = null;
	
	public InternalControlsModel getInternalControlsData(Date icStartDate,Date icEndDate){
		InternalControlsModel model = null;
		try{
			dao = new InternalControlsDao();
			model = new InternalControlsModel();
			
			DQPScoreModel dqpscoreModel = dao.getDQPScoreDetails();
			DQRIScoreModel dqriScoreModel = dao.getDQRIScoreDetails();
			ImpactScoreModel impactScoreModel = dao.getImapactScoreDetails();
			List<ECDECntL1SrcSysLegalEntityModel> ecdeCntL1SrcSysLegalEntityList = dao.getECDECntL1SrcSysLegalDetails(icStartDate,icEndDate);
			List<ECDECntL2SrcSysLegalEntityModel> ecdeCntL2SrcSysLegalEntityList = dao.getECDECntL2SrcSysLegalDetails(icStartDate,icEndDate);
			List<ImpactScoreL1L2SrcLegalEntityModel> impactScoreList = dao.getImpactScoreL1L2SrcLegalEntityDetails();
			List<ImpactScoreL2SrcSystemLegalEntityLOBModel> impactScoreL2 = dao.getImpactScoreL2SrcSystemLegalEntityLOBModel(icStartDate,icEndDate);
			List<ImpactScoreSrcSysSrcSystemLegalEntityLOBModel> impactScoreSrc = dao.getImpactScoreSrcSysSrcSystemLegalEntityLOBModel(icStartDate,icEndDate);
			List<EcdeCountEcdeRecords> ecdeCont = dao.getEcdeCountEcdeRecords(icStartDate,icEndDate);
			List<ImpactScoreL1SrcSystemLegalEntityLOBModel> impactScoreL1 = dao.getImpactScoreL1SrcSystemLegalEntityLOBModel(icStartDate,icEndDate);
			List<ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB> impactScoreLegalEntityLOB = dao.getImpactScoreForLegalEntityLOBSourceSystemLegalEntityLOB(icStartDate,icEndDate);
			List<ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB> ecdeCountLegalEntityLOB = dao.getECDErecordsTestedForLegalEntityLOBSourceSystemLegalEntityLOB(icStartDate,icEndDate);
				
			
			if(null != dqpscoreModel){ model.setdQPScoreModel(dqpscoreModel);	}
			if(null != dqriScoreModel){ model.setdQRIScoreModel(dqriScoreModel);	}
			if(null != impactScoreModel){ model.setImpactScoreModel(impactScoreModel);	}
			if(null != ecdeCntL1SrcSysLegalEntityList){ model.setEcdeCntL1SrcSysLegalEntityModel(ecdeCntL1SrcSysLegalEntityList);	}
			if(null != ecdeCntL2SrcSysLegalEntityList){ model.setEcdeCntL2SrcSysLegalEntityModel(ecdeCntL2SrcSysLegalEntityList);	}
 		    if(null != impactScoreList){ model.setImpactScoreL1L2SrcLegalEntityModel(impactScoreList);	}			
			if(null != impactScoreL2){ model.setImpactScoreL2SrcSystemLegalEntityLOBModel(impactScoreL2);}
			if(null != impactScoreSrc){ model.setImpactScoreSrcSysSrcSystemLegalEntityLOBModel(impactScoreSrc);	}
			if(null != ecdeCont){ model.setEcdeCountEcdeRecords(ecdeCont);	}
			if(null != impactScoreL1){ model.setImpactScoreL1SrcSystemLegalEntityLOBModel(impactScoreL1);	}
			if(null != impactScoreLegalEntityLOB){model.setImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB(impactScoreLegalEntityLOB);}
			if(null != ecdeCountLegalEntityLOB){model.seteCDECountForLegalEntityLOBSourceSystemLegalEntityLOB(ecdeCountLegalEntityLOB);}
			
			
		}catch(Exception ex){
			System.out.println("Getting error in service layer");
		}
		return model;
	}
}
