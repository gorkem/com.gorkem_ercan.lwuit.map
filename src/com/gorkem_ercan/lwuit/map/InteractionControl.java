package com.gorkem_ercan.lwuit.map;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.nokia.maps.map.EventListener;
import com.nokia.maps.map.Point;

public class InteractionControl extends MapComponentBase implements EventListener {
	
	private Point pointDown = new Point(0,0);
	private boolean dragging;
	public InteractionControl() {
		super("lwuit.ineraction", VERSION);
	}
	
	public boolean keyPressed(int keyCode, int gameAction) {
		return doKeyPan(gameAction, -1);
	}

	public boolean keyReleased(int keyCode, int gameAction) {
		return doKeyPan(gameAction,0);
	}

	public boolean keyRepeated(int keyCode, int gameAction, int repeatCount) {
		return doKeyPan(gameAction, repeatCount);
	}

	public boolean pointerDragged(int x, int y) {
	    dragging = true;
	    Point p = new Point(-x, -y);
	    map.pan(pointDown,p);
	    pointDown = p;
	    return true;
	}

	public boolean pointerPressed(int x, int y) {
		pointDown.setX(-x);
		pointDown.setY(-y);
		return false;
	}

	public boolean pointerReleased(int x, int y) {
		if (dragging){
	      pointerDragged(x, y);
	      dragging = false;
	      return true;
	    }
	    return false;
	}

	public EventListener getEventListener() {
		return this;
	}

	public void mapUpdated(boolean zoomChanged) {

	}

	public void paint(Graphics arg0) {

	}
	
	 private synchronized boolean doKeyPan(int gameAction, int repeatCount)
	  {
	    boolean keyConsumed = false;
	    int PAN = 1;

	    PAN += repeatCount * 1;
	    int maxPan = this.map.getWidth() / 32;

	    PAN = PAN > maxPan ? maxPan : PAN;
	    Point start = new Point(0, 0);
	    Point end = null;

	    switch (gameAction) {
	    case Canvas.LEFT:
	      end = new Point(-PAN, 0);
	      keyConsumed = true;
	      break;
	    case Canvas.RIGHT:
	      end = new Point(PAN, 0);
	      keyConsumed = true;
	      break;
	    case Canvas.UP:
	      end = new Point(0, -PAN);
	      keyConsumed = true;
	      break;
	    case Canvas.DOWN:
	      end = new Point(0, PAN);
	      keyConsumed = true;
	    }
	    if ((keyConsumed) && (repeatCount >= 0)) {
	      this.map.pan(start, end);
	    }
	    return keyConsumed;
	  }

}
