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

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import com.sun.lwuit.Display;

public class EarthQuakeMidlet extends MIDlet {

	private  EarthQuakePresenter presenter;

	public EarthQuakeMidlet() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		System.out.println("Starting EarthQuakeMidlet");
		System.getProperty("com.nokia.targetdebug");
		Display.init(this);
		if(presenter == null){
			presenter = new EarthQuakePresenter();
		}
		presenter.start();

	}

}
