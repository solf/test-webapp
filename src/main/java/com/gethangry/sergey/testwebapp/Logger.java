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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

import io.github.solf.extra2.stacktrace.StackTrace;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO - class description
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
@Slf4j
public class Logger
{
	public static void error(String msg)
	{
		try (	
			MDCCloseable mdc3 = MDC.putCloseable("stackTrace", " " + StackTrace.getShortExceptionStackTrace(new Exception(), Logger.class, true));
		){
			log.error(msg);
		}
		
	}
}
