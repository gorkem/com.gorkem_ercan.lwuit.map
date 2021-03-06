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

import java.util.Vector;

import com.nokia.maps.map.MapFactory;
import com.nokia.maps.map.MapObject;
import com.sun.lwuit.List;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;

/**
 * An abstract Model for adding {@link MapObject} items to a {@link Map}.
 * Since this class extends from {@link ListModel} it allows same model 
 * class to be used both for {@link List}s and {@link Map}.
 * 
 * @author Gorkem Ercan
 *
 */
public abstract class MapModel implements ListModel {
	
	private Vector selectionListeners;
	private int selectedIndex;
	private Vector dataListeners;
	private Map map;
	private MapModelSelectionComponent selectionComponent;
	protected volatile Object[] objects;
	private volatile MapObject[] mapObjects;

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
		if(selectionListeners == null ) return;
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
		if (obj == null)
			return;
		int idx = 0;

		if (objects == null) {
			mapObjects = new MapObject[1];
			objects = new Object[1];
			objects[0] = obj;
		} else {
			synchronized (mapObjects) {
				int newSize = objects.length + 1;
				Object[] newArray = new Object[newSize];
				MapObject[] newMapObjects = new MapObject[newSize];
				System.arraycopy(objects, 0, newArray, 0, objects.length);
				System.arraycopy(mapObjects, 0, newMapObjects, 0,
						mapObjects.length);
				idx = newArray.length - 1;
				newArray[idx] = obj;
				objects = newArray;
				mapObjects = newMapObjects;
			}
		}

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
		if (objects == null || index >= objects.length || objects.length < 0)
			return;
		synchronized (mapObjects) {
			Object[] newArray = new Object[objects.length - 1];
			System.arraycopy(objects, 0, newArray, 0, index);
			System.arraycopy(objects, index + 1, newArray, index,
					objects.length - index);
			objects = newArray;
			MapObject[] newMapObjects = new MapObject[objects.length - 1];
			System.arraycopy(mapObjects, 0, newMapObjects, 0, index);
			System.arraycopy(mapObjects, index + 1, newMapObjects, index,
					mapObjects.length - index);
			objects = newArray;
		}
		notifyDataChangedListeners(DataChangedListener.REMOVED, index);
	}
	
	public MapObject getMapObjectForItemAt( int index ) {
		if(mapObjects!= null && mapObjects[index] == null ){
			synchronized (mapObjects) {
				mapObjects[index] = createMapObjectForItemAt(index,
						map.getMapFactory());
			}
		}
		return mapObjects[index];
	}
	
	public int getIndexforMapObject(MapObject mapObject){
		if(mapObjects == null || mapObject == null )
			return -1;
		synchronized (mapObjects) {
			for (int i = 0; i < mapObjects.length; i++) {
				if (mapObjects[i] == mapObject)
					return i;
			}
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
