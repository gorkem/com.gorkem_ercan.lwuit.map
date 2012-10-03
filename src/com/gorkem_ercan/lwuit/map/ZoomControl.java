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

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.nokia.lwuit.GestureHandler;
import com.nokia.maps.map.EventListener;
import com.nokia.maps.map.MapDisplay;
import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;

/**
 * Internal control for zooming on the map. 
 * Supports touch, pinch and keys.
 * 
 * @author Gorkem Ercan
 *
 */
class ZoomControl extends MapComponentBase implements EventListener {

	
	private final class PinchZoomHandler extends GestureHandler{

		public void gestureAction(GestureEvent e) {
			if ( e.getType() == GestureInteractiveZone.GESTURE_PINCH ) {
				
				double zoom = map.getZoomLevel() + (e.getPinchDistanceChange()/10);
				if (zoom < map.getMinZoomLevel()){
					zoom = map.getMinZoomLevel();
				}else 
				if(zoom > map.getMaxZoomLevel() ){
					zoom = map.getMaxZoomLevel();
				}
				map.setZoomLevel(zoom, 0, 0);
			}	
		}
	}
	
	class ImageButton
	  {
	    Image imageNormal;
	    Image imageHighlight;
	    Image image;
	    int top;
	
	    public ImageButton(Image imageNormal, Image imageHighlight)
	    {
	      this.imageNormal = imageNormal;
	      this.imageHighlight = imageHighlight;
	      this.image = imageNormal;
	    }
	
	    public void paint(Graphics g)
	    {
	      g.drawImage(this.image, ZoomControl.this.right, this.top, Graphics.TOP | Graphics.RIGHT);
	    }
	
	    public boolean contains(int xCoord, int yCoord)
	    {
	      boolean contains = false;
	
	      if ((xCoord < ZoomControl.this.right) && (xCoord > ZoomControl.this.right - this.image.getWidth()) && (yCoord > this.top) && (yCoord < this.top + this.image.getHeight()))
	      {
	        contains = true;
	      }
	      return contains;
	    }
	
	    public boolean pressed(int x, int y)
	    {
	      if (contains(x, y)) {
	        this.image = this.imageHighlight;
	        return true;
	      }
	      this.image = this.imageNormal;
	      return false;
	    }
	
	    public boolean released(int x, int y)
	    {
	      boolean contains = (this.image == this.imageHighlight) && (contains(x, y));
	
	      this.image = this.imageNormal;
	      return contains;
	    }
	
	    public boolean isPressed()
	    {
	      return this.image == this.imageHighlight;
	    }
	  }

	private ImageButton minus;
	private ImageButton plus;
	private int buttonDiff;
	private int right;

	public ZoomControl() {
		super("lwuit.zoom", VERSION);
		try {
			this.plus = new ImageButton(Image.createImage("/zp_32_n.png"), Image.createImage("/zp_32_r.png"));
			this.minus = new ImageButton(Image.createImage("/zm_32_n.png"), Image.createImage("/zm_32_r.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void attach(MapDisplay map) {
		super.attach(map);

		this.buttonDiff = this.plus.image.getHeight();
		this.right = (map.getWidth() - 4 - (plus.image.getWidth() / 2));
		int yCenter = map.getHeight() / 2;

		this.plus.top = (yCenter - this.buttonDiff / 2 - this.plus.image
				.getHeight());
		this.minus.top = (yCenter + this.buttonDiff / 2);
		GestureHandler.setGlobalGestureHandler(new PinchZoomHandler());
	}

	public void detach(MapDisplay map) {
		super.detach(map);
		GestureHandler.setGlobalGestureHandler(null);
	}
	
	public boolean keyPressed(int key, int gameAction) {
		return (key== Canvas.KEY_STAR || key == Canvas.KEY_POUND);
	}

	public boolean keyReleased(int key, int gameAction) {
		return adjustZoom(key, 1);
	}

	public boolean keyRepeated(int key, int gameAction, int repeat) {
		return adjustZoom(key, repeat);
	}

	public boolean pointerDragged(int x, int y) {
		  boolean wasPressed = (this.plus.isPressed()) || (this.minus.isPressed());

		    if ((this.plus.isPressed()) && (!this.plus.contains(x, y)))
		      this.plus.released(x, y);
		    else if ((this.minus.isPressed()) && (!this.minus.contains(x, y))) {
		      this.minus.released(x, y);
		    }
		    return wasPressed;
	}

	public boolean pointerPressed(int x, int y) {
	    boolean consumed = false;

	    consumed = this.plus.pressed(x, y);
	    if (!consumed) {
	      consumed = this.minus.pressed(x, y);
	    }
	    return consumed;
	}

	public boolean pointerReleased(int x, int y) {
	    boolean consumed = false;

	    boolean wasPressed = (this.plus.isPressed()) || (this.minus.isPressed());
	    int z = 1;

	    consumed = this.plus.released(x, y);
	    if (!consumed) {
	      consumed = this.minus.released(x, y);
	      z = -1;
	    }

	    if (consumed) {
	      int zoom = this.map.getZoomLevel() + z;

	      if ((zoom >= this.map.getMinZoomLevel()) && (zoom <= this.map.getMaxZoomLevel())) {
	        this.map.setZoomLevel(zoom, 0, 0);
	      }
	    }
	    return (consumed) || (wasPressed);
	}


	public EventListener getEventListener() {
		return this;
	}

	public void mapUpdated(boolean arg0) {
	}

	public void paint(Graphics g) {
		this.plus.paint(g);
		this.minus.paint(g);
	}
	
	private boolean adjustZoom(int keyCode, int factor){
		int currentZoom = map.getZoomLevel();
		if(keyCode == Canvas.KEY_POUND ){
			map.setZoomLevel(Math.max(currentZoom, currentZoom-factor), 0, 0);
			return true;
		}
		if(keyCode == Canvas.KEY_STAR){
			map.setZoomLevel(Math.min(currentZoom, currentZoom + factor ), 0, 0);
			return true;
		}
		return false;
	}

}
