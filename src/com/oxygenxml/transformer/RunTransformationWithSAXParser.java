package com.oxygenxml.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.expr.parser.XPathParser;
import net.sf.saxon.expr.parser.XPathParser.NestedLocation;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.Validation;
import net.sf.saxon.tree.AttributeLocation;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.saxonica.config.EnterpriseTransformerFactory;
import com.saxonica.config.ProfessionalTransformerFactory;

public class RunTransformationWithSAXParser {

	private static ErrorListener errorListener = new ErrorListener() {
		@Override
		public void warning(TransformerException ex) throws TransformerException {
			System.out.println("Warning: " + ex + "\nLocation: " + ex.getLocator());
		}

	@Override
	public void fatalError(TransformerException ex) throws TransformerException {
		System.out.println("Error: " + ex.getMessage());
	}

		@Override
		public void error(TransformerException ex) throws TransformerException {
			System.out.println("Error: " + ex + "\nLocation: " + ex.getLocator());
		}
	};

	private static String xslPath = "samples/dita_xsd_based/copy_stylesheet.xsl";
	private static String xmlPath = "samples/dita_xsd_based/defAttr.xml";
	private static String outPath = "samples/dita_xsd_based/out.xml";

	public static void main(String[] args) throws TransformerException, InterruptedException, SAXNotRecognizedException, SAXNotSupportedException {
		// Create transformer
		File xslFile = new File(xslPath);
		InputSource inputSource = new InputSource(xslFile.toURI().toString());
		Source source = new SAXSource(inputSource);

		TransformerFactoryImpl transformerFactory = new EnterpriseTransformerFactory();
		transformerFactory.setErrorListener(errorListener);
		
		transformerFactory.getConfiguration().setConfigurationProperty(
				FeatureKeys.SCHEMA_VALIDATION_MODE,
				Validation.toString(Validation.STRICT));
		
		transformerFactory.setAttribute(net.sf.saxon.lib.FeatureKeys.XSLT_SCHEMA_AWARE, true);
		
		Transformer transformer = transformerFactory.newTransformer(source);
		transformer.setErrorListener(errorListener);

		// Create XML SAX source
		SAXSource xmlSource = new SAXSource(new InputSource(new File(xmlPath).toURI().toString()));
		SAXParser saxParser = new SAXParser();
		
	    //EXM-11081 Set this feature so that the default attributes from the schema are reported...
	    saxParser.setFeature("http://apache.org/xml/features/validation/schema", true);
		
		xmlSource.setXMLReader(saxParser);

		// Output stream result
		File outFile = new File(outPath);
		if (outFile.exists()) {
			outFile.delete();
		}
		Result outputTarget = new StreamResult(outFile);

		// Run transformation
		System.out.println("Run transformation...");
		try {
			transformer.transform(xmlSource, outputTarget);
			System.out.println("Transformation done.");
		} catch (Exception e) {
			System.out.println("Transformation failed: " + e);
			e.printStackTrace();
		}
	}
}
