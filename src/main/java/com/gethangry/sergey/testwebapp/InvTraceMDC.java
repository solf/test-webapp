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

import static io.github.solf.extra2.util.NullUtil.nn;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.github.solf.extra2.stacktrace.StackTrace;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * TODO - class description
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
@RequiredArgsConstructor
public class InvTraceMDC
{
	@NonNull
	private final Throwable stackTrace;
	
	@Nullable
	private final Class<?> skipUpToAndIncludingClass;
	
	@Nullable
	private volatile String cachedStringStackTrace = null;

	@Override
	public String toString()
	{
		String result = cachedStringStackTrace;
		if (result == null)
		{
			result = nn(StackTrace.getShortExceptionStackTrace(stackTrace, skipUpToAndIncludingClass, true));
			cachedStringStackTrace = result;
		}
		return result;
	}
}
