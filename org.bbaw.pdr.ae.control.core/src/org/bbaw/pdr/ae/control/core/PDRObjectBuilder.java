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
package org.bbaw.pdr.ae.control.core;

import java.util.LinkedList;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.IdentifierMods;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticDim;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.SpatialDim;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.eclipse.core.runtime.Platform;

public class PDRObjectBuilder
{
	private Facade _facade = Facade.getInstanz();

	private PDRObjectDisplayNameProcessor _displayNameProcessor = new PDRObjectDisplayNameProcessor();


	public Aspect buildCopyAspect(Aspect _originalAspect)
	{
		if (_originalAspect != null)
		{
			PdrId id = new PdrId("");
			try
			{
				id = _facade.getIdService().getNewId("pdrAo");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Aspect a = new Aspect(id);
			a.setNew(true);
			Revision revision = new Revision();
			revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
			revision.setTimeStamp(_facade.getCurrentDate());
			revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			a.setRecord(record);

			a.setSemanticDim(_originalAspect.getSemanticDim().clone());

			a.setRelationDim(_originalAspect.getRelationDim().clone());

			if (a.getRelationDim() != null && a.getRelationDim().getRelationStms() != null)
			{
				for (RelationStm rStm : a.getRelationDim().getRelationStms())
				{
					if (rStm.getSubject() != null && rStm.getSubject().equals(_originalAspect.getPdrId()))
					{
						rStm.setSubject(id);
					}
				}
			}

			a.setTimeDim(_originalAspect.getTimeDim().clone());
			a.setSpatialDim(_originalAspect.getSpatialDim().clone());
			a.setValidation(_originalAspect.getValidation().clone());
			a.setNotification(new String(_originalAspect.getNotification()));
			if (_originalAspect.getRangeList() != null)
			{
				a.setRangeList(new LinkedList<TaggingRange>());
				for (int i = 0; i < _originalAspect.getRangeList().size(); i++)
				{
					a.getRangeList().add(_originalAspect.getRangeList().get(i).clone());
				}
			}
			a.setNew(true);
			_displayNameProcessor.processDisplayName(a);
			return a;
		}
		return null;
	}

	public ReferenceMods buildCopyReference(ReferenceMods originalReference)
	{
		PdrId id = new PdrId("");
		try
		{
			id = _facade.getIdService().getNewId("pdrRo");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ReferenceMods ref = new ReferenceMods(id);
		ref.setNew(true);
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		revision.setRef(0);
		Record record = new Record();
		record.getRevisions().add(revision);
		ref.setRecord(record);
		if (originalReference.getAccessCondition() != null)
		{
			ref.setAccessCondition(originalReference.getAccessCondition().clone());
		}
		if (originalReference.getGenre() != null)
		{
			ref.setGenre(originalReference.getGenre().clone());
		}
		if (originalReference.getIdentifiersMods() != null)
		{
			ref.setIdentifiersMods(new Vector<IdentifierMods>(originalReference.getIdentifiersMods().size()));
			for (int i = 0; i < originalReference.getIdentifiersMods().size(); i++)
			{
				ref.getIdentifiersMods().add(originalReference.getIdentifiersMods().get(i).clone());
			}
		}
		if (originalReference.getLocation() != null)
		{
			ref.setLocation(originalReference.getLocation().clone());
		}
		if (originalReference.getNameMods() != null)
		{
			ref.setNameMods(new Vector<NameMods>(originalReference.getNameMods().size()));
			for (int i = 0; i < originalReference.getNameMods().size(); i++)
			{
				ref.getNameMods().add(originalReference.getNameMods().get(i).clone());
			}
		}
		if (originalReference.getNote() != null)
		{
			ref.setNote(originalReference.getNote().clone());
		}
		if (originalReference.getOriginInfo() != null)
		{
			ref.setOriginInfo(originalReference.getOriginInfo().clone());
		}
		if (originalReference.getSeriesTitleInfo() != null)
		{
			ref.setSeriesTitleInfo(originalReference.getSeriesTitleInfo().clone());
		}
		if (originalReference.getTitleInfo() != null)
		{
			ref.setTitleInfo(originalReference.getTitleInfo().clone());
		}
		if (originalReference.getDisplayNameLong() != null)
		{
			ref.setDisplayNameLong(new String(originalReference.getDisplayNameLong()));
		}
		if (originalReference.getHostedReferences() != null)
		{
			ref.setHostedReferences(new Vector<String>(originalReference.getHostedReferences().size()));
			for (int i = 0; i < originalReference.getHostedReferences().size(); i++)
			{
				ref.getHostedReferences().add(new String(originalReference.getHostedReferences().get(i)));
			}
		}
		if (originalReference.getRelatedItems() != null)
		{
			ref.setRelatedItems(new Vector<RelatedItem>(originalReference.getRelatedItems().size()));
			for (int i = 0; i < originalReference.getRelatedItems().size(); i++)
			{
				ref.getRelatedItems().add(originalReference.getRelatedItems().get(i).clone());
			}
		}

		return ref;

	}

	public Aspect buildNewAspect(PdrId idOfOwningObject, SemanticStm semanticStm)
	{
		PdrId id = new PdrId("");
		try
		{
			id = _facade.getIdService().getNewId("pdrAo");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Aspect a = new Aspect(id);
		a.setNew(true);
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		revision.setRef(0);
		Record record = new Record();
		record.getRevisions().add(revision);
		a.setRecord(record);

		a.setSemanticDim(new SemanticDim());

		if (semanticStm != null)
		{
			a.getSemanticDim().getSemanticStms().add(semanticStm);
		}
		else
		{
			String provider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
							AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
			SemanticStm sStm = new SemanticStm();
			sStm.setProvider(provider);
			a.getSemanticDim().getSemanticStms().add(sStm);
		}

		a.setRelationDim(new RelationDim());
		RelationStm rs = new RelationStm();
		rs.setSubject(a.getPdrId());
		Relation r = new Relation();
		r.setRelation("aspect_of"); //$NON-NLS-1$
		r.setProvider("PDR"); //$NON-NLS-1$
		r.setObject(idOfOwningObject);
		rs.setRelations(new Vector<Relation>(1));
		rs.getRelations().add(r);

		if (a.getTimeDim() == null)
		{
			a.setTimeDim(new TimeDim());
			a.getTimeDim().setTimeStms(new Vector<TimeStm>());

		}
		if (a.getTimeDim().getTimeStms().size() == 0)
		{
			TimeStm st = new TimeStm();
			st.setType("undefined"); //$NON-NLS-1$
			a.getTimeDim().getTimeStms().add(st);
		}
		if (a.getSpatialDim() == null)
		{
			a.setSpatialDim(new SpatialDim());
			a.getSpatialDim().setSpatialStms(new Vector<SpatialStm>());

		}
		if (a.getSpatialDim().getSpatialStms().size() == 0)
		{
			SpatialStm spS = new SpatialStm();
			spS.setType("undefined"); //$NON-NLS-1$
			a.getSpatialDim().getSpatialStms().add(spS);
		}

		a.getRelationDim().getRelationStms().add(rs);
		ValidationStm vStm = new ValidationStm();
		vStm.setReference(new Reference());
		a.setValidation(new Validation());
		a.getValidation().getValidationStms().add(vStm);
		
		if (Platform
			.getPreferencesService()
			.getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_VIEW_NOTIFICATION_TEMPLATE", AEConstants.ASPECT_VIEW_NOTIFICATION_TEMPLATE,
 null)
				&& semanticStm != null)
		{
			insertAspectTemplateText(a, semanticStm.getProvider(), semanticStm.getLabel());

		}
		return a;
	}

	private void insertAspectTemplateText(Aspect a, String provider, String label)
	{
		DatatypeDesc dtd = _facade.getConfigs().get(provider);
		if (dtd != null && dtd.getUsage().getTemplates() != null && dtd.getUsage().getTemplates().getChildren() != null)
		{
			ConfigData cd = dtd.getUsage().getTemplates().getChildren().get("aspectTemplates");
			if (cd != null && cd.getChildren() != null && cd.getChildren().containsKey("semanticTemplates")
					&& cd.getChildren().get("semanticTemplates").getChildren() != null)
			{
				SemanticTemplate semTempalte = (SemanticTemplate) cd.getChildren().get("semanticTemplates")
						.getChildren().get(label);
				if (semTempalte != null && !semTempalte.isIgnoreTemplateText())
				{
					a.setNotification(semTempalte.getTemplateText());
				}
			}
		}

	}

	public Person buildNewPerson()
	{
		PdrId id = new PdrId("");
		try
		{
			id = Facade.getInstanz().getIdService().getNewId("pdrPo");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Person p = new Person(id);
		Revision revision = new Revision();
		revision.setRevisor(_facade.getCurrentUser().getDisplayName());
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId());
		revision.setRef(0);
		Record record = new Record();
		record.getRevisions().add(revision);
		p.setRecord(record);
		p.setNew(true);

		return p;
	}

	public Aspect buildSimilarAspect(PdrId owningObject, Aspect originalAspect)
	{
		return buildSimilarAspect(owningObject, null, originalAspect);
	}

	public Aspect buildSimilarAspect(PdrId owningObject, PdrId obj, Aspect originalAspect)
	{
		String provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
		PdrId id = new PdrId("");
		try
		{
			id = _facade.getIdService().getNewId("pdrAo");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Aspect a = new Aspect(id);
		a.setNew(true);
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		revision.setRef(0);
		Record record = new Record();
		record.getRevisions().add(revision);
		a.setRecord(record);

		if (originalAspect != null)
		{
			a.setSemanticDim(originalAspect.getSemanticDim().clone());
			a.setValidation(originalAspect.getValidation().clone());
		}
		if (a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
		{
			for (SemanticStm sStm : a.getSemanticDim().getSemanticStms())
			{
				if (sStm.getLabel() != null && sStm.getProvider() != null
						&& (_facade.getPersonDisplayNameTags(sStm.getProvider()).contains(sStm.getLabel()) || sStm
								.getLabel().startsWith("NormName")))
				{
					String label = _facade.getPersonNameTags(provider).firstElement();
					if (label != null)
					{
						sStm.setLabel(label);
						break;
					}
				}
			}
		}

		if (owningObject != null)
		{
			a.setRelationDim(new RelationDim());
			RelationStm rs = new RelationStm();
			rs.setSubject(a.getPdrId());
			Relation r = new Relation();
			r.setRelation("aspect_of"); //$NON-NLS-1$
			r.setProvider("PDR"); //$NON-NLS-1$
			r.setObject(owningObject);
			rs.setRelations(new Vector<Relation>(1));
			rs.getRelations().add(r);
			a.getRelationDim().getRelationStms().add(rs);

			if (obj != null)
			{
				rs = new RelationStm();
				rs.setSubject(owningObject);
				r = new Relation();
				r.setObject(obj);
				rs.setRelations(new Vector<Relation>(1));
				rs.getRelations().add(r);
				a.getRelationDim().getRelationStms().add(rs);
			}
		}

		if (a.getTimeDim() == null)
		{
			a.setTimeDim(new TimeDim());
			a.getTimeDim().setTimeStms(new Vector<TimeStm>());

		}
		if (a.getTimeDim().getTimeStms().size() == 0)
		{
			TimeStm st = new TimeStm();
			st.setType("undefined"); //$NON-NLS-1$
			a.getTimeDim().getTimeStms().add(st);
		}
		if (a.getSpatialDim() == null)
		{
			a.setSpatialDim(new SpatialDim());
			a.getSpatialDim().setSpatialStms(new Vector<SpatialStm>());

		}
		if (a.getSpatialDim().getSpatialStms().size() == 0)
		{
			SpatialStm spS = new SpatialStm();
			spS.setType("undefined"); //$NON-NLS-1$
			a.getSpatialDim().getSpatialStms().add(spS);
		}
		if (a.getSemanticDim() == null)
		{
			a.setSemanticDim(new SemanticDim());
		}
		if (a.getSemanticDim().getSemanticStms().size() == 0)
		{
			SemanticStm sts = new SemanticStm();
			sts.setProvider(provider);
			a.getSemanticDim().getSemanticStms().add(sts);
		}
		a.setNew(true);

		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_VIEW_NOTIFICATION_TEMPLATE",
				AEConstants.ASPECT_VIEW_NOTIFICATION_TEMPLATE, null))
		{

			if (a.getSemanticDim().getSemanticLabelByProvider(provider) != null
					&& a.getSemanticDim().getSemanticLabelByProvider(provider).size() > 0)
			insertAspectTemplateText(a, provider, a.getSemanticDim().getSemanticLabelByProvider(provider)
					.firstElement());
		}
		_displayNameProcessor.processDisplayName(a);
		return a;

	}
}
