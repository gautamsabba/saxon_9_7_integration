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

import org.xml.sax.InputSource;

import com.saxonica.config.EnterpriseTransformerFactory;
import com.saxonica.config.ProfessionalTransformerFactory;

public class RunTransformation {

	private static ErrorListener errorListener = new ErrorListener() {
		@Override
		public void warning(TransformerException ex) throws TransformerException {
			System.out.println("Warning: " + ex + "\nLocation: " + ex.getLocator());
		}

	@Override
	public void fatalError(TransformerException ex) throws TransformerException {
//		XPathParser.NestedLocation nestedLoc = (NestedLocation) ex.getLocator();
		
		System.out.println("Error: " + ex.getMessage());		
	}

		@Override
		public void error(TransformerException ex) throws TransformerException {
			System.out.println("Error: " + ex + "\nLocation: " + ex.getLocator());
		}
	};

//	private static String xslPath = "samples/transformation/BookStore.xsl";
//	private static String xmlPath = "samples/transformation/BookStore.xml";
	
	private static String xslPath = "samples/validation/bookstore/BookStore.xsl";
	private static String xmlPath = "samples/validation/bookstore/BookStore.xml";
	private static String outPath = "samples/validation/bookstore/out.xml";

	public static void main(String[] args) throws TransformerException, InterruptedException {
		// Create transformer
		File xslFile = new File(xslPath);
		InputSource inputSource = new InputSource(xslFile.toURI().toString());
		Source source = new SAXSource(inputSource);

		TransformerFactoryImpl transformerFactory = new EnterpriseTransformerFactory();
		transformerFactory.setErrorListener(errorListener);
		transformerFactory.getConfiguration().setConfigurationProperty(FeatureKeys.SCHEMA_VALIDATION_MODE,
				Validation.toString(Validation.STRICT));
		Transformer transformer = transformerFactory.newTransformer(source);
		transformer.setErrorListener(errorListener);

		// Create XML SAX source
		Source xmlSource = new StreamSource(new File(xmlPath));

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
