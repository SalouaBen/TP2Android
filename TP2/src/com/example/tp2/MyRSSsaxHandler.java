package com.example.tp2;

import java.io.IOException;
import java.io.InputStream;


import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MyRSSsaxHandler extends DefaultHandler {
	private String url = null; // l'URL du flux RSS à parser
	// Ensemble de drapeau permettant de savoir où en est le parseur dans le flux XML
	private boolean inTitle = false; 
	private boolean inDescription = false; 
	private boolean inItem = false; 
	private boolean inDate = false;
	// L'image référencé par l'attribut url du tag <enclosure>
	private Bitmap image = null; 
	private String imageURL = null; 
	// Le titre, la description et la date extraits du flux RSS 
	private StringBuffer title = new StringBuffer();
	private StringBuffer description = new StringBuffer();
	private StringBuffer date = new StringBuffer();
	private int numItem = 0; // Le numéro de l'item à extraire du flux RSS
	private int numItemMax = -1; // Le nombre total d’items dans le flux RSS
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void processFeed() {
		try {
			numItem = 0; // A modifier pour visualiser un autre item
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(this);
			InputStream inputStream = new URL(url).openStream();
			reader.parse(new InputSource(inputStream));
			image = getBitmap(imageURL);
			numItemMax = numItem; 
		} catch (Exception e) {
			Log.e("smb116rssview","processFeed Exception "+e.getMessage());
		}
	}
	
	private Bitmap getBitmap(String imageURL) throws MalformedURLException, IOException {
		Bitmap image;
		InputStream inputstream = new URL(imageURL).openStream();
		image = BitmapFactory.decodeStream(inputstream);
		
		
		return null;
	}

	public void startElement (String uri, String localName, String qName, Attributes attributes) {
		if(qName.equalsIgnoreCase("item")) {
			inItem = true;
			
			if(qName.equalsIgnoreCase("title")) {
				inTitle = true;
			}
			if(qName.equalsIgnoreCase("description")) {
				inDescription = true;
			}
			if(qName.equalsIgnoreCase("item")) {
				inItem = true;
			}	
		}
	}
	
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if( inItem ) {
			if( inTitle) {
				title = new StringBuffer(new String(ch, start, length));
				inTitle = false;
			}
			
			if( inDescription ) {
				description = new StringBuffer(new String(ch, start, length));
				inDescription = false;
			}
			
			if( inDate ) {
				date = new StringBuffer(new String(ch, start, length));
				inDate = false;
			}

		}
	}
	
	public void endElement (String uri, String localName, String qName) {
		if( inItem ) {
			if( inTitle) {
				inTitle = false;
			}
			
			if( inDescription ) {
				inDescription = false;
			}
			 
			if( inDate ) {
				inDate = false;
			}
			
			inItem = false;

		}
	}
	
	
	
}
