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

import com.nokia.maps.common.ApplicationContext;
import com.nokia.maps.map.EventListener;
import com.nokia.maps.map.MapComponent;
import com.nokia.maps.map.MapDisplay;
import com.nokia.maps.map.MapFactory;
import com.nokia.maps.map.MapListener;
import com.nokia.maps.map.MapObject;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.geom.Dimension;


public class Map extends Component implements MapListener,DataChangedListener {
	
	private MapDisplay map;
	private MapFactory mapFactory;
	private MapModel model;
	
	public Map(String appId, String token) {
		ApplicationContext context = ApplicationContext.getInstance();
		context.setAppID(appId);
		context.setToken(token);
		setFocusable(true);
		setTactileTouch(false);
	}
	
	public MapFactory getMapFactory(){
	
		if(mapFactory == null ){
			mapFactory = MapFactory.createMapFactory(
					MapDisplay.MAP_RESOLUTION_AUTO,
		            this.getPreferredW(), 
		           this.getPreferredH());                
		}
		return mapFactory;
	}
	
	public MapDisplay getMapDisplay(){
		if(map==null){
			map = getMapFactory().createMapDisplay();
			map.addMapComponent(new InteractionControl());
			map.addMapComponent(new ZoomControl());
		}
		return map;
	}
	
	public void setModel(MapModel aModel){
		if( this.model == aModel ) return;
		if (this.model != null ){
			this.model.detach(this);
			this.model.removeDataChangedListener(this);
		}
		if(aModel != null ){
			this.model = aModel;
			this.model.attach(this);
			initFromModel();
			this.model.addDataChangedListener(this);
		}
	}
	
	protected void initComponent() {
		getMapDisplay().setMapListener(this);
	}

	private void initFromModel() {
		if( model == null  )
			return;
		for (int i = 0; i < model.getSize(); i++) {
			getMapDisplay().addMapObject(model.getMapObjectForItemAt(i));
		}		
	}

		
	public void paint(Graphics g) {
		javax.microedition.lcdui.Graphics lcduiGraphics = (javax.microedition.lcdui.Graphics)Display.getInstance().getImplementation().getNativeGraphics();
		getMapDisplay().paint(lcduiGraphics);
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			components[i].paint(lcduiGraphics);
		}
		
	}
	

	protected Dimension calcPreferredSize() {
		Display display = Display.getInstance();
		return new Dimension(display.getDisplayWidth(),
				display.getDisplayHeight());
	}
	
	public String getUIID() {
		return "nokia.MapComponent";
	}
	
	public void keyPressed(int keyCode) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.keyPressed(keyCode, Display.getInstance()
							.getGameAction(keyCode))) {
				repaint();
				break;
			}
		}
	}

	public void keyReleased(int keyCode) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.keyReleased(keyCode, Display.getInstance()
							.getGameAction(keyCode))) {
				repaint();
				break;
			}
		}
	}

	public void keyRepeated(int keyCode) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.keyRepeated(keyCode, Display.getInstance()
							.getGameAction(keyCode),1 )) {//TODO: handle key repeat with a scheduler
				repaint();
				break;
			}
		}
	}

	public void pointerPressed(int x, int y) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.pointerPressed(x, y)) {
				repaint();
				break;
			}
		}

	}

	public void pointerReleased(int x, int y) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.pointerReleased(x, y)) {
				repaint();
				break;
			}
		}
	}
	
	public void pointerDragged(int x, int y) {
		MapComponent[] components = getMapDisplay().getAllMapComponents();
		for (int i = 0; i < components.length; i++) {
			EventListener e = components[i].getEventListener();
			if (e != null
					&& e.pointerDragged(x, y)) {
				repaint();
				break;
			}
		}
	}

	public void onMapContentComplete() {
	
	}

	public void onMapContentUpdated() {
		repaint();
		
	}

	public void onMapUpdateError(String description, Throwable detail,
			boolean critical) {
		detail.printStackTrace();
	}

	public void dataChanged(int type, int index) {

		MapObject mapObject = this.model.getMapObjectForItemAt(index);
		
		switch (type) {
		case DataChangedListener.ADDED:
			getMapDisplay().addMapObject(mapObject);
			break;
		case DataChangedListener.REMOVED:
			getMapDisplay().removeMapObject(mapObject);
			break;
		case DataChangedListener.CHANGED: // MapModel does not support CHANGED
		default:
			break;
		}

	}
	


	
}
