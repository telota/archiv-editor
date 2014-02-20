<?xml version="1.0" encoding="UTF-8"?>
<!--<?xml-model href="http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_all.rng" schematypens="http://relaxng.org/ns/structure/1.0"?>-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:aodl="http://pdr.bbaw.de/namespaces/aodl/"
 xmlns:podl="http://pdr.bbaw.de/namespaces/podl/"
 version="1.0">
 
 <xsl:template match="/export">
 
  <TEI
   xmlns:xi="http://www.w3.org/2001/XInclude"
   xmlns="http://www.tei-c.org/ns/1.0">
   <teiHeader>
    <fileDesc>
     <title>
      <xsl:value-of select="podl:person/@displayName"/>
     </title>
    	<publicationStmt>
    		<p>
    			
    		</p>
    	</publicationStmt>
    	<sourceDesc>
    		<p>
    			
    		</p>
    	</sourceDesc>
    </fileDesc>
   </teiHeader>
  	<text>
  			<body>
  				
  			</body>
  	</text>
  </TEI>
 </xsl:template>

</xsl:stylesheet>