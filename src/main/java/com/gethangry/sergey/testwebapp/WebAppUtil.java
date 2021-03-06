/**
 * Copyright Sergey Olefir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gethangry.sergey.testwebapp;

import java.util.Date;
import java.util.ResourceBundle;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.telemetry.SeverityLevel;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO - class description
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
@Slf4j
public class WebAppUtil
{
	public static final TelemetryClient INSIGHTS;
	
	public static final Date LOAD_DATE = new Date();
	
	static
	{
		String AZURE_TELE_KEY = System.getenv("APPINSIGHTS_INSTRUMENTATIONKEY");
		System.out.println(">>> Instrkey: " + AZURE_TELE_KEY);
		
		TelemetryConfiguration configuration = TelemetryConfiguration.getActive();
		if (AZURE_TELE_KEY != null)
			configuration.setInstrumentationKey(AZURE_TELE_KEY);
		
		configuration.getChannel().setDeveloperMode(true); //logs faster in dev only for performance
		
		CustomAppInsightsProcessor customFilter = new CustomAppInsightsProcessor();
		
		configuration.getTelemetryProcessors().add(customFilter);
		
		INSIGHTS = new TelemetryClient(configuration);
		
		log.warn("INSIGHTS TELEMETRY INITIALIZED");
	}
	
	public static void doStuff()
	{
		try (	
			MDCCloseable mdc1 = MDC.putCloseable("client", "123456");
			MDCCloseable mdc2 = MDC.putCloseable("request", "help");
//			MDCCloseable mdc3 = MDC.putCloseable("stackTrace", StackTrace.getShortExceptionStackTrace(new Exception(), WebAppUtil.class, false));
		){
			
			log.debug("debug Debug debug -- Testing error logging @ " + new Date());
			log.info("info INFO info -- Testing error logging @ " + new Date());
			log.warn("warn WARN WARN -- Testing error logging @ " + new Date());
			err("[2] ERR ERR ERR -- Testing error logging @ " + new Date());
			
			INSIGHTS.trackTrace("AZURE MESSAGE", SeverityLevel.Error);
			
			@SuppressWarnings("unused") Marker fatal = MarkerFactory.getMarker("FATAL");
			
	//		log.error(fatal, "F-A-T-A-L -- Testing error logging @ " + new Date(), new Exception("This is an exception stack trace."));
		}
	}
	
	public static void err(String msg)
	{
		RedirectClass.err2(msg);
	}
	
	public static String getReleaseTimestamp()
	{
		ResourceBundle bundle = ResourceBundle.getBundle("release");
		
		return bundle.getString("release.timestamp");
	}
}
