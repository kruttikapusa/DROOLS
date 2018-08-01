package com.deloitte.bedqd.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.bedqd.model.InternalControlsModel;
import com.deloitte.bedqd.scheduler.JSONSchedulerService;
import com.deloitte.bedqd.service.DataQualityService;
import com.deloitte.bedqd.service.InternalControlService;

@RestController
public class DataQualityController {

	@Autowired
	DataQualityService service;
	
	@Autowired
	InternalControlService internalControlService;
	
	@RequestMapping(value="/dataQualityMonitoring", method = RequestMethod.GET)
	@CrossOrigin
	public Object getDataQualityEndPoint(@RequestParam("DMPriorStartDate") String dmPriorStartDate,
			@RequestParam("DMPriorEndDate") String dmPriorEndDate, @RequestParam("DMCurrentStartDate") String dmCurrentStartDate,
			@RequestParam("DMCurrentEndDate") String dmCurrentEndDate){
		Date priorStartDate = null;
		Date priorEndDate = null;
		Date currentStartDate= null;
		Date currentEndDate= null;
		try 
		{
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			priorStartDate = format.parse(dmPriorStartDate);
			priorEndDate = format.parse(dmPriorEndDate);
			currentStartDate= format.parse(dmCurrentStartDate);
			currentEndDate= format.parse(dmCurrentEndDate);
			
			System.out.println("Inside getDataQualityEndPoint :: priorStartDate = " + priorStartDate+ " priorEndDate = " + priorEndDate 
					+ " currentStartDate = " + currentStartDate + " currentEndDate = " + currentEndDate);
		
		} catch (ParseException e) {
			System.out.println("Error occured in getDataQualityEndPoint");
			e.printStackTrace();
		}

		return service.getDQMonitoringData(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
	}

	@RequestMapping(value="/keyHighlights", method = RequestMethod.GET)
	@CrossOrigin
	public Object getKeyHighlightsEndPoint(@RequestParam("KHPriorStartDate") String kHPriorStartDate,
			@RequestParam("KHPriorEndDate") String kHPriorEndDate, @RequestParam("KHCurrentStartDate") String kHCurrentStartDate,
			@RequestParam("KHCurrentEndDate") String kHCurrentEndDate){
		Date priorStartDate = null;
		Date priorEndDate = null;
		Date currentStartDate= null;
		Date currentEndDate= null;
		try 
		{
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			priorStartDate = format.parse(kHPriorStartDate);
			priorEndDate = format.parse(kHPriorEndDate);
			currentStartDate= format.parse(kHCurrentStartDate);
			currentEndDate= format.parse(kHCurrentEndDate);
			
			System.out.println("Inside getKeyHighlightsEndPoint :: priorStartDate = " + priorStartDate+ " priorEndDate = " + priorEndDate 
					+ " currentStartDate = " + currentStartDate + " currentEndDate = " + currentEndDate);
		
		} catch (ParseException e) {
			System.out.println("Error occured in getKeyHighlightsEndPoint");
			e.printStackTrace();
		}
		return service.getKeyHighlightsData(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
	}
	
	@RequestMapping(value="/measureRemediateDataQuality", method = RequestMethod.GET)
	@CrossOrigin
	public Object getMeasureRemediateDQEndPoint(@RequestParam("MDQPriorStartDate") String mDQPriorStartDate,
			@RequestParam("MDQPriorEndDate") String mDQPriorEndDate, @RequestParam("MDQCurrentStartDate") String mDQCurrentStartDate,
			@RequestParam("MDQCurrentEndDate") String mDQCurrentEndDate){
		Date priorStartDate = null;
		Date priorEndDate = null;
		Date currentStartDate= null;
		Date currentEndDate= null;
		try 
		{
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			priorStartDate = format.parse(mDQPriorStartDate);
			priorEndDate = format.parse(mDQPriorEndDate);
			currentStartDate= format.parse(mDQCurrentStartDate);
			currentEndDate= format.parse(mDQCurrentEndDate);
			
			System.out.println("Inside getMeasureRemediateDQEndPoint :: priorStartDate = " + priorStartDate+ " priorEndDate = " + priorEndDate 
					+ " currentStartDate = " + currentStartDate + " currentEndDate = " + currentEndDate);
		
		} catch (ParseException e) {
			System.out.println("Error occured in getMeasureRemediateDQEndPoint");
			e.printStackTrace();
		}
		return service.getMeasureRemediateDQData(priorStartDate,priorEndDate,currentStartDate,currentEndDate);
	}
	
	@RequestMapping(value="/internalControls", method = RequestMethod.GET)
	@CrossOrigin
	public InternalControlsModel getInternalControlsEndPoint(@RequestParam("ICStartDate") String iCStartDate,@RequestParam("ICEndDate") String iCEndDate){
		Date icStartDate = null;
		Date icEndDate = null;
		
		try 
		{
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			icStartDate = format.parse(iCStartDate);
			icEndDate = format.parse(iCEndDate);
			
			
			System.out.println("Inside getInternalControlsEndPoint :: priorStartDate = " + icStartDate+ " priorEndDate = " + icEndDate);
		
		} catch (ParseException e) {
			System.out.println("Error occured in getInternalControlsEndPoint");
			e.printStackTrace();
		}
		return internalControlService.getInternalControlsData(icStartDate,icEndDate);
	}
    @RequestMapping(value="/refreshDashboardEndPoint", method = RequestMethod.GET)
    @CrossOrigin
    public Object getRefreshDashboardEndPoint(){
          return service.getRefreshDashboardEndPoint();
    }


}


