package com.deloitte.debdeep.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.deloitte.bedqd.connection.SQLConnection;
import com.deloitte.bedqd.model.BcdeWithDQModel;
import com.deloitte.bedqd.model.DQDimension;
import com.deloitte.bedqd.model.DQMoniteringStats;
import com.deloitte.bedqd.model.DQMonitoringDetailsbySourceSystem;
import com.deloitte.bedqd.model.DQRIAndDQPScores;
import com.deloitte.bedqd.model.DQScoreModel;
import com.deloitte.bedqd.model.DataQualityMonitering;
import com.deloitte.bedqd.model.ECDEandBCDEwithDQmonitoringbyADS;
import com.deloitte.bedqd.model.EcdeWithDQModel;
import com.deloitte.bedqd.model.HighPriorityDQIssues;
import com.deloitte.bedqd.model.OpenDQIssueCount;
import com.deloitte.bedqd.model.OpenDQIssues;
import com.deloitte.bedqd.model.OpenDQIssuesSummaryModel;
import com.deloitte.bedqd.model.OpenDQIssuesTypeDetails;
import com.deloitte.bedqd.model.OpenDQIussuesPriorVsCurntQutr;
import com.deloitte.bedqd.model.OpenDQPrioritySummary;
import com.deloitte.bedqd.model.OpenDQPrioritySummaryDetail;
import com.deloitte.bedqd.model.PerstOfAdsProfileModel;

public class DataQualityDao {

	SQLConnection dbConnection = new SQLConnection();
	int docEntry;
	
	private static final String EXCEPTION_STR = "Exception has occured while fetching details for";
	
	@SuppressWarnings("static-access")
	public BcdeWithDQModel getBcdeWithDQDetails()throws Exception {
		System.out.println("Connecting to a selected database..."); 
		Connection connection  = dbConnection.connection();
		System.out.println("Connected database successfully...");
		BcdeWithDQModel bcdeWithDQBean = null;

		String sqlQuery =   "SELECT SUM( " +
							"  CASE " +
							"    WHEN Is_Monitored='Y' " +
							"    AND Is_Bcde      ='Y' " +
							"    THEN 1 " +
							"    ELSE 0 " +
							"  END)*100/SUM( " +
							"  CASE " +
							"    WHEN (Is_Monitored='Y' " +
							"    OR Is_Monitored   ='N') " +
							"    AND Is_Bcde       ='Y' " +
							"    THEN 1 " +
							"    ELSE 0 " +
							"  END) AS BCDE_WITH_DQ_MONITORING " +
							"FROM V_CDE_1";
	 try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				bcdeWithDQBean = new BcdeWithDQModel();
				bcdeWithDQBean.setBcdeWithDQcore(dbConnection.resultset.getString("BCDE_WITH_DQ_MONITORING"));
				bcdeWithDQBean.setHeader("BCDE_WITH_DQ_MONITORING");
			}
			//dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);*/
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return bcdeWithDQBean;
	}
	
	
	
	@SuppressWarnings("static-access")
	public DQScoreModel getDQScoreDetails(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		DQScoreModel dQScoreBean = null;
		
		String sqlQuery = 	"SELECT ROUND( ((SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? "  +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Rows_Profiled AS NUMBER) " +
				"    ELSE 0 " +
				"  END))- SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? " +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Non_Compliant_Count AS NUMBER) " +
				"    ELSE 0 " +
				"  END)))/ SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? " +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Rows_Profiled AS NUMBER) " +
				"    ELSE 0 " +
				"  END)) - (SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? " +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Rows_Profiled AS NUMBER) " +
				"    ELSE 0 " +
				"  END))- SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? " +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Non_Compliant_Count AS NUMBER) " +
				"    ELSE 0 " +
				"  END)))/ SUM(( " +
				"  CASE " +
				"    WHEN TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? " +
				"    AND TO_DATE(Profile_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? " +
				"    THEN CAST(Rows_Profiled AS NUMBER) " +
				"    ELSE 0 " +
				"  END)) )*100,2) AS DATA_QUALITY_SCORE " +
				"FROM V_DQ_PROFILE " +
				"WHERE Profile_Date<> ''";
		

/*		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){*/
		try{
			System.out.println("Inside getDQScoreDetails");
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(3,  new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(4,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(5,  new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(6,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(7, new java.sql.Timestamp(currentStartDate.getTime()));
		prepStmt.setTimestamp(8,  new java.sql.Timestamp(currentEndDate.getTime()));
		prepStmt.setTimestamp(9,  new java.sql.Timestamp(currentStartDate.getTime()));
		prepStmt.setTimestamp(10,  new java.sql.Timestamp(currentEndDate.getTime()));
		prepStmt.setTimestamp(11,  new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(12,  new java.sql.Timestamp(currentEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();
		
			while(resultSet.next()){
				dQScoreBean = new DQScoreModel();
				dQScoreBean.setDqScore(resultSet.getString("DATA_QUALITY_SCORE"));
				dQScoreBean.setHeader("Open DQ Issues");
			}
			//dbConnection.close_Connection(connection);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}	
		
	/*	}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return dQScoreBean;
	}
	
	
	@SuppressWarnings("static-access")
	public EcdeWithDQModel getEcdeWithDQDetails()throws Exception {
		Connection connection  = dbConnection.connection();
		EcdeWithDQModel ecdeWithDQBean = null;

		String sqlQuery =   "SELECT SUM( " +
							"  CASE " +
							"    WHEN (IS_MONITORED)='Y' " +
							"    AND IS_ECDE        ='Y' " +
							"    THEN 1 " +
							"    ELSE 0 " +
							"  END)*100 " +
							"      / " +
							"SUM( " +
							"CASE " +
							"  WHEN(IS_MONITORED='Y' " +
							"  OR IS_MONITORED  ='N') " +
							"  AND IS_ECDE      ='Y' " +
							"  THEN 1 " +
							"  ELSE 0 " +
							"END) AS ECDE_DQ_MNTR " +
							"FROM V_CDE_1";

		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				ecdeWithDQBean = new EcdeWithDQModel();
				ecdeWithDQBean.setEcdeWithDQScore(dbConnection.resultset.getString("ECDE_DQ_MNTR"));
				ecdeWithDQBean.setHeader("ECDE_DQ_MNTR");
			}
			//dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return ecdeWithDQBean;
	}
	
	@SuppressWarnings("static-access")
	public List<PerstOfAdsProfileModel> getPerstOfAdsProfileDetails(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		PerstOfAdsProfileModel perstOfAdsProfileBean = null;
		List<PerstOfAdsProfileModel> list = new ArrayList<PerstOfAdsProfileModel>();

		String sqlQuery =   "SELECT BUCF_NAME, " +
							"  ADS_CNT, " +
							"  PR_QTR_ADS  *100/TOT_PR_QTR_ADS   AS PR_QTR, " +
							"  CURR_QTR_ADS*100/TOT_CURR_QTR_ADS AS CRNT_QTR " +
							"FROM " +
							"  (SELECT BUCF.BUCF_NAME, " +
							"    COUNT( " +
							"    CASE " +
							"      WHEN DQ_MONITOR.PROFILE_DATE>'0' " +
							"      AND DATASOURCES.ADS_TYPE    ='YES' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)/ (COUNT( " +
							"    CASE " +
							"      WHEN ADS_TYPE='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)) AS PR_QTR_ADS, " +
							"    COUNT( " +
							"    CASE " +
							"      WHEN DQ_MONITOR.PROFILE_DATE                                  >'0' " +
							"      AND TO_DATE(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')<= ? " +
							"      AND ADS_TYPE                                                  ='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)/ (COUNT( " +
							"    CASE " +
							"      WHEN ADS_TYPE='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)) AS CURR_QTR_ADS, " +
							"    COUNT(DISTINCT " +
							"    CASE " +
							"      WHEN ADS_TYPE='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END) AS ADS_CNT " +
							"  FROM V_BUCF BUCF " +
							"  INNER JOIN V_DQ_MONITOR DQ_MONITOR " +
							"  ON BUCF.BUCF_ID = DQ_MONITOR.BUCF_ID " +
							"  INNER JOIN V_CDE_SOURCES CDE_SOURCES " +
							"  ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " +
							"  INNER JOIN V_DATASOURCES DATASOURCES " +
							"  ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID " +
							"  GROUP BY BUCF.BUCF_NAME " +
							"  ) SM " +
							"INNER JOIN " +
							"  (SELECT COUNT( " +
							"    CASE " +
							"      WHEN DQ_MONITOR.PROFILE_DATE>'0' " +
							"      AND ADS_TYPE                ='YES' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)/ (COUNT( " +
							"    CASE " +
							"      WHEN ADS_TYPE='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)) AS TOT_PR_QTR_ADS, " +
							"    COUNT( " +
							"    CASE " +
							"      WHEN DQ_MONITOR.PROFILE_DATE                                  >'0' " +
							"      AND TO_DATE(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')<= ? " +
							"      AND ADS_TYPE                                                  ='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)/ (COUNT( " +
							"    CASE " +
							"      WHEN ADS_TYPE='Y' " +
							"      THEN DATASOURCES.DATASOURCE_ID " +
							"      ELSE 0 " +
							"    END)) AS TOT_CURR_QTR_ADS " +
							"  FROM V_BUCF BUCF " +
							"  INNER JOIN V_DQ_MONITOR DQ_MONITOR " +
							"  ON BUCF.BUCF_ID = DQ_MONITOR.BUCF_ID " +
							"  INNER JOIN V_CDE_SOURCES CDE_SOURCES " +
							"  ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " +
							"  INNER JOIN V_DATASOURCES DATASOURCES " +
							"  ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID " +
							"  ) TOT ON 1                   =1";
		try{

		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1,  new java.sql.Timestamp(currentEndDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(currentEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();

		
		/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){*/
			while(resultSet.next()){
				perstOfAdsProfileBean = new PerstOfAdsProfileModel();
				perstOfAdsProfileBean.setBucfName(resultSet.getString("BUCF_NAME"));
				perstOfAdsProfileBean.setAdsCount(resultSet.getString("ADS_CNT"));
				perstOfAdsProfileBean.setPrQtr(resultSet.getString("PR_QTR"));
				perstOfAdsProfileBean.setCrntQtr(resultSet.getString("CRNT_QTR"));
				perstOfAdsProfileBean.setHeader("% of ADS Profiled");
				
				list.add(perstOfAdsProfileBean);
			}
			//dbConnection.close_Connection(connection);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
/*		}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return list;
	}
	

	
	@SuppressWarnings("static-access")
	public List<DQMonitoringDetailsbySourceSystem> getDQMonitoringDetailsbySourceSystem(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		DQMonitoringDetailsbySourceSystem dqMonitoringDetailsbySourceSystem = null;
		List<DQMonitoringDetailsbySourceSystem> sourceList = new ArrayList<DQMonitoringDetailsbySourceSystem>();

		String sqlQuery = "SELECT " + " 'SOURCE_SYSTEM' AS DIMENSION_FLTR,"
				+ " DATABASE_NAME, "
				+ " ADS as SOURCE_SYSTEM, "
				+ " BUCF_NAME as LOB, "
				+ " CASE WHEN ROWS_PROFILED_CNT = 0 THEN 0 ELSE ROUND((ROWS_PROFILED_CNT - NON_COMPLIANT_CNT)*100/(ROWS_PROFILED_CNT),1) END AS DATA_QUALITY_SCORE, "
				+ " ROUND(((ROWS_PROFILED_CNT - NON_COMPLIANT_CNT)*100/(ROWS_PRFLD_PR_QTR_CNT)),1) AS CHG_FRM_PR_QTR, "
				+ " ROUND((ROWS_PROFILED_CNT-NON_COMPLIANT_CNT)/1000) AS RCRDS_PSD, "
				+ " ROUND(ROWS_PROFILED_CNT/1000) AS ROWS_TSTD " 
				+ " FROM " 
				+ " ( "
				+ " SELECT DATASOURCES.DATABASE_NAME, " 
				+ " CDE_1.ADS, " 
				+ " BUCF.BUCF_NAME, " 
				+ " SUM( " 
				+ " CASE "
				+ " WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "
				+ " THEN CAST(DQ_MONITOR.ROWS_PROFILED as NUMBER) " 
				+ " ELSE 0 " 
				+ " END) AS ROWS_PROFILED_CNT, "
				+ " SUM( " 
				+ " CASE "
				+ " WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ?"
				+ " THEN CAST(DQ_MONITOR.NON_COMPLIANT_COUNT as NUMBER) " 
				+ " ELSE 0 " 
				+ " END) AS NON_COMPLIANT_CNT, "
				+ " SUM( " 
				+ " CASE "
				+ " WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "
				+ " THEN CAST(DQ_MONITOR.ROWS_PROFILED as NUMBER) " 
				+ " ELSE 0 " 
				+ "    END) AS ROWS_PRFLD_PR_QTR_CNT "
				+ " FROM V_DQ_MONITOR DQ_MONITOR " 
				+ " INNER JOIN V_BUCF BUCF "
				+ " ON DQ_MONITOR.BUCF_ID = BUCF.BUCF_ID " 
				+ " INNER JOIN V_CDE_1 CDE_1 "
				+ " ON DQ_MONITOR.CDE_DS_ID = CDE_1.CDE_DS_ID " 
				+ " INNER JOIN V_CDE_SOURCES CDE_SOURCES "
				+ " ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " 
				+ " INNER JOIN V_DATASOURCES DATASOURCES "
				+ " ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID "
				+ " GROUP BY DATASOURCES.DATABASE_NAME, CDE_1.ADS, BUCF.BUCF_NAME " 
				+ " )A  " 
				+ " UNION ALL "
				+ " SELECT  "
				+ " 'LEGAL_ENTITY_LOB' AS DIMENSION_FLTR, " 
				+ " A.BUCF_NAME as LEGAL_ENTITY_LOB, "
				+ " ADS AS SOURCE_SYSTEM,  " 
				+ " BUCF_NAME as LOB,  "
				+ " CASE WHEN ROWS_PROFILED_CNT = 0 THEN 0 ELSE ROUND((ROWS_PROFILED_CNT - NON_COMPLIANT_CNT)*100/(ROWS_PROFILED_CNT),1) END AS DATA_QUALITY_SCORE, "
				+ " ROUND(((ROWS_PROFILED_CNT - NON_COMPLIANT_CNT)*100/(ROWS_PRFLD_PR_QTR_CNT)),1) AS CHG_FRM_PR_QTR, "
				+ " ROUND((ROWS_PROFILED_CNT-NON_COMPLIANT_CNT)/1000) AS RCRDS_PSD, "
				+ " ROUND(ROWS_PROFILED_CNT/1000) AS ROWS_TSTD " 
				+ " FROM " 
				+ " ( " 
				+ " SELECT "
				+ "   DQ_MONITOR.BUCF_NAME as BUCF_NAME, " 
				+ "  CDE_1.ADS, " 
				+ "  SUM( " 
				+ "   CASE "
				+ "     WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "
				+ "     THEN CAST(DQ_MONITOR.ROWS_PROFILED as NUMBER) " 
				+ "    ELSE 0 "
				+ "  END) AS ROWS_PROFILED_CNT, " 
				+ " SUM( " 
				+ "   CASE "
				+ "   WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "
				+ "  THEN CAST(DQ_MONITOR.NON_COMPLIANT_COUNT as NUMBER) " 
				+ "   ELSE 0 "
				+ "   END) AS NON_COMPLIANT_CNT, " 
				+ "  SUM( " 
				+ "   CASE "
				+ "  WHEN TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? AND TO_TIMESTAMP(DQ_MONITOR.PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ?"
				+ "  THEN CAST(DQ_MONITOR.ROWS_PROFILED as NUMBER) " 
				+ " ELSE 0 "
				+ "      END) AS ROWS_PRFLD_PR_QTR_CNT " 
				+ " FROM V_DQ_MONITOR DQ_MONITOR " 
				+ " INNER JOIN V_BUCF BUCF "
				+ " ON DQ_MONITOR.BUCF_ID = BUCF.BUCF_ID " 
				+ " INNER JOIN V_CDE_1 CDE_1 "
				+ " ON DQ_MONITOR.CDE_DS_ID = CDE_1.CDE_DS_ID " 
				+ " INNER JOIN V_CDE_SOURCES CDE_SOURCES "
				+ " ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " 
				+ " INNER JOIN V_DATASOURCES DATASOURCES "
				+ " ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID "
				+ " AND BUCF.BUCF_NAME IN ('QS', 'Legal', 'HR', 'CD', 'XYZ', 'XXXGS') "
				+ " GROUP BY DQ_MONITOR.BUCF_NAME, CDE_1.ADS " 
				+ " )A " 
				+ " ORDER BY 1, 2,3, 4";


/*		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
		*/
		try{

			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(3,  new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(4,  new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(5,  new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(6,  new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(7, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(8,  new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(9,  new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(10,  new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(11,  new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(12,  new java.sql.Timestamp(currentEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
			
			while(resultSet.next()){
				dqMonitoringDetailsbySourceSystem = new DQMonitoringDetailsbySourceSystem();
				dqMonitoringDetailsbySourceSystem.setAds(resultSet.getString("SOURCE_SYSTEM"));
				dqMonitoringDetailsbySourceSystem.setBucfName(resultSet.getString("LOB"));
				dqMonitoringDetailsbySourceSystem.setChgFrmPrQtr(resultSet.getString("CHG_FRM_PR_QTR"));
				dqMonitoringDetailsbySourceSystem.setDatabaseName(resultSet.getString("DATABASE_NAME"));
				dqMonitoringDetailsbySourceSystem.setDataQualityScore(resultSet.getString("DATA_QUALITY_SCORE"));
				dqMonitoringDetailsbySourceSystem.setRcrdsPsd(resultSet.getString("RCRDS_PSD"));
				dqMonitoringDetailsbySourceSystem.setRowsTstd(resultSet.getString("ROWS_TSTD"));
				dqMonitoringDetailsbySourceSystem.setHeader("DQ_MONITORING_BY_SOURCE_SYSTEM");
				dqMonitoringDetailsbySourceSystem.setDimensionFltr(resultSet.getString("DIMENSION_FLTR"));
				sourceList.add(dqMonitoringDetailsbySourceSystem);
			}
			//dbConnection.close_Connection(connection);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
/*		}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return sourceList;
	}
	
	@SuppressWarnings("static-access")
	public List<ECDEandBCDEwithDQmonitoringbyADS> getECDEandBCDEwithDQmonitoringbyADS()throws Exception {
		Connection connection  = dbConnection.connection();
		ECDEandBCDEwithDQmonitoringbyADS eCDEandBCDEwithDQmonitoringbyADS = null;
		List<ECDEandBCDEwithDQmonitoringbyADS> adsList = new ArrayList<ECDEandBCDEwithDQmonitoringbyADS>();

		//Aggregation query
		/*String sqlQuery =   "SELECT ADS, " +
				"  BUCF_NAME, " +
				"  YEAR_QTR, " +
				"  BCDE_TOT, " +
				"  ECDE_TOT " +
				"FROM " +
				"  (SELECT ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    REPLACE(REPLACE(REPLACE(REPLACE(CDE_1.QUARTER_DATE_ADDED, 'Q1', 'Jan-Mar'), 'Q2', 'Apr-Jun'), 'Q3', 'Jul-Sep'), 'Q4', 'Nov-Dec') AS YEAR_QTR, " +
				"    COUNT(DISTINCT " +
				"    CASE " +
				"      WHEN CDE_1.Is_Monitored  ='Y' " +
				"      AND CDE_1.Is_Bcde        ='Y' " +
				"      AND DATASOURCES.Ads_Type ='Yes' " +
				"      THEN CDE_1.Cde_Ds_Id " +
				"      ELSE '0' " +
				"    END) AS BCDE_TOT, " +
				"    COUNT(DISTINCT " +
				"    CASE " +
				"      WHEN CDE_1.Is_Monitored  ='Y' " +
				"      AND CDE_1.Is_Ecde        ='Y' " +
				"      AND DATASOURCES.Ads_Type ='Yes' " +
				"      THEN CDE_1.Cde_Ds_Id " +
				"      ELSE '0' " +
				"    END) AS ECDE_TOT " +
				"  FROM V_DQ_MONITOR DQ_MONITOR " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON DQ_MONITOR.BUCF_ID = BUCF.BUCF_ID " +
				"  INNER JOIN V_CDE_1 CDE_1 " +
				"  ON DQ_MONITOR.CDE_DS_ID = CDE_1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_SOURCES CDE_SOURCES " +
				"  ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " +
				"  INNER JOIN V_DATASOURCES DATASOURCES " +
				"  ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID " +
				"  AND ADS                     IS NOT NULL " +
				"  AND ADS                     <> '' " +
				"  AND BUCF.BUCF_NAME          IS NOT NULL " +
				"  AND BUCF.BUCF_NAME          <> '' " +
				"  GROUP BY ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    CDE_1.QUARTER_DATE_ADDED " +
				"  )";*/
		
		//Non Aggregating Query
		String sqlQuery = "  SELECT ADS, BUCF_NAME, YEAR_QTR, BCDE_TOT, ECDE_TOT FROM  " +
				"  ( " +
				"  SELECT  " +
				"  ADS, BUCF.BUCF_NAME, REPLACE(REPLACE(REPLACE(REPLACE(CDE_1.QUARTER_DATE_ADDED, 'Q1', 'Jan-Mar'), 'Q2', 'Apr-Jun'),  " +
				"  'Q3', 'Jul-Sep'), 'Q4', 'Nov-Dec') AS YEAR_QTR, " +
				"  CASE WHEN CDE_1.Is_Monitored='Y' and CDE_1.Is_Bcde='Y' and DATASOURCES.Ads_Type ='Yes' then CDE_1.Cde_Ds_Id  " +
				"  else '0' " +
				"  end AS BCDE_TOT, " +
				"  CASE WHEN CDE_1.Is_Monitored='Y' and CDE_1.Is_Ecde='Y' and DATASOURCES.Ads_Type ='Yes' then CDE_1.Cde_Ds_Id " +
				"  else '0' " +
				"  end AS ECDE_TOT " +
				"  FROM  V_DQ_MONITOR DQ_MONITOR " +
				"  INNER JOIN  V_BUCF BUCF " +
				"  ON DQ_MONITOR.BUCF_ID = BUCF.BUCF_ID " +
				"  INNER JOIN  V_CDE_1 CDE_1 " +
				"  ON DQ_MONITOR.CDE_DS_ID = CDE_1.CDE_DS_ID " +
				"  INNER JOIN  V_CDE_SOURCES CDE_SOURCES " +
				"  ON DQ_MONITOR.CDE_DS_ID = CDE_SOURCES.CDE_DS_ID " +
				"  INNER JOIN  V_DATASOURCES DATASOURCES " +
				"  ON CDE_SOURCES.DATASOURCE_ID = DATASOURCES.DATASOURCE_ID " +
				"  AND ADS IS NOT NULL AND ADS <> '' AND BUCF.BUCF_NAME is NOT NULL AND BUCF.BUCF_NAME <> '' " +
				"  ) ";

		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				eCDEandBCDEwithDQmonitoringbyADS = new ECDEandBCDEwithDQmonitoringbyADS();
				eCDEandBCDEwithDQmonitoringbyADS.setAds(dbConnection.resultset.getString("ADS"));
				eCDEandBCDEwithDQmonitoringbyADS.setBucfName(dbConnection.resultset.getString("BUCF_NAME"));
				eCDEandBCDEwithDQmonitoringbyADS.setYearQtr(dbConnection.resultset.getString("YEAR_QTR"));
				eCDEandBCDEwithDQmonitoringbyADS.setBcdeTot(dbConnection.resultset.getString("BCDE_TOT"));
				eCDEandBCDEwithDQmonitoringbyADS.setEcdeTot(dbConnection.resultset.getString("ECDE_TOT"));			
				eCDEandBCDEwithDQmonitoringbyADS.setHeader("ECDE_AND_BCDE_WITH_DQ_MONITORING_BY_ADS");
				adsList.add(eCDEandBCDEwithDQmonitoringbyADS);
			}
				//dbConnection.close_Connection(connection);
			/*}else if(flag==0){			
				dbConnection.close_Connection(connection);*/
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return adsList;
	}

	@SuppressWarnings("static-access")
	public OpenDQIssues getOpenDQIssues() throws Exception {
		Connection connection  = dbConnection.connection();
		OpenDQIssues openDQIssues = null;

		String sqlQuery =   "SELECT "+
							"COUNT( DISTINCT "+
							            " CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_DATE('2014-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
							            " AND TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_DATE('2014-09-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
							            " THEN ISSUE_ID ELSE 0 END "+
							      " ) AS Prior_Quarter, "+
							" COUNT( DISTINCT "+
							           "  CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_DATE('2014-10-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
							           "  AND TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_DATE('2014-12-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
							           "  THEN ISSUE_ID ELSE 0 END "+
							    "  ) AS Current_Quarter "+
							" FROM V_DQ_ISSUE ";


		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				openDQIssues = new OpenDQIssues();
				openDQIssues.setHeader("openDQIssues");
				openDQIssues.setCurrentQuarter(dbConnection.resultset.getString("Current_Quarter"));
				openDQIssues.setPriorQuarter(dbConnection.resultset.getString("Prior_Quarter"));
			}
			dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return openDQIssues;
	}
	@SuppressWarnings("static-access")
	public OpenDQIssues getOpenDQIssues(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception {
				
		Connection connection  = dbConnection.connection();
		OpenDQIssues openDQIssues = null;

		String sqlQuery =   "SELECT "+
							"COUNT( DISTINCT "+
							            " CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? "+
							            " AND TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
							            " THEN ISSUE_ID ELSE 0 END "+
							      " ) AS Prior_Quarter, "+
							" COUNT( DISTINCT "+
							           "  CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ?"+
							           "  AND TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ?"+
							           "  THEN ISSUE_ID ELSE 0 END "+
							    "  ) AS Current_Quarter "+
							" FROM V_DQ_ISSUE ";


		
		try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(3,  new java.sql.Timestamp(currentStartDate.getTime()));
		prepStmt.setTimestamp(4,  new java.sql.Timestamp(currentEndDate.getTime()));
		
		ResultSet resultSet=prepStmt.executeQuery();
		
		/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){*/
			while(resultSet.next()){
				openDQIssues = new OpenDQIssues();
				openDQIssues.setHeader("openDQIssues");
				openDQIssues.setCurrentQuarter(resultSet.getString("Current_Quarter"));
				openDQIssues.setPriorQuarter(resultSet.getString("Prior_Quarter"));
			}
		//	dbConnection.close_Connection(connection);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		/*}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return openDQIssues;
	}


	@SuppressWarnings("static-access")
	public DataQualityMonitering getDataMonitering() throws Exception {
		Connection connection  = dbConnection.connection();
		DataQualityMonitering dataQualityMonitering = null;

		String sqlQuery =   "SELECT "+
				"ROUND((((SM_RWS_PRFLD_RNG1-SM_NON_CMPLNT_CNT_RNG1)/SM_RWS_PRFLD_RNG1) - ((SM_RWS_PRFLD_RNG2-SM_NON_CMPLNT_CNT_RNG2)/SM_RWS_PRFLD_RNG3) )*100,2) AS DQ_SCORE, "+
				"ROUND((SM_RWS_PRFLD_RNG2-SM_NON_CMPLNT_CNT_RNG2)*100/SM_RWS_PRFLD_RNG3, 2) AS Change_From_Prior_Quarter "+
				"FROM "+
				"( "+
				"SELECT "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_TIMESTAMP('2014-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_TIMESTAMP('2014-05-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG1, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_TIMESTAMP('2014-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_TIMESTAMP('2014-05-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
				"THEN CAST(VDQPRFL.NON_COMPLIANT_COUNT AS NUMBER) ELSE 0 END) AS SM_NON_CMPLNT_CNT_RNG1,"+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_TIMESTAMP('2014-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_TIMESTAMP('2014-09-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG2, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_TIMESTAMP('2014-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_TIMESTAMP('2014-09-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
				"THEN CAST(VDQPRFL.NON_COMPLIANT_COUNT AS NUMBER) ELSE 0 END) AS SM_NON_CMPLNT_CNT_RNG2, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_TIMESTAMP('2014-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= TO_TIMESTAMP('2014-09-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG3 "+
				"FROM V_DQ_PROFILE VDQPRFL "+
				"WHERE Profile_Date <> '' "+
				") TBL";
		
		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				dataQualityMonitering = new DataQualityMonitering();
				dataQualityMonitering.setHeader("dataQualityDQScore");
				dataQualityMonitering.setDqScore(dbConnection.resultset.getString("DQ_SCORE"));
				dataQualityMonitering.setChangeFromPriorQuarter(dbConnection.resultset.getString("Change_From_Prior_Quarter"));
			}
			//dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return dataQualityMonitering;
	}

	@SuppressWarnings("static-access")
	public DataQualityMonitering getDataMonitering(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception {
		Connection connection  = dbConnection.connection();
		DataQualityMonitering dataQualityMonitering = null;

		String sqlQuery =   "SELECT "+
				"ROUND((((SM_RWS_PRFLD_RNG1-SM_NON_CMPLNT_CNT_RNG1)/SM_RWS_PRFLD_RNG1) - ((SM_RWS_PRFLD_RNG2-SM_NON_CMPLNT_CNT_RNG2)/SM_RWS_PRFLD_RNG3) )*100,2) AS DQ_SCORE, "+
				"ROUND((SM_RWS_PRFLD_RNG2-SM_NON_CMPLNT_CNT_RNG2)*100/SM_RWS_PRFLD_RNG3, 2) AS Change_From_Prior_Quarter "+
				"FROM "+
				"( "+
				"SELECT "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG1, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
				"THEN CAST(VDQPRFL.NON_COMPLIANT_COUNT AS NUMBER) ELSE 0 END) AS SM_NON_CMPLNT_CNT_RNG1,"+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG2, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
				"THEN CAST(VDQPRFL.NON_COMPLIANT_COUNT AS NUMBER) ELSE 0 END) AS SM_NON_CMPLNT_CNT_RNG2, "+
				"SUM(CASE WHEN TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? AND TO_TIMESTAMP(PROFILE_DATE, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
				"THEN CAST(VDQPRFL.ROWS_PROFILED AS NUMBER) ELSE 0 END) AS SM_RWS_PRFLD_RNG3 "+
				"FROM V_DQ_PROFILE VDQPRFL "+
				"WHERE Profile_Date <> '' "+
				") TBL";
		try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(3,  new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(4,  new java.sql.Timestamp(priorEndDate.getTime()));
		prepStmt.setTimestamp(5, new java.sql.Timestamp(currentStartDate.getTime()));
		prepStmt.setTimestamp(6,  new java.sql.Timestamp(currentEndDate.getTime()));
		prepStmt.setTimestamp(7,  new java.sql.Timestamp(currentStartDate.getTime()));
		prepStmt.setTimestamp(8,  new java.sql.Timestamp(currentEndDate.getTime()));
		prepStmt.setTimestamp(9,  new java.sql.Timestamp(priorStartDate.getTime()));
		prepStmt.setTimestamp(10,  new java.sql.Timestamp(currentEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();
		
			while(resultSet.next()){
				dataQualityMonitering = new DataQualityMonitering();
				dataQualityMonitering.setHeader("dataQualityDQScore");
				dataQualityMonitering.setDqScore(resultSet.getString("DQ_SCORE"));
				dataQualityMonitering.setChangeFromPriorQuarter(resultSet.getString("Change_From_Prior_Quarter"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		//	dbConnection.close_Connection(connection);
		return dataQualityMonitering;
	}

	@SuppressWarnings("static-access")
	public List<DQMoniteringStats> getDQMoniteringStats() throws Exception {
		Connection connection  = dbConnection.connection();
		DQMoniteringStats dQMoniteringStats = null;
		List<DQMoniteringStats> list = new ArrayList<DQMoniteringStats>();

		String sqlQuery =   "SELECT  DISTINCT   "+
				"CASE WHEN VCDE2.IS_BCDE = 'Y' THEN 'BCDEs' ELSE 'ECDEs' END as LABEL,  "+
				"ROUND((1-(VCDE2.IS_MNTRD_Y/VCDE2.IS_MNTRD))*100) AS Not_DQ_Monitored, "+
				" ROUND(VCDE2.IS_MNTRD_Y*100/VCDE2.IS_MNTRD) AS DQ_Monitored, "+
				"DECODE(SUBSTR(VCDE1.QUARTER_DATE_ADDED, 1,2), 'Q1', 'Jan-Mar', 'Q2', 'Apr-Jun', 'Q3', 'Jul-Sep', 'Q4', 'Oct-Dec', NULL) || ' ' || SUBSTR(VCDE1.QUARTER_DATE_ADDED, 4) AS YearQuarter, "+
				"VCDE1.BUCF_NAME as LOB,  VCDE1.ADS AS Source_System  FROM V_CDE_1 VCDE1 INNER JOIN     ( SELECT      VCDE.IS_BCDE,      CAST(SUM(CASE WHEN VCDE.IS_MONITORED = 'Y' THEN 1 ELSE 0 END) AS NUMBER) AS IS_MNTRD_Y,     CAST(SUM(CASE WHEN VCDE.IS_MONITORED in ('Y', 'N') THEN 1 ELSE 0 END)  AS NUMBER) AS IS_MNTRD    FROM V_CDE_1 VCDE GROUP BY VCDE.IS_BCDE) VCDE2 ON VCDE1.IS_BCDE = VCDE2.IS_BCDE";
		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				dQMoniteringStats = new DQMoniteringStats();
				dQMoniteringStats.setHeader("dataMoniteringStats");
				dQMoniteringStats.setLabel(dbConnection.resultset.getString("LABEL"));
				dQMoniteringStats.setNotDQMonitered(dbConnection.resultset.getString("Not_DQ_Monitored"));
				dQMoniteringStats.setDqMonitered(dbConnection.resultset.getString("DQ_Monitored"));
				dQMoniteringStats.setYearQuarter(dbConnection.resultset.getString("YearQuarter"));
				dQMoniteringStats.setLob(dbConnection.resultset.getString("LOB"));
				dQMoniteringStats.setSourceSystem(dbConnection.resultset.getString("Source_System"));
				list.add(dQMoniteringStats);
			}
			//dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return list;
	}


	@SuppressWarnings("static-access")
	public DQRIAndDQPScores getDQRIAndDQPScores() throws Exception {
		Connection connection  = dbConnection.connection();
		DQRIAndDQPScores dQRIAndDQPScores = null;

		/*String sqlQuery =   "SELECT  "+
				" SUM(TOTAL_ROWS)/1000000 AS Total_Records, "+
				" ROUND( "+
				"    SUM(CASE WHEN DQRI IS NOT NULL and TOTAL_ROWS IS NOT NULL THEN (TOTAL_ROWS)*(DQRI) ELSE (DQRI_RESULT_ID) END)/ "+
				"    SUM(CASE WHEN DQRI IS NOT NULL AND TOTAL_ROWS IS NOT NULL then (TOTAL_ROWS) ELSE (DQRI_RESULT_ID) END)  "+
				"    , 1) AS DQRI_Score, "+
				" ROUND( "+
				"    SUM(CASE WHEN DQ_PROFILING_SCORE IS NOT NULL AND TOTAL_ROWS IS NOT NULL THEN ((TOTAL_ROWS)*(DQ_PROFILING_SCORE)) ELSE (DQRI_RESULT_ID) END) *100/ "+
				"    SUM(CASE WHEN DQ_PROFILING_SCORE IS NOT NULL AND TOTAL_ROWS IS NOT NULL THEN (TOTAL_ROWS) ELSE (DQRI_RESULT_ID) END)  "+
				"    ) AS DQP_Score, "+
				" ROUND( "+
				"    AVG((IMPACT)) "+
				"    , 1) AS Impact_Score "+
				"FROM  "+
				"( "+
				"SELECT  "+
				"CASE WHEN TOTAL_ROWS = '' THEN NULL ELSE CAST(TOTAL_ROWS AS NUMBER) END as TOTAL_ROWS, "+
				"CASE WHEN DQRI = '' THEN NULL ELSE CAST(DQRI AS NUMBER) END as DQRI, "+
				"CASE WHEN DQRI_RESULT_ID = '' THEN NULL ELSE CAST(DQRI_RESULT_ID AS NUMBER) END as DQRI_RESULT_ID, "+
				"CASE WHEN DQ_PROFILING_SCORE = '' THEN NULL ELSE CAST(DQ_PROFILING_SCORE AS NUMBER) END as DQ_PROFILING_SCORE, "+
				"CASE WHEN IMPACT = '' THEN NULL ELSE CAST(IMPACT AS NUMBER) END AS IMPACT "+
				"FROM V_DQRI_RESULT "+
				") TBL";*/
		
		//Updated Query
		String sqlQuery = "SELECT  "+
				" CAST(ROUND(SUM(TOTAL_ROWS)/1000000) as INTEGER) AS Total_Records, "+
				" ROUND( "+
				" SUM(CASE WHEN DQRI IS NOT NULL and TOTAL_ROWS IS NOT NULL THEN (TOTAL_ROWS)*(DQRI) ELSE (DQRI_RESULT_ID) END)/ "+
				" SUM(CASE WHEN DQRI IS NOT NULL AND TOTAL_ROWS IS NOT NULL then (TOTAL_ROWS) ELSE (DQRI_RESULT_ID) END)  "+
				" , 1) AS DQRI_Score, "+
				" CAST(  "+
				" ROUND( "+
				" SUM(CASE WHEN DQ_PROFILING_SCORE IS NOT NULL AND TOTAL_ROWS IS NOT NULL THEN ((TOTAL_ROWS)*(DQ_PROFILING_SCORE)) ELSE (DQRI_RESULT_ID) END) *100/ "+
				" SUM(CASE WHEN DQ_PROFILING_SCORE IS NOT NULL AND TOTAL_ROWS IS NOT NULL THEN (TOTAL_ROWS) ELSE (DQRI_RESULT_ID) END)  "+
				" )  "+
				" as INTEGER )AS DQP_Score, "+
				" ROUND( "+
				" AVG((IMPACT)) "+
				" , 1) AS Impact_Score "+
				" FROM  "+
				" ( "+
				" SELECT "+ 
				" CASE WHEN DQRR.TOTAL_ROWS = '' THEN NULL ELSE CAST(DQRR.TOTAL_ROWS AS NUMBER) END as TOTAL_ROWS, "+
				" CASE WHEN DQRR.DQRI = '' THEN NULL ELSE CAST(DQRR.DQRI AS NUMBER) END as DQRI, "+
				" CASE WHEN DQRR.DQRI_RESULT_ID = '' THEN NULL ELSE CAST(DQRR.DQRI_RESULT_ID AS NUMBER) END as DQRI_RESULT_ID, "+
				" CASE WHEN DQRR.DQ_PROFILING_SCORE = '' THEN NULL ELSE CAST(DQRR.DQ_PROFILING_SCORE AS NUMBER) END as DQ_PROFILING_SCORE, "+
				" CASE WHEN DQRR.IMPACT = '' THEN NULL ELSE CAST(DQRR.IMPACT AS NUMBER) END AS IMPACT "+
				" FROM  V_DQRI_RESULT DQRR "+
				" INNER JOIN  V_BUSINESS_PROCESSES VBP "+
				" ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID "+
				" INNER JOIN  V_CDE_1 VDE1 "+
				" ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID "+
				" INNER JOIN  V_CDE_OWNER CDO "+
				" ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID "+
				" INNER JOIN  V_BUCF BUCF "+
				" ON CDO.BUCF_ID = BUCF.BUCF_ID "+
				" ) TBL ";
		try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				dQRIAndDQPScores = new DQRIAndDQPScores();
				dQRIAndDQPScores.setHeader("dqriandDQPScores");
				dQRIAndDQPScores.setTotalRecords(dbConnection.resultset.getString("Total_Records"));
				dQRIAndDQPScores.setDqriScore(dbConnection.resultset.getString("DQRI_Score"));
				dQRIAndDQPScores.setDqpScore(dbConnection.resultset.getString("DQP_Score"));
				dQRIAndDQPScores.setImpactScore(dbConnection.resultset.getString("Impact_Score"));
			}
			//dbConnection.close_Connection(connection);
		}/*else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		}catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return dQRIAndDQPScores;
	}

	

	@SuppressWarnings("static-access")
	public List<HighPriorityDQIssues> getListOfHPDQIssues(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception {
		
	
        System.out.println("Connecting to a selected database..."); 
		Connection connection  = dbConnection.connection();
        System.out.println("Connected database successfully..."); 

        SimpleDateFormat  simpleDateformatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat  simpleDateformat = new SimpleDateFormat("MMM"); 
        String priorStartMonth = simpleDateformat.format(priorStartDate);
        String currentStartMonth = simpleDateformat.format(currentStartDate);
        
        HashMap <String,String> quarterMonthMap = new HashMap<String,String>();
        quarterMonthMap.put("Jan", "Jan-Mar");
        quarterMonthMap.put("Feb", "Jan-Mar");
        quarterMonthMap.put("Mar", "Jan-Mar");
        quarterMonthMap.put("Apr", "Apr-Jun");
        quarterMonthMap.put("May", "Apr-Jun");
        quarterMonthMap.put("Jun", "Apr-Jun");
        quarterMonthMap.put("Jul", "Jul-Sep");
        quarterMonthMap.put("Aug", "Jul-Sep");
        quarterMonthMap.put("Sep", "Jul-Sep");
        quarterMonthMap.put("Oct", "Oct-Dec");
        quarterMonthMap.put("Nov", "Oct-Dec");
        quarterMonthMap.put("Dec", "Oct-Dec");
        
        HashMap <String,String> quarterMonthNumMap = new HashMap<String,String>();
        //'Jan-Mar', 'Q2', 'Apr-Jun', 'Q3', 'Jul-Sep', 'Q4', 'Oct-Dec'
        quarterMonthNumMap.put("Jan", "1, 2, 3");
        quarterMonthNumMap.put("Feb", "1, 2, 3");
        quarterMonthNumMap.put("Mar", "1, 2, 3");
        quarterMonthNumMap.put("Apr", "4, 5, 6");
        quarterMonthNumMap.put("May", "4, 5, 6");
        quarterMonthNumMap.put("Jun", "4, 5, 6");
        quarterMonthNumMap.put("Jul", "7, 8, 9");
        quarterMonthNumMap.put("Aug","7, 8, 9");
        quarterMonthNumMap.put("Sep", "7, 8, 9");
        quarterMonthNumMap.put("Oct", "10, 11, 12");
        quarterMonthNumMap.put("Nov", "10, 11, 12");
        quarterMonthNumMap.put("Dec", "10, 11, 12");

		HighPriorityDQIssues highPriorityDQIssues = null;
		Statement stmt = null;
		List<HighPriorityDQIssues> list = new ArrayList<HighPriorityDQIssues>();
/*		String sqlQuery =   "SELECT  "+
				"CASE WHEN ISS_AGNG_DTL.MONTH IN ( ) THEN 'Jul-Sep 2014' WHEN ISS_AGNG_DTL.MONTH IN (10, 11, 12) THEN 'Oct-Dec 2014' ELSE NULL END AS NBR_OF_DAYS_OPEN, "+
				"ISS_AGNG_DTL.AGING_BUCKET AS AGING_BUCKET,  "+
				"SUM(CAST(ISS_AGNG_DTL.OPENED_COUNT as NUMBER)) AS NBR_OF_ISSUES "+
				"FROM V_ISSUE_AGING_DETAIL ISS_AGNG_DTL "+
				"INNER JOIN V_BUCF BUCF "+
				"ON ISS_AGNG_DTL.BUCF_ID = BUCF.BUCF_ID "+
				"INNER JOIN V_CDE_OWNER VCDE_OWNR "+
				" ON BUCF.BUCF_ID = VCDE_OWNR.BUCF_ID "+
				"INNER JOIN V_CDE_1 VCDE1 "+
				"ON VCDE_OWNR.CDE_DS_ID = VCDE1.CDE_DS_ID "+
				"AND CASE WHEN ISS_AGNG_DTL.MONTH IN (7,8,9) THEN 'Jul-Sep 2014' WHEN ISS_AGNG_DTL.MONTH IN (10, 11, 12) THEN 'Oct-Dec 2014' ELSE NULL END IS NOT NULL "+
				"GROUP BY CASE WHEN ISS_AGNG_DTL.MONTH IN (7,8,9) THEN 'Jul-Sep 2014' WHEN ISS_AGNG_DTL.MONTH IN (10, 11, 12) THEN 'Oct-Dec 2014' ELSE NULL END, ISS_AGNG_DTL.AGING_BUCKET,ISS_AGNG_DTL.MONTH ";
*/		
        String priorQuarterMonth = quarterMonthMap.get(priorStartMonth) + " " + simpleDateformatYear.format(priorStartDate);
        String currentQuarterMonth = quarterMonthMap.get(currentStartMonth)+ " " + simpleDateformatYear.format(currentStartDate);
        String priorQuarteMonthNum = quarterMonthNumMap.get(priorStartMonth) ;
        String currentQuarteMonthNum = quarterMonthNumMap.get(currentStartMonth);
        
		
		String sqlQuery = "SELECT  "+
				" CASE WHEN ISS_AGNG_DTL.MONTH IN ("+ priorQuarteMonthNum +") THEN '"+ priorQuarterMonth +"' "
				+ "WHEN ISS_AGNG_DTL.MONTH IN (" + currentQuarteMonthNum + ") THEN '" + currentQuarterMonth + "' ELSE NULL END AS NBR_OF_DAYS_OPEN, "+
				" ISS_AGNG_DTL.AGING_BUCKET AS AGING_BUCKET,  "+
				" SUM(CAST(ISS_AGNG_DTL.OPENED_COUNT as NUMBER)) AS NBR_OF_ISSUES "+
				" FROM  V_ISSUE_AGING_DETAIL ISS_AGNG_DTL "+
				" WHERE CASE WHEN ISS_AGNG_DTL.MONTH IN ("+ priorQuarteMonthNum +") THEN '"+ priorQuarterMonth +"' "
				+ "WHEN ISS_AGNG_DTL.MONTH IN (" + currentQuarteMonthNum + ") THEN '" + currentQuarterMonth + "' ELSE NULL END IS NOT NULL "+
				" GROUP BY CASE WHEN ISS_AGNG_DTL.MONTH IN ("+ priorQuarteMonthNum +") THEN '"+ priorQuarterMonth +"' "
				+ "WHEN ISS_AGNG_DTL.MONTH IN (" + currentQuarteMonthNum + ") THEN '" + currentQuarterMonth + "' ELSE NULL END, ISS_AGNG_DTL.AGING_BUCKET ";
		
	try{
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
		
		 while(dbConnection.resultset.next()){
				highPriorityDQIssues = new HighPriorityDQIssues();
				highPriorityDQIssues.setHeader("dqriandDQPScores");
				highPriorityDQIssues.setNbrDaysOpen(dbConnection.resultset.getString("NBR_OF_DAYS_OPEN"));
				highPriorityDQIssues.setAgingBucket(dbConnection.resultset.getString("AGING_BUCKET"));
				highPriorityDQIssues.setNbrOfIssues(dbConnection.resultset.getString("NBR_OF_ISSUES"));
				list.add(highPriorityDQIssues);
			}
		}
			/*dbConnection.close_Connection(connection);
		}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
	}catch (Exception e) {
		e.printStackTrace();
		System.out.println(e.getMessage());
	}finally{
		if(null != connection){
			dbConnection.close_Connection(connection);
		}
	}

		return list;
	}

	



	public OpenDQIssueCount getOpenDQIssueCount() throws Exception {

		OpenDQIssueCount openDQIssueCount = null;
		/*Connection connection  = dbConnection.connection();
		String sqlQuery = "";
		//TODO
		
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			while(dbConnection.resultset.next()){
				openDQIssueCount = new OpenDQIssueCount();
				openDQIssueCount.setHeader("openIssueDQCount");
				openDQIssueCount.setOpnDQCount(dbConnection.resultset.getString(""));
			}
			dbConnection.close_Connection(connection);
		}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return openDQIssueCount;
	}



	public List<OpenDQIussuesPriorVsCurntQutr> getDQIussuesPriorVsCurntQutrList() throws Exception {
		
		List<OpenDQIussuesPriorVsCurntQutr> listOpenDQIssuePriorVsCurrQuatr = null;
		
		/*Connection connection  = dbConnection.connection();
		OpenDQIussuesPriorVsCurntQutr openDQIussuesPriorVsCurntQutr = null;
		String sqlQuery = "";
		//TODO
		
		int flag = dbConnection.data_Retrive(sqlQuery , connection);
		if(flag == 1){
			listOpenDQIssuePriorVsCurrQuatr = new ArrayList<OpenDQIussuesPriorVsCurntQutr>();
			while(dbConnection.resultset.next()){
				openDQIussuesPriorVsCurntQutr = new OpenDQIussuesPriorVsCurntQutr();
				openDQIussuesPriorVsCurntQutr.setHeader("openIssueDQCount");
				openDQIussuesPriorVsCurntQutr.setChange(dbConnection.resultset.getString(""));
				openDQIussuesPriorVsCurntQutr.setLegalEntityLob(dbConnection.resultset.getString(""));
				openDQIussuesPriorVsCurntQutr.setCurrentQuarter(dbConnection.resultset.getString(""));
				openDQIussuesPriorVsCurntQutr.setPriorQuarter(dbConnection.resultset.getString(""));
				
				listOpenDQIssuePriorVsCurrQuatr.add(openDQIussuesPriorVsCurntQutr);
			}
			dbConnection.close_Connection(connection);
		}else if(flag==0){			
			dbConnection.close_Connection(connection);
		}*/
		return listOpenDQIssuePriorVsCurrQuatr;
	}
	
/*Queries for Measure/Remediate Data Quality Begin*/
	
	@SuppressWarnings("static-access")
	public List<OpenDQIssuesSummaryModel> getOpenDQIssuesSummaryModel(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception{
		List<OpenDQIssuesSummaryModel> openDQIssuesSummaryModelLst = new ArrayList<OpenDQIssuesSummaryModel>();
		
		String sqlQuery = "SELECT DISTINCT "+ 
							"VCDE1.BUCF_NAME as \"Legal Entity / LOB\", "+
							" DECODE(SUBSTR(VCDE1.QUARTER_DATE_ADDED, 1,2), 'Q1', 'Jan-Mar', 'Q2', 'Apr-Jun', 'Q3', 'Jul-Sep', 'Q4', 'Oct-Dec', NULL) || ' ' || SUBSTR(VCDE1.QUARTER_DATE_ADDED, 4) AS \"Year - Quarter\", "+
							" VCDE1.ADS AS \"Source System\", "+
							" VCDE1.BUCF_NAME as \"LOB\", "+
							" TBL.PRIOR_QUARTER, "+
							" TBL.CURRENT_QUARTER, "+
							" TBL.CHANGE "+
							" FROM V_CDE_1 VCDE1 "+
							" INNER JOIN "+
							" ( "+
							" SELECT DISTINCT VBUCF.BUCF_NAME, "+
							" COUNT(DISTINCT CASE WHEN TO_DATE(Entered_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? and TO_DATE(Entered_Date, 'YYYY-MM-DD HH24:MI:SS')<= ? "+
							" THEN ISSUE_ID ELSE 0 END) AS CURRENT_QUARTER, "+
							" COUNT( DISTINCT CASE WHEN TO_DATE(Entered_Date, 'YYYY-MM-DD HH24:MI:SS')>= ? and TO_DATE(Entered_Date, 'YYYY-MM-DD HH24:MI:SS')<= ? "+
							" then Issue_Id ELSE 0 END) AS PRIOR_QUARTER, "+
							" ( "+
							" COUNT(DISTINCT CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? and TO_DATE(Entered_Date, 'YYYY-MM-DD HH24:MI:SS') <= ? "+
							" THEN ISSUE_ID ELSE 0 END)  "+
							"   - "+
							"   COUNT(DISTINCT CASE WHEN TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? and TO_DATE(ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')<= ? "+
							"   THEN ISSUE_ID ELSE 0 END) "+
							" ) AS CHANGE "+
							" FROM V_DQ_ISSUE VDQIS "+
							" INNER JOIN V_CDE_1 VCDE1 "+ 
							" ON VDQIS.CDE_DS_ID = VCDE1.CDE_DS_ID "+
							" INNER JOIN V_CDE_OWNER VCDEOWNR "+
							" ON VDQIS.CDE_DS_ID = VCDEOWNR.CDE_DS_ID "+
							" INNER JOIN V_BUCF VBUCF "+
							" ON VCDEOWNR.BUCF_ID = VBUCF.BUCF_ID "+
							" GROUP BY VBUCF.BUCF_NAME "+
							" ) TBL "+
							" ON VCDE1.BUCF_NAME = TBL.BUCF_NAME;";
		
		
		Connection connection = null;
		try{
			System.out.println("Inside getOpenDQIssuesSummaryModel");
			connection  = dbConnection.connection();
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(2, new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(3, new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(4,  new java.sql.Timestamp(priorEndDate.getTime()));
			prepStmt.setTimestamp(5, new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(6,  new java.sql.Timestamp(priorEndDate.getTime()));
			prepStmt.setTimestamp(7,  new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(8,  new java.sql.Timestamp(currentEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
		
			/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
			
			if(flag==1){*/
				while(resultSet.next()){
					OpenDQIssuesSummaryModel model = new OpenDQIssuesSummaryModel();
					model.setChange(resultSet.getString("CHANGE"));
					model.setCurrentQuarter(resultSet.getString("CURRENT_QUARTER"));
					model.setLegalEntity(resultSet.getString("Legal Entity / LOB"));
					model.setLob(resultSet.getString("LOB"));
					model.setPriorQuarter(resultSet.getString("PRIOR_QUARTER"));
					model.setSourceSystem(resultSet.getString("Source System"));
					model.setYearQuarter(resultSet.getString("Year - Quarter"));
					
					openDQIssuesSummaryModelLst.add(model);
				//}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return openDQIssuesSummaryModelLst;
	}
	
	@SuppressWarnings("static-access")
	public OpenDQIssuesTypeDetails getOpenDQIssuesTypeDetails(java.util.Date priorStartDate,java.util.Date priorEndDate,
			java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception{
		OpenDQIssuesTypeDetails openDQIssuesTypeDetails = new OpenDQIssuesTypeDetails();
		Map<String,List<DQDimension>> openDQIssuesTypeDetailsMap = new HashMap<String,List<DQDimension>>();
		openDQIssuesTypeDetails.setOpenDQIssuesTypeDetailsMap(openDQIssuesTypeDetailsMap);
		
		//Aggregated Query
		/*String sqlQuery = "SELECT DISTINCT VBUCF.BUCF_NAME, VDQIS.DQ_DIMENSION, "+
							" COUNT( DISTINCT CASE WHEN TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= TO_DATE('2014-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')<=TO_DATE('2014-09-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+ 
							" THEN VDQIS.ISSUE_ID ELSE 0 END) AS PRIOR_QUARTER, "+
							" COUNT( DISTINCT CASE WHEN TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')>=TO_DATE('2014-10-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')<= TO_DATE('2014-12-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') "+
							" THEN VDQIS.ISSUE_ID ELSE 0 END) AS CURRENT_QUARTER "+
							" FROM V_DQ_ISSUE VDQIS "+
							" INNER JOIN V_CDE_1 VCDE1 "+ 
							" ON VDQIS.CDE_DS_ID = VCDE1.CDE_DS_ID "+
							" INNER JOIN V_CDE_OWNER VCDEOWNR "+
							" ON VDQIS.CDE_DS_ID = VCDEOWNR.CDE_DS_ID "+
							" INNER JOIN V_BUCF VBUCF "+
							" ON VCDEOWNR.BUCF_ID = VBUCF.BUCF_ID "+
							" GROUP BY VBUCF.BUCF_NAME, VDQIS.DQ_DIMENSION;";*/
		
		//Non Aggregated Query
		String sqlQuery = " SELECT DISTINCT  " +
				" VBUCF.BUCF_NAME, VDQIS.DQ_DIMENSION, " +
				" REPLACE(REPLACE(REPLACE(REPLACE(VCDE1.QUARTER_DATE_ADDED, 'Q1', 'Jan-Mar'), 'Q2', 'Apr-Jun'), 'Q3', 'Jul-Sep'), 'Q4', 'Nov-Dec')  " +
				" AS YEAR_QTR, " +
				" CASE WHEN TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS') >= ? and  " + 
				" TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')<= ? " +
				" THEN VDQIS.ISSUE_ID ELSE 0 END AS PRIOR_QUARTER, " +
				" CASE WHEN TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')>= ? and " +
				" TO_DATE(VDQIS.ENTERED_DATE, 'YYYY-MM-DD HH24:MI:SS')<= ? " +
				" THEN VDQIS.ISSUE_ID ELSE 0 END AS CURRENT_QUARTER " +
				" FROM  V_DQ_ISSUE VDQIS " +
				" INNER JOIN  V_CDE_1 VCDE1  " +
				" ON VDQIS.CDE_DS_ID = VCDE1.CDE_DS_ID " +
				" INNER JOIN  V_CDE_OWNER VCDEOWNR " +
				" ON VDQIS.CDE_DS_ID = VCDEOWNR.CDE_DS_ID " +
				" INNER JOIN  V_BUCF VBUCF " +
				" ON VCDEOWNR.BUCF_ID = VBUCF.BUCF_ID " +
				" ORDER BY 1,2,3";

		Connection connection = null;
		
		try{
			System.out.println("Inside getOpenDQIssuesTypeDetails");
			connection  = dbConnection.connection();
		/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
			
			if(flag==1){*/
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(priorEndDate.getTime()));
			prepStmt.setTimestamp(3,  new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(4,  new java.sql.Timestamp(currentEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
				while(resultSet.next()){
					String bucfName = resultSet.getString("BUCF_NAME");
					DQDimension dqDimnsn = new DQDimension();
					dqDimnsn.setDqDimension(resultSet.getString("DQ_DIMENSION"));
					dqDimnsn.setCurrentQuarter(resultSet.getString("CURRENT_QUARTER"));
					dqDimnsn.setPriorQuarter(resultSet.getString("PRIOR_QUARTER"));
					dqDimnsn.setYearQtr(resultSet.getString("YEAR_QTR"));
					
					if(CollectionUtils.isEmpty(openDQIssuesTypeDetailsMap.get(bucfName))){
						List<DQDimension> dqDimensionLst = new ArrayList<DQDimension>();
						dqDimensionLst.add(dqDimnsn);
						openDQIssuesTypeDetailsMap.put(bucfName, dqDimensionLst);
					}
					else{
						openDQIssuesTypeDetailsMap.get(bucfName).add(dqDimnsn);
					}
				}
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		
		return openDQIssuesTypeDetails;
	}
	
	@SuppressWarnings("static-access")
	public OpenDQPrioritySummary getOpenDQPrioritySummary(java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception{
		OpenDQPrioritySummary openDQPrioritySummary = new OpenDQPrioritySummary();
		Map<String,List<OpenDQPrioritySummaryDetail>> openDQPrioritySummaryMap = new HashMap<String,List<OpenDQPrioritySummaryDetail>>();
		openDQPrioritySummary.setOpenDQPrioritySummaryMap(openDQPrioritySummaryMap);
		
		String sqlQuery = "SELECT  "+
							" LEGAL_ENTITY, "+
							" DAYS, "+
							" SM_PRIOR_ENDING_COUNT AS FROM_PRIOR_QUARTER, "+
							" SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT AS NEW_MIGRATED_IN, "+
							" SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT AS TOTAL, "+
							" SM_CLOSED_COUNT AS CLOSED, "+
							" SM_MIGRATED_OUT_COUNT AS MIGRATE_OUT, "+
							" SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT - (SM_CLOSED_COUNT + SM_MIGRATED_OUT_COUNT) AS OUTSTANDING, "+
							" CASE WHEN SM_CLOSED_COUNT/(SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT)<0.25 THEN 'Green' "+
							" WHEN SM_CLOSED_COUNT/(SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT) <0.5 THEN 'Light Green' "+
							" ELSE 'Red' "+
							" END AS COLOR_CLOSED, "+
							" CASE WHEN (SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT - (SM_CLOSED_COUNT + SM_MIGRATED_OUT_COUNT))/(SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT)<0.25 THEN 'Green' "+
							" WHEN (SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT - (SM_CLOSED_COUNT + SM_MIGRATED_OUT_COUNT))/(SM_MIGRATED_IN_COUNT+SM_OPENED_COUNT+SM_PRIOR_ENDING_COUNT)<0.5 THEN 'Light Green' "+
							" ELSE 'Red' "+
							" End AS COLOR_OUTSTANDING "+
							" FROM "+
							" ( "+
							" SELECT "+ 
							" VBUCF.BUCF_NAME AS LEGAL_ENTITY, "+
							" V_IS_AGNG_DTL.AGING_BUCKET AS DAYS, "+
							" sum(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY') >= ? and TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY') <= ? "+
							" then  V_IS_AGNG_DTL.PRIOR_ENDING_COUNT ELSE 0 END "+
							" ) AS SM_PRIOR_ENDING_COUNT, "+
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY') >= ? AND TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')<= ? "+ 
							" then CLOSED_COUNT ELSE 0 "+ 
							" END "+
							" ) AS SM_CLOSED_COUNT, "+
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')>= ? AND TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')<= ? "+ 
							" THEN V_IS_AGNG_DTL.MIGRATED_IN_COUNT  "+
							" ELSE 0 END "+
							" ) AS SM_MIGRATED_IN_COUNT, "+ 
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')>= ? AND TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')<= ? "+ 
							" THEN V_IS_AGNG_DTL.OPENED_COUNT "+
							" ELSE 0 END "+
							" ) AS SM_OPENED_COUNT, "+
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')>= ? AND TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')<= ? "+ 
							" THEN V_IS_AGNG_DTL.MIGRATED_OUT_COUNT "+
							" ELSE 0 END "+
							" ) AS SM_MIGRATED_OUT_COUNT "+
							" FROM (SELECT MONTH||'-01-'||YEAR as DT_ISS, CLOSED_COUNT, BUCF_ID, AGING_BUCKET, PRIOR_ENDING_COUNT, MIGRATED_IN_COUNT, OPENED_COUNT, MIGRATED_OUT_COUNT "+ 
							" FROM V_ISSUE_AGING_DETAIL "+
							" ) V_IS_AGNG_DTL "+
							" INNER JOIN V_BUCF VBUCF "+
							" ON V_IS_AGNG_DTL.BUCF_ID = VBUCF.BUCF_ID "+
							" INNER JOIN V_CDE_OWNER VCDEOWNR "+
							" ON V_IS_AGNG_DTL.BUCF_ID = VCDEOWNR.BUCF_ID "+
							" INNER JOIN V_CDE_1 VCDE1 "+
							" ON VCDEOWNR.CDE_DS_ID = VCDE1.CDE_DS_ID "+
							" GROUP BY VBUCF.BUCF_NAME, V_IS_AGNG_DTL.AGING_BUCKET "+
							" ) TBL "+
							" ORDER BY 1,2;";
		
		Connection connection = null;
		
		try{
			System.out.println("Inside getOpenDQPrioritySummary");
			connection  = dbConnection.connection();
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(2, new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(3, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(4, new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(5, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(6, new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(7, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(8, new java.sql.Timestamp(currentEndDate.getTime()));
			prepStmt.setTimestamp(9, new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(10, new java.sql.Timestamp(currentEndDate.getTime()));
			
			//prepStmt.setTimestamp(8,  new java.sql.Timestamp(currentEndDate.getTime()));
			
			
			/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
			
			if(flag==1){*/
			ResultSet resultSet=prepStmt.executeQuery();
				while(resultSet.next()){
					String bucfName = resultSet.getString("LEGAL_ENTITY");
					OpenDQPrioritySummaryDetail openDQPrioritySummaryDetail = new OpenDQPrioritySummaryDetail();
					openDQPrioritySummaryDetail.setAgingBucket(resultSet.getString("DAYS"));
					openDQPrioritySummaryDetail.setClosed(resultSet.getString("CLOSED"));
					openDQPrioritySummaryDetail.setColorClosed(resultSet.getString("COLOR_CLOSED"));
					openDQPrioritySummaryDetail.setColorOutstanding(resultSet.getString("COLOR_OUTSTANDING"));
					openDQPrioritySummaryDetail.setMigrateOut(resultSet.getString("MIGRATE_OUT"));
					openDQPrioritySummaryDetail.setNewOrMigrateIn(resultSet.getString("NEW_MIGRATED_IN"));
					openDQPrioritySummaryDetail.setOutstanding(resultSet.getString("OUTSTANDING"));
					openDQPrioritySummaryDetail.setPriorQuarter(resultSet.getString("FROM_PRIOR_QUARTER"));
					openDQPrioritySummaryDetail.setTotal(resultSet.getString("TOTAL"));
					
					if(CollectionUtils.isEmpty(openDQPrioritySummaryMap.get(bucfName))){
						List<OpenDQPrioritySummaryDetail> openDQPrioritySummaryDetailLst = new ArrayList<OpenDQPrioritySummaryDetail>();
						openDQPrioritySummaryDetailLst.add(openDQPrioritySummaryDetail);
						if(!bucfName.equalsIgnoreCase("") || null != bucfName){
						openDQPrioritySummaryMap.put(bucfName, openDQPrioritySummaryDetailLst);
						}
					}
					else{
						openDQPrioritySummaryMap.get(bucfName).add(openDQPrioritySummaryDetail);
					}
				}
			}
		
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return openDQPrioritySummary;
	}
	
	@SuppressWarnings("static-access")
	public OpenDQPrioritySummary openDQPriorityDtlsHighPr(java.util.Date priorStartDate,java.util.Date priorEndDate) throws Exception{
		
		Connection connection = dbConnection.connection();

		OpenDQPrioritySummary openDQPrioritySummary = new OpenDQPrioritySummary();
		Map<String,List<OpenDQPrioritySummaryDetail>> openDQPrioritySummaryMap = new HashMap<String,List<OpenDQPrioritySummaryDetail>>();
		openDQPrioritySummary.setOpenDQPrioritySummaryMap(openDQPrioritySummaryMap);
		
		String sqlQuery = "SELECT DISTINCT VBUCF.BUCF_NAME, V_IS_AGNG_DTL.AGING_BUCKET, "+
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')>= ? and TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY') <= ? "+ 
							"           THEN V_IS_AGNG_DTL.OPENED_COUNT ELSE 0 "+
							"     END) AS High_Priority_DQ_Issue_Pstv "+
							" FROM V_BUCF VBUCF "+
							" INNER JOIN (SELECT MONTH||'-01-'||YEAR as DT_ISS, AGING_BUCKET, BUCF_ID, OPENED_COUNT FROM V_ISSUE_AGING_DETAIL) V_IS_AGNG_DTL "+
							" ON VBUCF.BUCF_ID = V_IS_AGNG_DTL.BUCF_ID "+
							" INNER JOIN V_CDE_OWNER VCDEOWNR "+
							" ON V_IS_AGNG_DTL.BUCF_ID = VCDEOWNR.BUCF_ID "+
							" INNER JOIN V_CDE_1 VCDE1 "+
							" ON VCDEOWNR.CDE_DS_ID = VCDE1.CDE_DS_ID "+
							" GROUP BY VBUCF.BUCF_NAME, V_IS_AGNG_DTL.AGING_BUCKET "+
							" ORDER BY 1,2 ";
		
		
		
		try{
			System.out.println("Inside openDQPriorityDtlsHighPr");
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(priorStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(priorEndDate.getTime()));
			/*int flag = dbConnection.data_Retrive(sqlQuery , connection);
			
			if(flag==1){*/
			ResultSet resultSet=prepStmt.executeQuery();
				while(resultSet.next()){
					String bucfName = resultSet.getString("BUCF_NAME");
					OpenDQPrioritySummaryDetail openDQPrioritySummaryDetail = new OpenDQPrioritySummaryDetail();
					openDQPrioritySummaryDetail.setAgingBucket(resultSet.getString("AGING_BUCKET"));
					openDQPrioritySummaryDetail.setBucfName(resultSet.getString("BUCF_NAME"));
					openDQPrioritySummaryDetail.setHighPriority(resultSet.getString("High_Priority_DQ_Issue_Pstv"));
					
					if(CollectionUtils.isEmpty(openDQPrioritySummaryMap.get(bucfName))){
						List<OpenDQPrioritySummaryDetail> dqDimensionLst = new ArrayList<OpenDQPrioritySummaryDetail>();
						dqDimensionLst.add(openDQPrioritySummaryDetail);
						if(!bucfName.equalsIgnoreCase("") || null != bucfName){
						openDQPrioritySummaryMap.put(bucfName, dqDimensionLst);
						}
					}
					else{
						openDQPrioritySummaryMap.get(bucfName).add(openDQPrioritySummaryDetail);
					}
				}
			}
		
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return openDQPrioritySummary;
		
	}
	
	@SuppressWarnings("static-access")
	public OpenDQPrioritySummary openDQPriorityDtlsLowPr(java.util.Date currentStartDate,java.util.Date currentEndDate) throws Exception{
		
		Connection connection =  dbConnection.connection();
		OpenDQPrioritySummary openDQPrioritySummary = new OpenDQPrioritySummary();
		Map<String,List<OpenDQPrioritySummaryDetail>> openDQPrioritySummaryMap = new HashMap<String,List<OpenDQPrioritySummaryDetail>>();
		openDQPrioritySummary.setOpenDQPrioritySummaryMap(openDQPrioritySummaryMap);
		
		String sqlQuery = "SELECT DISTINCT VBUCF.BUCF_NAME, V_IS_AGNG_DTL.AGING_BUCKET, "+
							" SUM(CASE WHEN TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY')>= ? and TO_DATE(V_IS_AGNG_DTL.DT_ISS, 'MM-DD-YYYY') <= ? "+ 
							"           THEN V_IS_AGNG_DTL.OPENED_COUNT ELSE 0 "+
							"     END) AS Low_Priority "+
							" FROM V_BUCF VBUCF "+
							" INNER JOIN (SELECT MONTH||'-01-'||YEAR as DT_ISS, AGING_BUCKET, BUCF_ID, OPENED_COUNT FROM V_ISSUE_AGING_DETAIL) V_IS_AGNG_DTL "+
							" ON VBUCF.BUCF_ID = V_IS_AGNG_DTL.BUCF_ID "+
							" INNER JOIN V_CDE_OWNER VCDEOWNR "+
							" ON V_IS_AGNG_DTL.BUCF_ID = VCDEOWNR.BUCF_ID "+
							" INNER JOIN V_CDE_1 VCDE1 "+
							" ON VCDEOWNR.CDE_DS_ID = VCDE1.CDE_DS_ID "+
							" GROUP BY VBUCF.BUCF_NAME, V_IS_AGNG_DTL.AGING_BUCKET "+
							" ORDER BY 1,2 ";
		
		
		
		
		try{
			System.out.println("Inside openDQPriorityDtlsLowPr");
			//int flag = dbConnection.data_Retrive(sqlQuery , connection);
			
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1,  new java.sql.Timestamp(currentStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(currentEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
				while(resultSet.next()){
					String bucfName = resultSet.getString("BUCF_NAME");
					OpenDQPrioritySummaryDetail openDQPrioritySummaryDetail = new OpenDQPrioritySummaryDetail();
					openDQPrioritySummaryDetail.setAgingBucket(resultSet.getString("AGING_BUCKET"));
					openDQPrioritySummaryDetail.setBucfName(resultSet.getString("BUCF_NAME"));
					openDQPrioritySummaryDetail.setLowPriority(resultSet.getString("Low_Priority"));
					
					if(CollectionUtils.isEmpty(openDQPrioritySummaryMap.get(bucfName))){
						List<OpenDQPrioritySummaryDetail> dqDimensionLst = new ArrayList<OpenDQPrioritySummaryDetail>();
						dqDimensionLst.add(openDQPrioritySummaryDetail);
						if(!bucfName.equalsIgnoreCase("") || null != bucfName){
						openDQPrioritySummaryMap.put(bucfName, dqDimensionLst);
						}
					}
					else{
						openDQPrioritySummaryMap.get(bucfName).add(openDQPrioritySummaryDetail);
					}
				}
			}
		
		catch(Exception e){
			System.out.println(EXCEPTION_STR +" openDQPriorityDtlsLowPr");
		}
		finally{
			if(connection != null)
				dbConnection.close_Connection(connection);
			}
		
		return openDQPrioritySummary;
	}
	
	/*Queries for Measure/Remediate Data Quality end*/
	
 }
