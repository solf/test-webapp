package com.gethangry.sergey.testwebapp;

import java.util.Map;

import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;
import com.microsoft.applicationinsights.telemetry.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAppInsightsProcessor implements TelemetryProcessor
{
	
	static
	{
		log.info("[solf] CustomAppInsightsProcessor loading class...");
	}
	
	/* This method is called for each item of telemetry to be sent.
	   Return false to discard it.
	   Return true to allow other processors to inspect it. */
	@Override
	public boolean process(Telemetry telemetry)
	{
		boolean result = process0(telemetry);
		
		log.info("Filter result {} for: {}", result, telemetry);
		
		return result;
	}
	
	/* This method is called for each item of telemetry to be sent.
	   Return false to discard it.
	   Return true to allow other processors to inspect it. */
//	@Override
	public boolean process0(Telemetry telemetry)
	{
		if (telemetry == null)
			return true;
		
		String env_islive = System.getenv("IS_LIVE"); //"0" or "1"
		int IS_LIVE = 1;
		if (env_islive != null && env_islive.equalsIgnoreCase("1"))
			IS_LIVE = 1;
		else
			IS_LIVE = 0;
		
		Map<String, String> azureProperties = telemetry.getProperties();
		if (azureProperties != null)
		{
			if (azureProperties.containsKey("userid"))
				telemetry.getContext().getUser().setId("u_" + azureProperties.get("userid"));
			else if (azureProperties.containsKey("memberid"))
				telemetry.getContext().getUser().setId("m_" + azureProperties.get("memberid"));
			
			if (azureProperties.containsKey("orderid"))
				telemetry.getContext().getSession().setId("o_" + azureProperties.get("orderid"));
			
			if (azureProperties.containsKey("campusid"))
				telemetry.getContext().getUser().setAccountId("c_" + azureProperties.get("campusid"));
			
			if (azureProperties.containsKey("campus"))
				telemetry.getContext().getUser().setUserAgent(azureProperties.get("campus"));
		}
		
		if (telemetry instanceof RequestTelemetry)
		{
			RequestTelemetry requestTelemetry = (RequestTelemetry) telemetry;
			
			if (requestTelemetry.getUrlString().contains("health"))
				return true;
			
			if (requestTelemetry.getUrlString().contains("getlastordernumber") || requestTelemetry.getUrlString().contains(
					"loginwithcaftoken") || requestTelemetry.getUrlString().contains("getlocationmenu") ||
					requestTelemetry.getUrlString().contains("getopenorders"))
				return false;
			
			int minLength = IS_LIVE == 1 ? 100 : 30;
			Duration duration = requestTelemetry.getDuration();
			if (duration != null)
			{
				long msDuration = duration.getTotalMilliseconds();
				if (msDuration <= minLength)
					return false;
			}
		}
		else if (telemetry instanceof RemoteDependencyTelemetry)
		{
			RemoteDependencyTelemetry dependencyTelemetry = (RemoteDependencyTelemetry) telemetry;
			
			int minLength = IS_LIVE == 1 ? 70 : 30;
			Duration duration = dependencyTelemetry.getDuration();
			if (duration != null)
			{
				long msDuration = duration.getTotalMilliseconds();
				if (msDuration <= minLength)
					return false;
			}
		}
		else if (telemetry instanceof EventTelemetry)
			if (azureProperties != null && azureProperties.containsKey("total ms"))
			{
				String totalMSString = azureProperties.get("total ms");
				try
				{
					double totalms = Double.parseDouble(totalMSString);
					((EventTelemetry) telemetry).getMetrics().put("total ms", totalms);
				}
				catch (Exception e)
				{
				}
			}
		
		return true;
	}
	
}
//RequestTelemetryFilter durationFilter = new RequestTelemetryFilter();
//durationFilter.setMinimumDurationInMS("30");
//configuration.getTelemetryProcessors().add(durationFilter); 