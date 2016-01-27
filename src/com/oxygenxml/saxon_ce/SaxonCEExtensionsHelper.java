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

import com.saxonica.config.ProfessionalConfiguration;

import net.sf.saxon.Configuration;
import net.sf.saxon.trans.XPathException;

/**
 * Utility class used to register the Saxon-CE extension functions and elements.
 * 
 * @author radu_pisoi
 */
public class SaxonCEExtensionsHelper {
  
  /**
   * Register the Saxon-CE extension functions and instructions.
   * 
   * @param configuration The Saxon-CE professional configuration. 
   * @throws XPathException
   */
  public static void registerSaxonCEExtensions(Configuration configuration) throws XPathException {
    if (configuration instanceof ProfessionalConfiguration) {
      ProfessionalConfiguration pConfiguration = (ProfessionalConfiguration) configuration;
      
//      // EXM-30547 Recognize all javascript functions
//      pConfiguration.setExtensionBinder("saxon_ce_js", new JSFunctionLibrary());
//
//      // Register extension functions 
//      registerSaxonCEExtensionFunctions(pConfiguration);
      
      // Register extension instructions
      registerSaxonCEExtensionInstructions(pConfiguration);
    }
  }
  
  
//  /**
//   * Register the Saxon-CE extension functions to the given configuration.
//   * 
//   * @param configuration The configuration file where to register the functions.
//   */
//  private static void registerSaxonCEExtensionFunctions(ProfessionalConfiguration configuration) {
//    // ixsl:page
//    configuration.registerExtensionFunction(new SaxonCEPageFunction());
//    // ixsl:event
//    configuration.registerExtensionFunction(new SaxonCEEventFunction());
//    // ixsl:source
//    configuration.registerExtensionFunction(new SaxonCESourceFunction());
//    // ixsl:window
//    configuration.registerExtensionFunction(new SaxonCEWindowFunction());
//    // ixsl:get
//    configuration.registerExtensionFunction(new SaxonCEGetFunction());
//    // ixsl:call
//    configuration.registerExtensionFunction(new SaxonCECallFunction());
//    // ixsl:eval
//    configuration.registerExtensionFunction(new SaxonCEEvalFunction());
//  }

  /**
   * Register the Saxon-CE extension functions to the given configuration.
   * 
   * @param configuration The configuration file where to register the functions.
   * @throws XPathException 
   */
  private static void registerSaxonCEExtensionInstructions(ProfessionalConfiguration configuration) throws XPathException {
    configuration.setExtensionElementNamespace(
    	"http://saxonica.com/ns/interactiveXSLT", 
        SaxonCEExtensionElementFactory.class.getName());
  }
}
