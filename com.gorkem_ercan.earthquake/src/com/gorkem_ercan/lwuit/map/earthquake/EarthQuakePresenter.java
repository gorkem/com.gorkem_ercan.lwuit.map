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

import com.gorkem_ercan.lwuit.map.Map;
import com.gorkem_ercan.lwuit.map.MapModel;
import com.gorkem_ercan.lwuit.map.earthquake.model.Earthquake;
import com.nokia.maps.map.MapDisplay;
import com.nokia.maps.map.MapObject;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BorderLayout;

public class EarthQuakePresenter implements ActionListener, SelectionListener {

	private static final int EARTHQUAKE_DIALOG_SLIDE_TIME = 600;
	private MapModel quakeListModel;
	private List earthQuakeList;
	private Map earthQuakeMap;
	private Form mapForm;
	private Form listForm;
	private Command backFromMap;
	private Command exitCommand;

	public void start() {
		quakeListModel = new USGSEarthQuakeListModel();
		try {
			exitCommand = new Command("Exit", Image.createImage("/exit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		showListForm();
		
	}

	private List getEarthQuakeList() {
		if (earthQuakeList == null) {
			earthQuakeList = new List(quakeListModel);
			earthQuakeList.setRenderer(new EarthQuakeListCellRenderer());
			earthQuakeList.addActionListener(this);

		}
		return earthQuakeList;
	}

	private Map getEarthQuakeMap() {
		if (earthQuakeMap == null) {
			earthQuakeMap = new Map("o5x6vKLjiiKpiSttH_4R",
					"tyizajsTF21Bgx82XiWaZQ");
			earthQuakeMap.setModel(quakeListModel);
		}
		return earthQuakeMap;
	}

	private void showListForm() {
		if (listForm == null) {
			listForm = new Form();
			listForm.setLayout(new BorderLayout());
			listForm.setScrollable(false);
			
			listForm.setTransitionOutAnimator(CommonTransitions.createFade(400));
			listForm.addCommand(exitCommand);
			listForm.addCommandListener(this);
			listForm.addComponent(BorderLayout.CENTER, getEarthQuakeList());
			
			

		}
		quakeListModel.removeSelectionListener(this);
		listForm.show();
	}

	private void showMapForm() {
		if (mapForm == null) {
			mapForm = new Form();
			mapForm.setLayout(new BorderLayout());
			mapForm.addComponent(BorderLayout.CENTER, getEarthQuakeMap());
			mapForm.setTransitionOutAnimator(CommonTransitions.createFade(400));
			backFromMap = new Command("Back");
			mapForm.addCommand(backFromMap);
			mapForm.setBackCommand(backFromMap);
			mapForm.addCommandListener(this);

		}

		MapDisplay display = getEarthQuakeMap().getMapDisplay();
		MapObject selected = quakeListModel
				.getMapObjectForItemAt(quakeListModel.getSelectedIndex());
		display.setZoomLevel(9, 0, 0);
		display.setCenter(selected.getBoundingBox().getCenter());

		quakeListModel.addSelectionListener(this);
		mapForm.show();
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == earthQuakeList) {
			showMapForm();
		}
		if (evt.getSource() == backFromMap) {
			showListForm();
		}
		
        // Handle Commands
		if (evt.getSource() instanceof Command ){
			Command c = (Command) evt.getSource();
			if(c == exitCommand ){
				Display.getInstance().exitApplication();
			}
			if (c.getId() == 5) {
				showListForm();
			}	
		}
	}



	
	private EarthQuakeDetailDialog getMapQuakeDetailDialog() {

		EarthQuakeDetailDialog quakeDetailDialog = new EarthQuakeDetailDialog();

		quakeDetailDialog.setTransitionInAnimator(CommonTransitions
				.createSlide(CommonTransitions.SLIDE_VERTICAL, false,
						EARTHQUAKE_DIALOG_SLIDE_TIME));
		quakeDetailDialog.setTransitionOutAnimator(CommonTransitions
				.createSlide(CommonTransitions.SLIDE_VERTICAL, true,
						EARTHQUAKE_DIALOG_SLIDE_TIME));

		return quakeDetailDialog;
	}

	public void selectionChanged(int oldSelected, int newSelected) {
		Earthquake q = (Earthquake) quakeListModel.getItemAt(newSelected);
		if (q != null) {
			EarthQuakeDetailDialog dialog = getMapQuakeDetailDialog();
			dialog.updateEarthquake(q);
			dialog.setTimeout(5000);
			dialog.showPacked(BorderLayout.NORTH, true);
			
		}
	}

}
