package com.oxygenxml.transformer;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

import org.xml.sax.InputSource;

public class Streaming {

  private static String xsl = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
      "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n" + 
      "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" + 
      "    exclude-result-prefixes=\"xs\"\n" + 
      "    version=\"3.0\">\n"
      + "<xsl:mode streamable=\"true\"/>\n" + 
      "    <xsl:template match=\"*:person\"><xsl:value-of select=\"@id\"/></xsl:template>\n" + 
      "</xsl:stylesheet>";
  private static String xml = "<person id='test'/>";

  public static void main(String[] args) throws TransformerException {
    TransformerFactory factory = TransformerFactoryImpl.newInstance();
    Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(xsl)));
    
    InputSource inputSource = new InputSource(new StringReader(xml));
    SAXSource xmlSource = new SAXSource(inputSource);
    StringWriter writer = new StringWriter();
    transformer.transform(xmlSource, new StreamResult(writer));
    
    System.out.println(writer.toString());
  }
}
