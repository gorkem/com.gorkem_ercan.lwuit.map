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

import com.gorkem_ercan.lwuit.map.earthquake.model.Earthquake;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.table.TableLayout;
import com.sun.lwuit.table.TableLayout.Constraint;

public class EarthQuakeDetailDialog extends Dialog {
	
	
	private Label locationValue;
	private Label magnitudeValue;
	private Label dateValue;
	
	public EarthQuakeDetailDialog() {
		super("Details");
		locationValue = new Label();
		magnitudeValue = new Label();
		dateValue = new Label();
		TableLayout layout	= new TableLayout(2, 2);
		
		this.getContentPane().setLayout(layout);
		Constraint mc = layout.createConstraint(0,0);
		mc.setWidthPercentage(25);
		magnitudeValue.getStyle().set3DText(true, true);
		magnitudeValue.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
				Font.SIZE_LARGE));
		this.getContentPane().addComponent(mc,magnitudeValue);
		Constraint dc = layout.createConstraint(0, 1);
		dc.setWidthPercentage(75);
		this.getContentPane().addComponent(dc,dateValue);
		Constraint lc = layout.createConstraint(1, 0);
		lc.setHorizontalSpan(2);
		locationValue.setTickerEnabled(true);
		this.getContentPane().addComponent(lc,locationValue);
		
		
		
	}
	public void updateEarthquake(Earthquake earthquake) {
		locationValue.setText(earthquake.getDetail());
		magnitudeValue.setText(Double.toString(earthquake.getMagnitude()));
		magnitudeValue.getStyle().setFgColor(EarthQuakeListCellRenderer.getMagnitudeColor(earthquake.getMagnitude()));
		dateValue.setText(TextUtil.formatDate(earthquake.getDate()));
		
	}

	protected void onShow() {
		locationValue.startTicker();
	}
	
	
}
