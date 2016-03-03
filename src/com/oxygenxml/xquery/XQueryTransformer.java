package com.oxygenxml.xquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

import com.saxonica.config.EnterpriseConfiguration;

/**
 * XQuery executor sample class.
 */
public class XQueryTransformer {
	
	private Configuration config;
	private StaticQueryContext staticQueryContext;
	private XQueryExpression queryExpression;

	/**
	 * Error listener.
	 */
	private ErrorListener errorListener = new ErrorListener() {
		@Override
		public void warning(TransformerException exception)
				throws TransformerException {
			System.out.println("Warning: " + exception);
		}

		@Override
		public void fatalError(TransformerException exception)
				throws TransformerException {
			System.out.println("F error: " + exception);
			SourceLocator locator = exception.getLocator();
			System.out.println("Line: " + locator.getLineNumber());
		}

		@Override
		public void error(TransformerException exception)
				throws TransformerException {
			System.out.println("Error: " + exception);
		}
	};

	/**
	 * Constructor.
	 */
	public XQueryTransformer(File xqueryFile) throws XPathException,
			UnsupportedEncodingException, FileNotFoundException, IOException {
		config = new EnterpriseConfiguration();

		// Prepare for debugger
		config.setErrorListener(errorListener);
		config.setLineNumbering(true);

//		config.setConfigurationProperty(FeatureKeys.XSD_VERSION, "1.1");

		staticQueryContext = config.newStaticQueryContext();
		staticQueryContext.setBaseURI(xqueryFile.toURI().toString());

		queryExpression = staticQueryContext
				.compileQuery(new InputStreamReader(new FileInputStream(
						xqueryFile), "UTF8"));
	}

	/**
	 * Runs an XQuery.
	 */
	void runXQuery(Source source, Result result) throws TransformerException {
		DynamicQueryContext dynamicQueryContext = new DynamicQueryContext(config);
		if (source != null) {
			DocumentInfo document = staticQueryContext.getConfiguration()
					.buildDocument(source);
			dynamicQueryContext.setContextItem(document);
		}
		queryExpression.run(dynamicQueryContext, result, new Properties());
	}

	public static void main(String[] args) throws Exception {
		try {
			XQueryTransformer xQueryDebugger = new XQueryTransformer(new File(
					"samples/xquery/validation/test.xquery"));
			
			System.out.println("Run query...");
			xQueryDebugger.runXQuery(null, new StreamResult(new File("out/result.xml")));
			System.out.println("Done.");			
		} catch (Exception e) {
			System.out.println("Transformation failed: " + e);
			e.printStackTrace();
		}
	}
}
