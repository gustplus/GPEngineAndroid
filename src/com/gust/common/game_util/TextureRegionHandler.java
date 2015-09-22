package com.gust.common.game_util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TextureRegionHandler extends DefaultHandler {
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("map")) {
			return;
		}
		if (localName.equals("tileset")) {
			return;
		}
		if (localName.equals("image")) {
			return;
		}
		if (localName.equals("layer")) {
			return;
		}
		if (localName.equals("data")) {
			return;
		}
		if (localName.equals("objectgroup")) {
			return;
		}
		if (localName.equals("object")) {
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("map")) {
			return;
		}
		if (localName.equals("tileset")) {
			return;
		}
		if (localName.equals("image")) {
			return;
		}
		if (localName.equals("layer")) {
			return;
		}
		if(localName.equals("data")){
			return;
		}
		if (localName.equals("objectgroup")) {
			return;
		}
		if (localName.equals("object")) {
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
	}
}
