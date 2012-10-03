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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import com.nokia.maps.map.EventListener;
import com.nokia.maps.map.MapDisplay;
import com.nokia.maps.map.MapObject;
import com.nokia.maps.map.Point;
import com.sun.lwuit.Display;

/**
 * Internal control for selection events on map items.
 * 
 * @author Gorkem Ercan
 *
 */
class MapModelSelectionComponent extends MapComponentBase implements EventListener {
	
	private static final int CURSOR_SIZE = 6;
    private static final int CURSOR_SIZE_2_DELTA = 2;
	private static final int CURSOR_SIZE_2 = 10;
	
	private MapModel model;
	private MapObject startObj;// Object to hold the initial object we have started the selection with.
	private int cursorX;
	private int cursorY;
	
	private MapModelSelectionComponent(){
		this(null);
	}
	
	public MapModelSelectionComponent(MapModel aModel){
		super("lwuit.InteractionControl", VERSION);
		this.model = aModel;
	}
	
	public boolean keyPressed(int keyCode, int gameAction) {
		if(gameAction == Canvas.FIRE){
			return true;
		}
		return false;
	}

	public boolean keyReleased(int keyCode, int gameAction) {
		if( gameAction == Canvas.FIRE){ 
			selectObject(cursorX, cursorY);
			return true;
		}
		return false;
	}

	public boolean keyRepeated(int keyCode, int gameAction, int repeatCount) {
		return false;
	}

	public boolean pointerDragged(int x, int y) {
	    return false;
	}

	public boolean pointerPressed(int x, int y) {
		startObj = map.getObjectAt(new Point(x, y));
		return startObj != null;
	}

	public boolean pointerReleased(int x, int y) {
		return selectObject(x, y);
	}

	private boolean selectObject(int x, int y) {
		MapObject obj = map.getObjectAt(new Point(x, y));
		if(obj != null && obj == startObj){
			model.mapObjectSelected(obj);
			return true;
		}
		return false;
	}

	public void attach(MapDisplay map) {
		super.attach(map);
	    this.cursorX = (map.getWidth() / 2 - 3);
	    this.cursorY = (map.getHeight() / 2 - 3);
	}

	public EventListener getEventListener() {
		return this;
	}

	public void paint(Graphics g) {
		if( !Display.getInstance().isTouchScreenDevice() ){
			g.setColor(0);
			g.drawRoundRect(this.cursorX, this.cursorY, CURSOR_SIZE, CURSOR_SIZE, CURSOR_SIZE, CURSOR_SIZE);
			g.drawRoundRect(this.cursorX - CURSOR_SIZE_2_DELTA, this.cursorY - CURSOR_SIZE_2_DELTA, 
	    		CURSOR_SIZE_2, CURSOR_SIZE_2, CURSOR_SIZE_2, CURSOR_SIZE_2);
		}
	}
	
}
