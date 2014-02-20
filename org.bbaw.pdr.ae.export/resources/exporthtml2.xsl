<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:aodl="http://pdr.bbaw.de/namespaces/aodl/"
	xmlns:podl="http://pdr.bbaw.de/namespaces/podl/"
	version="1.0">
	
	<xsl:template match="/export">
		<html>
			<head>
				<meta http-equiv="content-type" content="text/html; charset=utf-8" />
				<title>
					<xsl:value-of select="podl:person/@displayName"/>
				</title>
				<style type="text/css">
					body {
						background-color: #FCFFEC;
					}
					
					#header .tables, #aspects .tables {
						color: #717A30;
						font-size: 14px;
					}
					
					.tables  > * > * > * > div > div {
						background-color: #FCFEEC;
						border: 1px solid #CCD971; 
						margin: 1px;
						padding: 1px;
					}
					
					.tables > * > * > * > div > div > a {
						color: inherit;
						text-decoration: none;
					}
					
					.tables > * > * > * > div > div:hover {
						color: #FCFEEC;
						background-color: #CCD971;
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
						background-color: #F8FFCD;
						border-color: #CCD971;
						border-width: 2px;
						border-style:solid;
					}
					
					.aspect {
						margin: 5px;
						padding: 6px;
						color: #CCD971;
						background-color: #FCFEEC;
						border-color: #CCD971;
						border-width: 3px;
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
						color: #CCD971;
						border: 1px solid #CCD971;
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
						color: #CCD971;
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
				<div id="header">
					<h3>
						person:
					</h3>
					<table width="100%" class="tables">
						<tr>
							<td width="30%" id="timeTable" valign="top">
								timetable:
								<xsl:apply-templates select="aodl:aspect/aodl:timeDim">
									<xsl:sort select="aodl:timeStm/aodl:time"/>
								</xsl:apply-templates>
							</td>
							<td valign="top">
								<xsl:value-of select="podl:person/@id"/>
								<h1>
									<xsl:value-of select="podl:person/@displayName"/>
								</h1>
							</td>
							<td width="30%" id="whereaboutTable" align="right" valign="top">
								whereabouts:
								<xsl:apply-templates select="aodl:aspect/aodl:spatialDim">
									<xsl:sort select="../aodl:timeDim/aodl:timeStm/aodl:time"/>
									<xsl:sort select="aodl:spatialStm/aodl:place"/>
								</xsl:apply-templates>
							</td>
						</tr>
						<tr>
							<xsl:choose>
								<xsl:when test="count(aodl:aspect/aodl:notification/aodl:orgName)=0 and count(aodl:aspect/aodl:notification/aodl:name)!=0">
									<td id="definitionTable" colspan="3" valign="top">
										<xsl:if test="count(aodl:aspect/aodl:notification/aodl:name)&gt;0">
											definitions/professions:
											<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:name">
												<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
												<xsl:sort select="."/>
											</xsl:apply-templates>
										</xsl:if>
									</td>
								</xsl:when>
								<xsl:when test="count(aodl:aspect/aodl:notification/aodl:name)=0">
									<td id="organisationsTable" colspan="3" align="right" valign="top">
										<xsl:if test="count(aodl:aspect/aodl:notification/aodl:orgName)&gt;0">
											organisations:
											<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:orgName">
												<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
												<xsl:sort select="."/>
											</xsl:apply-templates>
										</xsl:if>
									</td>									
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
									
										<xsl:when test="count(aodl:aspect/aodl:notification/aodl:name)&lt;count(aodl:aspect/aodl:notification/aodl:orgName)">
										
											<td id="definitionTable" colspan="1" valign="top">
												definitions/professions:
												<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:name">
													<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
													<xsl:sort select="."/>
												</xsl:apply-templates>
											</td>
											<td id="organisationsTable" colspan="2" align="right" valign="top">
												organisations:
												<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:orgName">
													<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
													<xsl:sort select="."/>
												</xsl:apply-templates>
											</td>
										</xsl:when>
										<xsl:otherwise>
											<td id="definitionTable" colspan="2" valign="top">
												definitions/professions:
												<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:name">
													<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
													<xsl:sort select="."/>
												</xsl:apply-templates>
											</td>
											<td id="organisationsTable" colspan="1" align="right" valign="top">
												organisations:
												<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:orgName">
													<xsl:sort select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>
													<xsl:sort select="."/>
												</xsl:apply-templates>
											</td>						
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</tr>
						<tr>
							<td id="socialTable" colspan="3" valign="top">
								social:
								<xsl:apply-templates select="aodl:aspect/aodl:notification/aodl:persName">
									<xsl:sort select="."/>
								</xsl:apply-templates>
							</td>
						</tr>
					</table>
				</div>
				<div id="aspects">
					<h3>
						Aspects:
					</h3>
					<xsl:apply-templates select="aodl:aspect"/>
				</div>
				<div id="bibliography">
					<h2>
						bibliography:
					</h2>
					<xsl:apply-templates select="mods"/>
				</div>

				<div id="footer">
				</div>
			</body>
		</html>
	</xsl:template>
	
	
	
	
	
	
	<!-- TIME -->
	<xsl:template match="aodl:aspect/aodl:timeDim">
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
	<xsl:template match="aodl:aspect/aodl:spatialDim">
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
	
	<!-- DEFINITIONS -->
	<xsl:template match="aodl:aspect/aodl:notification/aodl:name">
		<div class="definition">
			<div>
				<a href="#{../../@id}">
					<b>
						<xsl:value-of select="."/>
					</b>
					<xsl:if test="../../aodl:timeDim/aodl:timeStm/aodl:time">
						(<xsl:value-of select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>)
					</xsl:if>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<!-- ORGANISATIONS -->
	<xsl:template match="aodl:aspect/aodl:notification/aodl:orgName">
		<div class="organisations">
			<div>
				<a href="#{../../@id}">
					<b>
						<xsl:value-of select="."/>
					</b>
					<xsl:if test="../../aodl:timeDim/aodl:timeStm/aodl:time">
						(<xsl:value-of select="../../aodl:timeDim/aodl:timeStm/aodl:time"/>)
					</xsl:if>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<!-- SOCIAL -->
	<xsl:template match="aodl:aspect/aodl:notification/aodl:persName">
		<div class="social">
			<div>
				<a href="#{../../@id}">
					<b>
						<xsl:value-of select="."/>
					</b>
				</a>
			</div>
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
						<xsl:when test="./type">
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
			(<xsl:for-each select="aodl:validationStm">
				<a href="#{aodl:reference}">
					<xsl:value-of select="."/>
				</a>
				<xsl:if test="aodl:reference/@internal">,
					<xsl:value-of select="aodl:reference/@internal"/>
				</xsl:if>
				<xsl:if test="position()&lt;last()">;
				</xsl:if></xsl:for-each>)
		</div>
	</xsl:template>
	
	
	
	<!-- REFERENCES -->	
	<xsl:template match="mods">
		<div class="source">
			<a name="{./@displayName}">
			</a>
			<xsl:choose>
				<xsl:when test="count(relatedItem/*)&lt;3">
					<xsl:apply-templates select="titleInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<b>
						<span id="familyName"><xsl:value-of select="name[1]/namePart[@type='family']"/></span>,
					</b>
					<span id="givenName"><xsl:value-of select="name[1]/namePart[@type='given']"/></span>.
					<i><span id="title"><xsl:value-of select="titleInfo/title"/></span></i>; in: 
					<xsl:apply-templates select="relatedItem"/>
				</xsl:otherwise>
				
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	
	
	
	<xsl:template match="titleInfo">
		<xsl:for-each select="../name">
			
			
				<xsl:choose>
					<xsl:when test="position()=1 and name(..)='mods'">
						<b>
							<span id="familyName"><xsl:value-of select="namePart[@type='family']"/></span>,
						</b>
					</xsl:when>
					<xsl:otherwise>
						<span id="familyName"><xsl:value-of select="namePart[@type='family']"/></span>,
					</xsl:otherwise>
				</xsl:choose>
			
			<span id="givenName">
				<xsl:value-of select="namePart[@type='given']"/>
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
			<span id="title"><xsl:value-of select="title"/></span>.
			<xsl:if test="subTitle">
				<span id="subTitle"><xsl:value-of select="subTitle"/></span>.
			</xsl:if>
		</i>
		<xsl:if test="../originInfo/copyrightDate">(<span id="copyrightDate"><xsl:value-of select="../originInfo/copyrightDate"/></span>) 
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