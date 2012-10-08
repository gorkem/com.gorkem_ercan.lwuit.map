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

import java.io.IOException;

import javax.microedition.lcdui.Image;

import com.nokia.maps.map.MapFactory;
import com.nokia.maps.map.MapObject;
import com.gorkem_ercan.lwuit.map.earthquake.model.Earthquake;
import com.gorkem_ercan.lwuit.map.MapModel;;

public abstract class BaseEarthQuakeListModel extends MapModel {

	private Image icon;
	
	public BaseEarthQuakeListModel() {
		refreshEarthquakes();
	}

	protected abstract void refreshEarthquakes(); 
	
	protected MapObject createMapObjectForItemAt(int index, MapFactory factory) {
		Earthquake q = (Earthquake)getItemAt(index);
		if(icon == null){
			try {
				icon = Image.createImage("/qicon.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return factory.createMapMarker(q.getCoordinate(), icon);
		
	}


}