<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xsd"
    version="3.0">
    
    <xsl:output method="xml"/>
    
    <xsl:import-schema schema-location="BookStore.xsd"/>
    
    <xsl:template match="*">
        <xsl:try>
            <xsl:copy validation="lax">
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates/>
            </xsl:copy>
            <xsl:catch>
                <!-- Validation failed, remove the invalid item -->
            </xsl:catch>
        </xsl:try>
    </xsl:template>
    
</xsl:transform>