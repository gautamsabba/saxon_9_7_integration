package com.oxygenxml.debugger;

import net.sf.saxon.Controller;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.expr.instruct.TraceExpression;
import net.sf.saxon.lib.Logger;
import net.sf.saxon.lib.TraceListener;
import net.sf.saxon.om.Item;
import net.sf.saxon.style.StyleElement;
import net.sf.saxon.trace.InstructionInfo;
import net.sf.saxon.type.Type;

public class SaxonTraceListener implements TraceListener {

	@Override
	public void enter(InstructionInfo instruction, XPathContext context) {
		// Starting with Saxon 9.4, a TraceExpression comes also from a StyleElement.
		// So we patched StyleElement to add itself as a property when it creates a TraceExpression
		// because our code requires a StyleElement.
		if (instruction instanceof TraceExpression) {
			Object property = instruction.getProperty("details");
			if (property instanceof InstructionInfo) {
				instruction = (InstructionInfo) property;
			}
		}

		if (instruction instanceof StyleElement) {
			StyleElement currentStyleElement = (StyleElement) instruction;
			if (currentStyleElement.getNodeKind() == Type.ELEMENT) {
				enterStyleElement(currentStyleElement, context);
			}
		}
	}

	private void enterStyleElement(StyleElement currentStyleElement,
			XPathContext context) {
//		VariableHelper.printGlobalVariables(currentStyleElement, context);
	}

	@Override
	public void startCurrentItem(Item currentItem) {}

	@Override
	public void setOutputDestination(Logger arg0) {}

	@Override
	public void open(Controller controller) {}

	@Override
	public void close() {}

	@Override
	public void leave(InstructionInfo instruction) {}

	@Override
	public void endCurrentItem(Item currentItem) {}
}
