package com.deloitte.debdeep.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.deloitte.bedqd.connection.SQLConnection;
import com.deloitte.bedqd.model.DQPScoreModel;
import com.deloitte.bedqd.model.DQRIDQPImpactScoreL1L2SrcLegalModel;
import com.deloitte.bedqd.model.DQRIScoreModel;
import com.deloitte.bedqd.model.ECDECntL1SrcSysLegalEntityModel;
import com.deloitte.bedqd.model.ECDECntL2SrcSysLegalEntityModel;
import com.deloitte.bedqd.model.ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB;
import com.deloitte.bedqd.model.EcdeCountEcdeRecords;
import com.deloitte.bedqd.model.ImpactScoreL1L2SrcLegalEntityModel;
import com.deloitte.bedqd.model.ImpactScoreL1SrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScoreL2SrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScoreModel;
import com.deloitte.bedqd.model.ImpactScoreSrcSysSrcSystemLegalEntityLOBModel;
import com.deloitte.bedqd.model.ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB;

import ch.qos.logback.core.net.SyslogOutputStream;

public class InternalControlsDao {

	SQLConnection dbConnection = new SQLConnection();
	int docEntry;
	
	@SuppressWarnings("static-access")
	public DQRIScoreModel getDQRIScoreDetails()throws Exception {
		Connection connection  = dbConnection.connection();
		DQRIScoreModel dqriScoreBean = null;

		String sqlQuery =   "SELECT ROUND ( SUM( " +
							"  CASE " +
							"    WHEN DQRI      IS NOT NULL " +
							"    AND TOTAL_ROWS IS NOT NULL " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END )/ SUM( " +
							"  CASE " +
							"    WHEN DQRI      IS NOT NULL " +
							"    AND TOTAL_ROWS IS NOT NULL " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END ), 1 )AS DQRI_SCORE " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
							"WHERE TOTAL_ROWS   <> '' " +
							"AND BUCF.BUCF_NAME <> '' ";
		try{

			int flag = dbConnection.data_Retrive(sqlQuery , connection);
			if(flag == 1){
				while(dbConnection.resultset.next()){
					dqriScoreBean = new DQRIScoreModel();
					dqriScoreBean.setDqriScore(dbConnection.resultset.getString("DQRI_SCORE"));
					dqriScoreBean.setHeader("DQRI Score");
				}
				//dbConnection.close_Connection(connection);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			if(null != connection){
				dbConnection.close_Connection(connection);
			}
		}
		return dqriScoreBean;
	}
	
	
	
	@SuppressWarnings("static-access")
	public DQPScoreModel getDQPScoreDetails()throws Exception {
		Connection connection  = dbConnection.connection();
		DQPScoreModel dqpScoreModel = null;

		String sqlQuery =   "SELECT ROUND ( ( SUM( " +
							"  CASE " +
							"    WHEN Dq_Profiling_Score IS NOT NULL " +
							"    AND TOTAL_ROWS          IS NOT NULL " +
							"    THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) " +
							"  END )/ SUM( " +
							"  CASE " +
							"    WHEN Dq_Profiling_Score IS NOT NULL " +
							"    AND TOTAL_ROWS          IS NOT NULL " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END ) )*100 ) AS DQP_SCORE " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID          = BUCF.BUCF_ID " +
							"WHERE TOTAL_ROWS       <>'' " +
							"AND Dq_Profiling_Score <>'' " +
							"AND DQRI_RESULT_ID     <> '' " +
							"AND BUCF.BUCF_NAME     <> ''";

		try{

			int flag = dbConnection.data_Retrive(sqlQuery , connection);
			if(flag == 1){
				while(dbConnection.resultset.next()){
					dqpScoreModel = new DQPScoreModel();
					dqpScoreModel.setDqpScore(dbConnection.resultset.getString("DQP_SCORE"));
					dqpScoreModel.setHeader("DQP Score");
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
		return dqpScoreModel;
	}
	
	@SuppressWarnings("static-access")
	public ImpactScoreModel getImapactScoreDetails()throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScoreModel impactScoreBean = null;

		String sqlQuery =   "SELECT ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID = BUCF.BUCF_ID";
		try{
			int flag = dbConnection.data_Retrive(sqlQuery , connection);
			if(flag == 1){
				while(dbConnection.resultset.next()){
					impactScoreBean = new ImpactScoreModel();
					impactScoreBean.setImpactScore(dbConnection.resultset.getString("IMPACT_SCORE"));
					impactScoreBean.setHeader("IMPACT SCORE");
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
		return impactScoreBean;
	}
	

	@SuppressWarnings("static-access")
	public List<DQRIDQPImpactScoreL1L2SrcLegalModel> getDQRIDQPImpactScoreL1L2Details()throws Exception {
		Connection connection  = dbConnection.connection();
		DQRIDQPImpactScoreL1L2SrcLegalModel DQRIDQPImpactScoreL1L2SrcLegalModel = null;
		List<DQRIDQPImpactScoreL1L2SrcLegalModel> list = new ArrayList<DQRIDQPImpactScoreL1L2SrcLegalModel>();

		String sqlQuery =   "SELECT Level1_Process_Name AS Level_1_Process_DQP, " +
							"  Level2_Process_Name      AS Level2_Process_DQP, " +
							"  VDE1.ADS                 AS SOURCE_SYSTEM, " +
							"  BUCF.BUCF_NAME           AS SOURCE_LOB, " +
							"  ROUND ( SUM( " +
							"  CASE " +
							"    WHEN DQRI      IS NOT NULL " +
							"    AND TOTAL_ROWS IS NOT NULL " +
							"    AND TOTAL_ROWS <> '' " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END )/ SUM( " +
							"  CASE " +
							"    WHEN DQRI      IS NOT NULL " +
							"    AND TOTAL_ROWS IS NOT NULL " +
							"    AND TOTAL_ROWS <> '' " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END ), 1 )AS DQRI_SCORE, " +
							"  ROUND ( ( SUM( " +
							"  CASE " +
							"    WHEN Dq_Profiling_Score IS NOT NULL " +
							"    AND TOTAL_ROWS          IS NOT NULL " +
							"    AND TOTAL_ROWS          <> '' " +
							"    THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) " +
							"  END )/ SUM( " +
							"  CASE " +
							"    WHEN Dq_Profiling_Score IS NOT NULL " +
							"    AND TOTAL_ROWS          IS NOT NULL " +
							"    AND TOTAL_ROWS          <> '' " +
							"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
							"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
							"  END ) )*100 ) AS DQP_SCORE, " +
							"  COUNT(DISTINCT " +
							"  CASE " +
							"    WHEN IS_ECDE                                               = 'Y' " +
							"    AND VDE1.Ecde_Assign_Date                                  > '0' " +
							"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < TO_DATE('2014-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS') " +
							"    THEN DQRR.CDE_DS_ID " +
							"    ELSE '0' " +
							"  END)                                  AS ECDE_CNT, " +
							"  ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
							"WHERE VDE1.ADS     <> '' " +
							"AND BUCF.BUCF_NAME <> '' " +
							"GROUP BY Level1_Process_Name, " +
							"  Level2_Process_Name, " +
							"  VDE1.ADS, " +
							"  BUCF.BUCF_NAME";
		try{
			int flag = dbConnection.data_Retrive(sqlQuery , connection);
			if(flag == 1){
				while(dbConnection.resultset.next()){
					DQRIDQPImpactScoreL1L2SrcLegalModel = new DQRIDQPImpactScoreL1L2SrcLegalModel();
					
					DQRIDQPImpactScoreL1L2SrcLegalModel.setLevel1ProcessDQP(dbConnection.resultset.getString("Level_1_Process_DQP"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setLevel2ProcessDQP(dbConnection.resultset.getString("Level2_Process_DQP"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setSourceSystem(dbConnection.resultset.getString("SOURCE_SYSTEM"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setSourceLob(dbConnection.resultset.getString("SOURCE_LOB"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setDqriScore(dbConnection.resultset.getString("DQRI_SCORE"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setDqpScore(dbConnection.resultset.getString("DQP_SCORE"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setEcdeCnt(dbConnection.resultset.getString("ECDE_CNT"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setImpactScore(dbConnection.resultset.getString("IMPACT_SCORE"));
					DQRIDQPImpactScoreL1L2SrcLegalModel.setHeader("DQRI Score/DQP Score/Impact Score for  < Level1/Level2/SourceSystem/LegalEntityLOB> Process");
					
					list.add(DQRIDQPImpactScoreL1L2SrcLegalModel);
				}
				//dbConnection.close_Connection(connection);
			}//else if(flag==0){			
				//dbConnection.close_Connection(connection);
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
	public List<ECDECntL1SrcSysLegalEntityModel> getECDECntL1SrcSysLegalDetails(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ECDECntL1SrcSysLegalEntityModel ecdeCntL1SrcSysLegalEntityBean = null;
		List<ECDECntL1SrcSysLegalEntityModel> list = new ArrayList<ECDECntL1SrcSysLegalEntityModel>();

		String sqlQuery =   "SELECT Level1_Process_Name AS Level_1_Process_DQP, " +
							"  VDE1.ADS                 AS SOURCE_SYSTEM, " +
							"  BUCF.BUCF_NAME           AS SOURCE_LOB, " +
							"  COUNT(DISTINCT " +
							"  CASE " +
							"    WHEN IS_ECDE                                               = 'Y' " +
							"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " +
							"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < ? " +
							"    THEN DQRR.CDE_DS_ID " +
							"    ELSE '0' " +
							"  END) AS ECDE_CNT, " +
							"  ROUND(SUM( " +
							"  CASE " +
							"    WHEN TOTAL_ROWS <> '' " +
							"    THEN CAST(TOTAL_ROWS AS NUMBER) " +
							"    ELSE 0 " +
							"  END)/1000) AS ECDE_RCRDS_TSTD " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
							"WHERE VDE1.ADS     <> '' " +
							"AND BUCF.BUCF_NAME <> '' " +
							"GROUP BY Level1_Process_Name, " +
							"  VDE1.ADS, " +
							"  BUCF.BUCF_NAME " +
							"ORDER BY 3,2";
		try{
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
			
			while(resultSet.next()){
					ecdeCntL1SrcSysLegalEntityBean = new ECDECntL1SrcSysLegalEntityModel();
					//System.out.println("IN loop");
					ecdeCntL1SrcSysLegalEntityBean.setLevel1ProcessDqp(resultSet.getString("Level_1_Process_DQP"));
					ecdeCntL1SrcSysLegalEntityBean.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
					ecdeCntL1SrcSysLegalEntityBean.setSourceLOB(resultSet.getString("SOURCE_LOB"));
					ecdeCntL1SrcSysLegalEntityBean.setEcdeCnt(resultSet.getString("ECDE_CNT"));
					ecdeCntL1SrcSysLegalEntityBean.setEcdeRcrdsTstd(resultSet.getString("ECDE_RCRDS_TSTD"));
					ecdeCntL1SrcSysLegalEntityBean.setHeader("ECDE COUNT FOR <Level1/SourceSystem/LegalEntityLOB>");
					
					list.add(ecdeCntL1SrcSysLegalEntityBean);
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
		//System.out.println("END IN loop");
		return list;
	}
	
	@SuppressWarnings("static-access")
	public List<ECDECntL2SrcSysLegalEntityModel> getECDECntL2SrcSysLegalDetails(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ECDECntL2SrcSysLegalEntityModel ecdeCntL2SrcSysLegalEntityBean = null;
		List<ECDECntL2SrcSysLegalEntityModel> list = new ArrayList<ECDECntL2SrcSysLegalEntityModel>();

		String sqlQuery =   "SELECT Level2_Process_Name AS Level2_Process_DQP, " +
							"  VDE1.ADS                 AS SOURCE_SYSTEM, " +
							"  BUCF.BUCF_NAME           AS SOURCE_LOB, " +
							"  COUNT(DISTINCT " +
							"  CASE " +
							"    WHEN IS_ECDE                                               = 'Y' " +
							"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " +
							"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < ? " +
							"    THEN DQRR.CDE_DS_ID " +
							"    ELSE '0' " +
							"  END) AS ECDE_CNT, " +
							"  ROUND(SUM( " +
							"  CASE " +
							"    WHEN TOTAL_ROWS <> '' " +
							"    THEN CAST(TOTAL_ROWS AS NUMBER) " +
							"    ELSE 0 " +
							"  END)/1000) AS ECDE_RCRDS_TSTD " +
							"FROM V_DQRI_RESULT DQRR " +
							"INNER JOIN V_BUSINESS_PROCESSES VBP " +
							"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
							"INNER JOIN V_CDE_1 VDE1 " +
							"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
							"INNER JOIN V_CDE_OWNER CDO " +
							"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
							"INNER JOIN V_BUCF BUCF " +
							"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
							"WHERE VDE1.ADS     <> '' " +
							"AND BUCF.BUCF_NAME <> '' " +
							"GROUP BY Level2_Process_Name, " +
							"  VDE1.ADS, " +
							"  BUCF.BUCF_NAME " +
							"ORDER BY 3,2";
		try{
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
		
			while(resultSet.next()){
				ecdeCntL2SrcSysLegalEntityBean = new ECDECntL2SrcSysLegalEntityModel();
				
				ecdeCntL2SrcSysLegalEntityBean.setLevel2ProcessDqp(resultSet.getString("Level2_Process_DQP"));
				ecdeCntL2SrcSysLegalEntityBean.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
				ecdeCntL2SrcSysLegalEntityBean.setSourceLOB(resultSet.getString("SOURCE_LOB"));
				ecdeCntL2SrcSysLegalEntityBean.setEcdeCnt(resultSet.getString("ECDE_CNT"));
				ecdeCntL2SrcSysLegalEntityBean.setEcdeRcrdsTstd(resultSet.getString("ECDE_RCRDS_TSTD"));
				ecdeCntL2SrcSysLegalEntityBean.setHeader("ECDE COUNT FOR <Level2/SourceSystem/LegalEntityLOB>");
				
				list.add(ecdeCntL2SrcSysLegalEntityBean);
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
	
		return list;
	}
	
	@SuppressWarnings("static-access")
	public List<ImpactScoreL1L2SrcLegalEntityModel> getImpactScoreL1L2SrcLegalEntityDetails()throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScoreL1L2SrcLegalEntityModel impactScoreL1L2SrcLegalEntityBean = null;
		List<ImpactScoreL1L2SrcLegalEntityModel> list = new ArrayList<ImpactScoreL1L2SrcLegalEntityModel>();

		/*String sqlQuery = "SELECT DIMENSION, " +
				"  SOURCE_SYSTEM, " +
				"  LOB, " +
				"  Level_1_Process_DQP, " +
				"  ECDE, " +
				"  IMPACT_SCORE, " +
				"  COMPLETENESS, " +
				"  Confirmity, " +
				"  Validity, " +
				"  Accuracy " +
				"FROM " +
				"  (SELECT 'Level1_Process_Name' AS DIMENSION, " +
				"    Level1_Process_Name         AS Level_1_Process_DQP, " +
				"    VDE1.ADS                    AS SOURCE_SYSTEM, " +
				"    BUCF.BUCF_NAME              AS LOB, " +
				"    CASE " +
				"      WHEN VDE1.IS_ECDE = 'Y' " +
				"      THEN VDE1.CDE_NAME " +
				"      ELSE NULL " +
				"    END                                       AS ECDE, " +
				"    ROUND(AVG(CAST(DQRR.IMPACT AS            NUMBER)),2) AS IMPACT_SCORE, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Completeness AS NUMBER)),2) AS COMPLETENESS, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Conformity AS   NUMBER)),2) AS Confirmity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_VALIDITY AS     NUMBER)),2) AS Validity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_ACCURACY AS     NUMBER)),2) AS Accuracy " +
				"  FROM V_DQRI_RESULT DQRR " +
				"  INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"  INNER JOIN V_CDE_1 VDE1 " +
				"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_OWNER CDO " +
				"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"  WHERE VDE1.ADS     <> '' " +
				"  AND BUCF.BUCF_NAME <> '' " +
				"  GROUP BY Level1_Process_Name, " +
				"    VDE1.ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    VDE1.IS_ECDE, " +
				"    VDE1.CDE_NAME " +
				"  ) A " +
				"UNION ALL " +
				"SELECT DIMENSION, " +
				"  SOURCE_SYSTEM, " +
				"  LOB, " +
				"  Level_2_Process_DQP, " +
				"  ECDE, " +
				"  IMPACT_SCORE, " +
				"  COMPLETENESS, " +
				"  Confirmity, " +
				"  Validity, " +
				"  Accuracy " +
				"FROM " +
				"  (SELECT 'Level2_Process_Name' AS DIMENSION, " +
				"    Level2_Process_Name         AS Level_2_Process_DQP, " +
				"    VDE1.ADS                    AS SOURCE_SYSTEM, " +
				"    BUCF.BUCF_NAME              AS LOB, " +
				"    CASE " +
				"      WHEN VDE1.IS_ECDE = 'Y' " +
				"      THEN VDE1.CDE_NAME " +
				"      ELSE NULL " +
				"    END                                       AS ECDE, " +
				"    ROUND(AVG(CAST(DQRR.IMPACT AS            NUMBER)),2) AS IMPACT_SCORE, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Completeness AS NUMBER)),2) AS COMPLETENESS, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Conformity AS   NUMBER)),2) AS Confirmity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_VALIDITY AS     NUMBER)),2) AS Validity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_ACCURACY AS     NUMBER)),2) AS Accuracy " +
				"  FROM V_DQRI_RESULT DQRR " +
				"  INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"  INNER JOIN V_CDE_1 VDE1 " +
				"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_OWNER CDO " +
				"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"  WHERE VDE1.ADS     <> '' " +
				"  AND BUCF.BUCF_NAME <> '' " +
				"  GROUP BY Level2_Process_Name, " +
				"    VDE1.ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    VDE1.IS_ECDE, " +
				"    VDE1.CDE_NAME " +
				"  ) A " +
				"UNION ALL " +
				"SELECT DIMENSION, " +
				"  SOURCE_SYSTEM, " +
				"  LOB, " +
				"  SOURCE_SYSTEM, " +
				"  ECDE, " +
				"  IMPACT_SCORE, " +
				"  COMPLETENESS, " +
				"  Confirmity, " +
				"  Validity, " +
				"  Accuracy " +
				"FROM " +
				"  (SELECT 'SOURCE_SYSTEM' AS DIMENSION, " +
				"    VDE1.ADS              AS SOURCE_SYSTEM, " +
				"    BUCF.BUCF_NAME        AS LOB, " +
				"    CASE " +
				"      WHEN VDE1.IS_ECDE = 'Y' " +
				"      THEN VDE1.CDE_NAME " +
				"      ELSE NULL " +
				"    END                                       AS ECDE, " +
				"    ROUND(AVG(CAST(DQRR.IMPACT AS            NUMBER)),2) AS IMPACT_SCORE, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Completeness AS NUMBER)),2) AS COMPLETENESS, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Conformity AS   NUMBER)),2) AS Confirmity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_VALIDITY AS     NUMBER)),2) AS Validity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_ACCURACY AS     NUMBER)),2) AS Accuracy " +
				"  FROM V_DQRI_RESULT DQRR " +
				"  INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"  INNER JOIN V_CDE_1 VDE1 " +
				"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_OWNER CDO " +
				"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"  WHERE VDE1.ADS     <> '' " +
				"  AND BUCF.BUCF_NAME <> '' " +
				"  GROUP BY VDE1.ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    VDE1.IS_ECDE, " +
				"    VDE1.CDE_NAME " +
				"  ) A " +
				"--WHERE SOURCE_SYSTEM = 'CREST' AND LOB = 'HR' " +
				"UNION ALL " +
				"SELECT DIMENSION, " +
				"  SOURCE_SYSTEM, " +
				"  LOB, " +
				"  LEGAL_ENTITY_LOB, " +
				"  ECDE, " +
				"  IMPACT_SCORE, " +
				"  COMPLETENESS, " +
				"  Confirmity, " +
				"  Validity, " +
				"  Accuracy " +
				"FROM " +
				"  (SELECT 'LEGAL_ENTITY_LOB' AS DIMENSION, " +
				"    VDE1.BUCF_NAME           AS LEGAL_ENTITY_LOB, " +
				"    VDE1.ADS                 AS SOURCE_SYSTEM, " +
				"    BUCF.BUCF_NAME           AS LOB, " +
				"    CASE " +
				"      WHEN VDE1.IS_ECDE = 'Y' " +
				"      THEN VDE1.CDE_NAME " +
				"      ELSE NULL " +
				"    END                                       AS ECDE, " +
				"    ROUND(AVG(CAST(DQRR.IMPACT AS            NUMBER)),2) AS IMPACT_SCORE, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Completeness AS NUMBER)),2) AS COMPLETENESS, " +
				"    ROUND(AVG(CAST(DQRR.Dqri_Conformity AS   NUMBER)),2) AS Confirmity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_VALIDITY AS     NUMBER)),2) AS Validity, " +
				"    ROUND(AVG(CAST(DQRR.DQRI_ACCURACY AS     NUMBER)),2) AS Accuracy " +
				"  FROM V_DQRI_RESULT DQRR " +
				"  INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"  INNER JOIN V_CDE_1 VDE1 " +
				"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_OWNER CDO " +
				"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"  WHERE VDE1.ADS     <> '' " +
				"  AND BUCF.BUCF_NAME <> '' " +
				"  GROUP BY VDE1.BUCF_NAME, " +
				"    VDE1.ADS, " +
				"    BUCF.BUCF_NAME, " +
				"    VDE1.IS_ECDE, " +
				"    VDE1.CDE_NAME " +
				"  ) A " +
				"  --WHERE SOURCE_SYSTEM = 'CREST' AND LOB = 'HR' " +
				"ORDER BY 1,2,3,4,5";*/
		
		String sqlQuery = " SELECT DIMENSION, SOURCE_SYSTEM, LOB, Level_1_Process_DQP, ECDE, " +
				" IMPACT_SCORE,  COMPLETENESS,  Confirmity, Validity, Accuracy " +
				" FROM  " +
				" ( " +
				" SELECT " +
				" 'Level1_Process_Name' AS DIMENSION, " +
				" Level1_Process_Name AS Level_1_Process_DQP, " +
				" VDE1.ADS as SOURCE_SYSTEM, " +
				" BUCF.BUCF_NAME as LOB, " +
				" CASE WHEN VDE1.IS_ECDE = 'Y' THEN VDE1.CDE_NAME ELSE NULL END AS ECDE, " +
				" ROUND(AVG(CAST(DQRR.IMPACT as NUMBER)),2) AS IMPACT_SCORE, " +
				" ROUND(avg(CAST(DQRR.Dqri_Completeness as NUMBER)),2) AS COMPLETENESS, " +
				" ROUND(AVG(CAST(DQRR.Dqri_Conformity as NUMBER)),2) AS Confirmity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_VALIDITY as NUMBER)),2) AS Validity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_ACCURACY as NUMBER)),2) AS Accuracy " +
				" FROM  V_DQRI_RESULT DQRR " +
				" INNER JOIN  V_BUSINESS_PROCESSES VBP " +
				" ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				" INNER JOIN  V_CDE_1 VDE1 " +
				" ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				" INNER JOIN  V_CDE_OWNER CDO " +
				" ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				" INNER JOIN  V_BUCF BUCF " +
				" ON CDO.BUCF_ID = BUCF.BUCF_ID " +
				" WHERE VDE1.ADS <> '' AND BUCF.BUCF_NAME <> '' " +
				" GROUP BY Level1_Process_Name, VDE1.ADS, BUCF.BUCF_NAME, VDE1.IS_ECDE, VDE1.CDE_NAME " +
				" ) A " +
				" UNION ALL " +
				" SELECT DIMENSION, SOURCE_SYSTEM, LOB, Level_2_Process_DQP, ECDE, IMPACT_SCORE,  COMPLETENESS, " +
				"   Confirmity, Validity, Accuracy " +
				" FROM  " +
				" ( " +
				" SELECT " +
				" 'Level2_Process_Name' AS DIMENSION, " +
				" Level2_Process_Name AS Level_2_Process_DQP, " +
				" VDE1.ADS as SOURCE_SYSTEM, " +
				" BUCF.BUCF_NAME as LOB, " +
				" CASE WHEN VDE1.IS_ECDE = 'Y' THEN VDE1.CDE_NAME ELSE NULL END AS ECDE, " +
				" ROUND(AVG(CAST(DQRR.IMPACT as NUMBER)),2) AS IMPACT_SCORE, " +
				" ROUND(avg(CAST(DQRR.Dqri_Completeness as NUMBER)),2) AS COMPLETENESS, " +
				" ROUND(AVG(CAST(DQRR.Dqri_Conformity as NUMBER)),2) AS Confirmity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_VALIDITY as NUMBER)),2) AS Validity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_ACCURACY as NUMBER)),2) AS Accuracy " +
				" FROM  V_DQRI_RESULT DQRR " +
				" INNER JOIN  V_BUSINESS_PROCESSES VBP " +
				" ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				" INNER JOIN  V_CDE_1 VDE1 " +
				" ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				" INNER JOIN  V_CDE_OWNER CDO " +
				" ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				" INNER JOIN  V_BUCF BUCF " +
				" ON CDO.BUCF_ID = BUCF.BUCF_ID " +
				" WHERE VDE1.ADS <> '' AND BUCF.BUCF_NAME <> ''  " +
				" GROUP BY Level2_Process_Name, VDE1.ADS, BUCF.BUCF_NAME, VDE1.IS_ECDE, VDE1.CDE_NAME " +
				" ) A " +
				" UNION ALL " +
				" SELECT DIMENSION, SOURCE_SYSTEM, LOB, SOURCE_SYSTEM, ECDE, IMPACT_SCORE,  " +
				"  COMPLETENESS,  Confirmity, Validity, Accuracy " +
				" FROM  " +
				" ( " +
				" SELECT " +
				" 'SOURCE_SYSTEM' AS DIMENSION, " +
				" VDE1.ADS as SOURCE_SYSTEM, " +
				" BUCF.BUCF_NAME as LOB, " +
				" CASE WHEN VDE1.IS_ECDE = 'Y' THEN VDE1.CDE_NAME ELSE NULL END AS ECDE, " +
				" ROUND(AVG(CAST(DQRR.IMPACT as NUMBER)),2) AS IMPACT_SCORE, " +
				" ROUND(avg(CAST(DQRR.Dqri_Completeness as NUMBER)),2) AS COMPLETENESS, " +
				" ROUND(AVG(CAST(DQRR.Dqri_Conformity as NUMBER)),2) AS Confirmity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_VALIDITY as NUMBER)),2) AS Validity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_ACCURACY as NUMBER)),2) AS Accuracy " +
				" FROM  V_DQRI_RESULT DQRR " +
				" INNER JOIN  V_BUSINESS_PROCESSES VBP " +
				" ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				" INNER JOIN  V_CDE_1 VDE1 " +
				" ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				" INNER JOIN  V_CDE_OWNER CDO " +
				" ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				" INNER JOIN  V_BUCF BUCF " +
				" ON CDO.BUCF_ID = BUCF.BUCF_ID " +
				" WHERE VDE1.ADS <> '' AND BUCF.BUCF_NAME <> '' " +
				" GROUP BY VDE1.ADS, BUCF.BUCF_NAME, VDE1.IS_ECDE, VDE1.CDE_NAME " +
				" ) A " +
				" UNION ALL " +
				" SELECT DIMENSION, SOURCE_SYSTEM, LOB, LEGAL_ENTITY_LOB, ECDE, IMPACT_SCORE,   " +
				" COMPLETENESS,  Confirmity, Validity, Accuracy " +
				" FROM  " +
				" ( " +
				" SELECT " +
				" 'LEGAL_ENTITY_LOB' AS DIMENSION, " +
				" VDE1.BUCF_NAME as LEGAL_ENTITY_LOB, " +
				" VDE1.ADS as SOURCE_SYSTEM, " +
				" BUCF.BUCF_NAME as LOB, " +
				" CASE WHEN VDE1.IS_ECDE = 'Y' THEN VDE1.CDE_NAME ELSE NULL END AS ECDE, " +
				" ROUND(AVG(CAST(DQRR.IMPACT as NUMBER)),2) AS IMPACT_SCORE, " +
				" ROUND(avg(CAST(DQRR.Dqri_Completeness as NUMBER)),2) AS COMPLETENESS, " +
				" ROUND(AVG(CAST(DQRR.Dqri_Conformity as NUMBER)),2) AS Confirmity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_VALIDITY as NUMBER)),2) AS Validity, " +
				" ROUND(AVG(CAST(DQRR.DQRI_ACCURACY as NUMBER)),2) AS Accuracy " +
				" FROM  V_DQRI_RESULT DQRR " +
				" INNER JOIN  V_BUSINESS_PROCESSES VBP " +
				" ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				" INNER JOIN  V_CDE_1 VDE1 " +
				" ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				" INNER JOIN  V_CDE_OWNER CDO " +
				" ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				" INNER JOIN  V_BUCF BUCF " +
				" ON CDO.BUCF_ID = BUCF.BUCF_ID " +
				" WHERE VDE1.ADS <> '' AND BUCF.BUCF_NAME <> ''  " +
				" GROUP BY VDE1.BUCF_NAME, VDE1.ADS, BUCF.BUCF_NAME, VDE1.IS_ECDE, VDE1.CDE_NAME " +
				" ) A " +
				" ORDER BY 1,2,3,4,5 "; 

  
		try{
			int flag = dbConnection.data_Retrive(sqlQuery , connection);
			if(flag == 1){
				while(dbConnection.resultset.next()){
					impactScoreL1L2SrcLegalEntityBean = new ImpactScoreL1L2SrcLegalEntityModel();
					
					impactScoreL1L2SrcLegalEntityBean.setDimension(dbConnection.resultset.getString("DIMENSION"));
					impactScoreL1L2SrcLegalEntityBean.setLevel1ProcessDqp(dbConnection.resultset.getString("Level_1_Process_DQP"));
					impactScoreL1L2SrcLegalEntityBean.setImpactScore(dbConnection.resultset.getString("IMPACT_SCORE"));
					impactScoreL1L2SrcLegalEntityBean.setConfirmity(dbConnection.resultset.getString("Confirmity"));
					impactScoreL1L2SrcLegalEntityBean.setValidity(dbConnection.resultset.getString("Validity"));
					impactScoreL1L2SrcLegalEntityBean.setAccuracy(dbConnection.resultset.getString("Accuracy"));
					impactScoreL1L2SrcLegalEntityBean.setSourceSytem(dbConnection.resultset.getString("SOURCE_SYSTEM"));
					impactScoreL1L2SrcLegalEntityBean.setSourceLOB(dbConnection.resultset.getString("LOB"));				
					impactScoreL1L2SrcLegalEntityBean.setHeader("IMPACT SCORE FOR <Level1/Level2/SourceSystem/LegalEntityLOB>");
					
					list.add(impactScoreL1L2SrcLegalEntityBean);
				}
				//dbConnection.close_Connection(connection);
			}//else if(flag==0){			
				//dbConnection.close_Connection(connection);
			//}
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
	//"INTERNAL CONTROLS -
	//DQRI Score/DQP Score/Impact Score for  < Level1/SourceSystem/LegalEntityLOB> Proces"

	
	@SuppressWarnings("static-access")
	public List<ImpactScoreL1SrcSystemLegalEntityLOBModel> getImpactScoreL1SrcSystemLegalEntityLOBModel(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScoreL1SrcSystemLegalEntityLOBModel impactScoreL1SrcSystemLegalEntityLOBModel = null;
		List<ImpactScoreL1SrcSystemLegalEntityLOBModel> list = new ArrayList<ImpactScoreL1SrcSystemLegalEntityLOBModel>();
		
		StringBuffer SQL = new StringBuffer(""); 
		
				
		SQL.append("SELECT Level1_Process_Name AS Level_1_Process_DQP, \n");
		SQL.append("  VDE1.ADS                 AS SOURCE_SYSTEM, \n");
		SQL.append("  BUCF.BUCF_NAME           AS SOURCE_LOB, \n");
		SQL.append("  ROUND ( SUM( \n");
		SQL.append("  CASE \n");
		SQL.append("    WHEN DQRI      IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS <> '' \n");
		SQL.append("    THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) \n");
		SQL.append("    ELSE CAST(DQRI_RESULT_ID AS NUMBER) \n");
		SQL.append("  END )/ SUM( \n");
		SQL.append("  CASE \n");
		SQL.append("    WHEN DQRI      IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS <> '' \n");
		SQL.append("    THEN CAST(TOTAL_ROWS AS     NUMBER) \n");
		SQL.append("    ELSE CAST(DQRI_RESULT_ID AS NUMBER) \n");
		SQL.append("  END ), 1 )AS DQRI_SCORE, \n");
		SQL.append("  ROUND ( ( SUM( \n");
		SQL.append("  CASE \n");
		SQL.append("    WHEN Dq_Profiling_Score IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS          IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS          <> '' \n");
		SQL.append("    THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) \n");
		SQL.append("    ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) \n");
		SQL.append("  END )/ SUM( \n");
		SQL.append("  CASE \n");
		SQL.append("    WHEN Dq_Profiling_Score IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS          IS NOT NULL \n");
		SQL.append("    AND TOTAL_ROWS          <> '' \n");
		SQL.append("    THEN CAST(TOTAL_ROWS AS     NUMBER) \n");
		SQL.append("    ELSE CAST(DQRI_RESULT_ID AS NUMBER) \n");
		SQL.append("  END ) )*100 ) AS DQP_SCORE, \n");
		SQL.append("  COUNT(DISTINCT \n");
		SQL.append("  CASE \n");
		SQL.append("    WHEN IS_ECDE                                               = 'Y' \n");
		SQL.append("    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? \n");
		SQL.append("    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < ? \n");
		SQL.append("    THEN DQRR.CDE_DS_ID \n");
		SQL.append("    ELSE '0' \n");
		SQL.append("  END)                                  AS ECDE_CNT, \n");
		SQL.append("  ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE \n");
		SQL.append("FROM V_DQRI_RESULT DQRR \n");
		SQL.append("INNER JOIN V_BUSINESS_PROCESSES VBP \n");
		SQL.append("ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID \n");
		SQL.append("INNER JOIN V_CDE_1 VDE1 \n");
		SQL.append("ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID \n");
		SQL.append("INNER JOIN V_CDE_OWNER CDO \n");
		SQL.append("ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID \n");
		SQL.append("INNER JOIN V_BUCF BUCF \n");
		SQL.append("ON CDO.BUCF_ID = BUCF.BUCF_ID \n");
		SQL.append("WHERE --TOTAL_ROWS<>'' AND Dq_Profiling_Score <>'' AND BUCF.BUCF_NAME <> '' \n");
		SQL.append("  VDE1.ADS         <> '' \n");
		SQL.append("AND BUCF.BUCF_NAME <> '' \n");
		SQL.append("GROUP BY Level1_Process_Name, \n");
		SQL.append("  VDE1.ADS, \n");
		SQL.append("  BUCF.BUCF_NAME \n");
		SQL.append("ORDER BY 1,2");
		String sqlQuery =SQL.toString();

		System.out.println("Inside getImpactScoreL1SrcSystemLegalEntityLOBModel");
		try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();
		
			while(resultSet.next()){
				impactScoreL1SrcSystemLegalEntityLOBModel = new ImpactScoreL1SrcSystemLegalEntityLOBModel();
				
				impactScoreL1SrcSystemLegalEntityLOBModel.setDqpScore(resultSet.getString("DQP_SCORE"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setDqriScore(resultSet.getString("DQRI_SCORE"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setEcdeCnt(resultSet.getString("ECDE_CNT"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setImpactScore(resultSet.getString("IMPACT_SCORE"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setLevel1ProcessDqp(resultSet.getString("LEVEL_1_PROCESS_DQP"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setSourceLOB(resultSet.getString("SOURCE_LOB"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
				impactScoreL1SrcSystemLegalEntityLOBModel.setHeader("DQRI Score/DQP Score/Impact Score <Level1/SourceSystem/LegalEntityLOB>");
				
				list.add(impactScoreL1SrcSystemLegalEntityLOBModel);
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
			return list;
	}

	//////////////////////////////////////////////////////////////////
	

	
//	"INTERNAL CONTROLS - 
	//DQRI Score/DQP Score/Impact Score for  < Level2/SourceSystem/LegalEntityLOB> Process"
	
	@SuppressWarnings("static-access")
	public List<ImpactScoreL2SrcSystemLegalEntityLOBModel> getImpactScoreL2SrcSystemLegalEntityLOBModel(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScoreL2SrcSystemLegalEntityLOBModel impactScoreL2SrcSystemLegalEntityLOBModel = null;
		List<ImpactScoreL2SrcSystemLegalEntityLOBModel> list = new ArrayList<ImpactScoreL2SrcSystemLegalEntityLOBModel>();
		
	String sqlQuery="SELECT Level2_Process_Name AS Level2_Process_DQP, " +
			"  VDE1.ADS                 AS SOURCE_SYSTEM, " +
			"  BUCF.BUCF_NAME           AS SOURCE_LOB, " +
			"  ROUND ( SUM( " +
			"  CASE " +
			"    WHEN DQRI      IS NOT NULL " +
			"    AND TOTAL_ROWS IS NOT NULL " +
			"    AND TOTAL_ROWS <> '' " +
			"    THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) " +
			"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
			"  END )/ SUM( " +
			"  CASE " +
			"    WHEN DQRI      IS NOT NULL " +
			"    AND TOTAL_ROWS IS NOT NULL " +
			"    AND TOTAL_ROWS <> '' " +
			"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
			"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
			"  END ), 1 )AS DQRI_SCORE, " +
			"  ROUND ( ( SUM( " +
			"  CASE " +
			"    WHEN Dq_Profiling_Score IS NOT NULL " +
			"    AND TOTAL_ROWS          IS NOT NULL " +
			"    AND TOTAL_ROWS          <> '' " +
			"    THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) " +
			"    ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) " +
			"  END )/ SUM( " +
			"  CASE " +
			"    WHEN Dq_Profiling_Score IS NOT NULL " +
			"    AND TOTAL_ROWS          IS NOT NULL " +
			"    AND TOTAL_ROWS          <> '' " +
			"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
			"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
			"  END ) )*100 ) AS DQP_SCORE, " +
			"  COUNT(DISTINCT " +
			"  CASE " +
			"    WHEN IS_ECDE                                               = 'Y' " +
			"    AND  TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " +
			"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS')  < ? " +
			"    THEN DQRR.CDE_DS_ID " +
			"    ELSE '0' " +
			"  END)                                  AS ECDE_CNT, " +
			"  ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE " +
			"FROM V_DQRI_RESULT DQRR " +
			"INNER JOIN V_BUSINESS_PROCESSES VBP " +
			"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
			"INNER JOIN V_CDE_1 VDE1 " +
			"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
			"INNER JOIN V_CDE_OWNER CDO " +
			"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
			"INNER JOIN V_BUCF BUCF " +
			"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
			"WHERE VDE1.ADS     <> '' " +
			"AND BUCF.BUCF_NAME <> '' " +
			"GROUP BY Level2_Process_Name, " +
			"  VDE1.ADS, " +
			"  BUCF.BUCF_NAME " +
			"ORDER BY 1,2";
	
		//System.out.println("Inside getImpactScoreL2SrcSystemLegalEntityLOBModel ********_____________" +sqlQuery);
	try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();
	
		while(resultSet.next()){
			impactScoreL2SrcSystemLegalEntityLOBModel = new ImpactScoreL2SrcSystemLegalEntityLOBModel();
			
			impactScoreL2SrcSystemLegalEntityLOBModel.setDqpScore(resultSet.getString("DQP_SCORE"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setDqriScore(resultSet.getString("DQRI_SCORE"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setEcdeCnt(resultSet.getString("ECDE_CNT"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setImpactScore(resultSet.getString("IMPACT_SCORE"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setLevel2ProcessDqp(resultSet.getString("LEVEL2_PROCESS_DQP"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setSourceLOB(resultSet.getString("SOURCE_LOB"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
			impactScoreL2SrcSystemLegalEntityLOBModel.setHeader("/DQRI Score/DQP Score/Impact Score <Level2/SourceSystem/LegalEntityLOB>");
			
			list.add(impactScoreL2SrcSystemLegalEntityLOBModel);
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
	//System.out.println("list size is >>>>>>>>>>>>>>>"+list.size());
	return list;
}
	
	
/*	"INTERNAL CONTROLS - 
DQRI Score/DQP Score/Impact Score for  < SourceSystem/SourceSystem/LegalEntityLOB> Process"
*/

	@SuppressWarnings("static-access")
	public List<ImpactScoreSrcSysSrcSystemLegalEntityLOBModel> getImpactScoreSrcSysSrcSystemLegalEntityLOBModel(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScoreSrcSysSrcSystemLegalEntityLOBModel impactScoreSrcSysSrcSystemLegalEntityLOBModel = null;
		List<ImpactScoreSrcSysSrcSystemLegalEntityLOBModel> list = new ArrayList<ImpactScoreSrcSysSrcSystemLegalEntityLOBModel>();

		String sqlQuery="SELECT VDE1.ADS  AS SOURCE_SYSTEM, " +
				"  BUCF.BUCF_NAME AS SOURCE_LOB, " +
				"  ROUND ( SUM( " +
				"  CASE " +
				"    WHEN DQRI      IS NOT NULL " +
				"    AND TOTAL_ROWS IS NOT NULL " +
				"    AND TOTAL_ROWS <> '' " +
				"    THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) " +
				"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"  END )/ SUM( " +
				"  CASE " +
				"    WHEN DQRI      IS NOT NULL " +
				"    AND TOTAL_ROWS IS NOT NULL " +
				"    AND TOTAL_ROWS <> '' " +
				"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
				"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"  END ), 1 )AS DQRI_SCORE, " +
				"  ROUND ( ( SUM( " +
				"  CASE " +
				"    WHEN Dq_Profiling_Score IS NOT NULL " +
				"    AND TOTAL_ROWS          IS NOT NULL " +
				"    AND TOTAL_ROWS          <> '' " +
				"    THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) " +
				"    ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) " +
				"  END )/ SUM( " +
				"  CASE " +
				"    WHEN Dq_Profiling_Score IS NOT NULL " +
				"    AND TOTAL_ROWS          IS NOT NULL " +
				"    AND TOTAL_ROWS          <> '' " +
				"    THEN CAST(TOTAL_ROWS AS     NUMBER) " +
				"    ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"  END ) )*100 ) AS DQP_SCORE, " +
				"  COUNT(DISTINCT " +
				"  CASE " +
				"    WHEN IS_ECDE                                               = 'Y' " +
				"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " +
				"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS')  < ? " +
				"    THEN DQRR.CDE_DS_ID " +
				"    ELSE '0' " +
				"  END)                                  AS ECDE_CNT, " +
				"  ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE " +
				"	FROM V_DQRI_RESULT DQRR " +
				"	INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"	ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"	INNER JOIN V_CDE_1 VDE1 " +
				"	ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"	INNER JOIN V_CDE_OWNER CDO " +
				"	ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"	INNER JOIN V_BUCF BUCF " +
				"	ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"	WHERE VDE1.ADS     <> '' " +
				"	AND BUCF.BUCF_NAME <> '' " +
				"	GROUP BY VDE1.ADS, " +
				"  BUCF.BUCF_NAME " +
				"	ORDER BY 1,2";

	try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();

		while(resultSet.next()){
			impactScoreSrcSysSrcSystemLegalEntityLOBModel = new ImpactScoreSrcSysSrcSystemLegalEntityLOBModel();
			
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setDqpScore(resultSet.getString("DQP_SCORE"));
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setDqriScore(resultSet.getString("DQRI_SCORE"));
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setEcdeCnt(resultSet.getString("ECDE_CNT"));
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setImpactScore(resultSet.getString("IMPACT_SCORE"));			
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setSourceLOB(resultSet.getString("SOURCE_LOB"));
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
			impactScoreSrcSysSrcSystemLegalEntityLOBModel.setHeader("DQRI Score/DQP Score/Impact Score<SourceSystem/SourceSystem/LegalEntityLOB>");
			
			list.add(impactScoreSrcSysSrcSystemLegalEntityLOBModel);
		}
	}catch (Exception e) {
		e.printStackTrace();
		System.out.println(e.getMessage());
	}finally{
		if(null != connection){
			dbConnection.close_Connection(connection);
		}
	}
		//dbConnection.close_Connection(connection);
	
	return list;
}	
		
		
		
	/*	"INTERNAL CONTROLS - 
		ECDE COUNT/ECDE RECORDS Tested FOR <SourceSystem/SourceSystem/LegalEntityLOB>"
*/
	@SuppressWarnings("static-access")
	public List<EcdeCountEcdeRecords> getEcdeCountEcdeRecords(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		EcdeCountEcdeRecords ecdeCountEcdeRecords = null;
		List<EcdeCountEcdeRecords> list = new ArrayList<EcdeCountEcdeRecords>();

		String sqlQuery="SELECT VDE1.ADS  AS SOURCE_SYSTEM, " +
				"  BUCF.BUCF_NAME AS SOURCE_LOB, " +
				"  COUNT(DISTINCT " +
				"  CASE " +
				"    WHEN IS_ECDE                                               = 'Y' " +
				"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " +
				"    AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < ?" +
				"    THEN DQRR.CDE_DS_ID " +
				"    ELSE '0' " +
				"  END) AS ECDE_CNT, " +
				"  ROUND(SUM( " +
				"  CASE " +
				"    WHEN TOTAL_ROWS <> '' " +
				"    THEN CAST(TOTAL_ROWS AS NUMBER) " +
				"    ELSE 0 " +
				"  END)/1000) AS ECDE_RCRDS_TSTD " +
				"FROM V_DQRI_RESULT DQRR " +
				"INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"INNER JOIN V_CDE_1 VDE1 " +
				"ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"INNER JOIN V_CDE_OWNER CDO " +
				"ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"INNER JOIN V_BUCF BUCF " +
				"ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"WHERE VDE1.ADS     <> '' " +
				"AND BUCF.BUCF_NAME <> '' " +
				"GROUP BY VDE1.ADS, " +
				"  BUCF.BUCF_NAME " +
				"ORDER BY 1,2";
		
	try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();

			while(resultSet.next()){
				ecdeCountEcdeRecords = new EcdeCountEcdeRecords();
				
				ecdeCountEcdeRecords.setEcdeCnt(resultSet.getString("ECDE_CNT"));					
				ecdeCountEcdeRecords.setSourceLOB(resultSet.getString("SOURCE_LOB"));
				ecdeCountEcdeRecords.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
				ecdeCountEcdeRecords.setEcdeRcrdsTstd(resultSet.getString("ECDE_RCRDS_TSTD"));
				ecdeCountEcdeRecords.setHeader("ECDE COUNT/ECDE RECORDS Tested FOR <SourceSystem/SourceSystem/LegalEntityLOB>");
				
				list.add(ecdeCountEcdeRecords);
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
		return list;
	}	

	//"INTERNAL CONTROLS - 
	//DQRI Score/DQP Score/Impact Score for  < LegalEntityLOB/SourceSystem/LegalEntityLOB> Process"

	@SuppressWarnings("static-access")
	public List<ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB> getImpactScoreForLegalEntityLOBSourceSystemLegalEntityLOB(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB = null;
		List<ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB> list = new ArrayList<ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB>();

		String sqlQuery="SELECT SOURCE_SYSTEM, " +
				"  SOURCE_LOB, " +
				"  LEGAL_ENTITY_LOB, " +
				"  DQRI_SCORE, " +
				"  DQP_SCORE, " +
				"  ECDE_CNT, " +
				"  IMPACT_SCORE " +
				"FROM " +
				"  (SELECT VDE1.BUCF_NAME AS LEGAL_ENTITY_LOB, " +
				"    VDE1.ADS             AS SOURCE_SYSTEM, " +
				"    BUCF.BUCF_NAME       AS SOURCE_LOB, " +
				"    ROUND ( SUM( " +
				"    CASE " +
				"      WHEN DQRI      IS NOT NULL " +
				"      AND TOTAL_ROWS IS NOT NULL " +
				"      AND TOTAL_ROWS <> '' " +
				"      THEN CAST(TOTAL_ROWS AS     NUMBER)*(DQRI) " +
				"      ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"    END )/ SUM( " +
				"    CASE " +
				"      WHEN DQRI      IS NOT NULL " +
				"      AND TOTAL_ROWS IS NOT NULL " +
				"      AND TOTAL_ROWS <> '' " +
				"      THEN CAST(TOTAL_ROWS AS     NUMBER) " +
				"      ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"    END ), 1 )AS DQRI_SCORE, " +
				"    ROUND ( ( SUM( " +
				"    CASE " +
				"      WHEN Dq_Profiling_Score IS NOT NULL " +
				"      AND TOTAL_ROWS          IS NOT NULL " +
				"      AND TOTAL_ROWS          <> '' " +
				"      THEN (TOTAL_ROWS)*CAST(Dq_Profiling_Score AS NUMBER) " +
				"      ELSE CAST(DQRI_RESULT_ID AS                  NUMBER) " +
				"    END )/ SUM( " +
				"    CASE " +
				"      WHEN Dq_Profiling_Score IS NOT NULL " +
				"      AND TOTAL_ROWS          IS NOT NULL " +
				"      AND TOTAL_ROWS          <> '' " +
				"      THEN CAST(TOTAL_ROWS AS     NUMBER) " +
				"      ELSE CAST(DQRI_RESULT_ID AS NUMBER) " +
				"    END ) )*100 ) AS DQP_SCORE, " +
				"    COUNT(DISTINCT " +
				"    CASE " +
				"      WHEN IS_ECDE                                               = 'Y' " +
				"      AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS')  > ? " +
				"      AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') <  ? " +
				"      THEN DQRR.CDE_DS_ID " +
				"      ELSE '0' " +
				"    END)                                  AS ECDE_CNT, " +
				"    ROUND(AVG(CAST(IMPACT AS NUMBER)), 1) AS IMPACT_SCORE " +
				"  FROM V_DQRI_RESULT DQRR " +
				"  INNER JOIN V_BUSINESS_PROCESSES VBP " +
				"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " +
				"  INNER JOIN V_CDE_1 VDE1 " +
				"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " +
				"  INNER JOIN V_CDE_OWNER CDO " +
				"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " +
				"  INNER JOIN V_BUCF BUCF " +
				"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " +
				"  WHERE VDE1.ADS     <> '' " +
				"  AND BUCF.BUCF_NAME <> '' " +
				"  GROUP BY VDE1.BUCF_NAME, " +
				"    VDE1.ADS, " +
				"    BUCF.BUCF_NAME " +
				"  ) " +
				"ORDER BY 1,2";
		
		try{
			PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
			prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
			prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
			ResultSet resultSet=prepStmt.executeQuery();
		
			while(resultSet.next()){
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB = new ImpactScroreForLegalEntityLOBSourceSystemLegalEntityLOB();
				
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setEcdeCnt(resultSet.getString("ECDE_CNT"));					
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setSourceLOB(resultSet.getString("SOURCE_LOB"));
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setDqpScore(resultSet.getString("DQP_SCORE"));
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setDqriScore(resultSet.getString("DQRI_SCORE"));			
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setLegalEntityLOB(resultSet.getString("LEGAL_ENTITY_LOB"));
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setImpactScore(resultSet.getString("IMPACT_SCORE"));	
				impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB.setHeader("DQRI Score/DQP Score/Impact Score for  < LegalEntityLOB/SourceSystem/LegalEntityLOB> Process");
				list.add(impactScroreForLegalEntityLOBSourceSystemLegalEntityLOB);
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
			return list;
	}
	
	//"INTERNAL CONTROLS - 
	//ECDE COUNT/ECDE RECORDS Tested FOR <LegalEntityLOB/SourceSystem/LegalEntityLOB>"

	
	@SuppressWarnings("static-access")
	public List<ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB> getECDErecordsTestedForLegalEntityLOBSourceSystemLegalEntityLOB(java.util.Date icStartDate,java.util.Date icEndDate)throws Exception {
		Connection connection  = dbConnection.connection();
		ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB = null;
		List<ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB> list = new ArrayList<ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB>();

		String sqlQuery="SELECT SOURCE_SYSTEM, " 
				+"  SOURCE_LOB, " 
				+"  LEGAL_ENTITY_LOB, " 
				+"  ECDE_CNT, " 
				+"  ECDE_RCRDS_TSTD " 
				+"FROM " 
				+"  (SELECT VDE1.BUCF_NAME AS LEGAL_ENTITY_LOB, " 
				+"    VDE1.ADS             AS SOURCE_SYSTEM, " 
				+"    BUCF.BUCF_NAME       AS SOURCE_LOB, " 
				+"    COUNT(DISTINCT " 
				+"    CASE " 
				+"      WHEN IS_ECDE                                               = 'Y' " 
				+"      AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') > ? " 
				+"      AND TO_DATE(VDE1.Ecde_Assign_Date,'YYYY-MM-DD HH24:MI:SS') < ? " 
				+"      THEN DQRR.CDE_DS_ID " 
				+"      ELSE '0' " 
				+"    END) AS ECDE_CNT, " 
				+"    ROUND(SUM( " 
				+"    CASE " 
				+"      WHEN TOTAL_ROWS <> '' " 
				+"      THEN CAST(TOTAL_ROWS AS NUMBER) " 
				+"      ELSE 0 " 
				+"    END)/1000) AS ECDE_RCRDS_TSTD " 
				+"  FROM V_DQRI_RESULT DQRR " 
				+"  INNER JOIN V_BUSINESS_PROCESSES VBP " 
				+"  ON DQRR.LEVEL1_PROCESS_ID= VBP.LEVEL1_PROCESS_ID " 
				+"  INNER JOIN V_CDE_1 VDE1 " 
				+"  ON DQRR.CDE_DS_ID = VDE1.CDE_DS_ID " 
				+"  INNER JOIN V_CDE_OWNER CDO " 
				+"  ON DQRR.CDE_DS_ID = CDO.CDE_DS_ID " 
				+"  INNER JOIN V_BUCF BUCF " 
				+"  ON CDO.BUCF_ID      = BUCF.BUCF_ID " 
				+"  WHERE VDE1.ADS     <> '' " 
				+"  AND BUCF.BUCF_NAME <> '' " 
				+"  GROUP BY VDE1.BUCF_NAME, " 
				+"    VDE1.ADS, " 
				+"    BUCF.BUCF_NAME " 
				+"  ORDER BY 1,2 " 
				+"  ) A";
		
	try{
		PreparedStatement prepStmt=connection.prepareStatement(sqlQuery);
		prepStmt.setTimestamp(1, new java.sql.Timestamp(icStartDate.getTime()));
		prepStmt.setTimestamp(2,  new java.sql.Timestamp(icEndDate.getTime()));
		ResultSet resultSet=prepStmt.executeQuery();
			
		while(resultSet.next()){
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB = new ECDECountForLegalEntityLOBSourceSystemLegalEntityLOB();
				
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setEcdeCnt(resultSet.getString("ECDE_CNT"));					
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setSourceLOB(resultSet.getString("SOURCE_LOB"));
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setSourceSystem(resultSet.getString("SOURCE_SYSTEM"));
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setLegalEntityLOB(resultSet.getString("LEGAL_ENTITY_LOB"));
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setEcdeRecordsTested(resultSet.getString("ECDE_RCRDS_TSTD"));
				eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB.setHeader("DQRI Score/DQP Score/Impact Score for  < LegalEntityLOB/SourceSystem/LegalEntityLOB> Process");
				list.add(eCDECountForLegalEntityLOBSourceSystemLegalEntityLOB);
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
		return list;
	}
 }
