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
package com.gethangry.sergey.insight;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

import com.microsoft.applicationinsights.logback.ApplicationInsightsAppender;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * TODO - class description
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
public class HangryApplicationInsightsAppender extends ApplicationInsightsAppender
{
	@Nullable
	private String instrumentationKey = null;

	@Override
	public void setInstrumentationKey(@Nullable String instrumentationKey)
	{
		super.setInstrumentationKey(instrumentationKey);
		
		this.instrumentationKey = instrumentationKey;
	}

	@Override
	protected void append(ILoggingEvent eventObject)
	{
		{} // MDC doesn't really work reliably here, depends on appender order
		try (MDCCloseable mdc1 = MDC.putCloseable("preProcessed", "true"))
		{
			super.append(eventObject);
		}
	}

	@Override
	public void start()
	{
		if (instrumentationKey == null)
		{
			// Try to read instrumentation key from environment
			String appinsightsInstrKey = System.getenv("APPINSIGHTS_INSTRUMENTATIONKEY");
			if (appinsightsInstrKey != null)
				setInstrumentationKey(appinsightsInstrKey);
		}
		
		super.start();
	}
	
}
