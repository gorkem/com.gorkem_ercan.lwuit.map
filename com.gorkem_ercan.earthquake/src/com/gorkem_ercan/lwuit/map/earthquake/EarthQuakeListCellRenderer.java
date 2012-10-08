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
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Border;

public class EarthQuakeListCellRenderer extends Container implements ListCellRenderer {

	private Earthquake quake;
	private Label magnitudeLbl;
	private Label detailLbl;
	private Label dateLbl;
	
	public EarthQuakeListCellRenderer() {
		magnitudeLbl = new Label();
		detailLbl = new Label();
		dateLbl = new Label();
		
		setLayout(new BorderLayout());
		getStyle().setFgColor(0xffffff);
		getStyle().setBorder(Border.createRoundBorder(10, 10));
		magnitudeLbl.getStyle().setFont(
				Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,
						Font.SIZE_LARGE));
		addComponent(BorderLayout.WEST, magnitudeLbl);
		
		Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		detailLbl.getStyle().setBgTransparency(0);
		detailLbl.getStyle().setFont(
				Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
						Font.SIZE_MEDIUM));
		detailLbl.setTickerEnabled(true);

		dateLbl.getStyle().setFont(
				Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
						Font.SIZE_SMALL));
		dateLbl.getStyle().setBgTransparency(0);

		cnt.addComponent(detailLbl);
		cnt.addComponent(dateLbl);
		addComponent(BorderLayout.CENTER, cnt);

	}
	public Component getListCellRendererComponent(List list, Object value,
			int index, boolean selected) {
		 quake = (Earthquake) value;
		 
		 
		 magnitudeLbl.getStyle().set3DText(true, true);
		 double magnitude = quake.getMagnitude();
		 int color = getMagnitudeColor(magnitude);
		 
		 magnitudeLbl.getStyle().setFgColor(color);
		magnitudeLbl.setText(Double.toString(magnitude));
		 
		 detailLbl.setText(quake.getDetail());
		 if (detailLbl.getPreferredW() > this.getWidth()) {
	            detailLbl.startTicker();
		 }

	     dateLbl.setText(TextUtil.formatDate(quake.getDate()));
	
		return this;
	}
	
	public static int getMagnitudeColor(double magnitude) {
		int color = 0xffffff;
		 if(magnitude > 7){
			 color = 0xDC143C;
		 }else
		 if (magnitude >5 ){
			 color = 0xED9121;
		 }
		 else if (magnitude > 2 ){
			 color = 0xEEC900;
		 }
		return color;
	}

	public Component getListFocusComponent(List list) {
		return null;		
	}
	
	

}
