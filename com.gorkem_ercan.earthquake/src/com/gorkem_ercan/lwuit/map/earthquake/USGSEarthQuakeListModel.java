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
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nokia.maps.common.GeoCoordinate;
import com.gorkem_ercan.lwuit.map.earthquake.model.Earthquake;

public class USGSEarthQuakeListModel extends BaseEarthQuakeListModel {
	
	protected void refreshEarthquakes() {
		final String url = "http://earthquake.usgs.gov/earthquakes/catalogs/1day-M2.5.xml";
		try {
			HttpConnection con = (HttpConnection) Connector.open(url);
			int rc = con.getResponseCode();
			if (rc == HttpConnection.HTTP_OK) {
				InputStream is = con.openInputStream();
				SAXParser parser = SAXParserFactory.newInstance()
						.newSAXParser();
				parser.parse(is, new DefaultHandler() {
					private Earthquake currentQuake;
					private String currentTag;

					public void startElement(String uri, String localName,
							String qName, Attributes attributes)
							throws SAXException {
						currentTag = qName;
						if (qName.equals("link") && currentQuake != null) {
							currentQuake.setLink(attributes.getValue("href"));
						} else if (qName.equals("entry")) {
							currentQuake = new Earthquake();
						}
					}

					public void endElement(String uri, String localName,
							String qName) throws SAXException {

						if (qName.equals("entry")) {
							addItem(currentQuake);
							currentQuake = null;
						}
						currentTag = null;

					}

					public void characters(char[] ch, int start, int length)
							throws SAXException {
						if (currentQuake == null)
							return;
						if ("title".equals(currentTag)) {
							String details = String.valueOf(ch, start, length);
							int magEnd = details.indexOf(",");
							currentQuake.setDetail(details);
							currentQuake.setMagnitude(Double
									.parseDouble(details.substring(1, magEnd)));
						} else if ("georss:point".equals(currentTag)) {
							String georss = String.valueOf(ch, start, length);
							int split = georss.indexOf(" ");
							GeoCoordinate coord = new GeoCoordinate(
									Double.parseDouble(georss.substring(0,
											split)),
									Double.parseDouble(georss.substring(split)),
									0);
							currentQuake.setCoordinate(coord);

						} else if ("updated".equals(currentTag)) {
							// 2011-05-12T12:13:06Z
							Calendar calendar = Calendar.getInstance(TimeZone
									.getTimeZone("GMT"));
							int year = Integer.parseInt(String.valueOf(ch,
									start, 4));
							int month = Integer.parseInt(String.valueOf(ch,
									start + 5, 2));
							int day = Integer.parseInt(String.valueOf(ch,
									start + 8, 2));
							int hour = Integer.parseInt(String.valueOf(ch,
									start + 11, 2));
							int mins = Integer.parseInt(String.valueOf(ch,
									start + 14, 2));
							calendar.set(Calendar.YEAR, year);
							calendar.set(Calendar.MONTH, month - 1);
							calendar.set(Calendar.DAY_OF_MONTH, day);
							calendar.set(Calendar.HOUR_OF_DAY, hour);
							calendar.set(Calendar.MINUTE, mins);
							currentQuake.setDate(calendar.getTime());
						}

					}

				});

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
