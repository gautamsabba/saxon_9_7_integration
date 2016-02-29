package com.oxygenxml.debugger;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;

import com.saxonica.config.ProfessionalTransformerFactory;


public class SaxonDebugger {
	
//	private static String xslPath = "samples/testImportInclude1/main.xsl";
//    private static String xmlPath = "samples/personal.xml";
//    private static String outPath = "out/result.xml";
	
	private static String xslPath = "samples/debugger/test_a/main.xsl";
  private static String xmlPath = "samples/debugger/test_a/personal.xml";
//  private static String outPath = "out/result.xml";

	
	/**
	 * Create a new transformer for the given sylesheet.
	 * 
	 * @param xslSystemID The systemID of the stylesheet.
	 * @return The transformer for the given stylesheet.
	 * @throws TransformerConfigurationException
	 */
	static Transformer createTransformer(String xslSystemID) throws TransformerConfigurationException {
		// Create transformer factory and set the trace listener
		TransformerFactory transformerFactory = new ProfessionalTransformerFactory();
		transformerFactory.setAttribute(
                net.sf.saxon.lib.FeatureKeys.TRACE_LISTENER, new SaxonTraceListener());
        transformerFactory.setAttribute(
                net.sf.saxon.lib.FeatureKeys.COMPILE_WITH_TRACING, Boolean.TRUE);
            transformerFactory.setAttribute(
                net.sf.saxon.lib.FeatureKeys.LINE_NUMBERING, Boolean.valueOf(true));

		// Create transformer
		InputSource inputSource = new InputSource(xslSystemID);
		Source source = new SAXSource(inputSource);
		Transformer newTransformer = transformerFactory.newTransformer(source);
		
		return newTransformer;
	}
	
	public static void main(String[] args) throws TransformerException {
		File xslFile = new File(xslPath);		
		Transformer transformer = createTransformer(xslFile.toURI().toString());
		
		// Create XML  SAX source
		Source xmlSource = new SAXSource(new InputSource(new File(xmlPath).toURI().toString()));
		
		// Output stream result
//		Result outputTarget = new StreamResult(new File(outPath));
		Result outputTarget = createResult();
		
		// Run transformation
		System.out.println("Start transformation...");
		transformer.transform(xmlSource, outputTarget);
		System.out.println("Transformation done.");
	}
	
	private static SAXResult createResult() {
		return new DebuggerSAXResult();
	}
}
