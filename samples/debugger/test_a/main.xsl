<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="v1" select="//*"></xsl:variable>
    <xsl:template match="/">
    	Number of elements: <xsl:value-of select="count($v1)"/>
    </xsl:template>
</xsl:stylesheet>
