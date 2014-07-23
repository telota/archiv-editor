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
package org.bbaw.pdr.ae.control.saxHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.core.PDRObjectDisplayNameProcessor;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.internal.Activator;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Place;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticDim;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.SpatialDim;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class AspectSaxHandler.
 * @author Christoph Plutte
 */
public class AspectSaxHandler extends DefaultHandler
{

	/** The admin data format. */
	private SimpleDateFormat _adminDataFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();
	/** instance of PDRObjectDisplayNameProcessor. */
	private PDRObjectDisplayNameProcessor _pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();

	// /** The pdr object. */
	// private PdrObject _pdrObject;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;

	/** The aspects. */
	private HashMap<PdrId, Aspect> _aspects = new HashMap<PdrId, Aspect>();

	// /** The aspect ids. */
	// private Vector<PdrId> _aspectIds = new Vector<PdrId>();

	/** The aspect. */
	private Aspect _aspect;

	/** The revision. */
	private Revision _revision = null;

	/** The record. */
	private Record _record = null;

	// time dimension
	/** The time dim. */
	private TimeDim _timeDim = null;

	/** The time stm. */
	private TimeStm _timeStm = null;

	/** The time. */
	private Time _time = null;

	// spacial dimension
	/** The spatial dim. */
	private SpatialDim _spatialDim = null;

	/** The spatial stm. */
	private SpatialStm _spatialStm = null;

	/** The place. */
	private Place _place = null;

	// relation dimension
	/** The relation dim. */
	private RelationDim _relationDim = null;

	/** The relation stm. */
	private RelationStm _relationStm = null;

	/** The relation. */
	private Relation _relation = null;

	// semantic dimension
	/** The semantic dim. */
	private SemanticDim _semanticDim = null;

	/** The semantic stm. */
	private SemanticStm _semanticStm = null;


	// style range
	/** The tr. */
	private TaggingRange _tr = null;

	/** string size. */
	private static final int STRING_SIZE = 1000;
	/** The sb. */
	private StringBuilder _sb = new StringBuilder(STRING_SIZE);

	/** The range list. */
	private LinkedList<TaggingRange> _rangeList = null;

	/** The str. */
	private String _str;



	// validation
	/** The validation. */
	private Validation _validation = null;

	/** The validation stm. */
	private ValidationStm _validationStm = null;

	/** The reference. */
	private Reference _reference = null;

	/** The revisor b. */
	private boolean _revisorB = false;

	/** The time b. */
	private boolean _timeB = false;

	/** The place b. */
	private boolean _placeB = false;

	/** The relation b. */
	private boolean _relationB = false;

	/** The semantic b. */
	private boolean _semanticB = false;

	/** The reference b. */
	private boolean _referenceB = false;

	/** The noti b. */
	private boolean _notiB = false;

	/** The noti text b. */
	private boolean _notiTextB = false;

	/** The pers name b. */
	private boolean _persNameB = false;

	/** The org name b. */
	private boolean _orgNameB = false;

	/** The place name b. */
	private boolean _placeNameB = false;

	/** The name b. */
	private boolean _nameB = false;

	/** The date b. */
	private boolean _dateB = false;

	/** The val stm b. */
	private boolean _valStmB = false;

	/** The _result object. */
	private Object _resultObject;

	private IProgressMonitor _monitor;

	private PdrObject[] _pdrObjects;

	private PdrObject _pdrObject;

	private PdrId _aspectID;

	private int _counter;

	private boolean lb_start;

	// /**
	// * Instantiates a new aspect sax handler.
	// * @param pdrObject the pdr object
	// */
	// public AspectSaxHandler(final PdrObject pdrObject)
	// {
	// this._pdrObject = pdrObject;
	// if (this._pdrObject.getAspectIds() != null)
	// {
	// _aspectIds = _pdrObject.getAspectIds();
	// }
	// }

	public AspectSaxHandler(PdrObject pdrObject, IProgressMonitor monitor)
	{
		this._pdrObject = pdrObject;
		this._monitor = monitor;
		// if (this._pdrObject != null && this._pdrObject.getAspectIds() !=
		// null)
		// {
		// _aspectIds = _pdrObject.getAspectIds();
		// }
	}

	public AspectSaxHandler(PdrObject[] objects, IProgressMonitor monitor)
	{
		this._pdrObjects = objects;
		this._monitor = monitor;
	}

	/**
	 * @param ch chars
	 * @param start start
	 * @param len length of chars
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public final void characters(final char[] ch, final int start, final int len)
	{
		// System.out.println("Characters:\t\"" + new String(ch, start, len) +
		// "\"");
		_str = new String(ch, start, len);
		// record and revision
		if (_revisorB)
		{
			_revision.setRevisor(_str);
			_revisorB = false;
		}
		// time dimension
		else if (_timeB)
		{
			_time.setTimeStamp(new PdrDate(_str));
			_timeB = false;
		}
		// spatial dimension
		else if (_placeB)
		{
			_place.setPlaceName(_str);
			_placeB = false;
		}
		// relation dimension
		else if (_relationB)
		{
			_relation.setRelation(_str);
			_relationB = false;
		}
		// semantic dimension
		else if (_semanticB)
		{
			_semanticStm.setLabel(_str);
			_semanticB = false;
		}
		// notification
		else if (_notiB && _notiTextB)
		{
			addString2SB(_sb, _str, len, _tr);
			_notiTextB = false;
		}
		// persName
		else if (_persNameB)
		{
			addString2SB(_sb, _str, len, _tr);
			_tr.setTextValue(_str);
			_rangeList.add(_tr);
			_persNameB = false;
		}
		// orgName
		else if (_orgNameB)
		{
			addString2SB(_sb, _str, len, _tr);
			_tr.setTextValue(_str);
			_rangeList.add(_tr);
			_orgNameB = false;
		}
		// placeName
		else if (_placeNameB)
		{
			addString2SB(_sb, _str, len, _tr);
			_tr.setTextValue(_str);
			_rangeList.add(_tr);
			_placeNameB = false;
		}
		// name
		else if (_nameB)
		{
			addString2SB(_sb, _str, len, _tr);
			_tr.setTextValue(_str);
			_rangeList.add(_tr);
			_nameB = false;
		}
		// date
		else if (_dateB)
		{
			addString2SB(_sb, _str, len, _tr);
			_tr.setTextValue(_str);
			_rangeList.add(_tr);
			_dateB = false;
		}

		// valdiation reference
		else if (_referenceB)
		{
			PdrId id = new PdrId(_str);

			_reference.setSourceId(id);
			if (id != null)
			{
				PdrObject o = _facade.getReference(id);
				if (o != null)
				{
					o.addAspectId(_aspectID);
				}
			}
			_referenceB = false;
		}
		else if (_valStmB)
		{
			_validationStm.setInterpretation(_str);
			_valStmB = false;
		}
	}

	private void addString2SB(StringBuilder _sb2, String str, int len, TaggingRange _tr2)
	{
		if (_sb.toString().trim().length() == 0)
		{
			str = removeLeadingWhitespaces(str);
		}
		if (str.startsWith(",") || str.startsWith(".") || str.startsWith(";") || str.startsWith(":")
				|| str.startsWith("-") || str.startsWith("'") || str.startsWith("\"") || str.startsWith(")"))
		{
			_sb.append(str);
		}
		else if (_sb.toString().endsWith("\n"))
		{
			_sb.append(str);
			if (_tr != null && (_persNameB || _placeNameB || _orgNameB || _nameB || _dateB))
			{
				_tr.setLength(len);
			}
		}
		else if (_sb.toString().trim().length() > 0 && !_sb.toString().endsWith(" ") && !str.startsWith(" ")
				&& !_sb.toString().endsWith("(") && !_sb.toString().endsWith("\"") && !_sb.toString().endsWith("'"))
		{
			_sb.append(" ");
			_sb.append(str);
			if (_tr != null && (_persNameB || _placeNameB || _orgNameB || _nameB || _dateB))
			{
				_tr.setLength(len);
				_tr.setStart(_tr.getStart() + 1);
			}
		}
		else
		{
			_sb.append(str);
			if (_tr != null && (_persNameB || _placeNameB || _orgNameB || _nameB || _dateB))
			{
				_tr.setStart(_tr.getStart());
				_tr.setLength(len);
			}
		}
	}

	private String removeLeadingWhitespaces(String str)
	{
		return str.replaceAll("^\\s+", "");

	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public final void endDocument()
	{

	}

	/**
	 * @param u uri
	 * @param name localName
	 * @param qn QName
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public final void endElement(final String u, final String name, final String qn)
	{
		// System.out.println("End Element:\t\"" + name + "\"");
		if (name.equals("aspect") || name.equals("aodl:aspect") || qn.equals("aspect") || qn.equals("aodl:aspect"))
		{
			_pdrDisplayNameProc.processDisplayName(_aspect);
			_aspects.put(_aspectID, _aspect);
			setResultObject(_aspect);
		}
		else if (name.equals("record") || name.equals("aodl:record") || qn.equals("record") || qn.equals("aodl:record"))
		{
			_aspect.setRecord(_record);
		}
		else if (name.equals("revision") || name.equals("aodl:revision") || qn.equals("revision")
				|| qn.equals("aodl:revision"))
		{
			_revisorB = false;
			_record.getRevisions().add(_revision);
		}
		else if (name.equals("timeDim") || name.equals("aodl:timeDim") || qn.equals("timeDim")
				|| qn.equals("aodl:timeDim"))
		{
			_aspect.setTimeDim(_timeDim);
		}
		else if (name.equals("timeStm") || name.equals("aodl:timeStm") || qn.equals("timeStm")
				|| qn.equals("aodl:timeStm"))
		{
			_timeDim.getTimeStms().add(_timeStm);
		}
		else if (name.equals("time") || name.equals("aodl:time") || qn.equals("time") || qn.equals("aodl:time"))
		{
			_timeB = false;
			_timeStm.getTimes().add(_time);
		}
		else if (name.equals("spatialDim") || name.equals("aodl:spatialDim") || qn.equals("spatialDim")
				|| qn.equals("aodl:spatialDim"))
		{
			_aspect.setSpatialDim(_spatialDim);
		}
		else if (name.equals("spatialStm") || name.equals("aodl:spatialStm") || qn.equals("spatialStm")
				|| qn.equals("aodl:spatialStm"))
		{
			_spatialDim.getSpatialStms().add(_spatialStm);
		}
		else if (name.equals("place") || name.equals("aodl:place") || qn.equals("place") || qn.equals("aodl:place"))
		{
			_placeB = false;
			_spatialStm.getPlaces().add(_place);
		}
		else if (name.equals("relationDim") || name.equals("aodl:relationDim") || qn.equals("relationDim")
				|| qn.equals("aodl:relationDim"))
		{
			_aspect.setRelationDim(_relationDim);
		}
		else if (name.equals("relationStm") || name.equals("aodl:relationStm") || qn.equals("relationStm")
				|| qn.equals("aodl:relationStm"))
		{
			_relationDim.getRelationStms().add(_relationStm);
		}
		else if (name.equals("relation") || name.equals("aodl:relation") || qn.equals("relation")
				|| qn.equals("aodl:relation"))
		{
			_relationB = false;
			_relationStm.getRelations().add(_relation);

		}
		else if (name.equals("semanticDim") || name.equals("aodl:semanticDim") || qn.equals("semanticDim")
				|| qn.equals("aodl:semanticDim"))
		{
			_aspect.setSemanticDim(_semanticDim);
		}
		else if (name.equals("semanticStm") || name.equals("aodl:semanticStm") || qn.equals("semanticStm")
				|| qn.equals("aodl:semanticStm"))
		{
			_semanticB = false;
			_semanticDim.getSemanticStms().add(_semanticStm);
		}
		else if (name.equals("notification") || name.equals("aodl:notification") || qn.equals("notification")
				|| qn.equals("aodl:notification"))
		{
			_notiB = false;
			_aspect.setRangeList(_rangeList);
			_aspect.setNotification(_sb.toString().trim());
		}
		else if (name.equals("persName") || name.equals("aodl:persName") || qn.equals("persName")
				|| qn.equals("aodl:persName"))
		{
			_notiTextB = true;
			_persNameB = false;
		}
		// orgName
		else if (name.equals("orgName") || name.equals("aodl:orgName") || qn.equals("orgName")
				|| qn.equals("aodl:orgName"))
		{
			_orgNameB = false;
			_notiTextB = true;
		}
		// placeName
		else if (name.equals("placeName") || name.equals("aodl:placeName") || qn.equals("placeName")
				|| qn.equals("aodl:placeName"))
		{
			_placeNameB = false;
			_notiTextB = true;
		}
		// name
		else if (name.equals("name") || name.equals("aodl:name") || qn.equals("name") || qn.equals("aodl:name"))
		{
			_nameB = false;
			_notiTextB = true;
		}
		// date
		else if (name.equals("date") || name.equals("aodl:date") || qn.equals("date") || qn.equals("aodl:date"))
		{
			_dateB = false;
			_notiTextB = true;
		}
		// validation
		else if (name.equals("validation") || name.equals("aodl:validation") || qn.equals("validation")
				|| qn.equals("aodl:validation"))
		{
			_aspect.setValidation(_validation);
		}
		// validationStm
		else if (name.equals("validationStm") || name.equals("aodl:validationStm") || qn.equals("validationStm")
				|| qn.equals("aodl:validationStm"))
		{
			_validation.getValidationStms().add(_validationStm);
		}
		// reference
		else if (name.equals("reference") || name.equals("aodl:reference") || qn.equals("reference")
				|| qn.equals("aodl:reference"))
		{
			_referenceB = false;
			_validationStm.setReference(_reference);
		}

		else if (name.equals("result"))
		{
			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "AspectSaxHandler number of aspects: "
					+ _aspects.size());
			iLogger.log(_log);
			setResultObject(_aspect);
			_facade.getLoadedAspects().putAll(_aspects);

		}
		else if (name.equals("qAspect"))
		{
			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "AspectSaxHandler number of aspects: "
					+ _aspects.size());
			iLogger.log(_log);
			setResultObject(_aspects);
			_facade.getLoadedAspects().putAll(_aspects);

		}


	}

	/**
	 * Gets the result object.
	 * @return the result object
	 */
	public final Object getResultObject()
	{
		return _resultObject;
	}

	/**
	 * Sets the result object.
	 * @param resultObject the new result object
	 */
	private void setResultObject(final Object resultObject)
	{
		this._resultObject = resultObject;
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument()
	{
	}

	/**
	 * @param u uri
	 * @param name local name
	 * @param qn QName
	 * @param a attributes
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public final void startElement(final String u, final String name, final String qn, final Attributes a)
			throws SAXException
	{
		if (_monitor != null)
		{
			if (_pdrObjects.length < 25)
			{
				_monitor.worked(1);
			}
			else
			{
				if (_counter > _pdrObjects.length / 12)
				{
					_counter = 0;
					_monitor.worked(1);
				}
				_counter++;
			}
			if (_monitor.isCanceled())
			{
				_facade.getLoadedAspects().putAll(_aspects);

				if (_pdrObject != null)
				{
					_pdrObject.setAspectsLoaded(true);
					setResultObject(_pdrObject);
				}
				AEConstants.ILOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Parsing cancelled by user."));
				throw new SAXException("Parsing cancelled by user.");
			}
		}
		if (name.equals("aspect") || name.equals("aodl:aspect") || qn.equals("aspect") || qn.equals("aodl:aspect"))
		{

			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("id"))
				{
					_aspectID = new PdrId(a.getValue(i));
					_aspect = new Aspect(_aspectID);
				}
			}
		}
		else if (name.equals("record") || name.equals("aodl:record") || qn.equals("record") || qn.equals("aodl:record"))
		{
			_record = new Record();
		}
		else if (name.equals("revision") || name.equals("aodl:revision") || qn.equals("revision")
				|| qn.equals("aodl:revision"))
		{
			_revision = new Revision();
			for (int i = 0; i < a.getLength(); i++)
			{
				// System.out.println("Attribut: " + a.getQName(i)
				// + " Wert: " + a.getValue(i));
				if (a.getQName(i).equals("ref"))
				{
					_revision.setRef(Integer.valueOf(a.getValue(i)).intValue());
				}
				else if (a.getQName(i).equals("timestamp"))
				{
					try
					{
						_revision.setTimeStamp(_adminDataFormat.parse(a.getValue(i)));
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
				else if (a.getQName(i).equals("authority"))
				{
					_revision.setAuthority(new PdrId(a.getValue(i)));
				}
			}
		}
		else if (name.equals("timeDim") || name.equals("aodl:timeDim") || qn.equals("timeDim")
				|| qn.equals("aodl:timeDim"))
		{
			_timeDim = new TimeDim();
		}
		else if (name.equals("timeStm") || name.equals("aodl:timeStm") || qn.equals("timeStm")
				|| qn.equals("aodl:timeStm"))
		{
			_timeStm = new TimeStm();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_timeStm.setType(a.getValue(i));
				}
			}
		}
		else if (name.equals("time") || name.equals("aodl:time") || qn.equals("time") || qn.equals("aodl:time"))
		{
			_timeB = true;
			_time = new Time();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("accuracy"))
				{
					_time.setAccuracy(a.getValue(i));
				}
				else if (a.getQName(i).equals("type"))
				{
					_time.setType(a.getValue(i));
				}
			}
		}
		else if (name.equals("spatialDim") || name.equals("aodl:spatialDim") || qn.equals("spatialDim")
				|| qn.equals("aodl:spatialDim"))
		{
			_spatialDim = new SpatialDim();
		}
		else if (name.equals("spatialStm") || name.equals("aodl:spatialStm") || qn.equals("spatialStm")
				|| qn.equals("aodl:spatialStm"))
		{
			_spatialStm = new SpatialStm();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_spatialStm.setType(a.getValue(i));
				}
			}
		}
		else if (name.equals("place") || name.equals("aodl:place") || qn.equals("place") || qn.equals("aodl:place"))
		{
			_placeB = true;
			_place = new Place();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_place.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_place.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("key"))
				{
					_place.setKey(a.getValue(i));
				}
			}
		}
		else if (name.equals("relationDim") || name.equals("aodl:relationDim") || qn.equals("relationDim")
				|| qn.equals("aodl:relationDim"))
		{
			_relationDim = new RelationDim();
		}
		else if (name.equals("relationStm") || name.equals("aodl:relationStm") || qn.equals("relationStm")
				|| qn.equals("aodl:relationStm"))
		{
			_relationStm = new RelationStm();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("subject"))
				{
					PdrId id = new PdrId(a.getValue(i));
					_relationStm.setSubject(id);
					if (!id.equals(_aspect.getPdrId()))
					{
						PdrObject o = _facade.getPerson(id);
						if (o != null)
						{
							o.addAspectId(_aspectID);
						}
					}

				}

			}
		}
		else if (name.equals("relation") || name.equals("aodl:relation") || qn.equals("relation")
				|| qn.equals("aodl:relation"))
		{
			_relationB = true;
			_relation = new Relation();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("object"))
				{
					PdrId id = new PdrId(a.getValue(i));

					_relation.setObject(id);
					if (!id.equals(_aspect.getPdrId()))
					{
						PdrObject o = _facade.getPerson(id);
						if (o != null)
						{
							o.addAspectId(_aspectID);
						}
					}
				}
				else if (a.getQName(i).equals("provider"))
				{
					_relation.setProvider(a.getValue(i));
				}
				else if (a.getQName(i).equals("class"))
				{
					_relation.setRClass(a.getValue(i));

				}
				else if (a.getQName(i).equals("context"))
				{
					_relation.setContext(a.getValue(i));
				}
			}
		}
		else if (name.equals("semanticDim") || name.equals("aodl:semanticDim") || qn.equals("semanticDim")
				|| qn.equals("aodl:semanticDim"))
		{
			_semanticDim = new SemanticDim();
		}
		else if (name.equals("semanticStm") || name.equals("aodl:semanticStm") || qn.equals("semanticStm")
				|| qn.equals("aodl:semanticStm"))
		{
			_semanticStm = new SemanticStm();
			_semanticB = true;
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("provider"))
				{
					_semanticStm.setProvider(a.getValue(i));
				}
			}
		}
		else if (name.equals("notification") || name.equals("aodl:notification") || qn.equals("notification")
				|| qn.equals("aodl:notification"))
		{
			_notiB = true;
			_notiTextB = true;
			_sb = new StringBuilder(STRING_SIZE);
			_rangeList = new LinkedList<TaggingRange>();
		}
		else if (name.equals("lb") || name.equals("aodl:lb") || qn.equals("lb") || qn.equals("aodl:lb"))
		{
//			if (!lb_start)
//			{
//				_sb.append("\n");
//				lb_start = false;
//			} else
//			{
//				lb_start = false;
//			}
			_sb.append("\n");
			 _notiTextB = true;
		}
		else if (name.equals("persName") || name.equals("aodl:persName") || qn.equals("persName")
				|| qn.equals("aodl:persName"))
		{
			_persNameB = true;
			_notiTextB = false;
			_tr = new TaggingRange();
			_tr.setName("persName");
			_tr.setStart(_sb.length());
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_tr.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_tr.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("role"))
				{
					_tr.setRole(a.getValue(i));
				}
				else if (a.getQName(i).equals("ana"))
				{
					_tr.setAna(a.getValue(i));
				}
				else if (a.getQName(i).equals("key"))
				{
					_tr.setKey(a.getValue(i));
				}
			}
		}
		// orgName
		else if (name.equals("orgName") || name.equals("aodl:orgName") || qn.equals("orgName")
				|| qn.equals("aodl:orgName"))
		{
			_orgNameB = true;
			_notiTextB = false;
			_tr = new TaggingRange();
			_tr.setName("orgName");
			_tr.setStart(_sb.length());
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_tr.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_tr.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("role"))
				{
					_tr.setRole(a.getValue(i));
				}
				else if (a.getQName(i).equals("ana"))
				{
					_tr.setAna(a.getValue(i));
				}
				else if (a.getQName(i).equals("key"))
				{
					_tr.setKey(a.getValue(i));
				}
			}
		}
		// placeName
		else if (name.equals("placeName") || name.equals("aodl:placeName") || qn.equals("placeName")
				|| qn.equals("aodl:placeName"))
		{
			_placeNameB = true;
			_notiTextB = false;
			_tr = new TaggingRange();
			_tr.setName("placeName");
			_tr.setStart(_sb.length());
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_tr.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_tr.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("role"))
				{
					_tr.setRole(a.getValue(i));
				}
				else if (a.getQName(i).equals("ana"))
				{
					_tr.setAna(a.getValue(i));
				}
				else if (a.getQName(i).equals("key"))
				{
					_tr.setKey(a.getValue(i));
				}
			}
		}
		// name
		else if (name.equals("name") || name.equals("aodl:name") || qn.equals("name") || qn.equals("aodl:name"))
		{
			_nameB = true;
			_notiTextB = false;
			_tr = new TaggingRange();
			_tr.setName("name");
			_tr.setStart(_sb.length());
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_tr.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_tr.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("role"))
				{
					_tr.setRole(a.getValue(i));
				}
				else if (a.getQName(i).equals("ana"))
				{
					_tr.setAna(a.getValue(i));
				}
				else if (a.getQName(i).equals("key"))
				{
					_tr.setKey(a.getValue(i));
				}
			}
		}
		// date
		else if (name.equals("date") || name.equals("aodl:date") || qn.equals("date") || qn.equals("aodl:date"))
		{
			_dateB = true;
			_notiTextB = false;
			_tr = new TaggingRange();
			_tr.setName("date");
			_tr.setStart(_sb.length());
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					_tr.setType(a.getValue(i));
				}
				else if (a.getQName(i).equals("subtype"))
				{
					_tr.setSubtype(a.getValue(i));
				}
				else if (a.getQName(i).equals("role"))
				{
					_tr.setRole(a.getValue(i));
				}
				else if (a.getQName(i).equals("ana"))
				{
					_tr.setAna(a.getValue(i));
				}
				else if (a.getQName(i).equals("when"))
				{
					_tr.setWhen(new PdrDate(a.getValue(i)));
				}
				else if (a.getQName(i).equals("from"))
				{
					_tr.setFrom(new PdrDate(a.getValue(i)));
				}
				else if (a.getQName(i).equals("to"))
				{
					_tr.setTo(new PdrDate(a.getValue(i)));
				}
				else if (a.getQName(i).equals("notBefore"))
				{
					_tr.setNotBefore(new PdrDate(a.getValue(i)));
				}
				else if (a.getQName(i).equals("notAfter"))
				{
					_tr.setNotAfter(new PdrDate(a.getValue(i)));
				}
			}
		}
		// validation
		else if (name.equals("validation") || name.equals("aodl:validation") || qn.equals("validation")
				|| qn.equals("aodl:validation"))
		{
			_validation = new Validation();
		}
		// validationStm
		else if (name.equals("validationStm") || name.equals("aodl:validationStm") || qn.equals("validationStm")
				|| qn.equals("aodl:validationStm"))
		{
			_validationStm = new ValidationStm();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("authority"))
				{
					_validationStm.setAuthority(new PdrId(a.getValue(i)));
				}
			}
		}

		// reference
		else if (name.equals("reference") || name.equals("aodl:reference") || qn.equals("reference")
				|| qn.equals("aodl:reference"))
		{
			_referenceB = true;
			_reference = new Reference();
			for (int i = 0; i < a.getLength(); i++)
			{
				// System.out.println("Attribut: " + a.getQName(i)
				// + " Wert: " + a.getValue(i));
				if (a.getQName(i).equals("internal"))
				{
					_reference.setInternal(a.getValue(i));
				}
				else if (a.getQName(i).equals("quality"))
				{
					_reference.setQuality(a.getValue(i));
				}
			}
		}
		// interpretation
		else if (name.equals("interpretation") || name.equals("aodl:interpretation") || qn.equals("interpretation")
				|| qn.equals("aodl:interpretation"))
		{
			_valStmB = true;
		}
	}
}
