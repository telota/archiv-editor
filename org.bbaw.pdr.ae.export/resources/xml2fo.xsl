<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:aodl="http://pdr.bbaw.de/namespaces/aodl/"
	xmlns:podl="http://pdr.bbaw.de/namespaces/podl/"
	xmlns:fo = "http://www.w3.org/1999/XSL/Format"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:mods="http://www.loc.gov/mods/v3"
	version="1.0">
	<xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8"/>
	
	<xsl:template match="/export">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			
			
			<fo:layout-master-set>
				<fo:simple-page-master master-name="AspectPage" page-width="210mm" page-height="297mm" margin="5mm" margin-left="5mm">
					<fo:region-body margin-top="15mm" margin-bottom="15mm" margin-left="5mm" margin-right="9mm"/>
					<fo:region-before extent = "15mm"/>
					<fo:region-after extent="12mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="Bibliography" page-width="210mm" page-height="297mm" margin="5mm" margin-left="15mm">
					<fo:region-body margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm"/>
					<fo:region-before extent="15mm"/>
					<fo:region-after extent="15mm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			
			
			<fo:page-sequence master-reference="AspectPage">
<!-- HEADER -->
				<fo:static-content flow-name="xsl-region-before" font-size="7pt" >
					<fo:block-container margin="3pt">
						<fo:block text-align-last="left">
							<xsl:value-of select="podl:person/@displayName"/>
							(<xsl:value-of select="podl:person/@id"/>)
						</fo:block>
						<fo:block text-align-last="justify">
							<xsl:value-of select="./@date"/>
							<fo:inline keep-together.within-line="always">
								<fo:leader leader-pattern="space"/>
								Page <fo:page-number/> of <fo:page-number-citation ref-id="endofdoc"/> 
							</fo:inline>
						</fo:block>
					</fo:block-container>
					
					<fo:block>
						<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness=".5pt"/>
					</fo:block> 
				</fo:static-content>

<!-- FOOTER -->
				<fo:static-content flow-name="xsl-region-after" font-size="7pt">
					<fo:block-container margin="3pt">
						<fo:block>
							<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness=".5pt"/>
						</fo:block> 
						<fo:block text-align-last="justify">
							<xsl:value-of select="count(pdrEntity/pdrAspectsGroup/aodl:aspect)"/> Aspects
							<fo:inline keep-together.within-line="always">
								<fo:leader leader-pattern="space"/>
								<xsl:value-of select="count(mods:mods)"/> References
							</fo:inline>
						</fo:block>
					</fo:block-container>
				</fo:static-content>	
				
<!-- CONTENT -->
				<fo:flow flow-name="xsl-region-body" font-family="Times New Roman">
					<fo:block text-align="left" font-size="12pt">
						<!-- begin -->
						<xsl:apply-templates select="pdrEntity"/>
					</fo:block>
					<!--<xsl:apply-templates select="aodl:aspect"/>-->
					<xsl:if test="count(mods:mods)=0">
						<fo:block id="endofdoc"></fo:block>
					</xsl:if>
				</fo:flow>
				
			</fo:page-sequence>
			
			
			
<!-- BIBLIOGRAPHY -->
			<xsl:if test="count(mods:mods)>0">
				<fo:page-sequence master-reference="Bibliography">
		<!-- HEADER -->
					<fo:static-content flow-name="xsl-region-before" font-size="7pt" >
						<fo:block-container margin="3pt">
							<fo:block text-align-last="left">
								<xsl:value-of select="podl:person/@displayName"/>
								(<xsl:value-of select="podl:person/@id"/>)
							</fo:block>
							<fo:block text-align-last="justify">
								<xsl:value-of select="./@date"/>
								<fo:inline keep-together.within-line="always">
									<fo:leader leader-pattern="space"/>
									Page <fo:page-number/> of <fo:page-number-citation ref-id="endofdoc"/> 
								</fo:inline>
							</fo:block>
						</fo:block-container>
						
						<fo:block>
							<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness=".5pt"/>
						</fo:block> 
					</fo:static-content>
					
					
			<!-- FOOTER -->
					<fo:static-content flow-name="xsl-region-after" font-size="7pt">
						<fo:block-container margin="3pt">
							<fo:block>
								<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness=".5pt"/>
							</fo:block> 
							<fo:block text-align-last="justify">
								<xsl:value-of select="count(aodl:aspect)"/> Aspects
								<fo:inline keep-together.within-line="always">
									<fo:leader leader-pattern="space"/>
									<xsl:value-of select="count(mods:mods)"/> References
								</fo:inline>
							</fo:block>
						</fo:block-container>
					</fo:static-content>						
					
					<fo:flow flow-name="xsl-region-body" font-family="Times New Roman">
						<fo:block-container font-size="10pt">
							<fo:block id="endofdoc"></fo:block>
							<fo:block font-size="14pt">
								bibliography
							</fo:block>
							<fo:block>
								<fo:leader leader-pattern="rule" rule-style="solid" leader-length="30%"/>
							</fo:block>
							<fo:block>
								<xsl:apply-templates select="mods:mods"/>
							</fo:block>
						</fo:block-container>
					</fo:flow>
				</fo:page-sequence>
			</xsl:if>
			
		</fo:root>
	</xsl:template>
	
	
	
	
	
	<!-- PdrEntity -->
	<xsl:template match="pdrEntity">
		<fo:block text-align="left" margin-bottom="9pt" margin-top="10pt">
			<fo:block font-size="14pt">
				<xsl:value-of select="./@label"/>
			</fo:block>
			<fo:block font-size="5pt" font-family="sans-serif" margin-left="10pt" >
				<xsl:value-of select="./@id"/>
				<fo:leader leader-pattern="rule" leader-length="25%" rule-style="dotted"/>
			</fo:block>
		</fo:block>
		<!-- <xsl:apply-templates select="podl:person"/>-->
			<xsl:apply-templates select="pdrAspectsGroup"/>
	</xsl:template>
	
	
	<!-- PERSON -->
	<xsl:template match="podl:person">
		<fo:block>
			
		</fo:block>
	</xsl:template>
	
	<!-- OrdererdHead -->
	<xsl:template match="pdrAspectsGroup">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="180mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="sans-serif" font-size="8pt" color="#222222" text-align="center">
							<!---<xsl:value-of select="./@classification"/>-->
							<!--<xsl:value-of select="./@label"/>-->
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!--<fo:leader leader-pattern="rule" leader-length="40%" rule-style="solid"/>-->
		<!-- weiter -->
		<xsl:apply-templates select="aodl:aspect"/>
	</xsl:template>
	
	
	
	
	<!-- ASPECT -->
	<xsl:template match="aodl:aspect">
		
		<fo:block-container margin-bottom="10pt" margin-left="5pt" block-progression-dimension.minimum="2cm" keep-together.within-page="always">
			<!-- align columns -->
			<fo:table table-layout="fixed">
				<fo:table-column column-width="30mm"/>
				<fo:table-column column-width="100mm"/>
				<fo:table-column column-width="40mm"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt" color="#999999" font-family="sans-serif">
								<xsl:value-of select="../@label"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:apply-templates select="aodl:notification"/>
								<xsl:apply-templates select="aodl:validation"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block margin-top="0pt">
								<xsl:apply-templates select="aodl:semanticDim"/>
								<xsl:apply-templates select="aodl:spatialDim"/>
								<xsl:apply-templates select="aodl:timeDim"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

		</fo:block-container>
	</xsl:template>
	
	
	<!-- Ort, Raeumliche Attribute -->
	<xsl:template match="aodl:spatialDim">
			<xsl:if test="count(aodl:spatialStm/aodl:place)>0">
				<xsl:for-each select="aodl:spatialStm/aodl:place">
					<fo:block text-align="right" font-size="8pt" margin-left="7pt">
						<fo:inline font-weight="normal">
							<xsl:text> </xsl:text>
							<xsl:value-of select="."/>
						</fo:inline>
						<!--<xsl:value-of select="./@type"/>-->
						<!--<xsl:if test="(count(./@*) and not(./@type)) or (count(./@*)&gt;1 and ./@type)">
							(<xsl:for-each select="./@*">
								<xsl:if test="not(name(.)='type')">
									<xsl:value-of select="name(.)"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="."/>
								</xsl:if>
							</xsl:for-each>)</xsl:if>-->
					</fo:block>
				</xsl:for-each>
			</xsl:if>
	</xsl:template>
	
	<!-- Zeitattribute -->
	<xsl:template match="aodl:timeDim">
			<!-- ZEIT -->
			<xsl:if test="count(aodl:timeStm/aodl:time)>0">
				<fo:block text-align="right" font-size="8pt" margin-left="7pt">
					<xsl:for-each select="aodl:timeStm/aodl:time">
						<!--<xsl:value-of select="./@type"/>-->
						<fo:inline font-weight="normal">
							<xsl:text> </xsl:text>
							<xsl:if test="./@type and ./@type='from' or ./@type='to'">
								<xsl:value-of select="./@type"/>
							</xsl:if>
							<xsl:text> </xsl:text>
							<xsl:value-of select="."/>
						</fo:inline>
						<!--(<xsl:for-each select="./@*">
							<xsl:if test="not(name(.)='type')">
								<xsl:value-of select="name(.)"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="."/>
							</xsl:if>
						</xsl:for-each>)-->
					</xsl:for-each>
				</fo:block>
			</xsl:if>
		</xsl:template>
	
	<xsl:template match="aodl:notification">
			<!-- NOTIFICATION -->
			<fo:block text-align="left" font-size="10pt" text-indent="-10pt" margin-left="8pt">

				<xsl:choose>
					<xsl:when test="count(./*)>0">
						<!--<fo:block font-size="8pt">
							<xsl:for-each select="aodl:notification/*">
								<xsl:choose>
									<xsl:when test="./@element">
										<xsl:value-of select="./@element"/>
										(<xsl:value-of select="./@type"/>)</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="./@type"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="following-sibling::*[1]">, </xsl:if>
							</xsl:for-each>
						</fo:block>-->
						
						<fo:block font-style="normal">
							<xsl:for-each select="./*">
								<xsl:if test="not(starts-with(normalize-space(preceding-sibling::text()[1]), ',')or starts-with(normalize-space(preceding-sibling::text()[1]), '-')or starts-with(normalize-space(preceding-sibling::text()[1]), '.'))">
									<xsl:text> </xsl:text>
								</xsl:if>
								<xsl:text> </xsl:text>
								<xsl:value-of select="normalize-space(preceding-sibling::text()[1])"/>
								<xsl:text></xsl:text>
								<fo:inline font-style="italic">
									<xsl:value-of select="normalize-space(.)"/>
								</fo:inline>
								<xsl:if test="not(following-sibling::*)">
									<xsl:if test="not(starts-with(normalize-space(following-sibling::text()[1]),',') or starts-with(normalize-space(following-sibling::text()[1]),'-')or starts-with(normalize-space(following-sibling::text()[1]),'.'))">
										<xsl:text></xsl:text>
									</xsl:if>
									<xsl:value-of select="normalize-space(following-sibling::text()[1])"/>
								</xsl:if>
							</xsl:for-each>
						</fo:block>
							
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./child::text()"/>
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
	</xsl:template>


	<!-- VALIDATION -->
	<xsl:template match="aodl:validation">
		<xsl:for-each select="aodl:validationStm">
			<fo:block text-align="right" font-size="8pt">
				
				<!--<xsl:choose>
					<xsl:when test="aodl:reference/@internal">
						(<xsl:value-of select="aodl:reference"/>,
						<xsl:value-of select="aodl:reference/@internal"/>)
					</xsl:when>
					<xsl:otherwise>
						(<xsl:value-of select="aodl:reference"/>)
					</xsl:otherwise>
				</xsl:choose>-->
				
				<xsl:variable name="refId">
					<xsl:value-of select="aodl:reference"/>
				</xsl:variable>
				<xsl:variable name="internal">
					<xsl:value-of select="aodl:reference/@internal"/>
				</xsl:variable>
				
				<xsl:for-each select="/export/mods:mods[@ID=$refId]">
					<xsl:value-of select="mods:name/mods:namePart[@type='family']"/>
					<xsl:text> </xsl:text>
					<!--<xsl:value-of select="mods:titleInfo"/>-->
					<xsl:value-of select="mods:originInfo/mods:dateCreated"/>
					<xsl:if test="$internal">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="$internal"/>
					</xsl:if>
				</xsl:for-each>
			</fo:block>
		</xsl:for-each>
	</xsl:template>

	
	
	<!-- Semantische Klassifikation -->
	<xsl:template match="aodl:semanticDim">
		<xsl:for-each select="aodl:semanticStm">
			<fo:block text-align-last="right" font-size="8pt">
				<xsl:value-of select="./@provider"/>:
				
				<xsl:value-of select="."/>
			</fo:block>
		</xsl:for-each>
	</xsl:template>
	
	
	<!-- REFERENCES -->	
	<xsl:template match="mods:mods">
		<fo:block margin="6pt" text-indent="-12pt" margin-left="24pt">
			
			<!-- Names -->
			<xsl:for-each select="mods:name">
			
				<xsl:choose>
					<xsl:when test="position()&lt;2">
						<fo:inline font-weight="bold">
							<xsl:value-of select="mods:namePart[@type='family']"/>,
						</fo:inline>
						<xsl:value-of select="mods:namePart[@type='given']"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="mods:namePart[@type='family']"/>,
						<xsl:value-of select="substring(mods:namePart[@type='given'],1,1)"/>.
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:choose>
					<xsl:when test="position()&lt;last()">,
					</xsl:when>
					<xsl:otherwise>:
					</xsl:otherwise>
				</xsl:choose>
			
			</xsl:for-each>
			
			<!--Titel-->
			<xsl:apply-templates select="mods:titleInfo"/>
			
			<!-- ?? 
			<xsl:choose>
				<xsl:when test="count(mods:relatedItem/*)&lt;3">
					<xsl:apply-templates select="mods:titleInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<fo:inline font-weight="bold">
						<xsl:value-of select="mods:name[1]/mods:namePart[@type='family']"/>,
					</fo:inline>
					<xsl:value-of select="mods:name[1]/mods:namePart[@type='given']"/>.
					<xsl:value-of select="mods:titleInfo/mods:title"/>; in: 
					<xsl:apply-templates select="mods:relatedItem"/>
				</xsl:otherwise>
				
			</xsl:choose>-->
		</fo:block>
		
	</xsl:template>
	
		
		
	
	<!-- Titel -->
	<xsl:template match="mods:titleInfo">
		<!--<xsl:if test="../originInfo/copyrightDate">(<xsl:value-of select="../originInfo/copyrightDate"/>) -->
		
		<xsl:value-of select="mods:title"/>.
		<xsl:if test="mods:subTitle">
			<fo:inline font-style="italic">
				<xsl:value-of select="mods:subTitle"/>.
			</fo:inline>
		</xsl:if>
		<xsl:if test="mods:partNumber">
			<xsl:value-of select="mods:partNumber"/>: 
		</xsl:if>
		<xsl:if test="mods:partName">
			<xsl:value-of select="mods:partName"/>.
		</xsl:if>
		<!--</xsl:if>-->
	</xsl:template>
		
		
		<!-- Ort, Jahr, Verlag -->
	<xsl:template match="mods:originInfo">
		<xsl:value-of select="mods:place/mods:placeTerm"/><xsl:if test="mods:publisher">:
			<xsl:value-of select="mods:publisher"/></xsl:if><xsl:if test="mods:getDateIssued">,
				<xsl:choose>
					<xsl:when test="../mods:genre='journal'">
						<xsl:value-of select="mods:getDateIssued"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="substring(mods:getDateIssued,1,4)"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:template>
		
	
		
	
	
	<xsl:template match="relatedItem">
		<xsl:apply-templates select="titleInfo"/>
	</xsl:template>
	
	
</xsl:stylesheet>
