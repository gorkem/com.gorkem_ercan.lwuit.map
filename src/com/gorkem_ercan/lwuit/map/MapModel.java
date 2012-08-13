package com.gorkem_ercan.lwuit.map;

import java.util.Vector;

import com.nokia.maps.map.MapFactory;
import com.nokia.maps.map.MapObject;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;

public abstract class MapModel implements ListModel {
	
	private Vector selectionListeners;
	private int selectedIndex;
	private Vector dataListeners;
	private Map map;
	private MapModelSelectionComponent selectionComponent;
	protected Object[] objects;
	private MapObject[] mapObjects;

	protected abstract MapObject createMapObjectForItemAt(int index, MapFactory factory );
	


	public void addSelectionListener(SelectionListener listener) {
		if(selectionListeners == null ){
			addSelectionComponent();
			selectionListeners = new Vector(3);			
		}
		selectionListeners.addElement(listener);
	}



	private void addSelectionComponent() {
		if(map != null ){
			selectionComponent = new MapModelSelectionComponent(this);
			map.getMapDisplay().addMapComponent(selectionComponent);
		}
	}

	public void removeSelectionListener(SelectionListener listener) {
		if(selectionListeners == null || listener == null ) return;
		selectionListeners.removeElement(listener);
	}

	public void setSelectedIndex(int index) {
		int oldIndex = selectedIndex;
		selectedIndex = index;
		for (int i = 0; i < selectionListeners.size(); i++) {
			SelectionListener l = (SelectionListener)selectionListeners.elementAt(i);
			l.selectionChanged(oldIndex, selectedIndex);
		}
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	void attach(Map map) {
		this.map = map;
		if (selectionListeners != null ){
			addSelectionComponent();
		}
	}
	
	void detach(Map map) {
		this.map.getMapDisplay().removeMapComponent(selectionComponent);
		this.map = null;
	}

	public void addDataChangedListener(DataChangedListener listener) {
		if(dataListeners == null ){
			dataListeners = new Vector(3);
		}
		dataListeners.addElement(listener);
	}

	public void removeDataChangedListener(DataChangedListener listener) {
		if(dataListeners == null || listener == null )return;
		dataListeners.removeElement(listener);
	}

	protected void notifyDataChangedListeners(int type, int index) {
		if(dataListeners == null )return;
		for (int i = 0; i < dataListeners.size(); i++) {
			DataChangedListener l =(DataChangedListener)dataListeners.elementAt(i);
			l.dataChanged(type, index);
		}
	}
	public void addItem(Object obj) {
		if(obj == null ) return;
		if(objects == null){
			mapObjects = new MapObject[1];
			objects= new Object[1];
			objects[0] = obj;
			notifyDataChangedListeners(DataChangedListener.ADDED, 0);
			return;
		}
		int newSize = objects.length+1;
		Object[] newArray = new Object[newSize];
		MapObject[] newMapObjects = new MapObject[newSize];
		System.arraycopy(objects, 0, newArray, 0, objects.length);
		System.arraycopy(mapObjects, 0, newMapObjects, 0, mapObjects.length);
		int idx = newArray.length-1;
		newArray[idx] = obj;
		objects = newArray;
		mapObjects = newMapObjects;
		notifyDataChangedListeners(DataChangedListener.ADDED, idx);
	}
	public Object getItemAt(int index) {
		if(index < 0 
				|| index>=objects.length){
			return null;
		}
		return objects[index];
	}
	public int getSize() {
		if(objects==null )return 0;
		return objects.length;
	}
	public void removeItem(int index) {
		if (objects == null || 
				index >= objects.length ||
				objects.length < 0 
				) return; 
		Object[] newArray = new Object[objects.length-1];
		System.arraycopy(objects, 0, newArray, 0, index);
		System.arraycopy(objects, index+1, newArray, index, objects.length-index);
		objects = newArray;
		MapObject[] newMapObjects = new MapObject[objects.length-1];
		System.arraycopy(mapObjects, 0, newMapObjects, 0, index);
		System.arraycopy(mapObjects, index+1, newMapObjects, index, mapObjects.length-index);
		objects = newArray;
		notifyDataChangedListeners(DataChangedListener.REMOVED, index);
	}
	
	public MapObject getMapObjectForItemAt( int index) {
		if(mapObjects!= null && mapObjects[index] == null ){
			mapObjects[index] = createMapObjectForItemAt(index, map.getMapFactory() );
		}
		return mapObjects[index];
	}
	public int getIndexforMapObject(MapObject mapObject){
		if(mapObjects == null || mapObject == null )
			return -1;
		for (int i = 0; i < mapObjects.length; i++) {
			if(mapObjects[i] == mapObject )
				return i;
		}
		return -1;
	}



	void mapObjectSelected(MapObject obj) {
		int idx = getIndexforMapObject(obj);
		if(idx > -1){
			setSelectedIndex(idx);
		}
		
	}
	
}
