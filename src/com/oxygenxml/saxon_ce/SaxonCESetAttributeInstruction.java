/*
 *  The Syncro Soft SRL License
 *
 *  Copyright (c) 1998-2012 Syncro Soft SRL, Romania.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistribution of source or in binary form is allowed only with
 *  the prior written permission of Syncro Soft SRL.
 *
 *  2. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  3. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  4. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Syncro Soft SRL (http://www.sync.ro/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  5. The names "Oxygen" and "Syncro Soft SRL" must
 *  not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact support@oxygenxml.com.
 *
 *  6. Products derived from this software may not be called "Oxygen",
 *  nor may "Oxygen" appear in their name, without prior written
 *  permission of the Syncro Soft SRL.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE SYNCRO SOFT SRL OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 */
package com.oxygenxml.saxon_ce;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.Literal;
import net.sf.saxon.om.AttributeCollection;
import net.sf.saxon.style.Compilation;
import net.sf.saxon.style.ComponentDeclaration;
import net.sf.saxon.style.StyleElement;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.BooleanValue;

/**
 * Recognize the Saxon-CE extension element ixsl:set-attribute in the stylesheet.
 * 
 * @author radu_pisoi
 */
public class SaxonCESetAttributeInstruction extends StyleElement {
  
  /**
   * Compiled expression from select attribute.
   */
  private Expression selectExpression;

  /**
   * @see net.sf.saxon.style.StyleElement#isInstruction()
   */
  @Override
  public boolean isInstruction() {
    return true;
  }
  

  @Override
  public void prepareAttributes() throws XPathException {
      AttributeCollection atts = getAttributeList();
        
      String selectAttributeVal = null;
      int selectAttributeIdx = -1;
      String nameAttributeVal = null;
      for (int a = 0; a < atts.getLength(); a++) {
        String attrQName = atts.getQName(a);
        if (attrQName.equals("select")) {
          selectAttributeVal = atts.getValue(a);
          selectAttributeIdx  =a;
        } else if (attrQName.equals("name")) {
          nameAttributeVal = atts.getValue(a);
        } else if (attrQName.equals("namespace")) {
          // Do nothing
        } else {
          checkUnknownAttribute(atts.getNodeName(a));
        }
      }

      if (selectAttributeVal != null) {
        // Compile the XPath expression from select attribute
        try  {
          selectExpression = makeExpression(selectAttributeVal, selectAttributeIdx);
        } catch (XPathException err) {
          // already reported: prevent further errors
          selectExpression = Literal.makeLiteral(BooleanValue.FALSE);
        }
      }
    
      if (nameAttributeVal == null) {
        // Report an error is name attribute is absent
        reportAbsence("name");
      }
      
      if (selectExpression == null) {
        // Report an error is select attribute is absent
        reportAbsence("select");
      }
  }

  /**
   * @see net.sf.saxon.style.StyleElement#validate(net.sf.saxon.style.ComponentDeclaration)
   */
  @Override
  public void validate(ComponentDeclaration decl) throws XPathException {
    // Validate the XPath expression from select attribute
    selectExpression = typeCheck("select", selectExpression);
  }

  /**
   * @see net.sf.saxon.style.StyleElement#compile(net.sf.saxon.style.Compilation, net.sf.saxon.style.ComponentDeclaration)
   */
  @Override
  public Expression compile(Compilation compilation, ComponentDeclaration decl)
      throws XPathException {
    return new SaxonCERuntimeExpression();
  }
}
