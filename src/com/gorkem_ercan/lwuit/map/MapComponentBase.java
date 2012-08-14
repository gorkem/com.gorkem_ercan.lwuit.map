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
package com.gorkem_ercan.lwuit.map;

import javax.microedition.lcdui.Graphics;

import com.nokia.maps.map.EventListener;
import com.nokia.maps.map.MapComponent;
import com.nokia.maps.map.MapDisplay;

public class MapComponentBase implements MapComponent{
	
	protected static final String VERSION = "0.1";
	private String id;
	private String ver;
	protected MapDisplay map;

	public MapComponentBase(String id, String version) {
		this.id = id;
		this.ver = version;
	}

	public EventListener getEventListener() {
		return null;
	}

	public void mapUpdated(boolean arg0) {
	}

	public void paint(Graphics arg0) {
	}

	public void attach(MapDisplay map) {
		this.map = map;
	
	}

	public void detach(MapDisplay map) {
		this.map = null;
	
	}

	public String getId() {
		return this.id;
	}

	public String getVersion() {
		return this.ver;
	}

}
