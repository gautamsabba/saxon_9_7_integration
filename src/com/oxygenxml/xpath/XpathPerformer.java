package com.oxygenxml.xpath;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathException;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.ElementOverNodeInfo;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.event.Builder;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.Validation;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.tree.wrapper.AbstractNodeWrapper;

public class XpathPerformer {
	/**
	 * Runs an XPath 2.0 query using Saxon9 API.
	 *
	 * @param userExpression
	 *            The XPath expression provided by the user. This expression
	 *            will be evaluated in the context given by the second
	 *            expression.
	 * @param xpathType
	 *            The xPath expression type.
	 * @param contextExpression
	 *            Gives the context in which the userExpression will be
	 *            evaluated.
	 * @param domSource
	 *            The document source to perform XPath on.
	 * @param saxSource
	 *            The SAX source to perform XPath on.
	 * @param reader
	 *            The reader. This should be <code>null</code> when one of the
	 *            source parameter is specified.
	 * @param systemID
	 *            The system ID. This should be null when one of the source
	 *            parameter is specified.
	 * @param proxyNamespaceMappings
	 *            An array of proxy followed by namespace representing the
	 *            proxy-namespace mappings. It can be <code>null</code>.
	 * @param defaultElementNamespace
	 *            If the elements are not prefixed by any namespace, then if
	 *            this will be their namespace. If it is <code>null</code>, the
	 *            default XPath mechanism is used, considering the element-tests
	 *            to be from no namespace.
	 * @param validationLevel
	 *            One of net.sf.saxon.lib.Validation constants. Only used if
	 *            <code>isSchemaAware</code> is <code>true</code>.
	 * @param compileOnly
	 *            If <code>true</code> only a compile operation will take place.
	 * @param executionStopper
	 *            The execution stopper.
	 * @param extensionFunctions
	 *            Saxon's extension functions, if any.
	 * 
	 * @return The list of nodes selected by the expression, or an XObject. In
	 *         case the source is a DOM source, the nodes must be (same
	 *         referece) from the DOM tree.
	 * 
	 * @exception XPathException
	 *                When the XPath processing failed.
	 * @throws TransformerConfigurationException 
	 * @throws net.sf.saxon.trans.XPathException 
	 * @throws SaxonApiException 
	 * @throws URISyntaxException 
	 */
	public static void runQueryWithS9API(
			String userExpression, 
			String xmlInstanceSystemID, 
			String defaultElementNamespace) throws XPathException, TransformerConfigurationException, net.sf.saxon.trans.XPathException, URISyntaxException, SaxonApiException {

		Processor proc = getSaxonXPathProcessor();
		Source source = new StreamSource(xmlInstanceSystemID);

		runQueryWithS9API(source, proc, userExpression, defaultElementNamespace);		
	}

	/**
	 * Get the Saxon processor with the specified configuration.
	 * 
	 * @param systemId The system ID of the current document.
	 * @return The Saxon XPath processor.
	 * @throws TransformerConfigurationException
	 */
	private static Processor getSaxonXPathProcessor() throws TransformerConfigurationException {


		// Create XPath processor
		Processor proc = new Processor(true);
		
		
		System.out.println("Schema val? " + 
				proc.getUnderlyingConfiguration().isLicensedFeature(Configuration.LicenseFeature.PROFESSIONAL_EDITION));

//			SaxonEEUtilFacade.getSaxonEEUtil().supplySaxonLicenseAndSchemaResolver(proc.getUnderlyingConfiguration(),
//					false);			

		proc.setConfigurationProperty(FeatureKeys.NAME_POOL, proc.getUnderlyingConfiguration().getNamePool());
//		proc.getUnderlyingConfiguration().setErrorListener(errorListener);
//		proc.setConfigurationProperty(FeatureKeys.SOURCE_PARSER_CLASS, CatalogEnabledXMLReader.class.getName());
		proc.setConfigurationProperty(FeatureKeys.LINE_NUMBERING, Boolean.TRUE);
		proc.setConfigurationProperty(FeatureKeys.TREE_MODEL, Builder.LINKED_TREE);
		
		proc.setConfigurationProperty(FeatureKeys.SCHEMA_VALIDATION, Validation.LAX);
		
//		proc.setConfigurationProperty(FeatureKeys.TRACE_LISTENER, new TraceListener() {
//			@Override
//			public void startCurrentItem(Item currentItem) {
//				// Not needed
//			}
//
//			@Override
//			public void open(Controller controller) {
//				// EXM-13054: clear the Trace messages before transforming.
//				IDEAccess.getInstance().updateMessagesResults(
//						EXMLResourceBoundle.getResourceBundleInstance().getString(Tags.TRACE_MESSAGES), (List) null,
//						DPIMessagesCategory.TRANSFORMATION);
//			}
//
//			@Override
//			public void leave(InstructionInfo instruction) {
//				// Not needed
//			}
//
//			@Override
//			public void enter(InstructionInfo instruction, net.sf.saxon.expr.XPathContext context) {
//				// EXM-13054: The case for user trace calls.
//				if (instruction instanceof InstructionDetails) {
//					SaxonTransformerUtil.addTraceMessage((InstructionDetails) instruction);
//				}
//			}
//
//			@Override
//			public void endCurrentItem(Item currentItem) {
//				// Not needed
//			}
//
//			@Override
//			public void close() {
//				// Not needed
//			}
//
//			@Override
//			public void setOutputDestination(net.sf.saxon.lib.Logger stream) {
//				// Not needed.
//			}
//		});

		return proc;
	}

	/**
	 * Runs an XPath 2.0 query using Saxon9 API.
	 *
	 * @param source
	 *            The source on which the XPath will be executed.
	 * @param proc
	 *            The Saxon processor.
	 * @param userExpression
	 *            The XPath expression provided by the user. This expression
	 *            will be evaluated in the context given by the second
	 *            expression.
	 * @param xpathType
	 *            The xPath expression type.
	 * @param contextExpression
	 *            Gives the context in which the userExpression will be
	 *            evaluated.
	 * @param proxyNamespaceMappings
	 *            An array of proxy followed by namespace representing the
	 *            proxy-namespace mappings. It can be <code>null</code>.
	 * @param defaultElementNamespace
	 *            If the elements are not prefixed by any namespace, then if
	 *            this will be their namespace. If it is <code>null</code>, the
	 *            default XPath mechanism is used, considering the element-tests
	 *            to be from no namespace.
	 * @param compileOnly
	 *            If <code>true</code> only a compile operation will take place.
	 * @param executionStopper
	 *            The execution stopper.
	 * @param extensionFunctions
	 *            Saxon's extension functions, if any.
	 * 
	 * @return The list of nodes selected by the expression, or an XObject. In
	 *         case the source is a DOM source, the nodes must be (same
	 *         referece) from the DOM tree.
	 * 
	 * @exception XPathException
	 *                When the XPath processing failed.
	 * 
	 */
	private static void runQueryWithS9API(
			Source source, 
			Processor proc, 
			String userExpression,
			String defaultElementNamespace)
					throws net.sf.saxon.trans.XPathException, URISyntaxException, SaxonApiException, XPathException {

	    XdmNode contextNode = null;
		if (source != null) {
		    TreeInfo docInfo = proc.getUnderlyingConfiguration().buildDocumentTree(source);
			
			DocumentBuilder newDocumentBuilder = proc.newDocumentBuilder();
			contextNode = newDocumentBuilder.wrap(docInfo);
		}
		
		System.out.println("conf: " + proc.getUnderlyingConfiguration());

		XPathCompiler comp = proc.newXPathCompiler();
		comp.setSchemaAware(true);
		String sourceSystemId = source != null ? source.getSystemId() : null;
		if (sourceSystemId != null && sourceSystemId.length() > 0) {
			// Set the base URI to be the system ID.
			comp.setBaseURI(new URI(sourceSystemId));
		}

		// // EXM-24296 - Set 3.0 as language version
		// comp.setLanguageVersion("3.0");

		// updateContextNamespacesS9API(comp, contextNode,
		// proxyNamespaceMappings, defaultElementNamespace, isSchemaAware);
		
	    // Computes the default namespace    
	    if (defaultElementNamespace != null) {
	     
	      // The default namespace.
	      comp.declareNamespace("", defaultElementNamespace);
	      comp.importSchemaNamespace(defaultElementNamespace);
	    }

		// if (contextNode != null) {
		// contextNode = computeContextNodeS9API(comp, contextNode,
		// sourceSystemId, contextExpression);
		// }

		XPathExecutable exec = comp.compile(userExpression);

		// If the expression only needs compilation exit at this point with a
		// null result.
		XPathSelector selector = exec.load();
		if (contextNode != null) {
			selector.setContextItem(contextNode);
		}

		printQueryResult(selector, sourceSystemId);		
	}

	/**
	 * Create a result from the XAPth
	 * 
	 * @param selector
	 *            Tcompiled XPath expression
	 * @param systemID
	 *            The system ID of the document on which the XPath was executed.
	 * @param executionStopper
	 *            Used to stop the XPath executuion
	 * @return The result of the XPath as a {@link QueryResult}.
	 * @throws XPathException
	 */
	private static void printQueryResult(XPathSelector selector, String systemID) throws XPathException {
		Iterator<XdmItem> iter = selector.iterator();

		// Convert NodeInfo list to DOM Nodes
		for (; iter.hasNext();) {

			XdmItem nextItem = iter.next();
			// logger.info("next");
			if (nextItem.isAtomicValue()) {
				System.out.println("Atomic value: " + ((XdmAtomicValue) nextItem).getValue());
			} else {
				if (nextItem instanceof XdmNode) {
					XdmNode element = (XdmNode) nextItem;
					
					System.out.println("Element " + element.getClass());
					
				   if (element.getUnderlyingValue() instanceof AbstractNodeWrapper) {
						// In our implementation the result should consist of
						// XdmNode's which contain NodeWrapper's over original
						// DOM nodes
						// if the source was a DOMSource.
						AbstractNodeWrapper nodeWrapper = (AbstractNodeWrapper) element.getUnderlyingValue();
						
						System.out.println("1 res: " + nodeWrapper.getUnderlyingNode());
					} else {
						NodeInfo underlyingNode = element.getUnderlyingNode();
						System.out.println("2 res: " + underlyingNode.getLineNumber() + " -- " + underlyingNode);
						
						Object nodeToAdd = NodeOverNodeInfo.wrap(underlyingNode);
						
						System.out.println("3 res: " + nodeToAdd.getClass());
						System.out.println("3 res: " + ((ElementOverNodeInfo)nodeToAdd).getUnderlyingNodeInfo().getLineNumber());
						
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws TransformerConfigurationException, net.sf.saxon.trans.XPathException, XPathException, URISyntaxException, SaxonApiException {
		File xmlInstance = new File("samples/xpath/personal-schema.xml");
		
		XpathPerformer.runQueryWithS9API(
				"//schema-element(person)", 
				xmlInstance.toURI().toString(), 
				"http://www.oxygenxml.com/ns/samples/personal");
	}
}
