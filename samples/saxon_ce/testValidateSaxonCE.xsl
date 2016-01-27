<!-- 
     This is a XSLT stylesheet for Saxon-CE processor.
     
     For more information about Saxon-CE see: http://saxonica.com/ce/index.xml
-->
<xsl:transform
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:prop="http://saxonica.com/ns/html-property"
  xmlns:style="http://saxonica.com/ns/html-style-property"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"  
  xmlns:ixslt="http://saxonica.com/ns/interactiveXSLT"
  xmlns:js="http://saxonica.com/ns/globalJS"
  
  exclude-result-prefixes="xs prop"
  extension-element-prefixes="ixslt"
  version="2.0">
  
  <xsl:template match="/">
    
    <ixslt:set-attribute name="attr1" select="test"/>
   
  </xsl:template>  
  
</xsl:transform>