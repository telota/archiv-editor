<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:aodl="http://pdr.bbaw.de/namespaces/aodl/"
	xmlns:podl="http://pdr.bbaw.de/namespaces/podl/"
	xmlns:mods="http://www.loc.gov/mods/v3"
	version="1.0">
	
	<xsl:template match="/export">
		<html>
			<head>
				<meta http-equiv="content-type" content="text/html; charset=utf-8" />
				<title>
					PDR Export
				</title>
				<style type="text/css">
					body {
						background-color: #FFFFFF;
					}
					
					#header .tables, #aspects .tables {
						color: #717A30;
						font-size: 14px;
					}
					
					.tables  > * > * > * > div > div {
						background-color: #FFFFFF;
						border: 1px solid #EEEEEE; 
						margin: 1px;
						padding: 1px;
					}
					
					.tables > * > * > * > div > div > a {
						color: inherit;
						text-decoration: none;
					}
					
					.tables > * > * > * > div > div:hover {
						color: #FCFEEC;
						background-color: #FFFFFF;
						border-color: #717A30;
					}
					
					#aspects h1, #header h1 {
						text-align: center;
						font-size: 40px;
						color: #717A30;
					}
					
					#aspects h3, #header h3 {
						color: #CCD971;
					}
					
					#aspects, #bibliography, #header{
						margin: 20px;
						padding: 30px;
						background-color: #FFFFFF;
						border-color: #DDDDDD;
						border-width: 1px;
						border-style:solid;
					}
					
					.aspect {
						margin: 5px;
						padding: 6px;
						color: #CCD971;
						background-color: #FFFFFF;
						border-color: #DDDDDD;
						border-width: 1px;
						border-style: solid;
						min-height:100px;
					}
					
					
					.aspect div:hover {
						background-color:white;
					}

					#aspectHeader {
						margin: 1px;
						padding: 1px;
					}
					
					#aspectHeader .aspectTime {
						margin: 3px;
						padding: 2px;
						border: 1px solid #49A282;
						color: #49A282;
						background-color: #DFF9F0;
					}

					#aspectHeader .aspectPlace {
						margin: 3px;
						padding: 2px;
						border: 1px solid #A6A04A;
						color: #A6A04A;
						background-color: #FFFEEF;
					}

					#semantic {
						margin: 6px;
					}
					
					#semantic span {
						margin: 3px;
						padding: 2px;
					}
					
					#semantic > .PDR {
						background-color: #FFF6D0;
						color: #634812;
						border-style: solid;
						border-width:1px;
						border-color: #E6D9A2;
					}
					
					#semantic > .MUSICI {
						background-color: #CBEAC7;
						color: #5CA151;
						border-style: solid;
						border-width: 1px;
						border-color: #90B08C;						
					}
					
					#notification {
						margin: 10px;
						padding: 10px;
						color: #222222;
						border: 1px solid #999999;
					}
					
					#notification span:hover {
						border-style: solid;
						border-width: 1px;
					}
					
					#notification span.persName {
						background-color: #FFE8BC;
						color: #A6813D;
					}
					
					#notification span.name {
						background-color: #FFEFFF;
						color: #A64AA6;
					}
					
					#notification span.date {
						background-color: #DFF9F0;
						color: #49A282;
					}
					
					#notification span.placeName {
						background-color: #FFFEEF;
						color: #A6A04A;
					}
					
					#notification span.orgName {
						background-color: #E1EBF9;
						color: #496DA2;
					}
					
					#validation {
						text-align:right;
						color: #CCD971;
					}
					
					#validation a {
						color: #717A30;
						text-decoration: none;
					}
					
					#validation a:hover {
						color: black;
					}
					
					#bibliography {
						color: #444444;
						font-size: 16px;
					}
					
					.bibliography h2 {
						margin: 12px;
					}

					.source {
						margin: 4px;
						margin-left: 40px;
						text-indent: -20px;
						color: #717A30;
					}
					
					.source span:hover {
						background-color: #717A30;
						color: white;
					}
				</style>
			</head>
			
			<body>
				 <xsl:apply-templates select="pdrEntity"/>
					<xsl:apply-templates select="mods:mods"/>
			</body>
		</html>
	</xsl:template>
	
	
	
	<xsl:template match="pdrEntity">
		<div>
			<h3>
				<xsl:value-of select="./@label"/>
			</h3>
			<xsl:apply-templates select="pdrAspectsGroup"/>
		</div>
	</xsl:template>
	
	
	<xsl:template match="pdrAspectsGroup">
		<div>
			<h4>
				<xsl:value-of select="./@label"/>
			</h4>
			<xsl:apply-templates select="aodl:aspect"/>
		</div>
	</xsl:template>
	
	
	<!-- ASPECTS -->
	<xsl:template match="aodl:aspect">
		<div class="aspect">
			<a name="{./@id}"></a>
			<div>
				<div id="aspectTitle">
					<xsl:value-of select="./@id"/>
				</div>
				<div id="aspectHeader">
					<table width="100%">
						<tr>
							<td>
								<xsl:apply-templates select="aodl:semanticDim"/>
							</td>
							<td align="right">
								<!-- TIME -->
								<xsl:for-each select="aodl:timeDim/aodl:timeStm/aodl:time">
									<span class="aspectTime" title="{./@accuracy}">
										<xsl:value-of select="./@type"/>:
										<xsl:value-of select="."/>
									</span>
								</xsl:for-each>
								<!-- PLACE -->
								<xsl:for-each select="aodl:spatialDim/aodl:spatialStm/aodl:place">
									<span class="aspectPlace" title="{./@key}">
										<xsl:value-of select="./@type"/>:
										<xsl:value-of select="."/>
									</span>
								</xsl:for-each>
							</td>
						</tr>
					</table>
				</div>
				<xsl:apply-templates select="aodl:notification"/>
				<xsl:apply-templates select="aodl:validation"/>
			</div>
		</div>
	</xsl:template>
	
	<!-- TIME -->
	<xsl:template match="aodl:timeDim">
		<xsl:if test="aodl:timeStm/aodl:time">
			<div class="timeStat">
				<div>
					<a href="#{../@id}">
						<b>
							<xsl:for-each select="aodl:timeStm/aodl:time">
								<xsl:value-of select="."/>
								<xsl:if test="position()&lt;last()"> - </xsl:if>
							</xsl:for-each>
						</b>
						<xsl:text> </xsl:text>
						<xsl:value-of select="../aodl:spatialDim/aodl:spatialStm/aodl:place"/><br/>
					</a>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!-- PLACES -->
	<xsl:template match="aodl:spatialDim">
		<xsl:if test="aodl:spatialStm/aodl:place">
			<div class="whereabout">
				<div>
					<a href="#{../@id}">
						<b>
							<xsl:value-of select="aodl:spatialStm/aodl:place"/>
						</b>
						<xsl:if test="../aodl:timeDim/aodl:timeStm/aodl:time">
							(<xsl:value-of select="../aodl:timeDim/aodl:timeStm/aodl:time"/>)
						</xsl:if>
					</a>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	

	
	
		
	
	

	
	<!-- PROVIDER -->
	<xsl:template match="aodl:semanticDim">
		<div id="semantic">
			<xsl:for-each select="aodl:semanticStm">
				<span class="{./@provider}" title="provider: {./@provider}">
					<xsl:value-of select="."/>
				</span>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	
	<!-- NOTIFICATION -->
	<xsl:template match="aodl:notification">
		<div id="notification">
			<xsl:for-each select="child::node()">
				<span>
					<xsl:if test="name(.)">
						<xsl:attribute name="class">
							<xsl:value-of select="substring(name(.),6)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="./@element">
							<xsl:attribute name="title">
								<xsl:value-of select="./@element"/>
								<xsl:if test="./@type">
									(<xsl:value-of select="./@type"/>)
								</xsl:if>
							</xsl:attribute>
						</xsl:when>
						<xsl:when test="./@type">
							<xsl:attribute name="title">
								<xsl:value-of select="./@type"/>
							</xsl:attribute>
						</xsl:when>
					</xsl:choose>
					
					<xsl:value-of select="."/>
				</span>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template match="aodl:validation">
		
		<div id="validation">
			<xsl:for-each select="aodl:validationStm">
				
				<xsl:variable name="refId">
					<xsl:value-of select="aodl:reference"/>
				</xsl:variable>
				<xsl:variable name="internal">
					<xsl:value-of select="aodl:reference/@internal"/>
				</xsl:variable>
				(<xsl:for-each select="aodl:validationStm">
					
				<a href="#{$refId}">
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
				</a>
				<xsl:if test="aodl:reference/@internal">,
					<xsl:value-of select="aodl:reference/@internal"/>
				</xsl:if>
				<xsl:if test="position()&lt;last()">;
				</xsl:if></xsl:for-each>)
		</xsl:for-each>
		</div>
	</xsl:template>
	
	
	
	<!-- REFERENCES -->	
	<xsl:template match="mods:mods">
		<div class="source">
			<a name="{./@displayName}">
			</a>
			<xsl:choose>
				<xsl:when test="count(mods:relatedItem/*)&lt;3">
					<xsl:apply-templates select="mods:titleInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<b>
						<span id="familyName"><xsl:value-of select="mods:name[1]/mods:namePart[@type='family']"/></span>,
					</b>
					<span id="givenName"><xsl:value-of select="mods:name[1]/mods:namePart[@type='given']"/></span>.
					<i><span id="title"><xsl:value-of select="mods:titleInfo/mods:title"/></span></i>; in: 
					<xsl:apply-templates select="mods:relatedItem"/>
				</xsl:otherwise>
				
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	
	
	
	<xsl:template match="mods:titleInfo">
		<xsl:for-each select="../mods:name">
				<xsl:choose>
					<xsl:when test="position()=1 and name(..)='mods:mods'">
						<b>
							<span id="familyName"><xsl:value-of select="mods:namePart[@type='family']"/></span>,
						</b>
					</xsl:when>
					<xsl:otherwise>
						<span id="familyName"><xsl:value-of select="mods:namePart[@type='family']"/></span>,
					</xsl:otherwise>
				</xsl:choose>
			
			<span id="givenName">
				<xsl:value-of select="mods:namePart[@type='given']"/>
			</span>
			
			<xsl:if test="role/roleTerm and /role/roleTerm!='aut'">
				<span id="role">(<xsl:value-of select="role/roleTerm"/>)</span></xsl:if>
			
			<xsl:choose>
				<xsl:when test="position()&lt;last()">,
				</xsl:when>
				<xsl:otherwise>:
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		
		<i>
			<span id="title"><xsl:value-of select="mods:title"/></span>.
			<xsl:if test="mods:subTitle">
				<span id="subTitle"><xsl:value-of select="mods:subTitle"/></span>.
			</xsl:if>
		</i>
		<xsl:if test="../mods:originInfo/mods:copyrightDate">(<span id="copyrightDate"><xsl:value-of select="../mods:originInfo/mods:copyrightDate"/></span>) 
		</xsl:if>
		
		
		
		<xsl:if test="partName">
			<span id="partName"><xsl:value-of select="partName"/></span>.
		</xsl:if>
		<xsl:if test="partNumber">
			Vol. <xsl:value-of select="partNumber"/>.
		</xsl:if>
		
		<xsl:choose>
			<xsl:when test="../originInfo">
				<span id="place"><xsl:value-of select="../originInfo/place/placeTerm"/></span><xsl:if test="../originInfo/publisher">:
					<span id="publisher"><xsl:value-of select="../originInfo/publisher"/></span></xsl:if><xsl:if test="../originInfo/getDateIssued">,
						<span id="dateIssued"><xsl:choose>
							<xsl:when test="../genre='journal'">
								<xsl:value-of select="../originInfo/getDateIssued"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring(../originInfo/getDateIssued,1,4)"/>
							</xsl:otherwise>
						</xsl:choose>
						</span>
					</xsl:if>
				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="../location/physicalLocation"/>
			</xsl:otherwise>
		</xsl:choose>		
	</xsl:template>
	
	
	<xsl:template match="relatedItem">
		<xsl:apply-templates select="titleInfo"/>
	</xsl:template>
	
	
</xsl:stylesheet>