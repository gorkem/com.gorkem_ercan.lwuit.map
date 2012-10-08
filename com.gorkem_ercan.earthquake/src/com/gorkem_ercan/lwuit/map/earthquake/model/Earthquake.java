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

package com.gorkem_ercan.lwuit.map.earthquake.model;

import java.util.Date;

import com.nokia.maps.common.GeoCoordinate;

public class Earthquake {
	private String detail;
	private Date date;
	private double magnitude;
	private String link;
	private GeoCoordinate coordinate;
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getMagnitude() {
		return magnitude;
	}
	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public GeoCoordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(GeoCoordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	public String toString() {
		StringBuffer buf= new StringBuffer();
		buf.append("Quake[ ");
		buf.append("detail: ").append(detail);
		buf.append(" date: " ).append(date);
		buf.append(" magnitude: ").append(magnitude);
		buf.append(" coords: ").append(coordinate);
		buf.append(" link: ").append(link);
		buf.append("]");
		return buf.toString();
	}
}
