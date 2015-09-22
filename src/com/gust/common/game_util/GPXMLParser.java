package com.gust.common.game_util;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class GPXMLParser {

	public void parse(InputStream in, DefaultHandler handler)
	{
		// TODO Auto-generated constructor stub
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(in));
		}
		catch (Exception e) {

		}
	}
}
