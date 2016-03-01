package com.oxygenxml.xpath;

import java.net.URISyntaxException;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import net.sf.saxon.event.Builder;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.Validation;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.trans.XPathException;

public class XpathCompiler {

	public static void compileQueryWithS9API(String userExpression)
			throws XPathException, TransformerConfigurationException, net.sf.saxon.trans.XPathException,
			URISyntaxException, SaxonApiException {

		// Create XPath processor
		Processor proc = new Processor(true);
		proc.setConfigurationProperty(FeatureKeys.NAME_POOL, proc.getUnderlyingConfiguration().getNamePool());
		proc.setConfigurationProperty(FeatureKeys.LINE_NUMBERING, Boolean.TRUE);
		proc.setConfigurationProperty(FeatureKeys.TREE_MODEL, Builder.LINKED_TREE);

		proc.setConfigurationProperty(FeatureKeys.SCHEMA_VALIDATION, Validation.LAX);

		XPathCompiler comp = proc.newXPathCompiler();
		comp.setSchemaAware(true);

		comp.compile(userExpression);

	}

	public static void main(String[] args) {
		try {
			compileQueryWithS9API("//personal&");

		} catch (net.sf.saxon.trans.XPathException e) {
			SourceLocator locator = e.getLocator();
			System.out.println("Line: " + locator.getLineNumber());
			System.out.println("Column: " + locator.getColumnNumber());
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaxonApiException e) {
			Throwable cause = e.getCause();
			if (cause instanceof XPathException) {
				SourceLocator locator = ((TransformerException) cause).getLocator();
				System.out.println("Line: " + locator.getLineNumber());
				System.out.println("Column: " + locator.getColumnNumber());
			}
			e.printStackTrace();
		}
	}
}
