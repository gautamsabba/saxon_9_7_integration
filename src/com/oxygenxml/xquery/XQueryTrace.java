package com.oxygenxml.xquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.Controller;
import net.sf.saxon.expr.PackageData;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.expr.instruct.Bindery;
import net.sf.saxon.expr.instruct.Executable;
import net.sf.saxon.expr.instruct.GlobalVariable;
import net.sf.saxon.expr.instruct.SlotManager;
import net.sf.saxon.lib.Logger;
import net.sf.saxon.lib.TraceListener;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trace.InstructionInfo;
import net.sf.saxon.trans.XPathException;

import com.saxonica.config.EnterpriseConfiguration;

/**
 * XQuery executor sample class.
 */
public class XQueryTrace {
  
  private class MyTraceListener implements TraceListener {
    @Override
    public void setOutputDestination(Logger stream) {}

    @Override
    public void open(Controller controller) {}

    @Override
    public void close() {}
    @Override
    public void enter(InstructionInfo instruction, XPathContext context) {
      if (instruction.getConstructType() == StandardNames.XSL_FUNCTION) {
        System.out.println();
        System.out.println("function " + instruction.getObjectName() + " line: " + instruction.getLineNumber());
        // Log the global variables.
        printXQueryGlobalVariables(instruction, context);
      }
    }

    @Override
    public void leave(InstructionInfo instruction) {}
    @Override
    public void startCurrentItem(Item currentItem) {}
    @Override
    public void endCurrentItem(Item currentItem) {}
    
  }
	
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
	public XQueryTrace(File xqueryFile) throws XPathException,
			UnsupportedEncodingException, FileNotFoundException, IOException {
		config = new EnterpriseConfiguration();

		// Prepare for debugger
		config.setErrorListener(errorListener);
		config.setLineNumbering(true);
		config.setTraceListener(new MyTraceListener());

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

	/**
   * Obtains the global variables.
   * 
   * @param pathContext The xpath context.
   * @param instructionInfo The instruction info.
   * @return The list of global variables.
   */
  public static void printXQueryGlobalVariables(
      InstructionInfo instructionInfo,
      XPathContext pathContext) {
    
    Executable executable = pathContext.getController().getExecutable();
    PackageData topLevelPackage = executable.getTopLevelPackage();
    
    //SlotManager slotManager = bindery.getGlobalVariableMap();
    SlotManager slotManager =  topLevelPackage.getGlobalSlotManager();
  
    if (slotManager != null) {
      List<StructuredQName> variableNames = slotManager.getVariableMap();
      if (variableNames != null) {
        
        for(int i = 0; i < variableNames.size(); i++) {
          // Extract global variable value
          StructuredQName varName = variableNames.get(i);
          
          List<GlobalVariable> compiledGlobalVariables = topLevelPackage.getGlobalVariableList();
          if (compiledGlobalVariables != null) {
            GlobalVariable globalVariable = null;
            for (Iterator<GlobalVariable> iterator = compiledGlobalVariables.iterator(); iterator.hasNext();) {
              GlobalVariable cgl = iterator.next();
              if (cgl.getObjectName().equals(varName)) {
                globalVariable = cgl;
                break;
              }
            }
            
            // Create a new variable info to work further. We do not want to modify Saxon own system id.
            // Saxon 9.5 - Do not present the context-item variable
            if (!"{http://saxon.sf.net/}context-item".equals(varName.getClarkName())) {
              if (globalVariable != null) {
                // Get compiled global variable based on the fingerprint.
                // Correct the systemId.
                int line = globalVariable.getLineNumber();
                
                System.out.println("Variable: " + globalVariable.getObjectName() + " line: " + line);
              }
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
		try {
			XQueryTrace xQueryDebugger = new XQueryTrace(new File(
					"samples/xquery/lines.xq"));
			
			System.out.println("Run query...");
			xQueryDebugger.runXQuery(null, new StreamResult(new File("out/result.xml")));
			System.out.println("Done.");			
		} catch (Exception e) {
			System.out.println("Transformation failed: " + e);
			e.printStackTrace();
		}
	}
	
	
	
}
