package com.deloitte.bedqd.scheduler;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.drools.core.io.impl.UrlResource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deloitte.bedqd.controller.DataQualityController;
import com.deloitte.bedqd.service.DataQualityService;
import com.myspace.bedqd.DataMonitoringObject;
import com.myspace.bedqd.InternalControlObject;
import com.myspace.bedqd.KeyHighlightsObject;
import com.myspace.bedqd.MeasureDataQualityObject;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

//import deloittemirza.testingruledrool.DataMonitoring;



import com.deloitte.bedqd.service.DataQualityService;
@Component
@Service 
@EnableCaching
public class JSONSchedulerService {

	
    public static final String FILE_PATH = "D://data//apache-tomcat-7.0.86-windows-x64//apache-tomcat-7.0.86//webapps//BEDQDData//KeyHighlights.json";
    public static final String KEY_HIGHLIGHT_JSON_REST_URL = "http://ussltccsw2220.solutions.glbsnet.com:8088/keyHighlights";
    public static final String FILE_PATH_MRDQ = "D://data//apache-tomcat-7.0.86-windows-x64//apache-tomcat-7.0.86//webapps//BEDQDData//MeasureRemidateDQ.json";
    public static final String  MEASURE_REMEDIATE_DATA_QUALITY_JSON_REST_URL = "http://ussltccsw2220.solutions.glbsnet.com:8088/measureRemediateDataQuality";
    public static final String DQ_MONITORING_FILE_PATH = "D://data//apache-tomcat-7.0.86-windows-x64//apache-tomcat-7.0.86//webapps//BEDQDData//DataQualityMonitoring.json";
	public static final String DQ_MONITORING_JSON_REST_URL = "http://ussltccsw2220.solutions.glbsnet.com:8088/dataQualityMonitoring";
	public static final String FILE_PATH_IC = "D://data//apache-tomcat-7.0.86-windows-x64//apache-tomcat-7.0.86//webapps//BEDQDData//InternalControls.json";
	public static final String INTERNAL_CONTROLS_JSON_REST_URL = "http://ussltccsw2220.solutions.glbsnet.com:8088/internalControls";
	
	 /*public static final String FILE_PATH = "C://Users//kpusa//Desktop//newwww//KeyHighlights.json";
	    public static final String KEY_HIGHLIGHT_JSON_REST_URL = "http://localhost:8088/keyHighlights";
	    public static final String FILE_PATH_MRDQ = "C://Users//kpusa//Desktop//newwww//MeasureRemidateDQ.json";
	    public static final String  MEASURE_REMEDIATE_DATA_QUALITY_JSON_REST_URL = "http://localhost:8088/measureRemediateDataQuality";
	    public static final String DQ_MONITORING_FILE_PATH = "C://Users//kpusa//Desktop//newwww//DataQualityMonitoring.json";
		public static final String DQ_MONITORING_JSON_REST_URL = "http://localhost:8088/dataQualityMonitoring";
		public static final String FILE_PATH_IC = "C://Users//kpusa//Desktop//newwww//InternalControls.json";
		public static final String INTERNAL_CONTROLS_JSON_REST_URL = "http://localhost:8088/internalControls";*/
		
    @Autowired
    DataQualityService service;
    
   // @Scheduled(fixedDelay=120000)
	public void writeJSONToFile(){
    	System.out.println("Inside JSON writer");
    	writeKeyHighlightJSON();
    	writeInternalControlsJSON();
    	writeMeasureRemediateJSON();
    	writeDQMonitoringJSON();
	}
	 private void writeMeasureRemediateJSON() {
	      System.out.println("Inside Measure Remediate JSON writer");
			 
			 try {
					
					HashMap<String,Date> dateMap = getMeasureRemediateDateParamFromDrool();
					
					JSONObject jsonObject = getMeasureRemediateJSON(dateMap);
					FileWriter fileWriter = new FileWriter(JSONSchedulerService.FILE_PATH_MRDQ);
					fileWriter.write(jsonObject.toJSONString());
					fileWriter.flush();
					fileWriter.close();
					
				} catch (Exception e) {
					System.out.println("Error occured in writeInternalControlsJSON::writeJSONToFile :" + e);
					e.printStackTrace();
					
				}
			
		}
	 private JSONObject getMeasureRemediateJSON(HashMap<String, Date> dateMap) throws Exception {
			JSONObject jsonObject = new JSONObject();
			 try {
				 
				 DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				 String mDQPriorStartDate = format.format(dateMap.get("MDQPriorStartDate"));
				 String mDQPriorEndDate = format.format(dateMap.get("MDQPriorEndDate"));
				 String mDQCurrentStartDate = format.format(dateMap.get("MDQCurrentStartDate"));
				 String mDQCurrentEndDate = format.format(dateMap.get("MDQCurrentEndDate"));
				 
				 System.out.println("in getMeasureRemediateJSON MDQPriorStartDate = " + mDQPriorStartDate 
						 + " MDQPriorEndDate = "+ mDQPriorEndDate + " MDQCurrentStartDate = " + mDQCurrentStartDate
						 + " MDQCurrentEndDate = " + mDQCurrentEndDate);
				 
				 	String restUrl = JSONSchedulerService. MEASURE_REMEDIATE_DATA_QUALITY_JSON_REST_URL+"?MDQPriorStartDate="+mDQPriorStartDate
				 			+"&MDQPriorEndDate="+mDQPriorEndDate+"&MDQCurrentStartDate="+mDQCurrentStartDate+"&MDQCurrentEndDate="+mDQCurrentEndDate;
					URL url = new URL(restUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");

					InputStream inputStream =conn.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(inputStreamReader);

					JSONParser jsonParser = new JSONParser();
					jsonObject = (JSONObject)jsonParser.parse(inputStreamReader);
					System.out.println(jsonObject.toJSONString());
					conn.disconnect();
				
				  } catch (MalformedURLException e) {
					  System.out.println("Error occured in JSONWriter::getKeyHighlightJSON :" + e);
					  e.printStackTrace();

				  } catch (IOException e) {
					  System.out.println("Error occured in JSONWriter::getKeyHighlightJSON  :" + e);
					  e.printStackTrace();

				  }
			 return jsonObject;
				
		}
		private HashMap<String, Date> getMeasureRemediateDateParamFromDrool() throws Exception {
			HashMap<String,Date> dateMap = new HashMap<String,Date>();
			
			 KieServices ks = KieServices.Factory.get();
	        KieResources resources = ks.getResources();
	        String url = "http://ussltccsw2220.solutions.glbsnet.com:9080/kie-wb/maven2/com/myspace/BEDQD/1.0.0/BEDQD-1.0.0.jar";
	        UrlResource urlResource = (UrlResource) resources.newUrlResource(url);
	        urlResource.setUsername("workbench");
	        urlResource.setPassword("workbench1!");
	        urlResource.setBasicAuthentication("enabled");
	        InputStream stream = urlResource.getInputStream();
	        KieRepository repo = ks.getRepository();
	        KieModule k = repo.addKieModule(resources.newInputStreamResource(stream));
	        KieContainer kc = ks.newKieContainer(k.getReleaseId());
	        KieBase kBase = kc.getKieBase();
	        System.out.println(kBase.getKiePackage("com.myspace.bedqd").getRules());

	        KieSession kSession = kBase.newKieSession();
	      
	        MeasureDataQualityObject measureRemediateDataQuality = new MeasureDataQualityObject();
	        measureRemediateDataQuality.setMdqRuleName("MeasureDataQualityForDates");
	        kSession.insert(measureRemediateDataQuality);
	        System.out.println("Fact count "+kSession.getFactCount());
	        kSession.fireAllRules();
	        System.out.println("Rules fires "+kSession.fireAllRules());
	       
	        dateMap.put("MDQPriorStartDate", measureRemediateDataQuality.getMdqPriorStartDate()); 
	        dateMap.put("MDQPriorEndDate", measureRemediateDataQuality.getMdqPriorEndDate()); 
	        dateMap.put("MDQCurrentStartDate",measureRemediateDataQuality.getMdqCurrentStartDate()); 
	        dateMap.put("MDQCurrentEndDate", measureRemediateDataQuality.getMdqCurrentEndDate()); 
	        
	        kSession.dispose();
	        return dateMap;
		}

	 public void writeInternalControlsJSON() {
		 System.out.println("Inside Internal Controls JSON writer");
		 
		 try {
				
				HashMap<String,Date> dateMap = getInternalControlsDateParamFromDrool();
				
				JSONObject jsonObject = getInternalControlsJSON(dateMap);
				FileWriter fileWriter = new FileWriter(JSONSchedulerService.FILE_PATH_IC);
				fileWriter.write(jsonObject.toJSONString());
				fileWriter.flush();
				fileWriter.close();
				
			} catch (Exception e) {
				System.out.println("Error occured in writeInternalControlsJSON::writeJSONToFile :" + e);
				e.printStackTrace();
				
			}

	 }
    public void writeKeyHighlightJSON(){
		System.out.println("Inside Key Highlight JSON writer");
		try {
			
			HashMap<String,Date> dateMap = getKeyHighlightDateParamFromDrool();
			
			JSONObject jsonObject = getKeyHighlightJSON(dateMap);
			FileWriter fileWriter = new FileWriter(JSONSchedulerService.FILE_PATH);
			fileWriter.write(jsonObject.toJSONString());
			fileWriter.flush();
			fileWriter.close();
			
		} catch (Exception e) {
			System.out.println("Error occured in JSONWriter::writeJSONToFile :" + e);
			e.printStackTrace();
			
		}

    }
    
	private JSONObject getKeyHighlightJSON(HashMap<String,Date> dateMap) throws Exception{
		JSONObject jsonObject = new JSONObject();
		 try {
			 
			 DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 String kHPriorStartDate = format.format(dateMap.get("KHPriorStartDate"));
			 String kHPriorEndDate = format.format(dateMap.get("KHPriorEndDate"));
			 String kHCurrentStartDate = format.format(dateMap.get("KHCurrentStartDate"));
			 String kHCurrentEndDate = format.format(dateMap.get("KHCurrentEndDate"));
			 
			 System.out.println("in getKeyHighlightJSON KHPriorStartDate = " + kHPriorStartDate 
					 + " KHPriorEndDate = "+ kHPriorEndDate + " KHCurrentStartDate = " + kHCurrentStartDate
					 + " KHCurrentEndDate = " + kHCurrentEndDate);
			 
			 	String restUrl = JSONSchedulerService.KEY_HIGHLIGHT_JSON_REST_URL+"?KHPriorStartDate="+kHPriorStartDate
			 			+"&KHPriorEndDate="+kHPriorEndDate+"&KHCurrentStartDate="+kHCurrentStartDate+"&KHCurrentEndDate="+kHCurrentEndDate;
				URL url = new URL(restUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				InputStream inputStream =conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader);

				JSONParser jsonParser = new JSONParser();
				jsonObject = (JSONObject)jsonParser.parse(inputStreamReader);
				System.out.println(jsonObject.toJSONString());
				conn.disconnect();
			
			  } catch (MalformedURLException e) {
				  System.out.println("Error occured in JSONWriter::getKeyHighlightJSON :" + e);
				  e.printStackTrace();

			  } catch (IOException e) {
				  System.out.println("Error occured in JSONWriter::getKeyHighlightJSON  :" + e);
				  e.printStackTrace();

			  }
		 return jsonObject;
			
	}
	private JSONObject getInternalControlsJSON(HashMap<String,Date> dateMap) throws Exception{
		JSONObject jsonObject = new JSONObject();
		 try {
			 
			 DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 String iCStartDate = format.format(dateMap.get("ICStartDate"));
			 String iCEndDate = format.format(dateMap.get("ICEndDate"));
			
			 
			 System.out.println("in getInternalControlsJSON ICStartDate = " + iCStartDate 
					 + " ICEndDate = "+ iCEndDate);
			 
			 	String restUrl = JSONSchedulerService.INTERNAL_CONTROLS_JSON_REST_URL+"?ICStartDate="+iCStartDate
			 			+"&ICEndDate="+iCEndDate;
				URL url = new URL(restUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				InputStream inputStream =conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader);

				JSONParser jsonParser = new JSONParser();
				jsonObject = (JSONObject)jsonParser.parse(inputStreamReader);
				System.out.println(jsonObject.toJSONString());
				conn.disconnect();
			
			  } catch (MalformedURLException e) {
				  System.out.println("Error occured in JSONWriter::getInternalControlsJSON :" + e);
				  e.printStackTrace();

			  } catch (IOException e) {
				  System.out.println("Error occured in JSONWriter::getInternalControlsJSON  :" + e);
				  e.printStackTrace();

			  }
		 return jsonObject;
			
	}
	public HashMap<String,Date> getKeyHighlightDateParamFromDrool() throws Exception{
		
		HashMap<String,Date> dateMap = new HashMap<String,Date>();
		
		 KieServices ks = KieServices.Factory.get();
         KieResources resources = ks.getResources();
         String url = "http://ussltccsw2220.solutions.glbsnet.com:9080/kie-wb/maven2/com/myspace/BEDQD/1.0.0/BEDQD-1.0.0.jar";
         UrlResource urlResource = (UrlResource) resources.newUrlResource(url);
         urlResource.setUsername("workbench");
         urlResource.setPassword("workbench1!");
         urlResource.setBasicAuthentication("enabled");
         InputStream stream = urlResource.getInputStream();
         KieRepository repo = ks.getRepository();
         KieModule k = repo.addKieModule(resources.newInputStreamResource(stream));
         KieContainer kc = ks.newKieContainer(k.getReleaseId());
         KieBase kBase = kc.getKieBase();
         System.out.println(kBase.getKiePackage("com.myspace.bedqd").getRules());

         KieSession kSession = kBase.newKieSession();
       
         
         KeyHighlightsObject keyHighlight = new KeyHighlightsObject();
         keyHighlight.setKhRuleName("KeyHighlightsForDates");
         kSession.insert(keyHighlight);
         System.out.println("Fact count "+kSession.getFactCount());
         kSession.fireAllRules();
         System.out.println("Rules fires "+kSession.fireAllRules());
       
         dateMap.put("KHPriorStartDate", keyHighlight.getKhPriorStartDate()); 
         dateMap.put("KHPriorEndDate", keyHighlight.getKhPriorEndDate()); 
         dateMap.put("KHCurrentStartDate",keyHighlight.getKhCurrentStartDate()); 
         dateMap.put("KHCurrentEndDate", keyHighlight.getKhCurrentEndDate()); 
         
         
         kSession.dispose();
         
         return dateMap;
	}
public HashMap<String,Date> getInternalControlsDateParamFromDrool() throws Exception{
		
		HashMap<String,Date> dateMap = new HashMap<String,Date>();
		
		/* KieServices ks = KieServices.Factory.get();
         KieResources resources = ks.getResources();
         String url = "http://ussltccsw2220.solutions.glbsnet.com:9080/kie-wb/maven2/com/myspace/BEDQD/1.0.0/BEDQD-1.0.0.jar";
         UrlResource urlResource = (UrlResource) resources.newUrlResource(url);
         urlResource.setUsername("workbench");
         urlResource.setPassword("workbench1!");
         urlResource.setBasicAuthentication("enabled");
         InputStream stream = urlResource.getInputStream();
         KieRepository repo = ks.getRepository();
         KieModule k = repo.addKieModule(resources.newInputStreamResource(stream));
         KieContainer kc = ks.newKieContainer(k.getReleaseId());
         KieBase kBase = kc.getKieBase();
         System.out.println(kBase.getKiePackage("com.myspace.bedqd").getRules());

         KieSession kSession = kBase.newKieSession();
       
         InternalControlObject internalControls = new InternalControlObject();
         internalControls.setInternalRuleName("InternalControlForDates");
         kSession.insert(internalControls);
         System.out.println("Fact count "+kSession.getFactCount());
         kSession.fireAllRules();
         System.out.println("Rules fires "+kSession.fireAllRules());
       		*/
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		
         dateMap.put("ICStartDate", sdf.parse("06/30/2014")); 
         dateMap.put("ICEndDate", sdf.parse("12/30/2014")); 
                  
         
         //kSession.dispose();
         
         return dateMap;
	}
public void writeDQMonitoringJSON(){
	System.out.println("Inside DQ Monitoring JSON writer");
	try {
		HashMap<String,Date> dateMap = getDQMonitoringDateParamFromDrool();
		
		JSONObject jsonObject = getDQMonitoringJSON(dateMap);
		FileWriter fileWriter = new FileWriter(JSONSchedulerService.DQ_MONITORING_FILE_PATH);
		fileWriter.write(jsonObject.toJSONString());
		fileWriter.flush();
		fileWriter.close();

	} catch (Exception e) {
		System.out.println("Error occured in JSONWriter::writeJSONToFile :" + e);
		e.printStackTrace();
	}
		
	}
	
	public HashMap<String,Date> getDQMonitoringDateParamFromDrool() throws Exception{
		
		HashMap<String,Date> dateMap = new HashMap<String,Date>();
		
		 KieServices ks = KieServices.Factory.get();
         KieResources resources = ks.getResources();
         String url = "http://ussltccsw2220.solutions.glbsnet.com:9080/kie-wb/maven2/com/myspace/BEDQD/1.0.0/BEDQD-1.0.0.jar";
         UrlResource urlResource = (UrlResource) resources.newUrlResource(url);
         urlResource.setUsername("workbench");
         urlResource.setPassword("workbench1!");
         urlResource.setBasicAuthentication("enabled");
         InputStream stream = urlResource.getInputStream();
         KieRepository repo = ks.getRepository();
         KieModule k = repo.addKieModule(resources.newInputStreamResource(stream));
         KieContainer kc = ks.newKieContainer(k.getReleaseId());
         KieBase kBase = kc.getKieBase();
         System.out.println(kBase.getKiePackage("com.myspace.bedqd").getRules());

         KieSession kSession = kBase.newKieSession();
       
         DataMonitoringObject datamonitoring = new DataMonitoringObject();
         datamonitoring.setDmRuleName("DataMonitoringRuleForDates");
         kSession.insert(datamonitoring);
         System.out.println("Fact count "+kSession.getFactCount());
         kSession.fireAllRules();
         System.out.println("Rules fires "+kSession.fireAllRules());
        
		 dateMap.put("DMPriorStartDate",  datamonitoring.getDmPriorStartDate()); 
         dateMap.put("DMPriorEndDate", datamonitoring.getDmPriorEndDate());
         dateMap.put("DMCurrentStartDate", datamonitoring.getDmCurrentStartDate()); 
         dateMap.put("DMCurrentEndDate", datamonitoring.getDmCurrentEndDate()); 
         
         
         kSession.dispose();
         
         return dateMap;
	}

	
	private JSONObject getDQMonitoringJSON(HashMap<String,Date> dateMap) throws Exception{
		JSONObject jsonObject = new JSONObject();
		 try {
			 
			 DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 String dmpriorStartDate = format.format(dateMap.get("DMPriorStartDate"));
			 String dmpriorEndDate = format.format(dateMap.get("DMPriorEndDate"));
			 String dmcurrentStartDate = format.format(dateMap.get("DMCurrentStartDate"));
			 String dmcurrentEndDate = format.format(dateMap.get("DMCurrentEndDate"));
			 
			 System.out.println("in getDataQualityJSON DMPriorStartDate = " + dmpriorStartDate 
					 + " DMPriorEndDate = "+ dmpriorEndDate + " DMCurrentStartDate = " + dmcurrentStartDate
					 + " DMCurrentEndDate = " + dmcurrentEndDate);
			 
			 	String restUrl = JSONSchedulerService.DQ_MONITORING_JSON_REST_URL+"?DMPriorStartDate="+dmpriorStartDate
			 			+"&DMPriorEndDate="+dmpriorEndDate+"&DMCurrentStartDate="+dmcurrentStartDate+"&DMCurrentEndDate="+dmcurrentEndDate;
				URL url = new URL(restUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				InputStream inputStream =conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader);

				JSONParser jsonParser = new JSONParser();
				jsonObject = (JSONObject)jsonParser.parse(inputStreamReader);
				System.out.println(jsonObject.toJSONString());
				conn.disconnect();
			
			  } catch (MalformedURLException e) {
				  System.out.println("Error occured in JSONWriter::getDQMonitoringJSON :" + e);
				  e.printStackTrace();

			  } catch (IOException e) {
				  System.out.println("Error occured in JSONWriter::getDQMonitoringJSON  :" + e);
				  e.printStackTrace();

			  }
		 return jsonObject;
			
	}
	


}
