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
