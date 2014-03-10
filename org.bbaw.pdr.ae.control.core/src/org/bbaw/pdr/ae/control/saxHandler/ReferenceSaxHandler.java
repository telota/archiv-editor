/**
 * This file is part of Archiv-Editor.
 * 
 * The software Archiv-Editor serves as a client user interface for working with
 * the Person Data Repository. See: pdr.bbaw.de
 * 
 * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
 * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
 * www.bbaw.de
 * 
 * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
 * of Sciences and Humanities
 * 
 * The software Archiv-Editor was developed by @author: Christoph Plutte.
 * 
 * Archiv-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Archiv-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Archiv-Editor.  
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.control.saxHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.AccessCondition;
import org.bbaw.pdr.ae.model.DetailMods;
import org.bbaw.pdr.ae.model.ExtendMods;
import org.bbaw.pdr.ae.model.Genre;
import org.bbaw.pdr.ae.model.IdentifierMods;
import org.bbaw.pdr.ae.model.LocationMods;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.NamePart;
import org.bbaw.pdr.ae.model.Note;
import org.bbaw.pdr.ae.model.OriginInfo;
import org.bbaw.pdr.ae.model.PartMods;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.RoleMods;
import org.bbaw.pdr.ae.model.TimeSpan;
import org.bbaw.pdr.ae.model.TitleInfo;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class ReferenceSaxHandler.
 * @author Christoph Plutte
 */
public class ReferenceSaxHandler extends DefaultHandler // implements
// ContentHandler
{

	/** The admin date format. */
	private SimpleDateFormat _adminDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	/** instance of PDRObjectDisplayNameProcessor. */
	// private PDRObjectDisplayNameProcessor pdrDisplayNameProc = new
	// PDRObjectDisplayNameProcessor();
	private HashMap<PdrId, ReferenceMods> _refs = new HashMap<PdrId, ReferenceMods>();

	/** The ref. */
	private ReferenceMods _ref = null;

	/** The title info. */
	private TitleInfo _titleInfo = null;

	/** The names mods. */
	private Vector<NameMods> _namesMods = null;

	/** The name mods. */
	private NameMods _nameMods = null;

	/** The name parts. */
	private Vector<NamePart> _nameParts = null;

	/** The name part. */
	private NamePart _namePart = null;

	/** The role mods. */
	private RoleMods _roleMods = null;

	/** The origin info. */
	private OriginInfo _originInfo = null;

	/** The time span. */
	private TimeSpan _timeSpan = null;

	/** The identifiers mods. */
	private Vector<IdentifierMods> _identifiersMods = null;

	/** The identifier mods. */
	private IdentifierMods _identifierMods = null;

	/** The location. */
	private LocationMods _location = null;

	/** The record. */
	private Record _record = new Record();

	/** The revision. */
	private Revision _revision = null;

	/** The genre. */
	private Genre _genre = null;

	/** The access condition. */
	private AccessCondition _accessCondition = null;

	/** The related item. */
	private RelatedItem _relatedItem = null;

	/** The related items. */
	private Vector<RelatedItem> _relatedItems = null;

	/** The part. */
	private PartMods _part = null;

	/** The detail. */
	private DetailMods _detail = null;

	/** The details. */
	private Vector<DetailMods> _details = null;

	/** The extend. */
	private ExtendMods _extend = null;

	/** The extends mods. */
	private Vector<ExtendMods> _extendsMods = null;

	/** The date rel item. */
	private PdrDate _dateRelItem = null;

	/** The date rel items. */
	private Vector<PdrDate> _dateRelItems = null;

	/** The note. */
	private Note _note = null;

	/** The t info. */
	private boolean _tInfo = false;

	/** The ti. */
	private boolean _ti = false;

	/** The sub ti. */
	private boolean _subTi = false;

	/** The part name. */
	private boolean _partName = false;

	/** The t number. */
	private boolean _tNumber = false;

	/** The name. */
	private boolean _name = false;

	/** The n part. */
	private boolean _nPart = false;

	/** The role. */
	private boolean _role = false;

	/** The aff. */
	private boolean _aff = false;

	/** The desc. */
	private boolean _desc = false;

	/** The genre b. */
	private boolean _genreB = false;

	/** The d creat. */
	private boolean _dCreat = false;

	/** The d issued. */
	private boolean _dIssued = false;

	/** The d cap. */
	private boolean _dCap = false;

	/** The d copy r. */
	private boolean _dCopyR = false;

	/** The timespan. */
	private boolean _timespan = false;

	/** The time start. */
	private boolean _timeStart = false;

	/** The time end. */
	private boolean _timeEnd = false;

	/** The pub. */
	private boolean _pub = false;

	/** The place. */
	private boolean _place = false;

	/** The ed. */
	private boolean _ed = false;

	/** The note b. */
	private boolean _noteB = false;

	/** The ident. */
	private boolean _ident = false;

	/** The url. */
	private boolean _url = false;

	/** The phys loc. */
	private boolean _physLoc = false;

	/** The shelf. */
	private boolean _shelf = false;

	/** The acc. */
	private boolean _acc = false;

	/** The rel ipart. */
	private boolean _relIpart = false;

	/** The rel iend. */
	private boolean _relIend = false;

	/** The rel istart. */
	private boolean _relIstart = false;

	/** The rel idate. */
	private boolean _relIdate = false;

	/** The rel inumber. */
	private boolean _relInumber = false;

	/** The rel icaption. */
	private boolean _relIcaption = false;

	/** The rel series. */
	private boolean _relSeries = false;

	/** The cre date. */
	private boolean _creDate = false;

	/** The cha date. */
	private boolean _chaDate = false;

	/** The rec source. */
	private boolean _recSource = false;

	/** The template. */
	private boolean _template = false;

	/** The docu. */
	private boolean _docu = false;

	/** The docpart. */
	private boolean _docpart = false;

	/** The image. */
	private boolean _image = false;

	/** The lang. */
	private String _lang = "";

	/** The ref templates. */
	private HashMap<String, ReferenceModsTemplate> _refTemplates = new HashMap<String, ReferenceModsTemplate>();

	/** The ref template. */
	private ReferenceModsTemplate _refTemplate = null;

	/** The _result object. */
	private Object _resultObject;

	@Override
	public final void characters(final char[] ch, final int start, final int length) throws SAXException
	{
		if (_ti)
		{
			_titleInfo.setTitle(new String(ch, start, length));
			_ti = false;
		}
		if (_subTi)
		{
			_titleInfo.setSubTitle(new String(ch, start, length));
			_subTi = false;
		}
		else if (_image)
		{
			_refTemplate.setImageString(new String(ch, start, length));
			_image = false;
		}
		else if (_docu && _docpart)
		{
			_refTemplate.getDocumentation().put(_lang, new String(ch, start, length));
			_docpart = false;
		}
		else if (_partName && _tInfo)
		{
			_titleInfo.setPartName(new String(ch, start, length));
			_partName = false;
		}
		else if (_tNumber && _tInfo)
		{
			_titleInfo.setPartNumber(new String(ch, start, length));
			_tNumber = false;
		}

		else if (_nPart && _name)
		{
			_namePart.setNamePart(new String(ch, start, length));
			_nPart = false;
		}
		else if (_role && _name)
		{
			_roleMods.setRoleTerm(new String(ch, start, length));
			_role = false;
		}
		else if (_aff && _name)
		{
			_nameMods.setAffiliation(new String(ch, start, length));
			_aff = false;
		}
		else if (_desc && _name)
		{
			_nameMods.setDescription(new String(ch, start, length));
			_desc = false;
		}
		else if (_genreB)
		{
			_genre.setGenre(new String(ch, start, length));
			_ref.setGenre(_genre);
			_genreB = false;
		}
		else if (_dCreat && !_timespan)
		{
			_originInfo.setDateCreated(new PdrDate(new String(ch, start, length)));
			_dCreat = false;
		}
		else if (_dCreat && _timespan)
		{
			if (_timeStart)
			{
				_timeSpan.setDateFrom(new PdrDate(new String(ch, start, length)));
				_timeStart = false;
			}
			else if (_timeEnd)
			{
				_timeSpan.setDateTo(new PdrDate(new String(ch, start, length)));
				_timeEnd = false;
				_originInfo.setDateCreatedTimespan(_timeSpan);
				_timespan = false;
			}
			_dCreat = false;
		}
		else if (_dIssued && !_timespan)
		{
			_originInfo.setDateIssued(new PdrDate(new String(ch, start, length)));
			_dIssued = false;
		}
		else if (_dIssued && _timespan)
		{
			if (_timeStart)
			{
				_timeSpan.setDateFrom(new PdrDate(new String(ch, start, length)));
				_timeStart = false;
			}
			else if (_timeEnd)
			{
				_timeSpan.setDateTo(new PdrDate(new String(ch, start, length)));
				_timeEnd = false;
				_originInfo.setDateIssuedTimespan(_timeSpan);
				_timespan = false;
			}
			_dIssued = false;
		}
		else if (_dCap && !_timespan)
		{
			_originInfo.setDateCaptured(new PdrDate(new String(ch, start, length)));
			_dCap = false;
		}
		else if (_dCap && _timespan)
		{
			if (_timeStart)
			{
				_timeSpan.setDateFrom(new PdrDate(new String(ch, start, length)));
				_timeStart = false;
			}
			else if (_timeEnd)
			{
				_timeSpan.setDateTo(new PdrDate(new String(ch, start, length)));
				_timeEnd = false;
				_originInfo.setDateCapturedTimespan(_timeSpan);
				_timespan = false;
			}
			_dCap = false;
		}
		else if (_dCopyR && !_timespan)
		{
			_originInfo.setCopyrightDate(new PdrDate(new String(ch, start, length)));
			_dCopyR = false;
		}
		else if (_dCopyR && _timespan)
		{
			if (_timeStart)
			{
				_timeSpan.setDateFrom(new PdrDate(new String(ch, start, length)));
				_timeStart = false;
			}
			else if (_timeEnd)
			{
				_timeSpan.setDateTo(new PdrDate(new String(ch, start, length)));
				_timeEnd = false;
				_originInfo.setCopyrightDateTimespan(_timeSpan);
				_timespan = false;
			}
			_dCopyR = false;
		}
		else if (_pub)
		{
			_originInfo.setPublisher(new String(ch, start, length));
			_pub = false;
		}
		else if (_place)
		{
			_originInfo.setPlaceTerm(new String(ch, start, length));
			_place = false;
		}
		else if (_ed)
		{
			_originInfo.setEdition(new String(ch, start, length));
			_ed = false;
		}
		else if (_noteB)
		{
			_note.setNote(new String(ch, start, length));
			_ref.setNote(_note);
			_noteB = false;
		}
		else if (_ident)
		{
			_identifierMods.setIdentifier(new String(ch, start, length));
			_ident = false;
		}
		else if (_url)
		{
			_location.setUrl(new String(ch, start, length));
			_url = false;
		}
		else if (_physLoc)
		{
			_location.setPhysicalLocation(new String(ch, start, length));
			_physLoc = false;
		}
		else if (_shelf)
		{
			_location.setShelfLocator(new String(ch, start, length));
			_shelf = false;
		}
		else if (_acc)
		{
			_accessCondition.setAccessCondition(new String(ch, start, length));
			_ref.setAccessCondition(_accessCondition);
			_acc = false;
		}
		else if (_relIcaption)
		{
			_detail.setCaption(new String(ch, start, length));
			_relIcaption = false;
		}
		else if (_relIdate)
		{
			_dateRelItem = new PdrDate(new String(ch, start, length));
			_relIdate = false;
		}
		else if (_relIend)
		{
			_extend.setEnd(new String(ch, start, length));
			_relIend = false;
		}
		else if (_relInumber)
		{
			_detail.setNumber(new String(ch, start, length));
			_relInumber = false;
		}
		else if (_relIstart)
		{
			_extend.setStart(new String(ch, start, length));
			_relIstart = false;
		}
		else if (_creDate && !_template)
		{
			_creDate = false;
			try
			{
				_revision.setTimeStamp(_adminDateFormat.parse(new String(ch, start, length)));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}

		}
		else if (_recSource && !_template)
		{
			_revision.setAuthority(new PdrId(new String(ch, start, length)));
			_recSource = false;
		}
		else if (_chaDate && !_template)
		{
			_chaDate = false;
			try
			{
				_revision.setTimeStamp(_adminDateFormat.parse(new String(ch, start, length)));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}

		}
	}

	@Override
	public final void endDocument() throws SAXException
	{
		if (_refTemplate != null)
		{
			setResultObject(_refTemplate);
		}
		else if (_ref != null)
		{
			setResultObject(_ref);
		}
	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException
	{
		if (localName.equals("documentation") || qName.equals("documentation")
				|| localName.equals("mods:documentation") || qName.equals("mods:documentation"))
		{
			_docu = false;
		}
		else if (!_relSeries
				&& (localName.equals("titleInfo") || qName.equals("titleInfo") || localName.equals("mods:titleInfo") || qName
						.equals("mods:titleInfo")))
		{
			_ref.setTitleInfo(_titleInfo);
			_tInfo = false;
		}
		else if (_relSeries
				&& (localName.equals("titleInfo") || qName.equals("titleInfo") || localName.equals("mods:titleInfo") || qName
						.equals("mods:titleInfo")))
		{
			_tInfo = false;
		}
		else if (localName.equals("title") || qName.equals("title") || localName.equals("mods:title")
				|| qName.equals("mods:title"))
		{
			_ti = false;
		}
		else if (localName.equals("subTitle") || qName.equals("subTitle") || localName.equals("mods:subTitle")
				|| qName.equals("mods:subTitle"))
		{
			_subTi = false;
		}
		else if (localName.equals("partName") || qName.equals("partName") || localName.equals("mods:partName")
				|| qName.equals("mods:partName"))
		{
			_partName = false;
		}
		else if (localName.equals("partNumber") || qName.equals("partNumber") || localName.equals("mods:partNumber")
				|| qName.equals("mods:partNumber"))
		{
			_tNumber = false;
		}

		else if (localName.equals("name") || qName.equals("name") || localName.equals("mods:name")
				|| qName.equals("mods:name"))
		{
			_nameMods.setNameParts(_nameParts);
			_namesMods.add(_nameMods);
			_nameParts = null;
			_name = false;
		}
		else if (localName.equals("namePart") || qName.equals("namePart") || localName.equals("mods:namePart")
				|| qName.equals("mods:namePart"))
		{
			_nameParts.add(_namePart);
			_nPart = false;
		}
		else if (localName.equals("role") || qName.equals("role") || localName.equals("mods:role")
				|| qName.equals("mods:role"))
		{
			_nameMods.setRoleMods(_roleMods);
			_role = false;
		}
		else if (localName.equals("affiliation") || qName.equals("affiliation") || localName.equals("mods:affiliation")
				|| qName.equals("mods:affiliation"))
		{
			_aff = false;
		}
		else if (localName.equals("description") || qName.equals("description") || localName.equals("mods:description")
				|| qName.equals("mods:description"))
		{
			_desc = false;
		}
		else if (localName.equals("genre") || qName.equals("genre") || localName.equals("mods:genre")
				|| qName.equals("mods:genre"))
		{
			_genreB = false;
		}
		else if (localName.equals("originInfo") || qName.equals("originInfo") || localName.equals("mods:originInfo")
				|| qName.equals("mods:originInfo"))
		{
			_ref.setOriginInfo(_originInfo);

		}
		else if (localName.equals("dateCreated") || qName.equals("dateCreated") || localName.equals("mods:dateCreated")
				|| qName.equals("mods:dateCreated"))
		{
			_dCreat = false;
			if (_timeStart)
			{
				_timeStart = false;
			}
			if (_timeEnd)
			{
				_timeEnd = false;
			}
		}
		else if (localName.equals("dateIssued") || qName.equals("dateIssued") || localName.equals("mods:dateIssued")
				|| qName.equals("mods:dateIssued"))
		{
			_dIssued = false;
			if (_timeStart)
			{
				_timeStart = false;
			}
			if (_timeEnd)
			{
				_timeEnd = false;
			}
		}
		else if (localName.equals("dateCaptured") || qName.equals("dateCaptured")
				|| localName.equals("mods:dateCaptured") || qName.equals("mods:dateCaptured"))
		{
			_dCap = false;
			if (_timeStart)
			{
				_timeStart = false;
			}
			if (_timeEnd)
			{
				_timeEnd = false;
			}
		}
		else if (localName.equals("copyrightDate") || qName.equals("copyrightDate")
				|| localName.equals("mods:copyrightDate") || qName.equals("mods:copyrightDate"))
		{
			_dCopyR = false;
			if (_timeStart)
			{
				_timeStart = false;
			}
			if (_timeEnd)
			{
				_timeEnd = false;
			}
		}
		else if (localName.equals("publisher") || qName.equals("publisher") || localName.equals("mods:publisher")
				|| qName.equals("mods:publisher"))
		{
			_pub = false;
		}
		else if (localName.equals("place") || qName.equals("place") || localName.equals("mods:place")
				|| qName.equals("mods:place"))
		{
			_place = false;
		}
		else if (localName.equals("edition") || qName.equals("edition") || localName.equals("mods:edition")
				|| qName.equals("mods:edition"))
		{
			_ed = false;
		}
		else if (localName.equals("identifier") || qName.equals("identifier") || localName.equals("mods:identifier")
				|| qName.equals("mods:identifier"))
		{
			_identifiersMods.add(_identifierMods);
			_ident = false;
		}
		else if (localName.equals("note") || qName.equals("note") || localName.equals("mods:note")
				|| qName.equals("mods:note"))
		{
			_noteB = false;
		}
		else if (localName.equals("url") || qName.equals("url") || localName.equals("mods:url")
				|| qName.equals("mods:url"))
		{
			_url = false;
		}
		else if (localName.equals("physicalLocation") || qName.equals("physicalLocation")
				|| localName.equals("mods:physicalLocation") || qName.equals("mods:physicalLocation"))
		{
			_physLoc = false;
		}
		else if (localName.equals("shelfLocator") || qName.equals("shelfLocator")
				|| localName.equals("mods:shelfLocator") || qName.equals("mods:shelfLocator"))
		{
			_shelf = false;
		}
		else if (localName.equals("location") || qName.equals("location") || localName.equals("mods:location")
				|| qName.equals("mods:location"))
		{
			_ref.setLocation(_location);
		}
		else if (_relSeries
				&& (localName.equals("relatedItem") || qName.equals("relatedItem")
						|| localName.equals("mods:relatedItem") || qName.equals("mods:relatedItem")))
		{
			if (_titleInfo != null)
			{
				_ref.setSeriesTitleInfo(_titleInfo);
			}
			_relSeries = false;
		}
		else if (localName.equals("part") || qName.equals("part") || localName.equals("mods:part")
				|| qName.equals("mods:part"))
		{
			_relIpart = false;
			_part.setDates(_dateRelItems);
			_part.setDetails(_details);
			_part.setExtendsMods(_extendsMods);
			_relatedItem.setPart(_part);
		}
		else if (_relIpart && localName.equals("detail") || qName.equals("detail") || _relIpart
				&& localName.equals("mods:detail") || qName.equals("mods:detail"))
		{
			_details.add(_detail);
		}
		else if (_relIpart
				&& (localName.equals("extent") || qName.equals("extent") || localName.equals("mods:extent")
						|| qName.equals("mods:extent") || localName.equals("extend") || qName.equals("extend")
						|| localName.equals("mods:extend") || qName
						.equals("mods:extend")))
		{
			_extendsMods.add(_extend);
		}
		else if (_relIpart
				&& (localName.equals("number") || qName.equals("number") || localName.equals("mods:number") || qName
						.equals("mods:number")))
		{
			_relInumber = false;
		}
		else if (_relIpart
				&& (localName.equals("start") || qName.equals("start") || localName.equals("mods:start") || qName
						.equals("mods:start")))
		{
			_relIstart = false;
		}
		else if (_relIpart
				&& (localName.equals("end") || qName.equals("end") || localName.equals("mods:end") || qName
						.equals("mods:end")))
		{
			_relIend = false;
		}
		else if (_relIpart
				&& (localName.equals("caption") || qName.equals("caption") || localName.equals("mods:caption") || qName
						.equals("mods:caption")))
		{
			_relIcaption = false;
		}
		else if (_relIpart
				&& (localName.equals("date") || qName.equals("date") || localName.equals("mods:date") || qName
						.equals("mods:date")))
		{
			_dateRelItems.add(_dateRelItem);
			_relIdate = false;
		}
		else if (localName.equals("recordCreationDate") || qName.equals("recordCreationDate")
				|| localName.equals("mods:recordCreationDate") || qName.equals("mods:recordCreationDate"))
		{
			_creDate = false;
		}
		else if (localName.equals("recordContentSource") || qName.equals("recordContentSource")
				|| localName.equals("mods:recordContentSource") || qName.equals("mods:recordContentSource"))
		{

			_chaDate = false;
		}
		else if (localName.equals("recordChangeDate") || qName.equals("recordChangeDate")
				|| localName.equals("mods:recordChangeDate") || qName.equals("mods:recordChangeDate"))
		{
			_chaDate = false;
		}
		else if (localName.equals("recordInfo") || qName.equals("recordInfo") || localName.equals("mods:recordInfo")
				|| qName.equals("mods:recordInfo"))
		{
			_record.getRevisions().add(_revision);
		}
		else if (localName.equals("r"))
		{
			_ref.setRelatedItems(_relatedItems);
			_ref.setNameMods(_namesMods);
			_namesMods = null;
			_ref.setIdentifiersMods(_identifiersMods);
			_identifiersMods = null;
			_ref.setRecord(_record);
			// pdrDisplayNameProc.processDisplayName(ref);
			// pdrDisplayNameProc.processDisplayNameLong(ref);
			_refs.put(_ref.getPdrId(), _ref);
		}
		else if (localName.equals("result") || qName.equals("result"))
		{
			// for (ReferenceMods r : refs.values())
			// {
			// // if (r.getRelatedItems() != null && r.getRelatedItems().size()
			// > 0
			// // && r.getRelatedItems().firstElement().getId() != null)
			// // {
			// // String id = r.getRelatedItems().firstElement().getId();
			// //// ReferenceMods host = refs.get(id);
			// //// if
			// (!host.getHostedReferences().contains(r.getPdrId().toString()))
			// //// {
			// //// host.getHostedReferences().add(r.getPdrId().toString());
			// //// }
			// // }
			// }
			// System.out.println("im saxhandler number of refs " +
			// refs.size());
			setResultObject(_refs);
			// UIJob job = new UIJob("Load Result") {
			// @Override
			// public IStatus runInUIThread(IProgressMonitor monitor) {
			// _facade.setAllReferences(refs);
			// return Status.OK_STATUS;
			// }
			// };
			// job.setUser(true);
			// job.schedule();

		}
		else if (localName.equals("template") || qName.equals("template"))
		{
			setResultObject(_ref);
		}
		else if (localName.equals("refTemplate") || qName.equals("refTemplate"))
		{
			_ref.setRelatedItems(_relatedItems);
			_ref.setNameMods(_namesMods);
			_namesMods = null;
			_ref.setIdentifiersMods(_identifiersMods);
			_identifiersMods = null;
			_ref.setRecord(_record);
			_refTemplate.setRefTemplate(_ref);
			_refTemplates.put(_refTemplate.getValue(), _refTemplate);
		}
		else if (localName.equals("resultTemplates") || qName.equals("resultTemplates"))
		{
			// _facade.setReferenceModsTemplates(refTemplates);
			setResultObject(_refTemplates);

		}

	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * get result object.
	 * @return result object.
	 */
	public final Object getResultObject()
	{
		return _resultObject;
	}

	@Override
	public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException
	{

	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException
	{

	}

	@Override
	public void setDocumentLocator(final Locator locator)
	{

	}

	/**
	 * set result object.
	 * @param resultObject object.
	 */
	private void setResultObject(final Object resultObject)
	{
		this._resultObject = resultObject;
	}

	@Override
	public void skippedEntity(final String name) throws SAXException
	{

	}

	@Override
	public void startDocument() throws SAXException
	{

	}

	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException
	{
		if (localName.equals("template") || qName.equals("template"))
		{
			_template = true;
			_refTemplate = new ReferenceModsTemplate();
		}
		else if (localName.equals("refTemplate") || qName.equals("refTemplate"))
		{
			if (_refTemplate == null)
			{
				_template = true;
				_refTemplate = new ReferenceModsTemplate();
			}
			// System.out.println("refTemplate");
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("label"))
				{
					_refTemplate.setLabel(atts.getValue(i));
				}
				else if (atts.getQName(i).equals("genre"))
				{
					_refTemplate.setValue(atts.getValue(i));
				}
				else if (atts.getQName(i).equals("ignore"))
				{
					_refTemplate.setIgnore(atts.getValue(i).equals("true"));
				}
			}
			_relatedItems = new Vector<RelatedItem>(1);
		}
		// usage
		else if (localName.equals("image") || qName.equals("image"))
		{
			_image = true;
		}
		else if (localName.equals("documentation") || qName.equals("documentation"))
		{
			_docu = true;
			_refTemplate.setDocumentation(new HashMap<String, String>());
		}
		else if (localName.equals("docPart") || qName.equals("docPart"))
		{
			_docpart = true;

			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("xml:lang"))
				{
					_lang = atts.getValue(i);
				}
			}
		}

		if ((localName.equals("r") || qName.equals("r")) && !_template)
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("id"))
				{
					_ref = new ReferenceMods(atts.getValue(i));
				}
			}
			_relatedItems = new Vector<RelatedItem>(1);
		}
		else if (localName.equals("r") && _template)
		{
			// try {
			// ref = new ReferenceMods(new
			// PdrIdService().getNewId("reference"));
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (XQException e) {
			// e.printStackTrace();
			// } catch (XMLStreamException e) {
			// e.printStackTrace();
			// }
			_ref = new ReferenceMods(new Genre());
		}
		else if ((localName.equals("mods") || qName.equals("mods") || localName.equals("mods:mods") || qName
				.equals("mods:mods")) && _template)
		{
			_ref = new ReferenceMods(new Genre());
		}

		else if (localName.equals("titleInfo") || qName.equals("titleInfo") || localName.equals("mods:titleInfo")
				|| qName.equals("mods:titleInfo"))
		{
			_titleInfo = new TitleInfo();
			_tInfo = true;
		}
		else if (localName.equals("title") || qName.equals("title") || localName.equals("mods:title")
				|| qName.equals("mods:title"))
		{
			_ti = true;
		}
		else if (localName.equals("subTitle") || qName.equals("subTitle") || localName.equals("mods:subTitle")
				|| qName.equals("mods:subTitle"))
		{
			_subTi = true;
		}
		else if (localName.equals("partName") || qName.equals("partName") || localName.equals("mods:partName")
				|| qName.equals("mods:partName"))
		{
			_partName = true;
			_ti = false;
		}
		else if (localName.equals("partNumber") || qName.equals("partNumber") || localName.equals("mods:partNumber")
				|| qName.equals("mods:partNumber"))
		{
			_tNumber = true;
			_partName = false;
		}

		else if (localName.equals("name") || qName.equals("name") || localName.equals("mods:name")
				|| qName.equals("mods:name"))
		{
			_tInfo = false;
			_name = true;
			_tNumber = false;
			if (_namesMods == null)
			{
				_namesMods = new Vector<NameMods>();
			}
			_nameMods = new NameMods();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_nameMods.setType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("namePart") || qName.equals("namePart") || localName.equals("mods:namePart")
				|| qName.equals("mods:namePart"))
		{
			_nPart = true;
			if (_nameParts == null)
			{
				_nameParts = new Vector<NamePart>();
			}
			_namePart = new NamePart();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_namePart.setType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("role") || qName.equals("role") || localName.equals("mods:role")
				|| qName.equals("mods:role"))
		{
			_roleMods = new RoleMods();
			_role = true;

			_nPart = false;
		}
		else if (localName.equals("roleTerm") || qName.equals("roleTerm") || localName.equals("mods:roleTerm")
				|| qName.equals("mods:roleTerm"))
		{
			_role = true;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_roleMods.setType(atts.getValue(i));
				}
				else if (atts.getQName(i).equals("authority"))
				{
					_roleMods.setAuthority(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("affiliation") || qName.equals("affiliation") || localName.equals("mods:affiliation")
				|| qName.equals("mods:affiliation"))
		{
			_aff = true;
		}
		else if (localName.equals("description") || qName.equals("description") || localName.equals("mods:description")
				|| qName.equals("mods:description"))
		{
			_desc = true;
			_aff = false;
		}
		else if (localName.equals("genre") || qName.equals("genre") || localName.equals("mods:genre")
				|| qName.equals("mods:genre"))
		{
			_genreB = true;
			_desc = false;
			_name = false;
			_role = false;
			_genre = new Genre();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("authority"))
				{
					_genre.setAuthority(atts.getValue(i));
				}
			}
		}

		else if (localName.equals("originInfo") || qName.equals("originInfo") || localName.equals("mods:originInfo")
				|| qName.equals("mods:originInfo"))
		{
			_originInfo = new OriginInfo();
			_genreB = false;
		}
		else if (localName.equals("dateCreated") || qName.equals("dateCreated") || localName.equals("mods:dateCreated")
				|| qName.equals("mods:dateCreated"))
		{
			_dCreat = true;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("point"))
				{
					if (atts.getValue(i).equals("start"))
					{
						_timeStart = true;
						_timespan = true;
					}
					else if (atts.getValue(i).equals("end"))
					{
						_timeEnd = true;
						_timespan = true;
					}
				}
			}
			if (_timespan)
			{
				_timeSpan = new TimeSpan();
			}

		}
		else if (localName.equals("dateIssued") || qName.equals("dateIssued") || localName.equals("mods:dateIssued")
				|| qName.equals("mods:dateIssued"))
		{
			_dIssued = true;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("point"))
				{
					if (atts.getValue(i).equals("start"))
					{
						_timeStart = true;
						_timespan = true;
					}
					else if (atts.getValue(i).equals("end"))
					{
						_timeEnd = true;
						_timespan = true;
					}
				}
			}
			if (_timespan)
			{
				_timeSpan = new TimeSpan();
			}

		}
		else if (localName.equals("dateCaptured") || qName.equals("dateCaptured")
				|| localName.equals("mods:dateCaptured") || qName.equals("mods:dateCaptured"))
		{
			_dCap = true;
			_dCreat = false;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("point"))
				{
					if (atts.getValue(i).equals("start"))
					{
						_timeStart = true;
						_timespan = true;
					}
					else if (atts.getValue(i).equals("end"))
					{
						_timeEnd = true;
						_timespan = true;
					}
				}
			}
			if (_timespan)
			{
				_timeSpan = new TimeSpan();
			}
		}
		else if (localName.equals("copyrightDate") || qName.equals("copyrightDate")
				|| localName.equals("mods:copyrightDate") || qName.equals("mods:copyrightDate"))
		{
			_dCopyR = true;
			_dCap = false;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("point"))
				{
					if (atts.getValue(i).equals("start"))
					{
						_timeStart = true;
						_timespan = true;
					}
					else if (atts.getValue(i).equals("end"))
					{
						_timeEnd = true;
						_timespan = true;
					}
				}
			}
			if (_timespan)
			{
				_timeSpan = new TimeSpan();
			}
		}
		else if (localName.equals("publisher") || qName.equals("publisher") || localName.equals("mods:publisher")
				|| qName.equals("mods:publisher"))
		{
			_pub = true;
			_dCopyR = false;
		}
		else if (localName.equals("placeTerm") || qName.equals("placeTerm") || localName.equals("mods:placeTerm")
				|| qName.equals("mods:placeTerm"))
		{
			_place = true;
			_pub = false;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_originInfo.setPlaceType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("edition") || qName.equals("edition") || localName.equals("mods:edition")
				|| qName.equals("mods:edition"))
		{
			_ed = true;
			_place = false;
		}
		else if (localName.equals("note") || qName.equals("note") || localName.equals("mods:note")
				|| qName.equals("mods:note"))
		{
			_noteB = true;
			_ed = false;
			_note = new Note();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_note.setType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("identifier") || qName.equals("identifier") || localName.equals("mods:identifier")
				|| qName.equals("mods:identifier"))
		{
			_ident = true;
			_noteB = false;
			_identifierMods = new IdentifierMods();
			if (_identifiersMods == null)
			{
				_identifiersMods = new Vector<IdentifierMods>();
			}
			_identifierMods = new IdentifierMods();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_identifierMods.setType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("location") || qName.equals("location") || localName.equals("mods:location")
				|| qName.equals("mods:location"))
		{
			_location = new LocationMods();
			_ident = false;
		}
		else if (localName.equals("url") || qName.equals("url") || localName.equals("mods:url")
				|| qName.equals("mods:url"))
		{
			_url = true;

		}
		else if (localName.equals("physicalLocation") || qName.equals("physicalLocation")
				|| localName.equals("mods:physicalLocation") || qName.equals("mods:physicalLocation"))
		{
			_physLoc = true;
			_url = false;
		}
		else if (localName.equals("shelfLocator") || qName.equals("shelfLocator")
				|| localName.equals("mods:shelfLocator") || qName.equals("mods:shelfLocator"))
		{
			_shelf = true;
			_physLoc = false;
		}
		else if (localName.equals("accessCondition") || qName.equals("accessCondition")
				|| localName.equals("mods:accessCondition") || qName.equals("mods:accessCondition"))
		{
			_acc = true;
			_shelf = false;
			_accessCondition = new AccessCondition();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_accessCondition.setType(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("relatedItem") || qName.equals("relatedItem") || localName.equals("mods:relatedItem")
				|| qName.equals("mods:relatedItem"))
		{
			// System.out.println("relatedItems");
			_acc = false;
			_shelf = false;
			_relatedItem = new RelatedItem();
			for (int i = 0; i < atts.getLength(); i++)
			{

				if (atts.getQName(i).equals("type"))
				{
					if (atts.getValue(i).equals("series"))
					{
						_relSeries = true;
					}
					else
					{
						_relatedItem.setType(atts.getValue(i));
					}
				}
				if (atts.getQName(i).equals("ID"))
				{
					_relatedItem.setId(atts.getValue(i));
					// System.out.println("relatedItem.getId " +
					// relatedItem.getId());
				}
			}
			if (!_relSeries)
			{
				_relatedItems.add(_relatedItem);
			}
		}
		else if (localName.equals("part") || qName.equals("part") || localName.equals("mods:part")
				|| qName.equals("mods:part"))
		{
			_part = new PartMods();
			_relIpart = true;
			_details = new Vector<DetailMods>(1);
			_extendsMods = new Vector<ExtendMods>(2);
			_dateRelItems = new Vector<PdrDate>(1);
		}
		else if (_relIpart
				&& (localName.equals("detail") || qName.equals("detail") || localName.equals("mods:detail") || qName
						.equals("mods:detail")))
		{
			_detail = new DetailMods();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_detail.setType(atts.getValue(i));
				}
			}
		}
		else if (_relIpart
				&& (localName.equals("extent") || qName.equals("extent") || localName.equals("mods:extent")
						|| qName.equals("mods:extent") || localName.equals("extend") || qName.equals("extend")
						|| localName.equals("mods:extend") || qName
						.equals("mods:extend")))
		{
			_extend = new ExtendMods();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("unit"))
				{
					_extend.setUnit(atts.getValue(i));
				}
			}
		}
		else if (_relIpart
				&& (localName.equals("number") || qName.equals("number") || localName.equals("mods:number") || qName
						.equals("mods:number")))
		{
			_relInumber = true;
		}
		else if (_relIpart
				&& (localName.equals("start") || qName.equals("start") || localName.equals("mods:start") || qName
						.equals("mods:start")))
		{
			_relIstart = true;
		}
		else if (_relIpart
				&& (localName.equals("end") || qName.equals("end") || localName.equals("mods:end") || qName
						.equals("mods:end")))
		{
			_relIend = true;
		}
		else if (_relIpart
				&& (localName.equals("caption") || qName.equals("caption") || localName.equals("mods:caption") || qName
						.equals("mods:caption")))
		{
			_relIcaption = true;
		}
		else if (_relIpart
				&& (localName.equals("date") || qName.equals("date") || localName.equals("mods:date") || qName
						.equals("mods:date")))
		{
			_relIdate = true;
		}
		else if (localName.equals("recordInfo") || qName.equals("recordInfo") || localName.equals("mods:recordInfo")
				|| qName.equals("mods:recordInfo"))
		{
			_record = new Record();
			_revision = new Revision();
			_acc = false;
		}
		else if (localName.equals("recordCreationDate") || qName.equals("recordCreationDate")
				|| localName.equals("mods:recordCreationDate") || qName.equals("mods:recordCreationDate"))
		{
			_creDate = true;

		}
		else if (localName.equals("recordContentSource") || qName.equals("recordContentSource")
				|| localName.equals("mods:recordContentSource") || qName.equals("mods:recordContentSource"))
		{
			_recSource = true;
			_creDate = false;
		}
		else if (localName.equals("recordChangeDate") || qName.equals("recordChangeDate")
				|| localName.equals("mods:recordChangeDate") || qName.equals("mods:recordChangeDate"))
		{
			_chaDate = true;
		}

	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}
}
