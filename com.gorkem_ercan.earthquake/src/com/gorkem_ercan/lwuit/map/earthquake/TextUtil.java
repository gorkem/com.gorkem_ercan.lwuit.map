/*******************************************************************************
 * Copyright (c) 2012 Gorkem Ercan and others.
 * 
 * All rights reserved. This program and the accompanying materials are 
 * made available under the terms of the the Apache License, Version 2.0 
 * (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *         
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contributors:
 *    Gorkem Ercan - initial API and implementation and/or initial documentation
 *******************************************************************************/
package com.gorkem_ercan.lwuit.map.earthquake;

import java.util.Calendar;
import java.util.Date;

public class TextUtil {
	private static String[] months = { "January", "February", "March", "April", 
		"May", "June","July","August",
		"September","October","November","December"};
		
	public static String formatDate(Date date){
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(date);
	     
	     int day = calendar.get(Calendar.DAY_OF_MONTH);
		 int month = calendar.get(Calendar.MONTH);
		// int year = calendar.get(Calendar.YEAR);
		 int hour = calendar.get(Calendar.HOUR_OF_DAY);
		 int mins = calendar.get(Calendar.MINUTE);
		 StringBuffer buf = new StringBuffer();
		 buf.append(day);
		 buf.append(" ");
		 buf.append(months[month]);
		 buf.append(" ");
		 buf.append(hour);
		 buf.append(":");
		 if(mins<10){
			 buf.append("0");
		 }
		 buf.append(mins);
		 return buf.toString();
	}

}
