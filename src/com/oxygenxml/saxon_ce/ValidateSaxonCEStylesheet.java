package com.oxygenxml.saxon_ce;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.saxonica.config.ProfessionalConfiguration;
import com.saxonica.config.ProfessionalTransformerFactory;

/**
 * Main class for reproducing Saxon-CE problems.
 *  
 * @author radu_pisoi
 */
public class ValidateSaxonCEStylesheet {
	
	private static String xslPath = "samples/saxon_ce/testValidateSaxonCE.xsl";
	
	public static void main(String[] args) throws TransformerException, InterruptedException {
		// Create transformer
		File xslFile = new File(xslPath);
		InputSource inputSource = new InputSource(xslFile.toURI().toString());
		Source source = new SAXSource(inputSource);
		
		ProfessionalConfiguration configuration = new ProfessionalConfiguration();
		SaxonCEExtensionsHelper.registerSaxonCEExtensions(configuration);
		TransformerFactory transformerFactory = new ProfessionalTransformerFactory(configuration);
		
		transformerFactory.newTransformer(source);
	}
}
