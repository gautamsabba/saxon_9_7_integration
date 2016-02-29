package com.oxygenxml.debugger;

import javax.xml.transform.sax.SAXResult;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * SAX result used in XSLT debugger. 
 *  
 * @author radu_pisoi
 */
public class DebuggerSAXResult extends SAXResult {
	
	private static class DebuggerContentHandler implements ContentHandler, LexicalHandler {

		private Locator locator;

		@Override
		public void comment(char[] arg0, int arg1, int arg2)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endCDATA() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endDTD() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endEntity(String arg0) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startCDATA() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startDTD(String arg0, String arg1, String arg2)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startEntity(String arg0) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
//			System.out.println("-----\nStart element: " + localName);
//			System.out.println("Locator: " + locator);
//			System.out.println("SystemID: " + locator.getSystemId());
//			System.out.println("Line: " + locator.getLineNumber());
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public DebuggerSAXResult() {
		DebuggerContentHandler contentHandler = new DebuggerContentHandler();
		setHandler(contentHandler);
		setLexicalHandler(contentHandler);
	}
}
